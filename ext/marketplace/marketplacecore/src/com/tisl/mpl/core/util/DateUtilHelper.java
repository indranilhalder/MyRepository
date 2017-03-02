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

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;


/**
 * @author Tech
 *
 */
public class DateUtilHelper
{
	private static final Logger LOG = Logger.getLogger(DateUtilHelper.class);

	/* DateFromat dd-MM-yyyy HH:mm:ss but it return Only Date With out Time
	 * 
	 */
	public  String getDateFromat(String selectedDate,SimpleDateFormat format){
   	 Date myDate = null;
   	 String dateWithOutTime=null;
			try {
				myDate = format.parse(selectedDate);
				 DateTime today =new DateTime(myDate);
		    	    DateTimeFormatter formatter = DateTimeFormat.forPattern( "dd-MM-yyyy" );
		    	    dateWithOutTime=formatter.print( today);
		    	   LOG.debug( formatter.print( today) );
		    	    } catch (ParseException e) {
		    	   	LOG.error("Time Formater ********:"+e.getMessage());
		    	    }    
   	 
   	 return dateWithOutTime;
    }
	 /* DateFromat dd-MM-yyyy HH:mm:ss but it return Only Time With out Date
		 * 
		 */
	public  String getTimeFromat(String selectedDate){
   	 String timeWithOutDate=null;
			try {
				SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
				SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");
				Date date = parseFormat.parse(selectedDate);
				timeWithOutDate=printFormat.format(date);
				LOG.debug("timeWithOutDate :"+timeWithOutDate);
			} catch (ParseException e) {
				LOG.error("Time Formater ********:"+e.getMessage());
			}
   	 
   	 return timeWithOutDate;
    } 
	 
	 /* Take input has 12:00 AM/PM Format but it returns 24 Hours Format
		 * 
		 */
	
	
	
	public  String convertTo24Hour(String time) {
		DateFormat df = new SimpleDateFormat("hh:mm aa");
		DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		String 	timeIn24hoursFormate =null;
		try{
			date= df.parse(time);
		 	timeIn24hoursFormate = outputformat.format(date);
		 	if(LOG.isDebugEnabled()){
		 		LOG.debug("TwentyFourHourFormate input="+time+" output  "+timeIn24hoursFormate);
		 	}
			
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		return timeIn24hoursFormate;
	}
	
	 /* Take input has 12:00 AM/PM Format but it returns 24 Hours Format
		 * 
		 */
	public  String convertTo12Hour(String time) {
		DateFormat df = new SimpleDateFormat("HH:mm");
		DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
		String 	timeIn12hoursFormate=null;
		Date date = null;
		try{
			date= df.parse(time);
			timeIn12hoursFormate = outputformat.format(date);
			if(LOG.isDebugEnabled()){
		 		LOG.debug("TwelveHourFormate input="+time+" output  "+timeIn12hoursFormate);
			}
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		return timeIn12hoursFormate;
	}
	 /* Take input has Date Format   but it returns 3 next Dates 
		 * 
		 */
	public  List<String> getDeteList(String estDate,SimpleDateFormat format,int numOfDays){
   	 Date myDate = null;
   	 List<String> dateList=null;
			try {
				myDate = format.parse(estDate);
				 DateTime today =new DateTime(myDate);
		    	    DateTimeFormatter formatter = DateTimeFormat.forPattern( "dd-MM-yyyy" );
		    	    dateList=new ArrayList<String>(); 
		    	
		    	    for(int i=0; i<numOfDays; i++){
			    		String dd=formatter.print( today.plusDays( i ) );
			    		
			    	    dateList.add(dd);
		           }
			} catch (ParseException e) {
				LOG.error("Time Formater ********:"+e.getMessage());
			}
			LOG.debug( "Calculated Dates is:"+dateList );
			return dateList;
    }
	 /* Take input has Date Format  but it Return Months Ex AUG 10  
		 * 
		 */
	public  List<String> convertMonthNames(List<String> dateList) {
   	 
   	 List<String> monthList=new ArrayList<String>();
   	 for(String monthformat:dateList){
   		 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    		Date myDate = null;
			try {
				myDate = format.parse(monthformat);
			} catch (ParseException e) {
				LOG.error("Time Formater ********:"+e.getMessage());
			}
    	    DateTime today =new DateTime(myDate);
    	    DateTimeFormatter formatter = DateTimeFormat.forPattern( "MMM dd" ); 
    	    LOG.debug(formatter.print(today));
    	    monthList.add(formatter.print(today));
   	 }
   	 
		return monthList;
   	 
    }
	
   
	public List<String> convertFromAndToTimeSlots(List<MplTimeSlotsModel> modelList){
		List<String> timeSlotsList=new ArrayList<String>();
		for(MplTimeSlotsModel  mm:modelList){
			try {
				  String timeFormat=null;
	           SimpleDateFormat twentyFourHoursSDF = new SimpleDateFormat("HH:mm");
	           SimpleDateFormat twelveHoursSDF = new SimpleDateFormat("hh:mm a");
	           LOG.debug(mm.getFromTime() +" "+ mm.getToTime() );
	           timeFormat =removeLeadingZeros(twelveHoursSDF.format(twentyFourHoursSDF.parse(mm.getFromTime()))) +" TO "+removeLeadingZeros(twelveHoursSDF.format(twentyFourHoursSDF.parse(mm.getToTime())));
	           LOG.debug("^^^^^^TimeSlots Is :********:"+timeFormat);
	           timeSlotsList.add(timeFormat);
			} catch (Exception e) {
				LOG.error("Time Formater ********:"+e.getMessage());
	      }
			 
		}
		return timeSlotsList;
	}
	
	public  String getNextDete(String estDate,SimpleDateFormat format){
	    	 Date myDate = null;
	    	 String dd=null;
				try {
					myDate = format.parse(estDate);
					 DateTime today =new DateTime(myDate);
			    	    DateTimeFormatter formatter = DateTimeFormat.forPattern( "dd-MM-yyyy" );
				    		 dd=formatter.print( today.plusDays( 1 ) );
				} catch (ParseException e) {
					LOG.error("Time Formater ********:"+e.getMessage());
				}
				return dd;
	     }
	
	
	public  List<InvReserForDeliverySlotsItemEDDInfoData> getUniqueEddDatesList(List<InvReserForDeliverySlotsItemEDDInfoData> tempList) {
		List<InvReserForDeliverySlotsItemEDDInfoData> uniqueEddDatesList = new ArrayList<InvReserForDeliverySlotsItemEDDInfoData>();
		Iterator it = tempList.iterator();
		while (it.hasNext()) {
			boolean isExist = false;
			InvReserForDeliverySlotsItemEDDInfoData eddObj = (InvReserForDeliverySlotsItemEDDInfoData) it.next();
			if (uniqueEddDatesList.size() == 0) {
				uniqueEddDatesList.add(eddObj);
			} else {
				Iterator it1 = uniqueEddDatesList.iterator();
				while (it1.hasNext()) {
					InvReserForDeliverySlotsItemEDDInfoData exisEddObj = (InvReserForDeliverySlotsItemEDDInfoData) it1.next();
					
					if (eddObj.getUssId().equals(exisEddObj.getUssId()) ) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			        	Date date1 = null;
			        	Date date2 = null;
						try {
							date1 = sdf.parse(eddObj.getEDD());
							date2 = sdf.parse(exisEddObj.getEDD());
						} catch (ParseException e) {
							LOG.error("Time Formater ********:"+e.getMessage());
						}
			        	if(date1.compareTo(date2)>0){
			        		exisEddObj.setEDD(eddObj.getEDD());
			        	}else{
			        		eddObj.setEDD(exisEddObj.getEDD());	
			        	}
			        	if(eddObj.getIsScheduled().equalsIgnoreCase("N")){
			        		exisEddObj.setIsScheduled("N");
			        	}
						isExist = true;
						break;
					}
				}
				if (!isExist) {
					uniqueEddDatesList.add(eddObj);
				}
			}

		}
		return uniqueEddDatesList;

	}
	
	
	public  List<String> calculatedLpHolidays(String date , int numOfDays){
		boolean ischeck=false;
        List<String> finalDateSet=new ArrayList<String>();
        for(int i=0; i<numOfDays; i++){
        	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
            DateTime dt = formatter.parseDateTime(date);
        	if(dt.plusDays( i ).dayOfWeek().getAsText().equalsIgnoreCase("Sunday") ){
        		ischeck=true;
        	}else if(!ischeck){
        		finalDateSet.add(formatter.print( dt.plusDays( i)));
        	}
        	if(ischeck){
        		finalDateSet.add(formatter.print( dt.plusDays( i+1)));
        	}
        }
		return finalDateSet;
	}
	
	public String removeLeadingZeros(String str) {
		  while (str.indexOf("0")==0)
		    str = str.substring(1);
		  return str;
		}
	
	
   public  String convertTo24HourWithSecodnds(String Time) {
	    DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
	    Date d = null;
	    try {
	        d = f1.parse(Time);
	    } catch (ParseException e) {
	   	 LOG.error("Time Formater ********:"+e.getMessage());
	    }
	    DateFormat f2 = new SimpleDateFormat("HHmmss");
	    String formatedTime = f2.format(d); // "23:00"
	    return formatedTime;
	}
   
   public  String convertDateWithFormat(String sourceDate) {
   	 
		 SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
 		Date myDate = null;
		try {
			myDate = format.parse(sourceDate);
		} catch (ParseException e) {
			 LOG.error("Date Formater ********:"+e.getMessage());
		}
 	    DateTime today =new DateTime(myDate);
 	    DateTimeFormatter formatter = DateTimeFormat.forPattern( "yyyyMMdd" ); 
	return formatter.print(today);
	 
 }
    
}
