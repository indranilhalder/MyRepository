package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
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
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


public class CustomOrderThresholdFreeGiftPromotion extends GeneratedCustomOrderThresholdFreeGiftPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomOrderThresholdFreeGiftPromotion.class.getName());


	/**
	 * @Description: Method for Item Creation
	 * @param:SessionContext ctx
	 * @param:ComposedType type
	 * @param:ItemAttributeMap allAttributes
	 * @throws:JaloBusinessException
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
	 * @Description : If Cart Value Greater than Threshold Value Set Customer gets free Gift
	 * @param :
	 *           SessionContext ctx ,PromotionEvaluationContext promoContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;

		final List promotionResults = new ArrayList();
		boolean checkChannelFlag = false;
		try
		{
			final boolean promotionAlreadyFired = getDefaultPromotionsManager().cartPromotionAlreadyFired(ctx,
					promoContext.getOrder());
			final AbstractOrder order = promoContext.getOrder();
			if (!promotionAlreadyFired)
			{
				checkChannelFlag = verifyChannelData(ctx, order);
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());//Adding restrictions to List
				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForOrderPromo(restrictionList);
				//for payment mode restriction check
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList, ctx);

				if (checkRestrictions(ctx, promoContext) && checkChannelFlag && flagForDeliveryModeRestrEval
						&& flagForPaymentModeRestrEval)
				{
					final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(),
							MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
					if (threshold != null)
					{
						final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
						if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
						{
							if (LOG.isDebugEnabled())
							{
								LOG.debug("(" + getPK() + ") "
										+ Localization.getLocalizedString("promotion.CustomOrderThresholdFreeGiftPromotion.subtotal")
										+ orderSubtotalAfterDiscounts
										+ Localization.getLocalizedString("promotion.CustomOrderThresholdFreeGiftPromotion.greater")
										+ threshold + Localization.getLocalizedString("promotion.freeGiftAction"));
							}
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
									promoContext.getOrder(), 1.0F);
							ctx.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(1));
							final List<Product> giftProductList = (List<Product>) getGiftProducts(ctx);
							if (null != giftProductList && !giftProductList.isEmpty())
							{
								final Map<String, Product> giftProductDetails = getDefaultPromotionsManager()
										.getCartPromoGiftProducts(giftProductList);//Validating the free gift corresponding to seller ID for scenario : Eligible product and Free gift from same DC
								if (null != giftProductDetails)
								{
									for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
									{
										ctx.setAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE,
												String.valueOf(this.getCode()));
										result.addAction(ctx, getDefaultPromotionsManager().createCustomPromotionOrderAddFreeGiftAction(ctx,
												entry.getValue(), entry.getKey(), result, Double.valueOf(1))); //Adding Free gifts to cart
									}
								}
							}
							promotionResults.add(result);
						}
						else
						{
							if (LOG.isDebugEnabled())
							{
								LOG.debug("(" + getPK() + ") "
										+ Localization.getLocalizedString("promotion.CustomOrderThresholdFreeGiftPromotion.subtotal")
										+ orderSubtotalAfterDiscounts
										+ Localization.getLocalizedString("promotion.CustomOrderThresholdFreeGiftPromotion.less")
										+ threshold + Localization.getLocalizedString("promotion.freeGiftAction.skip"));
							}
							final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
									promoContext.getOrder(), certainty);
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

	/**
	 * @Description : Verify Channel Data
	 * @param :
	 *           SessionContext arg0
	 * @return : minimumCategoryValue
	 */
	private boolean verifyChannelData(final SessionContext arg0, final AbstractOrder cart)
	{
		final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
				MarketplacecommerceservicesConstants.CHANNEL);
		return getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
	}


	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext ctx ,PromotionResult result ,Locale locale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{
		try
		{
			final AbstractOrder order = result.getOrder(ctx);
			if (order != null)
			{
				final Currency orderCurrency = order.getCurrency(ctx);
				final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order,
						MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
				if (threshold != null)
				{
					if (result.getFired(ctx))
					{
						final Object[] args =
						{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()) };
						return formatMessage(getMessageFired(ctx), args, locale);
					}
					if (result.getCouldFire(ctx))
					{
						final double amountRequired = threshold.doubleValue() - order.getTotalPrice().doubleValue();

						final Object[] args =
						{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()),
								Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired) };
						return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
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
		return "";
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
