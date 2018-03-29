/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.model.MplCartOfferVoucherModel;


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
	 * TPR-7486
	 */
	List<MplCartOfferVoucherModel> getPaymentModerelatedVouchers();

	Map<String, Double> getPaymentModerelatedVoucherswithTotal();


	/**
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */
	SearchPageData<VoucherInvalidationModel> getVoucherRedeemedOrder(CustomerModel customer, PageableData pageableData);

	/**
	 * @param customer
	 * @return Map<String, Double>
	 */
	Map<String, Double> getAllVoucherInvalidations(CustomerModel customer);


	boolean validateCartEligilityForCoupons(List<DiscountModel> discountList);

	boolean validateCartEligilityForCartCoupons(List<DiscountModel> discountList);


	String getVoucherCode(String manuallyselectedvoucher);

	/**
	 * The Method returns closed voucher details
	 *
	 * @param currentCustomer
	 * @return List<VoucherModel>
	 */
	List<VoucherModel> getClosedVoucherList(CustomerModel currentCustomer);

	/**
	 * The Method return Open Voucher List with Visibility set as 1
	 *
	 * @return List<VoucherModel>
	 */
	List<VoucherModel> getOpenVoucherList();

}