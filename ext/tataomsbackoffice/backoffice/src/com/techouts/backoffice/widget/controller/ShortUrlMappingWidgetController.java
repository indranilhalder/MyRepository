/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * this class is used for short url mapping report purpose
 *
 * @author prabhakar
 */
public class ShortUrlMappingWidgetController extends DefaultWidgetController
{
	private static final Logger LOG = LoggerFactory.getLogger(ShortUrlMappingWidgetController.class);
	private String startDate;
	private String endDate;
	private Date fromDate;
	private Date toDate;

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());

		//	shipTxnInfo.setFromDate(cal.getTime());
		//shipTxnInfo.setToDate(new Date());
		//call method to get
		getShortUrlMappingInfo();

	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SuppressWarnings("deprecation")
	@SocketEvent(socketId = "startendDates")
	public void getTshipVSshipReportBySocketEvent(final String startendDates)
	{
		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];

		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		LOG.info(" inside sockent Start Date " + startDate + "******* End Date " + endDate);

		try
		{
			fromDate = dateFormat.parse(startDate);
			toDate = dateFormat.parse(endDate);
		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}

		getShortUrlMappingInfo();

	}

	private void getShortUrlMappingInfo()
	{

	}

}
