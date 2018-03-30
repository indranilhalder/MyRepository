/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.coupon.service.MplCouponService;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.model.MplNoCostEMIVoucherModel;
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
	 * This method returns all active vouchers/offer in payment page
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 *
	 */
	@Override
	public List<MplCartOfferVoucherModel> getPaymentModerelatedVouchers()
	{
		return getMplCouponDao().getPaymentModerelatedVouchers();
	}

	@Override
	public Map<String, Double> getPaymentModerelatedVoucherswithTotal()
	{
		return getMplCouponDao().getPaymentModerelatedVoucherswithTotal();
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

	/**
	 * This method returns all voucher invalidations
	 *
	 * @param customer
	 * @return Map<String, Double>
	 */

	@Override
	public Map<String, Double> getAllVoucherInvalidations(final CustomerModel customer)
	{
		return getMplCouponDao().findVoucherHistoryAllInvalidations(customer);
	}

	public MplCouponDao getMplCouponDao()
	{
		return mplCouponDao;
	}

	public void setMplCouponDao(final MplCouponDao mplCouponDao)
	{
		this.mplCouponDao = mplCouponDao;
	}


	/**
	 * The method checks the applicability of Coupon in Cart / Order
	 *
	 * @param discountList
	 */
	@Override
	public boolean validateCartEligilityForCoupons(final List<DiscountModel> discountList)
	{
		boolean flag = true;

		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountModel discount : discountList)
			{
				if (discount instanceof PromotionVoucherModel && !(discount instanceof MplCartOfferVoucherModel))
				{
					flag = false;
					break;
				}
			}
		}


		return flag;
	}

	/**
	 * The method checks the applicability of Coupon in Cart / Order
	 *
	 * @param discountList
	 */
	@Override
	public boolean validateCartEligilityForCartCoupons(final List<DiscountModel> discountList)
	{
		boolean flag = true;

		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountModel discount : discountList)
			{
				if ((discount instanceof PromotionVoucherModel) && (discount instanceof MplCartOfferVoucherModel))
				{
					flag = false;
					break;
				}
			}
		}


		return flag;
	}



	@Override
	public String getVoucherCode(final String manuallyselectedvoucher)
	{
		return getMplCouponDao().getVoucherCode(manuallyselectedvoucher);
	}


	/**
	 * The Method returns closed coupon details
	 *
	 * @param currentCustomer
	 */
	@Override
	public List<VoucherModel> getClosedVoucherList(final CustomerModel currentCustomer)
	{
		return mplCouponDao.getClosedVoucherList(currentCustomer);
	}


	/**
	 * The Method Return Open Voucher List
	 */
	@Override
	public List<VoucherModel> getOpenVoucherList()
	{
		return mplCouponDao.getOpenVoucherList();
	}


	/**
	 *
	 * Returns Voucher Code for No Cost EMI Coupon code
	 *
	 * @param couponCode
	 */
	@Override
	public String getNoCostEMIVoucherCode(final String couponCode)
	{
		return getMplCouponDao().getNoCostEMIVoucherCode(couponCode);
	}


	/**
	 * The Method checks for eligibility of No Cost EMI Coupons
	 *
	 * @param discountList
	 */
	@Override
	public boolean validateCartEligilityForNoCostEMI(final List<DiscountModel> discountList)
	{
		boolean flag = true;

		if (CollectionUtils.isNotEmpty(discountList))
		{
			for (final DiscountModel discount : discountList)
			{
				if ((discount instanceof PromotionVoucherModel) && (discount instanceof MplCartOfferVoucherModel)
						&& (discount instanceof MplNoCostEMIVoucherModel))
				{
					flag = false;
					break;
				}
			}
		}

		return flag;
	}


}
