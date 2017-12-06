/**
 *
 */
package com.tisl.mpl.coupon.facade.impl;


import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercefacades.voucher.impl.DefaultVoucherFacade;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
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
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.NewCustomerRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.ChannelRestrictionModel;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.UnregisteredUserRestrictionModel;
import com.tisl.mpl.wsdto.OfferListWsData;
import com.tisl.mpl.wsdto.OfferResultWsData;


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
	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "voucherDisplayConverter")
	private Converter<VoucherModel, VoucherDisplayData> voucherDisplayConverter;

	@Resource(name = "voucherTransactionConverter")
	private Converter<VoucherInvalidationModel, CouponHistoryData> voucherTransactionConverter;
	@Resource(name = "voucherConverter")
	private Converter<VoucherModel, VoucherData> voucherConverter;

	private final static String COMMACONSTANT = ",";
	private final static String DOTCONSTANT = ".";




	/**
	 * This method recalculates the cart after redeeming/releasing the voucher
	 *
	 * @param cartModel
	 *
	 */
	@Override
	public void recalculateCartForCoupon(final CartModel cartModel, final OrderModel orderModel) throws EtailNonBusinessExceptions
	{
		getMplVoucherService().recalculateCartForCoupon(cartModel, orderModel); //OrderModel added for TPR-629
	}



	/**
	 * This method calculates the cart Values after redeeming or releasing the voucher
	 *
	 * @param orderModel
	 * @param cartModel
	 * @param couponStatus
	 * @param reddemIdentifier
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 *
	 */
	@Override
	public VoucherDiscountData calculateValues(final OrderModel orderModel, final CartModel cartModel, final boolean couponStatus,
			final boolean reddemIdentifier) throws VoucherOperationException //OrderModel added for TPR-629
	{
		LOG.debug("Calculating discounts after applying/releasing coupons based on redeemIdentifier=" + reddemIdentifier);
		double totalDiscount = 0.0d;

		//Set voucher details in VoucherDiscountData and return the data
		final VoucherDiscountData data = new VoucherDiscountData();

		//Calculate when cartModel is present
		if (null != cartModel)
		{
			//Find out all the vouchers for the selected cart
			final List<VoucherData> voucherDataList = getVouchersForAbstractOrder(null, cartModel);

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

			data.setVoucher(voucherDataList);
			data.setCouponDiscount(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));

		}
		//Calculate when orderModel is present
		else if (null != orderModel)
		{
			//Find out all the vouchers for the selected cart
			final List<VoucherData> voucherDataList = getVouchersForAbstractOrder(orderModel, null);

			for (final VoucherData voucher : voucherDataList)
			{
				//Check the global discount applied against the cart
				final List<DiscountValue> discount = orderModel.getGlobalDiscountValues();
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

			data.setVoucher(voucherDataList);
			data.setCouponDiscount(getMplCheckoutFacade().createPrice(orderModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(orderModel, orderModel.getTotalPriceWithConv()));
		}

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
		final List<VoucherDisplayData> voucherDataList = new ArrayList<VoucherDisplayData>();
		if (null != voucherList)
		{
			for (final VoucherModel voucherModel : voucherList)
			{
				if (voucherModel instanceof PromotionVoucherModel
						&& checkVoucherCanBeRedeemed(voucherModel, ((PromotionVoucherModel) voucherModel).getVoucherCode(), cart))
				{
					//sets voucher details in voucherDataList
					calculateVoucherDisplay(voucherModel, voucherDataList, cart);
				}
			}
		}

		//Sorts the voucherDataList based on coupon discount value
		getMplCouponService().getSortedVoucher(voucherDataList);//TODO remove assignment

		final int couponCount = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.COUPONTOPCOUNT, MarketplacecommerceservicesConstants.COUPONTOPCOUNTDEFVAL), 0);
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
	 */
	private void calculateVoucherDisplay(final VoucherModel voucherModel, final List<VoucherDisplayData> voucherDataList,
			final CartModel cartModel)
	{
		final List<AbstractOrderEntry> applicableOrderEntryList = getOrderEntriesFromVoucherEntries(voucherModel, cartModel);
		double totalPrice = 0.0D;
		double voucherDiscount = 0.0D;

		for (final AbstractOrderEntry entry : applicableOrderEntryList)
		{
			totalPrice += null == entry.getTotalPrice() ? 0.0D : entry.getTotalPrice().doubleValue();
		}

		if (null != voucherModel.getAbsolute() && voucherModel.getAbsolute().booleanValue())
		{
			voucherDiscount = voucherModel.getValue().doubleValue();
		}
		else
		{
			voucherDiscount = (totalPrice * voucherModel.getValue().doubleValue()) / 100;
		}

		setVoucherdata(voucherDiscount, voucherDataList, voucherModel);
	}

	/**
	 *
	 * This method sets the voucherData and returns a list
	 *
	 * @param voucherDiscount
	 * @param voucherDataList
	 * @param voucherModel
	 */
	private void setVoucherdata(final double voucherDiscount, final List<VoucherDisplayData> voucherDataList,
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

		//return voucherDataList;
	}

	/**
	 * Applies the voucher and returns true if successful
	 *
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@Override
	public boolean applyVoucher(final String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions //Added orderModel for TPR-629
	{
		boolean checkFlag = false;
		boolean applicabilityFlag = false;

		try
		{
			//Apply voucher for cartModel
			if (null != cartModel)
			{
				//if (CollectionUtils.isEmpty(cartModel.getDiscounts()))

				applicabilityFlag = mplCouponService.validateCartEligilityForCoupons(cartModel.getDiscounts());

				if (applicabilityFlag)
				{
					LOG.debug("Step 2:::No voucher is applied to cart");

					boolean isVoucherRedeemable = false;
					VoucherModel voucher = null;

					synchronized (cartModel)
					{
						//Checks if voucherCode is valid
						validateVoucherCodeParameter(voucherCode);
						if (!isVoucherCodeValid(voucherCode))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug("Step 3:::Voucher Code is valid");

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);

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
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.USER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDUSER + voucherCode);
							}
							/* TPR-1075 Changes Start */
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.NEWCUSTOMER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDNEWCUST + voucherCode);
							}
							/* TPR-1075 Changes End */
							//TPR-4460 Changes
							else if (null != error
									&& error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_MOBILE))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_MOBILE + voucherCode);
							}
							else if (null != error
									&& error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_WEB))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_WEB + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_CALLCENTRE + voucherCode);
							}
							else
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
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
							isVoucherRedeemable = getVoucherService().redeemVoucher(voucherCode, cartModel);
							if (!isVoucherRedeemable)
							{
								throw new VoucherOperationException(MarketplacecommerceservicesConstants.ERRORAPPLYVOUCHER + voucherCode);
							}
						}
					}

					if (isVoucherRedeemable)
					{
						recalculateCartForCoupon(cartModel, null); //Recalculates cart after applying voucher
						final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher,
								cartModel); //Finds applicable order entries

						//Important! Checking cart, if total amount <0, release this voucher
						checkVoucherApplicability(voucherCode, voucher, cartModel, null, applicableOrderEntryList);
						//apportioning
						setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);

						checkFlag = true;
					}
				}
			}
			//Apply voucher for orderModel
			else if (null != orderModel)
			{

				applicabilityFlag = mplCouponService.validateCartEligilityForCoupons(orderModel.getDiscounts());
				//if (CollectionUtils.isEmpty(orderModel.getDiscounts()))
				if (applicabilityFlag)
				{
					LOG.debug("Step 2:::No voucher is applied to cart");

					boolean isVoucherRedeemable = false;
					VoucherModel voucher = null;
					VoucherInvalidationModel voucherInvalidationModel = null;

					synchronized (orderModel)
					{
						//Checks if voucherCode is valid
						validateVoucherCodeParameter(voucherCode);
						if (!isVoucherCodeValid(voucherCode))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug("Step 3:::Voucher Code is valid");

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);
						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}

						LOG.debug("Step 4:::Voucher is present and value is not negative");
						if (!checkVoucherIsApplicable(voucher, voucherCode, orderModel)) //Checks whether voucher is applicable
						{
							LOG.debug("Step 5:::Voucher is not applicable");
							final String error = checkViolatedRestrictions(voucher, orderModel);
							if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.DATE))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.USER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDUSER + voucherCode);
							}
							else
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
							}
						}

						else if (!checkVoucherIsReservable(voucher, voucherCode, orderModel)) //Checks whether voucher is reservable
						{
							LOG.debug("Step 6:::Voucher is not reservable");
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug("Step 7:::Voucher can be redeemed");
							//							final VoucherInvalidationModel voucherInvalidationModel = getVoucherService().redeemVoucher(voucherCode,
							//									orderModel);
							voucherInvalidationModel = getVoucherService().redeemVoucher(voucherCode, orderModel);
							if (null != voucherInvalidationModel)
							{
								isVoucherRedeemable = true;
							}
							else
							{
								throw new VoucherOperationException(MarketplacecommerceservicesConstants.ERRORAPPLYVOUCHER + voucherCode);
							}

						}
					}
					if (isVoucherRedeemable)
					{
						recalculateCartForCoupon(null, orderModel); //Recalculates cart after applying voucher

						final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher,
								orderModel); //Finds applicable order entries

						//Important! Checking cart, if total amount <0, release this voucher
						final VoucherDiscountData discountData = checkVoucherApplicability(voucherCode, voucher, null, orderModel,
								applicableOrderEntryList);
						if (null != discountData)
						{
							voucherInvalidationModel.setSavedAmount(null != discountData.getCouponDiscount()
									? discountData.getCouponDiscount().getDoubleValue() : Double.valueOf(0.0D));
							getModelService().save(voucherInvalidationModel);
						}

						//apportioning
						setApportionedValueForVoucher(voucher, orderModel, voucherCode, applicableOrderEntryList);
						checkFlag = true;
					}
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
	 * @param orderModel
	 * @param applicableOrderEntryList
	 * @throws EtailNonBusinessExceptions
	 * @throws ModelSavingException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 */
	protected VoucherDiscountData checkVoucherApplicability(final String voucherCode, final VoucherModel lastVoucher,
			final CartModel cartModel, final OrderModel orderModel, final List<AbstractOrderEntryModel> applicableOrderEntryList)
			throws VoucherOperationException, EtailNonBusinessExceptions
	//Return changed and orderModel added as parameter to handle discountValue for TPR-629
	{
		VoucherDiscountData data = null;

		if (lastVoucher instanceof PromotionVoucherModel && !(lastVoucher instanceof MplCartOfferVoucherModel))
		{
			data = getMplVoucherService().checkCartAfterApply(lastVoucher, cartModel, orderModel, applicableOrderEntryList);
		}
		else if (lastVoucher instanceof MplCartOfferVoucherModel)
		{
			data = getMplVoucherService().checkCartAfterCartVoucherApply(lastVoucher, cartModel, orderModel,
					applicableOrderEntryList);
		}

		if (null != data && StringUtils.isNotEmpty(data.getRedeemErrorMsg()))
		{
			if (data.getRedeemErrorMsg().equalsIgnoreCase(MarketplacecommerceservicesConstants.EXCFREEBIE))
			{
				throw new VoucherOperationException(
						MarketplacecommerceservicesConstants.VOUCHER + voucherCode + MarketplacecommerceservicesConstants.FREEBIEERROR);
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
		return data;
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
	 * @param abstractOrderModel
	 * @return boolean
	 */
	protected boolean checkVoucherIsApplicable(final VoucherModel voucher, final String voucherCode,
			final AbstractOrderModel abstractOrderModel)
	{
		return getVoucherModelService().isApplicable(voucher, abstractOrderModel);
	}


	/**
	 * Checks if voucher is reservable for the cart
	 *
	 * @param voucher
	 * @param voucherCode
	 * @param abstractOrderModel
	 * @return boolean
	 */
	protected boolean checkVoucherIsReservable(final VoucherModel voucher, final String voucherCode,
			final AbstractOrderModel abstractOrderModel)
	{
		return getVoucherModelService().isReservable(voucher, voucherCode, abstractOrderModel);
	}



	/**
	 * This method returns whether date or user restriction is violated
	 *
	 * @param voucher
	 * @param abstractOrderModel
	 * @return the violated restriction
	 */
	protected String checkViolatedRestrictions(final VoucherModel voucher, final AbstractOrderModel abstractOrderModel)
	{
		final List<RestrictionModel> getViolatedRestrictions = getVoucherModelService().getViolatedRestrictions(voucher,
				abstractOrderModel);
		String error = null;
		for (final RestrictionModel restriction : getViolatedRestrictions)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.DATERESTVIOLATION);
				error = MarketplacecommerceservicesConstants.DATE;
				break;
			}
			//Unregisterd Users Restriction [TPR-1076]
			else if (restriction instanceof UserRestrictionModel || restriction instanceof UnregisteredUserRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.USERRESTVIOLATION);
				error = MarketplacecommerceservicesConstants.USER;
				break;
			}
			/* TPR-1075 Changes Start */
			else if (restriction instanceof NewCustomerRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.NEWUSERRESTVIOLATION);
				error = MarketplacecommerceservicesConstants.NEWCUSTOMER;
				break;
			}
			/* TPR-1075 Changes End */
			/* TPR-4460 Changes Start */
			else if (restriction instanceof ChannelRestrictionModel)
			{


				LOG.error(MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION);
				if (((ChannelRestrictionModel) restriction).getChannel().contains(SalesApplication.MOBILE))
				{
					error = MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_MOBILE;
				}
				else if (((ChannelRestrictionModel) restriction).getChannel().contains(SalesApplication.WEB))
				{
					error = MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_WEB;
				}
				else if (((ChannelRestrictionModel) restriction).getChannel().contains(SalesApplication.CALLCENTER))
				{
					error = MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER;
				}
			}
			/* TPR-4460 Changes End */
			else
			{
				continue;
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
	public boolean releaseVoucherInCheckout(final CartModel cart) throws VoucherOperationException, EtailNonBusinessExceptions
	{
		final List<DiscountModel> discountList = cart.getDiscounts();
		boolean recalculateRequired = false;
		try
		{
			if (CollectionUtils.isNotEmpty(discountList) && discountList.get(0) instanceof PromotionVoucherModel)
			{
				final String couponCode = ((PromotionVoucherModel) discountList.get(0)).getVoucherCode(); //Only 1 coupon can be applied
				getVoucherService().releaseVoucher(couponCode, cart); //Releases the voucher from the cart
				final List<AbstractOrderEntryModel> entryList = cart.getEntries();
				for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
				{
					entry.setCouponCode(MarketplacecommerceservicesConstants.EMPTY);
					entry.setCouponValue(Double.valueOf(0.00D));
				}
				if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
				{
					getModelService().saveAll(entryList);
				}

				//releaseVoucher(couponCode, cart); Commented as part of Performance fix TISPT-104
				//recalculateCartForCoupon(cart); Commented as part of Performance fix TISPT-104
				recalculateRequired = true; //TISPT-104
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in releaseVoucherInCheckout ", ex); //TISPT-104
			throw new EtailNonBusinessExceptions(ex);
		}

		return recalculateRequired;
	}

	/**
	 * @Description: This method returns list of all CouponTransactions corresponding to a specific customer to the
	 *               controller
	 * @param customer
	 * @return Map<String, Double>
	 * @throws VoucherOperationException
	 *
	 */

	@Override
	public Map<String, Double> getInvalidatedCouponCountSaved(final CustomerModel customer) throws VoucherOperationException
	{
		final Map<String, Double> result = getMplCouponService().getAllVoucherInvalidations(customer);
		return result;
	}


	/**
	 * @Description: For Apportioning of vouchers
	 * @param voucher
	 * @param abstractOrderModel
	 * @param voucherCode
	 */
	@Override
	public void setApportionedValueForVoucher(final VoucherModel voucher, final AbstractOrderModel abstractOrderModel,
			final String voucherCode, final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		if ((voucher instanceof PromotionVoucherModel) && !(voucher instanceof MplCartOfferVoucherModel))
		{
			getMplVoucherService().setApportionedValueForVoucher(voucher, abstractOrderModel, voucherCode, applicableOrderEntryList);
		}
		else if (voucher instanceof MplCartOfferVoucherModel)
		{
			getMplVoucherService().setApportionedValueForCartVoucher(voucher, abstractOrderModel, voucherCode,
					applicableOrderEntryList);
		}
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
	 * @param abstractOrderModel
	 * @return List<AbstractOrderEntryModel>
	 */
	private List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(final VoucherModel voucherModel,
			final AbstractOrderModel abstractOrderModel)
	{
		return getMplVoucherService().getOrderEntryModelFromVouEntries(voucherModel, abstractOrderModel);
	}



	/**
	 * @Description: This method is used to release the voucher applied
	 * @param voucherCode
	 * @throws VoucherOperationException
	 */
	@Override
	public void releaseVoucher(final String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException
	{
		getMplVoucherService().releaseVoucher(voucherCode, cartModel, orderModel);
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
	 * This method returns all offers
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherDisplayData>
	 *
	 */
	@Override
	public List<VoucherModel> getAllPaymentModeSpecificOffers()
	{
		return getMplCouponService().getPaymentModerelatedVouchers();

	}

	@Override
	public Map<String, Double> getPaymentModerelatedVoucherswithTotal()
	{
		return getMplCouponService().getPaymentModerelatedVoucherswithTotal();
	}



	@Override
	public OfferListWsData getAllOffersForMobile()
	{
		final OfferListWsData offerdata = new OfferListWsData();
		try
		{

			final List<OfferResultWsData> coupoonList = new ArrayList<OfferResultWsData>();

			final List<VoucherModel> allOffersData = getMplCouponService().getPaymentModerelatedVouchers();
			final Map<String, Double> allOffersTotalData = getMplCouponService().getPaymentModerelatedVoucherswithTotal();

			if (CollectionUtils.isNotEmpty(allOffersData))
			{
				for (final VoucherModel coupon : allOffersData)
				{
					final OfferResultWsData offer = new OfferResultWsData();
					offer.setOfferCode(coupon.getCode());
					offer.setOfferTitle(coupon.getName());
					offer.setOfferDescription(coupon.getDescription());
					offer.setOfferMaxDiscount(String.valueOf(coupon.getMaxDiscountValue()));

					for (final Map.Entry<String, Double> entry : allOffersTotalData.entrySet())
					{

						if (entry.getKey() != null)
						{
							if (entry.getKey().equalsIgnoreCase(String.valueOf(coupon.getPk())))
							{
								final String cartTotal = String.valueOf(entry.getValue());
								if (cartTotal != null)
								{
									offer.setOfferMinCartValue(cartTotal);
								}

							}

						}

					}

					coupoonList.add(offer);
				}
				offerdata.setCoupons(coupoonList);
				offerdata.setTotalOffers(String.valueOf(allOffersData.size()));
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			//ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions while getAllOffersForMobile ", e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions while getAllOffersForMobile ", e);

		}
		catch (final Exception e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
			//MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while getAllOffersForMobile ", e);

		}

		return offerdata;


	}

	/**
	 * This method returns all redeemed voucher data //TODO modify description
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

		SearchPageData<CouponHistoryData> searchPageDataVoucherHistory = null;
		if (null != searchVoucherModel)
		{
			searchPageDataVoucherHistory = convertPageData(searchVoucherModel, voucherTransactionConverter);

		}
		return searchPageDataVoucherHistory;
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
	 * @param abstractOrderModel
	 */
	@Override
	public void updatePaymentInfoSession(final Map<String, Double> paymentInfo, final AbstractOrderModel abstractOrderModel)
	{
		final Map<String, Double> updatedPaymentInfo = new HashMap<String, Double>();
		if (null != paymentInfo)
		{
			for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
			{
				if (null != entry.getKey() && !(MarketplacecommerceservicesConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					updatedPaymentInfo.put(entry.getKey(), abstractOrderModel.getTotalPriceWithConv());
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE, updatedPaymentInfo);
				}
			}
		}
	}





	/**
	 * Returns list of Voucher Data corresponding to abstractOrderModel. Added for TPR-629
	 *
	 * @param orderModel
	 * @param cartModel
	 * @return List<VoucherData>
	 * @throws VoucherOperationException
	 */
	private List<VoucherData> getVouchersForAbstractOrder(final OrderModel orderModel, final CartModel cartModel)
			throws VoucherOperationException
	{
		final List<VoucherData> vouchersData = new ArrayList<VoucherData>();
		Collection<String> voucherCodes = new ArrayList<String>();
		if (cartModel != null)
		{
			voucherCodes = getVoucherService().getAppliedVoucherCodes(cartModel);

		}
		else if (null != orderModel)
		{
			voucherCodes = getVoucherService().getAppliedVoucherCodes(orderModel);
		}

		for (final String code : voucherCodes)
		{
			vouchersData.add(getSingleVouchersByCode(code));
		}
		return vouchersData;

	}



	//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON starts here

	/**
	 * This method returns coupon message based on voucherCode for TPR-4461
	 *
	 * @param orderModel
	 * @return String
	 */

	@Override
	public String getCouponMessageInfo(final AbstractOrderModel orderModel)
	{
		String couponMessageInformation = null;
		final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
				getVoucherService().getAppliedVouchers(orderModel));

		if (CollectionUtils.isNotEmpty(voucherList))
		{
			VoucherModel appliedVoucher = null;

			final DiscountModel discount = voucherList.get(0);

			if (discount instanceof PromotionVoucherModel)//Redundant null check removed for Sonar Fix
			{
				final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
				appliedVoucher = promotionVoucherModel;

				final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
				for (final RestrictionModel restriction : restrictions)
				{
					if (restriction instanceof PaymentModeRestrictionModel)
					{
						final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction).getPaymentTypeData(); //Voucher Payment mode
						final List<BankModel> bankLists = ((PaymentModeRestrictionModel) restriction).getBanks(); //Voucher Bank Restriction List
						final StringBuilder sb = new StringBuilder();


						if (CollectionUtils.isNotEmpty(paymentTypeList))
						{
							final String messagePaymentMode = "This coupon code can be used on payments made by ";
							sb.append(messagePaymentMode);
							for (final PaymentTypeModel paymentType : paymentTypeList)
							{
								//sb.append(paymentType.getMode()).append(",");//SonarFix
								sb.append(paymentType.getMode()).append(COMMACONSTANT);
							}
							sb.deleteCharAt(sb.lastIndexOf(","));
							//sb.append(".");//SonarFix
							//sb.append(DOTCONSTANT);
						}

						if (CollectionUtils.isNotEmpty(bankLists))
						{
							final String messageVoucherBank = " via ";
							sb.append(messageVoucherBank);
							for (final BankModel bank : bankLists)
							{
								//sb.append(bank.getBankName()).append(",");//Sonarfix
								sb.append(bank.getBankName()).append(COMMACONSTANT);
							}
							sb.deleteCharAt(sb.lastIndexOf(","));
							//sb.append(".");//SonarFix
							sb.append(DOTCONSTANT);
						}

						couponMessageInformation = sb.toString();
					}
				}
			}
		}

		return couponMessageInformation;
	}


	//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON ends here



	/**
	 * This method returns voucher data based on voucherCode for TPR-629
	 *
	 * @param voucherCode
	 * @return VoucherData
	 * @throws VoucherOperationException
	 */
	protected VoucherData getSingleVouchersByCode(final String voucherCode) throws VoucherOperationException
	{
		final VoucherModel voucherModel = getVoucherService().getVoucher(voucherCode);
		final VoucherData voucherData = getVoucherConverter().convert(voucherModel);
		return voucherData;
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
	 * @return the voucherConverter
	 */
	public Converter<VoucherModel, VoucherData> getVoucherConverter()
	{
		return voucherConverter;
	}



	/**
	 * @param voucherConverter
	 *           the voucherConverter to set
	 */
	public void setVoucherConverter(final Converter<VoucherModel, VoucherData> voucherConverter)
	{
		this.voucherConverter = voucherConverter;
	}



	/**
	 * This method is invoked for Voucher Applicability
	 *
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 * @throws VoucherOperationException,EtailNonBusinessExceptions
	 */
	@Override
	public boolean applyCartVoucher(final String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions
	{

		boolean checkFlag = false;
		boolean applicabilityFlag = false;

		try
		{
			//Apply voucher for cartModel
			if (null != cartModel)
			{
				applicabilityFlag = mplCouponService.validateCartEligilityForCartCoupons(cartModel.getDiscounts());

				if (applicabilityFlag)
				{
					LOG.debug("Step 2:::No voucher is applied to cart");

					boolean isVoucherRedeemable = false;
					VoucherModel voucher = null;

					synchronized (cartModel)
					{
						//Checks if voucherCode is valid
						validateVoucherCodeParameter(voucherCode);
						if (!isVoucherCodeValid(voucherCode))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug("Step 3:::Voucher Code is valid");

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);

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
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.USER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDUSER + voucherCode);
							}
							/* TPR-1075 Changes Start */
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.NEWCUSTOMER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDNEWCUST + voucherCode);
							}
							/* TPR-1075 Changes End */
							//TPR-4460 Changes
							else if (null != error
									&& error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_MOBILE))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_MOBILE + voucherCode);
							}
							else if (null != error
									&& error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_RESTRICTION_WEB))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_WEB + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.CHANNELRESTVIOLATION_CALLCENTRE + voucherCode);
							}
							else
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
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
							isVoucherRedeemable = getVoucherService().redeemVoucher(voucherCode, cartModel);
							if (!isVoucherRedeemable)
							{
								throw new VoucherOperationException(MarketplacecommerceservicesConstants.ERRORAPPLYVOUCHER + voucherCode);
							}
						}
					}

					if (isVoucherRedeemable)
					{
						recalculateCartForCoupon(cartModel, null); //Recalculates cart after applying voucher
						final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher,
								cartModel); //Finds applicable order entries

						//Important! Checking cart, if total amount <0, release this voucher
						checkVoucherApplicability(voucherCode, voucher, cartModel, null, applicableOrderEntryList);
						//apportioning
						setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);

						checkFlag = true;
					}
				}
			}
			//Apply voucher for orderModel
			else if (null != orderModel)
			{
				applicabilityFlag = mplCouponService.validateCartEligilityForCartCoupons(orderModel.getDiscounts());
				if (applicabilityFlag)
				{
					LOG.debug("Step 2:::No voucher is applied to cart");

					boolean isVoucherRedeemable = false;
					VoucherModel voucher = null;
					VoucherInvalidationModel voucherInvalidationModel = null;

					synchronized (orderModel)
					{
						//Checks if voucherCode is valid
						validateVoucherCodeParameter(voucherCode);
						if (!isVoucherCodeValid(voucherCode))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug("Step 3:::Voucher Code is valid");

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);
						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}

						LOG.debug("Step 4:::Voucher is present and value is not negative");
						if (!checkVoucherIsApplicable(voucher, voucherCode, orderModel)) //Checks whether voucher is applicable
						{
							LOG.debug("Step 5:::Voucher is not applicable");
							final String error = checkViolatedRestrictions(voucher, orderModel);
							if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.DATE))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.USER))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINVALIDUSER + voucherCode);
							}
							else
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERINAPPLICABLE + voucherCode);
							}
						}

						else if (!checkVoucherIsReservable(voucher, voucherCode, orderModel)) //Checks whether voucher is reservable
						{
							LOG.debug("Step 6:::Voucher is not reservable");
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug("Step 7:::Voucher can be redeemed");
							//							final VoucherInvalidationModel voucherInvalidationModel = getVoucherService().redeemVoucher(voucherCode,
							//									orderModel);
							voucherInvalidationModel = getVoucherService().redeemVoucher(voucherCode, orderModel);
							if (null != voucherInvalidationModel)
							{
								isVoucherRedeemable = true;
							}
							else
							{
								throw new VoucherOperationException(MarketplacecommerceservicesConstants.ERRORAPPLYVOUCHER + voucherCode);
							}

						}
					}
					if (isVoucherRedeemable)
					{
						recalculateCartForCoupon(null, orderModel);

						final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher,
								orderModel);

						//Important! Checking cart, if total amount <0, release this voucher
						final VoucherDiscountData discountData = checkVoucherApplicability(voucherCode, voucher, null, orderModel,
								applicableOrderEntryList);
						if (null != discountData)
						{
							voucherInvalidationModel.setSavedAmount(null != discountData.getCouponDiscount()
									? discountData.getCouponDiscount().getDoubleValue() : Double.valueOf(0.0D));
							getModelService().save(voucherInvalidationModel);
						}

						//apportioning
						setApportionedValueForVoucher(voucher, orderModel, voucherCode, applicableOrderEntryList);
						checkFlag = true;
					}
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
	 * The Method is used to display Coupon Details on Payment Page
	 *
	 * @param orderModel
	 * @param cartModel
	 * @param couponRedStatus
	 * @param isRedeemed
	 * @param couponCode
	 */
	@Override
	public VoucherDiscountData populateCartVoucherData(final OrderModel orderModel, final CartModel cartModel,
			final boolean couponRedStatus, final boolean isRedeemed, final String couponCode)
	{
		LOG.debug("Populating Data class for Cart offer >>> Coupon Redeem Status" + isRedeemed);
		final VoucherDiscountData data = new VoucherDiscountData();
		double totalDiscount = 0.0;
		if (null != cartModel)
		{
			if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				for (final AbstractOrderEntryModel oModel : cartModel.getEntries())
				{

					final Double cartCouponValue = oModel.getCartCouponValue();
					final Double totalproductLevelDisc = oModel.getTotalProductLevelDisc();
					final Double totalcartLevelDisc = oModel.getCartLevelDisc();

					totalDiscount += (null == totalproductLevelDisc ? 0.0d : totalproductLevelDisc.doubleValue())
							+ (null == totalcartLevelDisc ? 0.0d : totalcartLevelDisc.doubleValue())
							+ (null == cartCouponValue ? 0.0d : cartCouponValue.doubleValue());

				}
			}
			data.setCouponDiscount(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
		}
		else if (null != orderModel)
		{
			for (final AbstractOrderEntryModel oModel : orderModel.getEntries())
			{

				final Double cartCouponValue = oModel.getCartCouponValue();
				final Double totalproductLevelDisc = oModel.getTotalProductLevelDisc();
				final Double totalcartLevelDisc = oModel.getCartLevelDisc();

				totalDiscount += (null == totalproductLevelDisc ? 0.0d : totalproductLevelDisc.doubleValue())
						+ (null == totalcartLevelDisc ? 0.0d : totalcartLevelDisc.doubleValue())
						+ (null == cartCouponValue ? 0.0d : cartCouponValue.doubleValue());

			}

			data.setCouponDiscount(getMplCheckoutFacade().createPrice(orderModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(orderModel, orderModel.getTotalPriceWithConv()));
		}


		// Generic Code

		if (isRedeemed)
		{
			data.setCouponRedeemed(isRedeemed);
		}
		else
		{
			data.setCouponReleased(isRedeemed);
		}
		if (!isRedeemed)
		{
			data.setRedeemErrorMsg(MarketplacecommerceservicesConstants.COUPONREDEEMERROR);
		}

		return data;
	}








}
