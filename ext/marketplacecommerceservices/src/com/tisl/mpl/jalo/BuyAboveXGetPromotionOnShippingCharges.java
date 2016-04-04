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
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;


public class BuyAboveXGetPromotionOnShippingCharges extends GeneratedBuyAboveXGetPromotionOnShippingCharges
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAboveXGetPromotionOnShippingCharges.class.getName());

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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotion#evaluate(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.result.PromotionEvaluationContext)
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean checkChannelFlag = false;

		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
					MarketplacecommerceservicesConstants.CHANNEL);
			//final CartModel cartModel = getCartService().getSessionCart();
			//checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
			final AbstractOrder cart = arg1.getOrder();
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			final AbstractOrder order = arg1.getOrder();
			//final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
			if (checkRestrictions(arg0, arg1) && checkChannelFlag)
			{
				final Double threshold = getPriceForOrder(arg0, getThresholdTotals(arg0), arg1.getOrder(),
						MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
				if (threshold != null)
				{
					final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(arg0, order);

					if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
					{
						final Map<String, Integer> validProdQCountMap = getDefaultPromotionsManager()
								.getvalidProdQCForOrderShippingPromotion(getDeliveryModeDetailsList());
						final Map<String, AbstractOrderEntry> validProductUssidMap = getValidProducts(order, arg0, validProdQCountMap);
						final Map<String, String> fetchProductRichAttribute = getDefaultPromotionsManager().fetchProductRichAttribute(
								validProdQCountMap);

						//for (final Map.Entry<String, Integer> mapEntry : validProdQCountMap.entrySet())
						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							arg1.startLoggingConsumed(this);
							final String validProdUssid = mapEntry.getKey();
							final AbstractOrderEntry entry = mapEntry.getValue();
							final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProdUssid);
							if ((isTShipAsPrimitive() && isSShipAsPrimitive())
									|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							{
								//*******Calculating delivery charges & setting it at entry level starts*******
								final EnumerationValue discountType = getDiscTypesOnShippingCharges();
								double adjustedDeliveryCharge = 0.00D;
								boolean isPercentageFlag = false;
								boolean isDeliveryFreeFlag = false;
								if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE)
										&& (getPercentageDiscount() != null))
								{
									isPercentageFlag = true;
									adjustedDeliveryCharge = getPercentageDiscount().doubleValue();
								}
								else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.AMOUNT)
										&& (getPriceForOrder(arg0, getDiscountPrices(arg0), order,
												MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
								{
									adjustedDeliveryCharge = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
											MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
								}
								else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
								{
									isDeliveryFreeFlag = true;
								}


								final Map<String, Map<String, Double>> apportionedProdDelChargeMap = getDefaultPromotionsManager()
										.updateDeliveryCharges(isDeliveryFreeFlag, isPercentageFlag, adjustedDeliveryCharge,
												validProdQCountMap, fetchProductRichAttribute);

								arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
								arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProdQCountMap);
								arg0.setAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE, String.valueOf(this.getCode()));
								arg0.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP,
										apportionedProdDelChargeMap);
								//********Calculating delivery charges & setting it at entry level ends*********
								//final double adjustment = 0.0D;
								final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
										arg1.getOrder(), 1.0F);
								final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
										.createCustomShippingChargesPromotionAdjustAction(arg0, entry, 0.0D);
								final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
								result.setConsumedEntries(arg0, consumed);
								result.addAction(arg0, poeac);
								promotionResults.add(result);
							}
						}

					}
					else
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
					final double orderSubtotalAfterDiscounts = getMplPromotionHelper().getTotalPrice(order);
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
	 * @param cart
	 * @param paramSessionContext
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

}
