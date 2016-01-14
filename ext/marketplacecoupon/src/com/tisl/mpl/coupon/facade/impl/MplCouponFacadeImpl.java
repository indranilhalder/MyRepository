/**
 *
 */
package com.tisl.mpl.coupon.facade.impl;


import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercefacades.voucher.impl.DefaultVoucherFacade;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.util.VoucherEntry;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.AllVoucherListData;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.order.impl.MplDefaultCalculationService;


/**
 * @author TCS
 *
 */
public class MplCouponFacadeImpl implements MplCouponFacade
{
	private static final Logger LOG = Logger.getLogger(MplCouponFacadeImpl.class);

	@Autowired
	private VoucherService voucherService;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplCommerceCartCalculationStrategy calculationStrategy;
	@Autowired
	private VoucherFacade voucherFacade;
	@Autowired
	private MplCouponService mplCouponService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private VoucherModelService voucherModelService;
	@Autowired
	private CartService cartService;
	@Autowired
	private DefaultVoucherService defaultVoucherService;
	@Autowired
	private DefaultVoucherFacade defaultVoucherFacade;
	@Autowired
	private CommerceCartService commerceCartService;
	@Autowired
	private MplDefaultCalculationService mplDefaultCalculationService;



	// Month list


	private static final String JANUARY = "January";
	private static final String FEBRUARY = "February";
	private static final String MARCH = "March";
	private static final String APRIL = "April";
	private static final String MAY = "May";
	private static final String JUNE = "June";
	private static final String JULY = "July";
	private static final String AUGUST = "August";
	private static final String SEPTEMBER = "September";
	private static final String OCTOBER = "October";
	private static final String NOVEMBER = "November";
	private static final String DECEMBER = "December";
	// Month list

	public static final String SINGLE_SPACE = " ";
	public static final String PARENT = "parent";
	public static final String BOXING = "boxing";

	/**
	 * This method recalculates the cart after redeeming/releasing the voucher
	 *
	 * @param cartModel
	 *
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel) throws JaloPriceFactoryException, CalculationException
	{
		LOG.debug("Step 5:::Inside recalculating cart after voucher apply");
		final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
		final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

		if (cartModel != null && cartModel.getEntries() != null)
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{
				if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
						&& cartEntryModel.getSelectedUSSID() != null)
				{
					freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
					freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
				}
			}
		}

		//recalculating cart
		final Double deliveryCost = cartModel.getDeliveryCost();
		getCommerceCartService().recalculateCart(cartModel);
		cartModel.setDeliveryCost(deliveryCost);
		cartModel.setTotalPrice(Double.valueOf(cartModel.getTotalPrice().doubleValue() + deliveryCost.doubleValue()));

		// Freebie item changes
		getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

		LOG.debug("Step 6:::Recalculation done successfully");

		getModelService().save(cartModel);

	}




	@Override
	public VoucherDiscountData calculateValues(final CartModel cartModel, final boolean couponStatus,
			final boolean reddemIdentifier)
	{
		LOG.debug("Calculating discounts after applying/releasing coupons based on redeemIdentifier=" + reddemIdentifier);
		double totalDiscount = 0.0d;

		final List<VoucherData> voucherDataList = getVoucherFacade().getVouchersForCart();

		for (final VoucherData voucher : voucherDataList)
		{
			final List<DiscountValue> discount = cartModel.getGlobalDiscountValues();
			for (final DiscountValue dis : discount)
			{
				if (dis.getCode().equalsIgnoreCase(voucher.getCode()))
				{
					totalDiscount += dis.getAppliedValue();
				}
			}

		}

		LOG.debug("Total discount for voucher is :::: " + totalDiscount);

		final VoucherDiscountData data = new VoucherDiscountData();
		data.setVoucher(voucherDataList);
		data.setCouponDiscount(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(totalDiscount)));
		data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
		if (reddemIdentifier)
		{
			data.setCouponRedeemed(couponStatus);
		}
		else
		{
			data.setCouponReleased(couponStatus);
		}
		if (!couponStatus)
		{
			data.setRedeemErrorMsg("Coupon cannot be redeemed");
		}
		return data;
	}




	/**
	 * Returns all active vouchers
	 *
	 * @return ArrayList<VoucherModel>
	 *
	 */
	@Override
	public List<VoucherModel> getAllCoupons()
	{
		final List<VoucherModel> voucherList = new ArrayList<VoucherModel>();
		final List<VoucherModel> voucherColl = getMplCouponService().getVoucher();
		if (CollectionUtils.isNotEmpty(voucherColl))
		{
			voucherList.addAll(voucherColl);
		}
		return voucherList;
	}



	/**
	 *
	 * @param cart
	 * @param customer
	 * @param voucherList
	 * @return ArrayList<VoucherDisplayData>
	 *
	 */
	@Override
	public List<VoucherDisplayData> displayTopCoupons(final CartModel cart, final CustomerModel customer,
			final List<VoucherModel> voucherList)
	{
		List<VoucherDisplayData> voucherDataList = new ArrayList<VoucherDisplayData>();

		{
			for (final VoucherModel voucherModel : voucherList)
			{
				if (voucherModel instanceof PromotionVoucherModel
						&& checkVoucherCanBeRedeemed(voucherModel, ((PromotionVoucherModel) voucherModel).getVoucherCode(), cart))
				{
					voucherDataList = calculateVoucherDisplay(voucherModel, voucherDataList, cart);
				}
			}
		}
		voucherDataList = getMplCouponService().getSortedVoucher(voucherDataList);

		final int couponCount = Integer.parseInt(getConfigurationService().getConfiguration().getString("coupon.display.topCount",
				"5"));

		if (voucherDataList.size() > couponCount)

		{
			voucherDataList.subList(couponCount, voucherDataList.size()).clear();
		}
		return voucherDataList;
	}


	/**
	 *
	 * @param voucherModel
	 * @param voucherDataList
	 * @return ArrayList<VoucherDisplayData>
	 */
	private List<VoucherDisplayData> calculateVoucherDisplay(final VoucherModel voucherModel,
			final List<VoucherDisplayData> voucherDataList, final CartModel cartModel)
	{
		final VoucherEntrySet entrySet = getVoucherModelService().getApplicableEntries(voucherModel, cartModel);
		final List<AbstractOrderEntry> applicableOrderEntryList = getOrderEntriesFromVoucherEntries(entrySet);
		double totalPrice = 0.0D;

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			totalPrice += entry.getTotalPrice().doubleValue();
		}

		double voucherDiscount = 0.0;
		if (voucherModel.getAbsolute().booleanValue())
		{
			final VoucherDisplayData voucherData = new VoucherDisplayData();
			voucherData.setCouponDiscount(voucherModel.getValue().doubleValue());
			voucherData.setVoucherCode(((PromotionVoucherModel) voucherModel).getVoucherCode());
			voucherData.setVoucherDescription(voucherModel.getDescription());
			voucherDataList.add(voucherData);
		}
		else
		{
			final VoucherDisplayData voucherData = new VoucherDisplayData();

			voucherDiscount = (totalPrice * voucherModel.getValue().doubleValue()) / 100;
			voucherData.setCouponDiscount(voucherDiscount);
			voucherData.setVoucherCode(((PromotionVoucherModel) voucherModel).getVoucherCode());
			voucherData.setVoucherDescription(voucherModel.getDescription());
			voucherDataList.add(voucherData);
		}

		return voucherDataList;
	}





	/**
	 * Applies the voucher and returns true if successful
	 *
	 * @param voucherCode
	 * @param cartModel
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@Override
	public boolean applyVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException,
			CalculationException, NumberFormatException, JaloInvalidParameterException, JaloSecurityException
	{
		boolean checkFlag = false;
		if (CollectionUtils.isEmpty(cartModel.getDiscounts()))
		{
			LOG.debug("Step 1:::No voucher is applied to cart");

			validateVoucherCodeParameter(voucherCode);
			if (!isVoucherCodeValid(voucherCode))
			{
				throw new VoucherOperationException("Voucher not found: " + voucherCode);
			}
			LOG.debug("Step 2:::Voucher Code is valid");

			final VoucherModel voucher = getVoucherModel(voucherCode);
			if (voucher.getValue().doubleValue() <= 0)
			{
				throw new VoucherOperationException("Voucher not found: " + voucherCode);
			}
			LOG.debug("Step 3:::Voucher value is not negative");
			if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel))
			{
				LOG.debug("Step 3.1:::Voucher is not applicable");
				final boolean dateFlag = checkViolatedRestrictions(voucher, cartModel);
				if (dateFlag)
				{
					throw new VoucherOperationException("Voucher cannot be redeemed: " + voucherCode);
				}
				else
				{
					throw new VoucherOperationException("Voucher is not applicable: " + voucherCode);
				}
			}

			else if (!checkVoucherIsReservable(voucher, voucherCode, cartModel))
			{
				LOG.debug("Step 3.2:::Voucher is not reservable");
				throw new VoucherOperationException("Voucher is not reservable: " + voucherCode);
			}

			else
			{
				try
				{
					LOG.debug("Step 4:::Voucher can be redeemed");
					if (!getVoucherService().redeemVoucher(voucherCode, cartModel))
					{
						throw new VoucherOperationException("Error while applying voucher: " + voucherCode);
					}

					recalculateCartForCoupon(cartModel);

					//Important! Checking cart, if total amount <0, release this voucher
					checkCartAfterApply(voucher, cartModel);

					//apportioning
					setApportionedValueForVoucher(voucher, cartModel, voucherCode);
					checkFlag = true;
				}
				catch (final JaloPriceFactoryException e)
				{
					throw new VoucherOperationException("Error while applying voucher: " + voucherCode);
				}
				catch (final ModelSavingException e)
				{
					throw new VoucherOperationException("Error while saving voucher discount values");
				}
			}
		}
		return checkFlag;
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
	 * Checks whether the voucher code entered by the customer is valid or not
	 *
	 * @param voucherCode
	 * @return boolean
	 */
	protected boolean isVoucherCodeValid(final String voucherCode)
	{
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
		if (voucher == null)
		{
			return false;
		}
		return true;
	}


	/**
	 * Returns voucher model from the voucher code
	 *
	 * @param voucherCode
	 * @return VoucherModel
	 * @throws VoucherOperationException
	 */
	protected VoucherModel getVoucherModel(final String voucherCode) throws VoucherOperationException
	{
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
		if (voucher == null)
		{
			throw new VoucherOperationException("Voucher not found: " + voucherCode);
		}
		return voucher;
	}



	/**
	 * Checks whether voucher can be redeemed
	 *
	 * @param voucher
	 * @param voucherCode
	 * @return boolean
	 */
	protected boolean checkVoucherCanBeRedeemed(final VoucherModel voucher, final String voucherCode, final CartModel cartModel)
	{
		return getVoucherModelService().isApplicable(voucher, cartModel)
				&& getVoucherModelService().isReservable(voucher, voucherCode, cartModel);
	}


	/**
	 *
	 * @param voucher
	 * @param voucherCode
	 * @param cartModel
	 * @return boolean
	 */
	protected boolean checkVoucherIsApplicable(final VoucherModel voucher, final String voucherCode, final CartModel cartModel)
	{
		return getVoucherModelService().isApplicable(voucher, cartModel);
	}


	/**
	 *
	 * @param voucher
	 * @param voucherCode
	 * @param cartModel
	 * @return boolean
	 */
	protected boolean checkVoucherIsReservable(final VoucherModel voucher, final String voucherCode, final CartModel cartModel)
	{
		return getVoucherModelService().isReservable(voucher, voucherCode, cartModel);
	}



	/**
	 *
	 * @param voucher
	 * @param cartModel
	 * @return boolean
	 */
	protected boolean checkViolatedRestrictions(final VoucherModel voucher, final CartModel cartModel)
	{
		final List<RestrictionModel> getViolatedRestrictions = getVoucherModelService().getViolatedRestrictions(voucher, cartModel);
		boolean dateFlag = false;
		for (final RestrictionModel restriction : getViolatedRestrictions)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				LOG.debug("Date restriction is violated");
				dateFlag = true;
				break;
			}
		}
		return dateFlag;
	}



	/**
	 * Checks the cart after applying the voucher
	 *
	 * @param lastVoucher
	 * @param cartModel
	 * @throws ModelSavingException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws JaloPriceFactoryException
	 */
	protected void checkCartAfterApply(final VoucherModel lastVoucher, final CartModel cartModel) throws ModelSavingException,
			VoucherOperationException, CalculationException, NumberFormatException, JaloInvalidParameterException,
			JaloSecurityException, JaloPriceFactoryException
	{
		LOG.debug("Step 7:::Inside checking cart after applying voucher");
		//Total amount in cart updated with delay... Calculating value of voucher regarding to order
		final double cartSubTotal = cartModel.getSubtotal().doubleValue();
		double voucherCalcValue = 0.0;
		double promoCalcValue = 0.0;
		List<DiscountValue> discountList = cartModel.getGlobalDiscountValues();
		final String voucherCode = ((PromotionVoucherModel) lastVoucher).getVoucherCode();

		final List<DiscountModel> voucherList = cartModel.getDiscounts();
		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountValue discount : discountList)
			{
				if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
				{
					voucherCalcValue = discount.getValue();
				}
				else if (!discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
				{
					promoCalcValue = discount.getValue();
				}
			}
		}

		LOG.debug("Step 8:::Voucher discount in cart is " + voucherCalcValue + " & promo discount in cart is " + promoCalcValue);

		final VoucherEntrySet entrySet = getVoucherModelService().getApplicableEntries(lastVoucher, cartModel);
		final List<AbstractOrderEntry> applicableOrderEntryList = getOrderEntriesFromVoucherEntries(entrySet);

		if (!lastVoucher.getAbsolute().booleanValue() && voucherCalcValue != 0 && null != lastVoucher.getMaxDiscountValue()
				&& voucherCalcValue > lastVoucher.getMaxDiscountValue().doubleValue())
		{
			LOG.debug("Step 11:::Inside max discount block");
			discountList = setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher, lastVoucher
					.getMaxDiscountValue().doubleValue());
			cartModel.setGlobalDiscountValues(discountList);
			mplDefaultCalculationService.calculateTotals(cartModel, false);
			getModelService().save(cartModel);
		}

		else if (voucherCalcValue != 0 && (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0)
		{
			LOG.debug("Step 12:::Inside (cartSubTotal - promoCalcValue - voucherCalcValue) <= 0 block");
			releaseVoucher(voucherCode, cartModel);
			recalculateCartForCoupon(cartModel);
			//mplDefaultCalculationService.calculateTotals(cartModel, false);
			getModelService().save(cartModel);
			//Throw exception with specific information
			throw new VoucherOperationException("Voucher " + voucherCode + " cannot be redeemed: total price exceeded");
		}

		else
		{
			double netAmountAfterAllDisc = 0.0D;
			double productPrice = 0.0D;
			boolean flag = false;

			if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
			{
				LOG.debug("Step 13:::applicableOrderEntryList is not empty");
				for (final AbstractOrderEntry entry : applicableOrderEntryList)
				{
					if ((null != entry.getAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) && StringUtils
							.isNotEmpty(entry.getAttribute(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE).toString()))
							|| (null != entry.getAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE) && StringUtils
									.isNotEmpty(entry.getAttribute(MarketplacecommerceservicesConstants.CARTPROMOCODE).toString())))
					{
						netAmountAfterAllDisc += Double.parseDouble((entry
								.getAttribute(MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC)).toString());
						flag = true;
					}

					//					else
					//					{
					//						productPrice += entry.getTotalPrice().doubleValue();
					//					}

					productPrice += entry.getTotalPrice().doubleValue();
				}

				LOG.debug("Step 14:::netAmountAfterAllDisc is " + netAmountAfterAllDisc + " & productPrice is " + productPrice);


				if ((productPrice < 1) || (flag && voucherCalcValue != 0 && (netAmountAfterAllDisc - voucherCalcValue) <= 0)
						|| (!flag && voucherCalcValue != 0 && (productPrice - voucherCalcValue) <= 0))
				{
					LOG.debug("Step 15:::inside freebie and (netAmountAfterAllDisc - voucherCalcValue) <= 0 and (productPrice - voucherCalcValue) <= 0 block");
					releaseVoucher(voucherCode, cartModel);
					recalculateCartForCoupon(cartModel);
					//mplDefaultCalculationService.calculateTotals(cartModel, false);
					getModelService().save(cartModel);
					//Throw exception with specific information
					throw new VoucherOperationException("Voucher " + voucherCode + " cannot be redeemed: total price exceeded");
				}
			}
		}

	}


	/**
	 *
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
		for (final DiscountValue discount : discountList)
		{
			if (CollectionUtils.isNotEmpty(voucherList) && discount.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
			{
				discountValue = new DiscountValue(discount.getCode(), discountAmt, true, discount.getCurrencyIsoCode());

				discountList.remove(discount);
				break;
			}
		}
		discountList.add(discountValue);
		return discountList;
	}



	/**
	 * This method releases the voucher already applied in the cart automatically
	 *
	 * @param cart
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	@Override
	public void releaseVoucherInCheckout(final CartModel cart) throws JaloPriceFactoryException, CalculationException
	{

		final List<DiscountModel> discountList = cart.getDiscounts();

		if (CollectionUtils.isNotEmpty(discountList))
		{
			final String couponCode = ((PromotionVoucherModel) discountList.get(0)).getVoucherCode();

			try
			{
				releaseVoucher(couponCode, cart);
			}
			catch (final VoucherOperationException e)
			{
				LOG.error("Error while releasing voucher with message " + e.getMessage());
			}

			recalculateCartForCoupon(cart);
		}
	}


	/**
	 * This method returns list of all Vouchers corresponding to a specific customer
	 *
	 * @param customer
	 * @param voucherList
	 * @return AllVoucherListData
	 *
	 */
	@Override
	public AllVoucherListData getAllVoucherList(final CustomerModel customer, final List<VoucherModel> voucherList)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.COUPONS_DATE_FORMAT);
		final List<VoucherDisplayData> openVoucherDataList = new ArrayList<VoucherDisplayData>();
		final List<VoucherDisplayData> closedVoucherDataList = new ArrayList<VoucherDisplayData>();
		final AllVoucherListData allVoucherListData = new AllVoucherListData();
		for (final VoucherModel voucherModel : voucherList)
		{
			if (voucherModel instanceof PromotionVoucherModel)
			{

				final PromotionVoucherModel VoucherObj = (PromotionVoucherModel) voucherModel;

				final Set<RestrictionModel> restrictionList = voucherModel.getRestrictions();
				if (CollectionUtils.isNotEmpty(restrictionList))
				{
					boolean dateRestrExists = false;
					boolean userRestrExists = false;
					final boolean semiClosedRestrExists = false;

					DateRestrictionModel dateRestrObj = null;
					UserRestrictionModel userRestrObj = null;
					//SemiClosedRestrictionModel semiClosedRestrObj = null;


					for (final RestrictionModel restrictionModel : restrictionList)
					{

						//final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
						if (restrictionModel instanceof DateRestrictionModel)
						{
							dateRestrExists = true;
							dateRestrObj = (DateRestrictionModel) restrictionModel;
						}
						if (restrictionModel instanceof UserRestrictionModel)
						{
							userRestrExists = true;
							userRestrObj = (UserRestrictionModel) restrictionModel;
						}
						//TODO: Semi Closed Restriction-----Commented as functionality out of scope of R2.1   Uncomment when in scope
						//						if (restrictionModel instanceof SemiClosedRestrictionModel)
						//						{
						//							semiClosedRestrExists = true;
						//							//semiClosedRestrObj = (SemiClosedRestrictionModel) restrictionModel;
						//						}



						//							if (restrictionModel instanceof SemiClosedRestrictionModel)
						//							{
						//								semiClosedRestrExists = false;
						//								semiClosedRestrObj = (SemiClosedRestrictionModel) restrictionModel;
						//							}
					}

					if (dateRestrExists)
					{
						final String voucherCode = VoucherObj.getVoucherCode();

						if (dateRestrExists && voucherModelService.isReservable(voucherModel, voucherCode, customer))
						{
							final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
							if (userRestrExists)
							{
								//								final Collection<PrincipalModel> userList = userRestrObj != null ? userRestrObj.getUsers()
								//										: new ArrayList<PrincipalModel>();
								if (userRestrObj != null && userRestrObj.getUsers().contains(customer))
								{
									voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
									voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
									final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
									voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
									final Date startDate = dateRestrObj.getStartDate();
									voucherDisplayData.setVoucherCreationDate(startDate);
									closedVoucherDataList.add(voucherDisplayData);
								}
							}
							else if (!semiClosedRestrExists)
							{
								voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
								voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
								final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
								voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
								final Date startDate = dateRestrObj.getStartDate();
								voucherDisplayData.setVoucherCreationDate(startDate);
								openVoucherDataList.add(voucherDisplayData);
							}
						}
					}
				}
			}
		}
		allVoucherListData.setClosedVoucherList(closedVoucherDataList);
		allVoucherListData.setOpenVoucherList(openVoucherDataList);
		return allVoucherListData;
	}


	/**
	 * This method returns list of all CouponTransactions corresponding to a specific customer
	 *
	 * @param customer
	 * @return CouponHistoryStoreDTO
	 *
	 */
	@SuppressWarnings(BOXING)
	@Override
	public CouponHistoryStoreDTO getCouponTransactions(final CustomerModel customer) throws VoucherOperationException
	{
		final List<OrderData> orderDataList = new ArrayList<OrderData>();
		final List<VoucherData> voucherDataList = new ArrayList<VoucherData>();
		final List<String> voucherCodeList = new ArrayList<String>();
		final List<String> amountList = new ArrayList<String>();
		final Set<String> treeStringSet = new TreeSet<>();
		final List<CouponHistoryData> couponHistoryDTOList = new ArrayList<CouponHistoryData>();
		Collection<DiscountModel> discountModelList = new ArrayList<DiscountModel>();

		final Map<OrderData, VoucherData> orderVoucherDataMap = new HashMap<OrderData, VoucherData>();
		final Map<Date, OrderData> orderDateMap = new TreeMap<Date, OrderData>(Collections.reverseOrder());

		final Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap = new TreeMap<String, Collection<VoucherInvalidationModel>>();
		final CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
		VoucherData voucherData = new VoucherData();
		String savedSum = null;
		double finalAmount = 0.0D;
		int couponsRedeemedCount = 0;
		boolean isOrderDateValid = false;

		LOG.debug("Step 1-************************Coupon History");

		final List<OrderModel> orderModelsList = (List<OrderModel>) customer.getOrders();

		for (final OrderModel order : orderModelsList)
		{
			if (order.getType().equalsIgnoreCase(PARENT))
			{
				LOG.debug("Step 2-************************Inside orderModelsList");
				discountModelList = getDefaultVoucherService().getAppliedVouchers(order); // getting the list of all vouchers that are redeemed through orders
				final String orderCode = order.getCode();

				final OrderData orderDetailsData = mplCheckoutFacade.getOrderDetailsForCode(orderCode);

				for (final DiscountModel discount : discountModelList)
				{
					LOG.debug("Step 3-************************Inside Discount Model");
					VoucherModel voucher = new VoucherModel();
					Collection<VoucherInvalidationModel> voucherInvalidations = new ArrayList<VoucherInvalidationModel>();
					voucher = (VoucherModel) discount;
					voucherInvalidations = voucher.getInvalidations();

					try
					{
						voucherData = getDefaultVoucherFacade().getVoucher(((PromotionVoucherModel) voucher).getVoucherCode());//type casting to PromotionVoucherModel

						if (null != orderDetailsData && null != voucherData)
						{
							voucherDataList.add(voucherData);
							isOrderDateValid = checkTransactionDateValidity(orderDetailsData.getCreated());// restrict orders to last six months only

							if (isOrderDateValid)
							{
								orderDataList.add(orderDetailsData);
							}

							orderVoucherDataMap.put(orderDetailsData, voucherData);//mapping each order to its corresponding redeemed voucher

							if (isOrderDateValid)
							{
								orderDateMap.put(orderDetailsData.getCreated(), orderDetailsData);//mapping order with date such that the latest order is on top

								if (voucherCodeInvalidationMap.isEmpty())
								{
									voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucherInvalidations);
								}
								else
								{
									if (!(voucherCodeInvalidationMap.containsKey(voucherData.getVoucherCode())))
									{
										voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucherInvalidations);
									}
								}

							}


						}
					}
					catch (final VoucherOperationException e)
					{

						throw new VoucherOperationException("Error while retrieving voucher data from voucher model in facade");

					}

				}
			}
		}



		if (!orderDateMap.isEmpty()) //arranging voucher and corresponding order in a DTO
		{
			final Iterator orderDateMapIterator = orderDateMap.entrySet().iterator();
			while (orderDateMapIterator.hasNext())
			{
				LOG.debug("************************Sorted Order Map********************");
				final Map.Entry orderDaterEntry = (Map.Entry) orderDateMapIterator.next();
				if (!orderVoucherDataMap.isEmpty())
				{
					final Iterator orderVoucherMapIterator = orderVoucherDataMap.entrySet().iterator();
					while (orderVoucherMapIterator.hasNext())
					{
						final Map.Entry orderVoucherEntry = (Map.Entry) orderVoucherMapIterator.next();
						final OrderData orderDataKey = (OrderData) orderVoucherEntry.getKey();
						final VoucherData voucherDataValue = (VoucherData) orderVoucherEntry.getValue();

						if (((OrderData) orderDaterEntry.getValue()).equals(orderDataKey))
						{
							final CouponHistoryData couponHistoryDTO = new CouponHistoryData();

							LOG.debug("Step 5-************************Inside voucherOrderMapIterator");

							couponHistoryDTO.setCouponCode(voucherDataValue.getVoucherCode());
							couponHistoryDTO.setCouponDescription(voucherDataValue.getDescription());
							couponHistoryDTO.setOrderCode(orderDataKey.getCode());
							couponHistoryDTO.setRedeemedDate(getCouponRedeemedDate(orderDataKey.getCreated()));
							couponHistoryDTOList.add(couponHistoryDTO);

						}

					}

				}
			}
		}

		if (!voucherCodeInvalidationMap.isEmpty())
		{
			final Iterator voucherCodeInvalidationIterator = voucherCodeInvalidationMap.entrySet().iterator();
			while (voucherCodeInvalidationIterator.hasNext())
			{
				LOG.debug("************************voucherCodeInvalidationMap ITERATE********************");
				final Map.Entry voucherCodeInvalidationEntry = (Map.Entry) voucherCodeInvalidationIterator.next();
				final String voucherCode = (String) voucherCodeInvalidationEntry.getKey();
				final Collection<VoucherInvalidationModel> voucherInvalidationsCol = (Collection<VoucherInvalidationModel>) voucherCodeInvalidationEntry
						.getValue();

				if (null != voucherCode)
				{
					for (final VoucherInvalidationModel voucherInv : voucherInvalidationsCol)
					{

						if (null != voucherInv.getSavedAmount())
						{
							amountList.add(String.valueOf(voucherInv.getSavedAmount())); //calculating the amount saved through vouchers
						}

					}
				}
			}
		}


		if (!amountList.isEmpty())
		{
			for (final String amount : amountList)
			{

				LOG.debug("Step 4-************************Inside amountList");
				final double decimalAmount = Double.parseDouble(amount);
				finalAmount += decimalAmount;

				BigDecimal bd = new BigDecimal(finalAmount);
				bd = bd.setScale(2, RoundingMode.HALF_UP);
				savedSum = bd.toPlainString();
			}
		}


		// calculating no. of unique coupon codes that has been redeemed by the customer

		if (voucherDataList.size() == 1)
		{
			couponsRedeemedCount = 1;
		}
		else
		{
			for (final VoucherData voucher : voucherDataList)
			{
				voucherCodeList.add(voucher.getVoucherCode());
			}
			for (final String code : voucherCodeList)
			{

				treeStringSet.add(code);

			}

			couponsRedeemedCount = treeStringSet.size();

		}


		// organizing the DTO with necessary data
		couponHistoryStoreDTO.setCouponHistoryDTOList(couponHistoryDTOList);
		couponHistoryStoreDTO.setCouponsRedeemedCount(couponsRedeemedCount);
		couponHistoryStoreDTO.setSavedSum(savedSum);


		return couponHistoryStoreDTO;

	}


	/**
	 * @param RedeemedDate
	 * @return String
	 */
	@SuppressWarnings("javadoc")
	private String getCouponRedeemedDate(final Date fmtDate)
	{
		String finalCouponRedeemedDate = "";
		if (fmtDate != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fmtDate);
			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH);
			final int day = cal.get(Calendar.DAY_OF_MONTH);
			final String strMonth = getMonthFromInt(month).substring(0, 3);
			String dayPrefix = "";

			if (day < 10)
			{
				dayPrefix = "0";

			}
			else
			{
				dayPrefix = "";
			}
			finalCouponRedeemedDate = strMonth + SINGLE_SPACE + dayPrefix + day + SINGLE_SPACE + year;
		}
		return finalCouponRedeemedDate;
	}

	/**
	 * @param isDateValid
	 * @return boolean
	 */
	@SuppressWarnings("javadoc")
	private boolean checkTransactionDateValidity(final Date orderCreationDate)
	{
		boolean isDateValid = false;
		if (orderCreationDate != null)
		{
			final Calendar endCalendar = Calendar.getInstance();
			final Calendar startCalendar = Calendar.getInstance();
			final SimpleDateFormat dateFormatforMONTH = new java.text.SimpleDateFormat("MM");


			endCalendar.setTime(new Date());
			startCalendar.setTime(orderCreationDate);

			final int endYear = endCalendar.get(Calendar.YEAR);
			final int endMonth = Integer.parseInt(dateFormatforMONTH.format(endCalendar.getTime()));
			final int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);


			final int startYear = startCalendar.get(Calendar.YEAR);
			final int startMonth = Integer.parseInt(dateFormatforMONTH.format(startCalendar.getTime()));
			final int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

			final DateTime startDate = new DateTime().withDate(startYear, startMonth, startDay);
			final DateTime endDate = new DateTime().withDate(endYear, endMonth, endDay);


			final Months monthsBetween = Months.monthsBetween(startDate, endDate);
			final int monthsBetweenInt = monthsBetween.getMonths();

			if (monthsBetweenInt <= 6)
			{
				isDateValid = true;
			}
		}
		return isDateValid;
	}


	/**
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)

	{
		final List<String> months = Arrays.asList(JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER,
				NOVEMBER, DECEMBER);
		final String strMonth = months.get(month);
		return strMonth;

	}


	/**
	 * @Description: For Apportioning of vouchers
	 * @param voucher
	 * @param cartModel
	 * @param voucherCode
	 */
	@Override
	public void setApportionedValueForVoucher(final VoucherModel voucher, final CartModel cartModel, final String voucherCode)
	{
		LOG.debug("Step 16:::Inside setApportionedValueForVoucher");
		final Voucher voucherObj = (Voucher) getModelService().getSource(voucher);
		final AbstractOrder orderObj = (AbstractOrder) getModelService().getSource(cartModel);

		final VoucherEntrySet voucherEntrySet = voucherObj.getApplicableEntries(orderObj);

		final List<AbstractOrderEntry> applicableOrderEntryList = getOrderEntriesFromVoucherEntries(voucherEntrySet);

		double totalApplicablePrice = 0.0D;
		double percentageDiscount = 0.0D;

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			totalApplicablePrice += entry.getTotalPriceAsPrimitive();
		}

		for (final DiscountValue discount : cartModel.getGlobalDiscountValues())
		{
			if (discount.getCode().equals(voucher.getCode()))
			{
				percentageDiscount = discount.getAppliedValue();
				break;
			}
		}

		percentageDiscount = (percentageDiscount / totalApplicablePrice) * 100;

		LOG.debug("Step 17:::percentageDiscount is " + percentageDiscount);


		double totalAmtDeductedOnItemLevel = 0.00D;

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			double entryLevelApportionedPrice = 0.00D;
			double currNetAmtAftrAllDisc = 0.00D;

			final double entryTotalPrice = entry.getTotalPriceAsPrimitive();

			if (applicableOrderEntryList.indexOf(entry) == (applicableOrderEntryList.size() - 1))
			{
				final double discountPriceValue = (percentageDiscount / 100) * totalApplicablePrice;
				entryLevelApportionedPrice = discountPriceValue - totalAmtDeductedOnItemLevel;
			}
			else
			{
				entryLevelApportionedPrice = (percentageDiscount / 100) * entryTotalPrice;
				totalAmtDeductedOnItemLevel += entryLevelApportionedPrice;
			}

			LOG.debug("Step 18:::entryLevelApportionedPrice is " + entryLevelApportionedPrice);

			entry.setProperty(MarketplacecouponConstants.COUPONCODE, voucherCode);//TODO
			entry.setProperty(MarketplacecouponConstants.COUPONVALUE, Double.valueOf(entryLevelApportionedPrice));//TODO


			if ((null != entry.getProperty(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE) && !((String) entry
					.getProperty(MarketplacecommerceservicesConstants.PRODUCTPROMOCODE)).isEmpty())
					|| (null != entry.getProperty(MarketplacecommerceservicesConstants.CARTPROMOCODE) && !((String) entry
							.getProperty(MarketplacecommerceservicesConstants.CARTPROMOCODE)).isEmpty()))
			{
				final double netAmtAftrAllDisc = entry.getProperty(MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC) != null ? ((Double) entry
						.getProperty(MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC)).doubleValue() : 0.00D;

				if (netAmtAftrAllDisc > entryLevelApportionedPrice)
				{
					currNetAmtAftrAllDisc = netAmtAftrAllDisc - entryLevelApportionedPrice;

				}
				else
				{
					currNetAmtAftrAllDisc = Double.parseDouble(MarketplacecouponConstants.ZEROPOINTZEROONE);
				}

			}
			else
			{
				if (entryTotalPrice > entryLevelApportionedPrice)
				{
					currNetAmtAftrAllDisc = entryTotalPrice - entryLevelApportionedPrice;

				}
				else
				{
					currNetAmtAftrAllDisc = Double.parseDouble(MarketplacecouponConstants.ZEROPOINTZEROONE);
				}

			}
			LOG.debug("Step 19:::currNetAmtAftrAllDisc is " + currNetAmtAftrAllDisc);
			entry.setProperty(MarketplacecommerceservicesConstants.NETAMOUNTAFTERALLDISC, Double.valueOf(currNetAmtAftrAllDisc));
		}

		//final VoucherValue voucherVal = voucherObj.getAppliedValue(orderObj);

	}

	/**
	 * @Description: For getting list of applicable AbstractOrderEntry from voucherEntrySet
	 * @param voucherEntrySet
	 * @return list of applicable AbstractOrderEntry
	 */
	@Override
	public List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherEntrySet voucherEntrySet)
	{
		LOG.debug("Step 9:::Inside getOrderEntriesFromVoucherEntries");
		final Iterator iter = voucherEntrySet.iterator();
		final List<AbstractOrderEntry> applicableOrderList = new ArrayList<AbstractOrderEntry>();

		while (iter.hasNext())
		{
			final VoucherEntry voucherEntry = (VoucherEntry) iter.next();
			final AbstractOrderEntry entry = voucherEntry.getOrderEntry();

			LOG.debug("Step 10:::applicable entry ===" + entry);

			applicableOrderList.add(entry);

		}

		return applicableOrderList;
	}



	/**
	 * This method is used to release the voucher applied
	 *
	 * @param voucherCode
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException
	{
		LOG.debug("Step 2:::Inside releaseVoucher");
		validateVoucherCodeParameter(voucherCode);
		final VoucherModel voucher = getVoucherModel(voucherCode);
		if (voucher != null && cartModel != null)
		{
			LOG.debug("Step 3:::Voucher and cart is not null");
			try
			{
				getVoucherService().releaseVoucher(voucherCode, cartModel);
				LOG.debug("Step 4:::Voucher released");
				return;
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new VoucherOperationException("Couldn't release voucher: " + voucherCode);
			}
		}
	}





	public void setMplCouponService(final MplCouponService mplCouponService)
	{
		this.mplCouponService = mplCouponService;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	public VoucherModelService getVoucherModelService()
	{


		return voucherModelService;
	}


	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}


	public CartService getCartService()
	{
		return cartService;
	}


	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}


	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}


	public VoucherService getVoucherService()
	{
		return voucherService;
	}


	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}


	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	public DefaultVoucherFacade getDefaultVoucherFacade()
	{
		return defaultVoucherFacade;
	}

	public DefaultVoucherService getDefaultVoucherService()
	{
		return defaultVoucherService;
	}


	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	public MplCommerceCartCalculationStrategy getCalculationStrategy()
	{
		return calculationStrategy;
	}



	public void setCalculationStrategy(final MplCommerceCartCalculationStrategy calculationStrategy)
	{
		this.calculationStrategy = calculationStrategy;
	}


	public VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}


	public void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}


	public MplCouponService getMplCouponService()
	{
		return mplCouponService;
	}
}
