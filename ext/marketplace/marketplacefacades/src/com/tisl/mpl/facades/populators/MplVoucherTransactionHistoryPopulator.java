/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercefacades.voucher.impl.DefaultVoucherFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;


/**
 * @author TCS
 *
 */
public class MplVoucherTransactionHistoryPopulator implements Populator<VoucherInvalidationModel, CouponHistoryData>
{
	@Autowired
	private DefaultVoucherFacade defaultVoucherFacade;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

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
		VoucherData voucherData = new VoucherData();
		final VoucherModel voucher = source.getVoucher();
		final boolean isOrderDateValid = true;

		if (voucher instanceof PromotionVoucherModel) //type casting to PromotionVoucherModel

		{
			try
			{
				if (source.getOrder().getType().equalsIgnoreCase(PARENT))
				{
					voucherData = getDefaultVoucherFacade().getVoucher(((PromotionVoucherModel) voucher).getVoucherCode());
					final OrderData orderDetailsData = mplCheckoutFacade.getOrderDetailsForCode(source.getOrder().getCode());
					//isOrderDateValid = checkTransactionDateValidity(orderDetailsData.getCreated());
					final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) source.getVoucher();

					if (isOrderDateValid && null != promoVoucher.getVoucherCode() && null != orderDetailsData.getCode())
					{
						target.setCouponCode(voucherData.getVoucherCode());
						target.setCouponDescription(voucherData.getDescription());
						target.setOrderCode(orderDetailsData.getCode());
						target.setRedeemedDate(getCouponRedeemedDate(orderDetailsData.getCreated()));
					}
				}
			}
			catch (final VoucherOperationException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Description: This method restricts orders in last six months
	 * @param orderCreationDate
	 * @return boolean
	 */
	/*
	 * private boolean checkTransactionDateValidity(final Date orderCreationDate) { boolean isDateValid = false; if
	 * (orderCreationDate != null) { final Calendar endCalendar = Calendar.getInstance(); final Calendar startCalendar =
	 * Calendar.getInstance(); final SimpleDateFormat dateFormatforMONTH = new java.text.SimpleDateFormat(
	 * MarketplacecommerceservicesConstants.COUPONS_TXN_DATE_FORMAT);
	 * 
	 * 
	 * endCalendar.setTime(new Date()); startCalendar.setTime(orderCreationDate);
	 * 
	 * final int endYear = endCalendar.get(Calendar.YEAR); final int endMonth =
	 * Integer.parseInt(dateFormatforMONTH.format(endCalendar.getTime())); final int endDay =
	 * endCalendar.get(Calendar.DAY_OF_MONTH);
	 * 
	 * 
	 * final int startYear = startCalendar.get(Calendar.YEAR); final int startMonth =
	 * Integer.parseInt(dateFormatforMONTH.format(startCalendar.getTime())); final int startDay =
	 * startCalendar.get(Calendar.DAY_OF_MONTH);
	 * 
	 * final DateTime startDate = new DateTime().withDate(startYear, startMonth, startDay); final DateTime endDate = new
	 * DateTime().withDate(endYear, endMonth, endDay);
	 * 
	 * 
	 * final Months monthsBetween = Months.monthsBetween(startDate, endDate); final int monthsBetweenInt =
	 * monthsBetween.getMonths();
	 * 
	 * if (monthsBetweenInt < 6) { isDateValid = true; } } return isDateValid; }
	 */
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



	public DefaultVoucherFacade getDefaultVoucherFacade()
	{
		return defaultVoucherFacade;
	}

}
