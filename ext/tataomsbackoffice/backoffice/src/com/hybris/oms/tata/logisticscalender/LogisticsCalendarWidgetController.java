package com.hybris.oms.tata.logisticscalender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.logisticscalendar.LogisticsCalendarFacade;
import com.hybris.oms.domain.logisticscalendar.dto.LogisticsCalendar;
import com.hybris.oms.tata.renderer.LogisticscalendarListItemRenderer;


/**
 * Controller Class to do CRUD operation to the LogisticsController
 * 
 * @author Saood
 * 
 */
public class LogisticsCalendarWidgetController extends DefaultWidgetController
{
	@Autowired
	LogisticsCalendarFacade logisticsCalendarRestClient;

	@WireVariable("logisticsCalendarRestClient")
	private LogisticsCalendarFacade logisticsCalendarFacade;

	// listbox to capture one row
	private Listbox listview;



	@Override
	public void initialize(final Component comp)
	{

		super.initialize(comp);
		final List<LogisticsCalendar> list = (List<LogisticsCalendar>) logisticsCalendarFacade.getAll();
		listview.setModel(new ListModelList(list));
		listview.setItemRenderer(new LogisticscalendarListItemRenderer());
	}


	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Add symbol.
	 * 
	 * @event ON_Click
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "createItem", eventName = Events.ON_CLICK)
	public void createItem() throws InterruptedException
	{
		// TOS should be checked before accepting order
		sendOutput("createlogisticscalendar", Boolean.TRUE);

	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Edit symbol.
	 * 
	 * @event ON_Click
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "editItem", eventName = Events.ON_CLICK)
	public void editItem() throws InterruptedException
	{

		if (listview.getSelectedItem() == null || listview.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One Logistics Row", "Alert", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			final Object logisticsCalendar = listview.getSelectedItem().getValue();
			sendOutput("editlogisticscalendar", logisticsCalendar);
		}
	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Delete symbol.
	 * 
	 * @event ON_Click
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "deleteItem", eventName = Events.ON_CLICK)
	public void deleteLogisticsCalendar() throws InterruptedException
	{
		if (listview.getSelectedItem() == null || listview.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One List Item", "Logistics Item Delete Dialog", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show("Are you sure to remove?", "Logistics Item Remove Dialog", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final LogisticsCalendar logisticsCalendar = (LogisticsCalendar) listview.getSelectedItem().getValue();
								logisticsCalendarRestClient.deleteLogisticsCalendar(logisticsCalendar.getLogisticsid());
								final List<LogisticsCalendar> logisticsCalendars = (List<LogisticsCalendar>) logisticsCalendarRestClient
										.getAll();

								//set logistics calender List to zul listView to display it
								listview.setModel(new ListModelList(logisticsCalendars));
								listview.setItemRenderer(new LogisticscalendarListItemRenderer());
								sendOutput("deletelogisticscalendar", Boolean.FALSE);
								Messagebox.show("Selected Item Removed", "Logistics Item Remove Dialog", Messagebox.OK,
										Messagebox.INFORMATION);

							}
							else
							{
								Messagebox.show("Removing Canceled", "Logistics Item Remove Dialog", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});

		}

	}


	/**
	 * getting the list items while adding
	 * 
	 * @param logisticsCalendar
	 * @throws InterruptedException
	 */
	@SocketEvent(socketId = "listitemlogisticscalendar")
	public void addListView(final Boolean logisticsCalendar) throws InterruptedException
	{

		if (logisticsCalendar.booleanValue())
		{
			//getting the logistics Ca
			final List<LogisticsCalendar> logisticsCalendars = (List<LogisticsCalendar>) logisticsCalendarRestClient.getAll();

			listview.setModel(new ListModelList(logisticsCalendars));
			listview.setItemRenderer(new LogisticscalendarListItemRenderer());
		}
		else
		{
			Messagebox.show("Failed to add new LogisticCalendar");
		}

	}



	@SocketEvent(socketId = "listitemlogisticscalendar_edit")
	public void editListView(final Boolean logisticsCalendar_edit) throws InterruptedException
	{

		if (logisticsCalendar_edit.booleanValue())
		{
			//getting the logistics Calender through webservices
			final List<LogisticsCalendar> logisticsCalendars = (List<LogisticsCalendar>) logisticsCalendarRestClient.getAll();

			listview.setModel(new ListModelList(logisticsCalendars));
			listview.setItemRenderer(new LogisticscalendarListItemRenderer());
		}
		else
		{
			Messagebox.show("Failed to edit selected LogisticCalendar");
		}

	}


}
