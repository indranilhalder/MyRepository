/**
 * 
 */
package com.tisl.mpl.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * @author Techouts
 *
 */
public class MplTimeconverUtility
{
	/**
	 * 
	 * @param time
	 * @return String
	 */
	public static String convert24hoursTo12hours(String time)
	{
		 final Logger LOG = Logger.getLogger(GenericUtilityMethods.class);
		String _12HourTime = null;
		 try {       
          SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
          SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
          Date _24HourDt = _24HourSDF.parse(time);
          _12HourTime=_12HourSDF.format(_24HourDt);
      } 
		 catch (Exception e) {
			 LOG.error(e);
      }
		 return _12HourTime;
	}
	
	/**
	 * 
	 * @param time
	 * @return String
	 */
	public static String convert12hoursTo24hours(String time)
	{
		 final Logger LOG = Logger.getLogger(GenericUtilityMethods.class);
			String _24HourTime = null;
			 try { 
		SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
      SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
      Date date = _24HourSDF.parse(time);
      _24HourTime=_12HourSDF.format(date);
		      } 
				 catch (Exception e) {
					 LOG.error(e);
		      }
				 return _24HourTime;
	}
	
	/**
	 * 
	 * @param time
	 * @return String
	 */
	public static List<String> splitTime(String time)
	{
		 List<String> toAndFrom=new ArrayList<String>();
		 
		 String [] string =StringUtils.split(time, "-");
		 
		 String fromTime=string[0];
		 String toTime=string[1];
		 
		if(StringUtils.isNotEmpty(fromTime))
		{
			toAndFrom.add(fromTime);
		}
		
		if(StringUtils.isNotEmpty(string[1]))
		{
			toAndFrom.add(toTime);
		}	 
		
		 return toAndFrom;
	}
	
	
	

}
