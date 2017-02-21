package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Map;

import javax.annotation.Resource;

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
	@RequestMapping(value = MarketplacecouponConstants.COUPONREDEEM, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData redeemCoupon(final String couponCode, final String paymentMode,
			final String bankNameSelected, final String guid) //Added guid to handle orderModel for TPR-629
	{
		VoucherDiscountData data = new VoucherDiscountData();
		OrderModel orderModel = null;
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

		//Redeem coupon for cartModel
		if (orderModel == null)
		{
			final CartModel cartModel = getCartService().getSessionCart();
			try
			{
				//	Commented-----to be implemented in R2 later
				//		final Collection<BankModel> bankList = getBaseStoreService().getCurrentBaseStore().getBanks();
				//		if (StringUtils.isEmpty(bankNameSelected))
				//		{
				//			getSessionService().setAttribute("bank", bankNameSelected);
				//		}
				//		else
				//		{
				//			for (final BankModel bank : bankList)
				//			{
				//				if (bank.getBankName().equalsIgnoreCase(bankNameSelected))
				//				{
				//					//setting the bank in session to be used for Promotion
				//					getSessionService().setAttribute("bank", bank);
				//					break;
				//				}
				//			}
				//		}

				//Apply the voucher
				couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, cartModel, null);

				LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				data = getMplCouponFacade().calculateValues(null, cartModel, couponRedStatus, redeem);

				final Map<String, Double> paymentInfo = getSessionService().getAttribute(
						MarketplacecheckoutaddonConstants.PAYMENTMODE);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, cartModel);

				//getSessionService().removeAttribute("bank");	//Do not remove---needed later
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
				/* TPR-1075 Changes Start */
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCFIRSTPURUSERINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.FIRSTPURUSERINVALID);
				}
				/* TPR-1075 Changes End */
				//TPR-4460
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCHANNELINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID);
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
				//Apply the voucher
				couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, null, orderModel);

				LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

				//Calculate and set data attributes
				data = getMplCouponFacade().calculateValues(orderModel, null, couponRedStatus, redeem);

				final Map<String, Double> paymentInfo = getSessionService().getAttribute(
						MarketplacecheckoutaddonConstants.PAYMENTMODE);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, orderModel);

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
				else if (e.getMessage().contains(MarketplacecouponConstants.EXCHANNELINVALID))
				{
					data.setRedeemErrorMsg(MarketplacecouponConstants.CHANNELINVALID);
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
	 * This method releases the Coupon applied
	 *
	 * @param couponCode
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@RequestMapping(value = MarketplacecouponConstants.COUPONRELEASE, method = RequestMethod.GET)
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

		//Release coupon for cartModel
		if (null == orderModel)
		{
			final CartModel cartModel = getCartService().getSessionCart();
			try
			{
				LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);

				//Release the coupon
				getMplCouponFacade().releaseVoucher(couponCode, cartModel, null);

				//Recalculate cart after releasing coupon
				getMplCouponFacade().recalculateCartForCoupon(cartModel, null); //Handled changed method signature for TPR-629

				data = getMplCouponFacade().calculateValues(null, cartModel, true, redeem);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, cartModel);

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

				//Release the coupon
				getMplCouponFacade().releaseVoucher(couponCode, null, orderModel);

				//Recalculate cart after releasing coupon
				getMplCouponFacade().recalculateCartForCoupon(null, orderModel); //Handled changed method signature for TPR-629

				data = getMplCouponFacade().calculateValues(orderModel, null, true, redeem);

				//Update paymentInfo in session
				getMplCouponFacade().updatePaymentInfoSession(paymentInfo, orderModel);

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




}
