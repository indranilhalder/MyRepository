/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.FiredPromoData;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.data.PotentialPromoData;
import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;
import com.tisl.mpl.model.BuyAGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.BuyAandBGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAandBPrecentageDiscountModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountPromotionModel;
import com.tisl.mpl.model.CustomProductBOGOFPromotionModel;


/**
 * @author TCS
 *
 */
public class DiscountUtility
{

	@Autowired
	private PriceDataFactory priceDataFactory;

	/**
	 * @Description : For Filtering Product Promotions
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	public MplPromotionData populateData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		MplPromotionData promoData = new MplPromotionData();
		if (productPromotion instanceof BuyAPercentageDiscountModel)
		{
			promoData = getBuyAPercentageDiscountData(productPromotion, cart);

		}
		else if (productPromotion instanceof BuyAandBPrecentageDiscountModel)
		{
			promoData = getBuyAandBPrecentageDiscountData(productPromotion, cart);
		}
		else if (productPromotion instanceof CustomProductBOGOFPromotionModel)
		{
			promoData = getBOGOData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			promoData = getBuyACashBackData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			promoData = getBuyAandBCashBackData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAAboveXGetPercentageOrAmountOffModel)
		{
			promoData = getBuyAAboveXData(productPromotion, cart);
		}
		return promoData;
	}





	/**
	 * @Description : For Filtering Cart Promotions
	 * @param orderPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	public MplPromotionData populateCartPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		MplPromotionData promoData = new MplPromotionData();
		if (orderPromotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			promoData = getCartDiscountPromoData(orderPromotion, cart);
		}

		return promoData;
	}


	/**
	 * @Description : Populate Potential Product Promotion Data
	 * @param productPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	public MplPromotionData populatePotentialPromoData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		MplPromotionData promoData = null;

		if (productPromotion instanceof BuyAPercentageDiscountModel)
		{
			promoData = new MplPromotionData();
			final BuyAPercentageDiscountModel oModel = (BuyAPercentageDiscountModel) productPromotion;
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAandBPrecentageDiscountModel)
		{
			final BuyAandBPrecentageDiscountModel oModel = (BuyAandBPrecentageDiscountModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof CustomProductBOGOFPromotionModel)
		{
			final CustomProductBOGOFPromotionModel oModel = (CustomProductBOGOFPromotionModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			final BuyAGetPrecentageDiscountCashbackModel cashBackModel = (BuyAGetPrecentageDiscountCashbackModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(cashBackModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			final BuyAGetPrecentageDiscountCashbackModel cashBackModel = (BuyAGetPrecentageDiscountCashbackModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(cashBackModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			final BuyAandBGetPrecentageDiscountCashbackModel cashBackModel = (BuyAandBGetPrecentageDiscountCashbackModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(cashBackModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAAboveXGetPercentageOrAmountOffModel)
		{
			final BuyAAboveXGetPercentageOrAmountOffModel threshPromo = (BuyAAboveXGetPercentageOrAmountOffModel) productPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(threshPromo.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}

		return promoData;
	}


	/**
	 * @Description: Populate Potential Promotion Data for Order Promotion
	 * @param orderPromotion
	 * @param cart
	 * @return responseData
	 */
	public MplPromotionData populatePotentialOrderPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		MplPromotionData promoData = null;

		if (orderPromotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			final CartOrderThresholdDiscountPromotionModel oModel = (CartOrderThresholdDiscountPromotionModel) orderPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		return promoData;
	}


	/**
	 * @Description : For Cart Level Discount Promotion
	 * @param orderPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getCartDiscountPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		final MplPromotionData cartPromoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final CartOrderThresholdDiscountPromotionModel cartModel = (CartOrderThresholdDiscountPromotionModel) orderPromotion;
		if (null != cartModel)
		{
			cartPromoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CART_PROMO);
			if (null != cartModel.getDiscountPrices())
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = cartModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
				}

				if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
						&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
				{
					final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
					cartPromoData.setDiscountPrice(discountPrice);
				}
			}

			if (null != cartModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					cartPromoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != cartModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					cartPromoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return cartPromoData;
	}


	/**
	 * @Description : For Buy A Above X Get Percentage Or Amount Off Promotion
	 * @param productPromotion
	 * @param cart
	 * @return promoData
	 */
	private MplPromotionData getBuyAAboveXData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final BuyAAboveXGetPercentageOrAmountOffModel buyAAboveXDiscountModel = (BuyAAboveXGetPercentageOrAmountOffModel) productPromotion;
		if (null != buyAAboveXDiscountModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			promoData.setIsPercentage(buyAAboveXDiscountModel.getPercentageOrAmount().toString());
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);

			if (null != buyAAboveXDiscountModel.getPercentageDiscount())
			{
				promoData.setPercentagePromotion(buyAAboveXDiscountModel.getPercentageDiscount().toString());
			}

			if (null != buyAAboveXDiscountModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(buyAAboveXDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != buyAAboveXDiscountModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(buyAAboveXDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}


	/**
	 * @Description : For Buy A Get Percentage/Amount CashBack
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyACashBackData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();
		final BuyAGetPrecentageDiscountCashbackModel cashBackModel = (BuyAGetPrecentageDiscountCashbackModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CASHBACK_FIREDPROMO_MESSAGE);
		if (null != cashBackModel.getPercentageOrAmount() && cashBackModel.getPercentageOrAmount().booleanValue()
				&& null != cashBackModel.getPercentageDiscount())
		{

			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (!cashBackModel.getPercentageOrAmount().booleanValue() && null != cashBackModel.getDiscountPrices())
		{
			final Collection<PromotionPriceRowModel> discountPriceRows = cashBackModel.getDiscountPrices();
			if (null != discountPriceRows)
			{
				priceRowList.addAll(discountPriceRows);
				if (priceRowList.size() > 0 && null != priceRowList.get(0).getPrice())
				{
					final Double cashbBackVal = priceRowList.get(0).getPrice();
					final PriceData discountPrice = createPrice(cart, cashbBackVal);
					promoData.setDiscountPrice(discountPrice);

					final FiredPromoData firedPromo = new FiredPromoData();
					firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifAmount"));
					if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
					{
						promoData.setFiredPromotion(firedPromo);
					}
				}
			}
		}
		return promoData;
	}


	/**
	 * @Description : For Buy A and B Get Percentage/Amount CashBack
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAandBCashBackData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAandBGetPrecentageDiscountCashbackModel cashBackModel = (BuyAandBGetPrecentageDiscountCashbackModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CASHBACK_FIREDPROMO_MESSAGE);
		if (null != cashBackModel.getPercentageOrAmount() && cashBackModel.getPercentageOrAmount().booleanValue()
				&& null != cashBackModel.getPercentageDiscount())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (!cashBackModel.getPercentageOrAmount().booleanValue() && null != cashBackModel.getDiscountPrices())
		{
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = cashBackModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
					if (priceRowList.size() > 0 && null != priceRowList.get(0).getPrice())
					{
						final Double cashbBackVal = priceRowList.get(0).getPrice();
						final PriceData discountPrice = createPrice(cart, cashbBackVal);
						promoData.setDiscountPrice(discountPrice);

						final FiredPromoData firedPromo = new FiredPromoData();
						firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifAmount"));
						if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
						{
							promoData.setFiredPromotion(firedPromo);
						}
					}
				}
			}

		}
		return promoData;
	}



	/**
	 * @Description : For BOGO Promotion
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBOGOData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final CustomProductBOGOFPromotionModel bogoModel = (CustomProductBOGOFPromotionModel) productPromotion;
		if (null != bogoModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);

			if (null != bogoModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(bogoModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != bogoModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(bogoModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy A and B Get Discount Promotion
	 * @param productPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	private MplPromotionData getBuyAandBPrecentageDiscountData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final BuyAandBPrecentageDiscountModel oModel = (BuyAandBPrecentageDiscountModel) productPromotion;
		if (null != oModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);
			if (null != oModel.getPercentageOrAmount())
			{
				promoData.setIsPercentage(oModel.getPercentageOrAmount().toString());
			}

			if (null != oModel.getPercentageDiscount())
			{
				promoData.setPercentagePromotion(oModel.getPercentageDiscount().toString());
			}

			if (null != oModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(oModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != oModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(oModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy A Get Discount Promotion
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAPercentageDiscountData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final BuyAPercentageDiscountModel buyAPercentageDiscountModel = (BuyAPercentageDiscountModel) productPromotion;
		if (null != buyAPercentageDiscountModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			promoData.setIsPercentage(buyAPercentageDiscountModel.getPercentageOrAmount().toString());
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);

			if (null != buyAPercentageDiscountModel.getPercentageDiscount())
			{
				promoData.setPercentagePromotion(buyAPercentageDiscountModel.getPercentageDiscount().toString());
			}

			if (null != buyAPercentageDiscountModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(buyAPercentageDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != buyAPercentageDiscountModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(buyAPercentageDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}


	/**
	 * @Description : To Calculate Discount value
	 * @param cart
	 * @return discount
	 */
	private Double calculateDiscount(final CartModel cart)
	{
		double discount = 0.0d;
		double totalPrice = 0.0D;
		if (null != cart && CollectionUtils.isNotEmpty(cart.getEntries()))
		{
			final List<DiscountModel> discountList = cart.getDiscounts();
			final List<DiscountValue> discountValueList = cart.getGlobalDiscountValues();
			double voucherDiscount = 0.0d;
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				totalPrice = totalPrice + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
			}

			for (final DiscountValue discountValue : discountValueList)
			{
				if (CollectionUtils.isNotEmpty(discountList) && discountValue.getCode().equals(discountList.get(0).getCode()))
				{
					voucherDiscount = discountValue.getAppliedValue();
					break;
				}
			}

			discount = (totalPrice + cart.getDeliveryCost().doubleValue()) - cart.getTotalPriceWithConv().doubleValue()
					- voucherDiscount;
		}
		return roundData(discount);
	}

	/**
	 * @Description : Round Discount value post calculation
	 * @param discount
	 * @return value
	 */
	private Double roundData(final double discount)
	{
		Double value = Double.valueOf(0);
		if (discount > 0.0D)
		{
			value = Double.valueOf(Math.round(discount * 100.0) / 100.0);
		}
		return value;
	}


	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
	public PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}


	/**
	 * @Description
	 * @param cartData
	 * @return promoPriceData
	 */
	public MplPromotionData populateNonPromoData(final CartData cartData)
	{
		final MplPromotionData promoData = new MplPromotionData();
		if (null != cartData && null != cartData.getTotalDiscounts())
		{
			promoData.setDiscountPrice(cartData.getTotalDiscounts());
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.OTHER_PROMO);
		}

		return promoData;
	}





	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}





	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

}