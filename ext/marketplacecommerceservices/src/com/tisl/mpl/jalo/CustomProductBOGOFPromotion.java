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
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


public class CustomProductBOGOFPromotion extends GeneratedCustomProductBOGOFPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CustomProductBOGOFPromotion.class.getName());
	//private final List<Product> excludedProductList = null;
	//private final List<String> excludeManufactureList = null;
	private int noOfProducts = 0;

	//private Map<String, Integer> qCMapForCatLevelBOGO = null;

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
	 * @Description : Buy One Get One Free
	 * @param : SessionContext ctx ,PromotionEvaluationContext promoContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		LOG.debug("Inside CustomProductBOGOFPromotion");
		boolean checkChannelFlag = false;
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();

		final AbstractOrder order = promoContext.getOrder(); // Fetch Order Details
		final List<Product> promotionProductList = new ArrayList<>(getProducts()); // Fetch Promotion set Primary Products
		final List<Category> promotionCategoryList = new ArrayList<>(getCategories()); // Fetch Promotion set Primary Categories
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

		final List<Product> excludedProductList = new ArrayList<Product>();
		final List<String> excludeManufactureList = new ArrayList<String>();
		//Populating Excluded Products and Excluded Manufacturer in separate Lists
		GenericUtilityMethods.populateExcludedProductManufacturerList(ctx, promoContext, excludedProductList,
				excludeManufactureList, restrictionList, this);
		//getDefaultPromotionsManager().promotionAlreadyFired(ctx, order, excludedProductList); // Check if Promotion already fired on promotion eligible products
		final PromotionsManager.RestrictionSetResult restrictResult = findEligibleProductsInBasket(ctx, promoContext); // Validating Promotion set restrictions

		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = promoContext.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram

			if ((restrictResult.isAllowedToContinue()) && (!(restrictResult.getAllowedProducts().isEmpty())) && checkChannelFlag)
			{
				// Get the valid Products for Promotions
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager().getValidProdListForBOGO(
						order, ctx, promotionProductList, promotionCategoryList, restrictionList, excludedProductList,
						excludeManufactureList, null, null);

				if (!getDefaultPromotionsManager().promotionAlreadyFired(ctx, validProductUssidMap))
				{
					promotionResults = promotionEvaluation(ctx, promoContext, validProductUssidMap, restrictionList);
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
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

		return promotionResults;
	}

	/**
	 * @Description : This is for modifying OOB method to handle BOGO at USSID level
	 * @param ctx
	 * @param comparator
	 * @param quantity
	 * @param orderEntries
	 * @param freeItemsUssidList
	 * @return list of PromotionOrderEntryConsumed objects
	 */
	private List<PromotionOrderEntryConsumed> consumeFromTail(final SessionContext ctx,
			final Comparator<PromotionOrderEntry> comparator, final long quantity, final List<PromotionOrderEntry> orderEntries,
			final Map<String, Integer> QCMapForValidItems, final Map<String, Integer> tcMapForValidEntries
	/* final List<String> validItemsUssidList */)
	{
		final List orderedEntries = new ArrayList(orderEntries);
		if (comparator != null)
		{
			Collections.sort(orderedEntries, Collections.reverseOrder(comparator));
		}
		return doConsume(ctx, orderedEntries, this, quantity, QCMapForValidItems, tcMapForValidEntries);
	}


	/**
	 * @Description : This is for modifying OOB method to handle BOGO at USSID level
	 * @param ctx
	 * @param comparator
	 * @param quantity
	 * @param orderEntries
	 * @param freeItemsUssidList
	 * @return list of PromotionOrderEntryConsumed objects
	 */
	private List<PromotionOrderEntryConsumed> consumeFromHead(final SessionContext ctx,
			final Comparator<PromotionOrderEntry> comparator, final long quantity, final List<PromotionOrderEntry> orderEntries,
			final Map<String, Integer> QCMapForFreeItems, final Map<String, Integer> tcMapForValidEntries
	/* final List<String> freeItemsUssidList */)
	{
		final List orderedEntries = new ArrayList(orderEntries);
		if (comparator != null)
		{
			Collections.sort(orderedEntries, comparator);
		}
		return doConsume(ctx, orderedEntries, this, quantity, QCMapForFreeItems, tcMapForValidEntries);
	}

	/**
	 * @Description : This is for modifying OOB method to handle BOGO at USSID level
	 * @param ctx
	 * @param workingEntries
	 * @param promotion
	 * @param quantity
	 * @param freeItemsUssidList
	 * @return list of PromotionOrderEntryConsumed objects
	 */
	private List<PromotionOrderEntryConsumed> doConsume(final SessionContext ctx, final List<PromotionOrderEntry> workingEntries,
			final AbstractPromotion promotion, final long quantity, final Map<String, Integer> validItemsUssidMap,
			final Map<String, Integer> tcMapForValidEntries)
	{
		final List consumed = new ArrayList();

		long remaining = quantity;
		for (final PromotionOrderEntry entry : workingEntries)
		{
			//get order entry
			final AbstractOrderEntry orderEntry = entry.getBaseOrderEntry();
			String selectedussid = null;
			try
			{
				selectedussid = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
				LOG.debug("promo entry :" + orderEntry.getEntryNumber() + " selectedussid:" + selectedussid + "-- qty:"
						+ orderEntry.getQuantity());
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				// YTODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
			if (validItemsUssidMap.containsKey(selectedussid))
			{

				if (remaining <= 0L)
				{
					break;
				}

				final long available = validItemsUssidMap.get(selectedussid).longValue();
				//final long available = orderEntry.getQuantityAsPrimitive();
				//final long available = entry.getQuantity(ctx);
				if (available <= 0L)
				{
					continue;
				}
				final long consumeCount = (available < remaining) ? available : remaining;
				//				consumed.add(entry.consume(ctx, promotion, consumeCount));
				consumed.add(getDefaultPromotionsManager().consume(ctx, promotion, consumeCount, available, orderEntry));
				remaining -= consumeCount;

				final int totalCount = tcMapForValidEntries.get(selectedussid).intValue();
				final int consumedCount = (int) consumeCount;
				tcMapForValidEntries.put(selectedussid, Integer.valueOf(totalCount - consumedCount));
			}
		}

		if (remaining > 0L)
		{
			throw new PromotionException("Attempt to consume more items than exist in this view of the order");
		}

		return consumed;
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext ctx ,PromotionResult promotionResult ,Locale locale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		final AbstractOrder order = promotionResult.getOrder(ctx);
		int finalNumberOfProducts = 0;
		double minimumCategoryValue = 0.00D;
		try
		{
			if (order != null)
			{
				final Currency orderCurrency = order.getCurrency(ctx);
				final Integer qualifyingCount = getQualifyingCount(ctx);
				final Integer freeCount = getFreeCount(ctx);

				if (promotionResult.getFired(ctx))
				{
					final double totalDiscount = promotionResult.getTotalDiscount(ctx);
					final Object[] args =
					{ qualifyingCount, freeCount, Double.valueOf(totalDiscount),
							Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount) };
					return formatMessage(getMessageFired(ctx), args, locale);
				}
				else if (promotionResult.getCouldFire(ctx))
				{
					minimumCategoryValue = populateCategoryMinValue(ctx);
					finalNumberOfProducts = getFinalNoOFProducts(qualifyingCount);

					final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(
							getRestrictions());
					String paymentModes = "";
					String deliveryModes = "";

					if (!restrictionList.isEmpty())
					{
						for (final AbstractPromotionRestriction restriction : restrictionList)
						{
							deliveryModes = populatDelModeMsgeData(restriction);
							paymentModes = populatPayModeMsgeData(restriction);
						}
					}

					final Object[] args = new Object[6];

					args[0] = Integer.valueOf(finalNumberOfProducts);
					args[1] = qualifyingCount;
					args[2] = freeCount;
					args[3] = minimumCategoryValue < 0.00D ? MarketplacecommerceservicesConstants.EMPTYSPACE : Double
							.valueOf(minimumCategoryValue);
					args[4] = populateMessageData(deliveryModes, paymentModes);
					return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
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

	/**
	 * Populate Delivery , Payment Mode details in Message : Sonar Fix
	 *
	 * @param deliveryModes
	 * @param paymentModes
	 * @return messageData
	 */
	private String populateMessageData(final String deliveryModes, final String paymentModes)
	{
		String messageData = MarketplacecommerceservicesConstants.EMPTYSTRING;
		if (!deliveryModes.isEmpty())
		{
			messageData = deliveryModes;
		}
		else if (!paymentModes.isEmpty())
		{
			messageData = paymentModes;
		}
		else
		{
			messageData = "purchase";
		}
		return messageData;
	}

	/**
	 * Populate Min Category Value
	 *
	 * @param ctx
	 * @return minimumCategoryValue
	 */
	private double populateCategoryMinValue(final SessionContext ctx)
	{
		double minimumCategoryValue = 0.00D;
		if (null != ctx && null != getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
		{
			minimumCategoryValue = ((Double) getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue();
		}
		return minimumCategoryValue;
	}

	/**
	 * Returns Final Count of Products
	 *
	 * @param qualifyingCount
	 * @return finalNumberOfProducts
	 */
	private int getFinalNoOFProducts(final Integer qualifyingCount)
	{
		int finalNumberOfProducts = 0;
		final int qCount = qualifyingCount.intValue();

		if (qCount > noOfProducts)
		{
			finalNumberOfProducts = qCount - noOfProducts;
		}
		else
		{
			finalNumberOfProducts = GenericUtilityMethods.calculateNoOfProducts(qCount, noOfProducts);
		}
		return finalNumberOfProducts;
	}


	/**
	 * Populate Delivery Mode Data For Message
	 *
	 * @param restriction
	 * @return deliveryModes
	 */
	private String populatDelModeMsgeData(final AbstractPromotionRestriction restriction)
	{
		String deliveryModes = MarketplacecommerceservicesConstants.EMPTYSTRING;

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
		return deliveryModes;
	}

	/**
	 * Populate Payment Mode Data For Message
	 *
	 * @param restriction
	 * @return paymentModes
	 */
	private String populatPayModeMsgeData(final AbstractPromotionRestriction restriction)
	{
		String paymentModes = MarketplacecommerceservicesConstants.EMPTYSTRING;

		if (restriction instanceof PaymentModeSpecificPromotionRestriction)
		{
			final List<PaymentType> paymentModeList = ((PaymentModeSpecificPromotionRestriction) restriction).getPaymentModes();
			if (paymentModeList.size() == 1)
			{
				paymentModes = paymentModes + paymentModeList.get(0).getMode();
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

		return paymentModes;
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
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList)
	{
		final List<PromotionResult> results = new ArrayList<PromotionResult>();
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;
		final int qualifyingCount = getQualifyingCount(paramSessionContext).intValue(); // Get the Promotion set Qualifying count
		final Map<AbstractOrderEntry, Double> customBogoPromoDataMap = new HashMap<>();

		LOG.debug("Qualifying Count for Promotion" + qualifyingCount);
		try
		{
			if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, paramSessionContext,
					paramPromotionEvaluationContext, this, restrictionList))
			{

				final int freeCount = getFreeCount(paramSessionContext).intValue();
				LOG.debug("Free Count for Promotion" + freeCount);
				//getting eligible Product List
				final List<Product> eligibleProductList = new ArrayList<Product>();
				for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
				{
					eligibleProductList.add(orderEntry.getProduct());
				}
				final PromotionOrderView orderView = paramPromotionEvaluationContext.createView(paramSessionContext, this,
						eligibleProductList);
				int totalQty = 0;
				for (final AbstractOrderEntry entry : validProductUssidMap.values())
				{
					totalQty += entry.getQuantityAsPrimitive();
				}
				noOfProducts = totalQty;
				List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;

				final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();
				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
				{
					tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
				}

				if (totalQty >= qualifyingCount)
				{
					final Map<String, Integer> qCMapForCatLevelBOGO = getDefaultPromotionsManager().getSortedValidProdUssidMap(
							validProductUssidMap, totalQty, qualifyingCount, paramSessionContext, restrictionList);

					flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForABPromo(restrictionList,
							validProductUssidMap);
					//for payment mode restriction check
					flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
							paramSessionContext);

					if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval)
					{
						//paramPromotionEvaluationContext.startLoggingConsumed(this);
						final Comparator comparator = PromotionEvaluationContext.createPriceComparator(paramSessionContext); // Comparing price

						final int totalFactorCount = totalQty / qualifyingCount;
						final int validFreeCount = totalFactorCount * freeCount;
						final Map<String, Integer> QCMapForFreeItems = new HashMap<String, Integer>();


						getDefaultPromotionsManager().populateSortedValidProdUssidMap(validProductUssidMap, validFreeCount,
								paramSessionContext, restrictionList, QCMapForFreeItems);

						final List<PromotionOrderEntryConsumed> freeItems = consumeFromHead(paramSessionContext, comparator,
								validFreeCount, orderView.getAllEntries(paramSessionContext), QCMapForFreeItems, tcMapForValidEntries);


						for (final String freeItemUssid : QCMapForFreeItems.keySet())
						{
							final int validItemQty = qCMapForCatLevelBOGO.get(freeItemUssid).intValue();
							final int freeItemQty = QCMapForFreeItems.get(freeItemUssid).intValue();
							qCMapForCatLevelBOGO.put(freeItemUssid, Integer.valueOf(validItemQty - freeItemQty));

						}

						final List<PromotionOrderEntryConsumed> consumedItemsFromTail = consumeFromTail(paramSessionContext,
								comparator, totalFactorCount, orderView.getAllEntries(paramSessionContext), qCMapForCatLevelBOGO,
								tcMapForValidEntries);

						final List actions = new ArrayList();
						Map<String, List<String>> productAssociatedItemsMap = null;

						if (qCMapForCatLevelBOGO.size() == 1)
						{
							productAssociatedItemsMap = new HashMap<String, List<String>>();
							final String ussidForSingleLineEntryBOGO = qCMapForCatLevelBOGO.keySet().iterator().next();
							productAssociatedItemsMap.put(ussidForSingleLineEntryBOGO,
									new ArrayList<String>(qCMapForCatLevelBOGO.keySet()));
						}
						else
						{
							productAssociatedItemsMap = getDefaultPromotionsManager().getAssociatedItemsForAorBOGOorFreebiePromotions(
									validProductUssidMap, null);
						}

						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS,
								productAssociatedItemsMap);
						paramSessionContext
								.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));

						//For setting qualifying count
						int qalifyingCount = 0;
						final Map<String, Integer> qCount = new HashMap<String, Integer>();
						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							final AbstractOrderEntry entry = mapEntry.getValue();
							final String selectedUssid = mapEntry.getKey();
							final int totalQtyForEntry = entry.getQuantity().intValue();

							if (QCMapForFreeItems.containsKey(selectedUssid))
							{
								final int freeItemQty = QCMapForFreeItems.get(selectedUssid).intValue();

								qalifyingCount = totalQtyForEntry - freeItemQty;
							}
							else
							{
								qalifyingCount = totalQtyForEntry;
							}
							qCount.put(selectedUssid, Integer.valueOf(qalifyingCount));
						}

						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, qCount);
						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.FREEITEMFORCATBOGO, QCMapForFreeItems);

						for (final PromotionOrderEntryConsumed poec : freeItems)
						{
							poec.setAdjustedUnitPrice(paramSessionContext, 0.01D);
							double adjustment = poec.getEntryPrice(paramSessionContext) * -1.0D; // Calculating the adjustment
							adjustment += (0.01D * poec.getQuantity().intValue());
							customBogoPromoDataMap.put(poec.getOrderEntry(paramSessionContext), Double.valueOf(adjustment));
						}

						//if (customBogoPromoDataMap.size() > 0)
						if (MapUtils.isNotEmpty(customBogoPromoDataMap))
						{
							LOG.debug("Populating the BOGO Promotion Details ");
							LOG.debug("Creating Action for BOGO Promotion");
							actions.add(getDefaultPromotionsManager().createCustomBOGOPromoOrderEntryAdjustAction(paramSessionContext,
									customBogoPromoDataMap, true));
						}

						final PromotionResult result = getDefaultPromotionsManager().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 1.0F);
						final List<PromotionOrderEntryConsumed> totalConsumedItems = new ArrayList<PromotionOrderEntryConsumed>(
								consumedItemsFromTail);
						//totalConsumedItems = paramPromotionEvaluationContext.finishLoggingAndGetConsumed(this, true);
						totalConsumedItems.addAll(freeItems);
						result.setConsumedEntries(paramSessionContext, totalConsumedItems);
						result.setActions(paramSessionContext, actions);
						results.add(result);
					}
				}

				//Setting remaining items
				remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
						orderView.getAllEntries(paramSessionContext), tcMapForValidEntries);

				if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L) // For Localization: To check for Excluded Products
				{

					final float certainty = (float) remainingItemsFromTail.size() / qualifyingCount;

					final PromotionResult result = getDefaultPromotionsManager().createPromotionResult(paramSessionContext, this,
							paramPromotionEvaluationContext.getOrder(), certainty);
					result.setConsumedEntries(remainingItemsFromTail);
					results.add(result);
				}
			}
			else
			{
				//certainty check
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
						paramPromotionEvaluationContext.getOrder(), 0.00F);
				results.add(result);
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

		return results;
	}

	@Override
	protected void buildDataUniqueKey(final SessionContext ctx, final StringBuilder builder)
	{
		super.buildDataUniqueKey(ctx, builder);

		builder.append(getQualifyingCount(ctx)).append('|');
		builder.append(getFreeCount(ctx)).append('|');
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
