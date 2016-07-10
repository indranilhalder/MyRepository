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
import de.hybris.platform.promotions.jalo.PromotionOrderEntryAdjustAction;
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


public class BuyAandBGetPrecentageDiscountCashback extends GeneratedBuyAandBGetPrecentageDiscountCashback
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAandBGetPrecentageDiscountCashback.class.getName());
	private List<Product> excludedProductList = null;
	private List<String> excludeManufactureList = null;
	private double totalCachback = 0.0D;
	private int primaryListSize = 0;
	private int secondaryListSize = 0;
	private Map<String, AbstractOrderEntry> validProductUssidMap = null;
	private int totalFactorCount = 0;


	/**
	 * @Description : This method is for creating item type
	 * @param :
	 *           ctx
	 * @param :
	 *           type
	 * @param :
	 *           allAttributes
	 * @return : item
	 *
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
	 * @Description : Buy Product A and B to get Percentage or Amount Discount cashback
	 * @param: paramSessionContext
	 * @param: paramPromotionEvaluationContext
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions()); //Get the Promotion set Restrictions

		excludedProductList = new ArrayList<Product>();
		excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(paramSessionContext, paramPromotionEvaluationContext,
				excludedProductList, excludeManufactureList, restrictionList, this);
		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(paramSessionContext,
				paramPromotionEvaluationContext);

		boolean checkChannelFlag = false;

		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(paramSessionContext,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();

			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram
			final List<String> eligibleProductList = eligibleForPromotion(cart, paramSessionContext);

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag)
			{
				if (!getDefaultPromotionsManager().promotionAlreadyFired(paramSessionContext, validProductUssidMap))
				{
					promotionResults = promotionEvaluation(paramSessionContext, paramPromotionEvaluationContext, validProductUssidMap,
							restrictionList, cart, eligibleProductList);
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
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order, final List<String> eligibleProductList)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final AbstractOrder cart = paramPromotionEvaluationContext.getOrder();
		final double maxDiscount = getMaxDiscount() == null ? 0.0D : getMaxDiscount().doubleValue();
		final List<Product> promoProductList = new ArrayList<Product>();

		if (!(getSecondProducts().isEmpty() && getSecondCategories().isEmpty())
				&& GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, paramSessionContext,
						paramPromotionEvaluationContext, this, restrictionList))
		{
			double percentageDiscount = getPercentageDiscount().doubleValue();
			boolean flagForDeliveryModeRestrEval = false;
			boolean flagForPaymentModeRestrEval = false;
			//for delivery mode restriction check
			flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForABPromo(restrictionList,
					validProductUssidMap);
			//for payment mode restriction check
			flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList,
					paramSessionContext);

			if (!eligibleProductList.isEmpty()) //Apply percentage/amount discount to valid products
			{
				if (flagForPaymentModeRestrEval && flagForDeliveryModeRestrEval)
				{
					final int totalCountFactor = eligibleProductList.size() / 2;

					final double totalvalidproductsPricevalue = getvalidProductTotalPrice(validProductUssidMap);
					//amount discount
					if (!isPercentageOrAmount().booleanValue())
					{
						final Double discountPrice = getPriceForOrder(paramSessionContext, getDiscountPrices(paramSessionContext), cart,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
						double discountPriceValue = discountPrice == null ? 0.0D : discountPrice.doubleValue();
						discountPriceValue = discountPriceValue * totalCountFactor;
						percentageDiscount = (discountPriceValue * 100) / totalvalidproductsPricevalue;
					}
					else
					{
						final double totalSavings = totalvalidproductsPricevalue * (totalCountFactor * percentageDiscount / 100);
						final double totalMaxDiscount = totalCountFactor * maxDiscount;
						if (totalSavings > totalMaxDiscount && totalMaxDiscount != 0)
						{
							percentageDiscount = (totalMaxDiscount * 100) / totalvalidproductsPricevalue;
						}
					}

					totalCachback = 0.0D;
					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						//paramPromotionEvaluationContext.startLoggingConsumed(this);
						final AbstractOrderEntry entry = mapEntry.getValue();
						final double percentageDiscountvalue = percentageDiscount / 100.0D;
						if (percentageDiscount < 100)
						{
							double cashback = (entry.getBasePrice().doubleValue() * percentageDiscountvalue);
							final int noOfProducts = entry.getQuantity().intValue();
							cashback = cashback * noOfProducts;
							totalCachback += cashback;

							final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
							consumed.add(getDefaultPromotionsManager().consume(paramSessionContext, this, totalFactorCount,
									totalFactorCount, entry));

							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext,
									this, paramPromotionEvaluationContext.getOrder(), 1.0F);
							final PromotionOrderEntryAdjustAction poeac = PromotionsManager.getInstance()
									.createPromotionOrderEntryAdjustAction(paramSessionContext, entry, 0.0D);
							//final List consumed = paramPromotionEvaluationContext.finishLoggingAndGetConsumed(this, true);

							result.setConsumedEntries(paramSessionContext, consumed);
							result.addAction(paramSessionContext, poeac);
							promoProductList.add(entry.getProduct());
							promotionResults.add(result);
						}

					}
					if (promoProductList.size() > 0)
					{
						getPromotionUtilityPOJO().setPromoProductList(promoProductList);
					}
				}
				else
				{
					float certainty = 1.00F;
					if (primaryListSize != secondaryListSize)
					{
						certainty = 0.00F;
					}
					promotionResults.add(getPromoResultData(certainty, paramSessionContext, paramPromotionEvaluationContext));
				}
			}
			else
			{
				promotionResults.add(getPromoResultData(0.00F, paramSessionContext, paramPromotionEvaluationContext));
			}

		}
		else
		{
			promotionResults.add(getPromoResultData(0.00F, paramSessionContext, paramPromotionEvaluationContext));
		}
		return promotionResults;
	}

	/**
	 * @Description: For Promotion Result Population
	 * @param certainty
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @return result
	 */
	private PromotionResult getPromoResultData(final float certainty, final SessionContext paramSessionContext,
			final PromotionEvaluationContext paramPromotionEvaluationContext)
	{
		final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(paramSessionContext, this,
				paramPromotionEvaluationContext.getOrder(), certainty);
		return result;
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext arg0 ,PromotionResult arg1 ,Locale arg2
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{
		final AbstractOrder order = arg1.getOrder(arg0);
		String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != order)
		{
			if (arg1.getFired(arg0))
			{
				final Object[] args =
				{ Double.valueOf(totalCachback) };
				return formatMessage(this.getMessageFired(arg0), args, arg2);
			}
			else if (arg1.getCouldFire(arg0))
			{
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
				else
				{
					args[2] = "purchase";
				}

				return formatMessage(this.getMessageCouldHaveFired(arg0), args, arg2);
			}
		}
		return MarketplacecommerceservicesConstants.EMPTY;
	}

	/**
	 * @Description : Provides List of Products eligible for Promotion
	 * @param: SessionContext
	 *            paramSessionContext
	 * @param: AbstractOrder
	 *            cart
	 * @return : List<Product> validProductListFinal
	 */
	@SuppressWarnings(
	{ "deprecation" })
	private List<String> eligibleForPromotion(final AbstractOrder cart, final SessionContext paramSessionContext)
	{
		resetFlag();
		final List<String> validProductListA = new ArrayList<String>();
		final List<String> validProductListB = new ArrayList<String>();
		boolean brandFlag = false;
		boolean sellerFlag = false;
		boolean promoEligible = false;
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());

		boolean productExistsInA = false;
		boolean productExistsInB = false;

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
	 * @Description : Provides Total Price of Promotion Valid Products
	 * @param :
	 *           List<Product> eligibleProductList ,AbstractOrder cart
	 * @return : totalvalidproductsPricevalue
	 */
	private double getvalidProductTotalPrice(final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		double totalvalidproductsPricevalue = 0.0D;
		try
		{
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				final AbstractOrderEntry entry = mapEntry.getValue();
				totalvalidproductsPricevalue += entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue();
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

		return totalvalidproductsPricevalue;
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
	 * @Description : Reset Flag Variables
	 * @param :
	 *           no
	 * @return : void
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


	/**
	 *
	 * Getter Setter Creations for Sonar Fix
	 *
	 */


	/**
	 * @return the excludedProductList
	 */
	public List<Product> getExcludedProductList()
	{
		return excludedProductList;
	}

	/**
	 * @param excludedProductList
	 *           the excludedProductList to set
	 */
	public void setExcludedProductList(final List<Product> excludedProductList)
	{
		this.excludedProductList = excludedProductList;
	}

	/**
	 * @return the excludeManufactureList
	 */
	public List<String> getExcludeManufactureList()
	{
		return excludeManufactureList;
	}

	/**
	 * @param excludeManufactureList
	 *           the excludeManufactureList to set
	 */
	public void setExcludeManufactureList(final List<String> excludeManufactureList)
	{
		this.excludeManufactureList = excludeManufactureList;
	}

	/**
	 * @return the totalCachback
	 */
	public double getTotalCachback()
	{
		return totalCachback;
	}

	/**
	 * @param totalCachback
	 *           the totalCachback to set
	 */
	public void setTotalCachback(final double totalCachback)
	{
		this.totalCachback = totalCachback;
	}

	/**
	 * @return the primaryListSize
	 */
	public int getPrimaryListSize()
	{
		return primaryListSize;
	}

	/**
	 * @param primaryListSize
	 *           the primaryListSize to set
	 */
	public void setPrimaryListSize(final int primaryListSize)
	{
		this.primaryListSize = primaryListSize;
	}

	/**
	 * @return the secondaryListSize
	 */
	public int getSecondaryListSize()
	{
		return secondaryListSize;
	}

	/**
	 * @param secondaryListSize
	 *           the secondaryListSize to set
	 */
	public void setSecondaryListSize(final int secondaryListSize)
	{
		this.secondaryListSize = secondaryListSize;
	}

	/**
	 * @return the validProductUssidMap
	 */
	public Map<String, AbstractOrderEntry> getValidProductUssidMap()
	{
		return validProductUssidMap;
	}

	/**
	 * @param validProductUssidMap
	 *           the validProductUssidMap to set
	 */
	public void setValidProductUssidMap(final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		this.validProductUssidMap = validProductUssidMap;
	}


}
