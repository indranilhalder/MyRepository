package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * This promotion is of type Buy above threshold value X and get discount on shipping charges
 *
 */
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
	 * @param: SessionContext arg0 ,PromotionEvaluationContext arg1
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean checkChannelFlag = false;
		//CR Changes : TPR-715
		//Map<String, AbstractOrderEntry> validUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
		//CR Changes : TPR-715 Ends
		try
		{
			final boolean promotionAlreadyFired = getDefaultPromotionsManager().cartPromotionAlreadyFired(arg0, arg1.getOrder());
			if (!promotionAlreadyFired)
			{
				final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
						MarketplacecommerceservicesConstants.CHANNEL);
				//final CartModel cartModel = getCartService().getSessionCart();
				//checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
				final AbstractOrder cart = arg1.getOrder();
				checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
				final AbstractOrder order = arg1.getOrder();
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());
				//final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
				final boolean flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(
						restrictionList, order);
				//for payment mode restriction check
				final boolean flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						arg0);

				//PR-15 starts here
				final PromotionsManager.RestrictionSetResult rsr = getDefaultPromotionsManager()
						.findEligibleProductsInBasketForCartPromo(arg0, arg1, this);
				//PR-15 ends here

				if (rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty() && checkRestrictions(arg0, arg1)
						&& checkChannelFlag && flagForPincodeRestriction && flagForPaymentModeRestrEval)//check added for PR-15
				{
					final Double threshold = getPriceForOrder(arg0, getThresholdTotals(arg0), arg1.getOrder(),
							MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
					if (threshold != null)
					{
						//CR Changes : TPR-715
						double orderSubtotalAfterDiscounts = 0.0D;
						//PR-15 starts here
						final List<Product> allowedProductList = new ArrayList<Product>(rsr.getAllowedProducts());
						//PR-15 ends here
						//						if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList)
						//								|| getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
						//						{
						//							validUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(arg0, order, restrictionList,
						//									allowedProductList);//PR-15 allowedProductList parameter added
						//							orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(arg0, validUssidMap);
						//							setSellersubTotalValue(orderSubtotalAfterDiscounts);
						//						}
						//						else
						//						{
						//							/* PR-15 starts here */
						//							validUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(arg0, order, null, allowedProductList);
						//							orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(arg0, validUssidMap);
						//							setSellersubTotalValue(orderSubtotalAfterDiscounts);
						//							/* PR-15 ends here */
						//						}
						//CR Changes : TPR-715Ends

						//						validUssidMap = (getDefaultPromotionsManager().isSellerRestrExists(restrictionList) || getDefaultPromotionsManager()
						//								.isExSellerRestrExists(restrictionList)) ? getMplPromotionHelper().getCartSellerEligibleProducts(arg0,
						//								order, restrictionList, allowedProductList) : getMplPromotionHelper().getCartSellerEligibleProducts(arg0,
						//								order, null, allowedProductList);

						Map<String, Integer> validProdQCountMap = null;
						Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
						final Map<String, AbstractOrderEntry> validUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(
								arg0, order, restrictionList, allowedProductList);//Seller Satisfied Map
						if (MapUtils.isNotEmpty(validUssidMap))
						{
							validProdQCountMap = getMplPromotionHelper().getvalidProdQCForOrderShippingPromotion(
									getDeliveryModeDetailsList(), validUssidMap);//Delivery Mode Satisfied Map with QC
						}
						if (MapUtils.isNotEmpty(validProdQCountMap))
						{
							validProductUssidMap = getValidProducts(order, arg0, validProdQCountMap);//Valid Product Map
						}

						orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(arg0, validProductUssidMap);
						setSellersubTotalValue(orderSubtotalAfterDiscounts);


						if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
						{
							//							if (MapUtils.isNotEmpty(validUssidMap))
							//							{
							//								validProdQCountMap = getMplPromotionHelper().getvalidProdQCForOrderShippingPromotion(
							//										getDeliveryModeDetailsList(), validUssidMap);
							//							}
							//
							//							//PR-15 ends here
							//							//CR Changes : TPR-715 Ends
							//							final Map<String, AbstractOrderEntry> validProductUssidMap = getValidProducts(order, arg0,
							//									validProdQCountMap);

							final Map<String, String> fetchProductRichAttribute = getDefaultPromotionsManager()
									.fetchProductRichAttribute(validProdQCountMap, order);
							int totalValidCount = 0;
							//validProdQCountMap.forEach((k, v) -> totalValidCount += v.intValue());
							for (final Integer value : validProdQCountMap.values())
							{
								totalValidCount += value.intValue();
							}
							final Map<String, Map<String, Double>> apportionedProdDelChargeMap = new HashMap<String, Map<String, Double>>();

							final EnumerationValue discountType = getDiscTypesOnShippingCharges();
							double adjustedDeliveryCharge = 0.00D;
							boolean isDeliveryFreeFlag = false;
							boolean isForcedShipping = false;
							if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE)
									&& (getPercentageDiscount() != null))
							{
								adjustedDeliveryCharge = getPercentageDiscount().doubleValue();
							}
							else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.AMOUNT)
									&& (getPriceForOrder(arg0, getDiscountPrices(arg0), order,
											MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
							{
								final double amount = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
										MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
								final double totalDelCostForValidProds = getDefaultPromotionsManager().getTotalDelCostForValidProds(
										validProductUssidMap, validProdQCountMap);
								adjustedDeliveryCharge = (amount / totalDelCostForValidProds) * 100;
							}
							else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
							{
								isDeliveryFreeFlag = true;
							}
							else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FORCED))
							{
								adjustedDeliveryCharge = getShippingCharge().doubleValue();
								isForcedShipping = true;
							}
							final Map<String, Boolean> isProdShippingPromoAppliedMap = getDefaultPromotionsManager()
									.getProdShippingPromoAppliedMap(order);
							for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
							{
								final String validProdUssid = mapEntry.getKey();
								final AbstractOrderEntry entry = mapEntry.getValue();
								final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProdUssid);
								if ((isTShipAsPrimitive() && isSShipAsPrimitive())
										|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
								{
									if (isForcedShipping)
									{
										apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcForcedDeliveryCharges(
												adjustedDeliveryCharge, isProdShippingPromoAppliedMap, totalValidCount, entry));

										//											apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcForcedDeliveryCharges(
										//											isDeliveryFreeFlag, adjustedDeliveryCharge, validProdUssid, order,
										//											isProdShippingPromoAppliedMap, totalValidCount, entry));
									}
									else
									{
										apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcDeliveryCharges(
												isDeliveryFreeFlag, adjustedDeliveryCharge, validProdUssid, order,
												isProdShippingPromoAppliedMap));
									}
								}
							}

							/////////////////////////////////////////////////////////////
							//							else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FORCED))
							//							{
							//								//							final double totalDelCostForTShipValidProds = getDefaultPromotionsManager()
							//								//									.getTotalDelCostForTshipValidProds(validProductUssidMap, validProdQCountMap);
							//								final double forcedTshipAmount = getShippingCharge().doubleValue();
							//								adjustedDeliveryCharge = forcedTshipAmount;
							//								final Map<String, Boolean> isProdShippingPromoAppliedMap = getDefaultPromotionsManager()
							//										.getProdShippingPromoAppliedMap(order);
							//								for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
							//								{
							//									//arg1.startLoggingConsumed(this);
							//									//final int validProductUssidMapSize = validProductUssidMap.size();
							//									final String validProdUssid = mapEntry.getKey();
							//									//final AbstractOrderEntry entry = mapEntry.getValue();
							//									final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProdUssid);
							//									if ((isTShipAsPrimitive() && isSShipAsPrimitive())
							//											|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
							//													.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							//									{
							//										//arg1.startLoggingConsumed(this);
							//										apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcForcedDeliveryCharges(
							//												isDeliveryFreeFlag, adjustedDeliveryCharge, validProdUssid, order,
							//												isProdShippingPromoAppliedMap, validProductUssidMapSize));
							//
							//										//										final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
							//										//												arg1.getOrder(), 1.0F);
							//										//										final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
							//										//												.createCustomShippingChargesPromotionAdjustAction(arg0, entry, 0.0D);
							//										//										final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
							//										//										result.setConsumedEntries(arg0, consumed);
							//										//										result.addAction(arg0, poeac);
							//										//										promotionResults.add(result);
							//									}
							//								}
							//
							//							}
							//							//For cart level shipping promotion only, to find entries for which product shipping is applied
							//							if (!discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FORCED))
							//							{
							//								final Map<String, Boolean> isProdShippingPromoAppliedMap = getDefaultPromotionsManager()
							//										.getProdShippingPromoAppliedMap(order);
							//
							//								for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
							//								{
							//									//arg1.startLoggingConsumed(this);
							//									final String validProdUssid = mapEntry.getKey();
							//									//final AbstractOrderEntry entry = mapEntry.getValue();
							//									final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProdUssid);
							//									if ((isTShipAsPrimitive() && isSShipAsPrimitive())
							//											|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
							//													.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							//									{
							//										//arg1.startLoggingConsumed(this);
							//										apportionedProdDelChargeMap.putAll(getDefaultPromotionsManager().calcDeliveryCharges(
							//												isDeliveryFreeFlag, adjustedDeliveryCharge, validProdUssid, order,
							//												isProdShippingPromoAppliedMap));
							//
							//										//										final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
							//										//												arg1.getOrder(), 1.0F);
							//										//										final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
							//										//												.createCustomShippingChargesPromotionAdjustAction(arg0, entry, 0.0D);
							//										//										final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
							//										//										result.setConsumedEntries(arg0, consumed);
							//										//										result.addAction(arg0, poeac);
							//										//										promotionResults.add(result);
							//									}
							//								}
							//							}

							/////////////////////////////////////////////
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
									arg1.getOrder(), 1.0F);
							final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
									.createCustomShippingChargesPromotionAdjustAction(arg0, null, 0.0D,
											new ArrayList<AbstractOrderEntry>(validProductUssidMap.values()));
							//final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
							//result.setConsumedEntries(arg0, consumed);
							result.addAction(arg0, poeac);
							promotionResults.add(result);
							/////////////////////////////////////////////
							//arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
							arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProdQCountMap);
							arg0.setAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE, String.valueOf(this.getCode()));
							arg0.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP, apportionedProdDelChargeMap);
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
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
									arg1.getOrder(), certainty);
							promotionResults.add(result);
						}
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
	 * @param arg0
	 * @param validProductUssidMap
	 * @return orderSubtotalAfterDiscounts
	 */
	private double getSellerSpecificSubtotal(final SessionContext arg0, final Map<String, AbstractOrderEntry> validProductUssidMap)
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

					if ((null != mapentry.getValue().getAttribute(arg0, "isBOGOapplied")
							&& BooleanUtils.toBoolean(mapentry.getValue().getAttribute(arg0, "isBOGOapplied").toString()) && null != mapentry
							.getValue().getAttribute(arg0, "bogoFreeItmCount")))
					{
						final double freecount = Double.parseDouble(mapentry.getValue().getAttribute(arg0, "bogoFreeItmCount")
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
		//PR-15 starts here
		/*
		 * final List<AbstractPromotionRestriction> restrictionList = new
		 * ArrayList<AbstractPromotionRestriction>(getRestrictions()); double subtotalVal = 0.0D; if
		 * (getDefaultPromotionsManager().isSellerRestrExists(restrictionList)) { subtotalVal = getSellersubTotalValue();
		 * } else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList)) { subtotalVal =
		 * getSellersubTotalValue(); } else { subtotalVal = getMplPromotionHelper().getTotalPrice(order); }
		 */
		final double subtotalVal = getSellersubTotalValue();
		return subtotalVal;
		//PR-15 ends here
	}

}