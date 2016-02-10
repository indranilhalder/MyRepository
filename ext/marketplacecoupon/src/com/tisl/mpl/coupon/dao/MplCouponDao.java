/**
 *
 */
package com.tisl.mpl.coupon.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
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

	SearchPageData<VoucherModel> findClosedVoucher(CustomerModel customer, PageableData pageableData);

}