/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author 752131
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
	ArrayList<VoucherDisplayData> getSortedVoucher(ArrayList<VoucherDisplayData> voucherDataList);

}