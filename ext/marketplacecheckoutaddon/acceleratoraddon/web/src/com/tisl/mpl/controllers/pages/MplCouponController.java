package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.voucher.VoucherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.util.ExceptionUtil;


@Controller
@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLCOUPONURL)
public class MplCouponController
{
	private static final Logger LOG = Logger.getLogger(MplCouponController.class);

	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	//Added for TPR-4461 starts here
	@Resource(name = "voucherService")
	private VoucherService voucherService;

	//Added for TPR-4461 ends here


	/**
	 * This method applies the voucher
	 *
	 * @param couponCode
	 * @param paymentMode
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	//@RequestMapping(value = MarketplacecouponConstants.COUPONREDEEM, method = RequestMethod.GET)
	@RequestMapping(value = MarketplacecouponConstants.COUPONREDEEM, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData redeemCoupon(final String couponCode, final String paymentMode,
			final String bankNameSelected, final String guid) //Added guid to handle orderModel for TPR-629
	{
		VoucherDiscountData data = new VoucherDiscountData();
		OrderModel orderModel = null;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;

		//Fetching orderModel based on guid TPR-629
		if (StringUtils.isNotEmpty(guid))
		{
			orderModel = getMplPaymentFacade().getOrderByGuid(guid);
		}

		final StringBuilder logBuilder = new StringBuilder();
		LOG.debug(logBuilder.append("Step 1:::The coupon code entered by the customer is :::").append(couponCode)
				.append("------The bank selected is  ::: ").append(bankNameSelected));
		getSessionService().setAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION, paymentMode);

		final boolean redeem = true;
		boolean couponRedStatus = false;
		String couponMessageInformation = null;//Added for TPR-4461
		boolean isCartVoucherPresent = false;

		//Redeem coupon for cartModel
		if (orderModel == null)
		{
			CartModel cartModel = getCartService().getSessionCart();
			try
			{

				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cartModel.getDiscounts());

				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					cartModel = (CartModel) getMplCouponFacade().removeLastCartCoupon(cartModel); // Removing any Cart level Coupon Offer
				}

				//Apply the voucher
				couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, cartModel, null);

				LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				data = getMplCouponFacade().calculateValues(null, cartModel, couponRedStatus, redeem);

				//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON starts here
				couponMessageInformation = getMplCouponFacade().getCouponMessageInfo(cartModel);
				if (StringUtils.isNotEmpty(couponMessageInformation))
				{
					data.setCouponMessageInfo(couponMessageInformation);
				}
				//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON ends here


				final Map<String, Double> paymentInfo = getSessionService()
						.getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, cartModel);

				//getSessionService().removeAttribute("bank");	//Do not remove---needed later

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					data = reapplyCartCoupon(data, cartCouponCode, cartModel);
				}

			}
			catch (final VoucherOperationException e)
			{
				//Set the data for exception cases
				data = setRedDataForException(data, cartModel);
				if (e.getMessage().contains(MarketplacecouponConstants.EXCPRICEEXCEEDED))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.PRICEEXCEEDED);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.INVALID);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCEXPIRED))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.EXPIRED);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCISSUE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCNOTAPPLICABLE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.NOTAPPLICABLE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCNOTRESERVABLE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.NOTRESERVABLE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCFREEBIE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.FREEBIE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCUSERINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.USERINVALID);
				}
				if (e.getMessage().contains(MarketplacecommerceservicesConstants.SELLERVIOLATION))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.SELLERVIOLATION);
				}
				if (e.getMessage().contains(MarketplacecommerceservicesConstants.ORDERTHRESHOLD))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.ORDERVIOLATION);
				}
				/* TPR-1075 Changes Start */
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCFIRSTPURUSERINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.FIRSTPURUSERINVALID);
				}
				/* TPR-1075 Changes End */
				//TPR-4460
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_WEB))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_WEB);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_MOBILE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_MOBILE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_CALLCENTRE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_CALLCENTRE);
				}

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					data = reapplyCartCoupon(data, cartCouponCode, cartModel);
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				//Set the data for exception cases
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				data = setRedDataForException(data, cartModel);
				data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
			}
			catch (final Exception e)
			{
				//Set the data for exception cases
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				data = setRedDataForException(data, cartModel);
				data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
			}

		}
		//Redeem coupon for orderModel
		else
		{
			try
			{
				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(orderModel.getDiscounts());

				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					orderModel = (OrderModel) getMplCouponFacade().removeLastCartCoupon(orderModel); // Removing any Cart level Coupon Offer
				}

				//Apply the voucher
				couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, null, orderModel);

				LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				data = getMplCouponFacade().calculateValues(orderModel, null, couponRedStatus, redeem);

				//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON starts here
				couponMessageInformation = getMplCouponFacade().getCouponMessageInfo(orderModel);
				if (StringUtils.isNotEmpty(couponMessageInformation) && couponMessageInformation != null)
				{
					data.setCouponMessageInfo(couponMessageInformation);
				}
				//TPR-4461 MESSAGE FOR PAYMENT MODE RESTRICTION FOR COUPON ends here


				final Map<String, Double> paymentInfo = getSessionService()
						.getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, orderModel);

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					data = reapplyCartCoupon(data, cartCouponCode, orderModel);
				}

			}
			catch (final VoucherOperationException e)
			{
				//Set the data for exception cases
				data = setRedDataForException(data, orderModel);
				if (e.getMessage().contains(MarketplacecouponConstants.EXCPRICEEXCEEDED))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.PRICEEXCEEDED);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.INVALID);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCEXPIRED))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.EXPIRED);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCISSUE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCNOTAPPLICABLE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.NOTAPPLICABLE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCNOTRESERVABLE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.NOTRESERVABLE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCFREEBIE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.FREEBIE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCUSERINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.USERINVALID);
				}
				//TPR-4460
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_WEB))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_WEB);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_MOBILE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_MOBILE);
				}
				else if (e.getMessage().contains(MarketplacecouponConstants.CHANNELRESTVIOLATION_CALLCENTRE))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID_CALLCENTRE);
				}

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					data = reapplyCartCoupon(data, cartCouponCode, orderModel);
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				//Set the data for exception cases
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				data = setRedDataForException(data, orderModel);
				data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
			}
			catch (final Exception e)
			{
				//Set the data for exception cases
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				data = setRedDataForException(data, orderModel);
				data.setRedeemErrorMsg(MarketplacecouponConstants.ISSUE);
			}
		}

		if (null != getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION))
		{
			getSessionService().removeAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
		}

		return data;
	}



	/**
	 * Re apply Cart Coupon
	 *
	 * @param data
	 * @param cartCouponCode
	 * @param cartModel
	 * @return dataPojo
	 */
	private VoucherDiscountData reapplyCartCoupon(final VoucherDiscountData data, final String cartCouponCode,
			final CartModel cartModel)
	{
		final VoucherDiscountData dataPojo = data;
		try
		{
			final boolean applyStatus = getMplCouponFacade().applyCartVoucher(cartCouponCode, cartModel, null);
			final VoucherDiscountData newData = getMplCouponFacade().populateCartVoucherData(null, cartModel, applyStatus, true,
					cartCouponCode);

			data.setTotalDiscount(newData.getTotalDiscount());
			data.setTotalPrice(newData.getTotalPrice());

		}
		catch (final VoucherOperationException e)
		{
			LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

		return dataPojo;
	}

	/**
	 * Re apply Cart Coupon
	 *
	 * @param data
	 * @param cartCouponCode
	 * @param oModel
	 * @return dataPojo
	 */
	private VoucherDiscountData reapplyCartCoupon(final VoucherDiscountData data, final String cartCouponCode,
			final OrderModel oModel)
	{
		final VoucherDiscountData dataPojo = data;
		try
		{
			final boolean applyStatus = getMplCouponFacade().applyCartVoucher(cartCouponCode, null, oModel);
			final VoucherDiscountData newData = getMplCouponFacade().populateCartVoucherData(oModel, null, applyStatus, true,
					cartCouponCode);

			data.setTotalDiscount(newData.getTotalDiscount());
			data.setTotalPrice(newData.getTotalPrice());

		}
		catch (final VoucherOperationException e)
		{
			LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

		return dataPojo;
	}



	private Tuple2<Boolean, String> isCartVoucherPresent(final List<DiscountModel> discounts)
	{
		boolean flag = false;
		String couponCode = MarketplacecommerceservicesConstants.EMPTY;
		if (CollectionUtils.isNotEmpty(discounts))
		{
			for (final DiscountModel discount : discounts)
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					final MplCartOfferVoucherModel object = (MplCartOfferVoucherModel) discount;
					flag = true;
					couponCode = object.getVoucherCode();
					break;
				}
			}
		}

		final Tuple2<Boolean, String> cartCouponObj = new Tuple2(Boolean.valueOf(flag), couponCode);

		return cartCouponObj;
	}


	/**
	 * This method releases the Coupon applied
	 *
	 * @param couponCode
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	//@RequestMapping(value = MarketplacecouponConstants.COUPONRELEASE, method = RequestMethod.GET)
	@RequestMapping(value = MarketplacecouponConstants.COUPONRELEASE, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData releaseCoupon(final String couponCode, final String guid) //Added guid to handle orderModel for TPR-629
	{

		VoucherDiscountData data = new VoucherDiscountData();
		OrderModel orderModel = null;
		if (StringUtils.isNotEmpty(guid))
		{
			orderModel = getMplPaymentFacade().getOrderByGuid(guid);
		}

		final Map<String, Double> paymentInfo = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
		if (null != paymentInfo)
		{
			for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
			{
				if (null != entry.getKey() && !(MarketplacecheckoutaddonConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION, entry.getKey());
				}
			}
		}
		final boolean redeem = false;
		boolean isCartVoucherPresent = false;
		String cartCouponCode = MarketplacecommerceservicesConstants.EMPTY;

		//Release coupon for cartModel
		if (null == orderModel)
		{
			CartModel cartModel = getCartService().getSessionCart();
			try
			{
				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);

				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(cartModel.getDiscounts());
				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					cartModel = (CartModel) getMplCouponFacade().removeLastCartCoupon(cartModel); // Removing any Cart level Coupon Offer
				}

				//Release the coupon
				getMplCouponFacade().releaseVoucher(couponCode, cartModel, null);
				//Recalculate cart after releasing coupon
				getMplCouponFacade().recalculateCartForCoupon(cartModel, null); //Handled changed method signature for TPR-629

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					try
					{
						final boolean applyStatus = getMplCouponFacade().applyCartVoucher(cartCouponCode, cartModel, null);
						final VoucherDiscountData newData = getMplCouponFacade().populateCartVoucherData(null, cartModel, applyStatus,
								true, couponCode);

						data = newData;
						//data.setTotalDiscount(newData.getTotalDiscount());
						//data.setTotalPrice(newData.getTotalPrice());

					}
					catch (final VoucherOperationException e)
					{
						LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
					}
					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
					}
				}
				else
				{
					data = getMplCouponFacade().calculateValues(null, cartModel, true, redeem);
					getMplCouponFacade().updatePaymentInfoSession(paymentInfo, cartModel);
				}



			}
			catch (final VoucherOperationException e)
			{
				LOG.error(MarketplacecouponConstants.COUPONRELISSUE, e);
				data = setRelDataForException(data, cartModel);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				data = setRelDataForException(data, cartModel);
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				data = setRelDataForException(data, cartModel);
			}
		}
		//Release coupon for orderModel
		else
		{
			try
			{
				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);

				final Tuple2<Boolean, String> cartCouponObj = isCartVoucherPresent(orderModel.getDiscounts());
				isCartVoucherPresent = cartCouponObj.getFirst().booleanValue();

				if (isCartVoucherPresent)
				{
					cartCouponCode = cartCouponObj.getSecond();
					orderModel = (OrderModel) getMplCouponFacade().removeLastCartCoupon(orderModel); // Removing any Cart level Coupon Offer
				}

				//Release the coupon
				getMplCouponFacade().releaseVoucher(couponCode, null, orderModel);

				//Recalculate cart after releasing coupon
				getMplCouponFacade().recalculateCartForCoupon(null, orderModel); //Handled changed method signature for TPR-629

				if (StringUtils.isNotEmpty(cartCouponCode))
				{
					try
					{
						final boolean applyStatus = getMplCouponFacade().applyCartVoucher(cartCouponCode, null, orderModel);
						final VoucherDiscountData newData = getMplCouponFacade().populateCartVoucherData(orderModel, null, applyStatus,
								true, couponCode);

						data = newData;
						//data.setTotalDiscount(newData.getTotalDiscount());
						//data.setTotalPrice(newData.getTotalPrice());

					}
					catch (final VoucherOperationException e)
					{
						LOG.debug("Failed to apply Voucher with Code >>>" + cartCouponCode);
					}
					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
					}
				}
				else
				{
					data = getMplCouponFacade().calculateValues(orderModel, null, true, redeem);

					//Update paymentInfo in session
					getMplCouponFacade().updatePaymentInfoSession(paymentInfo, orderModel);
				}



			}
			catch (final VoucherOperationException e)
			{
				LOG.error(MarketplacecouponConstants.COUPONRELISSUE, e);
				data = setRelDataForException(data, orderModel);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				data = setRelDataForException(data, orderModel);
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				data = setRelDataForException(data, orderModel);
			}
		}

		if (null != getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION))
		{
			getSessionService().removeAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
		}

		return data;
	}



	/**
	 *
	 * This method sets data for erroneous cases for voucher release
	 *
	 * @param data
	 * @param abstractOrderModel
	 * @return VoucherDiscountData
	 */
	private VoucherDiscountData setRelDataForException(final VoucherDiscountData data, final AbstractOrderModel abstractOrderModel)
	//Changed to AbstractOrderModel for TPR-629
	{
		final VoucherDiscountData errorData = data;
		errorData.setTotalPrice(getMplCheckoutFacade().createPrice(abstractOrderModel, abstractOrderModel.getTotalPriceWithConv()));
		errorData.setRedeemErrorMsg(MarketplacecouponConstants.RELEASEISSUE);
		errorData.setCouponReleased(false);

		return errorData;
	}


	/**
	 *
	 * This method sets data for erroneous cases for voucher redemption
	 *
	 * @param data
	 * @param abstractOrderModel
	 * @return VoucherDiscountData
	 */
	private VoucherDiscountData setRedDataForException(final VoucherDiscountData data, final AbstractOrderModel abstractOrderModel)
	//Changed to AbstractOrderModel for TPR-629
	{
		final VoucherDiscountData errorData = data;
		errorData.setTotalPrice(getMplCheckoutFacade().createPrice(abstractOrderModel, abstractOrderModel.getTotalPriceWithConv()));
		errorData.setCouponRedeemed(false);

		return errorData;
	}



	public CartService getCartService()
	{
		return cartService;
	}

	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	public MplCouponFacade getMplCouponFacade()
	{
		return mplCouponFacade;
	}

	public void setMplCouponFacade(final MplCouponFacade mplCouponFacade)
	{
		this.mplCouponFacade = mplCouponFacade;
	}

	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}


	/**
	 * @return the mplPaymentFacade
	 */
	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}


	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
	}

	//TPR-4461 starts here

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

	//TPR-4461 ends here

	/**
	 * The method deals with application of Cart Offer Coupons
	 *
	 *
	 * @param manuallyselectedvoucher
	 * @param guid
	 * @return data
	 */

	@RequestMapping(value = MarketplacecouponConstants.CARTCOUPONREDEEM, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData redeemCartCoupon(final String manuallyselectedvoucher, final String guid)
	{
		VoucherDiscountData data = new VoucherDiscountData();
		try
		{
			final String couponCode = getMplCouponFacade().getCouponCode(manuallyselectedvoucher);

			OrderModel orderModel = null;
			boolean couponRedStatus = false;

			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}

			if (orderModel == null)
			{
				CartModel cartModel = getCartService().getSessionCart();

				try
				{
					cartModel = (CartModel) getMplCouponFacade().removeLastCartCoupon(cartModel);
					if (StringUtils.isNotEmpty(couponCode))
					{
						couponRedStatus = getMplCouponFacade().applyCartVoucher(couponCode, cartModel, null);
						LOG.debug("Cart Coupon Redemption Status is >>>>" + couponRedStatus);
						data = getMplCouponFacade().populateCartVoucherData(null, cartModel, couponRedStatus, true, couponCode);
					}
				}
				catch (final VoucherOperationException e)
				{
					data = setCartVoucherDataForException(data, cartModel);
				}
			}
			else
			{
				try
				{
					orderModel = (OrderModel) getMplCouponFacade().removeLastCartCoupon(orderModel);
					if (StringUtils.isNotEmpty(couponCode))
					{
						couponRedStatus = getMplCouponFacade().applyCartVoucher(couponCode, null, orderModel);
						LOG.debug("Cart Coupon Redemption Status is >>>>" + couponRedStatus);
						data = getMplCouponFacade().populateCartVoucherData(orderModel, null, couponRedStatus, true, couponCode);
					}

				}
				catch (final VoucherOperationException e)
				{
					data = setCartVoucherDataForException(data, orderModel);
				}

			}
		}
		catch (final Exception exception)
		{
			LOG.debug("Exception >>>>" + exception.getMessage());
		}

		return data;

	}

	@RequestMapping(value = MarketplacecouponConstants.CARTCOUPONRELEASE, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData releaseCartCoupon(final String manuallyselectedvoucher, final String guid)
	{
		VoucherDiscountData data = new VoucherDiscountData();
		try
		{
			final String couponCode = getMplCouponFacade().getCouponCode(manuallyselectedvoucher);

			OrderModel orderModel = null;

			boolean isCartVoucherRemoved = false;

			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}


			if (null == orderModel)
			{
				CartModel cartModel = getCartService().getSessionCart();

				cartModel = (CartModel) getMplCouponFacade().removeCartCoupon(cartModel);

				isCartVoucherRemoved = checkforCartVoucherRemoved(cartModel.getDiscounts());

				if (isCartVoucherRemoved)
				{
					data = getMplCouponFacade().populateCartVoucherData(null, cartModel, false, false, couponCode);
				}
			}
			else
			{
				orderModel = (OrderModel) getMplCouponFacade().removeCartCoupon(orderModel);

				isCartVoucherRemoved = checkforCartVoucherRemoved(orderModel.getDiscounts());

				if (isCartVoucherRemoved)
				{
					data = getMplCouponFacade().populateCartVoucherData(orderModel, null, false, false, couponCode);
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.debug("Exception >>>>" + exception.getMessage());
		}
		return data;
	}



	private boolean checkforCartVoucherRemoved(final List<DiscountModel> discounts)
	{
		boolean isCartVoucherRemoved = true;

		if (CollectionUtils.isNotEmpty(discounts))
		{
			for (final DiscountModel discount : discounts)
			{
				if (discount instanceof MplCartOfferVoucherModel)
				{
					isCartVoucherRemoved = false;
					break;
				}
			}
		}

		return isCartVoucherRemoved;
	}


	/**
	 * Set Data Class for Voucher Exception Flow
	 *
	 * @param data
	 * @param abstractOrderModel
	 * @return VoucherDiscountData
	 */
	private VoucherDiscountData setCartVoucherDataForException(final VoucherDiscountData data,
			final AbstractOrderModel abstractOrderModel)
	{
		final VoucherDiscountData errorData = data;
		double totalDiscount = 0.0;

		//final double couponDiscount = 0.0;
		//final double totalMRP = 0.0;

		final List<AbstractOrderEntryModel> entryList = new ArrayList<>(abstractOrderModel.getEntries());
		if (CollectionUtils.isNotEmpty(entryList))
		{
			for (final AbstractOrderEntryModel oModel : entryList)
			{
				final Double mrp = oModel.getMrp();
				final Double entryPrice = (null == oModel.getTotalPrice() ? Double.valueOf(0) : oModel.getTotalPrice());
				final int quantity = oModel.getQuantity().intValue();

				final Double cartDiscount = (null == oModel.getCartLevelDisc() ? Double.valueOf(0) : oModel.getCartLevelDisc());


				final double value = (mrp.doubleValue() * quantity) - entryPrice.doubleValue();

				totalDiscount += value + cartDiscount.doubleValue();
			}
		}

		data.setCouponDiscount(getMplCheckoutFacade().createPrice(abstractOrderModel, Double.valueOf(totalDiscount)));

		errorData.setTotalPrice(getMplCheckoutFacade().createPrice(abstractOrderModel, abstractOrderModel.getTotalPriceWithConv()));
		errorData.setCouponRedeemed(false);

		return errorData;
	}


}
