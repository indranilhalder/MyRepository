/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.CommonCouponsDTO;


/**
 * @author TCS
 *
 */
public interface MplCouponWebService
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
}
