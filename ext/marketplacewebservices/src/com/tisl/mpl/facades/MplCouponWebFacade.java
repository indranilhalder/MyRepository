/**
 *
 */
package com.tisl.mpl.facades;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.net.MalformedURLException;

import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.CommonCouponsDTO;
import com.tisl.mpl.wsdto.ReleaseCouponsDTO;


/**
 * @author TCS
 *
 */
public interface MplCouponWebFacade
{
	/**
	 * @Description : For getting the details of all the Coupons available for the User
	 * @param currentPage
	 * @param pageSize
	 * @param emailId
	 * @param usedCoupon
	 * @param sortCode
	 * @return CommonCouponsDTO
	 */
	//	CommonCouponsDTO getCoupons(final int currentPage, final int pageSize, final String emailId, final String usedCoupon,
	//			final String sortCode);

	CommonCouponsDTO getCoupons(final int currentPage, final String emailId, final String usedCoupon, final String sortCode);

	/**
	 * @description apply the Coupon at payment page and get discount
	 * @param couponCode
	 * @param cartModel
	 * @return ApplyCouponsDTO
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws JaloSecurityException
	 */
	ApplyCouponsDTO applyVoucher(final String couponCode, final CartModel cartModel) throws VoucherOperationException,
			CalculationException, NumberFormatException, JaloInvalidParameterException, JaloSecurityException;


	/**
	 * @description release the Coupon for the particular user
	 * @param couponCode
	 * @param cartModel
	 * @return ReleaseCouponsDTO
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	ReleaseCouponsDTO releaseVoucher(final String couponCode, final CartModel cartModel) throws RequestParameterException,
			WebserviceValidationException, MalformedURLException, NumberFormatException, JaloInvalidParameterException,
			VoucherOperationException, CalculationException, JaloSecurityException, JaloPriceFactoryException, CalculationException;
}
