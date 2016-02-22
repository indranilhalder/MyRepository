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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.impl.DefaultVoucherService;
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
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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

		final int couponCount = Integer.parseInt(getConfigurationService().getConfiguration().getString("coupon.display.topCount",
				"5"));
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

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			totalPrice += null != entry.getTotalPrice() ? entry.getTotalPrice().doubleValue() : 0.0D;
		}

		final double voucherDiscount = voucherModel.getAbsolute().booleanValue() ? (null != voucherModel.getValue() ? voucherModel
				.getValue().doubleValue() : 0.0d) : ((totalPrice * voucherModel.getValue().doubleValue()) / 100);

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
		voucherData.setVoucherCode(((PromotionVoucherModel) voucherModel).getVoucherCode());
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
					throw new VoucherOperationException("Voucher not found: " + voucherCode);
				}
				LOG.debug("Step 3:::Voucher Code is valid");

				//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
				final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
				if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
				{
					throw new VoucherOperationException("Voucher not found: " + voucherCode);
				}

				LOG.debug("Step 4:::Voucher is present and value is not negative");
				if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel)) //Checks whether voucher is applicable
				{
					LOG.debug("Step 5:::Voucher is not applicable");
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

				else if (!checkVoucherIsReservable(voucher, voucherCode, cartModel)) //Checks whether voucher is reservable
				{
					LOG.debug("Step 6:::Voucher is not reservable");
					throw new VoucherOperationException("Voucher is not reservable: " + voucherCode);
				}

				else
				{
					LOG.debug("Step 7:::Voucher can be redeemed");
					if (!getVoucherService().redeemVoucher(voucherCode, cartModel))
					{
						throw new VoucherOperationException("Error while applying voucher: " + voucherCode);
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
			LOG.error("ModelSavingException", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final JaloPriceFactoryException e)
		{
			LOG.error("JaloPriceFactoryException", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0018);
		}
		catch (final VoucherOperationException e)
		{
			//LOG.error("VoucherOperationException", e);
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//LOG.error("VoucherOperationException", e);
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error("Exception", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
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
	 * This method releases the voucher already applied in the cart automatically
	 *
	 * @param cart
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucherInCheckout(final CartModel cart) throws VoucherOperationException
	{
		final List<DiscountModel> discountList = cart.getDiscounts();

		if (CollectionUtils.isNotEmpty(discountList))
		{
			final String couponCode = ((PromotionVoucherModel) discountList.get(0)).getVoucherCode(); //Only 1 coupon can be applied
			try
			{
				releaseVoucher(couponCode, cart);
				recalculateCartForCoupon(cart);
			}
			catch (final VoucherOperationException e)
			{
				LOG.error("VoucherOperationException", e);
				throw e;
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error("EtailNonBusinessExceptions", e);
				throw e;
			}
			catch (final Exception e)
			{
				LOG.error("Exception", e);
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}

		}
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
	 * This method returns the coupon redeemed date
	 *
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
			final String dayPrefix = day < 10 ? "0" : "";

			finalCouponRedeemedDate = strMonth + SINGLE_SPACE + dayPrefix + day + SINGLE_SPACE + year;
		}
		return finalCouponRedeemedDate;
	}


	/**
	 *
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
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)

	{
		final List<String> months = Arrays.asList(JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER,
				NOVEMBER, DECEMBER);
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
	 * This method returns the order entries which are applicable for the voucher
	 *
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
	 * This method is used to release the voucher applied
	 *
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




}
