package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
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
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;


@SuppressWarnings("deprecation")
public class CartOrderThresholdDiscountPromotion extends GeneratedCartOrderThresholdDiscountPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CartOrderThresholdDiscountPromotion.class.getName());

	private double adjustedDiscounts = 0.0d;
	private double sellersubTotalValue = 0.0D;

	//private boolean flagForDeliveryModeRestrEval;

	/**
	 * @Description : This method is for creating item type
	 * @param ctx
	 * @param type
	 * @param allAttributes
	 * @return item
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
	 * @param ctx
	 *           paramSessionContext ,PromotionEvaluationContext paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext evalCtx)
	{
		//boolean flagForDeliveryModeRestrEval = false;		//Commented as defined later TPR-969
		//boolean flagForPaymentModeRestrEval = false;		//Commented as defined later TPR-969

		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		//boolean checkChannelFlag = false;						//Commented as defined later TPR-969

		//CR Changes : TPR-715
		Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();

		try
		{
			final boolean promotionAlreadyFired = getDefaultPromotionsManager().cartPromotionAlreadyFired(ctx, evalCtx.getOrder());
			if (!promotionAlreadyFired)
			{
				final double maxDiscount = getMaxDiscountVal() == null ? 0.0D : getMaxDiscountVal().doubleValue();
				final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
						MarketplacecommerceservicesConstants.CHANNEL);
				//final CartModel cartModel = getCartService().getSessionCart();
				//checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
				//final AbstractOrder order = arg1.getOrder();
				//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
				final AbstractOrder order = evalCtx.getOrder();

				final boolean checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, order);
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());//Adding restrictions to List
				//for delivery mode restriction check
				final boolean flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForOrderPromo(
						restrictionList, order);
				//for payment mode restriction check
				final boolean flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						ctx);
				final boolean flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(
						restrictionList, order);
				if (checkRestrictions(ctx, evalCtx) && checkChannelFlag && flagForDeliveryModeRestrEval
						&& flagForPaymentModeRestrEval && flagForPincodeRestriction)
				{
					final boolean isPercentageDisc = false;
					final double percentageDiscount = getPercentageDiscount() == null ? 0.0D : getPercentageDiscount().doubleValue();
					final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), evalCtx.getOrder(),
							MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);

					//TPR-969 - Code to identify qualifying count for the promotion restriction
					Double entryQualifyingCount = null;
					for (final AbstractPromotionRestriction restriction : restrictionList)
					{
						if (restriction instanceof EntryCountPromotionRestriction)
						{
							entryQualifyingCount = ((EntryCountPromotionRestriction) restriction).getEntryQualifyingCount();
							break;
						}
					}

					//CR Changes : TPR-715
					double orderSubtotalAfterDiscounts = 0.0D;
					ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.FALSE);
					ctx.setAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS, validProductUssidMap);
					if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
					{
						validProductUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(ctx, order, restrictionList);
						ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.TRUE);
						orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(ctx, validProductUssidMap);
						setSellersubTotalValue(orderSubtotalAfterDiscounts);
					}
					else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
					{
						validProductUssidMap = getMplPromotionHelper().getCartSellerInEligibleProducts(ctx, order, restrictionList);
						ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.TRUE);
						orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(ctx, validProductUssidMap);
						setSellersubTotalValue(orderSubtotalAfterDiscounts);
					}
					else
					{
						orderSubtotalAfterDiscounts = getSubtotalAfterDiscount(ctx, order);
					}

					//CR Changes : TPR-715

					//TPR-969 - Code to identify count of products present in cart
					final int orderEntryCount = getOrderEntryCount(validProductUssidMap, ctx, order);

					if (threshold != null)
					{
						//final double orderSubtotalAfterDiscounts = getSubtotalAfterDiscount(arg0, order);

						//Commented as not needed for TPR-969
						//							if (isPercentageOrAmount().booleanValue())
						//							{
						//								final double totalSavingsForPercentage = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
						//								if (maxDiscount != 0 && totalSavingsForPercentage > maxDiscount)
						//								{
						//									percentageDiscount = (maxDiscount * 100) / orderSubtotalAfterDiscounts;
						//									LOG.info(Localization.getLocalizedString("promotion.percentageDiscount.maxVal") + percentageDiscount);
						//								}
						//								else
						//								{
						//									isPercentageDisc = true;
						//								}
						//							}
						//							else
						//							{
						//								LOG.debug(Localization.getLocalizedString("promotion.amountDiscount.msg"));
						//								final Double discountPrice = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
						//										MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
						//								final double discountPriceValue = (discountPrice == null) ? 0.0D : discountPrice.doubleValue();
						//								percentageDiscount = (discountPriceValue * 100) / orderSubtotalAfterDiscounts;
						//							}
						//
						//							if (percentageDiscount < 100)
						//							{
						//								arg0.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
						//										Double.valueOf(percentageDiscount));
						//								arg0.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
						//										Double.valueOf(orderSubtotalAfterDiscounts));
						//								arg0.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
						//								arg0.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC,
						//										Boolean.valueOf(isPercentageDisc));
						//
						//								//adjustedDiscounts = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
						//								setAdjustedDiscounts((percentageDiscount * orderSubtotalAfterDiscounts) / 100);
						//								final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
						//										arg1.getOrder(), 1.0F);
						//								result.addAction(
						//										arg0,
						//										getDefaultPromotionsManager().createCustomPromotionOrderAdjustTotalAction(arg0,
						//												-getAdjustedDiscounts()));
						//								promotionResults.add(result);
						//							}
						//						}
						//						else
						//						{
						//							if (LOG.isDebugEnabled())
						//							{
						//								LOG.debug("(" + getPK() + ")"
						//										+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg1")
						//										+ orderSubtotalAfterDiscounts
						//										+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg2")
						//										+ threshold
						//										+ Localization.getLocalizedString("promotion.orderLevelPromotion.cartAmtLessThanThreshold.msg3"));
						//							}
						//							final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
						//							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
						//									arg1.getOrder(), certainty);
						//							promotionResults.add(result);
						//						}

						//TPR-969 - To check for qualifying count
						if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
						{
							LOG.debug("Ordersubtotal greater than threshold");
							applyPromoForQualCount(entryQualifyingCount, orderEntryCount, ctx, evalCtx, order, isPercentageDisc,
									percentageDiscount, maxDiscount, promotionResults, orderSubtotalAfterDiscounts, validProductUssidMap);
						}
						else if (orderSubtotalAfterDiscounts > 0.0D)
						{
							final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
									evalCtx.getOrder(), certainty);
							promotionResults.add(result);
						}
					}
					//TPR-969 - When no threshold is mentioned
					else
					{
						applyPromoForQualCount(entryQualifyingCount, orderEntryCount, ctx, evalCtx, order, isPercentageDisc,
								percentageDiscount, maxDiscount, promotionResults, orderSubtotalAfterDiscounts, validProductUssidMap);
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(e.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e) //Added for TISPT-195
		{
			LOG.error(e.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		return promotionResults;
	}



	/**
	 * This method evaluates promotion. Moved to single private method for TPR-969
	 *
	 * @param ctx
	 * @param evalCtx
	 * @param order
	 * @param isPercentageDisc
	 * @param percentageDiscount
	 * @param maxDiscount
	 * @param promotionResults
	 * @param validProductUssidMap
	 * @return List<PromotionResult>
	 */
	private List<PromotionResult> evaluatePromotion(final SessionContext ctx, final PromotionEvaluationContext evalCtx,
			final AbstractOrder order, boolean isPercentageDisc, double percentageDiscount, final double maxDiscount,
			final List<PromotionResult> promotionResults, final double orderSubtotalAfterDiscounts,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		if (isPercentageOrAmount().booleanValue())
		{
			final double totalSavingsForPercentage = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
			if (maxDiscount != 0 && totalSavingsForPercentage > maxDiscount)
			{
				percentageDiscount = (maxDiscount * 100) / orderSubtotalAfterDiscounts;
				LOG.info(Localization.getLocalizedString("promotion.percentageDiscount.maxVal") + percentageDiscount);
			}
			else
			{
				isPercentageDisc = true;
			}
		}
		else
		{
			LOG.debug(Localization.getLocalizedString("promotion.amountDiscount.msg"));
			final Double discountPrice = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
					MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
			final double discountPriceValue = (discountPrice == null) ? 0.0D : discountPrice.doubleValue();
			percentageDiscount = (discountPriceValue * 100) / orderSubtotalAfterDiscounts;
		}

		if (percentageDiscount < 100)
		{
			ctx.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT, Double.valueOf(percentageDiscount));
			ctx.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
					Double.valueOf(orderSubtotalAfterDiscounts));
			ctx.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
			ctx.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));


			//CR Changes : TPR-715
			if (MapUtils.isNotEmpty(validProductUssidMap))
			{
				ctx.setAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS, validProductUssidMap);
			}
			//CR Changes : TPR-715 ends

			//adjustedDiscounts = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
			setAdjustedDiscounts((percentageDiscount * orderSubtotalAfterDiscounts) / 100);
			final PromotionResult result = PromotionsManager.getInstance()
					.createPromotionResult(ctx, this, evalCtx.getOrder(), 1.0F);
			result.addAction(ctx,
					getDefaultPromotionsManager().createCustomPromotionOrderAdjustTotalAction(ctx, -getAdjustedDiscounts()));
			promotionResults.add(result);
		}

		return promotionResults;

	}





	/**
	 * This method creates promotionResult based on entry qualifying count
	 *
	 * @param entryQualifyingCount
	 * @param orderEntryCount
	 * @param ctx
	 * @param evalCtx
	 * @param order
	 * @param isPercentageDisc
	 * @param percentageDiscount
	 * @param maxDiscount
	 * @param promotionResults
	 * @param orderSubtotalAfterDiscounts
	 * @param validProductUssidMap
	 * @return List<PromotionResult>
	 */
	private List<PromotionResult> applyPromoForQualCount(final Double entryQualifyingCount, final int orderEntryCount,
			final SessionContext ctx, final PromotionEvaluationContext evalCtx, final AbstractOrder order,
			final boolean isPercentageDisc, final double percentageDiscount, final double maxDiscount,
			List<PromotionResult> promotionResults, final double orderSubtotalAfterDiscounts,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		if (null != entryQualifyingCount)
		{
			LOG.debug("Inside entry qualifying count not null");
			if (orderEntryCount >= entryQualifyingCount.intValue())
			{
				promotionResults = evaluatePromotion(ctx, evalCtx, order, isPercentageDisc, percentageDiscount, maxDiscount,
						promotionResults, orderSubtotalAfterDiscounts, validProductUssidMap);
			}
			else
			{
				final float certainty = (float) (orderEntryCount / entryQualifyingCount.doubleValue());
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this, evalCtx.getOrder(),
						certainty);
				promotionResults.add(result);
			}
		}
		else
		{
			LOG.debug("Inside entry qualifying count null");
			promotionResults = evaluatePromotion(ctx, evalCtx, order, isPercentageDisc, percentageDiscount, maxDiscount,
					promotionResults, orderSubtotalAfterDiscounts, validProductUssidMap);
		}

		return promotionResults;
	}



	/**
	 * This method is used for Localization Purpose
	 *
	 * @param ctx
	 * @param result
	 * @param locale
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{
		LOG.debug("Inside Message Localization Method for Promotion");
		final AbstractOrder order = result.getOrder(ctx);
		if (order != null)
		{
			final Currency orderCurrency = order.getCurrency(ctx);

			final Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order,
					MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);

			final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

			//TPR-969 identifying qualifying count present in restriction
			Double entryQualifyingCount = null;
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof EntryCountPromotionRestriction)
				{
					entryQualifyingCount = ((EntryCountPromotionRestriction) restriction).getEntryQualifyingCount();
					break;
				}
			}

			Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();
			if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
			{
				validProductUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(ctx, order, restrictionList);
			}
			else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
			{
				validProductUssidMap = getMplPromotionHelper().getCartSellerInEligibleProducts(ctx, order, restrictionList);
			}




			if (threshold != null)
			{
				Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
						MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
				if (null == discountPriceValue)
				{
					discountPriceValue = Double.valueOf(0);
				}

				LOG.debug("DISCOUNTPRICEVALUE" + discountPriceValue);
				if (result.getFired(ctx))
				{
					LOG.debug("Adjusted Price" + getEntryAdjustedPrice(order));
					final Object[] args =
					{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, getEntryAdjustedPrice(order)) };
					return formatMessage(getMessageFired(ctx), args, locale);
				}
				else if (result.getCouldFire(ctx))
				{
					if (null == entryQualifyingCount)
					{
						//return formatMsg(entryQualifyingCount, 0, threshold, order, ctx, locale, orderCurrency, discountPriceValue);
						final double orderSubtotalAfterDiscounts = getEligibleSubtotal(order);
						final double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;

						final Object[] args =
						{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()),
								discountPriceValue,
								Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
								Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired),
								null };
						return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
					}
					//TPR-969 When entry count restriction is present in promotion
					else
					{
						//TPR-969 - Code to identify count of products present in cart
						final int orderEntryCount = getOrderEntryCount(validProductUssidMap, ctx, order);
						return formatMsg(entryQualifyingCount, orderEntryCount, threshold, order, ctx, locale, orderCurrency,
								discountPriceValue);
					}
				}
			}


			//TPR-969 display message without any threshold value
			else
			{
				final int orderEntryCount = getOrderEntryCount(validProductUssidMap, ctx, order);
				int quantityNeeded = 0;
				if (null != entryQualifyingCount)
				{
					quantityNeeded = entryQualifyingCount.intValue() - orderEntryCount;
					if (quantityNeeded < 0)
					{
						quantityNeeded = 0;
					}
				}

				final String qtyNeededMsg = quantityNeeded + " more item(s)";

				final Object[] args =
				{ Double.valueOf(0.0), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, 0.0), null, null, Double.valueOf(0.0),
						Helper.formatCurrencyAmount(ctx, locale, orderCurrency, 0.0), qtyNeededMsg };
				return formatMessage(getMessageCouldHaveFired(ctx), args, locale);

			}
		}

		return MarketplacecommerceservicesConstants.EMPTYSTRING;

	}




	/**
	 * This method returns valid order entry count for TPR-969
	 *
	 * @param validProductUssidMap
	 * @param ctx
	 * @param order
	 * @return int
	 */
	private int getOrderEntryCount(final Map<String, AbstractOrderEntry> validProductUssidMap, final SessionContext ctx,
			final AbstractOrder order)
	{
		//TPR-969 - Code to identify count of products present in cart
		int orderEntryCount = 0;
		//When seller restriction is present
		if (MapUtils.isNotEmpty(validProductUssidMap))
		{
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				final AbstractOrderEntry entry = mapEntry.getValue();
				orderEntryCount += entry.getQuantity(ctx).doubleValue();
			}
		}
		//When seller restriction is not present
		else
		{
			final List<AbstractOrderEntry> entryList = order.getEntries();
			for (final AbstractOrderEntry entry : entryList)
			{
				orderEntryCount += entry.getQuantity(ctx).doubleValue();
			}
		}
		return orderEntryCount;
	}



	/**
	 * This method formats message for the promotion when threshold is present For TPR-969
	 *
	 * @param entryQualifyingCount
	 * @param orderEntryCount
	 * @param threshold
	 * @param order
	 * @param ctx
	 * @param locale
	 * @param orderCurrency
	 * @param discountPriceValue
	 * @return String
	 */
	private String formatMsg(final Double entryQualifyingCount, final int orderEntryCount, final Double threshold,
			final AbstractOrder order, final SessionContext ctx, final Locale locale, final Currency orderCurrency,
			final Double discountPriceValue)
	{
		int quantityNeeded = 0;
		String printData = MarketplacecommerceservicesConstants.EMPTY; // Added for TPR-3580

		if (null != entryQualifyingCount)
		{
			quantityNeeded = entryQualifyingCount.intValue() - orderEntryCount;
			if (quantityNeeded < 0)
			{
				quantityNeeded = 0;
			}
		}

		final String qtyNeededMsg = quantityNeeded + " more item(s)";
		final double orderSubtotalAfterDiscounts = getEligibleSubtotal(order);
		double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;
		if (amountRequired < 0)
		{
			amountRequired = 0;
		}

		if (amountRequired > 0 && quantityNeeded == 0)
		{
			printData = Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired);
		}
		else if (amountRequired == 0 && quantityNeeded > 0)
		{
			printData = "to buy" + MarketplacecommerceservicesConstants.SINGLE_SPACE + qtyNeededMsg;
		}
		else if (amountRequired > 0 && quantityNeeded > 0)
		{
			final StringBuilder data = new StringBuilder(500);
			data.append(String.valueOf(amountRequired));
			data.append(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			data.append("and buy");
			data.append(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			data.append(qtyNeededMsg);

			printData = data.toString();
		}

		final Object[] args =
		{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
				Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
				Double.valueOf(amountRequired), printData, qtyNeededMsg };
		return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
	}



	/****
	 *
	 * The Method is used to calculate the total discount value post discount calculation
	 *
	 * @param order
	 * @return entryAdjustedPrice
	 */
	private double getEntryAdjustedPrice(final AbstractOrder order)
	{
		double entryAdjustedPrice = 0;
		Double entryPrice = Double.valueOf(0);
		try
		{
			if (null != order && CollectionUtils.isNotEmpty(order.getEntries()))
			{
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					if (null != entry.getAttribute("cartLevelDisc"))
					{
						entryPrice = Double.valueOf(entry.getAttribute("cartLevelDisc").toString());
						entryAdjustedPrice = entryAdjustedPrice + entryPrice.doubleValue();
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

		return entryAdjustedPrice;
	}

	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}


	/**
	 * Getter Setter Generated for Sonar Fix
	 */
	/**
	 * @return the adjustedDiscounts
	 */
	public double getAdjustedDiscounts()
	{
		return adjustedDiscounts;
	}

	/**
	 * @param adjustedDiscounts
	 *           the adjustedDiscounts to set
	 */
	public void setAdjustedDiscounts(final double adjustedDiscounts)
	{
		this.adjustedDiscounts = adjustedDiscounts;
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

	/**
	 * The Method returns the total entry price post product discount Added for TISPRO-318
	 *
	 * @param arg0
	 * @param order
	 * @return orderSubtotalAfterDiscounts
	 */
	private double getSubtotalAfterDiscount(final SessionContext arg0, final AbstractOrder order)
	{
		double orderSubtotalAfterDiscounts = 0.0D;
		double totalPrice = 0.0D;

		try
		{
			if (null != order && CollectionUtils.isNotEmpty(order.getEntries()))
			{
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					if (getMplPromotionHelper().validateEntryForFreebie(entry))
					{
						totalPrice = totalPrice + entry.getTotalPrice().doubleValue();
					}
					else if ((null != entry.getAttribute(arg0, MarketplacecommerceservicesConstants.IS_BOGO_APPLIED)
							&& BooleanUtils.toBoolean(entry.getAttribute(arg0, MarketplacecommerceservicesConstants.IS_BOGO_APPLIED)
									.toString()) && null != entry.getAttribute(arg0, MarketplacecommerceservicesConstants.BOGO_ITEM_COUNT)))
					{
						final double freecount = Double.parseDouble(entry.getAttribute(arg0,
								MarketplacecommerceservicesConstants.BOGO_ITEM_COUNT).toString());
						totalPrice = totalPrice + (freecount * 0.01);
					}
				}

				if (totalPrice != 0.0D)
				{
					orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(arg0, order) - totalPrice;
				}
				else
				{
					orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(arg0, order);
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
			orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(arg0, order);
		}

		return orderSubtotalAfterDiscounts;
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

					if ((null != mapentry.getValue().getAttribute(arg0, MarketplacecommerceservicesConstants.IS_BOGO_APPLIED)
							&& BooleanUtils.toBoolean(mapentry.getValue()
									.getAttribute(arg0, MarketplacecommerceservicesConstants.IS_BOGO_APPLIED).toString()) && null != mapentry
							.getValue().getAttribute(arg0, MarketplacecommerceservicesConstants.BOGO_ITEM_COUNT)))
					{
						final double freecount = Double.parseDouble(mapentry.getValue()
								.getAttribute(arg0, MarketplacecommerceservicesConstants.BOGO_ITEM_COUNT).toString());
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
}
