package com.tisl.mpl.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionPriceRow;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.pojo.MplLimitedOfferData;
import com.tisl.mpl.promotion.helper.MplBundlePromotionHelper;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;



public class FixedPricePromotion extends GeneratedFixedPricePromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FixedPricePromotion.class.getName());

	private int noOfProducts = 0;
	private int stockCount = 0;


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
	 * @Description : FixedPricePromotion
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @return promotionResults
	 */

	@SuppressWarnings("deprecation")
	@Override
	public final List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		LOG.debug("Inside FixedPricePromotion evaluate method");
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List
		boolean checkChannelFlag = false;
		final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();
		try
		{
			@SuppressWarnings("deprecation")
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);
			final PromotionsManager.RestrictionSetResult rsr = getDefaultPromotionsManager().findEligibleProductsInBasket(
					paramSessionContext, paramPromotionEvaluationContext, this, getCategories());

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag)
			{
				final Map<String, List<String>> productAssociatedItemsFinalMap = new ConcurrentHashMap<String, List<String>>();
				final List<Product> allowedProductList = new ArrayList<Product>(rsr.getAllowedProducts());
				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofA(cart, paramSessionContext, allowedProductList, restrictionList, null, null); // Adding Eligible Products to List

				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap)
						&& MapUtils.isNotEmpty(validProductUssidMap))
				{
					promotionResults = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext, validProductUssidMap,
							restrictionList, allowedProductList, cart, productAssociatedItemsFinalMap);
				}
				paramSessionContext
						.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsFinalMap);

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
	 * FixedPricePromotion Evaluation
	 *
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @param restrictionList
	 * @param allowedProductList
	 * @param cart
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> allowedProductList, final AbstractOrder cart,
			final Map<String, List<String>> productAssociatedItemsFinalMap)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager promotionsManager = PromotionsManager.getInstance();
		final Long qualifyingCount = getQuantity();
		LOG.debug("The qualifying count is " + qualifyingCount);
		final Collection promotionPriceRows = getProductFixedUnitPrice(paramSessionContext);
		int totalCount = 0;
		boolean isExhausted = false;
		double percentageDiscount = 0.0D;
		final Double fixedUnitPrice = getPriceForOrder(paramSessionContext, promotionPriceRows, cart, "productFixedUnitPrice");
		LOG.debug("The fixed unit price is " + fixedUnitPrice);
		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;
		boolean flagForPincodeRestriction = false;

		try
		{
			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
			}

			if (getMplPromotionHelper().validateForStockRestriction(restrictionList) && null != qualifyingCount
					&& qualifyingCount.intValue() > 0)
			{
				final int offerQuantity = (totalCount / qualifyingCount.intValue());
				final MplLimitedOfferData data = getMplPromotionHelper().checkCustomerRedeemCount(restrictionList, this.getCode(),
						cart, qualifyingCount.intValue());

				isExhausted = data.isExhausted();
				final int customerOfferCount = data.getActualCustomerCount();
				final int eligibleStockCount = getDefaultPromotionsManager().getStockRestrictionVal(restrictionList)
						* qualifyingCount.intValue();

				if (customerOfferCount > 0)
				{
					setStockCount(qualifyingCount.intValue() * customerOfferCount);
				}
				else
				{
					setStockCount(eligibleStockCount);
				}
				if (customerOfferCount > 0 && offerQuantity > customerOfferCount)
				{
					totalCount = (qualifyingCount.intValue() * customerOfferCount);
				}
				else if (totalCount >= eligibleStockCount)
				{
					totalCount = eligibleStockCount;
				}
			}

			if (!isExhausted)
			{
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
						validProductUssidMap, cart);
				flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
						paramSessionContext);
				flagForPincodeRestriction = getDefaultPromotionsManager().checkPincodeSpecificRestriction(restrictionList, cart);

				LOG.debug("The totalCount is " + totalCount);

				final boolean hasValidPromotionPriceRow = hasPromotionPriceRowForCurrency(cart, promotionPriceRows);

				List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;
				final Map<String, Integer> tcMapForValidEntries = new ConcurrentHashMap<String, Integer>();

				noOfProducts = totalCount;

				final PromotionOrderView view = paramPromotionEvaluationContext.createView(paramSessionContext, this,
						allowedProductList);
				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
				{
					tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
				}

				if (totalCount >= qualifyingCount.intValue() && hasValidPromotionPriceRow)
				{
					final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
							validProductUssidMap, totalCount, qualifyingCount.longValue(), paramSessionContext, restrictionList,
							getCode());//for getting the validProductList eligible for qualifying for promotion

					if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval && flagForPincodeRestriction)
					{
						// final Currency currency = paramPromotionEvaluationContext.getOrder().getCurrency(paramSessionContext);
						final double totalBasePriceForValidProducts = getDefaultPromotionsManager().getTotalValidProdPrice(
								validProductUssidMap, validProductList);
						LOG.debug("The totalBasePriceForValidProducts is " + totalBasePriceForValidProducts);

						final double totalValidProdFixedPrice = getDefaultPromotionsManager().getTotalValidProdFixedPrice(
								validProductUssidMap, validProductList, fixedUnitPrice.doubleValue());
						LOG.debug("The totalValidProdFixedPrice is " + totalValidProdFixedPrice);

						final double totaldiscountValue = totalBasePriceForValidProducts - totalValidProdFixedPrice;
						LOG.debug("The totaldiscountValue is " + totaldiscountValue);

						percentageDiscount = (totaldiscountValue * 100) / totalBasePriceForValidProducts;
						LOG.debug("The percentageDiscount is " + percentageDiscount);

						final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
								.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, null);

						productAssociatedItemsFinalMap.putAll(productAssociatedItemsMap);

						// Apportioning Code Implementation
						paramSessionContext.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT,
								Double.valueOf(percentageDiscount));
						paramSessionContext
								.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));


						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							final AbstractOrderEntry entry = mapEntry.getValue();
							LOG.debug("The entry is " + entry);
							final String validUssid = mapEntry.getKey();
							LOG.debug("The entry validUssid is " + validUssid);
							final long quantityOfOrderEntry = entry.getQuantity(paramSessionContext).longValue();
							LOG.debug("The quantityOfOrderEntry is " + quantityOfOrderEntry);
							final int eligibleCount = validProductList.get(validUssid).intValue();
							LOG.debug("The entry eligibleCount is " + eligibleCount);
							final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
							consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, eligibleCount,
									quantityOfOrderEntry, entry));

							tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - eligibleCount));

							for (final PromotionOrderEntryConsumed poec : consumed)
							{
								poec.setAdjustedUnitPrice(paramSessionContext, fixedUnitPrice);
							}

							final double adjustment = eligibleCount
									* (fixedUnitPrice.doubleValue() - entry.getBasePrice(paramSessionContext).doubleValue());
							final PromotionResult result = promotionsManager.createPromotionResult(paramSessionContext, this,
									paramPromotionEvaluationContext.getOrder(), 1.0F);
							final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
									.createCustomPromotionOrderEntryAdjustAction(paramSessionContext, entry, eligibleCount, adjustment);

							result.setConsumedEntries(paramSessionContext, consumed);
							result.addAction(paramSessionContext, poeac);
							promotionResults.add(result);
						}
					}
				}
				//Setting remaining items
				remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(paramSessionContext,
						view.getAllEntries(paramSessionContext), tcMapForValidEntries);
				if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L
						&& !getMplPromotionHelper().validateForStockRestriction(restrictionList)) // For Localization: To check for Excluded Products
				{
					final float certainty = (float) remainingItemsFromTail.size() / qualifyingCount.intValue();

					if (certainty < 1.0F && certainty > 0.0F)
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), certainty);
						result.setConsumedEntries(remainingItemsFromTail);
						promotionResults.add(result);
					}

				}
				else if (getMplPromotionHelper().validateForStockRestriction(restrictionList))
				{
					if (noOfProducts >= getStockCount())
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 1.00F);
						promotionResults.add(result);
					}
					else if (noOfProducts < getStockCount() && (noOfProducts % qualifyingCount.intValue() == 0))
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 1.00F);
						promotionResults.add(result);
					}
					else if (noOfProducts < getStockCount() && !(noOfProducts % qualifyingCount.intValue() == 0))
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
								paramPromotionEvaluationContext.getOrder(), 0.00F);
						promotionResults.add(result);
					}
				}
			}
		}
		catch (final JaloInvalidParameterException exception)
		{
			LOG.error(exception.getMessage());
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("Error in FixedPricePromotion EtailBusinessExceptions" + e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Error in FixedPricePromotion EtailNonBusinessExceptions" + e);
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
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
		String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != paramSessionContext.getCurrency() && null != paramSessionContext.getCurrency().getIsocode())
		{
			currency = paramSessionContext.getCurrency().getIsocode();
			if (null == currency)
			{
				currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
			}
		}
		int finalNumberOfProducts = 0;
		final AbstractOrder order = paramPromotionResult.getOrder(paramSessionContext);
		if (null != order)
		{
			if (paramPromotionResult.getFired(paramSessionContext))
			{
				final Object[] args = {};
				return formatMessage(this.getMessageFired(paramSessionContext), args, paramLocale);
			}
			else if (paramPromotionResult.getCouldFire(paramSessionContext))
			{
				final Collection promotionPriceRows = getProductFixedUnitPrice(paramSessionContext);
				final Double fixedUnitPrice = getPriceForOrder(paramSessionContext, promotionPriceRows, order,
						"productFixedUnitPrice");
				LOG.debug("The fixed unit price is " + fixedUnitPrice);
				/*
				 * final Object[] args = {}; return formatMessage(this.getMessageCouldHaveFired(paramSessionContext), args,
				 * paramLocale);
				 */
				final Long qualifyingCount = getQuantity(paramSessionContext);
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
				final Object[] args = new Object[7];


				args[0] = Integer.valueOf(finalNumberOfProducts);
				args[1] = qualifyingCount;
				args[2] = fixedUnitPrice;
				args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
				args[4] = MarketplacecommerceservicesConstants.EMPTYSPACE;

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

				args[6] = currency;
				return formatMessage(this.getMessageCouldHaveFired(paramSessionContext), args, paramLocale);

			}
		}
		return MarketplacecommerceservicesConstants.EMPTYSPACE;
	}




	private boolean hasPromotionPriceRowForCurrency(final AbstractOrder order,
			final Collection<PromotionPriceRow> promotionPriceRows)
	{
		if (promotionPriceRows.isEmpty())
		{
			LOG.warn(" has no PromotionPriceRow. Skipping evaluation");
			return false;
		}
		else
		{
			return true;
		}
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
	 * @return the stockCount
	 */
	public int getStockCount()
	{
		return stockCount;
	}




	/**
	 * @param stockCount
	 *           the stockCount to set
	 */
	public void setStockCount(final int stockCount)
	{
		this.stockCount = stockCount;
	}



}
