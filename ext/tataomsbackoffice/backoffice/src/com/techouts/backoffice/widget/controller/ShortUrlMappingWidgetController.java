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
import com.hybris.oms.tata.renderer.ShortUrlReportItemRenderer;
import com.tis.mpl.facade.shorturl.ShortUrlReportData;
import com.tis.mpl.facade.shorturl.ShortUrlTrackFacade;


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
	private Datebox startDateValue;
	@Wire
	private Datebox endDateValue;

	@Wire
	private Listbox listBoxData;

	@WireVariable("shortUrlFacade")
	private ShortUrlTrackFacade shortUrlFacade;
	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		startDateValue.setValue(cal.getTime());
		endDateValue.setValue(new Date());
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		getShortUrlMappingInfo(cal.getTime(), new Date());
	}


	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "startDateValue", eventName = Events.ON_CHANGE)
	public void getStartdpic()
	{

		if (startDateValue.getValue().after(endDateValue.getValue())) //this kind of comparison ,it will check date along with time sec
		{
			msgBox("Start date must be less than End date");
			return;
		}
		LOG.info("Start date " + startDateValue.getValue() + "end date " + endDateValue.getValue() + "output socket sended");
		getShortUrlMappingInfo(startDateValue.getValue(), endDateValue.getValue());

	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "endDateValue", eventName = Events.ON_CHANGE)
	public void getEnddpic()
	{
		if (endDateValue.getValue().before(startDateValue.getValue()))
		{
			msgBox("End date must be greater than  Start date");
			return;
		}

		LOG.info("Start date " + startDateValue.getValue() + "end date " + endDateValue.getValue() + "output socket sended");
		getShortUrlMappingInfo(startDateValue.getValue(), endDateValue.getValue());
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