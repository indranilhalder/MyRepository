package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;


@Controller
@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLCOUPONURL)
public class MplCouponController
{
	private static final Logger LOG = Logger.getLogger(MplCouponController.class);

	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;
	@Autowired
	private VoucherFacade voucherFacade;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private BaseStoreService baseStoreService;


	/**
	 * This method applies the voucher
	 *
	 * @param couponCode
	 * @param paymentMode
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/redeem", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData redeemCoupon(final String couponCode, final String paymentMode,
			final String bankNameSelected) throws EtailNonBusinessExceptions, JaloPriceFactoryException, CalculationException,
			NumberFormatException, JaloInvalidParameterException, JaloSecurityException
	{
		LOG.debug("The coupon code entered by the customer is ::: " + couponCode);
		final CartModel cartModel = getCartService().getSessionCart();

		LOG.debug("The bank selected is  ::: " + bankNameSelected);
		getSessionService().setAttribute("paymentModeForPromotion", paymentMode);
		//
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

		final boolean redeem = true;
		boolean couponRedStatus = false;
		VoucherDiscountData data = new VoucherDiscountData();

		try
		{
			couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, cartModel);
		}
		catch (final VoucherOperationException e)
		{
			LOG.error("Issue with voucher redeem " + e.getMessage());
			if (e.getMessage().contains("total price exceeded"))
			{
				data.setRedeemErrorMsg("Price_exceeded");
			}
			else if (e.getMessage().contains("Voucher not found"))
			{
				data.setRedeemErrorMsg("Invalid");
			}
			else if (e.getMessage().contains("Voucher cannot be redeemed"))
			{
				data.setRedeemErrorMsg("Expired");
			}
			else if (e.getMessage().contains("Error while"))
			{
				data.setRedeemErrorMsg("Issue");
			}
			else if (e.getMessage().contains("Voucher is not applicable"))
			{
				data.setRedeemErrorMsg("Not_Applicable");
			}
			else if (e.getMessage().contains("Voucher is not reservable"))
			{
				data.setRedeemErrorMsg("Not_Reservable");
			}
			else if (e.getMessage().contains("freebie"))
			{
				data.setRedeemErrorMsg("Freebie");
			}
			else if (e.getMessage().contains("User not valid"))
			{
				data.setRedeemErrorMsg("User_Invalid");
			}

			data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
			data.setCouponRedeemed(false);
			return data;
		}

		LOG.debug("Step 20:::Coupon Redemption Status is:::" + couponRedStatus);

		data = getMplCouponFacade().calculateValues(cartModel, couponRedStatus, redeem);

		final Map<String, Double> paymentInfo = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
		final Map<String, Double> updatedPaymentInfo = new HashMap<String, Double>();
		if (null != paymentInfo)
		{
			for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
			{
				if (!(MarketplacecheckoutaddonConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					updatedPaymentInfo.put(entry.getKey(), cartModel.getTotalPriceWithConv());
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, updatedPaymentInfo);
				}
			}
		}
		getSessionService().removeAttribute("paymentModeForPromotion");
		//getSessionService().removeAttribute("bank");

		return data;
	}


	/**
	 * This method releases the Coupon applied
	 *
	 * @param couponCode
	 * @return VoucherDiscountData
	 * @throws EtailNonBusinessExceptions
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	@RequestMapping(value = "/release", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData releaseCoupon(final String couponCode) throws EtailNonBusinessExceptions,
			JaloPriceFactoryException, CalculationException
	{
		LOG.debug("Step 1:::The coupon code to be released by the customer is ::: " + couponCode);
		final Map<String, Double> paymentInfo = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
		if (null != paymentInfo)
		{
			for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
			{
				if (!(MarketplacecheckoutaddonConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					getSessionService().setAttribute("paymentModeForPromotion", entry.getKey());
				}
			}
		}
		final CartModel cartModel = getCartService().getSessionCart();
		boolean couponRelStatus = false;
		final boolean redeem = false;

		try
		{
			getMplCouponFacade().releaseVoucher(couponCode, cartModel);
			couponRelStatus = true;
		}
		catch (final VoucherOperationException e)
		{
			LOG.error("Issue with voucher release " + e.getMessage());
			final VoucherDiscountData data = new VoucherDiscountData();
			data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
			data.setRedeemErrorMsg("Release Issue");
			data.setCouponReleased(false);
			return data;
		}

		getMplCouponFacade().recalculateCartForCoupon(cartModel);
		LOG.debug("Coupon Release Status is:::" + couponRelStatus);

		VoucherDiscountData data = new VoucherDiscountData();
		if (couponRelStatus)
		{
			data = getMplCouponFacade().calculateValues(cartModel, couponRelStatus, redeem);

			//final Map<String, Double> paymentInfo = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
			final Map<String, Double> updatedPaymentInfo = new HashMap<String, Double>();
			if (null != paymentInfo)
			{
				for (final Map.Entry<String, Double> entry : paymentInfo.entrySet())
				{
					if (!(MarketplacecheckoutaddonConstants.WALLET.equalsIgnoreCase(entry.getKey())))
					{
						updatedPaymentInfo.put(entry.getKey(), cartModel.getTotalPriceWithConv());
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, updatedPaymentInfo);
					}
				}
			}
		}

		getSessionService().removeAttribute("paymentModeForPromotion");

		return data;
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

	public VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}

	public void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
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




}