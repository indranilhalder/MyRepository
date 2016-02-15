/**
 *
 */
package com.tisl.mpl.coupon.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
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
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 */
	SearchPageData<VoucherModel> findClosedVoucher(CustomerModel customer, PageableData pageableData);

	/**
	 * @param customer
	 * @return
	 */
	Set<Map<OrderModel, VoucherModel>> findVoucherRedeemedOrder(CustomerModel customer);

	/**
	 * @param customer
	 * @param pageableData
	 * @return
	 */
	SearchPageData<VoucherInvalidationModel> findVoucherHistoryRedeemedOrders(CustomerModel customer, PageableData pageableData);

}