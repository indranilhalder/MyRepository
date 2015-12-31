/**
 *
 */
package com.tisl.mpl.coupon.dao;

import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplCouponDao
{

	/**
	 * @return List<VoucherModel>
	 */
	List<VoucherModel> findVoucher();

}