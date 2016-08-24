/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.facade.ShortUrlFacade;
import com.hybris.oms.tata.renderer.ShortUrlReportItemRenderer;
import com.techouts.backoffice.ShortUrlReportData;


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

	@Wire
	private Listbox listBoxData;
	@WireVariable
	private ShortUrlFacade shortUrlFacade;

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
		getShortUrlMappingInfo(cal.getTime(), new Date());

	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SuppressWarnings("deprecation")
	@SocketEvent(socketId = "startendDates")
	public void shortUrlTrackingReportBySocketEvent(final String startendDates)
	{
		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];

		LOG.info(" shortUrlTrackingReportBySocketEvent " + startDate + "******* End Date " + endDate);
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


		try
		{

			fromDate = formatter.parse(startDate);
			toDate = formatter.parse(endDate);


		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}

		getShortUrlMappingInfo(fromDate, toDate);

	}

	private void getShortUrlMappingInfo(final Date fromDate, final Date toDate)
	{
		final List<ShortUrlReportData> shortUrlData = shortUrlFacade.getShortUrlReportModels(fromDate, toDate);
		listBoxData.setModel(new ListModelList<ShortUrlReportData>(shortUrlData));
		listBoxData.setItemRenderer(new ShortUrlReportItemRenderer());
	}

}