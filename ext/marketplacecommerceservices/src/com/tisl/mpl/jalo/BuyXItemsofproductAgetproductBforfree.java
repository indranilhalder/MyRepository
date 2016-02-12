package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
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


public class BuyXItemsofproductAgetproductBforfree extends GeneratedBuyXItemsofproductAgetproductBforfree
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyXItemsofproductAgetproductBforfree.class.getName());
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
	 * @Description : Buy x no of Product A Get B Free
	 * @param :
	 *           SessionContext ctx ,PromotionEvaluationContext promoContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		boolean flagForDeliveryModeRestrEval = false;
		final List<PromotionResult> results = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions()); // Fetching Promotion set Restrictions
		final AbstractOrder order = promoContext.getOrder();
		final List<Product> promotionProductList = new ArrayList<>(getProducts()); // Fetching Promotion set Primary Products
		final List<Category> promotionCategoryList = new ArrayList<>(getCategories()); // Fetching Promotion set Primary Categories
		// Validating Exclude Manufacturer and Adding excluded Products to their respective lists
		final List<Product> excludedProductList = new ArrayList<Product>();
		final List<String> excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(ctx, promoContext, excludedProductList,
				excludeManufactureList, restrictionList, this);
		final List<String> skuFreebieList;
		final Map<String, Product> freegiftInfoMap;

		try
		{
			final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext); //Validating Restrictions
			boolean checkChannelFlag = false;
			boolean sellerFlag = false;
			sellerFlag = getDefaultPromotionsManager().isSellerRestrExists(restrictionList);
			//Verifying the Channel Data
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(ctx,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = promoContext.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram


			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag && sellerFlag)
			{
				final List<String> sellerIDData = new ArrayList<String>();
				final Map<AbstractOrderEntry, String> eligibleProductMap = new HashMap<AbstractOrderEntry, String>();
				//getting the valid products
				final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager()
						.getValidProdListForBuyXofAPromo(order, ctx, promotionProductList, promotionCategoryList, restrictionList,
								excludedProductList, excludeManufactureList, sellerIDData, eligibleProductMap);

				if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, ctx, promoContext, this,
						restrictionList) && !getDefaultPromotionsManager().promotionAlreadyFired(ctx, validProductUssidMap))
				{
					final int qualifyingCount = getQualifyingCount(ctx).intValue();
					int realQuantity = 0;
					for (final AbstractOrderEntry entry : validProductUssidMap.values())
					{
						realQuantity += entry.getQuantity().intValue(); // Fetches total count of Valid Products
					}
					noOfProducts = realQuantity;

					flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
							validProductUssidMap);

					List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;

					//getting eligible Product List
					final List<Product> eligibleProductList = new ArrayList<Product>();
					for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
					{
						eligibleProductList.add(orderEntry.getProduct());
					}

					final PromotionOrderView view = promoContext.createView(ctx, this, eligibleProductList);
					//final PromotionOrderEntry viewEntry = view.peek(ctx);

					final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();
					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
					}

					if (realQuantity >= qualifyingCount && flagForDeliveryModeRestrEval)
					{
						final Map<String, Integer> validProductList = getDefaultPromotionsManager()
								.getSortedValidProdUssidMap(validProductUssidMap, realQuantity, qualifyingCount, ctx, restrictionList);

						//Gift Products Could be multiple
						final List<Product> productList = (List<Product>) this.getGiftProducts(ctx);
						final int giftProductCount = realQuantity / qualifyingCount;
						ctx.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(giftProductCount)); // Setting Free gift details in Session Context
						ctx.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
						//promoContext.startLoggingConsumed(this);

						final List<PromotionOrderEntryConsumed> consumed = getDefaultPromotionsManager().getConsumedEntriesForFreebie(
								ctx, this, validProductUssidMap, validProductList, null, tcMapForValidEntries);

						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
								promoContext.getOrder(), 1.0F);
						//final List consumed = promoContext.finishLoggingAndGetConsumed(this, true);
						result.setConsumedEntries(ctx, consumed);

						if (null != productList && !productList.isEmpty())
						{
							final Map<String, Product> giftProductDetails = getDefaultPromotionsManager()
									.getGiftProductsUSSID(productList, sellerIDData); // Validating for Scenario: Eligible Products and Free Gift must be from the same DC
							if (null != giftProductDetails && !giftProductDetails.isEmpty())
							{
								getPromotionUtilityPOJO().setPromoProductList(eligibleProductList); // Adding Eligible Products for Scenario : One Product Promotion per eligible Product
								skuFreebieList = populateFreebieSKUIDs(giftProductDetails);
								freegiftInfoMap = populateFreebieDetails(giftProductDetails);
								int giftCount = 0;
								for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
								{
									giftCount = getDefaultPromotionsManager().getFreeGiftCount(entry.getKey(), eligibleProductMap,
											qualifyingCount);
								}
								if (giftCount > 0)
								{
									ctx.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(giftCount));
								}

								final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
										.getAssociatedItemsForAFreebiePromotions(validProductUssidMap, skuFreebieList);
								ctx.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
								ctx.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
								ctx.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductList);
								result.addAction(ctx, getDefaultPromotionsManager().createCustomPromotionOrderAddFreeGiftAction(ctx,
										freegiftInfoMap, result, Double.valueOf(giftCount)));

							}
						}
						results.add(result);

					}

					//Setting remaining items
					remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(ctx, view.getAllEntries(ctx),
							tcMapForValidEntries);

					if (noOfProducts > 0 && remainingItemsFromTail != null && remainingItemsFromTail.size() > 0L
							&& GenericUtilityMethods.checkRestrictionData(restrictionList))
					{// For Localization
					 //						final float certainty = remainingItemsFromTail != null ? (remainingItemsFromTail.isEmpty() ? 1.00F
					 //								: (float) remainingItemsFromTail.size() / qualifyingCount) : 0.00F;

						final float certainty = (float) remainingItemsFromTail.size() / qualifyingCount;

						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
								promoContext.getOrder(), certainty);
						result.setConsumedEntries(remainingItemsFromTail);
						results.add(result);
					}
				}
				else
				{
					//certainty check
					if (GenericUtilityMethods.checkRestrictionData(restrictionList))
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
								promoContext.getOrder(), 1.00F);
						results.add(result);
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
		return results;
	}


	/**
	 * Description : Populate Freebie Details *** For TISEE-5527
	 *
	 * @param giftProductDetails
	 * @return giftDetails
	 */
	private Map<String, Product> populateFreebieDetails(final Map<String, Product> giftProductDetails)
	{
		final Map<String, Product> giftDetails = new HashMap<>();
		for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
		{
			giftDetails.put(entry.getKey(), entry.getValue());
		}
		return giftProductDetails;
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
	 * @Description : Returns Minimum Category Amount
	 * @param :
	 *           SessionContext arg0
	 * @return : minimumCategoryValue
	 */
	private double calculateMinCategoryAmnt(final SessionContext arg0)
	{
		double minimumCategoryValue = 0.00D;
		if (null != arg0 && null != getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)
				&& ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() > 0.00D)
		{
			minimumCategoryValue = ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue();

		}
		return minimumCategoryValue;
	}


	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext ctx ,PromotionResult promotionResult ,Locale locale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		final Integer freeCount = Integer.valueOf(1);
		int finalNumberOfProducts = 0;
		try
		{
			if (promotionResult.getFired(ctx))
			{
				final Object[] args =
				{ this.getQualifyingCount(ctx), freeCount };
				return formatMessage(this.getMessageFired(ctx), args, locale);
			}
			else if (promotionResult.getCouldFire(ctx))
			{
				final Integer qualifyingCount = getQualifyingCount(ctx);
				final double minimumCategoryValue = calculateMinCategoryAmnt(ctx);
				final int qCount = getQualifyingCount(ctx).intValue();

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
	 *
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
}
