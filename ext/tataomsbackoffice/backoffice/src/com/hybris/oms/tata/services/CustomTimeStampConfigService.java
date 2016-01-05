/**
 * 
 */
package com.hybris.oms.tata.services;

/**
 * @author Saood
 * 
 */
public class CustomTimeStampConfigService
{

	public Integer[] getHourMinutes(final String time)
	{

		final char[] charMethod = time.toCharArray();
		final Integer[] hhmm = new Integer[2];
		int hIndex = 0;
		int mIndex = 0;
		for (int i = 0; i < charMethod.length; i++)
		{

			if (charMethod[i] == 'h' || charMethod[i] == 'H')
			{
				hIndex = i;
				i += 1;
				continue;
			}
			if (charMethod[i] == 'm' || charMethod[i] == 'M')
			{
				mIndex = i;
				break;
			}

		}
		hhmm[0] = Integer.valueOf(time.substring(0, hIndex).trim());

		hhmm[1] = Integer.valueOf(time.substring(hIndex + 2, mIndex).trim());

		return hhmm;
	}

	public String getTimeInMinutes(final int hours, final int minutes)
	{

		return String.valueOf(hours * 60 + minutes);
	}

	public String customDateFormat(final String timeDuration)
	{
		StringBuilder hhmm = new StringBuilder();
		final int timeDurationinInt = Integer.parseInt(timeDuration);
		final int hours = timeDurationinInt / 60; //since both are ints,
		final int minutes = timeDurationinInt % 60;
		hhmm = hhmm.append(hours).append(" Hr ").append(minutes).append(" min");
		return hhmm.toString();

	}


}
