/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.order.impl.MplDefaultCalculationService;
import com.tisl.mpl.util.DiscountUtility;


/**
 * @author TCS
 *
 */
public class MplVoucherServiceImpl implements MplVoucherService
{
	private static final Logger LOG = Logger.getLogger(MplVoucherServiceImpl.class);

	@Resource(name = "calculationService")
	private MplDefaultCalculationService mplDefaultCalculationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
	@Resource(name = "commerceCartService")
	private MplCommerceCartService mplCommerceCartService;
	@Resource(name = "voucherService")
	private VoucherService voucherService;
	@Resource(name = "commerceCartCalculationStrategy")
	private MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;



	/**
	 * @Description This method recalculates the cart after redeeming/releasing the voucher *
	 * @param cartModel
	 *
	 */
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		try
		{
			LOG.debug("Step 8:::Inside recalculating cart after voucher apply");
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			if (cartModel != null && cartModel.getEntries() != null)
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{
					if (null != cartEntryModel.getGiveAway() && !cartEntryModel.getGiveAway().booleanValue())
					{
						freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
						freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
					}
				}
			}

			//recalculating cart
			final Double deliveryCost = cartModel.getDeliveryCost();

			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			getMplCommerceCartCalculationStrategy().recalculateCart(parameter);

			cartModel.setDeliveryCost(deliveryCost);
			cartModel.setTotalPrice(Double.valueOf((null != cartModel.getTotalPrice() ? cartModel.getTotalPrice().doubleValue()
					: 0.0d) + (null != deliveryCost ? deliveryCost.doubleValue() : 0.0d)));

			// Freebie item changes
			getMplCommerceCartService().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

			LOG.debug("Step 9:::Recalculation done successfully");

			getModelService().save(cartModel);
		}
		catch (final IllegalStateException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0012);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}

	}




	/**
	 * @Description Checks the cart after applying the voucher
	 * @param lastVoucher
	 * @param cartModel
	 * @throws EtailNonBusinessExceptions
	 * @throws ModelSavingException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@Override
	public VoucherDiscountData checkCartAfterApply(final VoucherModel lastVoucher, final CartModel cartModel,
			final List<AbstractOrderEntryModel> applicableOrderEntryList) throws VoucherOperationException,
			EtailNonBusinessExceptions
	{
		VoucherDiscountData discountData = new VoucherDiscountData();
		try
		{
			LOG.debug("Step 11:::Inside checking cart after applying voucher");
			//Total amount in cart updated with delay... Calculating value of voucher regarding to order
			final double cartSubTotal = cartModel.getSubtotal().doubleValue();
			double voucherCalcValue = 0.0;
			double promoCalcValue = 0.0;
			List<DiscountValue> discountList = cartModel.getGlobalDiscountValues(); //Discount values against the cart
			String voucherCode = null;
			if (lastVoucher instanceof PromotionVoucherModel)
			{
				voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
			}

			final List<DiscountModel> voucherList = cartModel.getDiscounts(); //List of discounts against the cart
			if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
			{
				for (final DiscountValue discount : discountList)
				{
					if (null != discount.getCode() && null != voucherList.get(0).getCode()
							&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
					{
						voucherCalcValue = discount.getValue();
					}
					else
					{
						promoCalcValue = discount.getValue();
					}
				}
			}

			final StringBuilder logBuilder = new StringBuilder();
			LOG.debug(logBuilder.append("Step 12:::Voucher discount in cart is ").append(voucherCalcValue)
					.append(" & promo discount in cart is ").append(promoCalcValue));

			if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
					&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
			{
				LOG.debug("Step 13:::Inside max discount block");
				discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
						.getMaxDiscountValue().doubleValue());
				cartModel.setGlobalDiscountValues(discountList);
				getMplDefaultCalculationService().calculateTotals(cartModel, false);
				getModelService().save(cartModel);

				discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel,
						Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
			}
			else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
			{
				LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
				discountData = releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
			}
			else
			//In other cases
			{
				double netAmountAfterAllDisc = 0.0D;
				double productPrice = 0.0D;
				int size = 0;

				if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
				{
					LOG.debug("Step 15:::applicableOrderEntryList is not empty");
					for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
					{
						size += (null != entry.getQuantity()) ? entry.getQuantity().intValue() : 0; //Size in total count of all the order entries present in cart
					}
					final double cartTotalThreshold = 0.01 * size; //Threshold is min value which is allowable after applying coupon
					for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
					{
						netAmountAfterAllDisc += ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry
								.getProductPromoCode())) || (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry
								.getCartPromoCode()))) ? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice()
								.doubleValue();

						productPrice += (null != entry.getTotalPrice()) ? entry.getTotalPrice().doubleValue() : 0.0d;
					}

					LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
							.append(" & productPrice is ").append(productPrice));

					if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
					{
						LOG.debug("Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
						discountData = releaseVoucherAfterCheck(cartModel, voucherCode, Double.valueOf(productPrice),
								applicableOrderEntryList, voucherList);
					}
					else if (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) > 0
							&& (netAmountAfterAllDisc - voucherCalcValue) < cartTotalThreshold) //When discount value is less than .01*count of applicable entries
					{
						LOG.debug("Step 16.1:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) >= 0 < 0.01 block");
						discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher,
								(netAmountAfterAllDisc - cartTotalThreshold));

						cartModel.setGlobalDiscountValues(discountList);
						getMplDefaultCalculationService().calculateTotals(cartModel, false);
						getModelService().save(cartModel);
					}
					else
					//In other cases, just set the coupon discount for the discount data
					{
						discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
					}
				}
				else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries list is empty
				{
					LOG.debug("Step 17:::applicable entries empty");
					discountData = releaseVoucherAfterCheck(cartModel, voucherCode, null, applicableOrderEntryList, voucherList);
				}
				else
				//In other cases, just set the coupon discount for the discount data
				{
					discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
				}
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final CalculationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0017);
		}
		catch (final NumberFormatException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0015);
		}
		return discountData;
	}

	/**
	 * @Description This is for releasing voucher
	 * @param cartModel
	 * @param voucherCode
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 */
	private VoucherDiscountData releaseVoucherAfterCheck(final CartModel cartModel, final String voucherCode,
			final Double productPrice, final List<AbstractOrderEntryModel> applicableOrderEntryList,
			final List<DiscountModel> voucherList) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		final VoucherDiscountData discountData = new VoucherDiscountData();
		try
		{

			releaseVoucher(voucherCode, cartModel); //Releases voucher
			recalculateCartForCoupon(cartModel); //Recalculates cart after releasing voucher
			getModelService().save(cartModel);

			String msg = null;

			discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(0)));
			if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
			{
				msg = MarketplacecommerceservicesConstants.NOTAPPLICABLE;
			}
			else if (null != productPrice && productPrice.doubleValue() < 1)
			{
				msg = MarketplacecommerceservicesConstants.EXCFREEBIE;
			}
			else
			{
				msg = MarketplacecommerceservicesConstants.PRICEEXCEEDED;
			}

			discountData.setRedeemErrorMsg(msg);

		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		return discountData;
	}



	/**
	 * @Description: For getting list of applicable AbstractOrderEntry from voucherEntrySet
	 * @param voucherModel
	 * @param cartModel
	 * @return list of applicable AbstractOrderEntry
	 */
	@Override
	public List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherModel voucherModel, final CartModel cartModel)
	{
		LOG.debug("Step 9:::Inside getOrderEntriesFromVoucherEntries");
		final VoucherEntrySet voucherEntrySet = getVoucherModelService().getApplicableEntries(voucherModel, cartModel);
		final List<AbstractOrderEntry> applicableOrderList = new ArrayList<AbstractOrderEntry>();

		for (final Object voucherEntry : voucherEntrySet)
		{
			applicableOrderList.add(((VoucherEntry) voucherEntry).getOrderEntry());
		}

		return applicableOrderList;
	}


	/**
	 * @Description This method adds a new global discount and removes the old global discount
	 * @param discountList
	 * @param voucherList
	 * @param cartSubTotal
	 * @param promoCalcValue
	 * @param lastVoucher
	 * @param discountAmt
	 * @return List<DiscountValue>
	 */
	@Override
	public List<DiscountValue> setGlobalDiscount(final List<DiscountValue> discountList, final List<DiscountModel> voucherList,
			final double cartSubTotal, final double promoCalcValue, final VoucherModel lastVoucher, final double discountAmt)
	{
		DiscountValue discountValue = null;

		final Iterator iter = discountList.iterator();

		//Remove the existing discount and add a new discount row
		while (iter.hasNext())
		{
			final DiscountValue discount = (DiscountValue) iter.next();
			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());

				iter.remove();
				break;
			}
		}

		discountList.add(discountValue);
		return discountList;
	}


	/**
	 * @Description This is for releasing voucher
	 * @param cartModel
	 * @param voucherCode
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException
	{
		try
		{
			LOG.debug("Step 2:::Inside releaseVoucher");
			validateVoucherCodeParameter(voucherCode);
			final VoucherModel voucher = getVoucherService().getVoucher(voucherCode); //Finds voucher for the selected code
			if (voucher == null)
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
			}
			else if (cartModel != null)
			{
				LOG.debug("Step 3:::Voucher and cart is not null");

				getVoucherService().releaseVoucher(voucherCode, cartModel); //Releases the voucher from the cart
				LOG.debug("Step 4:::Voucher released");
				final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(voucher, cartModel);//new ArrayList<AbstractOrderEntryModel>();
				for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
				{
					entry.setCouponCode("");
					entry.setCouponValue(Double.valueOf(0.00D));
				}
				if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
				{
					getModelService().saveAll(entryList);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
			}
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
	}


	/**
	 * Validates the voucher code parameter
	 *
	 * @param voucherCode
	 */
	protected void validateVoucherCodeParameter(final String voucherCode)
	{
		if (StringUtils.isBlank(voucherCode))
		{
			throw new IllegalArgumentException("Parameter voucherCode must not be empty");
		}
	}


	/**
	 * @Description For getting list of applicable AbstractOrderEntryModel from voucherEntrySet
	 * @param voucherModel
	 * @param cartModel
	 * @return public List<AbstractOrderEntryModel>
	 */
	@Override
	public List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final CartModel cartModel)
	{
		LOG.debug("Step 10:::Inside getOrderEntryModelFromVouEntries");

		final List<AbstractOrderEntryModel> applicableOrderList = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntry entry : getOrderEntriesFromVoucherEntries(voucherModel, cartModel)) //Converts applicable order entries from AbstractOrderEntry to AbstractOrderEntryModel
		{
			applicableOrderList.add((AbstractOrderEntryModel) getModelService().get(entry));
		}

		return applicableOrderList;
	}



	/**
	 * @Description: For Apportioning of vouchers
	 * @param voucher
	 * @param cartModel
	 * @param voucherCode
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void setApportionedValueForVoucher(final VoucherModel voucher, final CartModel cartModel, final String voucherCode,
			final List<AbstractOrderEntryModel> applicableOrderEntryList) throws EtailNonBusinessExceptions
	{
		try
		{
			LOG.debug("Step 18:::Inside setApportionedValueForVoucher");
			if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()))
			{
				double totalApplicablePrice = 0.0D;
				final BigDecimal percentageDiscount = null;
				updateAppOrderEntriesForFreebieBogo(applicableOrderEntryList);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					totalApplicablePrice += entry.getTotalPrice() != null ? entry.getTotalPrice().doubleValue() : 0.0D;
				}

				final List<DiscountValue> discountList = cartModel.getGlobalDiscountValues(); //Discount values against the cart
				final List<DiscountModel> voucherList = cartModel.getDiscounts(); //List of discounts against the cart
				double discountValue = 0.0;
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && null != voucherList.get(0).getCode()
								&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
						{
							discountValue = discount.getValue();
						}
					}
				}

				final double formattedDiscVal = Math.round(discountValue * 100.0) / 100.0;

				LOG.debug("Step 19:::percentageDiscount is " + percentageDiscount);

				BigDecimal totalAmtDeductedOnItemLevel = BigDecimal.valueOf(0);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					BigDecimal entryLevelApportionedPrice = null;
					double currNetAmtAftrAllDisc = 0.00D;

					final double entryTotalPrice = entry.getTotalPrice().doubleValue();
					final BigDecimal discountPriceValue = BigDecimal.valueOf(formattedDiscVal);

					if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
					{
						entryLevelApportionedPrice = getApportionedValueForSingleEntry(discountPriceValue, totalAmtDeductedOnItemLevel);
					}
					else
					{
						entryLevelApportionedPrice = getApportionedValueForEntry(discountPriceValue, entryTotalPrice,
								totalApplicablePrice);
						totalAmtDeductedOnItemLevel = totalAmtDeductedOnItemLevel.add(entryLevelApportionedPrice);
					}

					LOG.debug("Step 20:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

					entry.setCouponCode(null != voucherCode ? voucherCode : voucher.getCode());
					entry.setCouponValue(Double.valueOf(entryLevelApportionedPrice.doubleValue()));

					if ((null != entry.getProductPromoCode() && !entry.getProductPromoCode().isEmpty())
							|| (null != entry.getCartPromoCode() && !entry.getCartPromoCode().isEmpty()))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() != null ? entry.getNetAmountAfterAllDisc()
								.doubleValue() : 0.00D;
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(netAmtAftrAllDisc, entryLevelApportionedPrice);
					}
					else
					{
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(entryTotalPrice, entryLevelApportionedPrice);
					}

					LOG.debug("Step 21:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);

					entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
				}

				getModelService().saveAll(applicableOrderEntryList);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
	}



	/**
	 * @Descriotion This is for getting apportioned value for a single entry
	 * @param discountPriceValue
	 * @param totalAmtDeductedOnItemLevel
	 * @return BigDecimal
	 */

	private BigDecimal getApportionedValueForSingleEntry(final BigDecimal discountPriceValue,
			final BigDecimal totalAmtDeductedOnItemLevel)
	{
		try
		{
			return discountPriceValue.subtract(totalAmtDeductedOnItemLevel);
		}
		catch (final ArithmeticException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0019);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Descriotion This is for getting apportioned value for any entry except the last one
	 * @param discountPriceValue
	 * @param totalApplicablePrice
	 * @param entryTotalPrice
	 * @return BigDecimal
	 */
	private BigDecimal getApportionedValueForEntry(final BigDecimal discountPriceValue, final double entryTotalPrice,
			final double totalApplicablePrice)
	{
		try
		{
			return discountPriceValue.multiply(BigDecimal.valueOf(entryTotalPrice / totalApplicablePrice));
		}
		catch (final ArithmeticException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0019);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Descriotion This is for getting apportioned value for entry
	 * @param amount
	 * @param entryLevelApportionedPrice
	 * @return BigDecimal
	 */
	private double getCurrNetAmtAftrAllDisc(final double amount, final BigDecimal entryLevelApportionedPrice)
	{
		return (amount > entryLevelApportionedPrice.doubleValue()) ? (amount - entryLevelApportionedPrice.doubleValue()) : Double
				.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
	}



	/**
	 * This method removes the freebie items from voucher applicable entries
	 *
	 * @param applicableOrderEntryList
	 */
	private void updateAppOrderEntriesForFreebieBogo(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		final Iterator iterator = applicableOrderEntryList.iterator();
		while (iterator.hasNext())
		{
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) iterator.next();
			if (null != entry.getTotalPrice() && entry.getTotalPrice().doubleValue() < 1)
			{
				iterator.remove();
			}
		}
	}



	/**
	 * @return the mplDefaultCalculationService
	 */
	public MplDefaultCalculationService getMplDefaultCalculationService()
	{
		return mplDefaultCalculationService;
	}




	/**
	 * @param mplDefaultCalculationService
	 *           the mplDefaultCalculationService to set
	 */
	public void setMplDefaultCalculationService(final MplDefaultCalculationService mplDefaultCalculationService)
	{
		this.mplDefaultCalculationService = mplDefaultCalculationService;
	}




	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}




	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}




	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}


	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}




	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}




	/**
	 * @return the mplCommerceCartCalculationStrategy
	 */
	public MplCommerceCartCalculationStrategy getMplCommerceCartCalculationStrategy()
	{
		return mplCommerceCartCalculationStrategy;
	}




	/**
	 * @param mplCommerceCartCalculationStrategy
	 *           the mplCommerceCartCalculationStrategy to set
	 */
	public void setMplCommerceCartCalculationStrategy(final MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy)
	{
		this.mplCommerceCartCalculationStrategy = mplCommerceCartCalculationStrategy;
	}




	/**
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}




	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
	}




	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}




	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}




}
