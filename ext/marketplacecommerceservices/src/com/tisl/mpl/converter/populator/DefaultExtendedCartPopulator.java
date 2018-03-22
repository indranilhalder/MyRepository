/**
 *
 */
package com.tisl.mpl.converter.populator;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.OrderPromotion;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.DiscountValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;

import net.sourceforge.pmd.util.StringUtil;


/**
 * @author TCS
 *
 */
public class DefaultExtendedCartPopulator extends CartPopulator
{

	private Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> mplDeliveryModeConverter;
	private static final Logger LOG = Logger.getLogger(DefaultExtendedCartPopulator.class);

	private PromotionsService promotionsService;

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		try
		{
			if (source != null)
			{
				final Double deliveryCost = source.getDeliveryCost();
				addOveriddenPromoDetails(source, target);
				addDeliveryAddress(source, target);
				addPaymentInformation(source, target);
				addMplDeliveryMethod(source, target);
				//TPR-5346
				addMaxCountMethod(source, target);

				/* TPR-928 */
				final DecimalFormat formatter = new DecimalFormat("0.00");
				//Defect-Fix ProductLevelDiscounts were Not Considered
				if (target != null && (target.getOrderDiscounts().getDoubleValue().doubleValue() > 0.0
						|| (target.getProductDiscounts() != null && target.getProductDiscounts().getDoubleValue().doubleValue() > 0.0)))
				{

					final String formate = formatter.format(100 * ((target.getOrderDiscounts().getDoubleValue().doubleValue()
							+ target.getProductDiscounts().getDoubleValue().doubleValue())
							/ (target.getSubTotal().getDoubleValue().doubleValue())));

					target.setDiscountPercentage(formate);

				}
				final CurrencyModel currency = source.getCurrency();
				if (null != currency)
				{
					target.setCurrencySymbol(currency.getSymbol());
				}
				target.setPickupPersonName(source.getPickupPersonName());
				target.setPickupPersonMobile(source.getPickupPersonMobile());


				/*
				 * else if (target != null) {
				 *
				 * final String formate = formatter.format(100 * (target.getTotalDiscounts().getDoubleValue().doubleValue()
				 * / (target .getSubTotal().getDoubleValue().doubleValue()))); target.setDiscountPercentage(formate); }
				 */

				/* TPR-928 */
				if (deliveryCost != null)
				{
					target.setDeliveryCost(createPrice(source, deliveryCost));
				}
				if (CollectionUtils.isNotEmpty(source.getAllPromotionResults()))
				{
					final Set<PromotionResultModel> eligiblePromoList = source.getAllPromotionResults();
					//boolean isShippingPromoApplied = false;
					for (final PromotionResultModel promotionResultModel : eligiblePromoList)
					{

						final AbstractPromotionModel promotion = promotionResultModel.getPromotion();

						if (promotionResultModel.getCertainty().floatValue() == 1.0F
								&& (promotion instanceof BuyAGetPromotionOnShippingChargesModel
										|| promotion instanceof BuyAandBGetPromotionOnShippingChargesModel
										|| promotion instanceof BuyAboveXGetPromotionOnShippingChargesModel))
						{
							//isShippingPromoApplied = true;
							break;
						}
					}

					//	if (isShippingPromoApplied)
					//	{
					//		addDeliveryModePromotion(source, target); commented as shipping promotion is leveraged for adding charge
					//	}

				}
				if (null != source.getConvenienceCharges())
				{
					target.setConvenienceChargeForCOD(createPrice(source, source.getConvenienceCharges()));
				}
				else
				{
					target.setConvenienceChargeForCOD(createPrice(source, Double.valueOf(0.0d)));
				}
				if (null != source.getTotalPriceWithConv())
				{
					target.setTotalPriceWithConvCharge(createPrice(source, source.getTotalPriceWithConv()));
				}
				if (null != source.getTotalDiscounts())
				{
					if (source.getStore() != null && StringUtil.isNotEmpty(source.getStore().getUid()))
					{
						if (MarketplacecommerceservicesConstants.LUXURY_PREFIX.equalsIgnoreCase(source.getStore().getUid()))
						{

							final double productsDiscountsAmount = getProductsDiscountsAmount(source);
							final double orderDiscountsAmount = getOrderDiscountAmount(source);

							target.setTotalDiscounts(
									createPrice(source, Double.valueOf(productsDiscountsAmount + orderDiscountsAmount)));
						}
					}
				}
				else
				{
					target.setTotalPriceWithConvCharge(createPrice(source, Double.valueOf(0.0d)));
				}
				//TPR-174
				if (null != source.getMerged())
				{
					target.setGotMerged(source.getMerged().booleanValue());
				}
				if (null != source.getExchangeAppliedCart())
				{
					target.setExchangeAppliedCart(source.getExchangeAppliedCart());
				}
				else
				{
					target.setExchangeAppliedCart(Boolean.FALSE);
				}
			}
			else
			{
				LOG.error(">> Cartmodel source is null");
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("Exception in DefaultExtendedCartPopulator due to ", ex);
		}
	}

	protected double getOrderDiscountAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final List<DiscountModel> discountList = source.getDiscounts(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountModel discount : discountList)
			{
				final Double value = discount.getValue();
				if (!discount.getItemtype().contains("Voucher"))
				{
					if (value.doubleValue() > 0.0d)
					{
						discounts += value.doubleValue();
					}
				}
			}
		}

		return discounts;
	}

	/**
	 * The Method is used to auto populate the Potential Promotions Details based on 'certainity'
	 *
	 * @param source
	 * @param target
	 */
	private void addOveriddenPromoDetails(final CartModel source, final CartData target)
	{
		final PromotionOrderResults promoOrderResults = getPromotionsService().getPromotionResults(source);
		final List<PromotionResult> eligiblePromoList = new ArrayList<PromotionResult>();
		List<PromotionResult> cartPromoList = null;
		List<PromotionResult> productPromoList = null;

		if (null != promoOrderResults && null != target)
		{
			if (null != promoOrderResults.getPotentialOrderPromotions()
					&& !promoOrderResults.getPotentialOrderPromotions().isEmpty())
			{
				cartPromoList = new ArrayList<PromotionResult>(promoOrderResults.getPotentialOrderPromotions());
				eligiblePromoList.addAll(cartPromoList);
			}

			if (null != promoOrderResults.getPotentialProductPromotions()
					&& !promoOrderResults.getPotentialProductPromotions().isEmpty())
			{
				productPromoList = new ArrayList<PromotionResult>(promoOrderResults.getPotentialProductPromotions());
				eligiblePromoList.addAll(productPromoList);
			}

			if (CollectionUtils.isNotEmpty(eligiblePromoList))
			{
				populateCartData(eligiblePromoList, target);
			}
		}
	}


	/**
	 * The Method is used to split Promotion Results on type and populate the cart data details
	 *
	 * @param eligiblePromoList
	 * @param target
	 */
	private void populateCartData(final List<PromotionResult> eligiblePromoList, final CartData target)
	{
		final List<PromotionResult> cartPromoList = new ArrayList<PromotionResult>();
		final List<PromotionResult> productPromoList = new ArrayList<PromotionResult>();
		final List<PromotionResult> validPromoList = new ArrayList<PromotionResult>();

		for (final PromotionResult result : eligiblePromoList)
		{
			if (result.getPromotion() instanceof ProductPromotion)
			{
				productPromoList.add(result);
			}
			else if (result.getPromotion() instanceof OrderPromotion)
			{
				cartPromoList.add(result);
			}
		}


		if (CollectionUtils.isNotEmpty(productPromoList))
		{
			validPromoList.add(productPromoList.get(0));
			target.setPotentialProductPromotions(getPromotions(productPromoList));
		}

		if (CollectionUtils.isNotEmpty(cartPromoList))
		{
			validPromoList.add(checkHighestCertainity(cartPromoList));
		}

		if (CollectionUtils.isNotEmpty(validPromoList))
		{
			target.setPotentialOrderPromotions(getPromotions(validPromoList));
		}
	}

	/**
	 * The Method is used to return Promotion Result based on certainity
	 *
	 * @param promoList
	 * @return PromotionResult
	 */
	private PromotionResult checkHighestCertainity(final List<PromotionResult> promoList)
	{
		Collections.sort(promoList, new Comparator<PromotionResult>()
		{
			@Override
			public int compare(final PromotionResult promo, final PromotionResult comparePromo)
			{
				int certainity = 0;
				try
				{
					if (null != promo.getAttribute(MarketplacecommerceservicesConstants.CERTAINTY)
							&& null != comparePromo.getAttribute(MarketplacecommerceservicesConstants.CERTAINTY))
					{
						certainity = ((Float) promo.getAttribute(MarketplacecommerceservicesConstants.CERTAINTY))
								.compareTo((Float) comparePromo.getAttribute(MarketplacecommerceservicesConstants.CERTAINTY));
					}
				}
				catch (JaloInvalidParameterException | JaloSecurityException e)
				{
					LOG.error(e.getMessage());
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage());
				}
				return certainity;
			}
		});
		Collections.reverse(promoList);
		return promoList.get(0);
	}

	/**
	 * @param source
	 * @param target
	 */
	private void addMplDeliveryMethod(final CartModel source, final CartData target)
	{
		boolean isMplDeliveryModeNotSet = true;

		for (final OrderEntryData targetEntry : target.getEntries())
		{
			if (targetEntry.getMplDeliveryMode() != null && targetEntry.getMplDeliveryMode().getCode() != null
					&& targetEntry.getMplDeliveryMode().getDeliveryCost() != null
					&& targetEntry.getMplDeliveryMode().getSellerArticleSKU() != null)
			{
				isMplDeliveryModeNotSet = false;
				break;
			}
		}

		if (isMplDeliveryModeNotSet)
		{
			for (final AbstractOrderEntryModel entry : source.getEntries())
			{
				if (entry.getMplDeliveryMode() != null)
				{
					for (final OrderEntryData targetEntry : target.getEntries())
					{
						if (entry.getMplDeliveryMode() != null)
						{
							targetEntry.setMplDeliveryMode(getMplDeliveryModeConverter().convert(entry.getMplDeliveryMode()));
						}
					}
				}
			}
		}

	}

	// TISSIT-1786
	@Override
	protected double getOrderDiscountsAmount(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getValue();
				if (value > 0.0d)
				{
					discounts += value;
				}
			}
		}

		return discounts;

	}

	//	private void addDeliveryModePromotion(final CartModel source, final CartData target)
	//	{
	//		Double discountedPrice = Double.valueOf(target.getTotalDiscounts().getValue().doubleValue());
	//		boolean deliveryCostPromotionApplied = false;
	//
	//		for (final AbstractOrderEntryModel entryModel : source.getEntries())
	//		{
	//			for (final OrderEntryData entryData : target.getEntries())
	//			{
	//				if (entryModel.getSelectedUSSID() != null && entryData.getSelectedUssid() != null
	//						&& entryModel.getCurrDelCharge() != null && entryModel.getPrevDelCharge() != null
	//						&& entryModel.getSelectedUSSID().equalsIgnoreCase(entryData.getSelectedUssid()))
	//				{
	//					final Double prevDeliveryCost = entryModel.getPrevDelCharge();
	//					final Double currDeliveryCost = entryModel.getCurrDelCharge();
	//					final Double deliveryCostDisc = Double.valueOf(prevDeliveryCost.doubleValue() - currDeliveryCost.doubleValue());
	//
	//					if (deliveryCostDisc.doubleValue() > 0)
	//					{
	//						discountedPrice = Double.valueOf(discountedPrice.doubleValue() + deliveryCostDisc.doubleValue());
	//						deliveryCostPromotionApplied = true;
	//					}
	//				}
	//			}
	//		}
	//
	//		if (discountedPrice.doubleValue() > 0 && deliveryCostPromotionApplied)
	//		{
	//			final BigDecimal totalDiscount = new BigDecimal(discountedPrice.doubleValue());
	//			final PriceData cartTotalDiscount = getPriceDataFactory().create(PriceDataType.BUY, totalDiscount,
	//					MarketplacecommerceservicesConstants.INR);
	//			target.setTotalDiscounts(cartTotalDiscount);
	//		}
	//
	//	}

	//TPR-5346

	/**
	 * @param source
	 * @param target
	 */
	private void addMaxCountMethod(final CartModel source, final CartData target)
	{

		for (final AbstractOrderEntryModel entry : source.getEntries())
		{
			for (final OrderEntryData targetEntry : target.getEntries())
			{
				targetEntry.setMaxCountReached(entry.isMaxCountReached());
			}
		}
	}

	/**
	 * @return the mplDeliveryModeConverter
	 */
	public Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> getMplDeliveryModeConverter()
	{
		return mplDeliveryModeConverter;
	}

	/**
	 * @param mplDeliveryModeConverter
	 *           the mplDeliveryModeConverter to set
	 */
	public void setMplDeliveryModeConverter(
			final Converter<MplZoneDeliveryModeValueModel, MarketplaceDeliveryModeData> mplDeliveryModeConverter)
	{
		this.mplDeliveryModeConverter = mplDeliveryModeConverter;
	}

	/**
	 * @return the promotionsService
	 */
	@Override
	public PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	/**
	 * @param promotionsService
	 *           the promotionsService to set
	 */
	@Override
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

}
