package com.tisl.mpl.jalo;

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
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * This promotion is of type Buy A get B free and discount
 *
 */
public class BuyABFreePrecentageDiscount extends GeneratedBuyABFreePrecentageDiscount
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyABFreePrecentageDiscount.class.getName());
	private int noOfProducts = 0;
	private boolean flagForCouldFireMessage = true;

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
	 * @Description : Buy A B Free Percentage Discount Promotion
	 * @param : ctx
	 * @param : evaluationContext
	 * @return :promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext evaluationContext)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		//Blocked for TISPT-154
		//final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, evaluationContext);
		final PromotionsManager.RestrictionSetResult rsr = getDefaultPromotionsManager().findEligibleProductsInBasket(ctx,
				evaluationContext, this, getCategories());
		//final List<Product> excludedProductList = new ArrayList<Product>();
		//final List<String> excludeManufactureList = new ArrayList<String>();
		//final List<Product> promotionProductList = new ArrayList<>(getProducts()); // Fetching Promotion set Primary Products
		//final List<Category> promotionCategoryList = new ArrayList<>(getCategories()); // Fetching Promotion set Primary Categories

		//		GenericUtilityMethods.populateExcludedProductManufacturerList(ctx, evaluationContext, excludedProductList,
		//				excludeManufactureList, restrictionList, this);

		boolean checkChannelFlag = false;
		try
		{
			final boolean sellerFlag = getDefaultPromotionsManager().isSellerRestrExists(restrictionList);
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel);
			//changes Start for omni cart fix @atmaram
			final AbstractOrder order = evaluationContext.getOrder();
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, order);

			//changes end for omni cart fix @atmaram
			//Blocked for TISPT-154
			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag && sellerFlag)
			{
				final Map<String, List<String>> productAssociatedItemsFinalMap = new ConcurrentHashMap<String, List<String>>();
				final Map<String, Integer> validProductFinalList = new ConcurrentHashMap<String, Integer>();
				//final Map<String, AbstractOrderEntry> validProductUssidFinalMap = new ConcurrentHashMap<String, AbstractOrderEntry>();

				final List<String> sellerIDData = new ArrayList<String>();
				final Map<AbstractOrderEntry, String> eligibleProductMap = new HashMap<AbstractOrderEntry, String>();
				//getting the valid products
				//				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
				//						.getValidProdListForBuyXofAPromo(order, arg0, promotionProductList, promotionCategoryList, restrictionList,
				//								excludedProductList, excludeManufactureList, sellerIDData, eligibleProductMap);
				final List<Product> allowedProductList = new ArrayList<Product>(rsr.getAllowedProducts());
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofA(order, ctx, allowedProductList, restrictionList, sellerIDData, eligibleProductMap);

				if (!getDefaultPromotionsManager().promotionAlreadyFired(ctx, validProductUssidMap))
				{
					promotionResults = promotionEvaluation(ctx, evaluationContext, validProductUssidMap, restrictionList,
							allowedProductList, order, productAssociatedItemsFinalMap, validProductFinalList, sellerIDData,
							eligibleProductMap);
					//					promotionResults = promotionEvaluation(arg0, arg1, validProductUssidMap, restrictionList, promotionProductList,
					//							promotionCategoryList, order, productAssociatedItemsFinalMap, validProductFinalList,
					//							validProductUssidFinalMap, sellerIDData, eligibleProductMap);
				}

				//Setting values
				//arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidFinalMap);
				ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductFinalList);
				ctx.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsFinalMap);
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
	 * @Description : Buy A B Free Percentage Discount Promotion
	 * @param : arg0
	 * @param : arg1
	 * @param : excludedProductList
	 * @param :excludeManufactureList
	 * @param : restrictionList
	 * @return: promotionResults
	 */
	//	public List<PromotionResult> promotionEvaluation(final SessionContext arg0, final PromotionEvaluationContext arg1,
	//			final List<Product> excludedProductList, final List<String> excludeManufactureList,
	//			final List<AbstractPromotionRestriction> restrictionList)

	private List<PromotionResult> promotionEvaluation(final SessionContext ctx,
			final PromotionEvaluationContext evaluationContext, final Map<String, AbstractOrderEntry> validProductUssidMap,
			final List<AbstractPromotionRestriction> restrictionList, final List<Product> allowedProductList,
			final AbstractOrder order, final Map<String, List<String>> productAssociatedItemsFinalMap,
			final Map<String, Integer> validProductFinalList, final List<String> sellerIDData,
			final Map<AbstractOrderEntry, String> eligibleProductMap)
	{
		final List<String> skuFreebieList;
		final Map<String, Product> freegiftInfoMap;
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		//final Map<AbstractOrderEntry, String> eligibleProductMap = new HashMap<AbstractOrderEntry, String>();
		boolean flagForDeliveryModeRestrEval = false;
		final double maxDiscount = getMaxDiscountVal().doubleValue();

		if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, ctx, evaluationContext, this,
				restrictionList)) // If exceeds set Category Amount and Restriction set Brand Value
		{
			final Double discountPrice = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
					MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null ? (Double) getPriceForOrder(ctx,
					getDiscountPrices(ctx), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES) : new Double(0.0);

			boolean isPercentageDisc = false;
			final Long eligibleQuantity = getQuantity();
			int totalCount = 0;
			//final List<Product> eligibleProductList = new ArrayList<Product>();

			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
				//eligibleProductList.add(entry.getProduct());
			}
			noOfProducts = totalCount;

			List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;

			final PromotionOrderView view = evaluationContext.createView(ctx, this, allowedProductList);

			final Map<String, Integer> tcMapForValidEntries = new ConcurrentHashMap<String, Integer>();
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
			}

			if (totalCount >= eligibleQuantity.intValue())
			{
				final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
						validProductUssidMap, totalCount, eligibleQuantity.longValue(), ctx, restrictionList);

				validProductFinalList.putAll(validProductList);
				//validProductUssidFinalMap.putAll(validProductUssidMap);

				if (!isPercentageOrAmount().booleanValue())
				{
					flagForCouldFireMessage = getDefaultPromotionsManager().getValidProductListForAmtDiscount(ctx, order,
							allowedProductList, eligibleQuantity, discountPrice, validProductUssidMap);
				}

				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
						validProductUssidMap, order);
				final boolean flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(
						restrictionList, order);
				if (flagForDeliveryModeRestrEval && flagForPincodeRestriction)
				{
					double percentageDiscount = getPercentageDiscount().doubleValue();

					//Promotion Value Calculations
					final int totalFactorCount = totalCount / eligibleQuantity.intValue();
					int totalValidProdCount = 0;
					for (final String key : validProductList.keySet())
					{
						totalValidProdCount += validProductList.get(key).intValue(); // Fetches total count of Valid Products
					}

					final double totalPricevalue = getDefaultPromotionsManager().getTotalValidProdPrice(validProductUssidMap,
							validProductList);
					//					final double percentageDiscountForAmount = getDefaultPromotionsManager().getConvertedPercentageDiscount(
					//							totalValidProdCount, discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);

					if (!isPercentageOrAmount().booleanValue())
					{
						percentageDiscount = getDefaultPromotionsManager().getConvertedPercentageDiscount(totalValidProdCount,
								discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);
						//percentageDiscount = percentageDiscountForAmount;
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

					ctx.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT, Double.valueOf(percentageDiscount));
					//					arg0.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
					//							Double.valueOf(totalPricevalue));
					//					arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
					ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductList);
					ctx.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
					ctx.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));

					final Currency currency = evaluationContext.getOrder().getCurrency(ctx);

					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						final AbstractOrderEntry entry = mapEntry.getValue();
						final String validUssid = mapEntry.getKey();
						final long quantityOfOrderEntry = entry.getQuantity(ctx).longValue();

						final double percentageDiscountvalue = percentageDiscount / 100.0D;

						if (percentageDiscount < 100)
						{
							final int eligibleCount = validProductList.get(validUssid).intValue();
							final double originalUnitPrice = entry.getBasePrice(ctx).doubleValue();
							final double originalEntryPrice = eligibleCount * originalUnitPrice;

							final BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(ctx, currency, originalEntryPrice
									- (originalEntryPrice * percentageDiscountvalue));

							final BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(
									ctx,
									currency,
									(adjustedEntryPrice.equals(BigDecimal.ZERO)) ? BigDecimal.ZERO : adjustedEntryPrice.divide(
											BigDecimal.valueOf(eligibleCount), RoundingMode.HALF_EVEN));

							final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
							consumed.add(getDefaultPromotionsManager().consume(ctx, this, eligibleCount, eligibleCount, entry));

							tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));

							for (final PromotionOrderEntryConsumed poec : consumed)
							{
								poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
							}

							final BigDecimal adjustment = Helper.roundCurrencyValue(ctx, currency,
									adjustedEntryPrice.subtract(BigDecimal.valueOf(originalEntryPrice)));
							//final double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue * eligibleCount);

							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
									evaluationContext.getOrder(), 1.0F);
							final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
									.createCustomPromotionOrderEntryAdjustAction(ctx, entry, quantityOfOrderEntry,
											adjustment.doubleValue());
							//final List consumed = paramPromotionEvaluationContext.finishLoggingAndGetConsumed(this, true);
							result.setConsumedEntries(ctx, consumed);
							result.addAction(ctx, poeac);
							promotionResults.add(result);
						}
					}

					final List<Product> promotionGiftProductList = (List<Product>) this.getGiftProducts(ctx);

					ctx.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(totalFactorCount)); // Setting Free gift details in Session Context
					//arg0.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));

					final PromotionResult freeResult = PromotionsManager.getInstance().createPromotionResult(ctx, this,
							evaluationContext.getOrder(), 1.0F);
					//freeResult.setConsumedEntries(arg0, consumed);

					if (CollectionUtils.isNotEmpty(promotionGiftProductList))
					{
						final Map<String, Product> giftProductDetails = getDefaultPromotionsManager().getGiftProductsUSSID(
								promotionGiftProductList, sellerIDData);

						if (MapUtils.isNotEmpty(giftProductDetails))

						{
							getPromotionUtilityPOJO().setPromoProductList(allowedProductList);
							skuFreebieList = populateFreebieSKUIDs(giftProductDetails);
							freegiftInfoMap = populateFreebieDetails(giftProductDetails);
							int giftCount = 0;

							for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
							{
								giftCount = getDefaultPromotionsManager().getFreeGiftCount(entry.getKey(), eligibleProductMap,
										eligibleQuantity.intValue());
							}

							if (giftCount > 0)
							{
								ctx.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(giftCount));
							}

							//final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
							//									.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, entry.getKey());
							final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
									.getAssociatedItemsForAFreebiePromotions(validProductUssidMap, skuFreebieList);
							productAssociatedItemsFinalMap.putAll(productAssociatedItemsMap);

							ctx.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
							ctx.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
							ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);

							freeResult.addAction(
									ctx,
									getDefaultPromotionsManager().createCustomPromotionOrderAddFreeGiftAction(ctx, freegiftInfoMap,
											freeResult, Double.valueOf(giftCount)));
							//}
						}
					}
					promotionResults.add(freeResult);
				}
			}

			//Setting remaining items
			remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(ctx, view.getAllEntries(ctx),
					tcMapForValidEntries);

			//if (noOfProducts > 0 && GenericUtilityMethods.checkRestrictionData(restrictionList)) // For Localization: To check for Excluded Products
			if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L
					&& GenericUtilityMethods.checkRestrictionData(restrictionList))
			{
				final float certainty = (float) remainingItemsFromTail.size() / eligibleQuantity.intValue();

				if (certainty < 1.0F && certainty > 0.0F)
				{
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
							evaluationContext.getOrder(), certainty);
					result.setConsumedEntries(remainingItemsFromTail);
					promotionResults.add(result);
				}
			}
		}
		else
		{
			if (GenericUtilityMethods.checkRestrictionData(restrictionList))
			{
				final PromotionResult result = getDefaultPromotionsManager().createPromotionResult(ctx, this,
						evaluationContext.getOrder(), 0.00F);
				promotionResults.add(result);
			}
		}
		return promotionResults;
	}


	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext paramSessionContext ,PromotionResult paramPromotionResult ,Locale paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{

		final AbstractOrder order = promotionResult.getOrder(ctx);
		final Integer freeCount = Integer.valueOf(1);
		int finalNumberOfProducts = 0;
		try
		{
			if (order != null)
			{
				if (!isPercentageOrAmount().booleanValue() && !flagForCouldFireMessage)
				{
					return "";
				}

				final Currency orderCurrency = order.getCurrency(ctx);
				if (promotionResult.getFired(ctx))
				{
					if (isPercentageOrAmount().booleanValue())
					{
						final Double percentageDiscount = getPercentageDiscount(ctx);
						final Double totalDiscount = Double.valueOf(promotionResult.getTotalDiscount(ctx));
						final Object[] args =
						{ percentageDiscount, totalDiscount,
								Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount.doubleValue()),
								MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE };
						return formatMessage(getMessageFired(ctx), args, locale);
					}
					else
					{
						final Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), order,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES);

						final Double totalDiscount = Double.valueOf(promotionResult.getTotalDiscount(ctx));
						final Object[] args =
						{ discountPriceValue, totalDiscount,
								Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount.doubleValue()),
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES_MESSAGE };
						return formatMessage(getMessageFired(ctx), args, locale);
					}
				}

				else if (promotionResult.getCouldFire(ctx))
				{
					final Integer qualifyingCount = Integer.valueOf(getQuantity().intValue());
					double minimumCategoryValue = 0.00D;
					if (null != getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
					{
						minimumCategoryValue = ((Double) getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
								.doubleValue();
						final int qCount = qualifyingCount.intValue();
						if (noOfProducts < qCount)
						{
							finalNumberOfProducts = qCount - noOfProducts;
						}
						else
						{
							final int factor = noOfProducts / qCount;
							finalNumberOfProducts = (factor + 1) * qCount - noOfProducts;
						}

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
										paymentModes = deliveryModes + paymentModeList.get(0).getMode();
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
						if (minimumCategoryValue > 0.00D)
						{
							args[0] = Integer.valueOf(finalNumberOfProducts);
							args[1] = qualifyingCount;
							args[2] = freeCount;
							args[3] = Double.valueOf(minimumCategoryValue);
						}
						else
						{
							args[0] = Integer.valueOf(finalNumberOfProducts);
							args[1] = qualifyingCount;
							args[2] = freeCount;
							args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
						}
						if (!deliveryModes.isEmpty())
						{
							args[4] = deliveryModes;
						}
						else if (!paymentModes.isEmpty())
						{
							args[4] = paymentModes;
						}
						else
						{
							args[4] = "purchase";
						}
						return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
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


	/**
	 * The Getter Setter creation for Sonar Fix
	 *
	 *
	 */
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

	/**
	 * Description : Populate Freebie SKU IDs *** For TISEE-5527
	 *
	 * @param giftProductDetails
	 * @return skuIDList
	 */
	private List<String> populateFreebieSKUIDs(final Map<String, Product> giftProductDetails)
	{
		final List<String> skuIDList = new ArrayList<>();
		for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
		{
			skuIDList.add(entry.getKey());
		}
		return skuIDList;
	}

	/**
	 * Description : Populate Freebie Details *** For TISEE-5527
	 *
	 * @param giftProductDetails
	 * @return giftDetails
	 */
	private Map<String, Product> populateFreebieDetails(final Map<String, Product> giftProductDetails)
	{
		final Map<String, Product> giftDetails = new ConcurrentHashMap<>();
		for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
		{
			giftDetails.put(entry.getKey(), entry.getValue());
		}
		return giftProductDetails;
	}
}