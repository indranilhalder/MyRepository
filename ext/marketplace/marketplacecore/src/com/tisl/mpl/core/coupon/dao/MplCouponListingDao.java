/**
 *
 */
package com.tisl.mpl.core.coupon.dao.impl;

import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author TCS
 *
 */
public interface MplCouponListingDao
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