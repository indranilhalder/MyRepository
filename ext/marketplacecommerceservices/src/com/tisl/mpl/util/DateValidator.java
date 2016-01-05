package com.tisl.mpl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;




/**
 * @author TCS
 *
 */
public class DateValidator
{
	private static final String DATE_PATTERN_1 = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)"; // DD/MM/YYYY

	public static void main(final String[] args)
	{
		dateValidate("29/02/2004", "/");
	}

	public static boolean dateValidate(final String date, final String splitter)
	{
		boolean isDateValid = date.matches(DATE_PATTERN_1);

		if (isDateValid)
		{
			final String dobArray[] = date.split("\\" + splitter);
			final String dobDay = dobArray[0];
			final String dobMon = dobArray[1];
			final String dobYear = dobArray[2];

			if ("31".equals(dobDay)
					&& ("4".equals(dobMon) || "6".equals(dobMon) || "9".equals(dobMon) || "11".equals(dobMon) || "04".equals(dobMon)
							|| "06".equals(dobMon) || "09".equals(dobMon)))
			{
				isDateValid = false; // only 1,3,5,7,8,10,12 has 31 days
			}
			else if ("2".equals(dobMon) || "02".equals(dobMon))
			{
				if (Integer.parseInt(dobYear) % 4 == 0)
				{
					if ("30".equals(dobDay) || "31".equals(dobDay))
					{
						isDateValid = false;
					}
					else
					{
						isDateValid = true;
					}
				}
				else
				{
					if ("29".equals(dobDay) || "30".equals(dobDay) || "31".equals(dobDay))
					{
						isDateValid = false;
					}
					else
					{
						isDateValid = true;
					}
				}
			}
			else
			{
				isDateValid = true;
			}
		}
		return isDateValid;
	}


	public static boolean checkFutureDate(final String date, final String dateFormatPattern)
	{
		boolean isDateInvalid = false;
		final SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormatPattern);
		final SimpleDateFormat sdfDateInt = new SimpleDateFormat("yyyyMMdd");

		Date doBirth = null;
		try
		{
			doBirth = sdfDate.parse(date);
		}
		catch (final ParseException e)
		{
			throw new EtailNonBusinessExceptions(e);
		}

		final String intDob = sdfDateInt.format(doBirth);
		final String intCurrDate = sdfDateInt.format(new Date());

		if (intDob.compareTo(intCurrDate) > 0)
		{
			isDateInvalid = true;
		}

		return isDateInvalid;
	}
}
