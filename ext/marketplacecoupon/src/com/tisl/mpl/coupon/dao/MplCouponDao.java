/**
 *
 */
package com.tisl.mpl.coupon.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.model.MplCartOfferVoucherModel;


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
	 * TPR-7486
	 */
	List<MplCartOfferVoucherModel> getPaymentModerelatedVouchers();

	Map<String, Double> getPaymentModerelatedVoucherswithTotal();


	/**
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */
	SearchPageData<VoucherInvalidationModel> findVoucherHistoryRedeemedOrders(CustomerModel customer, PageableData pageableData);

	/**
	 * @param customer
	 * @return Map<String, Double>
	 */
	Map<String, Double> findVoucherHistoryAllInvalidations(CustomerModel customer);

	/**
	 * @param manuallyselectedvoucher
	 * @return String
	 */
	String getVoucherCode(String manuallyselectedvoucher);

}