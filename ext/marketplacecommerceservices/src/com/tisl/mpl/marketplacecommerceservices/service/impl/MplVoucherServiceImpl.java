/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.VoucherCardPerOfferInvalidationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.promotions.util.Tuple3;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayCardStatusModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.coupon.enums.TimeLimitTypeEnum;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.AddCardResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.order.impl.MplDefaultCalculationService;
import com.tisl.mpl.util.DiscountUtility;


/**
 * @author TCS
 *
 */
public class MplVoucherServiceImpl implements MplVoucherService
{
	private static final Logger LOG = Logger.getLogger(MplVoucherServiceImpl.class);

	@Resource(name = "calculationService")
	private MplDefaultCalculationService mplDefaultCalculationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
	@Resource(name = "commerceCartService")
	private MplCommerceCartService mplCommerceCartService;
	@Resource(name = "voucherService")
	private VoucherService voucherService;
	@Resource(name = "commerceCartCalculationStrategy")
	private MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;

	@Resource(name = "mplVoucherDao")
	private MplVoucherDao mplVoucherDao;

	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private final static String CODE00 = "00".intern();
	private final static String CODE01 = "01".intern();
	private final static String CODE02 = "02".intern();
	private final static String CODE03 = "03".intern();

	/**
	 * @Description This method recalculates the cart after redeeming/releasing the voucher *
	 * @param cartModel
	 * @param orderModel
	 *           for TPR-629
	 *
	 */
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel, final OrderModel orderModel) throws EtailNonBusinessExceptions
	{
		try
		{
			LOG.debug("Step 8:::Inside recalculating cart after voucher apply");
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			if (null != cartModel)
			{
				if (cartModel.getEntries() != null)
				{
					for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
					{
						if (null != cartEntryModel.getGiveAway() && !cartEntryModel.getGiveAway().booleanValue())
						{
							freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
						}
					}
				}

				//recalculating cart
				final Double deliveryCost = cartModel.getDeliveryCost();
				Double modDeliveryCost = Double.valueOf(0);

				final CommerceCartParameter parameter = new CommerceCartParameter();
				parameter.setEnableHooks(true);
				parameter.setCart(cartModel);
				getMplCommerceCartCalculationStrategy().recalculateCart(parameter);

				//Code for Shipping Promotion & Voucher Integration
				//TPR-1702
				if (validateForShippingPromo(cartModel.getAllPromotionResults()))
				{
					modDeliveryCost = Double.valueOf(deliveryCost.doubleValue() - getModifiedDeliveryCost(cartModel.getEntries()));
				}
				//TPR-1702 : Changes Ends

				cartModel.setDeliveryCost(deliveryCost);
				//				cartModel.setTotalPrice(Double.valueOf((null == cartModel.getTotalPrice() ? 0.0d : cartModel.getTotalPrice()
				//						.doubleValue()) + (null == deliveryCost ? 0.0d : deliveryCost.doubleValue())));
				//TPR-1702 : Changes for Shipping + Coupon
				cartModel.setTotalPrice(
						Double.valueOf((null == cartModel.getTotalPrice() ? 0.0d : cartModel.getTotalPrice().doubleValue())
								+ (null == deliveryCost ? 0.0d : deliveryCost.doubleValue()) - modDeliveryCost.doubleValue()));
				//TPR-1702 : Changes for Shipping + Coupon

				// Freebie item changes
				getMplCommerceCartService().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

				LOG.debug("Step 9:::Recalculation done successfully");

				getModelService().save(cartModel);
			}
			else if (null != orderModel)
			{
				if (orderModel.getEntries() != null)
				{
					for (final AbstractOrderEntryModel orderEntryModel : orderModel.getEntries())
					{
						if (null != orderEntryModel.getGiveAway() && !orderEntryModel.getGiveAway().booleanValue())
						{
							freebieModelMap.put(orderEntryModel.getSelectedUSSID(), orderEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(orderEntryModel.getSelectedUSSID(), orderEntryModel.getQuantity());
						}
					}
				}

				//recalculating cart
				final Double deliveryCost = orderModel.getDeliveryCost();
				Double modDeliveryCost = Double.valueOf(0);

				final CommerceCartParameter parameter = new CommerceCartParameter();
				parameter.setEnableHooks(true);
				parameter.setOrder(orderModel);
				getMplCommerceCartCalculationStrategy().recalculateCart(parameter);

				//Code for Shipping Promotion & Voucher Integration
				//TPR-1702
				if (validateForShippingPromo(orderModel.getAllPromotionResults()))
				{
					modDeliveryCost = Double.valueOf(deliveryCost.doubleValue() - getModifiedDeliveryCost(orderModel.getEntries()));
				}
				//TPR-1702 : Changes Ends

				orderModel.setDeliveryCost(deliveryCost);
				//TPR-1702 : Changes for Shipping + Coupon
				orderModel.setTotalPrice(
						Double.valueOf((null == orderModel.getTotalPrice() ? 0.0d : orderModel.getTotalPrice().doubleValue())
								+ (null == deliveryCost ? 0.0d : deliveryCost.doubleValue()) - modDeliveryCost.doubleValue()));
				//TPR-1702 : Changes for Shipping + Coupon

				// Freebie item changes
				getMplCommerceCartService().saveDeliveryMethForFreebie(orderModel, freebieModelMap, freebieParentQtyMap);

				LOG.debug("Step 9:::Recalculation done successfully");

				getModelService().save(orderModel);
			}

		}
		catch (final IllegalStateException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0012);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}

	}


	/**
	 * Provide the Modified Delivery Cost for Shipping Promotions
	 *
	 * @param entries
	 * @return deliveryCost
	 */
	private double getModifiedDeliveryCost(final List<AbstractOrderEntryModel> entries)
	{
		double deliveryCost = 0.0D;
		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (null != entry && !entry.getGiveAway().booleanValue() && null != entry.getCurrDelCharge()
						&& entry.getCurrDelCharge().doubleValue() > 0)
				{
					deliveryCost += entry.getCurrDelCharge().doubleValue();
				}
			}
		}
		return deliveryCost;
	}




	/**
	 * Validate Cart for Shipping Promotion
	 *
	 * @param allPromotionResults
	 * @return flag
	 */
	private boolean validateForShippingPromo(final Set<PromotionResultModel> allPromotionResults)
	{
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(allPromotionResults))
		{
			final List<PromotionResultModel> promotionList = new ArrayList<PromotionResultModel>(allPromotionResults);
			for (final PromotionResultModel oModel : promotionList)
			{
				if (oModel.getCertainty().floatValue() == 1.0F && null != oModel.getPromotion()
						&& (oModel.getPromotion() instanceof BuyAboveXGetPromotionOnShippingChargesModel
								|| oModel.getPromotion() instanceof BuyAGetPromotionOnShippingChargesModel
								|| oModel.getPromotion() instanceof BuyAandBGetPromotionOnShippingChargesModel))
				{
					flag = true;
					break;
				}
			}
		}
		return flag;
	}




	/**
	 * @Description Checks the cart after applying the voucher
	 * @param lastVoucher
	 * @param cartModel
	 * @param orderModel
	 *           for TPR-629
	 * @throws EtailNonBusinessExceptions
	 * @throws ModelSavingException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@Override
	public VoucherDiscountData checkCartAfterApply(final VoucherModel lastVoucher, final CartModel cartModel,
			final OrderModel orderModel, final List<AbstractOrderEntryModel> applicableOrderEntryList)
			throws VoucherOperationException, EtailNonBusinessExceptions
	{
		VoucherDiscountData discountData = new VoucherDiscountData();
		final long startTime = System.currentTimeMillis();
		try
		{
			LOG.debug("Step 11:::Inside checking cart after applying voucher");
			//Total amount in cart updated with delay... Calculating value of voucher regarding to order

			if (cartModel != null)
			{
				//For cart
				final double cartSubTotal = cartModel.getSubtotal().doubleValue();
				double voucherCalcValue = 0.0;
				double promoCalcValue = 0.0;
				List<DiscountValue> discountList = cartModel.getGlobalDiscountValues(); //Discount values against the cart
				String voucherCode = null;
				if (lastVoucher instanceof PromotionVoucherModel)
				{
					voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
					discountData.setVoucherCode(voucherCode);
				}

				final List<DiscountModel> voucherList = cartModel.getDiscounts(); //List of discounts against the cart
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && null != voucherList.get(0).getCode()
								&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
						{
							voucherCalcValue = discount.getValue();
						}
						else
						{
							promoCalcValue = discount.getValue();
						}
					}
				}

				final StringBuilder logBuilder = new StringBuilder();
				LOG.debug(logBuilder.append("Step 12:::Voucher discount in cart is ").append(voucherCalcValue)
						.append(" & promo discount in cart is ").append(promoCalcValue));

				if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
						&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
				{
					LOG.debug("Step 13:::Inside max discount block");
					discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher,
							lastVoucher.getMaxDiscountValue().doubleValue());
					cartModel.setGlobalDiscountValues(discountList);

					getMplDefaultCalculationService().calculateTotals(cartModel, false);
					getModelService().save(cartModel);

					discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel,
							Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
				}
				else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
				{
					LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
					discountData = releaseVoucherAfterCheck(cartModel, null, voucherCode, null, applicableOrderEntryList, voucherList);
				}
				else
				//In other cases
				{
					double netAmountAfterAllDisc = 0.0D;
					double productPrice = 0.0D;
					int size = 0;

					if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
					{
						LOG.debug("Step 15:::applicableOrderEntryList is not empty");
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							size += (null == entry.getQuantity()) ? 0 : entry.getQuantity().intValue(); //Size in total count of all the order entries present in cart
						}
						final BigDecimal cartTotalThreshold = BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(size)); //Threshold is min value which is allowable after applying coupon
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode()
									&& StringUtils.isNotEmpty(entry.getProductPromoCode()))
									|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
											? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice().doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug(
									"Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseVoucherAfterCheck(cartModel, null, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) > 0
								&& ((BigDecimal.valueOf(netAmountAfterAllDisc).subtract(BigDecimal.valueOf(voucherCalcValue)))
										.compareTo(cartTotalThreshold) == -1)) //When cart value after applying discount is less than .01*count of applicable entries
						{
							LOG.debug("Step 16.1:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) >= 0 < 0.01 block");
							discountData = releaseVoucherAfterCheck(cartModel, null, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else
						//In other cases, just set the coupon discount for the discount data
						{
							discountData
									.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
						}
					}
					else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries list is empty
					{
						LOG.debug("Step 17:::applicable entries empty");
						discountData = releaseVoucherAfterCheck(cartModel, null, voucherCode, null, applicableOrderEntryList,
								voucherList);
					}
					else
					//In other cases, just set the coupon discount for the discount data
					{
						discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
					}
				}
			}
			else if (orderModel != null)
			{
				//For order
				final double cartSubTotal = orderModel.getSubtotal().doubleValue();
				double voucherCalcValue = 0.0;
				double promoCalcValue = 0.0;
				List<DiscountValue> discountList = orderModel.getGlobalDiscountValues(); //Discount values against the cart
				String voucherCode = null;
				if (lastVoucher instanceof PromotionVoucherModel)
				{
					voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
					discountData.setVoucherCode(voucherCode);
				}

				final List<DiscountModel> voucherList = orderModel.getDiscounts(); //List of discounts against the cart
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && null != voucherList.get(0).getCode()
								&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
						{
							voucherCalcValue = discount.getValue();
						}
						else
						{
							promoCalcValue = discount.getValue();
						}
					}
				}

				final StringBuilder logBuilder = new StringBuilder();
				LOG.debug(logBuilder.append("Step 12:::Voucher discount in cart is ").append(voucherCalcValue)
						.append(" & promo discount in cart is ").append(promoCalcValue));

				if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
						&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
				{
					LOG.debug("Step 13:::Inside max discount block");
					discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher,
							lastVoucher.getMaxDiscountValue().doubleValue());
					orderModel.setGlobalDiscountValues(discountList);
					getMplDefaultCalculationService().calculateTotals(orderModel, false);
					getModelService().save(orderModel);

					discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel,
							Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
				}
				else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
				{
					LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
					discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, null, applicableOrderEntryList,
							voucherList);
				}
				else
				//In other cases
				{
					double netAmountAfterAllDisc = 0.0D;
					double productPrice = 0.0D;
					int size = 0;

					if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
					{
						LOG.debug("Step 15:::applicableOrderEntryList is not empty");
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							size += (null == entry.getQuantity()) ? 0 : entry.getQuantity().intValue(); //Size in total count of all the order entries present in cart
						}
						final BigDecimal cartTotalThreshold = BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(size)); //Threshold is min value which is allowable after applying coupon
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode()
									&& StringUtils.isNotEmpty(entry.getProductPromoCode()))
									|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
											? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice().doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug(
									"Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) > 0
								&& ((BigDecimal.valueOf(netAmountAfterAllDisc).subtract(BigDecimal.valueOf(voucherCalcValue)))
										.compareTo(cartTotalThreshold) == -1)) //When cart value after applying discount is less than .01*count of applicable entries
						{
							LOG.debug("Step 16.1:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) >= 0 < 0.01 block");
							discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else
						//In other cases, just set the coupon discount for the discount data
						{
							discountData
									.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(voucherCalcValue)));
						}
					}
					else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries list is empty
					{
						LOG.debug("Step 17:::applicable entries empty");
						discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, null, applicableOrderEntryList,
								voucherList);
					}
					else
					//In other cases, just set the coupon discount for the discount data
					{
						discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(voucherCalcValue)));
					}
				}
			}


		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final CalculationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0017);
		}
		catch (final NumberFormatException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0015);
		}
		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service checkCartAfterApply====== " + (endTime - startTime));
		return discountData;
	}

	/**
	 * @Description This is for releasing voucher
	 * @param cartModel
	 * @param orderModel
	 *           TPR-629
	 * @param voucherCode
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 */
	private VoucherDiscountData releaseVoucherAfterCheck(final CartModel cartModel, final OrderModel orderModel,
			final String voucherCode, final Double productPrice, final List<AbstractOrderEntryModel> applicableOrderEntryList,
			final List<DiscountModel> voucherList) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		final VoucherDiscountData discountData = new VoucherDiscountData();
		String msg = null;
		try
		{
			if (null != cartModel)
			{
				//For cart
				releaseVoucher(voucherCode, cartModel, null); //Releases voucher
				recalculateCartForCoupon(cartModel, null); //Recalculates cart after releasing voucher
				getModelService().save(cartModel);

				discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(0)));
				if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
				{
					msg = MarketplacecommerceservicesConstants.NOTAPPLICABLE;
				}
				else if (null != productPrice && productPrice.doubleValue() < 1)
				{
					msg = MarketplacecommerceservicesConstants.EXCFREEBIE;
				}
				else
				{
					msg = MarketplacecommerceservicesConstants.PRICEEXCEEDED;
				}

			}
			else if (null != orderModel)
			{
				//For order
				releaseVoucher(voucherCode, null, orderModel); //Releases voucher
				recalculateCartForCoupon(null, orderModel); //Recalculates cart after releasing voucher
				getModelService().save(orderModel); //TPR-1079

				discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(0))); //TPR-1079/
				if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
				{
					msg = MarketplacecommerceservicesConstants.NOTAPPLICABLE;
				}
				else if (null != productPrice && productPrice.doubleValue() < 1)
				{
					msg = MarketplacecommerceservicesConstants.EXCFREEBIE;
				}
				else
				{
					msg = MarketplacecommerceservicesConstants.PRICEEXCEEDED;
				}

			}


			discountData.setRedeemErrorMsg(msg);

		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		return discountData;
	}

	/**
	 * @Description: For getting list of applicable AbstractOrderEntry from voucherEntrySet
	 * @param voucherModel
	 * @param abstractOrderModel
	 * @return list of applicable AbstractOrderEntry
	 */
	@Override
	public List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherModel voucherModel,
			final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		LOG.debug("Step 9:::Inside getOrderEntriesFromVoucherEntries");
		final VoucherEntrySet voucherEntrySet = getVoucherModelService().getApplicableEntries(voucherModel, abstractOrderModel);
		final List<AbstractOrderEntry> applicableOrderList = new ArrayList<AbstractOrderEntry>();

		for (final Object voucherEntry : voucherEntrySet)
		{
			applicableOrderList.add(((VoucherEntry) voucherEntry).getOrderEntry());
		}

		return applicableOrderList;
	}


	/**
	 * @Description This method adds a new global discount and removes the old global discount
	 * @param discountList
	 * @param voucherList
	 * @param cartSubTotal
	 * @param promoCalcValue
	 * @param lastVoucher
	 * @param discountAmt
	 * @return List<DiscountValue>
	 */
	@Override
	public List<DiscountValue> setGlobalDiscount(final List<DiscountValue> discountList, final List<DiscountModel> voucherList,
			final double cartSubTotal, final double promoCalcValue, final VoucherModel lastVoucher, final double discountAmt)
	{
		DiscountValue discountValue = null;

		final Iterator iter = discountList.iterator();

		//Remove the existing discount and add a new discount row
		while (iter.hasNext())
		{
			final DiscountValue discount = (DiscountValue) iter.next();
			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discountAmt, discount.getCurrencyIsoCode());

				iter.remove();
				break;
			}
		}

		discountList.add(discountValue);
		return discountList;
	}


	/**
	 * @Description This is for releasing voucher
	 * @param cartModel
	 * @param voucherCode
	 * @param orderModel
	 *           for TPR-629
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions
	{
		try
		{
			LOG.debug("Step 2:::Inside releaseVoucher");
			validateVoucherCodeParameter(voucherCode);
			final VoucherModel voucher = getVoucherService().getVoucher(voucherCode); //Finds voucher for the selected code
			if (voucher == null)
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
			}
			else if (cartModel != null)
			{
				//For cart
				LOG.debug("Step 3:::Voucher and cart is not null");

				getVoucherService().releaseVoucher(voucherCode, cartModel); //Releases the voucher from the cart
				LOG.debug("Step 4:::Voucher released");
				final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(voucher, cartModel);//new ArrayList<AbstractOrderEntryModel>();
				for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
				{
					entry.setCouponCode("");
					entry.setCouponValue(Double.valueOf(0.00D));

					//TPR-7408 starts here
					entry.setCouponCostCentreOnePercentage(Double.valueOf(0.00D));
					entry.setCouponCostCentreTwoPercentage(Double.valueOf(0.00D));
					entry.setCouponCostCentreThreePercentage(Double.valueOf(0.00D));
					//TPR-7408 ends here

				}
				if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
				{
					getModelService().saveAll(entryList);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
			}
			else if (null != orderModel)
			{
				//For order
				LOG.debug("Step 3:::Voucher and cart is not null");
				//Added for INC144317090: For Mobile App, releaseCoupons API is getting called on order if user clicks on application "back" button from order confirmation page
				if (!OrderStatus.PAYMENT_SUCCESSFUL.equals(orderModel.getStatus())
						&& CollectionUtils.isEmpty(orderModel.getChildOrders()))
				{

					getVoucherService().releaseVoucher(voucherCode, orderModel); //Releases the voucher from the order
					LOG.debug("Step 4:::Voucher released");
					final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(voucher, orderModel);//new ArrayList<AbstractOrderEntryModel>();
					for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
					{
						entry.setCouponCode("");
						entry.setCouponValue(Double.valueOf(0.00D));

						//TPR-7408 starts here
						entry.setCouponCostCentreOnePercentage(Double.valueOf(0.00D));
						entry.setCouponCostCentreTwoPercentage(Double.valueOf(0.00D));
						entry.setCouponCostCentreThreePercentage(Double.valueOf(0.00D));
						//TPR-7408 ends here

					}
					if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
					{
						getModelService().saveAll(entryList);
					}

					LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
				}
			}

		}
		catch (final JaloPriceFactoryException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ConsistencyCheckException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}
	}


	/**
	 * Validates the voucher code parameter
	 *
	 * @param voucherCode
	 */
	protected void validateVoucherCodeParameter(final String voucherCode)
	{
		if (StringUtils.isBlank(voucherCode))
		{
			throw new IllegalArgumentException("Parameter voucherCode must not be empty");
		}
	}


	/**
	 * @Description For getting list of applicable AbstractOrderEntryModel from voucherEntrySet
	 * @param voucherModel
	 * @param abstractOrderModel
	 * @return public List<AbstractOrderEntryModel>
	 */
	@Override
	public List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Step 11:::Inside getOrderEntryModelFromVouEntries");

		final List<AbstractOrderEntryModel> applicableOrderList = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntry entry : getOrderEntriesFromVoucherEntries(voucherModel, abstractOrderModel)) //Converts applicable order entries from AbstractOrderEntry to AbstractOrderEntryModel
		{
			applicableOrderList.add((AbstractOrderEntryModel) getModelService().get(entry));
		}

		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service getOrderEntryModelFromVouEntries====== " + (endTime - startTime));
		return applicableOrderList;
	}



	/**
	 * @Description: For Apportioning of vouchers
	 * @param voucher
	 * @param abstractOrderModel
	 * @param voucherCode
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void setApportionedValueForVoucher(final VoucherModel voucher, final AbstractOrderModel abstractOrderModel,
			final String voucherCode, final List<AbstractOrderEntryModel> applicableOrderEntryList) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			final long startTime = System.currentTimeMillis();
			LOG.debug("Step 18:::Inside setApportionedValueForVoucher");
			if (CollectionUtils.isNotEmpty(abstractOrderModel.getDiscounts()))
			{
				double totalApplicablePrice = 0.0D;
				final BigDecimal percentageDiscount = null;
				updateAppOrderEntriesForFreebieBogo(applicableOrderEntryList);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					totalApplicablePrice += entry.getTotalPrice() == null ? 0.0D : entry.getTotalPrice().doubleValue();
				}

				final List<DiscountValue> discountList = abstractOrderModel.getGlobalDiscountValues(); //Discount values against the cart
				final List<DiscountModel> voucherList = abstractOrderModel.getDiscounts(); //List of discounts against the cart
				double discountValue = 0.0;
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && null != voucherList.get(0).getCode()
								&& discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode())) //Only 1 voucher can be applied and code is mandatory field
						{
							discountValue = discount.getValue();
						}
					}
				}

				final double formattedDiscVal = Math.round(discountValue * 100.0) / 100.0;

				LOG.debug("Step 19:::percentageDiscount is " + percentageDiscount);

				BigDecimal totalAmtDeductedOnItemLevel = BigDecimal.valueOf(0);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					BigDecimal entryLevelApportionedPrice = null;
					double currNetAmtAftrAllDisc = 0.00D;

					final double entryTotalPrice = entry.getTotalPrice().doubleValue();
					final BigDecimal discountPriceValue = BigDecimal.valueOf(formattedDiscVal);

					if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
					{
						entryLevelApportionedPrice = getApportionedValueForSingleEntry(discountPriceValue, totalAmtDeductedOnItemLevel);
					}
					else
					{
						entryLevelApportionedPrice = getApportionedValueForEntry(discountPriceValue, entryTotalPrice,
								totalApplicablePrice);
						totalAmtDeductedOnItemLevel = totalAmtDeductedOnItemLevel.add(entryLevelApportionedPrice);
					}

					LOG.debug("Step 20:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

					entry.setCouponCode(null == voucherCode ? voucher.getCode() : voucherCode);
					entry.setCouponValue(Double.valueOf(entryLevelApportionedPrice.doubleValue()));


					//TPR-7408 starts here
					if (null != voucher)
					{
						if (StringUtils.isNotEmpty(String.valueOf(voucher.getCouponCostCentreOnePercentage())))
						{
							entry.setCouponCostCentreOnePercentage(voucher.getCouponCostCentreOnePercentage());
						}
						if (StringUtils.isNotEmpty(String.valueOf(voucher.getCouponCostCentreTwoPercentage())))
						{
							entry.setCouponCostCentreTwoPercentage(voucher.getCouponCostCentreTwoPercentage());
						}
						if (StringUtils.isNotEmpty(String.valueOf(voucher.getCouponCostCentreThreePercentage())))
						{
							entry.setCouponCostCentreThreePercentage(voucher.getCouponCostCentreThreePercentage());
						}
					}
					//TPR-7408 ends here

					if ((StringUtils.isNotEmpty(entry.getProductPromoCode())) || (StringUtils.isNotEmpty(entry.getCartPromoCode())))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() == null ? 0.00D
								: entry.getNetAmountAfterAllDisc().doubleValue();
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(netAmtAftrAllDisc, entryLevelApportionedPrice);
					}
					else
					{
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(entryTotalPrice, entryLevelApportionedPrice);
					}

					LOG.debug("Step 21:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);

					entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
				}

				getModelService().saveAll(applicableOrderEntryList);

				final Double totalPrice = setTotalPrice(abstractOrderModel);
				abstractOrderModel.setTotalPrice(totalPrice);
				getModelService().save(abstractOrderModel);
			}

			final long endTime = System.currentTimeMillis();
			LOG.debug("Exiting service setApportionedValueForVoucher====== " + (endTime - startTime));
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
	}



	/**
	 * @Descriotion This is for getting apportioned value for a single entry
	 * @param discountPriceValue
	 * @param totalAmtDeductedOnItemLevel
	 * @return BigDecimal
	 */

	private BigDecimal getApportionedValueForSingleEntry(final BigDecimal discountPriceValue,
			final BigDecimal totalAmtDeductedOnItemLevel)
	{
		try
		{
			return discountPriceValue.subtract(totalAmtDeductedOnItemLevel);
		}
		catch (final ArithmeticException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0019);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Descriotion This is for getting apportioned value for any entry except the last one
	 * @param discountPriceValue
	 * @param totalApplicablePrice
	 * @param entryTotalPrice
	 * @return BigDecimal
	 */
	private BigDecimal getApportionedValueForEntry(final BigDecimal discountPriceValue, final double entryTotalPrice,
			final double totalApplicablePrice)
	{
		try
		{
			return discountPriceValue.multiply(BigDecimal.valueOf(entryTotalPrice / totalApplicablePrice));
		}
		catch (final ArithmeticException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0019);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Descriotion This is for getting apportioned value for entry
	 * @param amount
	 * @param entryLevelApportionedPrice
	 * @return BigDecimal
	 */
	private double getCurrNetAmtAftrAllDisc(final double amount, final BigDecimal entryLevelApportionedPrice)
	{
		return (amount > entryLevelApportionedPrice.doubleValue()) ? (amount - entryLevelApportionedPrice.doubleValue())
				: Double.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
	}



	/**
	 * This method removes the freebie items from voucher applicable entries
	 *
	 * @param applicableOrderEntryList
	 */
	private void updateAppOrderEntriesForFreebieBogo(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		final Iterator iterator = applicableOrderEntryList.iterator();
		while (iterator.hasNext())
		{
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) iterator.next();
			if (null != entry.getTotalPrice() && entry.getTotalPrice().doubleValue() < 1)
			{
				iterator.remove();
			}
		}
	}



	/**
	 * This method checks the cart after promotion application(recalculate cart) and handles custom voucher calculation
	 *
	 * @param cartModel
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void checkCartWithVoucher(final CartModel cartModel) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		//setting coupon discount starts
		if (CollectionUtils.isNotEmpty(cartModel.getDiscounts())
				&& cartModel.getDiscounts().get(0) instanceof PromotionVoucherModel)
		{
			final PromotionVoucherModel voucher = (PromotionVoucherModel) cartModel.getDiscounts().get(0);
			final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, cartModel);
			checkCartAfterApply(voucher, cartModel, null, applicableOrderEntryList); //Checking the cart after OOB voucher application
			setApportionedValueForVoucher(voucher, cartModel, voucher.getVoucherCode(), applicableOrderEntryList); //Apportioning voucher discount
		}
		//setting coupon discount ends
	}

	/**
	 * This method returns Invalidation model for a particular voucher-user-order
	 *
	 * @param voucher
	 * @param user
	 * @param order
	 * @return VoucherInvalidationModel
	 */
	@Override
	public VoucherInvalidationModel findVoucherInvalidation(final VoucherModel voucher, final UserModel user,
			final OrderModel order)
	{
		return mplVoucherDao.findVoucherInvalidation(voucher, user, order);
	}



	/**
	 * @return the mplDefaultCalculationService
	 */
	public MplDefaultCalculationService getMplDefaultCalculationService()
	{
		return mplDefaultCalculationService;
	}




	/**
	 * @param mplDefaultCalculationService
	 *           the mplDefaultCalculationService to set
	 */
	public void setMplDefaultCalculationService(final MplDefaultCalculationService mplDefaultCalculationService)
	{
		this.mplDefaultCalculationService = mplDefaultCalculationService;
	}




	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}




	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}




	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}


	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}




	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}




	/**
	 * @return the mplCommerceCartCalculationStrategy
	 */
	public MplCommerceCartCalculationStrategy getMplCommerceCartCalculationStrategy()
	{
		return mplCommerceCartCalculationStrategy;
	}




	/**
	 * @param mplCommerceCartCalculationStrategy
	 *           the mplCommerceCartCalculationStrategy to set
	 */
	public void setMplCommerceCartCalculationStrategy(final MplCommerceCartCalculationStrategy mplCommerceCartCalculationStrategy)
	{
		this.mplCommerceCartCalculationStrategy = mplCommerceCartCalculationStrategy;
	}




	/**
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}




	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
	}




	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}




	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}


	/**
	 * The method deals with calculation after Cart Voucher apply
	 *
	 * @param lastVoucher
	 * @param cartModel
	 * @param orderModel
	 * @param applicableOrderEntryList
	 */
	@Override
	public VoucherDiscountData checkCartAfterCartVoucherApply(final VoucherModel lastVoucher, final CartModel cartModel,
			final OrderModel orderModel, final List<AbstractOrderEntryModel> applicableOrderEntryList)
			throws VoucherOperationException, EtailNonBusinessExceptions
	{

		VoucherDiscountData discountData = new VoucherDiscountData();
		final long startTime = System.currentTimeMillis();
		try
		{
			LOG.debug("Step 11:::Inside checking cart after applying voucher");
			//Total amount in cart updated with delay... Calculating value of voucher regarding to order

			if (cartModel != null)
			{
				//For cart
				final double cartSubTotal = cartModel.getSubtotal().doubleValue();
				double voucherCalcValue = 0.0;
				double discountCalcValue = 0.0;

				List<DiscountValue> discountList = new ArrayList<>(cartModel.getGlobalDiscountValues()); //Discount values against the cart
				String voucherCode = null;

				if (lastVoucher instanceof MplCartOfferVoucherModel)
				{
					voucherCode = ((MplCartOfferVoucherModel) lastVoucher).getVoucherCode();
					discountData.setVoucherCode(voucherCode);
				}

				final List<DiscountModel> voucherList = cartModel.getDiscounts(); //List of discounts against the cart
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && discount.getCode().equalsIgnoreCase(lastVoucher.getCode()))
						{
							voucherCalcValue = discount.getValue();
						}

						discountCalcValue += discount.getValue();

					}
				}


				if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
						&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
				{
					LOG.debug("Step 13:::Inside max discount block");
					discountList = setGlobalDiscount(discountList, lastVoucher, lastVoucher.getMaxDiscountValue().doubleValue());
					cartModel.setGlobalDiscountValues(discountList);
					getMplDefaultCalculationService().calculateTotals(cartModel, false);
					getModelService().save(cartModel);

					discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel,
							Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
				}
				else if (voucherCalcValue != 0 && (cartSubTotal - discountCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
				{
					LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
					discountData = releaseCartVoucherAfterCheck(cartModel, null, voucherCode, null, applicableOrderEntryList,
							voucherList);
				}
				else
				//In other cases
				{
					double netAmountAfterAllDisc = 0.0D;
					double productPrice = 0.0D;
					int size = 0;

					if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
					{
						LOG.debug("Step 15:::applicableOrderEntryList is not empty");
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							size += (null == entry.getQuantity()) ? 0 : entry.getQuantity().intValue(); //Size in total count of all the order entries present in cart
						}
						final BigDecimal cartTotalThreshold = BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(size)); //Threshold is min value which is allowable after applying coupon
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode()
									&& StringUtils.isNotEmpty(entry.getProductPromoCode()))
									|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
											? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice().doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						//						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
						//								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug(
									"Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseCartVoucherAfterCheck(cartModel, null, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) > 0
								&& ((BigDecimal.valueOf(netAmountAfterAllDisc).subtract(BigDecimal.valueOf(voucherCalcValue)))
										.compareTo(cartTotalThreshold) == -1)) //When cart value after applying discount is less than .01*count of applicable entries
						{
							LOG.debug("Step 16.1:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) >= 0 < 0.01 block");
							discountData = releaseCartVoucherAfterCheck(cartModel, null, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else
						//In other cases, just set the coupon discount for the discount data
						{
							discountData
									.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
						}
					}
					else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries list is empty
					{
						LOG.debug("Step 17:::applicable entries empty");
						discountData = releaseCartVoucherAfterCheck(cartModel, null, voucherCode, null, applicableOrderEntryList,
								voucherList);
					}
					else
					//In other cases, just set the coupon discount for the discount data
					{
						discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(voucherCalcValue)));
					}
				}
			}
			else if (orderModel != null)
			{
				//For order
				final double cartSubTotal = orderModel.getSubtotal().doubleValue();
				double voucherCalcValue = 0.0;
				double discountCalcValue = 0.0;
				List<DiscountValue> discountList = orderModel.getGlobalDiscountValues(); //Discount values against the cart
				String voucherCode = null;
				if (lastVoucher instanceof PromotionVoucherModel)
				{
					voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();
					discountData.setVoucherCode(voucherCode);
				}

				final List<DiscountModel> voucherList = new ArrayList<>(orderModel.getDiscounts()); //List of discounts against the cart
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && discount.getCode().equalsIgnoreCase(voucherCode))
						{
							voucherCalcValue = discount.getValue();
						}

						discountCalcValue += discount.getValue();

					}
				}

				//				final StringBuilder logBuilder = new StringBuilder();
				//				LOG.debug(logBuilder.append("Step 12:::Voucher discount in cart is ").append(voucherCalcValue)
				//						.append(" & promo discount in cart is ").append(promoCalcValue));

				if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
						&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue()) //When discount value is greater than coupon max discount value
				{
					LOG.debug("Step 13:::Inside max discount block");
					discountList = setGlobalDiscount(discountList, lastVoucher, lastVoucher.getMaxDiscountValue().doubleValue());
					orderModel.setGlobalDiscountValues(discountList);
					getMplDefaultCalculationService().calculateTotals(orderModel, false);
					getModelService().save(orderModel);

					discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel,
							Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
				}
				else if (voucherCalcValue != 0 && (cartSubTotal - discountCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
				{
					LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
					discountData = releaseCartVoucherAfterCheck(null, orderModel, voucherCode, null, applicableOrderEntryList,
							voucherList);
				}
				else
				//In other cases
				{
					double netAmountAfterAllDisc = 0.0D;
					double productPrice = 0.0D;
					int size = 0;

					if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
					{
						LOG.debug("Step 15:::applicableOrderEntryList is not empty");
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							size += (null == entry.getQuantity()) ? 0 : entry.getQuantity().intValue(); //Size in total count of all the order entries present in cart
						}
						final BigDecimal cartTotalThreshold = BigDecimal.valueOf(0.01).multiply(BigDecimal.valueOf(size)); //Threshold is min value which is allowable after applying coupon
						for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
						{
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode()
									&& StringUtils.isNotEmpty(entry.getProductPromoCode()))
									|| (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry.getCartPromoCode())))
											? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice().doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						//						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
						//								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug(
									"Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseCartVoucherAfterCheck(null, orderModel, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) > 0
								&& ((BigDecimal.valueOf(netAmountAfterAllDisc).subtract(BigDecimal.valueOf(voucherCalcValue)))
										.compareTo(cartTotalThreshold) == -1)) //When cart value after applying discount is less than .01*count of applicable entries
						{
							LOG.debug("Step 16.1:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) >= 0 < 0.01 block");
							discountData = releaseCartVoucherAfterCheck(null, orderModel, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else
						//In other cases, just set the coupon discount for the discount data
						{
							discountData
									.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(voucherCalcValue)));
						}
					}
					else if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList)) //When applicable entries list is empty
					{
						LOG.debug("Step 17:::applicable entries empty");
						discountData = releaseCartVoucherAfterCheck(null, orderModel, voucherCode, null, applicableOrderEntryList,
								voucherList);
					}
					else
					//In other cases, just set the coupon discount for the discount data
					{
						discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(voucherCalcValue)));
					}
				}
			}


		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final CalculationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0017);
		}
		catch (final NumberFormatException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0015);
		}
		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service checkCartAfterApply====== " + (endTime - startTime));
		return discountData;

	}


	/**
	 * The method is used to remove the Cart Coupon Details
	 *
	 * @param cartModel
	 * @param orderModel
	 * @param voucherCode
	 * @param productPrice
	 * @param applicableOrderEntryList
	 * @param voucherList
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 * @throws VoucherOperationException
	 */
	@Override
	public VoucherDiscountData releaseCartVoucherAfterCheck(final CartModel cartModel, final OrderModel orderModel,
			final String voucherCode, final Double productPrice, final List<AbstractOrderEntryModel> applicableOrderEntryList,
			final List<DiscountModel> voucherList) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		final VoucherDiscountData discountData = new VoucherDiscountData();
		String msg = null;
		try
		{
			if (null != cartModel)
			{
				//For cart
				releaseCartVoucher(voucherCode, cartModel, null); //Releases voucher
				recalculateCartForCoupon(cartModel, null); //Recalculates cart after releasing voucher
				getModelService().save(cartModel);

				discountData.setCouponDiscount(getDiscountUtility().createPrice(cartModel, Double.valueOf(0)));
				if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
				{
					msg = MarketplacecommerceservicesConstants.NOTAPPLICABLE;
				}
				else if (null != productPrice && productPrice.doubleValue() < 1)
				{
					msg = MarketplacecommerceservicesConstants.EXCFREEBIE;
				}
				else
				{
					msg = MarketplacecommerceservicesConstants.PRICEEXCEEDED;
				}

			}
			else if (null != orderModel)
			{
				//For order
				releaseCartVoucher(voucherCode, null, orderModel); //Releases voucher
				recalculateCartForCoupon(null, orderModel); //Recalculates cart after releasing voucher
				getModelService().save(orderModel); //TPR-1079

				discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel, Double.valueOf(0))); //TPR-1079/
				if (CollectionUtils.isEmpty(applicableOrderEntryList) && CollectionUtils.isNotEmpty(voucherList))
				{
					msg = MarketplacecommerceservicesConstants.NOTAPPLICABLE;
				}
				else if (null != productPrice && productPrice.doubleValue() < 1)
				{
					msg = MarketplacecommerceservicesConstants.EXCFREEBIE;
				}
				else
				{
					msg = MarketplacecommerceservicesConstants.PRICEEXCEEDED;
				}

			}
			discountData.setRedeemErrorMsg(msg);

		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		return discountData;
	}


	/**
	 * The Method creates a fresh Discount Value
	 *
	 * @param discountList
	 * @param lastVoucher
	 * @param discountAmt
	 * @return List<DiscountValue>
	 */
	private List<DiscountValue> setGlobalDiscount(final List<DiscountValue> discountList, final VoucherModel lastVoucher,
			final double discountAmt)
	{

		DiscountValue discountValue = null;

		final Iterator iter = discountList.iterator();

		//Remove the existing discount and add a new discount row
		while (iter.hasNext())
		{
			final DiscountValue discount = (DiscountValue) iter.next();
			if (discount.getCode().equalsIgnoreCase(lastVoucher.getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discountAmt, discount.getCurrencyIsoCode());

				iter.remove();
				break;
			}
		}

		discountList.add(discountValue);
		return discountList;

	}


	/**
	 * The Method outlines the apportioning for Cart Vouchers
	 *
	 * @param voucher
	 * @param abstractOrderModel
	 * @param voucherCode
	 * @param applicableOrderEntryList
	 */
	@Override
	public void setApportionedValueForCartVoucher(final VoucherModel voucher, final AbstractOrderModel abstractOrderModel,
			final String voucherCode, final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		try
		{
			final long startTime = System.currentTimeMillis();
			LOG.debug("Step 18:::Inside setApportionedValueForVoucher for Cart Vouchers ");
			if (CollectionUtils.isNotEmpty(abstractOrderModel.getDiscounts()))
			{
				double totalApplicablePrice = 0.0D;
				final BigDecimal percentageDiscount = null;
				updateAppOrderEntriesForFreebieBogo(applicableOrderEntryList);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					totalApplicablePrice += entry.getTotalPrice() == null ? 0.0D : entry.getTotalPrice().doubleValue();
				}

				final List<DiscountValue> discountList = abstractOrderModel.getGlobalDiscountValues(); //Discount values against the cart
				final List<DiscountModel> voucherList = abstractOrderModel.getDiscounts(); //List of discounts against the cart
				double discountValue = 0.0;
				if (CollectionUtils.isNotEmpty(discountList) && CollectionUtils.isNotEmpty(voucherList))
				{
					for (final DiscountValue discount : discountList)
					{
						if (null != discount.getCode() && discount.getCode().equalsIgnoreCase(voucher.getCode()))
						{
							discountValue += discount.getValue();
						}

					}
				}

				final double formattedDiscVal = Math.round(discountValue * 100.0) / 100.0;

				LOG.debug("Step 19:::percentageDiscount is " + percentageDiscount);

				BigDecimal totalAmtDeductedOnItemLevel = BigDecimal.valueOf(0);

				for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
				{
					BigDecimal entryLevelApportionedPrice = null;
					double currNetAmtAftrAllDisc = 0.00D;

					final double entryTotalPrice = entry.getTotalPrice().doubleValue();
					final BigDecimal discountPriceValue = BigDecimal.valueOf(formattedDiscVal);

					if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
					{
						entryLevelApportionedPrice = getApportionedValueForSingleEntry(discountPriceValue, totalAmtDeductedOnItemLevel);
					}
					else
					{
						entryLevelApportionedPrice = getApportionedValueForEntry(discountPriceValue, entryTotalPrice,
								totalApplicablePrice);
						totalAmtDeductedOnItemLevel = totalAmtDeductedOnItemLevel.add(entryLevelApportionedPrice);
					}

					LOG.debug("Step 20:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

					entry.setCartCouponCode(null == voucherCode ? voucher.getCode() : voucherCode);
					entry.setCartCouponValue(Double.valueOf(entryLevelApportionedPrice.doubleValue()));

					if ((StringUtils.isNotEmpty(entry.getProductPromoCode())) || (StringUtils.isNotEmpty(entry.getCartPromoCode()))
							|| StringUtils.isNotEmpty(entry.getCouponCode()))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() == null ? 0.00D
								: entry.getNetAmountAfterAllDisc().doubleValue();
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(netAmtAftrAllDisc, entryLevelApportionedPrice);
					}
					else
					{
						currNetAmtAftrAllDisc = getCurrNetAmtAftrAllDisc(entryTotalPrice, entryLevelApportionedPrice);
					}

					LOG.debug("Step 21:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);

					entry.setNetAmountAfterAllDisc(Double.valueOf(currNetAmtAftrAllDisc));
				}

				getModelService().saveAll(applicableOrderEntryList);

				final Double totalPrice = setTotalPrice(abstractOrderModel);
				abstractOrderModel.setTotalPrice(totalPrice);
				getModelService().save(abstractOrderModel);
			}

			final long endTime = System.currentTimeMillis();
			LOG.debug("Exiting service setApportionedValueForVoucher====== " + (endTime - startTime));
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}


	}


	/**
	 * The method is used to release any cart Offer coupons
	 *
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 */
	@Override
	public void releaseCartVoucher(final String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException
	{
		try
		{
			LOG.debug("Step 2:::Inside releaseVoucher");
			validateVoucherCodeParameter(voucherCode);
			final VoucherModel voucher = getVoucherService().getVoucher(voucherCode); //Finds voucher for the selected code
			if (voucher == null)
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
			}
			else if (cartModel != null)
			{
				//For cart
				LOG.debug("Step 3:::Voucher and cart is not null");

				getVoucherService().releaseVoucher(voucherCode, cartModel); //Releases the voucher from the cart
				LOG.debug("Step 4:::Voucher released");
				final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(voucher, cartModel);//new ArrayList<AbstractOrderEntryModel>();
				for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
				{
					entry.setCartCouponCode("");
					entry.setCartCouponValue(Double.valueOf(0.00D));
				}
				if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
				{
					getModelService().saveAll(entryList);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
			}
			else if (null != orderModel)
			{
				//For order
				LOG.debug("Step 3:::Voucher and cart is not null");
				//Added for INC144317090: For Mobile App, releaseCoupons API is getting called on order if user clicks on application "back" button from order confirmation page
				if (!OrderStatus.PAYMENT_SUCCESSFUL.equals(orderModel.getStatus())
						&& CollectionUtils.isEmpty(orderModel.getChildOrders()))
				{

					getVoucherService().releaseVoucher(voucherCode, orderModel); //Releases the voucher from the order
					LOG.debug("Step 4:::Voucher released");
					final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(voucher, orderModel);//new ArrayList<AbstractOrderEntryModel>();
					for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
					{
						entry.setCartCouponCode("");
						entry.setCartCouponValue(Double.valueOf(0.00D));
					}
					if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
					{
						getModelService().saveAll(entryList);
					}

					LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
				}
			}

		}
		catch (final JaloPriceFactoryException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ConsistencyCheckException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}

	}

	/**
	 * TPR-7448
	 *
	 * @param abstractOrderModel
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public Tuple3<?, ?, ?> checkCardPerOfferValidation(final AbstractOrderModel abstractOrderModel, final String token,
			final String cardSaved, final String cardRefNo, final String cardToken) throws Exception
	{
		Boolean response = Boolean.TRUE;
		boolean savedCardBoolean = false;
		//final String failureCode = CODE00;
		//final Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3 = null;
		boolean tokenRequired = false;
		if (StringUtils.isNotEmpty(cardSaved))
		{
			savedCardBoolean = Boolean.parseBoolean(cardSaved);
		}
		try
		{
			final CustomerModel user = (CustomerModel) abstractOrderModel.getUser();
			AddCardResponse addCardResponse = null;
			if (savedCardBoolean)//This will be called for new cards
			{
				if (StringUtils.isNotEmpty(token))
				{
					addCardResponse = mplPaymentService.getCurrentCardReferenceNo(token, user.getOriginalUid(), user.getUid());
				}
				else
				{//For new cards token is required in order to save the card and fetch its reference no.
					tokenRequired = true;
				}
			}
			else
			{//This will be called for saved cards
				addCardResponse = new AddCardResponse();
				addCardResponse.setCardReference(cardRefNo);
				addCardResponse.setCardToken(cardToken);
			}
			final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
					getVoucherService().getAppliedVouchers(abstractOrderModel));

			if (CollectionUtils.isNotEmpty(voucherList))
			{
				for (final DiscountModel discount : voucherList)
				{
					if (discount instanceof MplCartOfferVoucherModel)
					{
						tuple3 = promoVoucherForCardPerOfr(tokenRequired, abstractOrderModel, discount, addCardResponse, "C");
					}
					else if (discount instanceof PromotionVoucherModel)
					{
						tuple3 = promoVoucherForCardPerOfr(tokenRequired, abstractOrderModel, discount, addCardResponse, "P");
					}
					response = (Boolean) tuple3.getFirst();
					if (!response.booleanValue())
					{
						break;
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw e;
		}
		return tuple3;
	}

	/**
	 * TPR-7448
	 *
	 * Failure Code: 00:No failure 01:Failed at max avail count 02:Failed at max amount per time limit 03:Failed at max
	 * amount per time limit, But will get offer for lesser amount
	 */
	private Tuple3<?, ?, ?> promoVoucherForCardPerOfr(final boolean tokenRequired, final AbstractOrderModel abstractOrderModel,
			final DiscountModel discount, final AddCardResponse addCardResponse, final String flag) throws CalculationException
	{
		Boolean response = Boolean.TRUE;
		String failureCode = flag + CODE00;
		Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3Resp = null;
		Tuple3<?, ?, ?> tuple3 = null;
		final VoucherModel promotionVoucherModel = (VoucherModel) discount;
		for (final RestrictionModel restrictionModel : ((VoucherModel) discount).getRestrictions())
		{
			if (restrictionModel instanceof PaymentModeRestrictionModel)
			{
				List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel = null;
				final int maxAvailCount = ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount() != null
						? ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount().intValue() : 0;
				final double maxAmountPerTimeLimit = ((PaymentModeRestrictionModel) restrictionModel)
						.getMaxAmountPerTimeLimit() != null
								? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountPerTimeLimit().doubleValue() : 0.0D;
				final double maxAmountAllTransactions = ((PaymentModeRestrictionModel) restrictionModel)
						.getMaxAmountAllTransaction() != null
								? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountAllTransaction().doubleValue() : 0.0D;
				final TimeLimitTypeEnum timeLimitTypeEnum = ((PaymentModeRestrictionModel) restrictionModel).getTimeLimit();
				if (tokenRequired && (maxAvailCount > 0 || maxAmountPerTimeLimit > 0.0 || maxAmountAllTransactions > 0.0))
				{
					LOG.error("Token is required inorder to proceed,cannot be empty");
					throw new EtailNonBusinessExceptions(new Exception("Token cannot be empty"));
				}
				if (maxAvailCount > 0 && maxAmountPerTimeLimit == 0.0D && maxAmountAllTransactions == 0.0D)
				{
					voucherInvalidationModel = mplVoucherDao.findCardPerOfferInvalidation(promotionVoucherModel,
							addCardResponse.getCardReference());
					response = checkCardPerOfferMaxCountValidation(voucherInvalidationModel, maxAvailCount);
					if (!response.booleanValue())
					{
						failureCode = flag + CODE01;
					}
				}
				else if (maxAvailCount == 0 && maxAmountPerTimeLimit > 0.0D && maxAmountAllTransactions == 0.0D)
				{
					voucherInvalidationModel = getCardPerOfrInvalidationModel(promotionVoucherModel, timeLimitTypeEnum,
							addCardResponse.getCardReference());
					tuple3Resp = checkCardPerOfferMaxAmtValidation(voucherInvalidationModel, maxAmountPerTimeLimit,
							getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					response = (Boolean) tuple3Resp.getFirst();
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				else if (maxAvailCount > 0 && maxAmountPerTimeLimit > 0.0D && maxAmountAllTransactions == 0.0D)
				{
					voucherInvalidationModel = getCardPerOfrInvalidationModel(promotionVoucherModel, timeLimitTypeEnum,
							addCardResponse.getCardReference());
					tuple3Resp = checkCardPerOfferMaxCntAmtValidation(voucherInvalidationModel, maxAvailCount, maxAmountPerTimeLimit,
							getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					response = (Boolean) tuple3Resp.getFirst();
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				else if (maxAvailCount > 0 && maxAmountPerTimeLimit == 0.0D && maxAmountAllTransactions > 0.0D)
				{
					voucherInvalidationModel = mplVoucherDao.findCardPerOfferInvalidation(promotionVoucherModel,
							addCardResponse.getCardReference());
					tuple3Resp = checkCardPerOfferMaxCntAmtValidation(voucherInvalidationModel, maxAvailCount,
							maxAmountAllTransactions, getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				else if (maxAvailCount == 0 && maxAmountPerTimeLimit > 0.0D && maxAmountAllTransactions > 0.0D)
				{
					voucherInvalidationModel = getCardPerOfrInvalidationModel(promotionVoucherModel, timeLimitTypeEnum,
							addCardResponse.getCardReference());

					tuple3Resp = checkCPOMaxAmtLmtAllTrnxValidation(voucherInvalidationModel, maxAmountPerTimeLimit,
							maxAmountAllTransactions, getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					response = (Boolean) tuple3Resp.getFirst();
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				else if (maxAvailCount > 0 && maxAmountPerTimeLimit > 0.0D && maxAmountAllTransactions > 0.0D)
				{
					voucherInvalidationModel = getCardPerOfrInvalidationModel(promotionVoucherModel, timeLimitTypeEnum,
							addCardResponse.getCardReference());
					tuple3Resp = checkCPOMaxCntAmtLmtAllTrnxValidation(voucherInvalidationModel, maxAvailCount, maxAmountPerTimeLimit,
							maxAmountAllTransactions, getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					response = (Boolean) tuple3Resp.getFirst();
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				else if (maxAvailCount == 0 && maxAmountPerTimeLimit == 0.0D && maxAmountAllTransactions > 0.0D)
				{
					voucherInvalidationModel = mplVoucherDao.findCardPerOfferInvalidation(promotionVoucherModel,
							addCardResponse.getCardReference());
					tuple3Resp = checkCardPerOfferMaxAmtValidation(voucherInvalidationModel, maxAmountPerTimeLimit,
							getVoucherDiscountValue(abstractOrderModel, promotionVoucherModel));
					response = (Boolean) tuple3Resp.getFirst();
					if (!response.booleanValue())
					{
						failureCode = flag + (String) tuple3Resp.getSecond();
						priceDiff = (Double) tuple3Resp.getThird();
					}
				}
				if (maxAvailCount > 0 || maxAmountPerTimeLimit > 0.0 || maxAmountAllTransactions > 0.0)
				{
					if (response.booleanValue())
					{
						//Save card ref no in db
						saveCardRefNoInDb(addCardResponse, abstractOrderModel);
					}
					if (priceDiff.doubleValue() > 0.0)
					{
						//Updating model with new discount price
						updateVoucherPriceAbstractOrderModel(abstractOrderModel, promotionVoucherModel, priceDiff);
					}
				}
			}
		}
		tuple3 = new Tuple3<Boolean, String, Double>(response, failureCode, priceDiff);
		return tuple3;
	}

	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @param maxAvailCount
	 * @param maxAmountPerTimeLimit
	 * @param maxAmountAllTransactions
	 * @param currentDiscountPrice
	 * @return tuple3
	 */
	private Tuple3<?, ?, ?> checkCPOMaxCntAmtLmtAllTrnxValidation(
			final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel, final int maxAvailCount,
			final double maxAmountPerTimeLimit, final double maxAmountAllTransactions, final double currentDiscountPrice)
	{
		Boolean check = Boolean.FALSE;
		final Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3 = null;
		String failureCode = CODE01;//If default value of check i.e false is returned failure code will be 01=maxAvailCount<=voucherInvalidationModel.size()
		if (CollectionUtils.isNotEmpty(voucherInvalidationModel) && maxAvailCount > voucherInvalidationModel.size())
		{
			tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountAllTransactions, currentDiscountPrice);
			if (((Boolean) tuple3.getFirst()).booleanValue())
			{
				tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountPerTimeLimit, currentDiscountPrice);
				return tuple3;
			}
			else
			{
				return tuple3;
			}
		}
		else if (CollectionUtils.isEmpty(voucherInvalidationModel))
		{
			check = Boolean.TRUE;//As invalidation model is empty
			failureCode = CODE00;//00 is success scenario
		}
		tuple3 = new Tuple3<Boolean, String, Double>(check, failureCode, priceDiff);
		return tuple3;
	}


	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @param maxAmountPerTimeLimit
	 * @param maxAmountAllTransactions
	 * @param currentDiscountPrice
	 * @return tuple3
	 */
	private Tuple3<?, ?, ?> checkCPOMaxAmtLmtAllTrnxValidation(
			final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel, final double maxAmountPerTimeLimit,
			final double maxAmountAllTransactions, final double currentDiscountPrice)
	{
		Boolean check = Boolean.FALSE;
		final Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3 = null;
		String failureCode = CODE01;//If default value of check i.e false is returned failure code will be 01=maxAvailCount<=voucherInvalidationModel.size()
		if (CollectionUtils.isNotEmpty(voucherInvalidationModel))
		{
			tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountAllTransactions, currentDiscountPrice);
			if (((Boolean) tuple3.getFirst()).booleanValue())
			{
				tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountPerTimeLimit, currentDiscountPrice);
				return tuple3;
			}
			else
			{
				return tuple3;
			}
		}
		else
		{
			check = Boolean.TRUE;//As invalidation model is empty
			failureCode = "00";//00 is for validation success scenario
		}
		tuple3 = new Tuple3<Boolean, String, Double>(check, failureCode, priceDiff);
		return tuple3;
	}


	private List<VoucherCardPerOfferInvalidationModel> getCardPerOfrInvalidationModel(final VoucherModel promotionVoucherModel,
			final TimeLimitTypeEnum timeLimitTypeEnum, final String cardReference)
	{
		List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel = null;
		if (null == timeLimitTypeEnum)
		{
			voucherInvalidationModel = mplVoucherDao.findInvalidationMaxAmtPMnth(promotionVoucherModel, cardReference);
		}
		else if (timeLimitTypeEnum.getCode().equalsIgnoreCase("day"))
		{
			voucherInvalidationModel = mplVoucherDao.findInvalidationMaxAmtPDay(promotionVoucherModel, cardReference);
		}
		else if (timeLimitTypeEnum.getCode().equalsIgnoreCase("month"))
		{
			voucherInvalidationModel = mplVoucherDao.findInvalidationMaxAmtPMnth(promotionVoucherModel, cardReference);
		}
		else if (timeLimitTypeEnum.getCode().equalsIgnoreCase("week"))
		{
			voucherInvalidationModel = mplVoucherDao.findInvalidationMaxAmtPWeek(promotionVoucherModel, cardReference);
		}
		else if (timeLimitTypeEnum.getCode().equalsIgnoreCase("year"))
		{
			voucherInvalidationModel = mplVoucherDao.findInvalidationMaxAmtPYear(promotionVoucherModel, cardReference);
		}
		return voucherInvalidationModel;
	}

	/*
	 * TPR-7448 (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService#getVoucherDiscountValue(de.hybris.platform.
	 * core.model.order.AbstractOrderModel, de.hybris.platform.voucher.model.VoucherModel)
	 */
	@Override
	public double getVoucherDiscountValue(final AbstractOrderModel abstractOrderModel, final VoucherModel voucherModel)
	{
		final List<DiscountValue> discountList = abstractOrderModel.getGlobalDiscountValues();

		final Iterator iter = discountList.iterator();
		while (iter.hasNext())
		{
			final DiscountValue discount = (DiscountValue) iter.next();
			if (discount.getCode().equalsIgnoreCase(voucherModel.getCode()))
			{
				return discount.getValue();
			}
		}
		return 0.0;
	}

	/**
	 * TPR-7448
	 *
	 * @param abstractOrderModel
	 * @param priceDiff
	 * @throws CalculationException
	 */
	private void updateVoucherPriceAbstractOrderModel(final AbstractOrderModel abstractOrderModel,
			final VoucherModel promotionVoucherModel, final Double priceDiff) throws CalculationException
	{
		double totalDiscountOld = 0.0, totalPrice = 0.0, totalDiscountNew = 0.0;
		List<DiscountValue> discountList = abstractOrderModel.getGlobalDiscountValues(); //Discount values against the cart
		//final List<DiscountModel> voucherList = abstractOrderModel.getDiscounts(); //List of discounts against the cart
		for (final DiscountValue discountValue : discountList)
		{
			totalDiscountOld += discountValue.getValue();
		}
		discountList = setGlobalDiscount(discountList, promotionVoucherModel, priceDiff.doubleValue());
		abstractOrderModel.setGlobalDiscountValues(discountList);
		//getMplDefaultCalculationService().calculateTotals(abstractOrderModel, false);


		for (final DiscountValue discountValue : discountList)
		{
			totalDiscountNew += discountValue.getValue();
		}
		totalPrice = (abstractOrderModel.getTotalPrice().doubleValue() + totalDiscountOld) - totalDiscountNew;
		abstractOrderModel.setTotalPrice(Double.valueOf(totalPrice));
		getModelService().save(abstractOrderModel);

	}

	/**
	 * TPR-7448
	 *
	 * @param addCardResponse
	 * @param abstractOrderModel
	 */
	private void saveCardRefNoInDb(final AddCardResponse addCardResponse, final AbstractOrderModel abstractOrderModel)
	{
		final UserModel user = abstractOrderModel.getUser();
		if (StringUtils.isNotEmpty(addCardResponse.getCardReference()) && null != user && StringUtils.isNotEmpty(user.getUid()))
		{
			final JuspayCardStatusModel juspayCardStatusModel = mplPaymentService.getJuspayCardStatusForCustomer(user.getUid(),
					abstractOrderModel.getGuid());
			juspayCardStatusModel.setCard_token(addCardResponse.getCardToken());
			juspayCardStatusModel.setCard_reference(addCardResponse.getCardReference());
			juspayCardStatusModel.setCard_fingerprint(addCardResponse.getCardFingerprint());
			juspayCardStatusModel.setCustomerId(user.getUid());
			juspayCardStatusModel.setGuid(abstractOrderModel.getGuid());
			modelService.save(juspayCardStatusModel);
		}
	}

	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @param maxAvailCount
	 * @param maxAmountPerMonth
	 * @return boolean
	 */
	private Tuple3<?, ?, ?> checkCardPerOfferMaxCntAmtValidation(
			final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel, final int maxAvailCount,
			final double maxAmountPerMonth, final double currentDiscountPrice)
	{
		Boolean check = Boolean.FALSE;
		final Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3 = null;
		String failureCode = CODE01;//If default value of check i.e false is returned failure code will be 01=maxAvailCount<=voucherInvalidationModel.size()
		if (CollectionUtils.isNotEmpty(voucherInvalidationModel) && maxAvailCount > voucherInvalidationModel.size())
		{
			tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountPerMonth, currentDiscountPrice);
			return tuple3;
		}
		else if (CollectionUtils.isEmpty(voucherInvalidationModel))
		{
			check = Boolean.TRUE;//As invalidation model is empty
			failureCode = CODE00;//00 is success scenario
		}
		tuple3 = new Tuple3<Boolean, String, Double>(check, failureCode, priceDiff);
		return tuple3;
	}


	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @param maxAmountPerMonth
	 * @return boolean
	 */
	private Tuple3<?, ?, ?> checkCardPerOfferMaxAmtValidation(
			final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel, final double maxAmountPerMonth,
			final double currentDiscountPrice)
	{
		Boolean check = Boolean.FALSE;
		final Double priceDiff = new Double(0.0);
		Tuple3<?, ?, ?> tuple3 = null;
		String failureCode = CODE01;//If default value of check i.e false is returned failure code will be 01=maxAvailCount<=voucherInvalidationModel.size()
		if (CollectionUtils.isNotEmpty(voucherInvalidationModel))
		{
			tuple3 = validateCardPerOfferDiscountPrice(voucherInvalidationModel, maxAmountPerMonth, currentDiscountPrice);
			return tuple3;
		}
		else
		{
			check = Boolean.TRUE;//As invalidation model is empty
			failureCode = CODE00;//00 is for validation success scenario
		}
		tuple3 = new Tuple3<Boolean, String, Double>(check, failureCode, priceDiff);
		return tuple3;
	}

	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @param maxAvailCount
	 * @return boolean
	 */
	private Boolean checkCardPerOfferMaxCountValidation(final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel,
			final int maxAvailCount)
	{
		if (CollectionUtils.isNotEmpty(voucherInvalidationModel) && voucherInvalidationModel.size() < maxAvailCount)
		{
			return Boolean.TRUE;
		}
		else if (CollectionUtils.isEmpty(voucherInvalidationModel))
		{
			return Boolean.TRUE;//As invalidation model is empty
		}
		return Boolean.FALSE;
	}

	private Tuple3<?, ?, ?> validateCardPerOfferDiscountPrice(
			final List<VoucherCardPerOfferInvalidationModel> voucherInvalidationModel, final double maxAmount,
			final double currentDiscountPrice)
	{
		double discountAlreadyReceived = 0.0D;
		Boolean check = Boolean.FALSE;
		Double priceDiff = new Double(0.0);
		String failureCode = CODE01;
		for (final VoucherCardPerOfferInvalidationModel voucherInvalidation : voucherInvalidationModel)
		{
			discountAlreadyReceived += voucherInvalidation.getDiscount().doubleValue();
		}
		if (maxAmount >= (discountAlreadyReceived + currentDiscountPrice))
		{
			check = Boolean.TRUE;
			failureCode = CODE00;//00 is for validation success scenario
		}
		if ((discountAlreadyReceived < maxAmount) && ((discountAlreadyReceived + currentDiscountPrice) > maxAmount))
		{
			//priceDiff = Double.valueOf((discountAlreadyReceived + currentDiscountPrice) - maxAmountPerMonth);
			priceDiff = Double.valueOf(maxAmount - discountAlreadyReceived);
			//sessionService.setAttribute("cardPerOfferPriceDiff", priceDiff);
			check = Boolean.FALSE;
			failureCode = CODE03;
		}
		if ((discountAlreadyReceived >= maxAmount) && currentDiscountPrice > 0.0)
		{
			//sessionService.setAttribute("cardPerOfferPriceDiff", priceDiff);
			check = Boolean.FALSE;
			failureCode = CODE02;
		}
		return new Tuple3<Boolean, String, Double>(check, failureCode, priceDiff);
	}


	/**
	 * TPR-7448
	 *
	 * @param voucher
	 * @param cardReferenceNo
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findCardPerOfferInvalidation(final VoucherModel voucher,
			final String cardReferenceNo)
	{
		return mplVoucherDao.findCardPerOfferInvalidation(voucher, cardReferenceNo);
	}

	/**
	 * TPR-7448
	 *
	 * @param voucher
	 * @param cardReferenceNo
	 * @return List<VoucherCardPerOfferInvalidationModel>
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPMnth(final VoucherModel voucher,
			final String cardReferenceNo)
	{
		return mplVoucherDao.findInvalidationMaxAmtPMnth(voucher, cardReferenceNo);
	}

	/**
	 * TPR-7448
	 *
	 * @param guid
	 * @param customerId
	 * @return List<JuspayCardStatus>
	 */
	@Override
	public List<JuspayCardStatusModel> findJuspayCardStatus(final String guid, final String customerId)
	{
		return mplVoucherDao.findJuspayCardStatus(guid, customerId);
	}


	/**
	 * Re populate Discount Value
	 *
	 * @param oModel
	 * @param voucher
	 */
	@Override
	public AbstractOrderModel getUpdatedDiscountValues(final AbstractOrderModel oModel, final VoucherModel voucher)
	{
		getModelService().refresh(oModel);
		final List<DiscountValue> globalDiscountList = new ArrayList<>(oModel.getGlobalDiscountValues());
		DiscountValue discountValue = null;
		final double discountAmt = getDiscountAmount(oModel.getEntries(), voucher);



		if (CollectionUtils.isNotEmpty(globalDiscountList))
		{
			final Iterator iter = globalDiscountList.iterator();

			while (iter.hasNext())
			{
				final DiscountValue discount = (DiscountValue) iter.next();
				if (discount.getCode().equalsIgnoreCase(voucher.getCode()))
				{
					discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discountAmt,
							discount.getCurrencyIsoCode());

					iter.remove();
					break;
				}
			}

			globalDiscountList.add(discountValue);

			oModel.setGlobalDiscountValues(globalDiscountList);
			getModelService().save(oModel);
			getModelService().refresh(oModel);

		}

		return oModel;
	}



	private double getDiscountAmount(final List<AbstractOrderEntryModel> entries, final VoucherModel voucher)
	{
		final double subtotal = getSubtotalForCoupon(entries);

		return getDiscountValue(voucher, subtotal);
	}




	private double getDiscountValue(final VoucherModel voucher, final double subtotal)
	{
		double discountAmt = 0;
		final Double voucherValue = voucher.getValue();

		if (null != voucher.getCurrency())
		{
			if (null != voucherValue && voucherValue.doubleValue() > 0)
			{
				discountAmt = voucherValue.doubleValue();
			}
		}
		else
		{
			if (null != voucherValue && voucherValue.doubleValue() > 0)
			{
				discountAmt = (voucherValue.doubleValue() * subtotal) / 100;
			}
		}
		return discountAmt;
	}


	private double getSubtotalForCoupon(final List<AbstractOrderEntryModel> entries)
	{
		refreshOrderEntries(entries);
		double subtotal = 0;

		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final Double netAmountAfterAllDisc = (null == entry.getNetAmountAfterAllDisc() ? Double.valueOf(0)
						: entry.getNetAmountAfterAllDisc());

				subtotal += (netAmountAfterAllDisc.doubleValue() > 0) ? netAmountAfterAllDisc.doubleValue()
						: entry.getTotalPrice().doubleValue();
			}
		}
		return subtotal;
	}



	private void refreshOrderEntries(final List<AbstractOrderEntryModel> entries)
	{
		for (final AbstractOrderEntryModel entry : entries)
		{
			getModelService().refresh(entry);
		}

	}


	@Override
	public Double setTotalPrice(final AbstractOrderModel abstractOrderModel)
	{
		final List<DiscountValue> globalDiscountList = abstractOrderModel.getGlobalDiscountValues();
		double discount = 0;
		double subtotal = 0;

		final List<AbstractOrderEntryModel> entryList = new ArrayList<AbstractOrderEntryModel>(abstractOrderModel.getEntries());


		if (CollectionUtils.isNotEmpty(globalDiscountList))
		{
			for (final DiscountValue value : globalDiscountList)
			{
				discount += value.getAppliedValue();
			}
		}

		//		final double subtotal = (null != abstractOrderModel.getSubtotal() && abstractOrderModel.getSubtotal().doubleValue() > 0)
		//				? abstractOrderModel.getSubtotal().doubleValue() : 0;

		if (CollectionUtils.isNotEmpty(entryList))
		{
			for (final AbstractOrderEntryModel entry : entryList)
			{
				if (BooleanUtils.isFalse(entry.getGiveAway()) || BooleanUtils.isFalse(entry.getIsBOGOapplied()))
				{
					subtotal += (null != entry.getTotalPrice() && entry.getTotalPrice().doubleValue() > 0)
							? entry.getTotalPrice().doubleValue() : 0;
				}
			}
		}

		final double deliveryCost = (null != abstractOrderModel.getDeliveryCost()
				&& abstractOrderModel.getDeliveryCost().doubleValue() > 0) ? abstractOrderModel.getDeliveryCost().doubleValue() : 0;

		final double totalPriceData = (subtotal + deliveryCost) - discount;

		if (totalPriceData > 0)
		{
			return Double.valueOf(totalPriceData);
		}

		return abstractOrderModel.getTotalPriceWithConv();
	}



	@Override
	public AbstractOrderModel getUpdatedCartDiscountValues(final AbstractOrderModel oModel, final VoucherModel voucher)
	{
		getModelService().refresh(oModel);
		final List<DiscountValue> globalDiscountList = new ArrayList<>(oModel.getGlobalDiscountValues());

		final Tuple2<Boolean, String> cartCouponObj = isUserVoucherPresent(oModel.getDiscounts());

		//For User Coupon
		if (cartCouponObj.getFirst().booleanValue())
		{
			final String code = cartCouponObj.getSecond();
			final double couponDiscount = getCouponDiscount(oModel.getEntries());
			DiscountValue cartdiscountValue = null;

			if (CollectionUtils.isNotEmpty(globalDiscountList))
			{
				final Iterator iter = globalDiscountList.iterator();
				while (iter.hasNext())
				{
					final DiscountValue discount = (DiscountValue) iter.next();
					if (discount.getCode().equalsIgnoreCase(code))
					{
						cartdiscountValue = new DiscountValue(discount.getCode(), couponDiscount, true, couponDiscount,
								discount.getCurrencyIsoCode());

						iter.remove();
						break;
					}
				}

				globalDiscountList.add(cartdiscountValue);
			}

		}

		//For Cart Coupon
		final double discountAmt = getDiscountAmount(oModel.getEntries(), voucher);

		if (CollectionUtils.isNotEmpty(globalDiscountList))
		{
			DiscountValue discountValue = null;
			final Iterator iter = globalDiscountList.iterator();

			while (iter.hasNext())
			{
				final DiscountValue discount = (DiscountValue) iter.next();
				if (discount.getCode().equalsIgnoreCase(voucher.getCode()))
				{
					discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discountAmt,
							discount.getCurrencyIsoCode());

					iter.remove();
					break;
				}
			}

			globalDiscountList.add(discountValue);
		}

		oModel.setGlobalDiscountValues(globalDiscountList);
		getModelService().save(oModel);
		getModelService().refresh(oModel);

		return oModel;
	}




	private double getCouponDiscount(final List<AbstractOrderEntryModel> entries)
	{
		refreshOrderEntries(entries);
		double couponvalue = 0;
		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final double val = (null != entry.getCouponValue() && entry.getCouponValue().doubleValue() > 0)
						? entry.getCouponValue().doubleValue() : 0;
				couponvalue += val;
			}
		}
		return couponvalue;
	}

	private double getCartCouponDiscount(final List<AbstractOrderEntryModel> entries)
	{
		refreshOrderEntries(entries);
		double couponvalue = 0;
		if (CollectionUtils.isNotEmpty(entries))
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final double val = (null != entry.getCartCouponValue() && entry.getCartCouponValue().doubleValue() > 0)
						? entry.getCartCouponValue().doubleValue() : 0;
				couponvalue += val;
			}
		}
		return couponvalue;
	}


	@Override
	public Tuple2<Boolean, String> isUserVoucherPresent(final List<DiscountModel> discounts)
	{
		boolean flag = false;
		String couponCode = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(discounts))
		{
			for (final DiscountModel discount : discounts)
			{
				if ((discount instanceof PromotionVoucherModel) && !(discount instanceof MplCartOfferVoucherModel))
				{
					couponCode = discount.getCode();
					flag = true;
				}
			}
		}

		final Tuple2<Boolean, String> cartCouponObj = new Tuple2(Boolean.valueOf(flag), couponCode);

		return cartCouponObj;
	}



	@Override
	public AbstractOrderModel modifyDiscountValues(final AbstractOrderModel oModel, final VoucherModel second)
	{
		getModelService().refresh(oModel);
		final List<DiscountValue> globalDiscountList = new ArrayList<>(oModel.getGlobalDiscountValues());


		final String code = second.getCode();
		final double couponDiscount = getCouponDiscount(oModel.getEntries());
		DiscountValue cartdiscountValue = null;

		if (CollectionUtils.isNotEmpty(globalDiscountList))
		{
			final Iterator iter = globalDiscountList.iterator();
			while (iter.hasNext())
			{
				final DiscountValue discount = (DiscountValue) iter.next();
				if (discount.getCode().equalsIgnoreCase(code))
				{
					cartdiscountValue = new DiscountValue(discount.getCode(), couponDiscount, true, couponDiscount,
							discount.getCurrencyIsoCode());

					iter.remove();
					break;
				}
			}

			globalDiscountList.add(cartdiscountValue);
		}


		oModel.setGlobalDiscountValues(globalDiscountList);
		getModelService().save(oModel);
		getModelService().refresh(oModel);

		return oModel;
	}



	@Override
	public AbstractOrderModel modifyDiscountValues(final AbstractOrderModel oModel)
	{
		getModelService().refresh(oModel);
		final List<DiscountValue> globalDiscountList = new ArrayList<>(oModel.getGlobalDiscountValues());

		final List<DiscountModel> voucherList = new ArrayList<>(oModel.getDiscounts());

		if (CollectionUtils.isNotEmpty(voucherList))
		{
			for (final DiscountModel discount : voucherList)
			{
				if ((discount instanceof PromotionVoucherModel) && !(discount instanceof MplCartOfferVoucherModel))
				{

					final PromotionVoucherModel voucher = (PromotionVoucherModel) discount;
					final String code = voucher.getCode();
					final double couponDiscount = getCouponDiscount(oModel.getEntries());
					DiscountValue cartdiscountValue = null;

					if (CollectionUtils.isNotEmpty(globalDiscountList))
					{
						final Iterator iter = globalDiscountList.iterator();
						while (iter.hasNext())
						{
							final DiscountValue discountVal = (DiscountValue) iter.next();
							if (discountVal.getCode().equalsIgnoreCase(code))
							{
								cartdiscountValue = new DiscountValue(discountVal.getCode(), couponDiscount, true, couponDiscount,
										discountVal.getCurrencyIsoCode());

								iter.remove();
								break;
							}
						}

						globalDiscountList.add(cartdiscountValue);
					}


				}
				else if (discount instanceof MplCartOfferVoucherModel)
				{

					final PromotionVoucherModel voucher = (PromotionVoucherModel) discount;
					final String code = voucher.getCode();
					final double couponDiscount = getCartCouponDiscount(oModel.getEntries());
					DiscountValue cartdiscountValue = null;

					if (CollectionUtils.isNotEmpty(globalDiscountList))
					{
						final Iterator iter = globalDiscountList.iterator();
						while (iter.hasNext())
						{
							final DiscountValue discountVal = (DiscountValue) iter.next();
							if (discountVal.getCode().equalsIgnoreCase(code))
							{
								cartdiscountValue = new DiscountValue(discountVal.getCode(), couponDiscount, true, couponDiscount,
										discountVal.getCurrencyIsoCode());

								iter.remove();
								break;
							}
						}

						globalDiscountList.add(cartdiscountValue);
					}


				}
			}

		}

		oModel.setGlobalDiscountValues(globalDiscountList);
		getModelService().save(oModel);
		getModelService().refresh(oModel);

		return oModel;
	}

	/**
	 * TPR-7448 Starts here Checking or card per offer restriction
	 *
	 * @param orderModel
	 */
	@Override
	public void cardPerOfferVoucherEntry(final OrderModel orderModel)
	{
		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
				getVoucherService().getAppliedVouchers(orderModel));
		VoucherCardPerOfferInvalidationModel voucherInvalidationModel = null;
		if (CollectionUtils.isNotEmpty(voucherList))
		{
			try
			{
				final UserModel userModel = orderModel.getUser();
				final List<JuspayCardStatusModel> cardList = findJuspayCardStatus(orderModel.getGuid(), userModel.getUid());
				//final DiscountModel discount = voucherList.get(0);
				for (final DiscountModel discount : voucherList)
				{
					if (discount instanceof PromotionVoucherModel || discount instanceof MplCartOfferVoucherModel)
					{
						String cardReferenceNo = "";
						if (CollectionUtils.isNotEmpty(cardList))
						{
							cardReferenceNo = cardList.get(0).getCard_reference();
							LOG.debug("cardReferenceNo=" + cardReferenceNo);
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							for (final RestrictionModel restrictionModel : ((PromotionVoucherModel) discount).getRestrictions())
							{
								if (restrictionModel instanceof PaymentModeRestrictionModel)
								{
									final int maxAvailCount = ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount() != null
											? ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount().intValue() : 0;
									final double maxAmountPerTimeLimit = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountPerTimeLimit() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountPerTimeLimit().doubleValue()
													: 0.0D;
									final double maxAmountAllTransactions = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountAllTransaction() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountAllTransaction()
															.doubleValue()
													: 0.0D;
									if (maxAvailCount > 0 || maxAmountPerTimeLimit > 0.0 || maxAmountAllTransactions > 0.0)
									{
										voucherInvalidationModel = modelService.create(VoucherCardPerOfferInvalidationModel.class);
										LOG.error(null != discount.getCode() ? discount.getCode() : "Discount Code is null");
										if (StringUtils.isNotEmpty(discount.getCode()))
										{
											final double discountValue = getVoucherDiscountValue(orderModel, promotionVoucherModel);
											voucherInvalidationModel.setVoucher(promotionVoucherModel);
											voucherInvalidationModel.setGuid(orderModel.getGuid());
											voucherInvalidationModel.setCardRefNo(cardReferenceNo);
											voucherInvalidationModel.setDiscount(Double.valueOf(discountValue));
											getModelService().save(voucherInvalidationModel);
										}
									}
								}

							}
						}
					}
				}
			}
			catch (final Exception e)
			{
				discountUtility.releaseVoucherAndInvalidation(orderModel);
				LOG.error("Error in cardPerOfferVoucherExists=", e);
				throw e;
			}

		}
	}

	/**
	 * TPR-7448 Starts here:Update/Create new invalidation entry for when order model is already created before payment.
	 *
	 * @param orderModel
	 */
	@Override
	public void updateCardPerOfferVoucherEntry(final OrderModel orderModel)
	{
		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
				getVoucherService().getAppliedVouchers(orderModel));
		VoucherCardPerOfferInvalidationModel voucherInvalidationModel = null;
		if (CollectionUtils.isNotEmpty(voucherList))
		{
			try
			{
				final UserModel userModel = orderModel.getUser();
				final List<JuspayCardStatusModel> cardList = findJuspayCardStatus(orderModel.getGuid(), userModel.getUid());
				voucherInvalidationModel = modelService.create(VoucherCardPerOfferInvalidationModel.class);
				voucherInvalidationModel.setGuid(orderModel.getGuid());
				try
				{
					modelService.removeAll(flexibleSearchService.getModelsByExample(voucherInvalidationModel));
				}
				catch (final ModelNotFoundException e)
				{
					LOG.debug("No VoucherCardPerOfferInvalidationModel Object found to remove");
				}
				//final DiscountModel discount = voucherList.get(0);
				for (final DiscountModel discount : voucherList)

				{
					if (discount instanceof PromotionVoucherModel || discount instanceof MplCartOfferVoucherModel)
					{
						String cardReferenceNo = "";
						if (CollectionUtils.isNotEmpty(cardList))
						{
							cardReferenceNo = cardList.get(0).getCard_reference();
							LOG.debug("cardReferenceNo=" + cardReferenceNo);
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							for (final RestrictionModel restrictionModel : ((PromotionVoucherModel) discount).getRestrictions())
							{
								if (restrictionModel instanceof PaymentModeRestrictionModel)
								{
									final int maxAvailCount = ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount() != null
											? ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount().intValue() : 0;
									final double maxAmountPerTimeLimit = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountPerTimeLimit() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountPerTimeLimit().doubleValue()
													: 0.0D;
									final double maxAmountAllTransactions = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountAllTransaction() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountAllTransaction()
															.doubleValue()
													: 0.0D;
									if (maxAvailCount > 0 || maxAmountPerTimeLimit > 0.0 || maxAmountAllTransactions > 0.0)
									{
										voucherInvalidationModel = modelService.create(VoucherCardPerOfferInvalidationModel.class);
										LOG.error(null != discount.getCode() ? discount.getCode() : "Discount Code is null");
										if (StringUtils.isNotEmpty(discount.getCode()))
										{
											final double discountValue = getVoucherDiscountValue(orderModel, promotionVoucherModel);
											voucherInvalidationModel.setVoucher(promotionVoucherModel);
											voucherInvalidationModel.setGuid(orderModel.getGuid());
											voucherInvalidationModel.setCardRefNo(cardReferenceNo);
											voucherInvalidationModel.setDiscount(Double.valueOf(discountValue));
											getModelService().save(voucherInvalidationModel);
										}
									}
								}

							}
						}
					}
				}
			}
			catch (final Exception e)
			{
				discountUtility.releaseVoucherAndInvalidation(orderModel);
				LOG.error("Error in cardPerOfferVoucherExists=", e);
				throw e;
			}

		}
	}

	/**
	 * TPR-7448 -- To be called from MplProcessOrderServiceImpl, Cannot use removeCPOVoucherInvalidation as
	 * removeCPOVoucherInvalidation will not work if voucher is already released
	 *
	 * @param abstractOrderModel
	 */
	@Override
	public void removeCardPerOfferVoucherInvalidation(final AbstractOrderModel abstractOrderModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Inside removeCardPerOfferVoucherInvalidation");
		}
		VoucherCardPerOfferInvalidationModel voucherInvalidationModel = null;
		try
		{
			final UserModel userModel = abstractOrderModel.getUser();
			final List<JuspayCardStatusModel> cardList = findJuspayCardStatus(abstractOrderModel.getGuid(), userModel.getUid());
			//final DiscountModel discount = voucherList.get(0);

			String cardReferenceNo = "";
			if (CollectionUtils.isNotEmpty(cardList))
			{
				cardReferenceNo = cardList.get(0).getCard_reference();
				LOG.debug("cardReferenceNo=" + cardReferenceNo);

				voucherInvalidationModel = modelService.create(VoucherCardPerOfferInvalidationModel.class);

				voucherInvalidationModel.setGuid(abstractOrderModel.getGuid());
				voucherInvalidationModel.setCardRefNo(cardReferenceNo);
				removeCardPerOfferInvalidation(voucherInvalidationModel);

			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in cardPerOfferVoucherExists=", e);
			throw e;
		}
	}

	/**
	 * TPR-7448
	 *
	 * @param voucherInvalidationModel
	 * @return boolean
	 */
	private boolean removeCardPerOfferInvalidation(final VoucherCardPerOfferInvalidationModel voucherInvalidationModel)
	{
		boolean success = true;
		try
		{

			final List<VoucherCardPerOfferInvalidationModel> voucherModel = flexibleSearchService
					.getModelsByExample(voucherInvalidationModel);
			modelService.removeAll(voucherModel);
		}
		catch (final ModelNotFoundException e)
		{
			success = false;
			LOG.error("VoucherCardPerOfferInvalidationModel not found for=>VoucherCode=" + ",Guid="
					+ voucherInvalidationModel.getGuid() + ",CardReferenceNo=" + voucherInvalidationModel.getCardRefNo());
		}
		return success;
	}

	/**
	 * TPR-7448 --To be used while cancellation
	 *
	 * @param orderModel
	 */
	@Override
	public void removeCPOVoucherInvalidation(final OrderModel orderModel)
	{
		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
				getVoucherService().getAppliedVouchers(orderModel));
		VoucherCardPerOfferInvalidationModel voucherInvalidationModel = null;
		if (CollectionUtils.isNotEmpty(voucherList))
		{
			try
			{
				final UserModel userModel = orderModel.getUser();
				final List<JuspayCardStatusModel> cardList = findJuspayCardStatus(orderModel.getGuid(), userModel.getUid());
				//final DiscountModel discount = voucherList.get(0);
				for (final DiscountModel discount : voucherList)

				{
					if (discount instanceof PromotionVoucherModel || discount instanceof MplCartOfferVoucherModel)
					{
						String cardReferenceNo = "";
						if (CollectionUtils.isNotEmpty(cardList))
						{
							cardReferenceNo = cardList.get(0).getCard_reference();
							LOG.debug("cardReferenceNo=" + cardReferenceNo);
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							for (final RestrictionModel restrictionModel : ((PromotionVoucherModel) discount).getRestrictions())
							{
								if (restrictionModel instanceof PaymentModeRestrictionModel)
								{
									final int maxAvailCount = ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount() != null
											? ((PaymentModeRestrictionModel) restrictionModel).getMaxAvailCount().intValue() : 0;
									final double maxAmountPerTimeLimit = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountPerTimeLimit() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountPerTimeLimit().doubleValue()
													: 0.0D;
									final double maxAmountAllTransactions = ((PaymentModeRestrictionModel) restrictionModel)
											.getMaxAmountAllTransaction() != null
													? ((PaymentModeRestrictionModel) restrictionModel).getMaxAmountAllTransaction()
															.doubleValue()
													: 0.0D;
									if (maxAvailCount > 0 || maxAmountPerTimeLimit > 0.0 || maxAmountAllTransactions > 0.0)
									{
										voucherInvalidationModel = modelService.create(VoucherCardPerOfferInvalidationModel.class);
										LOG.error(null != discount.getCode() ? discount.getCode() : "Discount Code is null");
										if (StringUtils.isNotEmpty(discount.getCode()))
										{
											voucherInvalidationModel.setVoucher(promotionVoucherModel);
											voucherInvalidationModel.setGuid(orderModel.getGuid());
											voucherInvalidationModel.setCardRefNo(cardReferenceNo);
											removeCardPerOfferInvalidation(voucherInvalidationModel);
										}
									}
								}

							}
						}
					}
				}
			}
			catch (final Exception e)
			{
				discountUtility.releaseVoucherAndInvalidation(orderModel);
				LOG.error("Error in cardPerOfferVoucherExists=", e);
				throw e;
			}

		}
	}

	/**
	 * Populate Discount Data when returned back to Payment Page for 3D Secure
	 *
	 * @param abstractOrderModel
	 */
	@Override
	public VoucherDiscountData getVoucherData(final AbstractOrderModel abstractOrderModel)
	{
		final VoucherDiscountData data = new VoucherDiscountData();
		boolean isUserCoupon = false;
		String couponCode = MarketplacecommerceservicesConstants.EMPTY;

		final List<DiscountModel> voucherList = new ArrayList<>(abstractOrderModel.getDiscounts());
		final List<DiscountValue> discountList = new ArrayList<>(abstractOrderModel.getGlobalDiscountValues());

		if (CollectionUtils.isNotEmpty(voucherList))
		{
			final Tuple2<Boolean, String> couponObj = isUserVoucherPresent(voucherList);
			isUserCoupon = couponObj.getFirst().booleanValue();
			couponCode = couponObj.getSecond();
		}

		if (isUserCoupon)
		{
			data.setVoucherCode(couponCode);
			for (final DiscountValue discountVal : discountList)
			{
				if (discountVal.getCode().equalsIgnoreCase(couponCode))
				{
					data.setCouponDiscount(
							getDiscountUtility().createPrice(abstractOrderModel, Double.valueOf(discountVal.getAppliedValue())));
					data.setCouponRedeemed(true);
				}
			}
		}


		return data;
	}


}
