package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;


@SuppressWarnings("deprecation")
public class BuyAboveXGetPromotionOnShippingCharges extends GeneratedBuyAboveXGetPromotionOnShippingCharges
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAboveXGetPromotionOnShippingCharges.class.getName());

	private double sellersubTotalValue = 0.0D;

	/**
	 * @Description : This method is for creating item type
	 * @param: ctx
	 * @param: type
	 * @param: allAttributes
	 * @return: item
	 */
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	/**
	 * @Description : Order Threshold Shipping Promotion
	 * @param: SessionContext ctx ,PromotionEvaluationContext evaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext evaluationContext)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean checkChannelFlag = false;
		//CR Changes : TPR-715
		Map<String, AbstractOrderEntry> validUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
		//CR Changes : TPR-715 Ends
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			//final CartModel cartModel = getCartService().getSessionCart();
			//checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
			final AbstractOrder cart = evaluationContext.getOrder();
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			final AbstractOrder order = evaluationContext.getOrder();
			final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
			//final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
			final boolean flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(restrictionList,
					order);
			if (checkRestrictions(ctx, evaluationContext) && checkChannelFlag && flagForPincodeRestriction)
			{
				final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), evaluationContext.getOrder(),
						MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
				if (threshold != null)
				{
					//final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(arg0, order);

					//CR Changes : TPR-715
					double orderSubtotalAfterDiscounts = 0.0D;
					boolean sellerFlag = false;
					Map<String, Integer> validProdQCountMap = new HashMap<String, Integer>();

					if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
					{
						validUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(ctx, order, restrictionList);
						orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(ctx, validUssidMap);
						setSellersubTotalValue(orderSubtotalAfterDiscounts);
						sellerFlag = true;
					}
					else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
					{
						validUssidMap = getMplPromotionHelper().getCartSellerInEligibleProducts(ctx, order, restrictionList);
						orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(ctx, validUssidMap);
						setSellersubTotalValue(orderSubtotalAfterDiscounts);
						sellerFlag = true;
					}
					else
					{
						orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
					}

					//CR Changes : TPR-715Ends


					if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
					{
						//CR Changes : TPR-715
						if (!sellerFlag)
						{
							validProdQCountMap = getDefaultPromotionsManager().getvalidProdQCForOrderShippingPromotion(
									getDeliveryModeDetailsList(), order);
						}
						else if (MapUtils.isNotEmpty(validUssidMap))
						{
							validProdQCountMap = getMplPromotionHelper().getvalidProdQCForOrderShippingPromotion(
									getDeliveryModeDetailsList(), validUssidMap);
						}

						//CR Changes : TPR-715 Ends

						final Map<String, AbstractOrderEntry> validProductUssidMap = getValidProducts(order, ctx, validProdQCountMap);
						final Map<String, String> fetchProductRichAttribute = getDefaultPromotionsManager().fetchProductRichAttribute(
								validProdQCountMap, order);
						final Map<String, Map<String, Double>> apportionedProdDelChargeMap = new HashMap<String, Map<String, Double>>();

						final EnumerationValue discountType = getDiscTypesOnShippingCharges();
						double adjustedDeliveryCharge = 0.00D;
						boolean isDeliveryFreeFlag = false;
						if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE)
								&& (getPercentageDiscount() != null))
						{
							adjustedDeliveryCharge = getPercentageDiscount().doubleValue();
						}
						else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.AMOUNT)
								&& (getPriceForOrder(ctx, getDiscountPrices(ctx), order,
										MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
						{
							final double amount = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
									MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
							final double totalDelCostForValidProds = getDefaultPromotionsManager().getTotalDelCostForValidProds(
									validProductUssidMap, validProdQCountMap);
							adjustedDeliveryCharge = (amount / totalDelCostForValidProds) * 100;
						}
						else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
						{
							isDeliveryFreeFlag = true;
						}

						//For cart level shipping promotion only, to find entries for which product shipping is applied
						final Map<String, Boolean> isProdShippingPromoAppliedMap = getDefaultPromotionsManager()
								.getProdShippingPromoAppliedMap(order);

						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							//arg1.startLoggingConsumed(this);
							final String validProdUssid = mapEntry.getKey();
							final AbstractOrderEntry entry = mapEntry.getValue();
							final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProdUssid);
							if ((isTShipAsPrimitive() && isSShipAsPrimitive())
									|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							{
								evaluationContext.startLoggingConsumed(this);
								apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcDeliveryCharges(isDeliveryFreeFlag,
										adjustedDeliveryCharge, validProdUssid, order, isProdShippingPromoAppliedMap));

								final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
										evaluationContext.getOrder(), 1.0F);
								final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
										.createCustomShippingChargesPromotionAdjustAction(ctx, entry, 0.0D);
								final List consumed = evaluationContext.finishLoggingAndGetConsumed(this, true);
								result.setConsumedEntries(ctx, consumed);
								result.addAction(ctx, poeac);
								promotionResults.add(result);
							}
						}

						ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
						ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProdQCountMap);
						ctx.setAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE, String.valueOf(this.getCode()));
						ctx.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP, apportionedProdDelChargeMap);

					}
					else if (orderSubtotalAfterDiscounts > 0.0D)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("(" + getPK() + ")"
									+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg1")
									+ orderSubtotalAfterDiscounts
									+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg2")
									+ threshold
									+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg3"));
						}
						final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
								evaluationContext.getOrder(), certainty);
						promotionResults.add(result);
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

		return promotionResults;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.promotions.jalo.AbstractPromotion#getResultDescription(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.jalo.PromotionResult, java.util.Locale)
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{

		final AbstractOrder order = result.getOrder(ctx);
		if (order != null)
		{
			//final Currency orderCurrency = order.getCurrency(ctx);

			final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order,
					MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);


			if (threshold != null)
			{
				String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
				if (null != ctx.getCurrency() && null != ctx.getCurrency().getIsocode())
				{
					currency = ctx.getCurrency().getIsocode();
					if (null == currency)
					{
						currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
					}
				}

				final EnumerationValue discountType = getDiscTypesOnShippingCharges();
				double adjustedDeliveryCharge = 0.00D;
				final StringBuilder discPerOrAmtStr = new StringBuilder();
				if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE)
						&& (getPercentageDiscount() != null))
				{
					adjustedDeliveryCharge = getPercentageDiscount().doubleValue();
					discPerOrAmtStr.append(String.valueOf(adjustedDeliveryCharge));
					discPerOrAmtStr.append('%');
				}
				else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.AMOUNT)
						&& (getPriceForOrder(ctx, getDiscountPrices(ctx), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
				{
					adjustedDeliveryCharge = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
							MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
					discPerOrAmtStr.append(currency);
					discPerOrAmtStr.append(String.valueOf(adjustedDeliveryCharge));
				}
				else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
				{
					discPerOrAmtStr.append(MarketplacecommerceservicesConstants.FREE);
				}
				if (result.getFired(ctx))
				{
					final Object[] args =
					{ threshold, discPerOrAmtStr.toString() };
					return formatMessage(getMessageFired(ctx), args, locale);
				}
				if (result.getCouldFire(ctx))
				{
					final double orderSubtotalAfterDiscounts = getEligibleSubtotal(order);
					final double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;

					final Object[] args =
					{ Double.valueOf(amountRequired), discPerOrAmtStr };
					return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
				}
			}
		}

		return "";

	}

	/**
	 * @Description : Returns Valid Product List
	 * @param order
	 * @param ctx
	 * @return Map<Product, Integer>
	 */
	private Map<String, AbstractOrderEntry> getValidProducts(final AbstractOrder order, final SessionContext ctx,
			final Map<String, Integer> validProdQCountMap)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		for (final AbstractOrderEntry entry : order.getEntries())
		{
			try
			{
				final String selectedUSSID = (String) entry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
				if (validProdQCountMap.containsKey(selectedUSSID))
				{
					validProductUssidMap.put(selectedUSSID, entry);
				}
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e.toString());
			}
		}
		return validProductUssidMap;
	}


	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

	/**
	 * @return the sellersubTotalValue
	 */
	public double getSellersubTotalValue()
	{
		return sellersubTotalValue;
	}

	/**
	 * @param sellersubTotalValue
	 *           the sellersubTotalValue to set
	 */
	public void setSellersubTotalValue(final double sellersubTotalValue)
	{
		this.sellersubTotalValue = sellersubTotalValue;
	}

	/**
	 * The Method returns the total entry price post product discount
	 *
	 * CR Changes : TPR-715
	 *
	 * @param ctx
	 * @param validProductUssidMap
	 * @return orderSubtotalAfterDiscounts
	 */
	private double getSellerSpecificSubtotal(final SessionContext ctx, final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		double orderSubtotalAfterDiscounts = 0.0D;
		try
		{
			if (MapUtils.isNotEmpty(validProductUssidMap))
			{
				double bogoFreePrice = 0.0D;
				double totalPrice = 0.0D;

				for (final Map.Entry<String, AbstractOrderEntry> mapentry : validProductUssidMap.entrySet())
				{
					if (null != mapentry && null != mapentry.getValue() && null != mapentry.getValue().getTotalPrice())
					{
						totalPrice = totalPrice + (mapentry.getValue().getTotalPrice().doubleValue());
					}

					if ((null != mapentry.getValue().getAttribute(ctx, "isBOGOapplied")
							&& BooleanUtils.toBoolean(mapentry.getValue().getAttribute(ctx, "isBOGOapplied").toString()) && null != mapentry
							.getValue().getAttribute(ctx, "bogoFreeItmCount")))
					{
						final double freecount = Double.parseDouble(mapentry.getValue().getAttribute(ctx, "bogoFreeItmCount")
								.toString());
						bogoFreePrice = bogoFreePrice + (freecount * 0.01);
					}
				}

				if (totalPrice != 0.0D)
				{
					orderSubtotalAfterDiscounts = totalPrice - bogoFreePrice;
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}

		return orderSubtotalAfterDiscounts;
	}

	/**
	 * The Method Calculates Data for Message
	 *
	 * CR Changes : TPR-715
	 *
	 * @param order
	 *
	 * @return subtotalVal
	 */
	private double getEligibleSubtotal(final AbstractOrder order)
	{
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		double subtotalVal = 0.0D;
		if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
		{
			subtotalVal = getSellersubTotalValue();
		}
		else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
		{
			subtotalVal = getSellersubTotalValue();
		}
		else
		{
			subtotalVal = getMplPromotionHelper().getTotalPrice(order);
		}
		return subtotalVal;
	}

}