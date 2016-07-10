package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


public class BuyAAboveXGetPercentageOrAmountOff extends GeneratedBuyAAboveXGetPercentageOrAmountOff
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAAboveXGetPercentageOrAmountOff.class.getName());
	private List<Product> excludedProductList = null;
	private boolean productFlag = false;
	private boolean categoryFlag = false;
	private int noOfProducts = 0;
	private Map<AbstractOrderEntry, String> productSellerDetails = null;

	/**
	 * @Description : This method is for creating item type
	 * @param :
	 *           ctx
	 * @param :
	 *           type
	 * @param :
	 *           allAttributes
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
	 * @Description : Buy A Above X and get y percentage/amount off - this is for seller brand threshold promotion
	 * @param :
	 *           ctx
	 * @param :
	 *           evaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext evaluationContext)
	{
		LOG.debug("Buy A Above X and get y percentage/amount off");
		productSellerDetails = new HashMap<AbstractOrderEntry, String>();

		boolean isMultipleSeller = false;

		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, evaluationContext);
		boolean checkChannelFlag = false;
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

		try
		{
			excludedProductList = new ArrayList<Product>();
			GenericUtilityMethods.populateExcludedProductManufacturerList(ctx, evaluationContext, excludedProductList, null,
					restrictionList, this);
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			Object cartChennal = null;
			final AbstractOrder cart = evaluationContext.getOrder();
			try
			{
				if (cart instanceof de.hybris.platform.jalo.order.Cart)
				{
					cartChennal = cart.getAttribute(ctx, CartModel.CHANNEL);
				}
				else if (cart instanceof Order)
				{
					cartChennal = cart.getAttribute(ctx, OrderModel.SALESAPPLICATION);
				}
			}
			catch (final Exception ex)
			{
				LOG.error(ex.getMessage());
			}
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel,
					cartChennal == null ? null : (EnumerationValue) cartChennal);

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag) // if Restrictions return valid && Channel is valid
			{
				LOG.debug("Promotion Restriction and Channel Satisfied");
				final Map<String, AbstractOrderEntry> validProductUssidMap = getValidProductList(cart, ctx);

				if (!getDefaultPromotionsManager().promotionAlreadyFired(ctx, validProductUssidMap))
				{
					isMultipleSeller = getMplPromotionHelper().checkMultipleSeller(restrictionList);
					if (isMultipleSeller)
					{
						Map<String, Map<String, AbstractOrderEntry>> multiSellerValidUSSIDMap = new HashMap<String, Map<String, AbstractOrderEntry>>();
						multiSellerValidUSSIDMap = getMplPromotionHelper().populateMultiSellerData(validProductUssidMap,
								productSellerDetails, ctx);
						for (final Map.Entry<String, Map<String, AbstractOrderEntry>> multiSellerData : multiSellerValidUSSIDMap
								.entrySet())
						{
							final Map<String, AbstractOrderEntry> validMultiUssidMap = multiSellerData.getValue();
							if (null != validProductUssidMap && !validProductUssidMap.isEmpty())
							{
								List<PromotionResult> promotionResultList = new ArrayList<PromotionResult>(); // This needs to be done for Multiple Seller Integration
								final Double totalEligibleProductPrice = getTotalofEligibleProducts(validMultiUssidMap);
								promotionResultList = promotionEvaluation(ctx, evaluationContext, validMultiUssidMap,
										totalEligibleProductPrice);
								promotionResults.addAll(promotionResultList);
							}
						}
					}
					else
					{
						final Double totalEligibleProductPrice = getTotalofEligibleProducts(validProductUssidMap);
						promotionResults = promotionEvaluation(ctx, evaluationContext, validProductUssidMap, totalEligibleProductPrice);
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
		return promotionResults;
	}

	/**
	 * @Description : For Promotion Evaluation
	 * @param ctx
	 * @param evaluationContext
	 * @param validProductUssidMap
	 * @param totalEligibleProductPrice
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext ctx, final PromotionEvaluationContext evaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final Double totalEligibleProductPrice)
	{

		boolean flagForDeliveryModeRestrEval = false;
		boolean flagForPaymentModeRestrEval = false;
		//double percentageDiscount = Double.valueOf(0.0D).doubleValue();	SONAR Fix
		double percentageDiscount = 0.0D;
		double totalProductPrice = 0.0D;
		final AbstractOrder cart = evaluationContext.getOrder();
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

		final PromotionsManager promotionsManager = PromotionsManager.getInstance();
		Double thresholdVal = getPriceForOrder(ctx, getThresholdTotals(ctx), cart,
				MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
		final Double discountPrice = getPriceForOrder(ctx, getDiscountPrices(ctx), cart,
				MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null
						? (Double) getPriceForOrder(ctx, getDiscountPrices(ctx), cart,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES)
						: new Double(0.0);

		if (null == thresholdVal)
		{
			thresholdVal = Double.valueOf(0.0D);
		}


		//for delivery mode restriction check
		flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
				validProductUssidMap);
		//for payment mode restriction check
		flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList, ctx);

		if (null != thresholdVal && totalEligibleProductPrice.doubleValue() >= thresholdVal.doubleValue()
				&& flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval)
		{
			boolean isPercentageDisc = false;
			if (isPercentageOrAmount().booleanValue() && null != getPercentageDiscount())
			{
				percentageDiscount = getPercentageDiscount().doubleValue();
				LOG.debug("Percentage Discount" + percentageDiscount);
				isPercentageDisc = true;
			}
			else
			{
				totalProductPrice = totalEligibleProductPrice.doubleValue();
				if (totalProductPrice > 0)
				{
					percentageDiscount = (discountPrice.doubleValue() * 100) / totalProductPrice;
					LOG.debug("Amount Discount" + percentageDiscount);
				}
			}

			//getting eligible Product List from ussid map
			final List<Product> eligibleProductList = new ArrayList<Product>();
			for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
			{
				eligibleProductList.add(orderEntry.getProduct());
			}
			final Map<String, Integer> qCount = new HashMap<String, Integer>();
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				qCount.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
			}

			final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
					.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, null);

			// Apportioning Code Implementation
			ctx.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT, Double.valueOf(percentageDiscount));
			ctx.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE, totalEligibleProductPrice);
			ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
			ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, qCount);
			ctx.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
			ctx.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
			ctx.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));

			//for (final Map.Entry<Product, Integer> mapEntry : validProductList.entrySet())
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				evaluationContext.startLoggingConsumed(this);
				final AbstractOrderEntry entry = mapEntry.getValue();
				final PromotionOrderView view = evaluationContext.createView(ctx, this, eligibleProductList);
				final PromotionOrderEntry viewEntry = view.peek(ctx);
				final long quantityOfOrderEntry = viewEntry.getBaseOrderEntry().getQuantity(ctx).longValue();
				LOG.debug("BaseOrderEntry" + quantityOfOrderEntry);
				final double percentageDiscountvalue = percentageDiscount / 100.0D;

				if (percentageDiscount < 100)
				{
					final double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue
							* entry.getQuantity().doubleValue());
					LOG.debug("Adjustment" + adjustment);
					final PromotionResult result = promotionsManager.createPromotionResult(ctx, this, evaluationContext.getOrder(),
							1.0F);
					final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
							.createCustomPromotionOrderEntryAdjustAction(ctx, entry, quantityOfOrderEntry, adjustment);
					final List consumed = evaluationContext.finishLoggingAndGetConsumed(this, true);
					result.setConsumedEntries(ctx, consumed);
					result.addAction(ctx, poeac);
					promotionResults.add(result);
				}
			}
		}
		else
		{
			if (getNoOfProducts() > 0 && flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval)
			{
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
						evaluationContext.getOrder(), 0.00F);
				promotionResults.add(result);
			}
		}
		return promotionResults;

	}

	/**
	 * @Description : Returns the Total price for eligible Products
	 * @param validProductUssidMap
	 * @return totalVal
	 */

	private Double getTotalofEligibleProducts(final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		Double totalVal = Double.valueOf(0.0D);
		int count = 0;

		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
		{
			final AbstractOrderEntry entry = mapEntry.getValue();
			totalVal = Double
					.valueOf(totalVal.doubleValue() + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue()));
			count = count + 1;
		}

		setNoOfProducts(count);
		LOG.debug("No: of Valid Products" + getNoOfProducts());
		LOG.debug("Total Eligible Product Price Value" + totalVal);
		return totalVal;
	}

	/**
	 * @Description : Populates the valid Products in Cart
	 * @param cart
	 * @param ctx
	 * @return validProductList
	 */
	private Map<String, AbstractOrderEntry> getValidProductList(final AbstractOrder cart, final SessionContext ctx)
	{
		//final Map<String, Integer> validUssidList = new HashMap<String, Integer>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		try
		{
			boolean sellerFlag = false;
			boolean brandFlag = false;
			String sellerID = MarketplacecommerceservicesConstants.EMPTY;
			final List<Product> promotionProductList = new ArrayList<>(getProducts());
			final List<Category> promotionCategoryList = new ArrayList<>(getCategories());
			//int productQty = 0;
			for (final AbstractOrderEntry entry : cart.getEntries())
			{
				boolean applyPromotion = false;
				final Product product = entry.getProduct();
				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList))
				{
					continue;
				}
				//checking product is a valid product for promotion
				if (!promotionProductList.isEmpty())
				{
					if (promotionProductList.contains(product))
					{
						applyPromotion = true;
						productFlag = true;
						brandFlag = true;
						sellerFlag = getDefaultPromotionsManager().checkSellerData(ctx, restrictionList, entry);
					}
				}
				else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
				//checking product category is permitted by promotion category or not
				{
					final List<String> productCategoryList = getDefaultPromotionsManager().getcategoryList(product, ctx);
					applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
					sellerFlag = getDefaultPromotionsManager().checkSellerData(ctx, restrictionList, entry);
					if (applyPromotion && sellerFlag && brandFlag)
					{
						categoryFlag = true;
					}
				}

				if (applyPromotion && sellerFlag && brandFlag)
				{
					validProductUssidMap.putAll(
							getDefaultPromotionsManager().populateValidProductUssidMap(product, cart, restrictionList, ctx, entry));
					sellerID = getDefaultPromotionsManager().getSellerID(ctx, restrictionList, entry);
					productSellerDetails.put(entry, sellerID);
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
		return validProductUssidMap;
	}


	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           ctx
	 * @param :
	 *           result
	 * @param :
	 *           locale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{
		resetFlags();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		String firedData = MarketplacecommerceservicesConstants.EMPTYSPACE;
		String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
		Double thresholdVal = Double.valueOf(0.00D);
		String sellerData = MarketplacecommerceservicesConstants.EMPTYSPACE;

		if (result.getFired(ctx))
		{
			LOG.debug("The Total Discount" + result.getTotalDiscount(ctx));
			firedData = messageData();
			currency = getCurrency(ctx);
			final Object[] args =
			{ firedData, currency, Double.valueOf(result.getTotalDiscount(ctx)) };
			return formatMessage(this.getMessageFired(ctx), args, locale);
		}
		else if (result.getCouldFire(ctx))
		{
			getValidProductList(result.getOrder(), ctx);
			data = messageData();
			currency = getCurrency(ctx);
			thresholdVal = getThresholdValForMsg(ctx, result);
			sellerData = getMplPromotionHelper().sellerDataForMsg(ctx, restrictionList);
			final Object[] args =
			{ data, currency, thresholdVal, sellerData };
			return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale);
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

	/**
	 * @Description: Returns the Currency of the Order
	 * @param ctx
	 * @return currency
	 */
	private String getCurrency(final SessionContext ctx)
	{
		String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != ctx.getCurrency() && null != ctx.getCurrency().getIsocode())
		{
			currency = ctx.getCurrency().getIsocode();
			if (null == currency)
			{
				currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
			}
		}
		return currency;
	}


	/**
	 * @Description: Message Localization
	 * @return: String
	 */
	private String messageData()
	{
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		final StringBuffer messageData = new StringBuffer();
		messageData.append(MarketplacecommerceservicesConstants.EMPTYSPACE);

		if (productFlag && null != getProducts() && !getProducts().isEmpty() && getProducts().size() > 0)
		{
			for (final Product product : getProducts())
			{
				messageData.append(product.getName());
				messageData.append(',');
			}
		}
		else if (categoryFlag && null != getCategories() && !getCategories().isEmpty() && getCategories().size() > 0)
		{
			for (final Category category : getCategories())
			{
				messageData.append(category.getName());
				messageData.append(',');
			}
		}

		data = messageData.toString();
		return data;
	}

	/**
	 * @Description: Reset the boolean flags
	 * @return: void
	 */
	private void resetFlags()
	{
		productFlag = false;
		categoryFlag = false;
	}

	/**
	 * @Description : Get Threshold value for Promotional Message
	 * @param ctx
	 * @param result
	 * @return threshold
	 */
	private Double getThresholdValForMsg(final SessionContext ctx, final PromotionResult result)
	{
		Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), result.getOrder(),
				MarketplacecommerceservicesConstants.THRESHOLD_TOTALS);
		if (null == threshold)
		{
			threshold = Double.valueOf(0.00D);
		}
		return threshold;
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}


	/**
	 * Getter Setter Declarations For Sonar
	 */

	/**
	 * @return the productFlag
	 */
	public boolean isProductFlag()
	{
		return productFlag;
	}

	/**
	 * @param productFlag
	 *           the productFlag to set
	 */
	public void setProductFlag(final boolean productFlag)
	{
		this.productFlag = productFlag;
	}

	/**
	 * @return the categoryFlag
	 */
	public boolean isCategoryFlag()
	{
		return categoryFlag;
	}

	/**
	 * @param categoryFlag
	 *           the categoryFlag to set
	 */
	public void setCategoryFlag(final boolean categoryFlag)
	{
		this.categoryFlag = categoryFlag;
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
	 * @return the productSellerDetails
	 */
	public Map<AbstractOrderEntry, String> getProductSellerDetails()
	{
		return productSellerDetails;
	}

	/**
	 * @param productSellerDetails
	 *           the productSellerDetails to set
	 */
	public void setProductSellerDetails(final Map<AbstractOrderEntry, String> productSellerDetails)
	{
		this.productSellerDetails = productSellerDetails;
	}

	/**
	 * @return the excludedProductList
	 */
	public List<Product> getExcludedProductList()
	{
		return excludedProductList;
	}

}
