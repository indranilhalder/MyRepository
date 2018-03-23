/**
 *
 */
package com.tisl.mpl.coupon.facade.impl;


import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
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
import de.hybris.platform.promotions.util.Tuple2;
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
import de.hybris.platform.voucher.model.OrderRestrictionModel;
import de.hybris.platform.voucher.model.ProductCategoryRestrictionModel;
import de.hybris.platform.voucher.model.ProductRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cache.strategy.MplDisplayCouponCachingStrategy;
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
import com.tisl.mpl.model.MplOrderRestrictionModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerRestrictionModel;
import com.tisl.mpl.model.UnregisteredUserRestrictionModel;
import com.tisl.mpl.wsdto.MplFinalVisibleCouponsDTO;
import com.tisl.mpl.wsdto.MplVisibleCouponsDTO;
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

	@Autowired
	private MplDisplayCouponCachingStrategy mplDisplayCouponCachingStrategy;
	@Autowired
	private PriceDataFactory priceDataFactory;

	private final static String COMMACONSTANT = ",";
	private final static String DOTCONSTANT = ".";
	private final static String STEP2 = "Step 2:::No voucher is applied to cart";
	private final static String STEP3 = "Step 3:::Voucher Code is valid";
	private final static String STEP4 = "Step 4:::Voucher is present and value is not negative";
	private final static String STEP5 = "Step 5:::Voucher is not applicable";
	private final static String STEP6 = "Step 6:::Voucher is not reservable";
	private final static String STEP7 = "Step 7:::Voucher can be redeemed";

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
			getModelService().refresh(cartModel);
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
			getModelService().refresh(orderModel);
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
	public boolean applyVoucher(final String voucherCode, CartModel cartModel, OrderModel orderModel)
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
					LOG.debug(STEP2);

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
						LOG.debug(STEP3);

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);

						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug(STEP4);
						if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel)) //Checks whether voucher is applicable
						{
							LOG.debug(STEP5);
							final String error = checkViolatedRestrictions(voucher, cartModel);
							if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.DATE))
							{
								throw new VoucherOperationException(
										MarketplacecommerceservicesConstants.VOUCHERNOTREDEEMABLE + voucherCode);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.SELLERVIOLATION))
							{
								throw new VoucherOperationException(error);
							}
							else if (null != error && error.equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDERTHRESHOLD))
							{
								throw new VoucherOperationException(error);
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
							LOG.debug(STEP6);
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug(STEP7);
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
						cartModel = (CartModel) getMplVoucherService().getUpdatedDiscountValues(cartModel, voucher);


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
					LOG.debug(STEP2);

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
						LOG.debug(STEP3);

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);
						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}

						LOG.debug(STEP4);
						if (!checkVoucherIsApplicable(voucher, voucherCode, orderModel)) //Checks whether voucher is applicable
						{
							LOG.debug(STEP5);
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
							LOG.debug(STEP6);
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug(STEP7);
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
						orderModel = (OrderModel) getMplVoucherService().getUpdatedDiscountValues(orderModel, voucher);

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
			else if (restriction instanceof SellerRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.SELLERVIOLATION);
				error = MarketplacecommerceservicesConstants.SELLERVIOLATION;
				break;
			}
			else if (restriction instanceof ProductCategoryRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.SELLERVIOLATION);
				error = MarketplacecommerceservicesConstants.SELLERVIOLATION;
				break;
			}
			else if (restriction instanceof ProductRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.SELLERVIOLATION);
				error = MarketplacecommerceservicesConstants.SELLERVIOLATION;
				break;
			}
			else if (restriction instanceof OrderRestrictionModel)
			{
				LOG.error(MarketplacecommerceservicesConstants.ORDERRESTRICTION);
				error = MarketplacecommerceservicesConstants.ORDERTHRESHOLD;
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
			//			if (CollectionUtils.isNotEmpty(discountList) && discountList.get(0) instanceof PromotionVoucherModel)
			//			{
			//				final String couponCode = ((PromotionVoucherModel) discountList.get(0)).getVoucherCode(); //Only 1 coupon can be applied
			//				getVoucherService().releaseVoucher(couponCode, cart); //Releases the voucher from the cart
			//				final List<AbstractOrderEntryModel> entryList = cart.getEntries();
			//				for (final AbstractOrderEntryModel entry : entryList)//Resets the coupon details against the entries
			//				{
			//					entry.setCouponCode(MarketplacecommerceservicesConstants.EMPTY);
			//					entry.setCouponValue(Double.valueOf(0.00D));
			//				}
			//				if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
			//				{
			//					getModelService().saveAll(entryList);
			//				}
			//
			//				//releaseVoucher(couponCode, cart); Commented as part of Performance fix TISPT-104
			//				//recalculateCartForCoupon(cart); Commented as part of Performance fix TISPT-104
			//				recalculateRequired = true; //TISPT-104
			//			}


			if (CollectionUtils.isNotEmpty(discountList))
			{
				for (final DiscountModel oModel : discountList)
				{
					if (oModel instanceof PromotionVoucherModel && !(oModel instanceof MplCartOfferVoucherModel))
					{
						final PromotionVoucherModel coupon = (PromotionVoucherModel) oModel;
						getVoucherService().releaseVoucher(coupon.getVoucherCode(), cart);
						final List<AbstractOrderEntryModel> entryList = cart.getEntries();
						if (CollectionUtils.isNotEmpty(entryList))
						{
							for (final AbstractOrderEntryModel entry : entryList)
							{
								entry.setCouponCode(MarketplacecommerceservicesConstants.EMPTY);
								entry.setCouponValue(Double.valueOf(0.00D));

								entry.setCouponCostCentreOnePercentage(Double.valueOf(0.00D));
								entry.setCouponCostCentreTwoPercentage(Double.valueOf(0.00D));
								entry.setCouponCostCentreThreePercentage(Double.valueOf(0.00D));
							}
							getModelService().saveAll(entryList);
						}
					}

					else if (oModel instanceof MplCartOfferVoucherModel)
					{

						final MplCartOfferVoucherModel coupon = (MplCartOfferVoucherModel) oModel;
						getVoucherService().releaseVoucher(coupon.getVoucherCode(), cart);
						final List<AbstractOrderEntryModel> entryList = cart.getEntries();
						if (CollectionUtils.isNotEmpty(entryList))
						{
							for (final AbstractOrderEntryModel entry : entryList)
							{
								entry.setCartCouponCode(MarketplacecommerceservicesConstants.EMPTY);
								entry.setCartCouponValue(Double.valueOf(0.00D));

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
						}

					}


				}


				recalculateRequired = true;
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
	public List<MplCartOfferVoucherModel> getAllPaymentModeSpecificOffers()
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

			final List<MplCartOfferVoucherModel> allOffersData = getMplCouponService().getPaymentModerelatedVouchers();
			final Map<String, Double> allOffersTotalData = getMplCouponService().getPaymentModerelatedVoucherswithTotal();

			if (CollectionUtils.isNotEmpty(allOffersData))
			{
				for (final MplCartOfferVoucherModel coupon : allOffersData)
				{
					final OfferResultWsData offer = new OfferResultWsData();
					offer.setOfferCode(coupon.getCode());
					offer.setOfferTitle(coupon.getName());
					offer.setOfferDescription(coupon.getDescription());
					//TISPRDT-7921
					if ((coupon.getMaxDiscountValue() != null))
					{
						offer.setOfferMaxDiscount(String.valueOf(coupon.getMaxDiscountValue()));

					}
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

	@Override
	public OfferListWsData getAllOffersTermsAndConditionForMobile()
	{
		final OfferListWsData offerdata = new OfferListWsData();
		try
		{

			final List<OfferResultWsData> coupoonList = new ArrayList<OfferResultWsData>();

			final List<MplCartOfferVoucherModel> allOffersData = getMplCouponService().getPaymentModerelatedVouchers();

			if (CollectionUtils.isNotEmpty(allOffersData))
			{
				for (final MplCartOfferVoucherModel coupon : allOffersData)
				{
					final OfferResultWsData offer = new OfferResultWsData();
					offer.setOfferTitle(coupon.getName());
					offer.setOfferDescription(coupon.getDescription());
					offer.setOfferTermsConditions(coupon.getTermsAndCondition());
					coupoonList.add(offer);
				}
				offerdata.setCoupons(coupoonList);
				offerdata.setTotalOffers(String.valueOf(allOffersData.size()));
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			//ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions while getAllOffersTermsAndConditionForMobile ", e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions while getAllOffersTermsAndConditionForMobile ", e);

		}
		catch (final Exception e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
			//MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while getAllOffersTermsAndConditionForMobile ", e);

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
	 * @throws VoucherOperationException
	 *            ,EtailNonBusinessExceptions
	 */
	@Override
	public boolean applyCartVoucher(final String voucherCode, CartModel cartModel, OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions
	{

		boolean checkFlag = false;
		boolean applicabilityFlag = false;

		try
		{
			//Apply voucher for cartModel
			if (null != cartModel)
			{
				if (cartModel.getSplitModeInfo().equalsIgnoreCase("CliqCash"))
				{
					return checkFlag;
				}

				applicabilityFlag = mplCouponService.validateCartEligilityForCartCoupons(cartModel.getDiscounts());

				if (applicabilityFlag)
				{
					LOG.debug(STEP2);

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
						LOG.debug(STEP3);

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);

						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}
						LOG.debug(STEP4);
						if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel)) //Checks whether voucher is applicable
						{
							LOG.debug(STEP5);
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
							LOG.debug(STEP6);
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug(STEP7);
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
						cartModel = (CartModel) getMplVoucherService().getUpdatedCartDiscountValues(cartModel, voucher);

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
				if (orderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash"))
				{
					return checkFlag;
				}

				applicabilityFlag = mplCouponService.validateCartEligilityForCartCoupons(orderModel.getDiscounts());
				if (applicabilityFlag)
				{
					LOG.debug(STEP2);

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
						LOG.debug(STEP3);

						//Finds voucherModel for the code and checks whether it is null or voucher discount value is less than 0
						//						final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
						voucher = getVoucherService().getVoucher(voucherCode);
						if (voucher == null || (null != voucher.getValue() && voucher.getValue().doubleValue() <= 0))
						{
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTFOUND + voucherCode);
						}

						LOG.debug(STEP4);
						if (!checkVoucherIsApplicable(voucher, voucherCode, orderModel)) //Checks whether voucher is applicable
						{
							LOG.debug(STEP5);
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
							LOG.debug(STEP6);
							throw new VoucherOperationException(MarketplacecommerceservicesConstants.VOUCHERNOTRESERVABLE + voucherCode);
						}

						else
						{
							LOG.debug(STEP7);
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
						orderModel = (OrderModel) getMplVoucherService().getUpdatedCartDiscountValues(orderModel, voucher);

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
		double mobileTotalDiscount = 0.0;
		//final double couponDiscount = 0.0;
		//final double totalMRP = 0.0;

		if (null != cartModel)
		{
			getModelService().refresh(cartModel);

			if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				for (final AbstractOrderEntryModel oModel : cartModel.getEntries())
				{
					if (!oModel.getGiveAway().booleanValue())
					{
						final Double mrp = oModel.getMrp();
						final Double cartDiscount = (null != oModel.getCartLevelDisc() && oModel.getCartLevelDisc().doubleValue() > 0)
								? oModel.getCartLevelDisc() : Double.valueOf(0);
						final Double couponDiscount = (null != oModel.getCartCouponValue()
								&& oModel.getCartCouponValue().doubleValue() > 0) ? oModel.getCartCouponValue() : Double.valueOf(0);
						final Double totalPrice = oModel.getTotalPrice();
						final int quantity = oModel.getQuantity().intValue();

						final Double productDiscount = (null != oModel.getTotalProductLevelDisc()
								&& oModel.getTotalProductLevelDisc().doubleValue() > 0) ? oModel.getTotalProductLevelDisc()
										: Double.valueOf(0);

						final double value = (mrp.doubleValue() * quantity) - totalPrice.doubleValue();

						totalDiscount += value + cartDiscount.doubleValue() + couponDiscount.doubleValue();

						mobileTotalDiscount += productDiscount.doubleValue() + cartDiscount.doubleValue()
								+ couponDiscount.doubleValue();
					}
				}
			}

			data.setCouponDiscount(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
			data.setTotalDiscount(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(totalDiscount)));

			data.setMbDiscountAftrCVoucher(getMplCheckoutFacade().createPrice(cartModel, Double.valueOf(mobileTotalDiscount)));
		}
		else if (null != orderModel)
		{
			getModelService().refresh(orderModel);

			if (CollectionUtils.isNotEmpty(orderModel.getEntries()))
			{
				for (final AbstractOrderEntryModel oModel : orderModel.getEntries())
				{
					if (!oModel.getGiveAway().booleanValue())
					{
						final Double mrp = oModel.getMrp();
						final Double cartDiscount = (null != oModel.getCartLevelDisc() && oModel.getCartLevelDisc().doubleValue() > 0)
								? oModel.getCartLevelDisc() : Double.valueOf(0);
						final Double couponDiscount = (null != oModel.getCartCouponValue()
								&& oModel.getCartCouponValue().doubleValue() > 0) ? oModel.getCartCouponValue() : Double.valueOf(0);
						final Double totalPrice = oModel.getTotalPrice();
						final int quantity = oModel.getQuantity().intValue();

						final Double productDiscount = (null != oModel.getTotalProductLevelDisc()
								&& oModel.getTotalProductLevelDisc().doubleValue() > 0) ? oModel.getTotalProductLevelDisc()
										: Double.valueOf(0);

						final double value = (mrp.doubleValue() * quantity) - totalPrice.doubleValue();

						totalDiscount += value + cartDiscount.doubleValue() + couponDiscount.doubleValue();

						mobileTotalDiscount += productDiscount.doubleValue() + cartDiscount.doubleValue()
								+ couponDiscount.doubleValue();
					}

				}
			}

			data.setCouponDiscount(getMplCheckoutFacade().createPrice(orderModel, Double.valueOf(totalDiscount)));
			data.setTotalPrice(getMplCheckoutFacade().createPrice(orderModel, orderModel.getTotalPriceWithConv()));
			data.setTotalDiscount(getMplCheckoutFacade().createPrice(orderModel, Double.valueOf(totalDiscount)));

			data.setMbDiscountAftrCVoucher(getMplCheckoutFacade().createPrice(orderModel, Double.valueOf(mobileTotalDiscount)));
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




	@Override
	public String getCouponCode(final String manuallyselectedvoucher)
	{
		return getMplCouponService().getVoucherCode(manuallyselectedvoucher);
	}



	/**
	 * The method removes the earlier applied offers
	 *
	 * @param oModel
	 */
	@Override
	public AbstractOrderModel removeLastCartCoupon(final AbstractOrderModel oModel)
	{
		final List<DiscountModel> discountList = oModel.getDiscounts();
		boolean isPresent = false;
		MplCartOfferVoucherModel voucher = null;

		double productPrice = 0.0;
		String voucherCode = MarketplacecommerceservicesConstants.EMPTY;


		try
		{
			if (CollectionUtils.isNotEmpty(discountList))
			{
				for (final DiscountModel discount : discountList)
				{
					if ((discount instanceof PromotionVoucherModel) && (discount instanceof MplCartOfferVoucherModel))
					{
						isPresent = true;
						voucher = (MplCartOfferVoucherModel) discount;
						voucherCode = voucher.getVoucherCode();
						break;
					}
				}
			}


			if (isPresent)
			{
				if (oModel instanceof CartModel)
				{
					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, oModel);

					productPrice = getTotalProductPrice(applicableOrderEntryList);

					getMplVoucherService().releaseCartVoucherAfterCheck((CartModel) oModel, null, voucherCode,
							Double.valueOf(productPrice), applicableOrderEntryList, discountList);
				}
				else if (oModel instanceof OrderModel)
				{
					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, oModel);

					productPrice = getTotalProductPrice(applicableOrderEntryList);

					getMplVoucherService().releaseCartVoucherAfterCheck(null, (OrderModel) oModel, voucherCode,
							Double.valueOf(productPrice), applicableOrderEntryList, discountList);
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.debug("Exception" + exception.getMessage());
		}

		getModelService().refresh(oModel);

		return oModel;
	}




	private double getTotalProductPrice(final List<AbstractOrderEntryModel> applicableOrderEntryList)
	{
		double productPrice = 0.0;

		if (CollectionUtils.isNotEmpty(applicableOrderEntryList))
		{
			for (final AbstractOrderEntryModel entry : applicableOrderEntryList)
			{
				productPrice += (null == entry.getTotalPrice()) ? 0.0d : entry.getTotalPrice().doubleValue();
			}
		}
		return productPrice;
	}



	/**
	 * Code to Remove Cart Voucher
	 *
	 * @param oModel
	 */
	@Override
	public AbstractOrderModel removeCartCoupon(final AbstractOrderModel oModel)
	{
		final List<DiscountModel> discountList = oModel.getDiscounts();
		boolean isPresent = false;
		MplCartOfferVoucherModel voucher = null;

		double productPrice = 0.0;
		String voucherCode = MarketplacecommerceservicesConstants.EMPTY;

		boolean isUserCouponPresent = false;


		try
		{
			if (CollectionUtils.isNotEmpty(discountList))
			{
				for (final DiscountModel discount : discountList)
				{
					if ((discount instanceof PromotionVoucherModel) && (discount instanceof MplCartOfferVoucherModel))
					{
						isPresent = true;
						voucher = (MplCartOfferVoucherModel) discount;
						voucherCode = voucher.getVoucherCode();
						break;
					}
				}
			}


			if (isPresent)
			{
				if (oModel instanceof CartModel)
				{
					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, oModel);

					productPrice = getTotalProductPrice(applicableOrderEntryList);

					getMplVoucherService().releaseCartVoucherAfterCheck((CartModel) oModel, null, voucherCode,
							Double.valueOf(productPrice), applicableOrderEntryList, discountList);

					getModelService().refresh(oModel);

					final Tuple2<Boolean, VoucherModel> couponObj = isUserCouponPresent(oModel.getDiscounts());
					isUserCouponPresent = couponObj.getFirst().booleanValue();
					if (isUserCouponPresent)
					{
						CartModel cartModel = (CartModel) oModel;
						final PromotionVoucherModel coupon = (PromotionVoucherModel) couponObj.getSecond();
						cartModel = (CartModel) getMplVoucherService().modifyDiscountValues(cartModel, couponObj.getSecond());
						final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(couponObj.getSecond(),
								cartModel);
						//checkVoucherApplicability(coupon.getCode(), couponObj.getSecond(), cartModel, null, entryList);
						setApportionedValueForVoucher(couponObj.getSecond(), cartModel, coupon.getVoucherCode(), entryList);
					}
				}
				else if (oModel instanceof OrderModel)
				{
					final List<AbstractOrderEntryModel> applicableOrderEntryList = getOrderEntryModelFromVouEntries(voucher, oModel);

					productPrice = getTotalProductPrice(applicableOrderEntryList);

					getMplVoucherService().releaseCartVoucherAfterCheck(null, (OrderModel) oModel, voucherCode,
							Double.valueOf(productPrice), applicableOrderEntryList, discountList);

					getModelService().refresh(oModel);
					final Tuple2<Boolean, VoucherModel> couponObj = isUserCouponPresent(oModel.getDiscounts());
					isUserCouponPresent = couponObj.getFirst().booleanValue();
					if (isUserCouponPresent)
					{
						OrderModel orderModel = (OrderModel) oModel;
						final PromotionVoucherModel coupon = (PromotionVoucherModel) couponObj.getSecond();

						orderModel = (OrderModel) getMplVoucherService().modifyDiscountValues(orderModel, couponObj.getSecond());
						final List<AbstractOrderEntryModel> entryList = getOrderEntryModelFromVouEntries(couponObj.getSecond(),
								orderModel);
						//checkVoucherApplicability(voucherCode, couponObj.getSecond(), null, orderModel, entryList);
						setApportionedValueForVoucher(couponObj.getSecond(), orderModel, coupon.getVoucherCode(), entryList);
					}
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.debug("Exception" + exception.getMessage());
		}

		getModelService().refresh(oModel);

		return oModel;
	}



	/**
	 * Check for User Coupon
	 *
	 * @param discountList
	 * @return isPresent
	 */
	private Tuple2<Boolean, VoucherModel> isUserCouponPresent(final List<DiscountModel> discountList)
	{
		boolean isPresent = false;
		VoucherModel oModel = null;

		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountModel discount : discountList)
			{
				if ((discount instanceof PromotionVoucherModel) && !(discount instanceof MplCartOfferVoucherModel))
				{
					isPresent = true;
					oModel = (PromotionVoucherModel) discount;
					break;
				}
			}
		}

		final Tuple2<Boolean, VoucherModel> cartCouponObj = new Tuple2(Boolean.valueOf(isPresent), oModel);

		return cartCouponObj;
	}



	/**
	 * The Method returns Coupon Details to be displayed on Cart Page
	 *
	 * @param cartGuid
	 * @param currentCustomer
	 */
	@Override
	public MplFinalVisibleCouponsDTO getDisplayCouponList(final String cartGuid, final CustomerModel currentCustomer)
	{
		List<MplVisibleCouponsDTO> closedVoucherDataList = null;
		List<MplVisibleCouponsDTO> openVoucherDataList = null;
		//final List<MplVisibleCouponsDTO> finalVoucherDataList = new ArrayList<>();
		final MplFinalVisibleCouponsDTO finalDTO = new MplFinalVisibleCouponsDTO();

		final String customerUID = currentCustomer.getUid();

		LOG.debug("Customer UID>>>" + customerUID);
		try
		{
			closedVoucherDataList = mplDisplayCouponCachingStrategy.get(customerUID);

			if (CollectionUtils.isEmpty(closedVoucherDataList))
			{
				final List<VoucherModel> closedVoucherList = mplCouponService.getClosedVoucherList(currentCustomer);
				if (CollectionUtils.isNotEmpty(closedVoucherList))
				{
					closedVoucherDataList = populateDataList(closedVoucherList);

					if (StringUtils.isNotEmpty(cartGuid))
					{
						mplDisplayCouponCachingStrategy.put(customerUID, closedVoucherDataList);
					}

				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error during Closed Voucher Fetch", exception);
		}

		try
		{
			openVoucherDataList = mplDisplayCouponCachingStrategy.get("OPENCOUPONS");

			if (CollectionUtils.isEmpty(openVoucherDataList))
			{
				final List<VoucherModel> openVoucherList = mplCouponService.getOpenVoucherList();
				if (CollectionUtils.isNotEmpty(openVoucherList))
				{
					openVoucherDataList = populateDataList(openVoucherList);
					mplDisplayCouponCachingStrategy.put("OPENCOUPONS", openVoucherDataList);
				}
			}

		}
		catch (final Exception exception)
		{
			LOG.error("Error during Open Voucher Fetch", exception);
		}


		if (CollectionUtils.isNotEmpty(closedVoucherDataList))
		{
			//finalVoucherDataList.addAll(closedVoucherDataList);
			finalDTO.setClosedcouponsList(closedVoucherDataList);
		}

		if (CollectionUtils.isNotEmpty(openVoucherDataList))
		{
			//finalVoucherDataList.addAll(openVoucherDataList);
			finalDTO.setOpencouponsList(openVoucherDataList);
		}

		//		if (CollectionUtils.isNotEmpty(finalVoucherDataList))
		//		{
		//			finalDTO.setCouponsList(finalVoucherDataList);
		//		}

		return finalDTO;
	}

	/**
	 * The Method returns Coupon Details to be displayed on Cart Page
	 *
	 */
	@Override
	public MplFinalVisibleCouponsDTO getDisplayOpenCouponList()
	{
		List<MplVisibleCouponsDTO> openVoucherDataList = null;
		final MplFinalVisibleCouponsDTO finalDTO = new MplFinalVisibleCouponsDTO();

		try
		{
			openVoucherDataList = mplDisplayCouponCachingStrategy.get("OPENCOUPONS");

			if (CollectionUtils.isEmpty(openVoucherDataList))
			{
				final List<VoucherModel> openVoucherList = mplCouponService.getOpenVoucherList();
				if (CollectionUtils.isNotEmpty(openVoucherList))
				{
					openVoucherDataList = populateDataList(openVoucherList);
					mplDisplayCouponCachingStrategy.put("OPENCOUPONS", openVoucherDataList);
				}
			}

		}
		catch (final Exception exception)
		{
			LOG.error("Error during Open Voucher Fetch", exception);
		}

		if (CollectionUtils.isNotEmpty(openVoucherDataList))
		{
			finalDTO.setOpencouponsList(openVoucherDataList);
		}

		return finalDTO;
	}


	/**
	 * The Method populates data in Data Class
	 *
	 * @param voucherList
	 * @return List<MplVisibleCouponsDTO>
	 */
	private List<MplVisibleCouponsDTO> populateDataList(final List<VoucherModel> voucherList)
	{
		final List<MplVisibleCouponsDTO> voucherDataList = new ArrayList<>();

		for (final VoucherModel oModel : voucherList)
		{
			final MplVisibleCouponsDTO dto = new MplVisibleCouponsDTO();
			//dto.setCouponCode(oModel.getCode());
			dto.setCouponName(oModel.getName());
			dto.setDescription(oModel.getDescription());
			dto.setIsPercentage(((oModel.getAbsolute().booleanValue()) ? (false) : (true)));
			dto.setValue(oModel.getValue());
			dto.setDescription(oModel.getDescription());

			if (null != oModel.getMaxDiscountValue())
			{
				dto.setMaxDiscount(oModel.getMaxDiscountValue());
			}

			final List<RestrictionModel> restrictionList = new ArrayList<>(oModel.getRestrictions());
			if (CollectionUtils.isNotEmpty(restrictionList))
			{
				for (final RestrictionModel restriction : restrictionList)
				{
					if (restriction instanceof DateRestrictionModel)
					{
						final DateRestrictionModel dateRestrictmodel = (DateRestrictionModel) restriction;
						dto.setCouponCreationDate(dateRestrictmodel.getStartDate().toString());
						dto.setCouponExpiryDate(dateRestrictmodel.getEndDate().toString());
					}

					else if (restriction instanceof MplOrderRestrictionModel)
					{
						final MplOrderRestrictionModel restrictionModel = (MplOrderRestrictionModel) restriction;

						final PriceData price = priceDataFactory.create(PriceDataType.BUY,
								BigDecimal.valueOf(restrictionModel.getTotal().doubleValue()), restrictionModel.getCurrency());

						dto.setEligibleCartThreshold(price);

					}
				}
			}

			if ((oModel instanceof PromotionVoucherModel) && !(oModel instanceof MplCartOfferVoucherModel))
			{
				final PromotionVoucherModel coupon = (PromotionVoucherModel) oModel;
				dto.setCouponCode(coupon.getVoucherCode());
				dto.setCouponType("COUPON");
			}
			else
			{
				dto.setCouponCode(oModel.getCode());
				dto.setCouponType("BANKCOUPON");
			}

			voucherDataList.add(dto);

		}

		return voucherDataList;
	}



	/**
	 * @return the mplDisplayCouponCachingStrategy
	 */
	public MplDisplayCouponCachingStrategy getMplDisplayCouponCachingStrategy()
	{
		return mplDisplayCouponCachingStrategy;
	}



	/**
	 * @param mplDisplayCouponCachingStrategy
	 *           the mplDisplayCouponCachingStrategy to set
	 */
	public void setMplDisplayCouponCachingStrategy(final MplDisplayCouponCachingStrategy mplDisplayCouponCachingStrategy)
	{
		this.mplDisplayCouponCachingStrategy = mplDisplayCouponCachingStrategy;
	}



	/**
	 * Releases Cart Voucher from Cart
	 *
	 * @param cart
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean releaseCartVoucherInCheckout(final CartModel cart) throws VoucherOperationException, EtailNonBusinessExceptions
	{

		final List<DiscountModel> discountList = cart.getDiscounts();
		boolean recalculateRequired = false;
		try
		{
			if (CollectionUtils.isNotEmpty(discountList))
			{
				for (final DiscountModel oModel : discountList)
				{
					if (oModel instanceof MplCartOfferVoucherModel)
					{
						final MplCartOfferVoucherModel coupon = (MplCartOfferVoucherModel) oModel;
						getVoucherService().releaseVoucher(coupon.getVoucherCode(), cart);
						final List<AbstractOrderEntryModel> entryList = cart.getEntries();
						if (CollectionUtils.isNotEmpty(entryList))
						{
							for (final AbstractOrderEntryModel entry : entryList)
							{
								entry.setCartCouponCode(MarketplacecommerceservicesConstants.EMPTY);
								entry.setCartCouponValue(Double.valueOf(0.00D));
							}
							if (CollectionUtils.isNotEmpty(entryList)) //Saving the entryList
							{
								getModelService().saveAll(entryList);
							}
						}
					}
				}
				recalculateRequired = true;
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in releaseVoucherInCheckout ", ex); //TISPT-104
			throw new EtailNonBusinessExceptions(ex);
		}
		return recalculateRequired;
	}

}
