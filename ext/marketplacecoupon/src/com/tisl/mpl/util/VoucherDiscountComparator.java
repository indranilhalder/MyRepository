/**
 *
 */
package com.tisl.mpl.util;

import java.util.Comparator;

import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author TCS
 *
 */
public class VoucherDiscountComparator implements Comparator<VoucherDisplayData>
{


	/**
	 * This method compares two VoucherDisplayData for comparing
	 *
	 * @param data0
	 * @param data1
	 * @return int
	 *
	 */
	@Override
	public int compare(final VoucherDisplayData data0, final VoucherDisplayData data1)
	{
		return ((data0.getCouponDiscount() <= data1.getCouponDiscount()) ? 1 : (-1));
	}


}