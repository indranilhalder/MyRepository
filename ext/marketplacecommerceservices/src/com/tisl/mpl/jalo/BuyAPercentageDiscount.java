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
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.math.RoundingMode;
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




public class BuyAPercentageDiscount extends GeneratedBuyAPercentageDiscount
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAPercentageDiscount.class.getName());

	//	private double totalPricevalue;
	private int noOfProducts = 0;
	private boolean flagForCouldFireMessage = true;



	/**
	 * @Description : This method is for creating item type
	 * @param : ctx
	 * @param : type
	 * @param : allAttributes
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
	 * @Description : Buy Product A get Percentage or Amount Discount
	 * @param : SessionContext paramSessionContext ,PromotionEvaluationContext paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
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
		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(paramSessionContext, // Promotion added Restriction evaluation
				paramPromotionEvaluationContext);

		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit

			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram


			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag) // if Restrictions return valid && Channel is valid
			{
				final Map<String, List<String>> productAssociatedItemsFinalMap = new HashMap<String, List<String>>();

				final Map<String, Integer> validProductFinalList = new HashMap<String, Integer>();

				final Map<String, AbstractOrderEntry> validProductUssidFinalMap = new HashMap<String, AbstractOrderEntry>();

				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofAPromo(order, paramSessionContext, promotionProductList, promotionCategoryList,
								restrictionList, excludedProductList, excludeManufactureList, null, null); // Adding Eligible Products to List

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
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final AbstractOrder order,
			final Map<String, List<String>> productAssociatedItemsFinalMap, final Map<String, Integer> validProductFinalList,
			final Map<String, AbstractOrderEntry> validProductUssidFinalMap)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager promotionsManager = PromotionsManager.getInstance();
		final Long eligibleQuantity = getQuantity();
		int totalCount = 0;
		try
		{
			if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, paramSessionContext,
					paramPromotionEvaluationContext, this, restrictionList)) // If exceeds set Category Amount and Restriction set Brand Value
			{
				boolean flagForDeliveryModeRestrEval = false;
				boolean flagForPaymentModeRestrEval = false;
				boolean isPercentageDisc = false;
				final double maxDiscount = getMaxDiscountVal() == null ? 0.0D : getMaxDiscountVal().doubleValue(); //Adding the Promotion set Max  Discount Value to a variable
				final Double discountPrice = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext), order,
						MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null ? (Double) getPriceForOrder(paramSessionContext,
						getDiscountPrices(paramSessionContext), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES)
						: new Double(0.0);

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
					double percentageDiscount = getPercentageDiscount() == null ? 0.0D : getPercentageDiscount().doubleValue();

					//Promotion Value Calculations
					final int totalFactorCount = totalCount / eligibleQuantity.intValue();

					final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
							validProductUssidMap, totalCount, eligibleQuantity.longValue(), paramSessionContext, restrictionList);

					validProductFinalList.putAll(validProductList);
					validProductUssidFinalMap.putAll(validProductUssidMap);

					if (!isPercentageOrAmount().booleanValue())
					{
						flagForCouldFireMessage = getDefaultPromotionsManager()
								.getValidProductListForAmtDiscount(paramSessionContext, order, promotionProductList,
										promotionCategoryList, eligibleQuantity, discountPrice, validProductUssidMap);
					}

					//for delivery mode restriction check
					flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
							validProductUssidMap);
					//for payment mode restriction check
					flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
							paramSessionContext);

					if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval) // If Total no of valid Products exceeds Qualifying Count
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
							percentageDiscount = getDefaultPromotionsManager().getConvertedPercentageDiscount(totalValidProdCount,
									discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);
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
						//						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
						//						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductList);
						//						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS,
						//								productAssociatedItemsMap);
						paramSessionContext
								.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
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
								consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount,
										eligibleCount, entry));

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

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext paramSessionContext ,PromotionResult paramPromotionResult ,Locale paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		String firedData = MarketplacecommerceservicesConstants.EMPTYSPACE;
		String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;

		final AbstractOrder order = promotionResult.getOrder(ctx);

		int finalNumberOfProducts = 0;
		final Double percentage = getPercentageDiscount();

		final Double checkDiscountVal = Double.valueOf(promotionResult.getTotalDiscount(ctx));

		if (null != order)
		{
			if (!isPercentageOrAmount().booleanValue() && !flagForCouldFireMessage)
			{
				return "";
			}

			if (promotionResult.getFired(ctx))
			{
				firedData = messageData();

				if (null != ctx.getCurrency() && null != ctx.getCurrency().getIsocode())
				{
					currency = ctx.getCurrency().getIsocode();
					if (null == currency)
					{
						currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
					}
				}

				if (checkDiscountVal.doubleValue() > 0.0D)
				{
					final Object[] args =
					{ this.getQuantity(ctx), percentage, Double.valueOf(promotionResult.getTotalDiscount(ctx)), firedData, currency };
					return formatMessage(this.getMessageFired(ctx), args, locale);
				}
				else
				{
					final Object[] args =
					{ this.getQuantity(ctx), percentage, MarketplacecommerceservicesConstants.EMPTYSPACE, firedData, currency };
					return formatMessage(this.getMessageFired(ctx), args, locale);
				}

			}

			else if (promotionResult.getCouldFire(ctx))
			{
				final Long qualifyingCount = getQuantity(ctx);

				final double minimumCategoryValue = getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT) != null ? ((Double) getProperty(
						ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() : 0.00D;
				final int qCount = qualifyingCount.intValue();

				if (qCount > noOfProducts)
				{
					finalNumberOfProducts = qCount - noOfProducts;
				}
				else
				{
					finalNumberOfProducts = GenericUtilityMethods.calculateNoOfProducts(qCount, noOfProducts);
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
								deliveryModes += " Modes";
							}
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
								paymentModes += " Modes";
							}
						}
					}
				}
				final Object[] args = new Object[6];
				if (minimumCategoryValue > 0.00D)
				{
					data = messageData();
					args[0] = Integer.valueOf(finalNumberOfProducts);
					args[1] = qualifyingCount;
					args[2] = percentage;
					args[3] = Double.valueOf(minimumCategoryValue);
					args[4] = data;
				}
				else
				{
					data = messageData();
					args[0] = Integer.valueOf(finalNumberOfProducts);
					args[1] = qualifyingCount;
					args[2] = percentage;
					args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
					args[4] = data;
				}
				if (!deliveryModes.isEmpty())
				{
					args[5] = deliveryModes;
				}
				else if (!paymentModes.isEmpty())
				{
					args[5] = paymentModes;
				}
				else
				{
					args[5] = "purchase";
				}
				return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
			}
		}
		return MarketplacecommerceservicesConstants.EMPTYSPACE;
	}



	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	@Override
	protected void buildDataUniqueKey(final SessionContext ctx, final StringBuilder builder)
	{
		super.buildDataUniqueKey(ctx, builder);
		builder.append(getPercentageDiscount(ctx)).append('|');
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


	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}





	/**
	 * Generation of Getter and Setter for Sonar Fix
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


}
