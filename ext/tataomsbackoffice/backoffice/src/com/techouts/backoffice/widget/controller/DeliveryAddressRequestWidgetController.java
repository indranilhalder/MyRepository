/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import com.hybris.oms.tata.renderer.DeliveryAdressReportItemRenderer;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;

/**
 * this class is used for Delivery Address request report purposes
 *
 * @author prabhakar
 *
 */
public class DeliveryAddressRequestWidgetController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryAddressRequestWidgetController.class);
	@Wire
	private Datebox startDateValue;
	@Wire
	private Datebox endDateValue;
	
	@Wire
	private Listbox listBoxData;

	@WireVariable(value = "deliveryAddressFacade")
	private MplDeliveryAddressFacade mplDeliveryAddressFacade;

	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		startDateValue.setValue(cal.getTime());
		endDateValue.setValue(new Date());	
		getDeliveryAddressRequestInfo(cal.getTime(), new Date());

	}

	/**
	 * This method is to Get the selected  date
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
		getDeliveryAddressRequestInfo(startDateValue.getValue(), endDateValue.getValue());

	}

	/**
	 * This method is to Get the selected end date
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
		getDeliveryAddressRequestInfo(startDateValue.getValue(), endDateValue.getValue());
	}
	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}
	private void getDeliveryAddressRequestInfo(final Date dateFrom, final Date toDate)
	{
		final Collection<MplDeliveryAddressReportData> deliveryAddressData = mplDeliveryAddressFacade
				.getDeliveryAddressRepot(dateFrom, toDate);
		listBoxData.setModel(new ListModelList<MplDeliveryAddressReportData>(deliveryAddressData));
		listBoxData.setItemRenderer(new DeliveryAdressReportItemRenderer());
	}
}
