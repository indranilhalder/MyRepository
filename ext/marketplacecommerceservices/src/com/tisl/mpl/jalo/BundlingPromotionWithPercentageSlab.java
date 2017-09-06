package com.tisl.mpl.jalo;

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
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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


public class BundlingPromotionWithPercentageSlab extends GeneratedBundlingPromotionWithPercentageSlab
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BundlingPromotionWithPercentageSlab.class.getName());


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
	 * @Description : BundlingPromotionWithPercentageSlab (Bundle)
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @return promotionResults
	 */

	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		LOG.debug("Inside Bundling Promotion With Percentage Slab evaluate method");
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
		boolean checkChannelFlag = false;
		final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			final PromotionsManager.RestrictionSetResult rsr = getDefaultPromotionsManager().findEligibleProductsInBasket(
					paramSessionContext, paramPromotionEvaluationContext, this, getCategories());

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag)
			{
				final List<Product> allowedProductList = new ArrayList<Product>(rsr.getAllowedProducts());
				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofA(cart, paramSessionContext, allowedProductList, restrictionList, null, null); // Adding Eligible Products to List

				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap)
						&& MapUtils.isNotEmpty(validProductUssidMap))
				{
					promotionResults = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext, validProductUssidMap,
							restrictionList, allowedProductList, cart);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(e.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
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
			final List<Product> allowedProductList, final AbstractOrder cart)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		try
		{
			LOG.debug("Inside Bundling Promotion With Percentage Slab promotion Evaluation Method");
			final List<QuantityPrice> steps = getSteps(paramSessionContext, cart,
					getQualifyingCountsAndBundlePrices(paramSessionContext));


			if (CollectionUtils.isNotEmpty(steps))
			{
				long totalCount = 0;
				long promotionEligibleCount = 0;
				long promotionEligibleCountFinal = 0;
				long highestStepQuantityValue = 0;
				boolean flagForDeliveryModeRestrEval = false;
				boolean flagForPaymentModeRestrEval = false;
				boolean flagForPincodeRestriction = false;
				double totalAdjustment = 0.0D;
				double totalPrice = 0.0D;
				long promotionConsmdCount = 0;
				List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;
				double totalAdjustOffPricePercentageDiscount = 0.0D;
				double totalEligiblePrice = 0.0D;

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
					for (final AbstractOrderEntry entry : validProductUssidMap.values())
					{
						totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
						totalPrice += entry.getTotalPrice().doubleValue();
					}
					LOG.debug("Total Eligible Count of Products" + totalCount);

					if (totalCount > 0)
					{
						for (final QuantityPrice priceCountData : steps)
						{
							if (totalCount >= priceCountData.quantity)//for getting the promotion eligible quantity
							{
								promotionEligibleCount = priceCountData.quantity;//to find out promotion eligible quantity
								promotionEligibleCountFinal = priceCountData.quantity;//to find out promotion eligible count to be consumed
								break;
							}
						}

						for (final QuantityPrice priceCountData : steps)//for getting the highest step quantity for potential promotion evaluation
						{
							highestStepQuantityValue = priceCountData.quantity;
							break;
						}

					}
					LOG.debug("The promotionEligibleCount is " + promotionEligibleCount);

					LOG.debug("*************Setting Promotion Consumed Data***************************");

					final PromotionOrderView view = paramPromotionEvaluationContext.createView(paramSessionContext, this,
							allowedProductList);

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
							totalAdjustOffPricePercentageDiscount = priceData.percentageDiscount;//For getting the eligible percentage discount

							promotionEligibleCount = promotionEligibleCount - priceData.quantity;

						}
					}

					final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
							validProductUssidMap, (int) promotionEligibleCountFinal, promotionEligibleCountFinal, paramSessionContext,
							restrictionList, getCode());//for getting the validProductList eligible for qualifying for promotion

					LOG.debug("The totalAdjustOffPricePercentageDiscount is" + totalAdjustOffPricePercentageDiscount);

					totalEligiblePrice = getMplBundlePromotionHelper().getTotalEligiblePrice(validProductUssidMap, validProductList);

					totalAdjustment = (totalAdjustOffPricePercentageDiscount * totalEligiblePrice) / 100;

					LOG.debug("The totalAdjustment is" + totalAdjustment);
					LOG.debug("Previous Cart Value" + totalPrice);

					if (totalAdjustment > 0)
					{
						LOG.debug("Fired Promotion Scenario");
						promotionConsmdCount = promotionEligibleCountFinal;
						if (promotionConsmdCount > 0)
						{
							final BigDecimal unitAdjustment = BigDecimal.valueOf(totalAdjustment).divide(
									BigDecimal.valueOf(promotionConsmdCount), RoundingMode.HALF_EVEN);
							LOG.debug("Unit Adjustment:" + unitAdjustment);

							LOG.debug("Converting the Price off to Percentage: ");
							final double percentageDiscount = getMplBundlePromotionHelper().getDiscountPercentage(validProductList,
									validProductUssidMap, totalAdjustment);

							LOG.debug("Percentage Discount : " + percentageDiscount);

							// Apportioning Code Implementation

							LOG.debug("Apportioning Implementation");


							paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
									Double.valueOf(percentageDiscount));

							paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE,
									String.valueOf(this.getCode()));

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
											quantityOfOrderEntry, entry));

									tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));

									for (final PromotionOrderEntryConsumed poec : consumed)
									{
										poec.setAdjustedUnitPrice(paramSessionContext, adjustedUnitPrice.doubleValue());
									}


									LOG.debug("*************Setting Promotion Consumed Data Per Entry Ends***************************");

									final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(
											paramSessionContext, this, paramPromotionEvaluationContext.getOrder(), 1.0F);
									final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
											.createCustomPromotionOrderEntryAdjustAction(paramSessionContext, entry, eligibleCount,
													adjustment.doubleValue());
									result.setConsumedEntries(paramSessionContext, consumed);
									result.addAction(paramSessionContext, poeac);
									promotionResults.add(result);
								}
							}
						}
					}


					remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
							view.getAllEntries(paramSessionContext), tcMapForValidEntries);//items not qualified for promotion

					LOG.debug("Evaluating Potential Promotion Scenario");

					if (totalCount < highestStepQuantityValue)//potential promotion decider
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
			LOG.error("Error in Bundling Promotion With Percentage Slab EtailBusinessExceptions" + e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Error in Bundling Promotion With Percentage Slab EtailNonBusinessExceptions" + e);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		catch (final Exception e)
		{
			LOG.error("Error in Bundling Promotion With Percentage Slab MaterException " + e);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}

		return promotionResults;
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
		public double percentageDiscount;

		QuantityPrice(final long quantity, final double percentageDiscount)
		{
			LOG.debug("Inside QuantityPrice");
			this.quantity = quantity;
			this.percentageDiscount = percentageDiscount;

		}
	}

	/**
	 * Return Sorted Set Data of the steps
	 *
	 *
	 * @param ctx
	 * @param order
	 * @param rows
	 * @return qualifyingCountAndPercentageDiscount
	 */
	private List getSteps(final SessionContext ctx, final AbstractOrder order, final Collection<PromotionQuantityAndPricesRow> rows)
	{
		final List qualifyingCountAndPercentageDiscount = new ArrayList<QuantityPrice>();
		Double promotionPercentageDiscountValue = Double.valueOf(0);
		List finalSet = new ArrayList<QuantityPrice>();

		try
		{
			if (CollectionUtils.isNotEmpty(rows))
			{
				for (final PromotionQuantityAndPricesRow row : rows)
				{
					final long quantity = row.getQuantity(ctx).longValue();
					if (quantity <= 0L)
					{
						continue;
					}

					promotionPercentageDiscountValue = (Double) row.getAttribute(ctx, "percentageDiscount");

					if (promotionPercentageDiscountValue == null)
					{
						continue;
					}
					qualifyingCountAndPercentageDiscount.add(new QuantityPrice(quantity, promotionPercentageDiscountValue
							.doubleValue()));
				}

				if (CollectionUtils.isNotEmpty(qualifyingCountAndPercentageDiscount)
						&& qualifyingCountAndPercentageDiscount.size() > 1)
				{
					finalSet = getSortedSetData(qualifyingCountAndPercentageDiscount);
				}

			}
		}
		catch (final Exception exception)
		{
			exception.getMessage();
		}

		return finalSet;
	}

	/**
	 * Return Sorted Set Data of the steps
	 *
	 * @param qualifyingCountAndPrices
	 * @return finalSet
	 */
	private List getSortedSetData(final List qualifyingCountAndPercentageDiscount)
	{
		final List finalSet = new ArrayList<QuantityPrice>();
		List listData = null;
		if (CollectionUtils.isNotEmpty(qualifyingCountAndPercentageDiscount))
		{
			listData = new ArrayList<QuantityPrice>(qualifyingCountAndPercentageDiscount);
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

	/**
	 * Building the Hash Key for Promotion
	 *
	 * @param builder
	 * @param ctx
	 */
	@Override
	protected void buildDataUniqueKey(final SessionContext ctx, final StringBuilder builder)
	{
		builder.append(super.getClass().getSimpleName()).append('|').append(getPromotionGroup(ctx).getIdentifier(ctx)).append('|')
				.append(getCode(ctx)).append('|').append(getPriority(ctx)).append('|').append(ctx.getLanguage().getIsocode())
				.append('|');

		final Date modifyDate = getModificationTime();
		builder.append(modifyDate);
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

	protected ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

}