package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


public class CartOrderThresholdDiscountCashback extends GeneratedCartOrderThresholdDiscountCashback
{

	private double adjustedDiscounts = 0.0d;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CartOrderThresholdDiscountCashback.class.getName());

	//private boolean flagForDeliveryModeRestrEval;

	/**
	 * @Description : This method is for creating item type
	 * @param :
	 *           ctx
	 * @param :
	 *           type
	 * @param :
	 *           allAttributes
	 * @throws: JaloBusinessException
	 * @return : item
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
	 * @Description : Order Threshold Percentage or Amount Discount Cashback
	 * @param :
	 *           SessionContext paramSessionContext ,PromotionEvaluationContext paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext evaluationContext)
	{
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;

		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		try
		{
			//Checks whether in Cart Already a Cart level Promotion has been fired : If Fired Promotion will not apply
			final boolean promotionAlreadyFired = getDefaultPromotionsManager().cartPromotionAlreadyFired(paramSessionContext,
					evaluationContext.getOrder());
			if (!promotionAlreadyFired)
			{
				final boolean checkChannelFlag = verifyChannelData(paramSessionContext, evaluationContext.getOrder()); // Verifying the Channel : Web/Web Mobile/ CockPit
				final AbstractOrder cart = evaluationContext.getOrder();
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());//Adding restrictions to List
				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForOrderPromo(restrictionList);
				//for payment mode restriction check
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						paramSessionContext);

				if (checkRestrictions(paramSessionContext, evaluationContext) && checkChannelFlag && flagForDeliveryModeRestrEval
						&& flagForPaymentModeRestrEval)
				{
					final double percentageDiscount = getPercentageDiscount() == null ? 0.0D : getPercentageDiscount().doubleValue();
					//final double orderAdjustment = 0.0D;
					final Double threshold = getPriceForOrder(paramSessionContext, getThresholdTotals(paramSessionContext), //Get the Threshold Limit set in Promotions
							evaluationContext.getOrder(), MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
					if (threshold != null)
					{
						//Calculating the Subtotal Post Discount
						final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(paramSessionContext, cart);
						adjustedDiscounts = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;

						if (orderSubtotalAfterDiscounts >= threshold.doubleValue()) // Checking if the value is greater than or equal to Promotion set Threshold
						{
							if (!isPercentageOrAmount().booleanValue())
							{
								final Double discountPrice = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext),
										cart, MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
								final double discountPriceValue = (discountPrice == null) ? 0.0D : discountPrice.doubleValue();
								adjustedDiscounts = discountPriceValue;
							}
							else
							{
								adjustedDiscounts = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
							}

							//percentage discount calculations
							if (percentageDiscount < 100)
							{
								//For Promotion Message on Cart Screen : Promotion Fired Scenario
								final PromotionResult result = getPromoResultData(1.0F, paramSessionContext, evaluationContext);
								result.addAction(paramSessionContext,
										PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(paramSessionContext, 0.0D));
								promotionResults.add(result);
							}
							else
							{
								adjustedDiscounts = 0;
							}
						}
						else
						{
							//For Promotion Message on Cart Screen
							if (LOG.isDebugEnabled())
							{
								LOG.debug(
										"(" + getPK() + ") " + Localization.getLocalizedString("promotion.CartCashBackPromotion.subtotal")
												+ orderSubtotalAfterDiscounts
												+ Localization
														.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg2")
										+ threshold + Localization.getLocalizedString("promotion.CartCashBackPromotion.skippingDiscount"));
							}
							final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
							promotionResults.add(getPromoResultData(certainty, paramSessionContext, evaluationContext));
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


	/**
	 * @Description: For Promotion Result Population
	 * @param certainty
	 * @param paramSessionContext
	 * @param evaluationContext
	 * @return result
	 */
	private PromotionResult getPromoResultData(final float certainty, final SessionContext paramSessionContext,
			final PromotionEvaluationContext evaluationContext)
	{
		final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
				evaluationContext.getOrder(), certainty);
		return result;
	}



	/**
	 * @Description : Verify Channel Data
	 * @param :
	 *           SessionContext arg0
	 * @return : minimumCategoryValue
	 */
	private boolean verifyChannelData(final SessionContext arg0, final AbstractOrder cart)
	{
		//The Method validates the Channel through a generic method in Default Promotion Manager
		final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
				MarketplacecommerceservicesConstants.CHANNEL);
		return getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext ctx
	 * @param :
	 *           PromotionResult result
	 * @param:: Locale
	 *             locale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{
		final AbstractOrder order = result.getOrder(ctx);
		if (order != null)
		{
			final Currency orderCurrency = order.getCurrency(ctx);

			final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order,
					MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
			if (null != threshold)
			{
				Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
						MarketplacecommerceservicesConstants.DISCOUNT_PRICES);

				if (null == discountPriceValue)
				{
					discountPriceValue = Double.valueOf(0.00);
				}

				if (result.getFired(ctx))
				{
					final Object[] args =
					{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, adjustedDiscounts) };
					return formatMessage(getMessageFired(ctx), args, locale);
				}
				else if (result.getCouldFire(ctx))
				{
					final double orderSubtotal = getOrderSubtotalAfterDiscounts(ctx, order);
					final double amountRequired = threshold.doubleValue() - orderSubtotal;

					final Object[] args =
					{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
							Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired) };
					return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
				}
			}
		}
		return MarketplacecommerceservicesConstants.EMPTY;
	}

	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}
