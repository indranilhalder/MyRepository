/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.CouponHistoryData;


/**
 * @author TCS
 *
 */
public class MplVoucherTransactionHistoryPopulator implements Populator<VoucherInvalidationModel, CouponHistoryData>
{

	// Month list
	private static final String JANUARY = "January";
	private static final String FEBRUARY = "February";
	private static final String MARCH = "March";
	private static final String APRIL = "April";
	private static final String MAY = "May";
	private static final String JUNE = "June";
	private static final String JULY = "July";
	private static final String AUGUST = "August";
	private static final String SEPTEMBER = "September";
	private static final String OCTOBER = "October";
	private static final String NOVEMBER = "November";
	private static final String DECEMBER = "December";
	// Month list

	public static final String SINGLE_SPACE = " ";
	public static final String PARENT = "parent";
	public static final String BOXING = "boxing";

	@Override
	public void populate(final VoucherInvalidationModel source, final CouponHistoryData target)
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		final VoucherModel voucher = source.getVoucher();

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
			target.setOrderCode(source.getOrder().getCode());
			if (null != getCouponRedeemedDate(source.getOrder().getDate()))
			{
				target.setRedeemedDate(getCouponRedeemedDate(source.getOrder().getDate()));
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
		String finalCouponRedeemedDate = "";
		if (fmtDate != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fmtDate);
			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH);
			final int day = cal.get(Calendar.DAY_OF_MONTH);
			final String strMonth = getMonthFromInt(month).substring(0, 3);
			String dayPrefix = "";

			if (day < 10)
			{
				dayPrefix = "0";

			}
			else
			{
				dayPrefix = "";
			}
			finalCouponRedeemedDate = strMonth + SINGLE_SPACE + dayPrefix + day + SINGLE_SPACE + year;
		}
		return finalCouponRedeemedDate;
	}


	/**
	 * @Description: This method derives the month from integer value
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)

	{
		final List<String> months = Arrays.asList(JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER,
				NOVEMBER, DECEMBER);
		final String strMonth = months.get(month);
		return strMonth;

	}

}
