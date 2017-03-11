/**
 *
 */
package com.tisl.mpl.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.mplconfig.dao.MplConfigDao;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;


/**
 * @author Tech
 *
 */
public class DateUtilHelper
{
	private static final Logger LOG = Logger.getLogger(DateUtilHelper.class);
	
	public static final String SUNDAY = "Sunday";
	public static final String MONDAY = "Monday";
	public static final String TUESDAY = "Tuesday";
	public static final String WEDNESDAY = "Wednesday";
	public static final String THURSDAY = "Thursday";
	public static final String FRIDAY = "Friday";
	public static final String SATURDAY = "Saturday";

	/*
	 * DateFromat dd-MM-yyyy HH:mm:ss but it return Only Date With out Time
	 */
	public String getDateFromat(final String selectedDate, final SimpleDateFormat format)
	{
		Date myDate = null;
		String dateWithOutTime = null;
		try
		{
			myDate = format.parse(selectedDate);
			final DateTime today = new DateTime(myDate);
			final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
			dateWithOutTime = formatter.print(today);
			LOG.debug(formatter.print(today));
		}
		catch (final ParseException e)
		{
			LOG.error("Time Formater ********:" + e.getMessage());
		}

		return dateWithOutTime;
	}

	/*
	 * DateFromat dd-MM-yyyy HH:mm:ss but it return Only Time With out Date
	 */
	public String getTimeFromat(final String selectedDate)
	{
		String timeWithOutDate = null;
		try
		{
			final SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			final SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");
			final Date date = parseFormat.parse(selectedDate);
			timeWithOutDate = printFormat.format(date);
			LOG.debug("timeWithOutDate :" + timeWithOutDate);
		}
		catch (final ParseException e)
		{
			LOG.error("Time Formater ********:" + e.getMessage());
		}

		return timeWithOutDate;
	}

	/*
	 * Take input has 12:00 AM/PM Format but it returns 24 Hours Format
	 */



	public String convertTo24Hour(final String time)
	{
		final DateFormat df = new SimpleDateFormat("hh:mm aa");
		final DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		String timeIn24hoursFormate = null;
		try
		{
			date = df.parse(time);
			timeIn24hoursFormate = outputformat.format(date);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("TwentyFourHourFormate input=" + time + " output  " + timeIn24hoursFormate);
			}

		}
		catch (final ParseException pe)
		{
			pe.printStackTrace();
		}
		return timeIn24hoursFormate;
	}

	/*
	 * Take input has 12:00 AM/PM Format but it returns 24 Hours Format
	 */
	public String convertTo12Hour(final String time)
	{
		final DateFormat df = new SimpleDateFormat("HH:mm");
		final DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
		String timeIn12hoursFormate = null;
		Date date = null;
		try
		{
			date = df.parse(time);
			timeIn12hoursFormate = outputformat.format(date);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("TwelveHourFormate input=" + time + " output  " + timeIn12hoursFormate);
			}
		}
		catch (final ParseException pe)
		{
			pe.printStackTrace();
		}
		return timeIn12hoursFormate;
	}

	/*
	 * Take input has Date Format but it returns 3 next Dates
	 */
	public List<String> getDeteList(final String estDate, final SimpleDateFormat format, final int numOfDays)
	{
		Date myDate = null;
		List<String> dateList = null;
		try
		{
			myDate = format.parse(estDate);
			final DateTime today = new DateTime(myDate);
			final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
			dateList = new ArrayList<String>();

			for (int i = 0; i < numOfDays; i++)
			{
				final String dd = formatter.print(today.plusDays(i));

				dateList.add(dd);
			}
		}
		catch (final ParseException e)
		{
			LOG.error("Time Formater ********:" + e.getMessage());
		}
		LOG.debug("Calculated Dates is:" + dateList);
		return dateList;
	}

	/*
	 * Take input has Date Format but it Return Months Ex AUG 10
	 */
	public List<String> convertMonthNames(final List<String> dateList)
	{

		final List<String> monthList = new ArrayList<String>();
		for (final String monthformat : dateList)
		{
			final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date myDate = null;
			try
			{
				myDate = format.parse(monthformat);
			}
			catch (final ParseException e)
			{
				LOG.error("Time Formater ********:" + e.getMessage());
			}
			final DateTime today = new DateTime(myDate);
			final DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd");
			LOG.debug(formatter.print(today));
			monthList.add(formatter.print(today));
		}

		return monthList;

	}


	public List<String> convertFromAndToTimeSlots(final List<MplTimeSlotsModel> modelList)
	{
		final List<String> timeSlotsList = new ArrayList<String>();
		for (final MplTimeSlotsModel mm : modelList)
		{
			try
			{
				String timeFormat = null;
				final SimpleDateFormat twentyFourHoursSDF = new SimpleDateFormat("HH:mm");
				final SimpleDateFormat twelveHoursSDF = new SimpleDateFormat("hh:mm a");
				LOG.debug(mm.getFromTime() + " " + mm.getToTime());
				timeFormat = removeLeadingZeros(twelveHoursSDF.format(twentyFourHoursSDF.parse(mm.getFromTime()))) + " TO "
						+ removeLeadingZeros(twelveHoursSDF.format(twentyFourHoursSDF.parse(mm.getToTime())));
				LOG.debug("^^^^^^TimeSlots Is :********:" + timeFormat);
				timeSlotsList.add(timeFormat);
			}
			catch (final Exception e)
			{
				LOG.error("Time Formater ********:" + e.getMessage());
			}

		}
		return timeSlotsList;
	}

	public String getNextDete(final String estDate, final SimpleDateFormat format)
	{
		Date myDate = null;
		String dd = null;
		try
		{
			myDate = format.parse(estDate);
			final DateTime today = new DateTime(myDate);
			final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
			dd = formatter.print(today.plusDays(1));
		}
		catch (final ParseException e)
		{
			LOG.error("Time Formater ********:" + e.getMessage());
		}
		return dd;
	}


	public List<InvReserForDeliverySlotsItemEDDInfoData> getUniqueEddDatesList(
			final List<InvReserForDeliverySlotsItemEDDInfoData> tempList)
	{
		final List<InvReserForDeliverySlotsItemEDDInfoData> uniqueEddDatesList = new ArrayList<InvReserForDeliverySlotsItemEDDInfoData>();
		final Iterator it = tempList.iterator();
		while (it.hasNext())
		{
			boolean isExist = false;
			final InvReserForDeliverySlotsItemEDDInfoData eddObj = (InvReserForDeliverySlotsItemEDDInfoData) it.next();
			if (uniqueEddDatesList.size() == 0)
			{
				uniqueEddDatesList.add(eddObj);
			}
			else
			{
				final Iterator it1 = uniqueEddDatesList.iterator();
				while (it1.hasNext())
				{
					final InvReserForDeliverySlotsItemEDDInfoData exisEddObj = (InvReserForDeliverySlotsItemEDDInfoData) it1.next();

					if (eddObj.getUssId().equals(exisEddObj.getUssId()))
					{
						final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
						Date date1 = null;
						Date date2 = null;
						try
						{
							date1 = sdf.parse(eddObj.getEDD());
							date2 = sdf.parse(exisEddObj.getEDD());
						}
						catch (final ParseException e)
						{
							LOG.error("Time Formater ********:" + e.getMessage());
						}
						if (date1.compareTo(date2) > 0)
						{
							exisEddObj.setEDD(eddObj.getEDD());
						}
						else
						{
							eddObj.setEDD(exisEddObj.getEDD());
						}
						if (eddObj.getIsScheduled().equalsIgnoreCase("N"))
						{
							exisEddObj.setIsScheduled("N");
						}
						isExist = true;
						break;
					}
				}
				if (!isExist)
				{
					uniqueEddDatesList.add(eddObj);
				}
			}

		}
		return uniqueEddDatesList;

	}


	public List<String> calculatedLpHolidays(String workingDays,final String date, final int requiredNumberOfDays)
	{
		try {
			List<String> calculatedDates = new ArrayList<String>();
			if(LOG.isInfoEnabled()) {
				LOG.info("Calculating next "+requiredNumberOfDays+" days with LP Holidays for the given date"+date);
			}
				if(null !=workingDays && !workingDays.isEmpty() ){
					String[] workingDaysList =  workingDays.split(",");
					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
					DateTime dt = formatter.parseDateTime(date);
					int numberOfWorkingDays = workingDaysList.length;
					if(LOG.isDebugEnabled()) {
						LOG.debug(" number LP WorkingDays="+numberOfWorkingDays);
					}
					int count=0;
					String startingDay = getStartingDay(dt);
					int starTDay = Integer.valueOf(startingDay);
					for (int i=starTDay,j=0; ;i++,j++) {
						String day = String.valueOf(i);
						if( ArrayUtils.contains( workingDaysList,day ) )   {
							if(LOG.isDebugEnabled()) {
								LOG.debug(" day "+formatter.print( dt.plusDays(j))+" " +  dt.plusDays(j).dayOfWeek().getAsText()+"  is not a holiday");
							}
							calculatedDates.add(formatter.print( dt.plusDays( j)));
							count++;
							if(count==requiredNumberOfDays){
								if(LOG.isDebugEnabled()) {
									LOG.debug("requiredNumberOfDays  "+requiredNumberOfDays+" added days count  "+count);
								}
								break;
							}
						}else {
							if(LOG.isDebugEnabled()) {
								LOG.debug(" day "+formatter.print( dt.plusDays(j))+" " +  dt.plusDays(j).dayOfWeek().getAsText()+"  is  a holiday");
							}
						}
						if(i==6) {
							i=0;
							i--;
						}
					}
				}
				if(LOG.isDebugEnabled()) {
					LOG.debug("final dates with LP holidays for the given date"+date+" are"+calculatedDates);
				}
				return calculatedDates;
			
		}catch(Exception e) {
			LOG.error("Exception while getting the LpHolidays "+e.getMessage());
		}
		return null;
		//		boolean ischeck = false;
		//		final List<String> finalDateSet = new ArrayList<String>();
		//		for (int i = 0; i < numOfDays; i++)
		//		{
		//			final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
		//			final DateTime dt = formatter.parseDateTime(date);
		//			if (dt.plusDays(i).dayOfWeek().getAsText().equalsIgnoreCase("Sunday"))
		//			{
		//				ischeck = true;
		//			}
		//			else if (!ischeck)
		//			{
		//				finalDateSet.add(formatter.print(dt.plusDays(i)));
		//			}
		//			if (ischeck)
		//			{
		//				finalDateSet.add(formatter.print(dt.plusDays(i + 1)));
		//			}
		//		}
		//		return finalDateSet;
	}

	/**
	 * @param dt
	 * @return
	 */
	private String getStartingDay(DateTime date)
	{

      if(null != date) {
   	   if(date.dayOfWeek().getAsText().equalsIgnoreCase(SUNDAY)) {
   		   return "0";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(MONDAY)) {
   		   return "1";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(TUESDAY)) {
   		   return "2";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(WEDNESDAY)) {
   		   return "3";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(THURSDAY)) {
   		   return "4";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(FRIDAY)) {
   		   return "5";
   	   }else if(date.dayOfWeek().getAsText().equalsIgnoreCase(SATURDAY)) {
   		   return "6";
   	   }
      }
	return null;
	}

	public String removeLeadingZeros(String str)
	{
		while (str.indexOf("0") == 0)
		{
			str = str.substring(1);
		}
		return str;
	}


	public String convertTo24HourWithSecodnds(final String Time)
	{
		final DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
		Date d = null;
		try
		{
			d = f1.parse(Time);
		}
		catch (final ParseException e)
		{
			LOG.error("Time Formater ********:" + e.getMessage());
		}
		final DateFormat f2 = new SimpleDateFormat("HHmmss");
		final String formatedTime = f2.format(d); // "23:00"
		return formatedTime;
	}

	public String convertDateWithFormat(final String sourceDate)
	{

		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date myDate = null;
		try
		{
			myDate = format.parse(sourceDate);
		}
		catch (final ParseException e)
		{
			LOG.error("Date Formater ********:" + e.getMessage());
		}
		final DateTime today = new DateTime(myDate);
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
		return formatter.print(today);

	}

}
