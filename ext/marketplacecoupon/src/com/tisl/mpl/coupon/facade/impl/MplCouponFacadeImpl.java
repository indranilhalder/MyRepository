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
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.GenericSearchConstants.LOG;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.T;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
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
	@Autowired
	private MplVoucherService mplVoucherService;


	@Autowired
	private Converter<VoucherModel, VoucherDisplayData> voucherDisplayConverter;



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
		getMplVoucherService().recalculateCartForCoupon(cartModel);
	}



	/**
	 * This method calculates the cart Values after redeeming or releasing the voucher
	 *
	 * @param cartModel
	 * @param couponStatus
	 * @param reddemIdentifier
	 * @return VoucherDiscountData
	 *
	 */
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
					break;
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
	 * This method displays top coupons
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
	 * This method calculates voucher discount and sets in VoucherDisplayData
	 *
	 * @param voucherModel
	 * @param voucherDataList
	 * @return ArrayList<VoucherDisplayData>
	 */
	private List<VoucherDisplayData> calculateVoucherDisplay(final VoucherModel voucherModel,
			final List<VoucherDisplayData> voucherDataList, final CartModel cartModel)
	{
		final List<AbstractOrderEntry> applicableOrderEntryList = getOrderEntriesFromVoucherEntries(voucherModel, cartModel);
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
				final String error = checkViolatedRestrictions(voucher, cartModel);
				if (error.equalsIgnoreCase("Date"))
				{
					throw new VoucherOperationException("Voucher cannot be redeemed: " + voucherCode);
				}
				else if (error.equalsIgnoreCase("User"))
				{
					throw new VoucherOperationException("User not valid for : " + voucherCode);
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

					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, cartModel);

					//Important! Checking cart, if total amount <0, release this voucher
					checkVoucherApplicability(voucherCode, voucher, cartModel, applicableOrderEntryList);

					//apportioning
					setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);
					checkFlag = true;
					//checkFlag = StringUtils.isNotEmpty(cartModel.getCouponErrorMsg()) ? false : true;
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
	 * This method checks if the voucher is applicable or not
	 *
	 * @param voucherCode
	 * @param lastVoucher
	 * @param cartModel
	 * @param applicableOrderEntryList
	 * @throws ModelSavingException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloPriceFactoryException
	 */
	protected void checkVoucherApplicability(final String voucherCode, final VoucherModel lastVoucher, final CartModel cartModel,
			final List<AbstractOrderEntryModel> applicableOrderEntryList) throws ModelSavingException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException,
			JaloPriceFactoryException
	{
		final VoucherDiscountData data = getMplVoucherService().checkCartAfterApply(lastVoucher, cartModel,
				applicableOrderEntryList);
		if (null != data && StringUtils.isNotEmpty(data.getRedeemErrorMsg()))
		{
			if (data.getRedeemErrorMsg().equalsIgnoreCase("freebie"))
			{
				throw new VoucherOperationException("Voucher " + voucherCode + " cannot be redeemed: freebie");
			}
			else if (data.getRedeemErrorMsg().equalsIgnoreCase("Price_exceeded"))
			{
				throw new VoucherOperationException("Voucher " + voucherCode + " cannot be redeemed: total price exceeded");
			}
			else if (data.getRedeemErrorMsg().equalsIgnoreCase("not_applicable"))
			{
				throw new VoucherOperationException("Voucher is not applicable: " + voucherCode);
			}
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
		return getMplVoucherService().getVoucherModel(voucherCode);
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
	 * Checks if voucher is applicable for the cart
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
	 * Checks if voucher is reservable for the cart
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
	 * This method returns the violated restrictions
	 *
	 * @param voucher
	 * @param cartModel
	 * @return boolean
	 */
	protected String checkViolatedRestrictions(final VoucherModel voucher, final CartModel cartModel)
	{
		final List<RestrictionModel> getViolatedRestrictions = getVoucherModelService().getViolatedRestrictions(voucher, cartModel);
		String error = "";
		for (final RestrictionModel restriction : getViolatedRestrictions)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				LOG.error("Date restriction is violated");
				error = "Date";
				break;
			}
			else if (restriction instanceof UserRestrictionModel)
			{
				LOG.error("user restriction is violated");
				error = "User";
				break;
			}
		}
		return error;
	}



	/**
	 * This method adds global discount
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
		return getMplVoucherService().setGlobalDiscount(discountList, voucherList, cartSubTotal, promoCalcValue, lastVoucher,
				discountAmt);
	}



	/**
	 * @Description:This method releases the voucher already applied in the cart automatically
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
	 * @Description: This method returns list of all CouponTransactions corresponding to a specific customer to the
	 *               controller
	 * @param customer
	 * @return CouponHistoryStoreDTO
	 * @throws VoucherOperationException
	 *
	 */

	@Override
	public CouponHistoryStoreDTO getCouponTransactions(final CustomerModel customer) throws VoucherOperationException
	{
		CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
		final Set<Map<OrderModel, VoucherModel>> voucherRedeemedOrderMap = getMplCouponService().getCouponHistoryTransactions(
				customer);
		couponHistoryStoreDTO = iterateSetToCreateCouponDTO(voucherRedeemedOrderMap);
		return couponHistoryStoreDTO;
	}

	/**
	 * @Description: This method returns list of all CouponTransactions corresponding to a specific customer
	 * @param customer
	 * @return CouponHistoryStoreDTO
	 *
	 */
	@SuppressWarnings(BOXING)
	private CouponHistoryStoreDTO iterateSetToCreateCouponDTO(final Set<Map<OrderModel, VoucherModel>> voucherRedeemedOrderMap)
			throws VoucherOperationException
	{

		final List<String> voucherCodeList = new ArrayList<String>();
		List<String> amountList = new ArrayList<String>();
		Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap = new TreeMap<String, Collection<VoucherInvalidationModel>>();
		final CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
		Collection<VoucherInvalidationModel> voucherInvalidations = new ArrayList<VoucherInvalidationModel>();
		VoucherData voucherData = new VoucherData();
		int couponsRedeemedCount = 0;
		String savedSum = null;
		final boolean isOrderDateValid = false;

		//iterating voucher and corresponding order model
		if (!voucherRedeemedOrderMap.isEmpty())
		{
			for (final Map<OrderModel, VoucherModel> entry : voucherRedeemedOrderMap)
			{
				for (final Map.Entry<OrderModel, VoucherModel> mapEntry : entry.entrySet())
				{
					final OrderModel order = mapEntry.getKey();
					final VoucherModel voucher = mapEntry.getValue();
					final String orderCode = order.getCode();
					final OrderData orderDetailsData = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
					voucherInvalidations = voucher.getInvalidations();

					//type casting to PromotionVoucherModel
					voucherData = getDefaultVoucherFacade().getVoucher(((PromotionVoucherModel) voucher).getVoucherCode());
					voucherCodeList.add(((PromotionVoucherModel) voucher).getVoucherCode());
					voucherCodeInvalidationMap = getVoucherCodeInvalidationMap(voucherInvalidations, voucherData, orderDetailsData,
							isOrderDateValid);
				}
			}
		}

		if (!voucherCodeInvalidationMap.isEmpty())
		{
			amountList = getAmountListFromInvalidations(voucherCodeInvalidationMap);
		}

		if (!amountList.isEmpty())
		{
			savedSum = getSumFromAmountList(amountList);

		}

		// calculating no. of unique coupon codes that has been redeemed by the customer

		if (!voucherCodeList.isEmpty())
		{
			couponsRedeemedCount = getCouponsRedeemedCount(voucherCodeList);
		}


		// organizing the DTO with necessary data
		couponHistoryStoreDTO.setCouponsRedeemedCount(couponsRedeemedCount);
		couponHistoryStoreDTO.setSavedSum(savedSum);

		return couponHistoryStoreDTO;

	}

	/**
	 * @Description: This method returns coupon redemption count for a specific customer
	 * @param voucherCodeList
	 * @return couponsRedeemedCount
	 */
	private int getCouponsRedeemedCount(final List<String> voucherCodeList)
	{
		final Set<String> treeStringSet = new TreeSet<>();
		int couponsRedeemedCount = 0;
		if (voucherCodeList.size() == 1)
		{
			couponsRedeemedCount = 1;
		}
		else
		{
			for (final String code : voucherCodeList)
			{
				treeStringSet.add(code);
			}
			couponsRedeemedCount = treeStringSet.size();
		}
		return couponsRedeemedCount;
	}



	/**
	 * @Description: This method returns total saved amount for a specific customer as yet
	 * @param amountList
	 * @return savedSum
	 */
	private String getSumFromAmountList(final List<String> amountList)
	{
		String savedSum = null;
		double finalAmount = 0.0D;
		for (final String amount : amountList)
		{
			LOG.debug("Step 4-************************Inside amountList");
			final double decimalAmount = Double.parseDouble(amount);
			finalAmount += decimalAmount;

			BigDecimal bd = new BigDecimal(finalAmount);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			savedSum = bd.toPlainString();
		}
		return savedSum;
	}



	/**
	 * @Description: This method returns list of saved amount for a customer
	 * @param voucherCodeInvalidationMap
	 * @return amountList
	 */
	private List<String> getAmountListFromInvalidations(
			final Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap)
	{

		final Iterator voucherCodeInvalidationIterator = voucherCodeInvalidationMap.entrySet().iterator();
		final List<String> amountList = new ArrayList<String>();
		while (voucherCodeInvalidationIterator.hasNext())
		{
			final Map.Entry voucherCodeInvalidationEntry = (Map.Entry) voucherCodeInvalidationIterator.next();
			final String voucherCode = (String) voucherCodeInvalidationEntry.getKey();
			final Collection<VoucherInvalidationModel> voucherInvalidationsCol = (Collection<VoucherInvalidationModel>) voucherCodeInvalidationEntry
					.getValue();

			if (null != voucherCode) //checking for valid voucherCode
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
		return amountList;
	}



	/**
	 * @Description: This method maps the voucher invalidations with to a unique-voucher code for a specific customer
	 * @param voucherInvalidations
	 * @param voucherData
	 * @param orderDetailsData
	 * @param isOrderDateValid
	 * @return Map<String, Collection<VoucherInvalidationModel>>
	 */
	private Map<String, Collection<VoucherInvalidationModel>> getVoucherCodeInvalidationMap(
			final Collection<VoucherInvalidationModel> voucherInvalidations, final VoucherData voucherData,
			final OrderData orderDetailsData, boolean isOrderDateValid)
	{

		final Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap = new TreeMap<String, Collection<VoucherInvalidationModel>>();

		if (null != orderDetailsData && null != voucherData)
		{

			isOrderDateValid = checkTransactionDateValidity(orderDetailsData.getCreated());// restrict orders to last six months only


			if (isOrderDateValid)
			{
				if (voucherCodeInvalidationMap.isEmpty())
				{
					voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucherInvalidations);//for an empty map
				}
				else
				{
					if (!(voucherCodeInvalidationMap.containsKey(voucherData.getVoucherCode())))
					{
						voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucherInvalidations);//when the map contains other invalidations
					}
				}

			}
		}
		return voucherCodeInvalidationMap;
	}



	/**
	 * @Description: This method returns the coupon redeemed date
	 * @param fmtDate
	 * @return String
	 *
	 */
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
	 * @Description: This method restricts orders in last six months
	 * @param orderCreationDate
	 * @return boolean
	 */
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

			if (monthsBetweenInt < 6)
			{
				isDateValid = true;
			}
		}
		return isDateValid;
	}


	/**
	 * @Description: This method derives the month from integer value
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
	public void setApportionedValueForVoucher(final VoucherModel voucher, final CartModel cartModel, final String voucherCode,
			final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		getMplVoucherService().setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);

	}



	/**
	 * @Description: For getting list of applicable AbstractOrderEntry from voucherEntrySet
	 * @param voucherModel
	 * @param cartModel
	 * @return list of applicable AbstractOrderEntry
	 */
	@Override
	public List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherModel voucherModel, final CartModel cartModel)
	{
		return getMplVoucherService().getOrderEntriesFromVoucherEntries(voucherModel, cartModel);
	}


	/**
	 * @Description: This method returns the order entries which are applicable for the voucher
	 * @param voucherModel
	 * @param cartModel
	 * @return List<AbstractOrderEntryModel>
	 */
	private List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final CartModel cartModel)
	{
		return getMplVoucherService().getOrderEntryModelFromVouEntries(voucherModel, cartModel);
	}



	/**
	 * @Description: This method is used to release the voucher applied
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
				for (final AbstractOrderEntryModel entry : getOrderEntryModelFromVouEntries(voucher, cartModel))//cartModel.getEntries()
				{
					entry.setCouponCode("");
					entry.setCouponValue(Double.valueOf(0.00D));
					getModelService().save(entry);
				}

				LOG.debug("Step 5:::CouponCode, CouponValue  resetted");
				return;
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new VoucherOperationException("Couldn't release voucher: " + voucherCode);
			}
		}
	}



	/**
	 * This method returns all closed coupons
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherDisplayData>
	 *
	 */
	@Override
	public SearchPageData<VoucherDisplayData> getAllClosedCoupons(final CustomerModel customer, final PageableData pageableData)
	{
		final SearchPageData<VoucherModel> searchVoucherModel = getMplCouponService().getClosedVoucher(customer, pageableData);
		final List<VoucherModel> voucherList = searchVoucherModel.getResults();

		for (final VoucherModel voucher : voucherList)
		{
			LOG.debug("---" + voucher.getCode());
		}


		final SearchPageData<VoucherDisplayData> searchPageDataVoucher = convertPageData(searchVoucherModel,
				voucherDisplayConverter);

		return searchPageDataVoucher;
	}


	/**
	 *
	 *
	 * @param source
	 * @param converter
	 * @return <S, T> SearchPageData<T>
	 */
	protected <S, T> SearchPageData<T> convertPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
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
	 * @param defaultVoucherService
	 *           the defaultVoucherService to set
	 */
	public void setDefaultVoucherService(final DefaultVoucherService defaultVoucherService)
	{
		this.defaultVoucherService = defaultVoucherService;
	}




	/**
	 * @param defaultVoucherFacade
	 *           the defaultVoucherFacade to set
	 */
	public void setDefaultVoucherFacade(final DefaultVoucherFacade defaultVoucherFacade)
	{
		this.defaultVoucherFacade = defaultVoucherFacade;
	}




	/**
	 * @return the mplVoucherService
	 */
	public MplVoucherService getMplVoucherService()
	{
		return mplVoucherService;
	}




	/**
	 * @param mplVoucherService
	 *           the mplVoucherService to set
	 */
	public void setMplVoucherService(final MplVoucherService mplVoucherService)
	{
		this.mplVoucherService = mplVoucherService;
	}




}
