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
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionQuantityAndPricesRow;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplBundlePromotionHelper;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


@SuppressWarnings("deprecation")
public class MplProductSteppedMultiBuyPromotion extends GeneratedMplProductSteppedMultiBuyPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplProductSteppedMultiBuyPromotion.class.getName());

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
	 * @Description : Multiple Buy Promotions (Bundle)
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @return promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		LOG.debug("Inside Multi Step Bundle Promotion");

		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<String> excludeManufactureList = new ArrayList<String>();
		final List<Product> excludedProductList = new ArrayList<Product>();


		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List


		boolean checkChannelFlag = false;
		final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();


		GenericUtilityMethods.populateExcludedProductManufacturerList(paramSessionContext, paramPromotionEvaluationContext,
				excludedProductList, excludeManufactureList, restrictionList, this);

		try
		{
			final List<Product> promotionProductList = new ArrayList<>(getProducts()); //Adding of Promotion Added Products to List
			final List<Category> promotionCategoryList = new ArrayList<>(getCategories());//Adding of Promotion Added Categories to List
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			if (checkChannelFlag)
			{
				//******************** Block Code ****************************************************************************
				//final Map<String, List<String>> productAssociatedItemsFinalMap = new ConcurrentHashMap<String, List<String>>();
				//final Map<String, Integer> validProductFinalList = new ConcurrentHashMap<String, Integer>();

				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofAPromo(cart, paramSessionContext, promotionProductList, promotionCategoryList,
								restrictionList, excludedProductList, excludeManufactureList, null, null);
				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap)
						&& MapUtils.isNotEmpty(validProductUssidMap) /* && validProductUssidMap.size() == 1 */) // For One Eligible line for Promotion in cart
				{
					promotionResults = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext, validProductUssidMap,
							restrictionList, promotionProductList, promotionCategoryList, cart);
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

	//	/**
	//	 * Method to evaluate for Multi Lines in Cart
	//	 *
	//	 * @param paramSessionContext
	//	 * @param paramPromotionEvaluationContext
	//	 * @param validProductUssidMap
	//	 * @param restrictionList
	//	 * @param promotionProductList
	//	 * @param promotionCategoryList
	//	 * @param cart
	//	 * @return promotionResults
	//	 */
	//	private List<PromotionResult> promotionEvaluationMultiLine(final SessionContext paramSessionContext,
	//			final PromotionEvaluationContext paramPromotionEvaluationContext,
	//			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
	//			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final AbstractOrder cart)
	//	{
	//		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
	//		try
	//		{
	//			LOG.debug("Evaluting  Multi Step Bundle Promotion for Multi Line");
	//			final List<QuantityPrice> steps = getSteps(paramSessionContext, cart,
	//					getQualifyingCountsAndBundlePrices(paramSessionContext));
	//			if (CollectionUtils.isNotEmpty(steps))
	//			{
	//				boolean flagForDeliveryModeRestrEval = false;
	//				boolean flagForPaymentModeRestrEval = false;
	//				boolean flagForPincodeRestriction = false;
	//				long promotionEligibleCount = 0;
	//				long totalCount = 0;
	//				double totalAdjustOffPrice = 0.0D;
	//				double promoSetTotalDiscount = 0.0D;
	//				double totalAdjustment = 0.0D;
	//				final List<Long> stepQuantityList = new ArrayList<Long>();
	//
	//				LOG.debug("Validated Steps for Bundle Promotions");
	//				LOG.debug("Checking Delivery Mode Restriction ");
	//				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
	//						validProductUssidMap, cart);
	//				LOG.debug("Checking Payment Mode Restriction && Pincode Restriction ");
	//				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
	//						paramSessionContext);
	//				flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(restrictionList, cart);
	//
	//				if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval && flagForPincodeRestriction)
	//				{
	//					final List<Product> eligibleProductList = new ArrayList<Product>();
	//					for (final AbstractOrderEntry entry : validProductUssidMap.values())
	//					{
	//						totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
	//						eligibleProductList.add(entry.getProduct());
	//					}
	//					LOG.debug("Total Eligible Count of Products" + totalCount);
	//
	//					if (totalCount > 0)
	//					{
	//						LOG.debug("Assignining Total Count for Promotion Evaluation");
	//						promotionEligibleCount = totalCount;
	//					}
	//
	//
	//					for (final QuantityPrice priceData : steps)
	//					{
	//						while (promotionEligibleCount > 0 && priceData.quantity <= promotionEligibleCount)
	//						{
	//							totalAdjustOffPrice += priceData.price;
	//							promotionEligibleCount = promotionEligibleCount - priceData.quantity;
	//							promoSetTotalDiscount += priceData.price;
	//
	//							if (CollectionUtils.isEmpty(stepQuantityList)
	//									|| !(stepQuantityList.contains(Long.valueOf(priceData.quantity))))
	//							{
	//								stepQuantityList.add(Long.valueOf(priceData.quantity));
	//							}
	//						}
	//					}
	//					LOG.debug("Promotion Set Total Off" + promoSetTotalDiscount);
	//
	//					final Map<String, Integer> validProductList = getMplBundlePromotionHelper().getSortedValidProdUssidMap(
	//							validProductUssidMap, stepQuantityList, paramSessionContext, (int) totalCount, restrictionList);
	//
	//					totalAdjustment = getMplBundlePromotionHelper().getTotalEligiblePrice(validProductUssidMap, validProductList)
	//							- totalAdjustOffPrice;
	//
	//					if (totalAdjustment > 0)
	//					{
	//						//
	//					}
	//
	//
	//
	//				}
	//			}
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			LOG.error("Error in Multi Stepped Promotion Evaluation + Multi Step" + e);
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e) //Added for TISPT-195
	//		{
	//			LOG.error("Error in Multi Stepped Promotion Evaluation + Multi Step" + e);
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
	//					MarketplacecommerceservicesConstants.E0000));
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error("Error in Multi Stepped Promotion Evaluation + Multi Step" + e);
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
	//					MarketplacecommerceservicesConstants.E0000));
	//		}
	//		return promotionResults;
	//	}

	/**
	 *
	 * Bundle Promotion Evaluation
	 *
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @param restrictionList
	 * @param promotionProductList
	 * @param promotionCategoryList
	 * @param cart
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final AbstractOrder cart)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		try
		{
			LOG.debug("Evaluting  Multi Step Bundle Promotion");
			final List<QuantityPrice> steps = getSteps(paramSessionContext, cart,
					getQualifyingCountsAndBundlePrices(paramSessionContext));
			if (CollectionUtils.isNotEmpty(steps))
			{
				long totalCount = 0;
				long promotionEligibleCount = 0;
				boolean flagForDeliveryModeRestrEval = false;
				boolean flagForPaymentModeRestrEval = false;
				boolean flagForPincodeRestriction = false;
				double totalAdjustment = 0.0D;
				double totalPrice = 0.0D;
				final List<Long> stepQuantityList = new ArrayList<Long>();
				long promotionConsmdCount = 0;
				double promoSetTotalDiscount = 0.0D;
				List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;
				double totalAdjustOffPrice = 0.0D;

				LOG.debug("Validated Steps for Bundle Promotions");
				LOG.debug("Checking Delivery Mode Restriction ");
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
						validProductUssidMap, cart);
				LOG.debug("Checking Payment Mode Restriction && Pincode Restriction ");
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						paramSessionContext);
				flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(restrictionList, cart);

				if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval && flagForPincodeRestriction)
				{
					final List<Product> eligibleProductList = new ArrayList<Product>();
					for (final AbstractOrderEntry entry : validProductUssidMap.values())
					{
						totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
						eligibleProductList.add(entry.getProduct());
						totalPrice += entry.getTotalPrice().doubleValue();
					}
					LOG.debug("Total Eligible Count of Products" + totalCount);

					if (totalCount > 0)
					{
						LOG.debug("Assignining Total Count for Promotion Evaluation");
						promotionEligibleCount = totalCount;
					}

					LOG.debug("*************Setting Promotion Consumed Data***************************");
					final PromotionOrderView view = paramPromotionEvaluationContext.createView(paramSessionContext, this,
							eligibleProductList);

					final Map<String, Integer> tcMapForValidEntries = new ConcurrentHashMap<String, Integer>();
					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
					}
					LOG.debug("*************Setting Promotion Consumed Data ends***************************");

					for (final QuantityPrice priceData : steps)
					{
						while (promotionEligibleCount > 0 && priceData.quantity <= promotionEligibleCount)
						{
							totalAdjustOffPrice += priceData.price;
							promotionEligibleCount = promotionEligibleCount - priceData.quantity;
							promoSetTotalDiscount += priceData.price;

							if (CollectionUtils.isEmpty(stepQuantityList)
									|| !(stepQuantityList.contains(Long.valueOf(priceData.quantity))))
							{
								stepQuantityList.add(Long.valueOf(priceData.quantity));
							}
						}
					}
					//totalAdjustment = totalPrice - totalAdjustOffPrice;



					final Map<String, Integer> validProductList = getMplBundlePromotionHelper().getSortedValidProdUssidMap(
							validProductUssidMap, stepQuantityList, paramSessionContext, (int) totalCount, restrictionList, getCode());

					totalAdjustment = getMplBundlePromotionHelper().getTotalEligiblePrice(validProductUssidMap, validProductList)
							- totalAdjustOffPrice;

					LOG.debug("Previous Cart Value" + totalPrice);
					LOG.debug("Total Adjustment Price" + totalAdjustOffPrice);
					LOG.debug("Total Post Discount" + (totalPrice - totalAdjustment));
					LOG.debug("Total Promotion Discount" + promoSetTotalDiscount);

					if (totalAdjustment > 0)
					{
						LOG.debug("Fired Promotion Scenario");
						promotionConsmdCount = totalCount - promotionEligibleCount;
						if (promotionConsmdCount > 0)
						{
							final BigDecimal unitAdjustment = BigDecimal.valueOf(totalAdjustment).divide(
									BigDecimal.valueOf(promotionConsmdCount), RoundingMode.HALF_EVEN);
							LOG.debug("Unit Adjustment: " + unitAdjustment);

							LOG.debug("Converting the Price off to Percentage: ");
							final double percentageDiscount = getMplBundlePromotionHelper().getDiscountPercentage(validProductList,
									validProductUssidMap, totalAdjustment);

							LOG.debug("Percentage Discount : " + percentageDiscount);

							// Apportioning Code Implementation

							LOG.debug("Apportioning Implementation");


							paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
									Double.valueOf(percentageDiscount));

							//							paramSessionContext.setAttribute(
							//									MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
							//									Double.valueOf(getMplBundlePromotionHelper().getTotalEligiblePrice(validProductUssidMap,
							//											validProductList)));

							paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE,
									String.valueOf(this.getCode()));
							paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.FALSE);
							//							paramSessionContext
							//									.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);

							if (MapUtils.isNotEmpty(validProductList))
							{
								paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductList);
							}

							// Apportioning Code Implementation ends

							final Currency currency = paramPromotionEvaluationContext.getOrder().getCurrency(paramSessionContext);
							for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
							{
								final AbstractOrderEntry entry = mapEntry.getValue();
								final long quantityOfOrderEntry = entry.getQuantity(paramSessionContext).longValue();
								final double percentageDiscountvalue = percentageDiscount / 100.0D;
								final String validUssid = mapEntry.getKey();

								if (percentageDiscountvalue < 100)
								{

									final int eligibleCount = validProductList.get(validUssid).intValue();
									final double originalUnitPrice = entry.getBasePrice(paramSessionContext).doubleValue();
									final double originalEntryPrice = eligibleCount * originalUnitPrice;

									final BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(paramSessionContext, currency,
											originalEntryPrice - (originalEntryPrice * percentageDiscountvalue));

									final BigDecimal adjustment = Helper.roundCurrencyValue(paramSessionContext, currency,
											adjustedEntryPrice.subtract(BigDecimal.valueOf(originalEntryPrice)));

									LOG.debug("*************Setting Promotion Consumed Data Per Entry***************************");
									final BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(
											paramSessionContext,
											currency,
											(adjustedEntryPrice.equals(BigDecimal.ZERO)) ? BigDecimal.ZERO : adjustedEntryPrice.divide(
													BigDecimal.valueOf(eligibleCount), RoundingMode.HALF_EVEN));

									final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
									consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount,
											eligibleCount, entry));

									tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));

									for (final PromotionOrderEntryConsumed poec : consumed)
									{
										poec.setAdjustedUnitPrice(paramSessionContext, adjustedUnitPrice.doubleValue());
									}


									LOG.debug("*************Setting Promotion Consumed Data Per Entry Ends***************************");

									final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(
											paramSessionContext, this, paramPromotionEvaluationContext.getOrder(), 1.0F);
									final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
											.createCustomPromotionOrderEntryAdjustAction(paramSessionContext, entry, quantityOfOrderEntry,
													adjustment.doubleValue());
									result.setConsumedEntries(paramSessionContext, consumed);
									result.addAction(paramSessionContext, poeac);
									promotionResults.add(result);
								}
							}
						}
					}


					remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
							view.getAllEntries(paramSessionContext), tcMapForValidEntries);

					LOG.debug("Evaluating Potential Promotion Scenario");
					if (promotionEligibleCount > 0)
					{
						LOG.debug("Potential Promotion Scenario");
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 0.00F);
						result.setConsumedEntries(remainingItemsFromTail);
						promotionResults.add(result);
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("Error in Multi Stepped Promotion Evaluation " + e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e) //Added for TISPT-195
		{
			LOG.error("Error in Multi Stepped Promotion Evaluation " + e);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		catch (final Exception e)
		{
			LOG.error("Error in Multi Stepped Promotion Evaluation " + e);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		return promotionResults;
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param paramSessionContext
	 * @param paramPromotionResult
	 * @param paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext paramSessionContext, final PromotionResult paramPromotionResult,
			final Locale paramLocale)
	{
		if (paramPromotionResult.getFired(paramSessionContext))
		{
			final Object[] args = {};
			return formatMessage(this.getMessageFired(paramSessionContext), args, paramLocale);
		}
		else if (paramPromotionResult.getCouldFire(paramSessionContext))
		{
			final Object[] args = {};
			return formatMessage(this.getMessageCouldHaveFired(paramSessionContext), args, paramLocale);
		}

		return MarketplacecommerceservicesConstants.EMPTYSPACE;
	}

	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}


	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected MplBundlePromotionHelper getMplBundlePromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplBundlePromotionHelper", MplBundlePromotionHelper.class);
	}


	/**
	 * Return Sorted Set Data of the steps
	 *
	 *
	 * @param ctx
	 * @param order
	 * @param rows
	 * @return qualifyingCountAndPrices
	 */
	private List getSteps(final SessionContext ctx, final AbstractOrder order, final Collection<PromotionQuantityAndPricesRow> rows)
	{
		final List qualifyingCountAndPrices = new ArrayList<QuantityPrice>();
		List finalSet = new ArrayList<QuantityPrice>();
		if ((rows != null) && (!(rows.isEmpty())))
		{
			for (final PromotionQuantityAndPricesRow row : rows)
			{
				final long quantity = row.getQuantity(ctx).longValue();
				if (quantity <= 0L)
				{
					continue;
				}
				final Double promotionPriceValue = getPriceForOrder(ctx, row.getPrices(ctx), order, "qualifyingCountsAndBundlePrices");
				if (promotionPriceValue == null)
				{
					continue;
				}
				qualifyingCountAndPrices.add(new QuantityPrice(quantity, promotionPriceValue.doubleValue()));
			}

			if (CollectionUtils.isNotEmpty(qualifyingCountAndPrices) && qualifyingCountAndPrices.size() > 1)
			{
				finalSet = getSotedSetData(qualifyingCountAndPrices);
			}

		}
		return finalSet;
	}

	/**
	 * Return Sorted Set Data of the steps
	 *
	 * @param qualifyingCountAndPrices
	 * @return finalSet
	 */
	private List getSotedSetData(final List qualifyingCountAndPrices)
	{
		final List finalSet = new ArrayList<QuantityPrice>();
		List listData = null;
		if (CollectionUtils.isNotEmpty(qualifyingCountAndPrices))
		{
			listData = new ArrayList<QuantityPrice>(qualifyingCountAndPrices);
			Collections.sort(listData, new Comparator<QuantityPrice>()
			{
				@Override
				public int compare(final QuantityPrice a, final QuantityPrice b)
				{
					return Long.valueOf(b.quantity).compareTo(Long.valueOf(a.quantity));
				}
			});
		}

		if (CollectionUtils.isNotEmpty(listData))
		{
			finalSet.addAll(listData);
		}
		return finalSet;
	}



	/**
	 * Static Class for Quantity
	 *
	 * @author TCS
	 *
	 */
	static class QuantityPrice
	{
		private final static Logger LOG = Logger.getLogger(QuantityPrice.class.getName());
		public long quantity;
		public double price;

		QuantityPrice(final long quantity, final double price)
		{
			LOG.debug("Inside QuantityPrice");
			this.quantity = quantity;
			this.price = price;

		}
	}

}
