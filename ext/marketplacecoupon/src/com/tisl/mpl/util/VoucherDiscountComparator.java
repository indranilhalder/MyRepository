/**
 *
 */
package com.tisl.mpl.util;

import java.util.Comparator;

import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author 752131
 *
 */
public class VoucherDiscountComparator implements Comparator<VoucherDisplayData>
{


	@Override
	public int compare(final VoucherDisplayData data0, final VoucherDisplayData data1)
	{
		return ((data0.getCouponDiscount() <= data1.getCouponDiscount()) ? 1 : (-1));
	}


}