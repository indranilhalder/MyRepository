/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.util.VoucherDiscountComparator;


/**
 * @author TCS
 *
 */
public class MplCouponServiceImpl implements MplCouponService
{
	@Resource(name = "mplCouponDao")
	private MplCouponDao mplCouponDao;

	/**
	 * This method returns all active vouchers
	 *
	 * return List<VoucherModel>
	 */
	@Override
	public List<VoucherModel> getVoucher()
	{
		return getMplCouponDao().findVoucher();
	}


	/**
	 * This method sorts the voucher data
	 *
	 * @param voucherDataList
	 * @return ArrayList<VoucherDisplayData>
	 */
	@Override
	public List<VoucherDisplayData> getSortedVoucher(final List<VoucherDisplayData> voucherDataList)
	{
		Collections.sort(voucherDataList, new VoucherDiscountComparator());
		return voucherDataList;
	}

	/**
	 * This method returns all active and closed vouchers
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 *
	 */
	@Override
	public SearchPageData<VoucherModel> getClosedVoucher(final CustomerModel customer, final PageableData pageableData)
	{
		return getMplCouponDao().findClosedVoucher(customer, pageableData);
	}

	/**
	 * This method returns all voucher invalidations
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */

	@Override
	public SearchPageData<VoucherInvalidationModel> getVoucherRedeemedOrder(final CustomerModel customer,
			final PageableData pageableData)
	{
		return getMplCouponDao().findVoucherHistoryRedeemedOrders(customer, pageableData);
	}


	public MplCouponDao getMplCouponDao()
	{
		return mplCouponDao;
	}

	public void setMplCouponDao(final MplCouponDao mplCouponDao)
	{
		this.mplCouponDao = mplCouponDao;
	}


}
