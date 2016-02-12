/**
 *
 */
package com.tisl.mpl.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author TCS
 *
 */
public class CouponUtilityMethods
{

	public static boolean compareDate(final Date start, final Date end, final Date comparableDate)
	{
		boolean status = false;
		if (comparableDate.after(start) && comparableDate.before(end))
		{
			status = true;
		}
		else if (comparableDate.equals(start) || comparableDate.equals(end))
		{
			status = true;
		}
		else
		{
			status = false;
		}
		return status;
	}


	public static int noOfDaysCalculatorBetweenDates(final Date date1, final Date date2)
	{
		int noOfDays = 0;
		final DateFormat df = new SimpleDateFormat("yyyyMMdd");
		if (null != date1 && null != date2)
		{
			Date dateBefore = null, dateAfter = null;
			if (date1.before(date2))
			{
				dateBefore = date1;
				dateAfter = date2;
			}
			else if (date1.after(date2))
			{
				dateBefore = date2;
				dateAfter = date1;
			}
			else if (df.format(date1).equalsIgnoreCase(df.format(date2)))
			{
				dateBefore = date1;
				dateAfter = date1;
			}
			noOfDays = (int) ((dateAfter.getTime() - dateBefore.getTime()) / (1000 * 60 * 60 * 24));
		}
		return noOfDays;
	}

	/**
	 * @Description: validate whether comparableDate lies between date1 and date2
	 * @param date1
	 * @param date2
	 * @param comparableDate
	 * @return true if comparableDate lies between date1 and date2
	 */

	public static boolean doDateValidation(final Date date1, final Date date2, final Date comparableDate)
	{
		boolean status = false;

		if (date1 != null && date2 != null && comparableDate != null)
		{
			final Calendar dateBeforeCal = Calendar.getInstance();
			final Calendar dateAfterCal = Calendar.getInstance();
			final Calendar comparableDateCal = Calendar.getInstance();

			Date dateBefore = null, dateAfter = null;

			if (date1.before(date2))
			{
				dateBefore = date1;
				dateAfter = date2;
			}
			else if (date1.after(date2))
			{
				dateBefore = date2;
				dateAfter = date1;
			}
			else if (date1.equals(date2))
			{
				dateBefore = date1;
				dateAfter = date1;
			}


			dateBeforeCal.setTime(dateBefore);
			dateAfterCal.setTime(dateAfter);
			comparableDateCal.setTime(comparableDate);

			final int diffMnthBfrAndComp = comparableDateCal.get(Calendar.MONTH) - dateBeforeCal.get(Calendar.MONTH);
			final int diffMnthAftrAndComp = dateAfterCal.get(Calendar.MONTH) - comparableDateCal.get(Calendar.MONTH);

			final int diffDayBfrAndComp = comparableDateCal.get(Calendar.DAY_OF_MONTH) - dateBeforeCal.get(Calendar.DAY_OF_MONTH);
			final int diffDayAftrAndComp = dateAfterCal.get(Calendar.DAY_OF_MONTH) - comparableDateCal.get(Calendar.DAY_OF_MONTH);

			if (diffMnthBfrAndComp >= 0 && diffMnthAftrAndComp >= 0 && diffDayBfrAndComp >= 0 && diffDayAftrAndComp >= 0)
			{
				status = true;
			}
		}

		return status;
	}

}