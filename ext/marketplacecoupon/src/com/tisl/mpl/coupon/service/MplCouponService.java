/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
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
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 */
	SearchPageData<VoucherModel> getClosedVoucher(final CustomerModel customer, PageableData pageableData);

	/**
	 * @param customer
	 * @return Set<Map<OrderModel, VoucherModel>>
	 */
	Set<Map<OrderModel, VoucherModel>> getCouponHistoryTransactions(CustomerModel customer);

	/**
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */
	SearchPageData<VoucherInvalidationModel> getVoucherRedeemedOrder(CustomerModel customer, PageableData pageableData);

}