/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.util.VoucherDiscountComparator;


/**
 * @author 752131
 *
 */
public class MplCouponServiceImpl implements MplCouponService
{
	private static final Logger LOG = Logger.getLogger(MplCouponServiceImpl.class);
	@Autowired
	private MplCouponDao mplCouponDao;

	/**
	 * return List<VoucherModel>
	 */
	@Override
	public List<VoucherModel> getVoucher()
	{
		return getMplCouponDao().findVoucher();
	}


	/**
	 *
	 * @param voucherDataList
	 * @return ArrayList<VoucherDisplayData>
	 */
	@Override
	public List<VoucherDisplayData> getSortedVoucher(final List<VoucherDisplayData> voucherDataList)
	{
		LOG.debug("Inside Sorted Voucher Service Impl");
		Collections.sort(voucherDataList, new VoucherDiscountComparator());
		return voucherDataList;
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