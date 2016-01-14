/**
 *
 */
package com.tisl.mpl.coupon.dao;

import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;
import java.util.Set;


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

	/**
	 * @return
	 */
	Set<Map<VoucherModel, DateRestrictionModel>> findClosedVoucher();

}