/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.ViewEvent;
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
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ShortUrlMappingWidgetController.class);

	@Wire
	private Datebox startdpic;
	@Wire
	private Datebox enddpic;

	@Wire
	private Listbox listBoxData;

	@WireVariable
	private ShortUrlFacade shortUrlFacade;
	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		startdpic.setValue(cal.getTime());
		enddpic.setValue(new Date());

		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		getShortUrlMappingInfo(cal.getTime(), new Date());
	}


	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "startdpic", eventName = Events.ON_CHANGE)
	public void getStartdpic()
	{

		if (startdpic.getValue() == null || enddpic.getValue() == null)
		{
			return;
		}

		if (dateFormat.format(startdpic.getValue()).compareTo(dateFormat.format(enddpic.getValue())) > 0)
		{
			msgBox("Start date must be less than or equal to End date");
			return;
		}

		LOG.info("Start date " + startdpic.getValue() + "end date " + enddpic.getValue() + "output socket sended");
		getShortUrlMappingInfo(startdpic.getValue(), enddpic.getValue());

	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "enddpic", eventName = Events.ON_CHANGE)
	public void getEnddpic()
	{
		if (startdpic.getValue() == null)
		{
			msgBox("Please choose the Start Date");
			return;
		}
		if (enddpic.getValue() == null)
		{
			msgBox("Please choose the End Date");
			return;
		}
		if (dateFormat.format(startdpic.getValue()).compareTo(dateFormat.format(enddpic.getValue())) > 0)
		{
			msgBox("End date must be greater than or equal to Start date");
			return;
		}

		LOG.info("Start date " + startdpic.getValue() + "end date " + enddpic.getValue() + "output socket sended");
		getShortUrlMappingInfo(startdpic.getValue(), enddpic.getValue());
	}

	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}


	private void getShortUrlMappingInfo(final Date fromDate, final Date toDate)
	{
		final List<ShortUrlReportData> shortUrlData = shortUrlFacade.getShortUrlReportModels(fromDate, toDate);
		listBoxData.setModel(new ListModelList<ShortUrlReportData>(shortUrlData));
		listBoxData.setItemRenderer(new ShortUrlReportItemRenderer());
	}

}