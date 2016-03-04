/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tisl.mpl.data.CouponHistoryData;


/**
 * @author TCS
 *
 */
public class MplVoucherTransactionHistoryPopulator implements Populator<VoucherInvalidationModel, CouponHistoryData>
{
	@Override
	public void populate(final VoucherInvalidationModel source, final CouponHistoryData target)
	{
		final VoucherModel voucher = source.getVoucher();
		final OrderModel order = source.getOrder();

		if (voucher instanceof PromotionVoucherModel)

		{
			final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) source.getVoucher();
			if (null != promoVoucher.getVoucherCode() && !promoVoucher.getVoucherCode().isEmpty())
			{
				target.setCouponCode(promoVoucher.getVoucherCode());
			}
			else
			{
				target.setCouponCode("");
			}
			target.setCouponDescription(promoVoucher.getDescription());
			target.setOrderCode(order.getCode());
			final String couponRedeemedDate = getCouponRedeemedDate(order.getDate());
			if (null != couponRedeemedDate)
			{
				target.setRedeemedDate(couponRedeemedDate);
			}
		}

	}

	/**
	 * @Description: This method returns the coupon redeemed date
	 * @param fmtDate
	 * @return String
	 *
	 */
	private String getCouponRedeemedDate(final Date fmtDate)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
		final String couponRedeemedDate = formatter.format(fmtDate);
		return couponRedeemedDate;
	}

}
