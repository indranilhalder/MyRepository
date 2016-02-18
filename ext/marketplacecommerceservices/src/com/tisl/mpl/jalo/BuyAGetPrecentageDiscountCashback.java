package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryAdjustAction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


public class BuyAGetPrecentageDiscountCashback extends GeneratedBuyAGetPrecentageDiscountCashback
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAGetPrecentageDiscountCashback.class.getName());
	//private List<Product> excludedProductList = null;
	//private List<String> excludeManufactureList = null;
	private double totalCashback = 0.0D;
	//private double totalPricevalue;
	private int noOfProducts = 0;

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
	 * @Description : Buy Product A get Percentage or Amount Discount cashback
	 * @param : SessionContext paramSessionContext
	 * @param : PromotionEvaluationContext paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext evaluationContext)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final AbstractOrder order = evaluationContext.getOrder();
		final List<Product> promotionProductList = new ArrayList<>(getProducts());
		final List<Category> promotionCategoryList = new ArrayList<>(getCategories());
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions()); // Fetching Promotion set Restrictions
		final List<Product> excludedProductList = new ArrayList<Product>();
		final List<String> excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(paramSessionContext, evaluationContext, excludedProductList,
				excludeManufactureList, restrictionList, this);
		// To check whether Promotion already applied on Product
		//getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, order, excludedProductList);
		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(paramSessionContext, evaluationContext);

		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			//final boolean checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = evaluationContext.getOrder();
			final boolean checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram
			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag) // if Restrictions return valid && Channel is valid
			{
				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofAPromo(order, paramSessionContext, promotionProductList, promotionCategoryList,
								restrictionList, excludedProductList, excludeManufactureList, null, null); // Adding Eligible Products to List

				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap))
				{
					promotionResults = promotionEvaluation(paramSessionContext, evaluationContext, validProductUssidMap,
							restrictionList);
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
	 * @Description : Promotion Evaluation Method
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext evaluationContext, final Map<String, AbstractOrderEntry> validProductUssidMap,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<Product> promoProductList = new ArrayList<Product>();
		final AbstractOrder order = evaluationContext.getOrder();
		int totalCount = 0;
		final Long eligibleQuantity = getQuantity();
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;
		final double maxDiscount = getMaxDiscount() == null ? 0.0D : getMaxDiscount().doubleValue();

		if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, paramSessionContext, evaluationContext,
				this, restrictionList))
		{
			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
			}
			noOfProducts = totalCount;

			List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;

			//getting eligible Product List
			final List<Product> eligibleProductList = new ArrayList<Product>();
			for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
			{
				eligibleProductList.add(orderEntry.getProduct());
			}

			final PromotionOrderView view = evaluationContext.createView(paramSessionContext, this, eligibleProductList);
			//final PromotionOrderEntry viewEntry = view.peek(paramSessionContext);

			final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
			}

			if (totalCount >= eligibleQuantity.intValue())
			{
				double percentageDiscount = getPercentageDiscount().doubleValue();

				//Promotion Value Calculations
				final int totalFactorCount = totalCount / eligibleQuantity.intValue();
				final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
						validProductUssidMap, totalCount, eligibleQuantity.longValue(), paramSessionContext, restrictionList);

				int totalValidProdCount = 0;
				for (final String key : validProductList.keySet())
				{
					totalValidProdCount += validProductList.get(key).intValue(); // Fetches total count of Valid Products
				}
				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
						validProductUssidMap);
				//for payment mode restriction check
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						paramSessionContext);

				if (flagForPaymentModeRestrEval && flagForDeliveryModeRestrEval) // If Total no of valid Products exceeds Qualifying Count
				{
					final Double discountPrice = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext), order,
							MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null ? (Double) getPriceForOrder(
							paramSessionContext, getDiscountPrices(paramSessionContext), order,
							MarketplacecommerceservicesConstants.DISCOUNT_PRICES) : new Double(0.0);
					final double totalPricevalue = getDefaultPromotionsManager().getTotalValidProdPrice(validProductUssidMap,
							validProductList);
					final double percentageDiscountForAmount = getDefaultPromotionsManager().getConvertedPercentageDiscount(
							totalValidProdCount, discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);

					if (!isPercentageOrAmount().booleanValue())
					{
						percentageDiscount = percentageDiscountForAmount; //Getting the amount discount
					}
					else
					{
						final double totalSavings = totalPricevalue * (totalFactorCount * percentageDiscount / 100);
						final double totalMaxDiscount = totalFactorCount * maxDiscount;
						if (totalSavings > totalMaxDiscount && totalMaxDiscount != 0)
						{
							percentageDiscount = (totalMaxDiscount * 100) / totalPricevalue;
						}
					}

					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						//evaluationContext.startLoggingConsumed(this);
						totalCashback = 0.0D;
						final AbstractOrderEntry entry = mapEntry.getValue();
						final String validUssid = mapEntry.getKey();
						final long quantityOfOrderEntry = entry.getQuantity(paramSessionContext).longValue();

						final double percentageDiscountvalue = percentageDiscount / 100.0D;
						if (percentageDiscount < 100)
						{
							//total adjustment to be deducted from total price
							final int eligibleCount = validProductList.get(validUssid).intValue();

							final double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue * eligibleCount);
							totalCashback += adjustment;

							final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
							consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount, eligibleCount,
									entry));

							tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));


							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext,
									this, evaluationContext.getOrder(), 1.0F);
							final PromotionOrderEntryAdjustAction poeac = PromotionsManager.getInstance()
									.createPromotionOrderEntryAdjustAction(paramSessionContext, entry, 0.0D);//no adjust requirement in case of bank cashback
							//final List consumed = evaluationContext.finishLoggingAndGetConsumed(this, true);
							result.setConsumedEntries(paramSessionContext, consumed);
							result.addAction(paramSessionContext, poeac);
							promoProductList.add(entry.getProduct());
							promotionResults.add(result);
						}

					}
					//if (promoProductList.size() > 0) // To check that another Product Promotion does not apply on the product
					if (CollectionUtils.isNotEmpty(promoProductList))
					{
						getPromotionUtilityPOJO().setPromoProductList(promoProductList);
					}
				}
			}

			//Setting remaining items
			remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
					view.getAllEntries(paramSessionContext), tcMapForValidEntries);

			if (LOG.isDebugEnabled()) // For Localization
			{
				LOG.debug("(" + getPK() + ")" + Localization.getLocalizedString("promotion.productQuantity.message") + totalCount
						+ ">" + eligibleQuantity.intValue() + Localization.getLocalizedString("promotion.freeGiftAction"));
			}

			if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L) // For Localization: To check for Excluded Products
			{
				//				final float certainty = remainingItemsFromTail != null ? (remainingItemsFromTail.isEmpty() ? 1.00F
				//						: (float) remainingItemsFromTail.size() / eligibleQuantity.intValue()) : 0.00F;

				final float certainty = (float) remainingItemsFromTail.size() / eligibleQuantity.intValue();

				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
						evaluationContext.getOrder(), certainty);
				result.setConsumedEntries(remainingItemsFromTail);
				promotionResults.add(result);
			}

		}
		else
		{
			//certainty check
			final float certainty = 0.00F;
			final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
					evaluationContext.getOrder(), certainty);
			promotionResults.add(result);
		}

		return promotionResults;
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext paramSessionContext
	 * @param : PromotionResult paramPromotionResult
	 * @param : Locale paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext paramSessionContext, final PromotionResult paramPromotionResult,
			final Locale paramLocale)
	{
		final AbstractOrder order = paramPromotionResult.getOrder(paramSessionContext);
		if (order != null)
		{
			final Currency orderCurrency = order.getCurrency(paramSessionContext);
			final Double percentageDiscount = getPercentageDiscount(paramSessionContext);
			final Double totalDiscount = Double.valueOf(totalCashback * -1.0D);
			final Long quantity = getQuantity(paramSessionContext);

			if (paramPromotionResult.getFired(paramSessionContext))
			{
				if (isPercentageOrAmount().booleanValue())
				{
					final Object[] args =
					{ percentageDiscount, totalDiscount,
							Helper.formatCurrencyAmount(paramSessionContext, paramLocale, orderCurrency, totalDiscount.doubleValue()),
							MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE };
					return formatMessage(getMessageFired(paramSessionContext), args, paramLocale);
				}

				else
				{
					final Double discountPriceValue = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext),
							order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES);

					final Object[] args =
					{ discountPriceValue, totalDiscount,
							Helper.formatCurrencyAmount(paramSessionContext, paramLocale, orderCurrency, totalDiscount.doubleValue()),
							MarketplacecommerceservicesConstants.DISCOUNT_PRICES_MESSAGE };
					return formatMessage(getMessageFired(paramSessionContext), args, paramLocale);

				}
			}
			if (paramPromotionResult.getCouldFire(paramSessionContext))
			{
				final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
						getRestrictions());
				String paymentModes = "";
				String deliveryModes = "";
				if (!restrictionList.isEmpty())
				{
					for (final AbstractPromotionRestriction restriction : restrictionList)
					{

						if (restriction instanceof DeliveryModePromotionRestriction)
						{
							final List<DeliveryMode> deliveryModeList = ((DeliveryModePromotionRestriction) restriction)
									.getDeliveryModeDetailsList();
							if (deliveryModeList.size() == 1)
							{
								deliveryModes = deliveryModes + deliveryModeList.get(0).getName();
							}
							else
							{
								for (final DeliveryMode deliveryMode : deliveryModeList)
								{
									deliveryModes = deliveryModes + deliveryMode.getName() + ", ";
								}
							}
							deliveryModes += " Modes";
						}
						else if (restriction instanceof PaymentModeSpecificPromotionRestriction)
						{
							final List<PaymentType> paymentModeList = ((PaymentModeSpecificPromotionRestriction) restriction)
									.getPaymentModes();
							if (paymentModeList.size() == 1)
							{
								deliveryModes = deliveryModes + paymentModeList.get(0).getMode();
							}
							else
							{
								for (final PaymentType paymentMode : paymentModeList)
								{
									paymentModes = paymentModes + paymentMode.getMode() + ", ";
								}
							}
							paymentModes += " Modes";
						}
					}
				}

				final Object[] args = new Object[6];
				args[0] = Long.valueOf(getQuantity(paramSessionContext).longValue()
						- paramPromotionResult.getConsumedCount(paramSessionContext, true));
				args[1] = quantity;
				args[2] = MarketplacecommerceservicesConstants.EMPTYSPACE;

				if (!deliveryModes.isEmpty())
				{
					args[3] = deliveryModes;
				}
				else if (!paymentModes.isEmpty())
				{
					args[3] = paymentModes;
				}
				else
				{
					args[3] = "purchase";
				}

				return formatMessage(getMessageCouldHaveFired(paramSessionContext), args, paramLocale);
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

	protected PromotionUtilityPOJO getPromotionUtilityPOJO()
	{
		return Registry.getApplicationContext().getBean("promotionUtilityPOJO", PromotionUtilityPOJO.class);
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}
}
