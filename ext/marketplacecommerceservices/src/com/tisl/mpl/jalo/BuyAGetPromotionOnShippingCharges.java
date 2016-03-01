package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


public class BuyAGetPromotionOnShippingCharges extends GeneratedBuyAGetPromotionOnShippingCharges
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAGetPromotionOnShippingCharges.class.getName());
	private int noOfProducts = 0;

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
	 * @Description : Buy Product A and get Percentage/Amount Discount on shipping charges or Free Shipping
	 * @param : SessionContext arg0 ,PromotionEvaluationContext arg1
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promotionEvalCtx)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean checkChannelFlag = false;
		boolean isMultipleSeller = false;

		try
		{
			final AbstractOrder order = promotionEvalCtx.getOrder();
			final List<Product> promotionProductList = new ArrayList<>(getProducts()); //Adding of Promotion Added Products to List
			final List<Category> promotionCategoryList = new ArrayList<>(getCategories());//Adding of Promotion Added Categories to List
			final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

			final List<Product> excludedProductList = new ArrayList<Product>();
			final List<String> excludeManufactureList = new ArrayList<String>();

			GenericUtilityMethods.populateExcludedProductManufacturerList(ctx, promotionEvalCtx, excludedProductList,
					excludeManufactureList, restrictionList, this);
			// To check whether Promotion already applied on Product
			//getDefaultPromotionsManager().promotionAlreadyFired(ctx, order, excludedProductList);
			final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, // Promotion added Restriction evaluation
					promotionEvalCtx);
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit

			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = promotionEvalCtx.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram


			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag) // if Restrictions return valid && Channel is valid
			{
				final Map<String, List<String>> productAssociatedItemsFinalMap = new HashMap<String, List<String>>();

				final Map<String, Integer> validProductFinalList = new HashMap<String, Integer>();

				final Map<String, AbstractOrderEntry> validProductUssidFinalMap = new HashMap<String, AbstractOrderEntry>();

				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofAPromo(order, ctx, promotionProductList, promotionCategoryList, restrictionList,
								excludedProductList, excludeManufactureList, null, null); // Adding Eligible Products to List

				final Map<String, Map<String, Double>> finalDelChrgMap = new HashMap<String, Map<String, Double>>();

				if (!getDefaultPromotionsManager().promotionAlreadyFired(ctx, validProductUssidMap))
				{
					isMultipleSeller = getMplPromotionHelper().checkMultipleSeller(restrictionList);

					if (isMultipleSeller)
					{
						Map<AbstractOrderEntry, String> productSellerDetails = new HashMap<AbstractOrderEntry, String>();
						Map<String, Map<String, AbstractOrderEntry>> multiSellerValidUSSIDMap = new HashMap<String, Map<String, AbstractOrderEntry>>();
						productSellerDetails = getMplPromotionHelper().populateSellerSpecificData(ctx, restrictionList,
								validProductUssidMap);
						if (MapUtils.isNotEmpty(productSellerDetails))
						{
							multiSellerValidUSSIDMap = getMplPromotionHelper().populateMultiSellerData(validProductUssidMap,
									productSellerDetails, ctx);
							if (null != multiSellerValidUSSIDMap && !multiSellerValidUSSIDMap.isEmpty())
							{
								for (final Map.Entry<String, Map<String, AbstractOrderEntry>> multiSellerData : multiSellerValidUSSIDMap
										.entrySet())
								{
									final Map<String, AbstractOrderEntry> validMultiUssidMap = multiSellerData.getValue();
									if (null != validProductUssidMap && !validProductUssidMap.isEmpty())
									{
										List<PromotionResult> promotionResultList = new ArrayList<PromotionResult>();
										/*
										 * promotionResultList = promotionEvaluation(ctx, promotionEvalCtx, validMultiUssidMap,
										 * restrictionList, promotionProductList, promotionCategoryList, order,
										 * productAssociatedItemsFinalMap, validProductFinalList, validProductUssidFinalMap,
										 * finalDelChrgMap);
										 */
										promotionResultList = promotionEvaluation(ctx, promotionEvalCtx, validMultiUssidMap,
												restrictionList, order, productAssociatedItemsFinalMap, validProductFinalList,
												validProductUssidFinalMap, finalDelChrgMap);


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
						/*
						 * promotionResults = promotionEvaluation(ctx, promotionEvalCtx, validProductUssidMap,
						 * restrictionList, promotionProductList, promotionCategoryList, order,
						 * productAssociatedItemsFinalMap, validProductFinalList, validProductUssidFinalMap, finalDelChrgMap);
						 */

						promotionResults = promotionEvaluation(ctx, promotionEvalCtx, validProductUssidMap, restrictionList, order,
								productAssociatedItemsFinalMap, validProductFinalList, validProductUssidFinalMap, finalDelChrgMap);

					}
				}

				//Setting values
				ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidFinalMap);
				ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductFinalList);
				ctx.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsFinalMap);
				ctx.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
				ctx.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP, finalDelChrgMap);
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
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext paramSessionContext ,PromotionResult paramPromotionResult ,Locale paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		//resetFlags();
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;

		final AbstractOrder order = promotionResult.getOrder(ctx);
		if (null != ctx.getCurrency() && null != ctx.getCurrency().getIsocode())
		{
			currency = ctx.getCurrency().getIsocode();
			if (null == currency)
			{
				currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
			}
		}
		int finalNumberOfProducts = 0;
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

		if (null != order)
		{
			if (promotionResult.getFired(ctx))
			{
				//getValidProductList(order, ctx);
				//				if (!getDefaultPromotionsManager().fetchUssidMplZoneDeliveryCost().isEmpty())
				//				{

				final Object[] args =
				{ this.getQuantity(ctx), discPerOrAmtStr };
				return formatMessage(this.getMessageFired(ctx), args, locale);
				//}
			}

			else if (promotionResult.getCouldFire(ctx))
			{
				final Long qualifyingCount = getQuantity(ctx);
				double minimumCategoryValue = 0.00D;
				//getValidProductList(order, ctx);
				if (null != getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
				{
					minimumCategoryValue = ((Double) getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
							.doubleValue();
					final int qCount = qualifyingCount.intValue();
					if (qCount > noOfProducts)
					{
						finalNumberOfProducts = qCount - noOfProducts;
					}
					else
					{
						finalNumberOfProducts = GenericUtilityMethods.calculateNoOfProducts(qCount, noOfProducts);
					}

					if (minimumCategoryValue > 0.00D)
					{
						data = messageData();
						final Object[] args =
						{ Integer.valueOf(finalNumberOfProducts), discPerOrAmtStr, Double.valueOf(minimumCategoryValue), data };
						return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
					}
					else
					{
						data = messageData();
						final Object[] args =
						{ Integer.valueOf(finalNumberOfProducts), discPerOrAmtStr, MarketplacecommerceservicesConstants.EMPTYSPACE,
								data };
						return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
					}
				}
			}
		}
		return MarketplacecommerceservicesConstants.EMPTYSPACE;
	}




	/**
	 * @Description: Message Localization
	 * @return: String
	 */
	private String messageData()
	{
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != getProducts() && !getProducts().isEmpty() && getProducts().size() > 0)
		{
			for (final Product product : getProducts())
			{
				data = data + product.getName() + ",";
			}
		}
		else if (null != getCategories() && !getCategories().isEmpty() && getCategories().size() > 0)
		{
			for (final Category category : getCategories())
			{
				data = data + category.getName() + ",";
			}

		}
		return data;
	}

	/**
	 * @Description : Promotion Evaluation Method
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	/*
	 * private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext, final
	 * PromotionEvaluationContext paramPromotionEvaluationContext, final Map<String, AbstractOrderEntry>
	 * validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList, final List<Product>
	 * promotionProductList, final List<Category> promotionCategoryList, final AbstractOrder order, final Map<String,
	 * List<String>> productAssociatedItemsFinalMap, final Map<String, Integer> validProductFinalList, final Map<String,
	 * AbstractOrderEntry> validProductUssidFinalMap, final Map<String, Map<String, Double>> finalDelChrgMap)
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order, final Map<String, List<String>> productAssociatedItemsFinalMap,
			final Map<String, Integer> validProductFinalList, final Map<String, AbstractOrderEntry> validProductUssidFinalMap,
			final Map<String, Map<String, Double>> finalDelChrgMap)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager promotionsManager = PromotionsManager.getInstance();
		int totalCount = 0;
		final Long eligibleQuantity = getQuantity();
		boolean flagForDeliveryModeRestrEval = false;

		try
		{
			if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, paramSessionContext,
					paramPromotionEvaluationContext, this, restrictionList)) // If exceeds set Category Amount and Restriction set Brand Value
			{
				//Fetching total count of valid products
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

				final PromotionOrderView view = paramPromotionEvaluationContext.createView(paramSessionContext, this,
						eligibleProductList);
				//final PromotionOrderEntry viewEntry = view.peek(paramSessionContext);

				final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();
				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
				{
					tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
				}

				if (totalCount >= eligibleQuantity.intValue())
				{
					final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
							validProductUssidMap, totalCount, eligibleQuantity.longValue(), paramSessionContext, restrictionList);

					validProductFinalList.putAll(validProductList);
					validProductUssidFinalMap.putAll(validProductUssidMap);

					//for delivery mode restriction check
					flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
							validProductUssidMap);

					if (flagForDeliveryModeRestrEval) // delivery mode true and If Total no of valid Products exceeds Qualifying Count
					{
						final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
								.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, null);

						productAssociatedItemsFinalMap.putAll(productAssociatedItemsMap);

						//Fetching product rich attribute
						final Map<String, String> fetchProductRichAttribute = getDefaultPromotionsManager().fetchProductRichAttribute(
								validProductList);

						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
							//paramPromotionEvaluationContext.startLoggingConsumed(this);
							final String validProductUSSID = mapEntry.getKey();
							final AbstractOrderEntry entry = mapEntry.getValue();
							final long quantityOfOrderEntry = entry.getQuantity(paramSessionContext).longValue();
							final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProductUSSID);

							if ((isTShipAsPrimitive() && isSShipAsPrimitive())
									|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							{
								//Calculating delivery charges & setting it at entry level
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
										&& (getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext), order,
												MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
								{
									adjustedDeliveryCharge = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext),
											order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
								}
								else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
								{
									isDeliveryFreeFlag = true;
								}
								//TISEE-5339 : Fix
								prodPrevCurrDelChargeMap = getDefaultPromotionsManager().calcDeliveryCharges(isDeliveryFreeFlag,
										isPercentageFlag, adjustedDeliveryCharge, validProductList, validProductUSSID);

								//TISEE-5339 : Fix
								//								paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE,
								//										String.valueOf(this.getCode()));
								//								paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP,
								//										prodPrevCurrDelChargeMap);
								finalDelChrgMap.putAll(prodPrevCurrDelChargeMap);
								final int eligibleCount = validProductList.get(validProductUSSID).intValue();

								final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
								consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount,
										eligibleCount, entry));

								tcMapForValidEntries.put(validProductUSSID, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));
								//add 0 as adjustment as no adjustment would be done for product price.
								final double adjustment = 0.0D;
								final PromotionResult result = promotionsManager.createPromotionResult(paramSessionContext, this,
										paramPromotionEvaluationContext.getOrder(), 1.0F);
								final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
										.createCustomShippingChargesPromotionAdjustAction(paramSessionContext, entry, quantityOfOrderEntry,
												adjustment);
								//final List consumed = paramPromotionEvaluationContext.finishLoggingAndGetConsumed(this, true);
								result.setConsumedEntries(paramSessionContext, consumed);
								result.addAction(paramSessionContext, poeac);
								promotionResults.add(result);

							}
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
				//if (noOfProducts > 0 && flagForDeliveryModeRestrEval) // For Localization: To check for Excluded Products
				if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L)
				{
					//					final float certainty = remainingItemsFromTail != null ? (remainingItemsFromTail.isEmpty() ? 1.00F
					//							: (float) remainingItemsFromTail.size() / eligibleQuantity.intValue()) : 0.00F;

					final float certainty = (float) remainingItemsFromTail.size() / eligibleQuantity.intValue();

					if (certainty < 1.0F && certainty > 0.0F)
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), certainty);
						result.setConsumedEntries(remainingItemsFromTail);
						promotionResults.add(result);
					}

				}
			}
			else
			{
				//certainty check
				final float certainty = 0.00F;
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
						paramPromotionEvaluationContext.getOrder(), certainty);
				promotionResults.add(result);
			}
		}
		catch (final JaloInvalidParameterException exception)
		{
			LOG.error(exception.getMessage());
		}

		return promotionResults;
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
