/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author TCS
 *
 */
public interface MplCouponService
{

	/**
	 * @return List<VoucherModel>
	 */
	List<VoucherModel> getVoucher();

	/**
	 * @param voucherDataList
	 * @return ArrayList<VoucherDisplayData>
	 */
	List<VoucherDisplayData> getSortedVoucher(List<VoucherDisplayData> voucherDataList);

	/**
	 * @return
	 */
	Set<Map<VoucherModel, DateRestrictionModel>> getClosedVoucher();

	SearchPageData<VoucherModel> getClosedVoucher(final CustomerModel customer, PageableData pageableData);

}