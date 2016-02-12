package com.tisl.mpl.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
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
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


public class BuyAandBPrecentageDiscount extends GeneratedBuyAandBPrecentageDiscount
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyAandBPrecentageDiscount.class.getName());
	private List<Product> excludedProductList = null;
	private List<String> excludeManufactureList = null;
	private int primaryListSize = 0;
	private int secondaryListSize = 0;
	private Map<String, AbstractOrderEntry> validProductUssidMap = null;
	private Map<String, AbstractOrderEntry> allValidProductUssidMap = null;
	private int totalFactorCount = 0;

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
	 * @Description : Buy Product A and B to get Percentage or Amount Discount
	 * @param : SessionContext arg0 ,PromotionEvaluationContext arg1
	 * @return : List<PromotionResult> promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>(); // Method returns List of Type PromotionResult
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());//Adding restrictions to List

		excludedProductList = new ArrayList<Product>();
		excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(arg0, arg1, excludedProductList, excludeManufactureList,
				restrictionList, this);

		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(arg0, arg1); // Validates Promotion Restrictions

		boolean checkChannelFlag = false;
		final AbstractOrder cart = arg1.getOrder();
		try
		{
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel); // Verifying the Channel : Web/Web Mobile/ CockPit
			//changes Start for omni cart fix @atmaram
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram


			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag) // Validates the Restriction :Allows only if Valid and Valid Channel
			{
				final List<String> eligibleProductList = eligibleForPromotion(cart, arg0); // Gets the Eligible Product List

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
	 * @param paramSessionContext
	 * @param paramPromotionEvaluationContext
	 * @param validProductUssidMap
	 * @return promotionResults
	 */
	private List<PromotionResult> promotionEvaluation(final SessionContext arg0, final PromotionEvaluationContext arg1,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order, final List<String> eligibleProductList)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final AbstractOrder cart = arg1.getOrder();

		if (!(getSecondProducts().isEmpty() && getSecondCategories().isEmpty())
				&& GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, arg0, arg1, this, restrictionList))
		{
			boolean isPercentageDisc = false;
			boolean flagForDeliveryModeRestrEval = false;
			boolean flagForPaymentModeRestrEval = false;
			final double maxDiscount = getMaxDiscount() == null ? 0.0D : getMaxDiscount().doubleValue(); // Sets the Promotion set Max Discount to a variable
			double percentageDiscount = getPercentageDiscount().doubleValue(); // Sets the Percentage Discount Value to a variable
			//for delivery mode restriction check
			flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForABPromo(restrictionList,
					validProductUssidMap);
			//for payment mode restriction check
			flagForPaymentModeRestrEval = getDefaultPromotionsManager().getPaymentModeRestrEval(restrictionList, arg0);

			final List<Product> validProductList = new ArrayList<Product>();
			final Map<String, Integer> tcMapForValidEntries = new HashMap<String, Integer>();

			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : allValidProductUssidMap.entrySet())
			{
				final AbstractOrderEntry orderEntry = mapEntry.getValue();
				validProductList.add(orderEntry.getProduct());
				tcMapForValidEntries.put(mapEntry.getKey(), Integer.valueOf(mapEntry.getValue().getQuantity().intValue()));
			}
			// Sonar critical fix
			//final PromotionOrderView view = arg1.createView(arg0, this, validProductList);

			if (!eligibleProductList.isEmpty()) //Apply percentage/amount discount to valid products
			{
				if (flagForDeliveryModeRestrEval && flagForPaymentModeRestrEval)
				{
					//List<PromotionOrderEntryConsumed> remainingItemsFromTail = null;

					final int totalCountFactor = totalFactorCount;
					final double totalvalidproductsPricevalue = getvalidProductTotalPrice(validProductUssidMap, totalCountFactor);
					if (!isPercentageOrAmount().booleanValue()) // If Discount mode is not Percentage
					{
						final Double discountPrice = getPriceForOrder(arg0, getDiscountPrices(arg0), cart,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
						double discountPriceValue = discountPrice == null ? 0.0D : discountPrice.doubleValue();
						discountPriceValue = discountPriceValue * totalCountFactor;

						percentageDiscount = (discountPriceValue * 100) / totalvalidproductsPricevalue;
					}
					else
					{
						final double totalSavings = totalvalidproductsPricevalue * (totalCountFactor * percentageDiscount / 100); //If Discount mode is Percentage
						final double totalMaxDiscount = totalCountFactor * maxDiscount;
						if (totalSavings > totalMaxDiscount && totalMaxDiscount != 0)
						{
							percentageDiscount = (totalMaxDiscount * 100) / totalvalidproductsPricevalue;
						}
						else
						{
							isPercentageDisc = true;
						}
					}

					final Map<String, Integer> qCount = getDefaultPromotionsManager().getQualifyingCountForABPromotion(
							eligibleProductList, totalCountFactor);
					final Map<String, List<String>> productAssociatedItemsMap = getMplPromotionHelper().getAssociatedData(cart,
							validProductUssidMap, null);

					arg0.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT, Double.valueOf(percentageDiscount));
					arg0.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
							Double.valueOf(totalvalidproductsPricevalue));
					arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
					arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, qCount);
					arg0.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
					arg0.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
					arg0.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));

					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						//arg1.startLoggingConsumed(this);
						final AbstractOrderEntry entry = mapEntry.getValue();
						final String validUssid = mapEntry.getKey();
						final long quantityOfOrderEntry = entry.getQuantity(arg0).longValue();
						final double percentageDiscountvalue = percentageDiscount / 100.0D;

						if (percentageDiscount < 100) // Apportioning
						{
							//							double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue);
							//							final int noOfProducts = totalCountFactor;
							//							adjustment = adjustment * noOfProducts;

							final double originalUnitPrice = entry.getBasePrice(arg0).doubleValue();
							final double originalEntryPrice = totalCountFactor * originalUnitPrice;

							final Currency currency = cart.getCurrency(arg0);

							final BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(arg0, currency, originalEntryPrice
									- (originalEntryPrice * percentageDiscountvalue));

							final BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(
									arg0,
									currency,
									(adjustedEntryPrice.equals(BigDecimal.ZERO)) ? BigDecimal.ZERO : adjustedEntryPrice.divide(
											BigDecimal.valueOf(totalCountFactor), RoundingMode.HALF_EVEN));

							final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();
							consumed.add(getDefaultPromotionsManager().consume(arg0, this, totalCountFactor, totalCountFactor, entry));

							tcMapForValidEntries.put(validUssid, Integer.valueOf((int) quantityOfOrderEntry - totalCountFactor));

							for (final PromotionOrderEntryConsumed poec : consumed)
							{
								poec.setAdjustedUnitPrice(arg0, adjustedUnitPrice.doubleValue());
							}

							final BigDecimal adjustment = Helper.roundCurrencyValue(arg0, currency,
									adjustedEntryPrice.subtract(BigDecimal.valueOf(originalEntryPrice)));

							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
									arg1.getOrder(), 1.0F);
							final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
									.createCustomPromotionOrderEntryAdjustAction(arg0, entry, adjustment.doubleValue());
							//final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
							result.setConsumedEntries(arg0, consumed);
							result.addAction(arg0, poeac);
							promotionResults.add(result);

						}
					}

					//Setting remaining items
					//					remainingItemsFromTail = getDefaultPromotionsManager().findRemainingEntries(arg0,
					//							view.getAllEntries(arg0), tcMapForValidEntries);

					//final float certainty = totalCountFactor / ;

					float certainty = 1.00F;
					if (primaryListSize != secondaryListSize) // For localization Purpose
					{
						certainty = 0.00F;
					}
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
							certainty);
					//result.setConsumedEntries(remainingItemsFromTail);
					promotionResults.add(result);
				}
				else
				{
					float certainty = 1.00F;
					if (primaryListSize != secondaryListSize) // For localization Purpose
					{
						certainty = 0.00F;
					}
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
							certainty);
					promotionResults.add(result);
				}
			}
			else
			{
				final int count = getMplPromotionHelper().returnValidProductCount(arg0, cart, validProductUssidMap,
						excludedProductList);
				if (count > 0)
				{
					final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
							0.0F);
					promotionResults.add(result);
				}
			}
		}
		else
		{
			final int count = getMplPromotionHelper().returnValidProductCount(arg0, cart, validProductUssidMap, excludedProductList);
			if (count > 0)
			{
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this, arg1.getOrder(),
						0.0F);
				promotionResults.add(result);
			}
		}

		return promotionResults;
	}

	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param : SessionContext arg0 ,PromotionResult arg1 ,Locale arg2
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{
		try
		{
			final AbstractOrder order = arg1.getOrder(arg0);
			String data = MarketplacecommerceservicesConstants.EMPTYSPACE;
			String maxDiscountval = MarketplacecommerceservicesConstants.EMPTYSPACE;
			boolean checkFlagPerOrDis = false;

			if (order != null)
			{
				final Currency orderCurrency = order.getCurrency(arg0);
				if (arg1.getFired(arg0))
				{
					final Double totalDiscount = Double.valueOf(arg1.getTotalDiscount(arg0));

					//TISEEII-229 : Regression Fix
					if (isPercentageOrAmount().booleanValue())
					{
						final Double percentageDiscount = getPercentageDiscount(arg0);
						final Object[] args =
						{ percentageDiscount, Helper.formatCurrencyAmount(arg0, arg2, orderCurrency, totalDiscount.doubleValue()),
								totalDiscount, MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE };
						return formatMessage(getMessageFired(arg0), args, arg2);
					}
					else
					{
						final Double discountPriceValue = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
						final Object[] args =
						{ discountPriceValue, Helper.formatCurrencyAmount(arg0, arg2, orderCurrency, totalDiscount.doubleValue()),
								totalDiscount, MarketplacecommerceservicesConstants.DISCOUNT_PRICES_MESSAGE };
						return formatMessage(getMessageFired(arg0), args, arg2);
					}
				}
				else if (arg1.getCouldFire(arg0))
				{
					final Object[] args = new Object[6];
					final double minimumCategoryValue = getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT) != null ? ((Double) getProperty(
							arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() : 0.00D;
					Double discountPriceValue = Double.valueOf(0.00);
					Double discountMaxVal = Double.valueOf(0.00);
					Double percentageDiscount = Double.valueOf(0.00);
					if (isPercentageOrAmount().booleanValue())
					{
						checkFlagPerOrDis = true;
						percentageDiscount = getPercentageDiscount(arg0);
						discountMaxVal = getMaxDiscount() == null ? Double.valueOf(0.00) : getMaxDiscount();
						if (discountMaxVal.doubleValue() > 0)
						{
							maxDiscountval = String.valueOf(discountMaxVal);
						}
					}
					else
					{
						discountPriceValue = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES) == null ? Double.valueOf(0.00D) : getPriceForOrder(
								arg0, getDiscountPrices(arg0), order, MarketplacecommerceservicesConstants.DISCOUNT_PRICES);
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

						if (checkFlagPerOrDis)
						{
							args[0] = data;
							args[1] = (percentageDiscount + "%");
							args[2] = maxDiscountval;
							args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;

						}
						else
						{
							args[0] = data;
							args[1] = discountPriceValue;
							args[2] = maxDiscountval;
							args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
						}
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

						if (checkFlagPerOrDis)
						{
							args[0] = data;
							args[1] = (percentageDiscount + "%");
							args[2] = maxDiscountval;
							args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;

						}
						else
						{
							args[0] = data;
							args[1] = discountPriceValue;
							args[2] = maxDiscountval;
							args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
						}
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

						if (checkFlagPerOrDis)
						{
							args[0] = data;
							args[1] = (percentageDiscount + "%");
							args[2] = maxDiscountval;
							if (minimumCategoryValue > 0.00D)
							{
								args[3] = Double.valueOf(minimumCategoryValue);
							}
							else
							{
								args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
							}
						}
						else
						{
							args[0] = data;
							args[1] = discountPriceValue;
							args[2] = maxDiscountval;
							if (minimumCategoryValue > 0.00D)
							{
								args[3] = Double.valueOf(minimumCategoryValue);
							}
							else
							{
								args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
							}
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

						if (checkFlagPerOrDis)
						{
							args[0] = data;
							args[1] = (percentageDiscount + "%");
							args[2] = maxDiscountval;
							if (minimumCategoryValue > 0.00D)
							{
								args[3] = Double.valueOf(minimumCategoryValue);
							}
							else
							{
								args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
							}
						}
						else
						{
							args[0] = data;
							args[1] = discountPriceValue;
							args[2] = maxDiscountval;
							if (minimumCategoryValue > 0.00D)
							{
								args[3] = Double.valueOf(minimumCategoryValue);
							}
							else
							{
								args[3] = MarketplacecommerceservicesConstants.EMPTYSPACE;
							}
						}
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
					//productAFlag = true;
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
					//productBFlag = true;

					sellerFlag = getDefaultPromotionsManager().checkSellerData(paramSessionContext, restrictionList, entry);
					if (sellerFlag)
					{
						validProductBUssidMap.putAll(getDefaultPromotionsManager().populateValidProductUssidMap(product, cart,
								restrictionList, paramSessionContext, entry));
					}
					brandFlag = true;//At Product level Brand Restriction is not applied
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
						//categoryAFlag = true;

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
						//categoryAFlag = true;

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

				//				eligibleCount = getMplPromotionHelper().getEligibleCountForABDiscountPromo(validProductAUssidMap,
				//						validProductBUssidMap);

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
	 * @param totalCountFactor
	 * @Description : Provides Total Price of Promotion Valid Products
	 * @param : List<Product> eligibleProductList ,AbstractOrder cart
	 * @return : totalvalidproductsPricevalue
	 */
	private double getvalidProductTotalPrice(final Map<String, AbstractOrderEntry> validProductUssidMap, final int totalCountFactor)
	{
		double totalvalidproductsPricevalue = 0.0D;
		try
		{
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				final AbstractOrderEntry entry = mapEntry.getValue();
				totalvalidproductsPricevalue += entry.getBasePrice().doubleValue() * totalCountFactor;
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
	 * @param : no
	 * @return: void
	 */
	private void resetFlag()
	{
		primaryListSize = 0;
		secondaryListSize = 0;
	}


	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

}
