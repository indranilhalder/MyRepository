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


public class BuyAandBgetC extends GeneratedBuyAandBgetC
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAandBgetC.class.getName());
	private List<Product> excludedProductList = null;
	private List<String> excludeManufactureList = null;
	private int primaryListSize = 0;
	private int secondaryListSize = 0;
	private String sellerID;
	private Map<String, AbstractOrderEntry> validProductUssidMap = null;
	private Map<String, AbstractOrderEntry> allValidProductUssidMap = null;
	int totalFactorCount = 0;

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
	 * @Description : Buy Product A and B to get Product C Free
	 * @param :
	 *           SessionContext arg0 ,PromotionEvaluationContext arg1
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions()); //Get the Promotion set Restrictions
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();

		//Populate Excluded Products/Manufacturers in their respective Lists
		excludedProductList = new ArrayList<Product>();
		excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(arg0, arg1, excludedProductList, excludeManufactureList,
				restrictionList, this);
		//getDefaultPromotionsManager().promotionAlreadyFired(arg0, order, excludedProductList); // Checking if Promotions already Fired on the promotion eligible products

		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(arg0, arg1);
		boolean checkChannelFlag = false;
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
					MarketplacecommerceservicesConstants.CHANNEL);
			//commented for omni cart issue
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			final AbstractOrder cart = arg1.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			final List<String> eligibleProductList = eligibleForPromotion(cart, arg0); //Get Promotion eligible Products

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag
					&& !(getSecondProducts().isEmpty() && getSecondCategories().isEmpty())
					&& GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, arg0, arg1, this, restrictionList)) // If Satisfies all Restriction and Channel validates to be true
			{
				if (!getDefaultPromotionsManager().promotionAlreadyFired(arg0, validProductUssidMap))
				{
					promotionResults = promotionEvaluation(arg0, arg1, validProductUssidMap, restrictionList, cart,
							eligibleProductList);
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
	 * @Description : Promotion Evaluation Method
	 * @param arg0
	 * @param arg1
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext arg0, final PromotionEvaluationContext arg1,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order, final List<String> eligibleProductList)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		boolean flagForDeliveryModeRestrEval = false;

		//for delivery mode restriction check
		flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForABPromo(restrictionList,
				validProductUssidMap);

		final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();
		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : allValidProductUssidMap.entrySet())
		{
			tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
		}

		if (flagForDeliveryModeRestrEval)
		{
			if (!eligibleProductList.isEmpty()) //Apply percentage/amount discount to valid products
			{
				final int noOfTimes = totalFactorCount;
				arg0.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(noOfTimes)); // Setting Free gift details in Session Context
				arg0.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
				//getting eligible Product List
				final List<Product> validProductList = new ArrayList<Product>();
				for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
				{
					validProductList.add(orderEntry.getProduct());
				}
				//@Description:Gift Product can be multiple
				final List<Product> productList = (List<Product>) this.getGiftProducts(arg0); // Get Promotion set Free Gifts

				final List<PromotionOrderEntryConsumed> consumed = getDefaultPromotionsManager().getConsumedEntriesForFreebie(arg0,
						this, validProductUssidMap, null, Integer.valueOf(noOfTimes), tcMapForValidEntries);

				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
						1.00F);
				result.setConsumedEntries(arg0, consumed);

				if (null != productList && !productList.isEmpty())
				{
					final Map<String, Product> giftProductDetails = getDefaultPromotionsManager().getGiftProducts(productList,
							sellerID); //Validating the free gift corresponding to seller ID for scenario : Eligible product and Free gift from same DC

					getPromotionUtilityPOJO().setPromoProductList(validProductList); // Setting valid Products
					final Map<String, Integer> qCount = getDefaultPromotionsManager()
							.getQualifyingCountForABPromotion(eligibleProductList, totalFactorCount);
					for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
					{
						final Map<String, List<String>> productAssociatedItemsMap = getMplPromotionHelper().getAssociatedData(order,
								validProductUssidMap, entry.getKey());

						arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
						arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, qCount);
						arg0.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);

						result.addAction(arg0, getDefaultPromotionsManager().createCustomPromotionOrderAddFreeGiftAction(arg0,
								entry.getValue(), entry.getKey(), result, Double.valueOf(noOfTimes))); //Adding Free gifts to cart
					}
				}
				promotionResults.add(result);

				float certainty = 1.00F;
				if (primaryListSize != secondaryListSize)
				{// For Message Localization
					certainty = 0.00F;
				}
				final PromotionResult result1 = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
						certainty);
				promotionResults.add(result1);
			}
			else
			{
				for (final AbstractOrderEntry entry : order.getEntries())
				{// For Message Localization
					final Product orderProduct = entry.getProduct();
					if (!excludedProductList.contains(orderProduct) && GenericUtilityMethods.checkRestrictionData(restrictionList))
					{
						final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
								arg1.getOrder(), 0.00F);
						promotionResults.add(result);
					}
				}
			}
		}
		return promotionResults;
	}





	/**
	 * @Description : Returns Minimum Category Amount
	 * @param :
	 *           SessionContext arg0
	 * @return : minimumCategoryValue
	 */
	//	private double calculateMinCategoryAmnt(final SessionContext arg0)
	//	{
	//		double minimumCategoryValue = 0.00D;
	//		if (null != arg0 && null != getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)
	//				&& ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() > 0.00D)
	//		{
	//			minimumCategoryValue = ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue();
	//		}
	//		return minimumCategoryValue;
	//	}




	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext arg0 ,PromotionResult arg1 ,Locale arg2
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{
		try
		{
			final AbstractOrder order = arg1.getOrder(arg0);
			String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
			if (order != null)
			{
				//final double minimumCategoryValue = calculateMinCategoryAmnt(arg0);

				if (arg1.getFired(arg0))
				{
					final Object[] args = {};
					return formatMessage(this.getMessageFired(arg0), args, arg2);
				}
				else if (arg1.getCouldFire(arg0))
				{
					//eligibleForPromotion(order, arg0);
					final Object[] args = new Object[6];
					final double minimumCategoryValue = getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT) != null
							? ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() : 0.00D;

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

						args[0] = data;
						args[1] = MarketplacecommerceservicesConstants.EMPTYSPACE;
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

						args[0] = data;
						args[1] = MarketplacecommerceservicesConstants.EMPTYSPACE;
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
							args[0] = data;
							args[1] = Double.valueOf(minimumCategoryValue);
						}
						else
						{
							args[0] = data;
							args[1] = MarketplacecommerceservicesConstants.EMPTYSPACE;
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
							args[0] = data;
							args[1] = Double.valueOf(minimumCategoryValue);
						}
						else
						{
							args[0] = data;
							args[1] = MarketplacecommerceservicesConstants.EMPTYSPACE;
						}

					}

					if (!deliveryModes.isEmpty())
					{
						args[2] = deliveryModes;
					}
					else if (!paymentModes.isEmpty())
					{
						args[2] = paymentModes;
					}

					return formatMessage(this.getMessageCouldHaveFired(arg0), args, arg2);
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

			for (final DeliveryMode mode : deliveryModeList)
			{
				deliveryModes = deliveryModes + mode.getName() + ", ";
			}
			deliveryModes += " delivery modes";
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

			for (final PaymentType mode : paymentModeList)
			{
				paymentModes = paymentModes + mode.getMode() + ", ";
			}
			paymentModes += " payment modes";
		}

		return paymentModes;
	}

	/**
	 * @Description : Provides List of Products eligible for Promotion
	 * @param :
	 *           SessionContext paramSessionContext ,AbstractOrder cart
	 * @return : List<Product> validProductListFinal
	 */
	private List<String> eligibleForPromotion(final AbstractOrder cart, final SessionContext paramSessionContext)
	{
		resetFlag();
		boolean brandFlag = false;
		boolean sellerFlag = false;
		//final boolean sellerMatches = false;
		boolean promoEligible = false;
		boolean isFreebie = false;
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		sellerID = MarketplacecommerceservicesConstants.EMPTY;

		boolean productExistsInA = false;
		boolean productExistsInB = false;

		final List<String> validProductListA = new ArrayList<String>();
		final List<String> validProductListB = new ArrayList<String>();
		final List<String> validProductListFinal = new ArrayList<String>();

		final List<Product> promotionProductListA = new ArrayList<Product>(getProducts());
		final List<Product> promotionProductListB = new ArrayList<Product>(getSecondProducts());

		final List<Category> promotionCategoryListA = new ArrayList<Category>(getCategories());
		final List<Category> promotionCategoryListB = new ArrayList<Category>(getSecondCategories());

		validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		allValidProductUssidMap = new HashMap<String, AbstractOrderEntry>();

		final Map<String, AbstractOrderEntry> validProductAUssidMap = new HashMap<String, AbstractOrderEntry>();
		final Map<String, AbstractOrderEntry> validProductBUssidMap = new HashMap<String, AbstractOrderEntry>();

		try
		{
			if (GenericUtilityMethods.checkRestrictionData(restrictionList))
			{
				for (final AbstractOrderEntry entry : cart.getEntries())
				{
					//boolean sellerFlag = false;
					isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
					if (!isFreebie)
					{
						final Product product = entry.getProduct();
						//excluded product check
						if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
								|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
						{
							continue;
						}

						//checking products list A
						if (!promotionProductListA.isEmpty() && promotionProductListA.contains(product))
						{

							sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
							if (sellerFlag)
							{
								productExistsInA = true;
								validProductAUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
										restrictionList, paramSessionContext, entry));
								sellerID = getDefaultPromotionsManager().getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
							}

						}

						//checking products list B
						if (!promotionProductListB.isEmpty() && promotionProductListB.contains(product))
						{
							sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
							if (sellerFlag) //Matching of Seller ID of the Primary and Secondary Product
							{
								productExistsInB = true;
								validProductBUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
										restrictionList, paramSessionContext, entry));
							}

						}

						//checking products category list A
						if (promotionProductListA.isEmpty() && !promotionCategoryListA.isEmpty())
						{
							final List<String> productCategoryList = getDefaultPromotionsManager().getcategoryList(product,
									paramSessionContext);
							promoEligible = GenericUtilityMethods.productExistsIncat(promotionCategoryListA, productCategoryList);
							if (promoEligible)
							{
								brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
								sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
								if (sellerFlag && brandFlag)
								{
									productExistsInA = true;
									validProductAUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
											restrictionList, paramSessionContext, entry));
									sellerID = getDefaultPromotionsManager().getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
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
								brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
								sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
								if (sellerFlag && brandFlag)
								{
									productExistsInB = true;
									validProductBUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
											restrictionList, paramSessionContext, entry));
								}
							}
						}
					}
				}


				if (productExistsInA && productExistsInB)
				{
					allValidProductUssidMap.putAll(validProductAUssidMap);
					allValidProductUssidMap.putAll(validProductBUssidMap);

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

					totalFactorCount = validProductListA.size() < validProductListB.size() ? validProductListA.size()
							: validProductListB.size();
					final Set<String> validProdAUssidSet = getDefaultPromotionsManager().populateSortedValidProdUssidMap(
							validProductAUssidMap, totalFactorCount, paramSessionContext, restrictionList, null);

					final Set<String> validProdBUssidSet = getDefaultPromotionsManager().populateSortedValidProdUssidMap(
							validProductBUssidMap, totalFactorCount, paramSessionContext, restrictionList, null);

					validProductAUssidMap.keySet().retainAll(validProdAUssidSet);
					validProductBUssidMap.keySet().retainAll(validProdBUssidSet);

					validProductListA.retainAll(validProdAUssidSet);
					validProductListB.retainAll(validProdBUssidSet);

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



	protected CartService getCartService()
	{
		return Registry.getApplicationContext().getBean("cartService", CartService.class);
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}


	/**
	 * @Description : Reset boolean Flag Variables
	 * @param: void
	 * @return: void
	 */
	private void resetFlag()
	{
		primaryListSize = 0;
		secondaryListSize = 0;
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

}
