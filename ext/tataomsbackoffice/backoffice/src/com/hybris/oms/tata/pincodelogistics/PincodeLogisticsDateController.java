/**
 *
 */
package com.hybris.oms.tata.pincodelogistics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;



/**
 * @author Nagarjuna
 *
 */
public class PincodeLogisticsDateController extends DefaultWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Datebox startdpic;
	private Datebox enddpic;
	@Wire
	private Listbox shipmentListBox;

	private static final Logger LOG = LoggerFactory.getLogger(PincodeLogisticsDateController.class);
	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		startdpic.setValue(cal.getTime());
		enddpic.setValue(new Date());
		super.initialize(comp);


	}


	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "startdpic", eventName = Events.ON_CHANGE)
	public void getStartdpic()
	{
		if (startdpic.getValue().after(enddpic.getValue())) //this kind of comparison ,it will check date along with time sec
		{
			msgBox("Start date must be less than End date");
			return;
		}


		sendOutput("startendDates", dateFormat.format(startdpic.getValue()) + "," + dateFormat.format(enddpic.getValue()));
		LOG.info(
				"Start date " + startdpic.getValue() + "end date " + dateFormat.format(enddpic.getValue()) + "output socket sended");

	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "enddpic", eventName = Events.ON_CHANGE)
	public void getEnddpic()
	{
		if (enddpic.getValue().before(startdpic.getValue()))
		{
			msgBox("End date must be greater than  Start date");
			return;
		}

		sendOutput("startendDates", dateFormat.format(startdpic.getValue()) + "," + dateFormat.format(enddpic.getValue()));
		LOG.info(
				"Start date " + startdpic.getValue() + "end date " + dateFormat.format(enddpic.getValue()) + "output socket sended");

	}

	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "shipmentListBox", eventName = Events.ON_SELECT)
	public void getShipmentType()
	{

		sendOutput("selectedShipmentType", shipmentListBox.getSelectedItem().getLabel());
		LOG.info("selected shipment type" + shipmentListBox.getSelectedItem().getLabel());


	}
}
