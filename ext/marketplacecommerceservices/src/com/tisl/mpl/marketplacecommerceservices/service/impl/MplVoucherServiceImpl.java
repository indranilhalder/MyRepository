/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;
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
				cartModel.setTotalPrice(Double.valueOf((null == cartModel.getTotalPrice() ? 0.0d : cartModel.getTotalPrice()
						.doubleValue()) + (null == deliveryCost ? 0.0d : deliveryCost.doubleValue()) - modDeliveryCost.doubleValue()));
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
				orderModel.setTotalPrice(Double.valueOf((null == orderModel.getTotalPrice() ? 0.0d : orderModel.getTotalPrice()
						.doubleValue()) + (null == deliveryCost ? 0.0d : deliveryCost.doubleValue()) - modDeliveryCost.doubleValue()));
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
				if (oModel.getCertainty().floatValue() == 1.0F
						&& null != oModel.getPromotion()
						&& (oModel.getPromotion() instanceof BuyAboveXGetPromotionOnShippingChargesModel
								|| oModel.getPromotion() instanceof BuyAGetPromotionOnShippingChargesModel || oModel.getPromotion() instanceof BuyAandBGetPromotionOnShippingChargesModel))
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
					discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
							.getMaxDiscountValue().doubleValue());
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
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry
									.getProductPromoCode())) || (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry
									.getCartPromoCode()))) ? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice()
									.doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug("Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseVoucherAfterCheck(cartModel, null, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0
								&& (netAmountAfterAllDisc - voucherCalcValue) > 0
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
					discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
							.getMaxDiscountValue().doubleValue());
					orderModel.setGlobalDiscountValues(discountList);
					getMplDefaultCalculationService().calculateTotals(orderModel, false);
					getModelService().save(orderModel);

					discountData.setCouponDiscount(getDiscountUtility().createPrice(orderModel,
							Double.valueOf(lastVoucher.getMaxDiscountValue().doubleValue())));
				}
				else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0) //When discount value is greater than cart totals after applying promotion
				{
					LOG.debug("Step 14:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
					discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, null, applicableOrderEntryList, voucherList);
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
							netAmountAfterAllDisc += ((null != entry.getProductPromoCode() && StringUtils.isNotEmpty(entry
									.getProductPromoCode())) || (null != entry.getCartPromoCode() && StringUtils.isNotEmpty(entry
									.getCartPromoCode()))) ? entry.getNetAmountAfterAllDisc().doubleValue() : entry.getTotalPrice()
									.doubleValue();

							productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
						}

						LOG.debug(logBuilder.append("Step 15:::netAmountAfterAllDisc is ").append(netAmountAfterAllDisc)
								.append(" & productPrice is ").append(productPrice));

						if ((productPrice < 1) || (voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)) //When discount value is greater than entry totals after applying promotion
						{
							LOG.debug("Step 16:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
							discountData = releaseVoucherAfterCheck(null, orderModel, voucherCode, Double.valueOf(productPrice),
									applicableOrderEntryList, voucherList);
						}
						else if (voucherCalcValue != 0
								&& (netAmountAfterAllDisc - voucherCalcValue) > 0
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
							discountData.setCouponDiscount(getDiscountUtility()
									.createPrice(orderModel, Double.valueOf(voucherCalcValue)));
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
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());

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
			final String voucherCode, final List<AbstractOrderEntryModel> applicableOrderEntryList)
			throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
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

					if ((StringUtils.isNotEmpty(entry.getProductPromoCode())) || (StringUtils.isNotEmpty(entry.getCartPromoCode())))
					{
						final double netAmtAftrAllDisc = entry.getNetAmountAfterAllDisc() == null ? 0.00D : entry
								.getNetAmountAfterAllDisc().doubleValue();
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
		return (amount > entryLevelApportionedPrice.doubleValue()) ? (amount - entryLevelApportionedPrice.doubleValue()) : Double
				.parseDouble(MarketplacecommerceservicesConstants.ZEROPOINTZEROONE);
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




}
