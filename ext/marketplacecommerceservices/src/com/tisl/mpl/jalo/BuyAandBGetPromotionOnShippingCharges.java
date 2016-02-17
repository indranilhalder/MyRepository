package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


public class BuyAandBGetPromotionOnShippingCharges extends GeneratedBuyAandBGetPromotionOnShippingCharges
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAandBGetPromotionOnShippingCharges.class.getName());

	private List<Product> excludedProductList = null;
	private List<String> excludeManufactureList = null;
	private int primaryListSize = 0;
	private int secondaryListSize = 0;
	private Map<String, AbstractOrderEntry> validProductUssidMap = null;
	private List<String> validProductListA = null;
	private List<String> validProductListB = null;
	private int totalFactorCount = 0;

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
	 * @Description : Buy Product A and B to get Percentage/Amount Discount on shipping charges or Free Shipping
	 * @param : SessionContext arg0 ,PromotionEvaluationContext arg1
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		boolean flagForDeliveryModeRestrEval;
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>(); // Method returns List of Type PromotionResult
		final AbstractOrder order = arg1.getOrder();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

		excludedProductList = new ArrayList<Product>();
		excludeManufactureList = new ArrayList<String>();
		// To filter out the Products on which Promotion is already applied
		GenericUtilityMethods.populateExcludedProductManufacturerList(arg0, arg1, excludedProductList, excludeManufactureList,
				restrictionList, this);
		//getDefaultPromotionsManager().promotionAlreadyFired(arg0, order, excludedProductList);

		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(arg0, arg1); // Validates Promotion Restrictions

		boolean checkChannelFlag = false;
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = arg1.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram
			final List<String> eligibleProductList = eligibleForPromotion(cart, arg0); // Gets the Eligible Product List

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag
					&& !(getSecondProducts().isEmpty() && getSecondCategories().isEmpty())
					&& GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, arg0, arg1, this, restrictionList)
					&& !getDefaultPromotionsManager().promotionAlreadyFired(arg0, validProductUssidMap)) // Validates the Restriction :Allows only if Valid and Valid Channel
			{
				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForABPromo(restrictionList,
						validProductUssidMap);
				if (!eligibleProductList.isEmpty()) //Apply percentage/amount discount to valid products
				{
					if (flagForDeliveryModeRestrEval)
					{
						final Map<String, Integer> qCount = getDefaultPromotionsManager().getQualifyingCountForABPromotion(
								eligibleProductList, totalFactorCount);
						final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
								.getAssociatedItemsForABorFreebiePromotions(validProductListA, validProductListB, null);
						final Map<String, String> fetchProductRichAttribute = getDefaultPromotionsManager().fetchProductRichAttribute(
								qCount);

						//for (final Map.Entry<Product, Integer> mapEntry : qCount.entrySet())
						for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
						{
							//arg1.startLoggingConsumed(this);
							final String validProductUSSID = mapEntry.getKey();
							final AbstractOrderEntry entry = mapEntry.getValue();
							final String fullfillmentTypeForProduct = fetchProductRichAttribute.get(validProductUSSID);
							if ((isTShipAsPrimitive() && isSShipAsPrimitive())
									|| ((fullfillmentTypeForProduct.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP) && isTShipAsPrimitive()) || (fullfillmentTypeForProduct
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP) && isSShipAsPrimitive())))
							{
								//*******Calculating delivery charges & setting it at entry level starts*******
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
										&& (getPriceForOrder(arg0, getDiscountPrices(arg0), order,
												MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
								{
									adjustedDeliveryCharge = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
											MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
								}
								else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
								{
									isDeliveryFreeFlag = true;
								}

								final Map<String, Map<String, Double>> apportionedProdDelChargeMap = getDefaultPromotionsManager()
										.updateDeliveryCharges(isDeliveryFreeFlag, isPercentageFlag, adjustedDeliveryCharge, qCount,
												fetchProductRichAttribute);
								//********Calculating delivery charges & setting it at entry level ends*********
								arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
								arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, qCount);
								arg0.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
								arg0.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
								arg0.setAttribute(MarketplacecommerceservicesConstants.PRODPREVCURRDELCHARGEMAP,
										apportionedProdDelChargeMap);

								final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
								consumed
										.add(getDefaultPromotionsManager().consume(arg0, this, totalFactorCount, totalFactorCount, entry));

								//add 0 as adjustment as no adjustment would be done for product price.
								final double adjustment = 0.0D;

								final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
										arg1.getOrder(), 1.0F);
								final CustomShippingChargesPromotionAdjustAction poeac = getDefaultPromotionsManager()
										.createCustomShippingChargesPromotionAdjustAction(arg0, entry, adjustment);
								//final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
								result.setConsumedEntries(arg0, consumed);
								result.addAction(arg0, poeac);
								promotionResults.add(result);
							}
						}
					}
					else
					{
						float certainty = 1.00F;
						if (primaryListSize != secondaryListSize) // For localization Purpose
						{
							certainty = 0.00F;
						}
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
								arg1.getOrder(), certainty);
						promotionResults.add(result);
					}
				}
				else
				{
					for (final AbstractOrderEntry entry : order.getEntries()) // For localization Purpose
					{
						final Product orderProduct = entry.getProduct();
						if (!excludedProductList.contains(orderProduct))
						{
							final float certainty = 0.00F;
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
									arg1.getOrder(), certainty);
							promotionResults.add(result);
						}
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
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext arg0 ,PromotionResult arg1 ,Locale arg2
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale locale)
	{
		try
		{
			final AbstractOrder order = arg1.getOrder(arg0);
			String currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
			if (null != arg0.getCurrency() && null != arg0.getCurrency().getIsocode())
			{
				currency = arg0.getCurrency().getIsocode();
				if (null == currency)
				{
					currency = MarketplacecommerceservicesConstants.EMPTYSPACE;
				}
			}
			String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
			final double minimumCategoryValue = calculateMinCategoryAmnt(arg0);
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
					&& (getPriceForOrder(arg0, getDiscountPrices(arg0), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null))
			{
				adjustedDeliveryCharge = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
						MarketplacecommerceservicesConstants.DISCOUNT_PRICES).doubleValue();
				discPerOrAmtStr.append(currency);
				discPerOrAmtStr.append(String.valueOf(adjustedDeliveryCharge));
			}
			else if (discountType.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FREE))
			{
				discPerOrAmtStr.append(MarketplacecommerceservicesConstants.FREE);
			}

			if (order != null)
			{
				//final Currency orderCurrency = order.getCurrency(arg0);
				if (arg1.getFired(arg0))
				{
					// if (!getDefaultPromotionsManager().fetchUssidMplZoneDeliveryCost().isEmpty())
					// {
					final Object[] args =
					{ discPerOrAmtStr };
					return formatMessage(this.getMessageFired(arg0), args, locale);
					//TODO
					//	}
				}
				else if (arg1.getCouldFire(arg0))
				{
					eligibleForPromotion(order, arg0);

					if (getProducts() != null && !getProducts().isEmpty())
					{
						if (getSecondProducts() != null && !getSecondProducts().isEmpty())
						{
							for (final Product secondProduct : getSecondProducts())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + secondProduct.getName() + ",";
							}
						}
						else if (getSecondCategories() != null && !getSecondCategories().isEmpty())
						{
							for (final Category category : getSecondCategories())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + category.getName() + ",";
							}

						}

						final Object[] args =
						{ data, discPerOrAmtStr, MarketplacecommerceservicesConstants.EMPTYSPACE };
						return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
					}

					if (getSecondProducts() != null && !getSecondProducts().isEmpty())
					{
						if (getProducts() != null && !getProducts().isEmpty())
						{
							for (final Product firstProduct : getProducts())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + firstProduct.getName() + ",";
							}
						}
						else if (getCategories() != null && !getCategories().isEmpty())
						{
							for (final Category category : getCategories())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + category.getName() + ",";
							}
						}

						final Object[] args =
						{ data, discPerOrAmtStr, MarketplacecommerceservicesConstants.EMPTYSPACE };
						return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
					}


					if (getCategories() != null && !getCategories().isEmpty())
					{
						if (getSecondCategories() != null && !getSecondCategories().isEmpty())
						{
							for (final Category category : getSecondCategories())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + category.getName() + ",";
							}
						}
						else if (getSecondProducts() != null && !getSecondProducts().isEmpty())
						{
							for (final Product secondProduct : getSecondProducts())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + secondProduct.getName() + ",";
							}
						}

						if (minimumCategoryValue > 0.00D)
						{
							final Object[] args =
							{ data, discPerOrAmtStr, Double.valueOf(minimumCategoryValue) };
							return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
						}
						else
						{
							final Object[] args =
							{ data, discPerOrAmtStr, MarketplacecommerceservicesConstants.EMPTYSPACE };
							return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
						}

					}

					if (getSecondCategories() != null && !getSecondCategories().isEmpty())
					{
						if (getCategories() != null && !getCategories().isEmpty())
						{
							for (final Category category : getCategories())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + category.getName() + ",";
							}
						}
						else if (getProducts() != null && !getProducts().isEmpty())
						{
							for (final Product firstProduct : getProducts())
							{
								data = data + MarketplacecommerceservicesConstants.SINGLE_SPACE + firstProduct.getName() + ",";
							}
						}

						if (minimumCategoryValue > 0.00D)
						{
							final Object[] args =
							{ data, discPerOrAmtStr, Double.valueOf(minimumCategoryValue) };
							return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
						}
						else
						{
							final Object[] args =
							{ data, discPerOrAmtStr, MarketplacecommerceservicesConstants.EMPTYSPACE };
							return formatMessage(this.getMessageCouldHaveFired(arg0), args, locale);
						}
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



	/**
	 * @Description : Verify Channel Data
	 * @param : SessionContext arg0
	 * @return : minimumCategoryValue
	 */
	//	private boolean verifyChannelData(final SessionContext arg0)
	//	{
	//		boolean flag = false;
	//		try
	//		{
	//			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
	//					MarketplacecommerceservicesConstants.CHANNEL);
	//			final CartModel cartModel = getCartService().getSessionCart();
	//			flag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cartModel.getChannel());
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
	//		}
	//		return flag;
	//	}



	/**
	 * @Description : Provides List of Products eligible for Promotion
	 * @param : SessionContext paramSessionContext ,AbstractOrder cart
	 * @return : List<Product> validProductListFinal
	 */
	private List<String> eligibleForPromotion(final AbstractOrder cart, final SessionContext paramSessionContext)
	{
		resetFlag();
		boolean brandFlag = false;
		boolean sellerFlag = false;
		boolean promoEligible = false;
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		boolean productExistsInA = false;
		boolean productExistsInB = false;
		validProductListA = new ArrayList<String>();
		validProductListB = new ArrayList<String>();
		final List<String> validProductListFinal = new ArrayList<String>();

		final List<Product> promotionProductListA = new ArrayList<Product>(getProducts());
		final List<Product> promotionProductListB = new ArrayList<Product>(getSecondProducts());

		final List<Category> promotionCategoryListA = new ArrayList<Category>(getCategories());
		final List<Category> promotionCategoryListB = new ArrayList<Category>(getSecondCategories());

		validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		final Map<String, AbstractOrderEntry> validProductAUssidMap = new HashMap<String, AbstractOrderEntry>();
		final Map<String, AbstractOrderEntry> validProductBUssidMap = new HashMap<String, AbstractOrderEntry>();

		try
		{
			for (final AbstractOrderEntry entry : cart.getEntries())
			{
				final Product product = entry.getProduct();
				//excluded product check
				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
						|| getDefaultPromotionsManager().isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
				{
					continue;
				}
				//checking products list A
				if (!promotionProductListA.isEmpty() && promotionProductListA.contains(product))
				{
					productExistsInA = true;

					sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
					if (sellerFlag)
					{
						validProductAUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
								restrictionList, paramSessionContext, entry));
						//validProductListA.add(product);
					}
					brandFlag = true; //At Product level Brand Restriction is not applied
				}
				//checking products list B
				if (!promotionProductListB.isEmpty() && promotionProductListB.contains(product))
				{
					productExistsInB = true;

					sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
					if (sellerFlag)
					{
						validProductBUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
								restrictionList, paramSessionContext, entry));
						//validProductListB.add(product);
					}
					brandFlag = true;
				}
				//checking products category list A
				if (promotionProductListA.isEmpty() && !promotionCategoryListA.isEmpty())
				{
					final List<String> productCategoryList = getDefaultPromotionsManager().getcategoryList(product,
							paramSessionContext);
					promoEligible = GenericUtilityMethods.productExistsIncat(promotionCategoryListA, productCategoryList);
					if (promoEligible)
					{
						productExistsInA = true;

						brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
						sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
						if (brandFlag && sellerFlag)
						{
							validProductAUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
									restrictionList, paramSessionContext, entry));
							//validProductListA.add(product);
						}
					}
				}
				//checking products category list B
				if (promotionProductListB.isEmpty() && !promotionCategoryListB.isEmpty())
				{
					final List<String> productCategoryList = getDefaultPromotionsManager().getcategoryList(product,
							paramSessionContext);
					promoEligible = GenericUtilityMethods.productExistsIncat(promotionCategoryListB, productCategoryList);
					if (promoEligible)
					{
						productExistsInB = true;

						brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
						sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
						if (brandFlag && sellerFlag)
						{
							validProductBUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
									restrictionList, paramSessionContext, entry));
							//validProductListB.add(product);
						}

					}
				}
			}
			if (productExistsInA && productExistsInB)
			{
				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductAUssidMap.entrySet())
				{
					final AbstractOrderEntry entry = mapEntry.getValue();
					final String valiProdAUssid = mapEntry.getKey();
					for (int i = 1; i <= entry.getQuantity().longValue(); i++)
					{
						validProductListA.add(valiProdAUssid);
					}
				}

				for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductBUssidMap.entrySet())
				{
					final AbstractOrderEntry entry = mapEntry.getValue();
					final String valiProdBUssid = mapEntry.getKey();
					for (int i = 1; i <= entry.getQuantity().longValue(); i++)
					{
						validProductListB.add(valiProdBUssid);
					}
				}
				//update validProductUssidMap based on price if no seller restriction attached
				//				if (!getDefaultPromotionsManager().isSellerRestrExists(restrictionList))
				//				{
				totalFactorCount = validProductListA.size() < validProductListB.size() ? validProductListA.size() : validProductListB
						.size();
				final Set<String> validProdAUssidSet = getDefaultPromotionsManager().populateSortedValidProdUssidMap(
						validProductAUssidMap, totalFactorCount, paramSessionContext, restrictionList, null);

				final Set<String> validProdBUssidSet = getDefaultPromotionsManager().populateSortedValidProdUssidMap(
						validProductBUssidMap, totalFactorCount, paramSessionContext, restrictionList, null);

				validProductAUssidMap.keySet().retainAll(validProdAUssidSet);
				validProductBUssidMap.keySet().retainAll(validProdBUssidSet);
				validProductListA.retainAll(validProdAUssidSet);
				validProductListB.retainAll(validProdBUssidSet);
				//}
				validProductUssidMap.putAll(validProductAUssidMap);
				validProductUssidMap.putAll(validProductBUssidMap);

				validProductListFinal.addAll(validProductListA);
				validProductListFinal.addAll(validProductListB);
			}

			if (validProductListA.size() > 0)
			{
				primaryListSize = validProductListA.size();
			}
			if (validProductListB.size() > 0)
			{
				secondaryListSize = validProductListB.size();
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
		return validProductListFinal;
	}

	/**
	 * @Description : Reset Flag Variables
	 * @param : no
	 * @return: void
	 */
	private void resetFlag()
	{
		primaryListSize = 0;
		secondaryListSize = 0;
	}

	/**
	 * @Description : Returns Minimum Category Amount
	 * @param : SessionContext arg0
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

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

}
