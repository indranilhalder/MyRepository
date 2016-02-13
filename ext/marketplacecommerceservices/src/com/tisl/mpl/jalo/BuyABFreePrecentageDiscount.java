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
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.util.Helper;

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


public class BuyABFreePrecentageDiscount extends GeneratedBuyABFreePrecentageDiscount
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BuyABFreePrecentageDiscount.class.getName());
	private int noOfProducts = 0;
	private boolean flagForCouldFireMessage = true;

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
	 * @Description : Buy A B Free Percentage Discount Promotion
	 * @param :
	 *           arg0
	 * @param :
	 *           arg1
	 * @return :promotionResults
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>(getRestrictions());
		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(arg0, arg1);
		final List<Product> excludedProductList = new ArrayList<Product>();
		final List<String> excludeManufactureList = new ArrayList<String>();
		GenericUtilityMethods.populateExcludedProductManufacturerList(arg0, arg1, excludedProductList, excludeManufactureList,
				restrictionList, this);

		boolean checkChannelFlag = false;
		try
		{
			final boolean sellerFlag = getDefaultPromotionsManager().isSellerRestrExists(restrictionList);
			final List<EnumerationValue> listOfChannel = (List<EnumerationValue>) getProperty(arg0,
					MarketplacecommerceservicesConstants.CHANNEL);
			//checkChannelFlag = getMplPromotionHelper().checkChannel(listOfChannel);
			//changes Start for omni cart fix @atmaram
			final AbstractOrder cart = arg1.getOrder();
			checkChannelFlag = getDefaultPromotionsManager().checkChannelData(listOfChannel, cart);

			//changes end for omni cart fix @atmaram

			if ((rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty())) && checkChannelFlag && sellerFlag)
			{
				promotionResults = promotionEvaluation(arg0, arg1, excludedProductList, excludeManufactureList, restrictionList);
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
	 * @Description : Buy A B Free Percentage Discount Promotion
	 * @param :
	 *           arg0
	 * @param :
	 *           arg1
	 * @param :
	 *           excludedProductList
	 * @param :excludeManufactureList
	 * @param :
	 *           restrictionList
	 * @return: promotionResults
	 */
	public List<PromotionResult> promotionEvaluation(final SessionContext arg0, final PromotionEvaluationContext arg1,
			final List<Product> excludedProductList, final List<String> excludeManufactureList,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final List<String> sellerIDData = new ArrayList<String>();
		final Map<AbstractOrderEntry, String> eligibleProductMap = new HashMap<AbstractOrderEntry, String>();
		boolean flagForDeliveryModeRestrEval = false;
		final double maxDiscount = getMaxDiscountVal().doubleValue();
		final List<Product> promotionProductList = new ArrayList<>(getProducts());
		final List<Category> promotionCategoryList = new ArrayList<>(getCategories());
		final List<Product> promotionalProductData = new ArrayList<Product>();
		final AbstractOrder order = arg1.getOrder();

		//getting the valid products
		final Map<String, AbstractOrderEntry> validProductUssidMap = getDefaultPromotionsManager().getValidProdListForBuyXofAPromo(
				order, arg0, promotionProductList, promotionCategoryList, restrictionList, excludedProductList,
				excludeManufactureList, sellerIDData, eligibleProductMap);

		if (GenericUtilityMethods.checkBrandAndCategoryMinimumAmt(validProductUssidMap, arg0, arg1, this, restrictionList)
				&& !getDefaultPromotionsManager().promotionAlreadyFired(arg0, validProductUssidMap))
		{
			final Double discountPrice = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
					MarketplacecommerceservicesConstants.DISCOUNT_PRICES) != null
							? (Double) getPriceForOrder(arg0, getDiscountPrices(arg0), order,
									MarketplacecommerceservicesConstants.DISCOUNT_PRICES)
							: new Double(0.0);

			boolean isPercentageDisc = false;
			final Long eligibleQuantity = getQuantity();
			int totalCount = 0;
			for (final AbstractOrderEntry entry : validProductUssidMap.values())
			{
				totalCount += entry.getQuantity().intValue(); // Fetches total count of Valid Products
			}
			noOfProducts = totalCount;
			if (totalCount >= eligibleQuantity.intValue())
			{
				final Map<String, Integer> validProductList = getDefaultPromotionsManager().getSortedValidProdUssidMap(
						validProductUssidMap, totalCount, eligibleQuantity.longValue(), arg0, restrictionList);

				if (!isPercentageOrAmount().booleanValue())
				{
					flagForCouldFireMessage = getDefaultPromotionsManager().getValidProductListForAmtDiscount(arg0, order,
							promotionProductList, promotionCategoryList, eligibleQuantity, discountPrice, validProductUssidMap);
				}

				//for delivery mode restriction check
				flagForDeliveryModeRestrEval = getDefaultPromotionsManager().getDelModeRestrEvalForAPromo(restrictionList,
						validProductUssidMap);
				if (flagForDeliveryModeRestrEval)
				{
					double percentageDiscount = getPercentageDiscount().doubleValue();

					//Promotion Value Calculations
					final int totalFactorCount = totalCount / eligibleQuantity.intValue();
					int totalValidProdCount = 0;
					for (final String key : validProductList.keySet())
					{
						totalValidProdCount += validProductList.get(key).intValue(); // Fetches total count of Valid Products
					}

					//getting eligible Product List
					final List<Product> eligibleProductList = new ArrayList<Product>();
					for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
					{
						eligibleProductList.add(orderEntry.getProduct());
					}
					promotionalProductData.addAll(eligibleProductList);

					final double totalPricevalue = getDefaultPromotionsManager().getTotalValidProdPrice(validProductUssidMap,
							validProductList);
					//					final double percentageDiscountForAmount = getDefaultPromotionsManager().getConvertedPercentageDiscount(
					//							totalValidProdCount, discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);

					if (!isPercentageOrAmount().booleanValue())
					{
						percentageDiscount = getDefaultPromotionsManager().getConvertedPercentageDiscount(totalValidProdCount,
								discountPrice.doubleValue(), eligibleQuantity, totalPricevalue);
						//percentageDiscount = percentageDiscountForAmount;
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

					arg0.setAttribute(MarketplacecommerceservicesConstants.PERCENTAGEDISCOUNT, Double.valueOf(percentageDiscount));
					arg0.setAttribute(MarketplacecommerceservicesConstants.TOTALVALIDPRODUCTSPRICEVALUE,
							Double.valueOf(totalPricevalue));
					arg0.setAttribute(MarketplacecommerceservicesConstants.VALIDPRODUCTLIST, validProductUssidMap);
					arg0.setAttribute(MarketplacecommerceservicesConstants.QUALIFYINGCOUNT, validProductList);
					arg0.setAttribute(MarketplacecommerceservicesConstants.PROMOCODE, String.valueOf(this.getCode()));
					arg0.setAttribute(MarketplacecommerceservicesConstants.ISPERCENTAGEDISC, Boolean.valueOf(isPercentageDisc));

					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						arg1.startLoggingConsumed(this);
						final AbstractOrderEntry entry = mapEntry.getValue();
						final String validUssid = mapEntry.getKey();
						final double percentageDiscountvalue = percentageDiscount / 100.0D;
						if (percentageDiscount < 100)
						{
							final int eligibleCount = validProductList.get(validUssid).intValue();
							final double adjustment = -(entry.getBasePrice().doubleValue() * percentageDiscountvalue * eligibleCount);
							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(arg0, this,
									arg1.getOrder(), 1.0F);
							final CustomPromotionOrderEntryAdjustAction poeac = getDefaultPromotionsManager()
									.createCustomPromotionOrderEntryAdjustAction(arg0, entry, adjustment);
							final List consumed = arg1.finishLoggingAndGetConsumed(this, true);
							result.setConsumedEntries(arg0, consumed);
							result.addAction(arg0, poeac);
							promotionResults.add(result);
						}
					}
					//final int giftProductCount = totalCount / eligibleQuantity.intValue();
					//arg0.setAttribute(MarketplacecommerceservicesConstants.FREEGIFT_QUANTITY, String.valueOf(giftProductCount));
					final List<Product> promotionGiftProductList = (List<Product>) this.getGiftProducts(arg0);
					final PromotionResult freeResult = PromotionsManager.getInstance().createPromotionResult(arg0, this,
							arg1.getOrder(), 1.0F);
					if (null != promotionGiftProductList && !promotionGiftProductList.isEmpty())
					{
						final Map<String, Product> giftProductDetails = getDefaultPromotionsManager()
								.getGiftProductsUSSID(promotionGiftProductList, sellerIDData);
						if (null != giftProductDetails && !giftProductDetails.isEmpty())
						{
							getPromotionUtilityPOJO().setPromoProductList(promotionalProductData);
							for (final Map.Entry<String, Product> entry : giftProductDetails.entrySet())
							{
								final int giftCount = getDefaultPromotionsManager().getFreeGiftCount(entry.getKey(), eligibleProductMap,
										eligibleQuantity.intValue());
								final Map<String, List<String>> productAssociatedItemsMap = getDefaultPromotionsManager()
										.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap, entry.getKey());
								arg0.setAttribute(MarketplacecommerceservicesConstants.ASSOCIATEDITEMS, productAssociatedItemsMap);
								arg0.setAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE, String.valueOf(this.getCode()));
								freeResult.addAction(arg0, getDefaultPromotionsManager().createCustomPromotionOrderAddFreeGiftAction(arg0,
										entry.getValue(), entry.getKey(), freeResult, Double.valueOf(giftCount)));
							}
						}
					}
					promotionResults.add(freeResult);
				}
			}

			if (noOfProducts > 0 && GenericUtilityMethods.checkRestrictionData(restrictionList)) // For Localization: To check for Excluded Products
			{
				float certainty = (totalCount / eligibleQuantity.intValue());
				if (totalCount >= eligibleQuantity.intValue() && certainty > 0.00F)
				{
					if (!(totalCount % eligibleQuantity.intValue() == 0))
					{
						certainty = 0.00F;
					}
					else
					{
						certainty = 1.00F;
					}
				}
				final PromotionResult result = getDefaultPromotionsManager().createPromotionResult(arg0, this, arg1.getOrder(),
						certainty);
				promotionResults.add(result);
			}
		}
		else
		{
			if (GenericUtilityMethods.checkRestrictionData(restrictionList))
			{
				final PromotionResult result = getDefaultPromotionsManager().createPromotionResult(arg0, this, arg1.getOrder(),
						0.00F);
				promotionResults.add(result);
			}
		}
		return promotionResults;
	}


	/**
	 * @Description : Assign Promotion Fired and Potential-Promotion Message
	 * @param :
	 *           SessionContext paramSessionContext ,PromotionResult paramPromotionResult ,Locale paramLocale
	 * @return : String
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{

		final AbstractOrder order = arg1.getOrder(arg0);
		final Integer freeCount = Integer.valueOf(1);
		int finalNumberOfProducts = 0;
		try
		{
			if (order != null)
			{
				if (!isPercentageOrAmount().booleanValue() && !flagForCouldFireMessage)
				{
					return "";
				}

				final Currency orderCurrency = order.getCurrency(arg0);
				if (arg1.getFired(arg0))
				{
					if (isPercentageOrAmount().booleanValue())
					{
						final Double percentageDiscount = getPercentageDiscount(arg0);
						final Double totalDiscount = Double.valueOf(arg1.getTotalDiscount(arg0));
						final Object[] args =
						{ percentageDiscount, totalDiscount,
								Helper.formatCurrencyAmount(arg0, arg2, orderCurrency, totalDiscount.doubleValue()),
								MarketplacecommerceservicesConstants.PERCENTAGE_MESSAGE };
						return formatMessage(getMessageFired(arg0), args, arg2);
					}
					else
					{
						final Double discountPriceValue = getPriceForOrder(arg0, getDiscountPrices(arg0), order,
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES);

						final Double totalDiscount = Double.valueOf(arg1.getTotalDiscount(arg0));
						final Object[] args =
						{ discountPriceValue, totalDiscount,
								Helper.formatCurrencyAmount(arg0, arg2, orderCurrency, totalDiscount.doubleValue()),
								MarketplacecommerceservicesConstants.DISCOUNT_PRICES_MESSAGE };
						return formatMessage(getMessageFired(arg0), args, arg2);
					}
				}

				else if (arg1.getCouldFire(arg0))
				{
					final Integer qualifyingCount = Integer.valueOf(getQuantity().intValue());
					double minimumCategoryValue = 0.00D;
					if (null != getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
					{
						minimumCategoryValue = ((Double) getProperty(arg0, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
								.doubleValue();
						final int qCount = qualifyingCount.intValue();
						if (noOfProducts < qCount)
						{
							finalNumberOfProducts = qCount - noOfProducts;
						}
						else
						{
							final int factor = noOfProducts / qCount;
							finalNumberOfProducts = (factor + 1) * qCount - noOfProducts;
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
										paymentModes = deliveryModes + paymentModeList.get(0).getMode();
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
						return formatMessage(this.getMessageCouldHaveFired(arg0), args, arg2);
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
	 * The Getter Setter creation for Sonar Fix
	 *
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
