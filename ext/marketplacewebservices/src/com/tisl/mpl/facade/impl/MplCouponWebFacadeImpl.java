/**
 *
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.MplCouponWebFacade;
import com.tisl.mpl.service.MplCouponWebService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.CommonCouponsDTO;
import com.tisl.mpl.wsdto.ReleaseCouponsDTO;


/**
 * @author TCS
 *
 */
public class MplCouponWebFacadeImpl implements MplCouponWebFacade
{
	private static final Logger LOG = Logger.getLogger(MplCouponWebFacadeImpl.class);

	@Autowired
	MplCouponWebService mplCouponWebService;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

	/**
	 * @Description : For getting the details of all the Coupons available for the User
	 */

	@Override
	//	public CommonCouponsDTO getCoupons(final int currentPage, final int pageSize, final String emailId, final String usedCoupon,
	//			final String sortCode)
	public CommonCouponsDTO getCoupons(final int currentPage, final String emailId, final String usedCoupon, final String sortCode)
	{
		return (mplCouponWebService.getCoupons(currentPage, emailId, usedCoupon, sortCode));
	}


	@Override
	public ApplyCouponsDTO applyVoucher(final String couponCode, final CartModel cartModel)
			throws VoucherOperationException, CalculationException, NumberFormatException, JaloInvalidParameterException,
			JaloSecurityException, EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		final ApplyCouponsDTO applycouponDto = new ApplyCouponsDTO();
		boolean applycouponDtoflag = false;
		try
		{
			applycouponDtoflag = mplCouponFacade.applyVoucher(couponCode, cartModel);
			if (applycouponDtoflag)
			{
				VoucherDiscountData data = new VoucherDiscountData();

				data = mplCouponFacade.calculateValues(cartModel, applycouponDtoflag, true);

				if (null != data.getCouponDiscount() && null != data.getCouponDiscount().getValue())
				{
					//Price data new calculation for 2 decimal values
					applycouponDto.setCouponDiscount(
							String.valueOf(data.getCouponDiscount().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					applycouponDto.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				applycouponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9500);
			}
		}
		catch (final VoucherOperationException e)
		{
			applycouponDto.setStatus(MarketplacecommerceservicesConstants.FAILURE);

			if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCPRICEEXCEEDED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9501);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9503);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCEXPIRED))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9505);
			}
			else if (null != e.getMessage() && e.getMessage().contains(MarketplacewebservicesConstants.EXCISSUE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTAPPLICABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9509);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCNOTRESERVABLE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9510);
			}
			else if (e.getMessage().contains(MarketplacewebservicesConstants.EXCFREEBIE))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9511);
			}
			else if (e.getMessage().contains(MarketplacecouponConstants.EXCUSERINVALID))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9512);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9507);
		}
		return applycouponDto;
	}

	@Override
	public ReleaseCouponsDTO releaseVoucher(final String couponCode, final CartModel cartModel)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException,
			JaloPriceFactoryException, EtailBusinessExceptions
	{
		boolean couponRelStatus = false;
		final boolean redeem = false;
		ReleaseCouponsDTO releaseCouponsDTO = new ReleaseCouponsDTO();
		try
		{
			mplCouponFacade.releaseVoucher(couponCode, cartModel);
			couponRelStatus = true;
			mplCouponFacade.recalculateCartForCoupon(cartModel);
			LOG.debug("Coupon Release Status is:::" + couponRelStatus);
			VoucherDiscountData data = new VoucherDiscountData();
			if (couponRelStatus)
			{
				data = mplCouponFacade.calculateValues(cartModel, couponRelStatus, redeem);
				if (null != data.getTotalPrice() && null != data.getTotalPrice().getValue())
				{
					releaseCouponsDTO.setTotal(String.valueOf(data.getTotalPrice().getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
				releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9508);
			}
		}
		catch (final VoucherOperationException e)
		{
			LOG.error("Issue with voucher release ", e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO, cartModel);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO, cartModel);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			releaseCouponsDTO = setRelDataForException(releaseCouponsDTO, cartModel);
		}

		return releaseCouponsDTO;
	}



	/**
	 * This method sets Release Coupon DTO for exception cases
	 *
	 * @param releaseCouponsDTO
	 * @param cartModel
	 * @return ReleaseCouponsDTO
	 */
	private ReleaseCouponsDTO setRelDataForException(final ReleaseCouponsDTO releaseCouponsDTO, final CartModel cartModel)
	{
		releaseCouponsDTO.setTotal(String.valueOf(mplCheckoutFacade.createPrice(cartModel, cartModel.getTotalPriceWithConv())
				.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
		releaseCouponsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE);
		releaseCouponsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9508);
		releaseCouponsDTO.setError(MarketplacewebservicesConstants.COUPONRELISSUE);
		return releaseCouponsDTO;
	}

}
