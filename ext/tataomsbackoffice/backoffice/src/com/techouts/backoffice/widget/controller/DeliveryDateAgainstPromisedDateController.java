/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * @author prabhakar
 *
 */
public class DeliveryDateAgainstPromisedDateController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryDateAgainstPromisedDateController.class);
	private String startDate;
	private String endDate;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void getDeliveryBreachDataByDates(final String startendDates)
	{


		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		LOG.info(" SShip Breach " + startDate + "******* End Date " + endDate);
	}

}
