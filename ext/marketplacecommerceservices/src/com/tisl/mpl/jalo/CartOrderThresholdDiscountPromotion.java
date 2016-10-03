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
	 * @Description : Order Threshold Percentage or Amount Discount Cashback
	 * @param: SessionContext paramSessionContext ,PromotionEvaluationContext paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;

		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean checkChannelFlag = false;

		//CR Changes : TPR-715
		Map<String, AbstractOrderEntry> validProductUssidMap = new ConcurrentHashMap<String, AbstractOrderEntry>();

		try
		{
			final boolean promotionAlreadyFired = getDefaultPromotionsManager().cartPromotionAlreadyFired(arg0, arg1.getOrder());
			if (!promotionAlreadyFired)
			{
				final double maxDiscount = getMaxDiscountVal() == null ? 0.0D : getMaxDiscountVal().doubleValue();
				final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
						MarketplacecommerceservicesConstants.CHANNEL);
				//final CartModel cartModel = getCartService().getSessionCart();
				//checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
				//final AbstractOrder order = arg1.getOrder();
				//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
				final AbstractOrder order = arg1.getOrder();

				checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, order);
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());//Adding restrictions to List
				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForOrderPromo(restrictionList);
				//for payment mode restriction check
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList, arg0);

				if (checkRestrictions(arg0, arg1) && checkChannelFlag && flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval)
				{
					boolean isPercentageDisc = false;
					double percentageDiscount = getPercentageDiscount() == null ? 0.0D : getPercentageDiscount().doubleValue();
					final Double threshold = getPriceForOrder(arg0, getThresholdTotals(arg0), arg1.getOrder(),
							MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
					if (threshold != null)
					{

						//CR Changes : TPR-715
						double orderSubtotalAfterDiscounts = 0.0D;
						arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.FALSE);
						arg0.setAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS, validProductUssidMap);
						if (getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
						{
							validProductUssidMap = getMplPromotionHelper().getCartSellerEligibleProducts(arg0, order, restrictionList);
							arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.TRUE);
							orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(arg0, validProductUssidMap);
							setSellersubTotalValue(orderSubtotalAfterDiscounts);
						}
						else if (getDefaultPromotionsManager().isExSellerRestrExists(restrictionList))
						{
							validProductUssidMap = getMplPromotionHelper().getCartSellerInEligibleProducts(arg0, order, restrictionList);
							arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDATE_SELLER, Boolean.TRUE);
							orderSubtotalAfterDiscounts = getSellerSpecificSubtotal(arg0, validProductUssidMap);
							setSellersubTotalValue(orderSubtotalAfterDiscounts);
						}
						else
						{
							orderSubtotalAfterDiscounts = getSubtotalAfterDiscount(arg0, order);
						}
						//CR Changes : TPR-715 ends

						if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
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
								final Double discountPrice = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
										MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
								final double discountPriceValue = (discountPrice == null) ? 0.0D : discountPrice.doubleValue();
								percentageDiscount = (discountPriceValue * 100) / orderSubtotalAfterDiscounts;
							}

							if (percentageDiscount < 100)
							{
								arg0.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
										Double.valueOf(percentageDiscount));
								arg0.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
										Double.valueOf(orderSubtotalAfterDiscounts));
								arg0.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
								arg0.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC,
										Boolean.valueOf(isPercentageDisc));

								//CR Changes : TPR-715
								if (MapUtils.isNotEmpty(validProductUssidMap))
								{
									arg0.setAttribute(MarketplacecommerceservicesConstants.CART_SELLER_PRODUCTS, validProductUssidMap);
								}
								//CR Changes : TPR-715 ends

								//adjustedDiscounts = (percentageDiscount * orderSubtotalAfterDiscounts) / 100;
								setAdjustedDiscounts((percentageDiscount * orderSubtotalAfterDiscounts) / 100);
								final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
										arg1.getOrder(), 1.0F);
								result.addAction(
										arg0,
										getDefaultPromotionsManager().createCustomPromotionOrderAdjustTotalAction(arg0,
												-getAdjustedDiscounts()));
								promotionResults.add(result);
							}
						}
						else if (orderSubtotalAfterDiscounts > 0.0D)
						{
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
	 * This method is used for Localization Purpose
	 *
	 * @param: ctx
	 * @param: result
	 * @param: locale
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
					final double orderSubtotalAfterDiscounts = getEligibleSubtotal(order);
					final double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;

					final Object[] args =
					{ threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
							Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired) };
					return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
				}
			}
		}

		return MarketplacecommerceservicesConstants.EMPTYSTRING;

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
			if (null != order && null != order.getEntries())
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
