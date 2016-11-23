package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


public class LimitedStockPromotion extends GeneratedLimitedStockPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LimitedStockPromotion.class.getName());
	private int noOfProducts = 0;
	private boolean flagForCouldFireMessage = true;

	/**
	 * @return the flagForCouldFireMessage
	 */
	public boolean isFlagForCouldFireMessage()
	{
		return flagForCouldFireMessage;
	}


	/**
	 * @param flagForCouldFireMessage
	 *           the flagForCouldFireMessage to set
	 */
	public void setFlagForCouldFireMessage(final boolean flagForCouldFireMessage)
	{
		this.flagForCouldFireMessage = flagForCouldFireMessage;
	}

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
	 * @return the noOfProducts
	 */
	public int getNoOfProducts()
	{
		return noOfProducts;
	}


	/**
	 * @param noOfProducts
	 *           the noOfProducts to set
	 */
	public void setNoOfProducts(final int noOfProducts)
	{
		this.noOfProducts = noOfProducts;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotion#evaluate(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.result.PromotionEvaluationContext)
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
		List<String> excludeManufactureList = null;
		List<Product> excludedProductList = null;
		boolean checkChannelFlag = false;
		boolean isMultipleSeller = false;
		final List<Product> promotionProductList = new ArrayList<>(getProducts()); //Adding of Promotion Added Products to List
		final List<Category> promotionCategoryList = new ArrayList<>(getCategories());//Adding of Promotion Added Categories to List
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final AbstractOrder order = paramPromotionEvaluationContext.getOrder();

		excludedProductList = new ArrayList<Product>();
		excludeManufactureList = new ArrayList<String>();

		GenericUtilityMethods.populateExcludedProductManufacturerList(paramSessionContext, paramPromotionEvaluationContext,
				excludedProductList, excludeManufactureList, restrictionList, this);
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			//changes Start for omni cart fix
			final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			if (checkChannelFlag)
			{
				final Map<String, List<String>> productAssociatedItemsFinalMap = new ConcurrentHashMap<String, List<String>>();

				final Map<String, Integer> validProductFinalList = new ConcurrentHashMap<String, Integer>();

				final Map<String, AbstractOrderEntry> validProductUssidFinalMap = new ConcurrentHashMap<String, AbstractOrderEntry>();

				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidEntriesForStockLevelPromo(order, paramSessionContext, promotionProductList, promotionCategoryList,
								restrictionList, excludedProductList, excludeManufactureList, getMaxStockCount().intValue(), getCode());
				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap))
				{
					isMultipleSeller = getMplPromotionHelper().checkMultipleSeller(restrictionList);

					if (isMultipleSeller)
					{
						Map<AbstractOrderEntry, String> productSellerDetails = new HashMap<AbstractOrderEntry, String>();
						Map<String, Map<String, AbstractOrderEntry>> multiSellerValidUSSIDMap = new HashMap<String, Map<String, AbstractOrderEntry>>();
						productSellerDetails = getMplPromotionHelper().populateSellerSpecificData(paramSessionContext, restrictionList,
								validProductUssidMap);


						if (MapUtils.isNotEmpty(productSellerDetails))
						{
							multiSellerValidUSSIDMap = getMplPromotionHelper().populateMultiSellerData(validProductUssidMap,
									productSellerDetails, paramSessionContext);
							if (null != multiSellerValidUSSIDMap && !multiSellerValidUSSIDMap.isEmpty())
							{
								for (final Map.Entry<String, Map<String, AbstractOrderEntry>> multiSellerData : multiSellerValidUSSIDMap
										.entrySet())
								{
									final Map<String, AbstractOrderEntry> validMultiUssidMap = multiSellerData.getValue();
									if (null != validProductUssidMap && !validProductUssidMap.isEmpty())
									{
										List<PromotionResult> promotionResultList = new ArrayList<PromotionResult>();
										promotionResultList = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext,
												validMultiUssidMap, restrictionList, promotionProductList, promotionCategoryList, order,
												productAssociatedItemsFinalMap, validProductFinalList, validProductUssidFinalMap);
										if (null != promotionResultList && !promotionResultList.isEmpty())
										{
											promotionResults.addAll(promotionResultList);
										}
									}
								}
							}
						}
					}
					else
					{
						promotionResults = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext,
								validProductUssidMap, restrictionList, promotionProductList, promotionCategoryList, order,
								productAssociatedItemsFinalMap, validProductFinalList, validProductUssidFinalMap);
					}
				}

				//Setting values
				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidFinalMap);
				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductFinalList);
				paramSessionContext
						.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsFinalMap);

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
	 * Gets Total Count for Promotion Message
	 *
	 * @param validProductUssidMap
	 * @return totalCount
	 */
	private int populateTotalProductCount(final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		int totalCount = 0;
		if (MapUtils.isNotEmpty(validProductUssidMap))
		{
			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
			}
		}

		return totalCount;
	}

	/**
	 * @Description Promotion Evaluation Method
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final AbstractOrder order,
			final Map<String, List<String>> productAssociatedItemsFinalMap, final Map<String, Integer> validProductFinalList,
			final Map<String, AbstractOrderEntry> validProductUssidFinalMap)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager promotionsManager = PromotionsManager.getInstance();
		final int eligibleQuantity = getMaxStockCount().intValue();
		int totalCount = 0;
		try
		{
			noOfProducts = populateTotalProductCount(validProductUssidMap);

			boolean flagForDeliveryModeRestrEval = false;
			boolean flagForPaymentModeRestrEval = false;
			final boolean flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(restrictionList,
					order);
			boolean isPercentageDisc = false;
			final double maxDiscount = getMaxDiscountVal() == null ? 0.0D : getMaxDiscountVal().doubleValue(); //Adding the Promotion set Max  Discount Value to a variable
			final Double discountPrice = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext), order,
					MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null ? (Double) getPriceForOrder(paramSessionContext,
					getDiscountPrices(paramSessionContext), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES) : new Double(
					0.0);

			//getting eligible Product List
			final List<Product> eligibleProductList = new ArrayList<Product>();
			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
				eligibleProductList.add(entry.getProduct());
			}
			noOfProducts = totalCount;

			List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;
			final PromotionOrderView view = paramPromotionEvaluationContext.createView(paramSessionContext, this,
					eligibleProductList);

			final Map<String, Integer> tcMapForValidEntries = new ConcurrentHashMap<String, Integer>();
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
			}

			//			if (totalCount >= eligibleQuantity)
			//			{
			double percentageDiscount = getPercentageDiscount() == null ? 0.0D : getPercentageDiscount().doubleValue();

			//Promotion Value Calculations
			final int totalFactorCount = totalCount;

			final Map<String, Integer> validProductList = getDefaultPromotionsManager().getProductUssidMapForStockPromo(
					validProductUssidMap, eligibleQuantity, paramSessionContext, restrictionList, getCode());

			validProductFinalList.putAll(validProductList);
			validProductUssidFinalMap.putAll(validProductUssidMap);

			if (!isPercentageOrAmount().booleanValue())
			{
				//					flagForCouldFireMessage = getDefaultPromotionsManager().getValidProductListForAmtDiscount(paramSessionContext,
				//							order, promotionProductList, promotionCategoryList, eligibleQuantity, discountPrice, validProductUssidMap);
			}

			//for delivery mode restriction check
			flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
					validProductUssidMap, order);

			//for payment mode restriction check
			flagForPaymentModeRestrEval = getDefaultPromotionsManager()
					.getPaymentModeRestrEval(restrictionList, paramSessionContext);

			if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval && flagForPincodeRestriction) // If Total no of valid Products exceeds Qualifying Count
			{
				int totalValidProdCount = 0;
				for (final String key : validProductList.keySet())
				{
					totalValidProdCount += validProductList.get(key).intValue(); // Fetches total count of Valid Products
				}

				final double totalPricevalue = getDefaultPromotionsManager().getTotalValidProdPrice(validProductUssidMap,
						validProductList);

				if (!isPercentageOrAmount().booleanValue())
				{
					percentageDiscount = getPercentageDiscountForStockPromo(totalValidProdCount, discountPrice.doubleValue(),
							totalPricevalue);
					//					percentageDiscount = getDefaultPromotionsManager().getConvertedPercentageDiscount(totalValidProdCount,
					//							discountPrice.doubleValue(), Long.valueOf(eligibleQuantity), totalPricevalue);
				}
				else
				{
					final double totalSavings = (totalPricevalue * percentageDiscount) / 100;
					final double totalMaxDiscount = totalFactorCount * maxDiscount;
					if (totalSavings > totalMaxDiscount && totalMaxDiscount != 0)
					{
						percentageDiscount = (totalMaxDiscount * 100) / totalPricevalue;
					}
					else
					{
						isPercentageDisc = true;
					}
				}

				final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
						.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, null);

				productAssociatedItemsFinalMap.putAll(productAssociatedItemsMap);

				// Apportioning Code Implementation
				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
						Double.valueOf(percentageDiscount));
				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
						Double.valueOf(totalPricevalue));

				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
				paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC,
						Boolean.valueOf(isPercentageDisc));

				final Currency currency = paramPromotionEvaluationContext.getOrder().getCurrency(paramSessionContext);

				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
				{
					//paramPromotionEvaluationContext.startLoggingConsumed(this);
					final AbstractOrderEntry entry = mapEntry.getValue();
					final String validUssid = mapEntry.getKey();
					final long quantityOfOrderEntry = entry.getQuantity(paramSessionContext).longValue();

					final double percentageDiscountvalue = percentageDiscount / 100.0D;

					if (percentageDiscount < 100)
					{
						final int eligibleCount = validProductList.get(validUssid).intValue();
						final double originalUnitPrice = entry.getBasePrice(paramSessionContext).doubleValue();
						final double originalEntryPrice = eligibleCount * originalUnitPrice;

						final BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(paramSessionContext, currency,
								originalEntryPrice - (originalEntryPrice * percentageDiscountvalue));

						final BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(
								paramSessionContext,
								currency,
								(adjustedEntryPrice.equals(BigDecimal.ZERO)) ? BigDecimal.ZERO : adjustedEntryPrice.divide(
										BigDecimal.valueOf(eligibleCount), RoundingMode.HALF_EVEN));

						final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
						consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount, eligibleCount,
								entry));

						tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));

						for (final PromotionOrderEntryConsumed poec : consumed)
						{
							poec.setAdjustedUnitPrice(paramSessionContext, adjustedUnitPrice.doubleValue());
						}

						final BigDecimal adjustment = Helper.roundCurrencyValue(paramSessionContext, currency,
								adjustedEntryPrice.subtract(BigDecimal.valueOf(originalEntryPrice)));
						//final double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue * eligibleCount);

						final PromotionResult result = promotionsManager.createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 1.0F);
						final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
								.createCustomPromotionOrderEntryAdjustAction(paramSessionContext, entry, quantityOfOrderEntry,
										adjustment.doubleValue());
						//final List consumed = paramPromotionEvaluationContext.finishLoggingAndGetConsumed(this, true);
						result.setConsumedEntries(paramSessionContext, consumed);
						result.addAction(paramSessionContext, poeac);
						promotionResults.add(result);
					}
				}
			}

			//}

			//Setting remaining items
			remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
					view.getAllEntries(paramSessionContext), tcMapForValidEntries);

			if (LOG.isDebugEnabled()) // For Localization
			{
				LOG.debug("(" + getPK() + ")" + Localization.getLocalizedString("promotion.productQuantity.message") + totalCount
						+ ">" + eligibleQuantity + Localization.getLocalizedString("promotion.freeGiftAction"));
			}

			if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L) // For Localization: To check for Excluded Products
			{
				final float certainty = (float) remainingItemsFromTail.size() / eligibleQuantity;

				if (certainty < 1.0F && certainty > 0.0F)
				{
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
							paramPromotionEvaluationContext.getOrder(), certainty);
					result.setConsumedEntries(remainingItemsFromTail);
					promotionResults.add(result);
				}

			}
			//	}
			else if (noOfProducts > 0)
			{
				//certainty check
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
						paramPromotionEvaluationContext.getOrder(), 0.00F);
				promotionResults.add(result);
			}

		}
		catch (final JaloInvalidParameterException exception)
		{
			LOG.error(exception.getMessage());
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
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
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{
		// YTODO Auto-generated method stub
		return null;
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

	public double getPercentageDiscountForStockPromo(final int totalCount, final double discountPriceValue,
			final double totalPricevalue)
	{
		final int totalFactorCount = totalCount;
		final double totalDiscount = totalFactorCount * discountPriceValue;
		final double percentageDiscount = (totalDiscount * 100) / totalPricevalue;

		return percentageDiscount;
	}

}
