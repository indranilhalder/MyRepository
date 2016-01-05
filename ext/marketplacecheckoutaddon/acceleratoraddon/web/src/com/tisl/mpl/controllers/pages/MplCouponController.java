package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;

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
import com.tisl.mpl.model.BankModel;


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
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	@RequestMapping(value = "/redeem", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData redeemCoupon(final String couponCode, final String paymentMode,
			final String bankNameSelected) throws EtailNonBusinessExceptions, JaloPriceFactoryException, CalculationException
	{
		LOG.debug("The coupon code entered by the customer is ::: " + couponCode);
		final CartModel cartModel = getCartService().getSessionCart();
		getSessionService().setAttribute("paymentModeForPromotion", paymentMode);

		final Collection<BankModel> bankList = getBaseStoreService().getCurrentBaseStore().getBanks();
		if (null == bankNameSelected)
		{
			getSessionService().setAttribute("bank", bankNameSelected);
		}
		else
		{
			for (final BankModel bank : bankList)
			{
				if (bank.getBankName().equalsIgnoreCase(bankNameSelected))
				{
					//setting the bank in session to be used for Promotion
					getSessionService().setAttribute("bank", bank);
					break;
				}
			}
		}

		final boolean redeem = true;
		boolean couponRedStatus = false;
		VoucherDiscountData data = new VoucherDiscountData();

		try
		{
			couponRedStatus = getMplCouponFacade().applyVoucher(couponCode, cartModel);
		}
		catch (final VoucherOperationException e)
		{
			if (e.getMessage().contains("total price exceeded"))
			{
				data.setRedeemErrorMsg("Cannot redeem voucher " + couponCode + " as total price exceeded");
				data.setTotalPrice(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv()));
				data.setCouponRedeemed(false);
				return data;
			}
		}

		LOG.debug("Coupon Redemption Status is:::" + couponRedStatus);

		data = getMplCouponFacade().calculateValues(cartModel, couponRedStatus, redeem);

		getSessionService().removeAttribute("bank");

		return data;
	}


	/**
	 * This method releases the Coupon applied
	 *
	 * @param couponCode
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	@RequestMapping(value = "/release", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody VoucherDiscountData releaseCoupon(final String couponCode)
			throws EtailNonBusinessExceptions, JaloPriceFactoryException, CalculationException
	{
		LOG.debug("The coupon code to be released by the customer is ::: " + couponCode);
		final CartModel cartModel = getCartService().getSessionCart();
		boolean couponRelStatus = false;
		final boolean redeem = false;

		try
		{
			getVoucherFacade().releaseVoucher(couponCode);
			couponRelStatus = true;
		}
		catch (final VoucherOperationException e)
		{
			e.printStackTrace();
		}

		getMplCouponFacade().recalculateCartForCoupon(cartModel);
		LOG.debug("Coupon Release Status is:::" + couponRelStatus);

		VoucherDiscountData data = new VoucherDiscountData();
		if (couponRelStatus)
		{
			data = getMplCouponFacade().calculateValues(cartModel, couponRelStatus, redeem);
		}

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