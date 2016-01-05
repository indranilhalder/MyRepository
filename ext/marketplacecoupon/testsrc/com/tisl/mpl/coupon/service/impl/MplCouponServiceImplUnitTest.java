/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.coupon.dao.MplCouponDao;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
//@UnitTest
public class MplCouponServiceImplUnitTest
{
	private MplCouponServiceImpl mplCouponServiceImpl;
	private MplCouponDao mplCouponDao;
	private VoucherModel voucher;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplCouponServiceImpl = new MplCouponServiceImpl();
		this.mplCouponDao = Mockito.mock(MplCouponDao.class);
		this.mplCouponServiceImpl.setMplCouponDao(mplCouponDao);
		this.voucher = Mockito.mock(VoucherModel.class);
	}

	@Test
	public void testGetVoucher()
	{
		final List<VoucherModel> voucherList = Arrays.asList(voucher);
		Mockito.when(mplCouponDao.findVoucher()).thenReturn(voucherList);
		final List<VoucherModel> actual = mplCouponServiceImpl.getVoucher();
		Assert.assertEquals(actual, voucherList);
	}


}
