/**
 *
 */
package com.tisl.mpl.coupon.facade.impl;


import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercefacades.voucher.impl.DefaultVoucherFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;


/**
 * @author TCS
 *
 */
public class MplCouponFacadeImpl implements MplCouponFacade
{
	private static final Logger LOG = Logger.getLogger(MplCouponFacadeImpl.class);

	@Resource(name = "voucherService")
	private VoucherService voucherService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;
	@Resource(name = "mplCouponService")
	private MplCouponService mplCouponService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
	@Resource(name = "defaultVoucherFacade")
	private DefaultVoucherFacade defaultVoucherFacade;
	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "voucherDisplayConverter")
	private Converter<VoucherModel, VoucherDisplayData> voucherDisplayConverter;

	@Resource(name = "voucherTransactionConverter")
	private Converter<VoucherInvalidationModel, CouponHistoryData> voucherTransactionConverter;


	/**
	 * This method recalculates the cart after redeeming/releasing the voucher
	 *
	 * @param cartModel
	 *
	 */
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel) throws EtailNonBusinessExceptions
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

		//Find out all the vouchers for the selected cart
		final List<VoucherData> voucherDataList = getVoucherFacade().getVouchersForCart();

		for (final VoucherData voucher : voucherDataList)
		{
			//Check the global discount applied against the cart
			final List<DiscountValue> discount = cartModel.getGlobalDiscountValues();
			for (final DiscountValue dis : discount)
			{
				if (null != dis.getCode() && null != voucher.getCode() && dis.getCode().equalsIgnoreCase(voucher.getCode()))
				{
					totalDiscount += dis.getAppliedValue();//totalDiscount is discount value for the applied voucher
					break;
				}
			}

		}

		LOG.debug("Total discount for voucher is :::: " + totalDiscount);

		//Set voucher details in VoucherDiscountData and return the data
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
			data.setRedeemErrorMsg(MarketplacecommerceservicesConstants.COUPONREDEEMERROR);
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
		//returns all active vouchers from service
		return getMplCouponService().getVoucher();
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

		for (final VoucherModel voucherModel : voucherList)
		{
			if (voucherModel instanceof PromotionVoucherModel
					&& checkVoucherCanBeRedeemed(voucherModel, ((PromotionVoucherModel) voucherModel).getVoucherCode(), cart))
			{
				//sets voucher details in voucherDataList
				voucherDataList = calculateVoucherDisplay(voucherModel, voucherDataList, cart);
			}
		}
		//Sorts the voucherDataList based on coupon discount value
		voucherDataList = getMplCouponService().getSortedVoucher(voucherDataList);

		final int couponCount = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.COUPONTOPCOUNT, MarketplacecommerceservicesConstants.COUPONTOPCOUNTDEFVAL));
		//to display only top 5 or configured coupons
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
		double voucherDiscount = 0.0D;

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			totalPrice += null == entry.getTotalPrice() ? 0.0D : entry.getTotalPrice().doubleValue();
		}

		if (voucherModel.getAbsolute().booleanValue())
		{
			voucherDiscount = voucherModel.getValue().doubleValue();
		}
		else
		{
			voucherDiscount = (totalPrice * voucherModel.getValue().doubleValue()) / 100;
		}

		return setVoucherdata(voucherDiscount, voucherDataList, voucherModel);
	}

	/**
	 *
	 * This method sets the voucherData and returns a list
	 *
	 * @param voucherDiscount
	 * @param voucherDataList
	 * @param voucherModel
	 * @return List<VoucherDisplayData>
	 */
	private List<VoucherDisplayData> setVoucherdata(final double voucherDiscount, final List<VoucherDisplayData> voucherDataList,
			final VoucherModel voucherModel)
	{
		final VoucherDisplayData voucherData = new VoucherDisplayData();

		voucherData.setCouponDiscount(voucherDiscount);
		if (voucherModel instanceof PromotionVoucherModel)
		{
			voucherData.setVoucherCode(((PromotionVoucherModel) voucherModel).getVoucherCode());
		}
		else
		{
			voucherData.setVoucherCode("");
		}
		voucherData.setVoucherDescription(voucherModel.getDescription());
		voucherDataList.add(voucherData);

		return voucherDataList;
	}

	/**
	 * Applies the voucher and returns true if successful
	 *
	 * @param voucherCode
	 * @param cartModel
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@Override
	public boolean applyVoucher(final String voucherCode, final CartModel cartModel) throws VoucherOperationException,
			EtailNonBusinessExceptions
	{
		boolean checkFlag = false;
		try
		{
			if (CollectionUtils.isEmpty(cartModel.getDiscounts()))
			{
				LOG.debug("Step 2:::No voucher is applied to cart");

				//Checks if voucherCode is valid
				validateVoucherCodeParameter(voucherCode);
				if (!isVoucherCodeValid(voucherCode))
				{
					throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
				}
				LOG.debug("Step 3:::Voucher Code is valid");

				//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
				final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
				if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
				{
					throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
				}

				LOG.debug("Step 4:::Voucher is present and value is not negative");
				if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel)) //Checks whether voucher is applicable
				{
					LOG.debug("Step 5:::Voucher is not applicable");
					final String error = checkViolatedRestrictions(voucher, cartModel);
					if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.DATE))
					{
						throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
					}
					else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.USER))
					{
						throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERINVALIDUSER + voucherCode);
					}
					else
					{
						throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
					}
				}

				else if (!checkVoucherIsReservable(voucher, voucherCode, cartModel)) //Checks whether voucher is reservable
				{
					LOG.debug("Step 6:::Voucher is not reservable");
					throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
				}

				else
				{
					LOG.debug("Step 7:::Voucher can be redeemed");
					if (!getVoucherService().redeemVoucher(voucherCode, cartModel))
					{
						throw new VoucherOperationException(MarketplacecommerceservicesConstants.ERRORAPPLYVOUCHER + voucherCode);
					}

					recalculateCartForCoupon(cartModel); //Recalculates cart after applying voucher

					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, cartModel); //Finds applicable order entries

					//Important! Checking cart, if total amount <0, release this voucher
					checkVoucherApplicability(voucherCode, voucher, cartModel, applicableOrderEntryList);

					//apportioning
					setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);
					checkFlag = true;
				}
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
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
	 * @throws EtailNonBusinessExceptions
	 * @throws ModelSavingException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 */
	protected void checkVoucherApplicability(final String voucherCode, final VoucherModel lastVoucher, final CartModel cartModel,
			final List<AbstractOrderEntryModel> applicableOrderEntryList) throws VoucherOperationException,
			EtailNonBusinessExceptions
	{
		final VoucherDiscountData data = getMplVoucherService().checkCartAfterApply(lastVoucher, cartModel,
				applicableOrderEntryList);
		if (null != data && StringUtils.isNotEmpty(data.getRedeemErrorMsg()))
		{
			if (data.getRedeemErrorMsg().equalsIgnoreCase(MarketplacecommerceservicesConstants.EXCFREEBIE))
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHER + voucherCode
						+ MarketplacecommerceservicesConstants.FREEBIEERROR);
			}
			else if (data.getRedeemErrorMsg().equalsIgnoreCase(MarketplacecommerceservicesConstants.PRICEEXCEEDED))
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHER + voucherCode
						+ MarketplacecommerceservicesConstants.PRICEEXCEEDERROR);
			}
			else if (data.getRedeemErrorMsg().equalsIgnoreCase(MarketplacecommerceservicesConstants.NOTAPPLICABLE))
			{
				throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
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
		return (getVoucherService().getVoucher(voucherCode) == null ? false : true);
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
		String error = null;
		for (final RestrictionModel restriction : getViolatedRestrictions)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.DATERESTVIOLATION);
				error = MarketplacecommerceservicesConstants.DATE;
				break;
			}
			else if (restriction instanceof UserRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.USERRESTVIOLATION);
				error = MarketplacecommerceservicesConstants.USER;
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
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucherInCheckout(final CartModel cart) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		final List<DiscountModel> discountList = cart.getDiscounts();

		if (CollectionUtils.isNotEmpty(discountList) && discountList.get(0) instanceof PromotionVoucherModel)
		{
			final String couponCode = ((PromotionVoucherModel) discountList.get(0)).getVoucherCode(); //Only 1 coupon can be applied
			releaseVoucher(couponCode, cart);
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
		final List<VoucherInvalidationModel> voucherInvalidationList = getMplCouponService().getAllVoucherInvalidations(customer);
		couponHistoryStoreDTO = iterateSetToCreateCouponDTO(voucherInvalidationList);
		return couponHistoryStoreDTO;
	}

	/**
	 * @Description: This method returns list of all CouponTransactions corresponding to a specific customer
	 * @param voucherInvalidationList
	 * @return CouponHistoryStoreDTO
	 *
	 */
	private CouponHistoryStoreDTO iterateSetToCreateCouponDTO(final List<VoucherInvalidationModel> voucherInvalidationList)
			throws VoucherOperationException
	{
		final Map<Date, OrderData> orderDateMap = new TreeMap<Date, OrderData>(Collections.reverseOrder());
		final List<String> voucherCodeList = new ArrayList<String>();
		List<String> amountList = new ArrayList<String>();
		final boolean isOrderDateValid = true;
		final Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap = new TreeMap<String, Collection<VoucherInvalidationModel>>();
		final Map<OrderData, VoucherData> orderVoucherMap = new HashMap<OrderData, VoucherData>();
		Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMapFinal = new TreeMap<String, Collection<VoucherInvalidationModel>>();

		final CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
		final List<CouponHistoryData> couponHistoryDataList = new ArrayList<CouponHistoryData>();

		OrderData orderDetailsData = new OrderData();
		VoucherData voucherData = new VoucherData();
		int couponsRedeemedCount = 0;
		String savedSum = null;
		if (CollectionUtils.isNotEmpty(voucherInvalidationList))
		{

			for (final VoucherInvalidationModel voucherInvalidation : voucherInvalidationList)
			{
				final OrderModel order = voucherInvalidation.getOrder();
				voucherCodeList.add(voucherInvalidation.getVoucher().getCode());
				voucherData = getDefaultVoucherFacade().getVoucher(
						((PromotionVoucherModel) voucherInvalidation.getVoucher()).getVoucherCode());

				if (order.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PARENT))
				{
					final String orderCode = order.getCode();
					orderDetailsData = getMplCheckoutFacade().getOrderDetailsForCode(orderCode);
					//	isOrderDateValid = checkTransactionDateValidity(orderDetailsData.getCreated());// restrict orders to last six months only

					if (isOrderDateValid && null != voucherData)
					{
						orderDateMap.put(orderDetailsData.getCreated(), orderDetailsData);//mapping order with date such that the latest order is on top
						orderVoucherMap.put(orderDetailsData, voucherData);
						voucherCodeInvalidationMapFinal = generateVoucherInvalidationMap(voucherCodeInvalidationMap,
								voucherInvalidation, voucherData);

					}

				}
			}
		}
		final List<CouponHistoryData> couponHistoryDTOListFinal = sortcouponHistoryDTOList(orderDateMap, orderVoucherMap,
				couponHistoryDataList);

		if (!voucherCodeInvalidationMapFinal.isEmpty())
		{
			amountList = getAmountListFromInvalidations(voucherCodeInvalidationMapFinal);
		}

		if (CollectionUtils.isNotEmpty(amountList))// as per IQA comments for code sanitization
		{
			savedSum = getSumFromAmountList(amountList);

		}

		// calculating no. of unique coupon codes that has been redeemed by the customer
		if (CollectionUtils.isNotEmpty(voucherCodeList))// as per IQA comments for code sanitization
		{
			couponsRedeemedCount = getCouponsRedeemedCount(voucherCodeList);
		}

		// organizing the DTO with necessary data
		couponHistoryStoreDTO.setCouponsRedeemedCount(couponsRedeemedCount);
		couponHistoryStoreDTO.setSavedSum(savedSum);
		if (CollectionUtils.isNotEmpty(couponHistoryDTOListFinal)) // as per IQA comments for code sanitization
		{
			couponHistoryStoreDTO.setCouponHistoryDataList(couponHistoryDTOListFinal);
		}
		return couponHistoryStoreDTO;

	}

	/**
	 * @param orderDateMap
	 * @param orderVoucherMap
	 * @param couponHistoryDTOList
	 * @return List<CouponHistoryData>
	 */
	private List<CouponHistoryData> sortcouponHistoryDTOList(final Map<Date, OrderData> orderDateMap,
			final Map<OrderData, VoucherData> orderVoucherMap, final List<CouponHistoryData> couponHistoryDTOList)
	{
		if (!orderDateMap.isEmpty()) //arranging voucher and corresponding order in a DTO
		{
			for (final Map.Entry<Date, OrderData> orderDaterEntry : orderDateMap.entrySet())// as per IQA comments for code sanitization
			{

				if (!orderVoucherMap.isEmpty())
				{
					for (final Map.Entry<OrderData, VoucherData> orderVoucherEntry : orderVoucherMap.entrySet()) // as per IQA comments for code sanitization
					{
						final OrderData orderDataKey = orderVoucherEntry.getKey();
						final VoucherData voucherDataValue = orderVoucherEntry.getValue();

						if ((orderDaterEntry.getValue()).equals(orderDataKey))
						{
							final CouponHistoryData couponHistoryDTO = new CouponHistoryData();

							couponHistoryDTO.setCouponCode(voucherDataValue.getVoucherCode());
							couponHistoryDTO.setCouponDescription(voucherDataValue.getDescription());
							couponHistoryDTO.setOrderCode(orderDataKey.getCode());
							if (null != getCouponRedeemedDate(orderDataKey.getCreated()))// as per IQA comments for code sanitization
							{
								couponHistoryDTO.setRedeemedDate(getCouponRedeemedDate(orderDataKey.getCreated()));
							}
							couponHistoryDTOList.add(couponHistoryDTO);

						}

					}

				}
			}
		}
		return couponHistoryDTOList;
	}

	/**
	 * @param voucherCodeInvalidationMap
	 * @param voucherInvalidation
	 * @param voucherData
	 * @return Map<String, Collection<VoucherInvalidationModel>>
	 */
	private Map<String, Collection<VoucherInvalidationModel>> generateVoucherInvalidationMap(
			final Map<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationMap,
			final VoucherInvalidationModel voucherInvalidation, final VoucherData voucherData)
	{
		final VoucherModel voucher = voucherInvalidation.getVoucher();
		if (voucherCodeInvalidationMap.isEmpty())
		{
			if (voucher instanceof PromotionVoucherModel) // as per IQA comments for code sanitization
			{
				voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucher.getInvalidations());//for an empty map
			}
		}
		else if (!(voucherCodeInvalidationMap.containsKey(voucherData.getVoucherCode())))
		{
			if (voucher instanceof PromotionVoucherModel) // as per IQA comments for code sanitization
			{
				voucherCodeInvalidationMap.put(voucherData.getVoucherCode(), voucher.getInvalidations());//when the map contains other invalidations
			}
		}
		return voucherCodeInvalidationMap;

	}


	/**
	 * This method returns the coupon redeemed date
	 *
	 * @param fmtDate
	 * @return String
	 *
	 */
	private String getCouponRedeemedDate(final Date fmtDate)
	{
		String finalCouponRedeemedDate = null;
		if (fmtDate != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fmtDate);
			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH);
			final int day = cal.get(Calendar.DAY_OF_MONTH);
			final String strMonth = getMonthFromInt(month).substring(0, 3);
			final String dayPrefix = day < 10 ? MarketplacecommerceservicesConstants.ZERO
					: MarketplacecommerceservicesConstants.EMPTY;

			finalCouponRedeemedDate = strMonth + MarketplacecommerceservicesConstants.SINGLE_SPACE + dayPrefix + day
					+ MarketplacecommerceservicesConstants.SINGLE_SPACE + year;
		}
		return finalCouponRedeemedDate;
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
		final List<String> amountList = new ArrayList<String>();
		for (final Map.Entry<String, Collection<VoucherInvalidationModel>> voucherCodeInvalidationEntry : voucherCodeInvalidationMap
				.entrySet())// as per IQA comments for code sanitization
		{

			final String voucherCode = voucherCodeInvalidationEntry.getKey();
			final Collection<VoucherInvalidationModel> voucherInvalidationsCol = voucherCodeInvalidationEntry.getValue();

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
	 * @Description: This method restricts orders in last six months
	 * @param orderCreationDate
	 * @return boolean
	 */
	/*
	 * private boolean checkTransactionDateValidity(final Date orderCreationDate) { boolean isDateValid = false; if
	 * (orderCreationDate != null) { final Calendar endCalendar = Calendar.getInstance(); final Calendar startCalendar =
	 * Calendar.getInstance(); final SimpleDateFormat dateFormatforMONTH = new java.text.SimpleDateFormat(
	 * MarketplacecommerceservicesConstants.COUPONS_TXN_DATE_FORMAT);
	 * 
	 * endCalendar.setTime(new Date()); startCalendar.setTime(orderCreationDate);
	 * 
	 * final int endYear = endCalendar.get(Calendar.YEAR); final int endMonth =
	 * Integer.parseInt(dateFormatforMONTH.format(endCalendar.getTime())); final int endDay =
	 * endCalendar.get(Calendar.DAY_OF_MONTH);
	 * 
	 * final int startYear = startCalendar.get(Calendar.YEAR); final int startMonth =
	 * Integer.parseInt(dateFormatforMONTH.format(startCalendar.getTime())); final int startDay =
	 * startCalendar.get(Calendar.DAY_OF_MONTH);
	 * 
	 * final DateTime startDate = new DateTime().withDate(startYear, startMonth, startDay); final DateTime endDate = new
	 * DateTime().withDate(endYear, endMonth, endDay);
	 * 
	 * final Months monthsBetween = Months.monthsBetween(startDate, endDate); final int monthsBetweenInt =
	 * monthsBetween.getMonths();
	 * 
	 * if (monthsBetweenInt < 6) { isDateValid = true; } } return isDateValid; }
	 */

	/**
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)

	{
		final List<String> months = Arrays.asList(MarketplacecommerceservicesConstants.JANUARY,
				MarketplacecommerceservicesConstants.FEBRUARY, MarketplacecommerceservicesConstants.MARCH,
				MarketplacecommerceservicesConstants.APRIL, MarketplacecommerceservicesConstants.MAY,
				MarketplacecommerceservicesConstants.JUNE, MarketplacecommerceservicesConstants.JULY,
				MarketplacecommerceservicesConstants.AUGUST, MarketplacecommerceservicesConstants.SEPTEMBER,
				MarketplacecommerceservicesConstants.OCTOBER, MarketplacecommerceservicesConstants.NOVEMBER,
				MarketplacecommerceservicesConstants.DECEMBER);
		return months.get(month);

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
		getMplVoucherService().releaseVoucher(voucherCode, cartModel);
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
		return convertPageData(getMplCouponService().getClosedVoucher(customer, pageableData), getVoucherDisplayConverter());
	}

	/**
	 * This method returns all redeemed voucher data
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<CouponHistoryData>
	 * @throws VoucherOperationException
	 *
	 */

	@Override
	public SearchPageData<CouponHistoryData> getVoucherHistoryTransactions(final CustomerModel customer,
			final PageableData pageableData) throws VoucherOperationException
	{

		final SearchPageData<VoucherInvalidationModel> searchVoucherModel = getMplCouponService().getVoucherRedeemedOrder(customer,
				pageableData);
		final List<CouponHistoryData> couponOrderDataDTOListFinal = new ArrayList<CouponHistoryData>();
		final List<VoucherInvalidationModel> voucherInvalidationList = searchVoucherModel.getResults();
		final Map<Date, OrderData> orderDateMap = new TreeMap<Date, OrderData>(Collections.reverseOrder());
		final boolean isOrderDateValid = true;
		final Map<OrderData, VoucherData> orderVoucherMap = new HashMap<OrderData, VoucherData>();

		OrderData orderDetailsData = new OrderData();
		VoucherData voucherData = new VoucherData();
		if (CollectionUtils.isNotEmpty(voucherInvalidationList))
		{

			for (final VoucherInvalidationModel voucherInvalidation : voucherInvalidationList)
			{
				final OrderModel order = voucherInvalidation.getOrder();
				voucherData = getDefaultVoucherFacade().getVoucher(
						((PromotionVoucherModel) voucherInvalidation.getVoucher()).getVoucherCode());

				if (order.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PARENT))
				{
					final String orderCode = order.getCode();
					orderDetailsData = getMplCheckoutFacade().getOrderDetailsForCode(orderCode);
					//	isOrderDateValid = checkTransactionDateValidity(orderDetailsData.getCreated());// restrict orders to last six months only

					if (isOrderDateValid && null != voucherData)
					{
						orderDateMap.put(orderDetailsData.getCreated(), orderDetailsData);//mapping order with date such that the latest order is on top
						orderVoucherMap.put(orderDetailsData, voucherData);

					}

				}
			}
		}

		final SearchPageData<CouponHistoryData> searchPageDataVoucherHistory = convertPageData(searchVoucherModel,
				voucherTransactionConverter);
		final List<CouponHistoryData> couponOrderDataDTOList = searchPageDataVoucherHistory.getResults();
		final List<CouponHistoryData> couponHistoryDTOListFinal = sortcouponHistoryDTOList(orderDateMap, orderVoucherMap,
				couponOrderDataDTOList);

		for (final CouponHistoryData couponHistoryData : couponHistoryDTOListFinal)
		{
			if (null != couponHistoryData.getCouponCode() && null != couponHistoryData.getCouponDescription()
					&& null != couponHistoryData.getOrderCode())
			{
				couponOrderDataDTOListFinal.add(couponHistoryData);
			}
		}

		final SearchPageData<CouponHistoryData> searchPageDataVoucherHistoryFinal = new SearchPageData<CouponHistoryData>();
		searchPageDataVoucherHistoryFinal.setResults(couponOrderDataDTOListFinal);
		searchPageDataVoucherHistoryFinal.setPagination(searchVoucherModel.getPagination());
		searchPageDataVoucherHistoryFinal.setSorts(searchVoucherModel.getSorts());
		return searchPageDataVoucherHistoryFinal;

	}

	/**
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



	/**
	 * This method updates the paymentInfo set in session based on coupon calculation
	 *
	 * @param paymentInfo
	 * @param cartModel
	 */
	@Override
	public void updatePaymentInfoSession(final Map<String, Double> paymentInfo, final CartModel cartModel)
	{
		final Map<String, Double> updatedPaymentInfo = new HashMap<String, Double>();
		if (null != paymentInfo)
		{
			for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
			{
				if (null != entry.getKey() && !(MarketplacecommerceservicesConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					updatedPaymentInfo.put(entry.getKey(), cartModel.getTotalPriceWithConv());
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE, updatedPaymentInfo);
				}
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


	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
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



	/**
	 * @return the voucherDisplayConverter
	 */
	public Converter<VoucherModel, VoucherDisplayData> getVoucherDisplayConverter()
	{
		return voucherDisplayConverter;
	}



	/**
	 * @param voucherDisplayConverter
	 *           the voucherDisplayConverter to set
	 */
	public void setVoucherDisplayConverter(final Converter<VoucherModel, VoucherDisplayData> voucherDisplayConverter)
	{
		this.voucherDisplayConverter = voucherDisplayConverter;
	}



	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}



	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}
