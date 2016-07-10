/**
 *
 */
package com.hybris.oms.tata.logisticscontacts;

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
import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.tata.renderer.LogisticsContactsListItemRenderer;


/**
 * Controller Class to do CRUD operation to the LogisticsController
 * 
 * @author Saood
 * 
 */
public class LogisticsContactsWidgetController extends DefaultWidgetController
{
	@Autowired
	private LogisticsFacade logisticsRestClient;

	@WireVariable("logisticsRestClient")
	private LogisticsFacade logisticsFacade;

	// listbox to capture one row
	private Listbox listview;



	@Override
	public void initialize(final Component comp)
	{

		super.initialize(comp);
		final List<Logistics> list = (List<Logistics>) logisticsFacade.getAll();
		listview.setModel(new ListModelList(list));
		listview.setItemRenderer(new LogisticsContactsListItemRenderer());
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
		sendOutput("createlogisticscontact", Boolean.TRUE);

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
			final Object logistics = listview.getSelectedItem().getValue();
			sendOutput("editlogisticscontact", logistics);
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
	public void deleteLogistics() throws InterruptedException
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
								final Logistics logistics = (Logistics) listview.getSelectedItem().getValue();
								logisticsRestClient.deleteLogistics(logistics.getLogisticsid());
								final List<Logistics> logisticsContacts = (List<Logistics>) logisticsRestClient.getAll();

								//setting all the logistics contact value in a ListModelList to display it in zul
								listview.setModel(new ListModelList(logisticsContacts));
								listview.setItemRenderer(new LogisticsContactsListItemRenderer());
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
	 * @param logistics
	 * @throws InterruptedException
	 */
	@SocketEvent(socketId = "listitemlogisticscontact")
	public void addListView(final Boolean logistics) throws InterruptedException
	{

		if (logistics.booleanValue())
		{
			//getting the logistics Contacts
			final List<Logistics> logisticsContacts = (List<Logistics>) logisticsRestClient.getAll();

			listview.setModel(new ListModelList(logisticsContacts));
			listview.setItemRenderer(new LogisticsContactsListItemRenderer());
		}
		else
		{
			Messagebox.show("Failed to add new Logistics");
		}

	}



	@SocketEvent(socketId = "listitemlogisticscontact_edit")
	public void editListView(final Boolean logistics_edit) throws InterruptedException
	{

		if (logistics_edit.booleanValue())
		{
			//getting the logistics Contacts
			final List<Logistics> logistics = (List<Logistics>) logisticsRestClient.getAll();

			listview.setModel(new ListModelList(logistics));
			listview.setItemRenderer(new LogisticsContactsListItemRenderer());
		}
		else
		{
			Messagebox.show("Failed to edit selected Logistic Contact");
		}

	}







}
