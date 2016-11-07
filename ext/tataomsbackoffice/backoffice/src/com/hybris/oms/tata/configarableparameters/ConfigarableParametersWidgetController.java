/**
 *
 */
package com.hybris.oms.tata.configarableparameters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timebox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.facade.ConfigarableParameterFacade;


/**
 * Configarable Serach parameters Controller class
 *
 * @author prabhakar
 *
 */
public class ConfigarableParametersWidgetController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(ConfigarableParametersWidgetController.class);

	@Wire
	private Listbox sdListbox;
	@Wire
	private Listbox edListbox;
	@Wire
	private Listbox rdListbox;
	@Wire
	private Doublebox sdCharge;
	@Wire
	private Label sdChargeMessage;
	@Wire
	private Timebox sdTimeBoxFrom;
	@Wire
	private Timebox edTimeBoxFrom;
	@Wire
	private Timebox rdTimeBoxFrom;
	@Wire
	private Timebox sdTimeBoxTo;
	@Wire
	private Timebox edTimeBoxTo;
	@Wire
	private Timebox rdTimeBoxTo;
	@Wire
	private Button saveMplBucConfigaration;
	@Wire
	private Div sdpopup;
	@Wire
	private Div edpopup;
	@Wire
	private Div rdpopup;

	Set<MplTimeSlotsData> sdTimeSlots;
	Set<MplTimeSlotsData> edTimeSlots;
	Set<MplTimeSlotsData> rdTimeSlots;


	@Resource(name = "configarableParameterFacade")
	private ConfigarableParameterFacade configarableParameterFacade;


	/**
	 * @param configarableParameterFacade
	 *           the configarableParameterFacade to set
	 */
	public void setConfigarableParameterFacade(final ConfigarableParameterFacade configarableParameterFacade)
	{
		this.configarableParameterFacade = configarableParameterFacade;
	}

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		LOG.info("configarable facade" + configarableParameterFacade);
		final List<MplTimeSlotsData> mplTimeSlotsList = configarableParameterFacade.onLoadMplTimeSlots();
		if (sdTimeSlots == null)
		{
			sdTimeSlots = new HashSet<MplTimeSlotsData>();
		}
		if (edTimeSlots == null)
		{
			edTimeSlots = new HashSet<MplTimeSlotsData>();
		}
		if (rdTimeSlots == null)
		{
			rdTimeSlots = new HashSet<MplTimeSlotsData>();
		}
		for (final MplTimeSlotsData mplTimeSlots : mplTimeSlotsList)
		{
			if (mplTimeSlots.getTimeslotType().equalsIgnoreCase(TataomsbackofficeConstants.SCHEDULEDDELIVERY))
			{
				sdTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase(TataomsbackofficeConstants.EXPRESSDELIVERY))
			{
				edTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase(TataomsbackofficeConstants.RETURNDELIVERY))
			{
				rdTimeSlots.add(mplTimeSlots);
			}
		}
		sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
		edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));
		rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));
	}

	/*
	 * this method is used save mplBucConfigaration airdays,surfacedays ,edcharge ,sd charge fields
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.SAVEMPLBUCCONFIG, eventName = Events.ON_CLICK)
	public void saveMplBucConfigaration()
	{
		final Double sd = sdCharge.getValue();

		if (sd == null || sd <= 0)
		{
			sdChargeMessage.setVisible(true);
		}
		else
		{
			final MplBUCConfigurationsData mplBucConfigData = new MplBUCConfigurationsData();
			mplBucConfigData.setSdCharge(sd);
			configarableParameterFacade.saveMplBUCConfigurations(mplBucConfigData);
			Messagebox.show("Sd Charge Saved SucessFully..");
		}
	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Delete symbol.
	 *
	 * @event ON_Click
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.SCHEDULEDDELIVERY_ITEMDELETE, eventName = Events.ON_CLICK)
	public void sdTimeSlotsDelete() throws InterruptedException
	{
		if (sdListbox.getSelectedItem() == null || sdListbox.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One List Item", "Sd TimeSlots Delete Dialog", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show("Are you sure to remove?", "Sd TimeSlots Delete Dialog", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData sdTimeSlotsData = sdListbox.getSelectedItem().getValue();
								sdTimeSlots.remove(sdTimeSlotsData);
								sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
								Messagebox.show(" Item tempararly deleted  Click save button To Persist",
										"Sd TimeSlots Item Remove Dialog", Messagebox.OK, Messagebox.INFORMATION);
							}
							else
							{
								Messagebox.show("Removing Canceled", "Sd TimeSlots Item Remove Dialog", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});
		}
	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Delete symbol.
	 *
	 * @event ON_Click
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.EXPRESSDELIVERY_ITEMDELETE, eventName = Events.ON_CLICK)
	public void edTimeSlotsDelete() throws InterruptedException
	{
		if (edListbox.getSelectedItem() == null || edListbox.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One List Item", "Sd TimeSlots Delete Dialog", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show("Are you sure to remove?", "Ed TimeSlots Delete Dialog", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData edTimeSlotsData = edListbox.getSelectedItem().getValue();
								edTimeSlots.remove(edTimeSlotsData);
								edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));
								Messagebox.show(" Item tempararly deleted  Click save button To Persist",
										"Ed TimeSlots Item Remove Dialog", Messagebox.OK, Messagebox.INFORMATION);
							}
							else
							{
								Messagebox.show("Removing Canceled", "Sd TimeSlots Item Remove Dialog", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});

		}
	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Delete symbol.
	 *
	 * @event ON_Click
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.RETURNDELIVERY_ITEMDELETE, eventName = Events.ON_CLICK)
	public void rdTimeSlotsDelete() throws InterruptedException
	{
		if (rdListbox.getSelectedItem() == null || rdListbox.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One List Item", "Sd TimeSlots Delete Dialog", Messagebox.OK, Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show("Are you sure to remove?", "Rd TimeSlots Delete Dialog", Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData rdTimeSlotsData = rdListbox.getSelectedItem().getValue();
								rdTimeSlots.remove(rdTimeSlotsData);
								rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));
								Messagebox.show(" Item tempararly deleted  Click save button To Persist",
										"Rd TimeSlots Item Remove Dialog", Messagebox.OK, Messagebox.INFORMATION);
							}
							else
							{
								Messagebox.show("Removing Canceled", "Sd TimeSlots Item Remove Dialog", Messagebox.OK,
										Messagebox.INFORMATION);
							}
						}
					});
		}
	}

	/**
	 * add new TimeSlot to sdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.SCHEDULEDDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void sdTimeSlotsAdd() throws InterruptedException
	{
		if (sdTimeBoxFrom == null || sdTimeBoxTo == null)
		{
		}
		else
		{
			final String fromTime = sdTimeBoxFrom.getValue().getHours() + ":" + sdTimeBoxFrom.getValue().getMinutes();
			final String toTime = sdTimeBoxTo.getValue().getHours() + ":" + sdTimeBoxTo.getValue().getMinutes();
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.SCHEDULEDDELIVERY);
			mplTimeSlots.setFromTime(fromTime);
			mplTimeSlots.setToTime(toTime);
			sdTimeSlots.add(mplTimeSlots);
			sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
			if (sdpopup != null)
			{
				sdpopup.setVisible(false);
			}
		}
	}

	/**
	 * add new TimeSlot to edlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.EXPRESSDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void edTimeSlotsAdd() throws InterruptedException
	{
		if (edTimeBoxFrom == null || edTimeBoxTo == null)
		{
		}
		else
		{
			final String fromTime = edTimeBoxFrom.getValue().getHours() + ":" + edTimeBoxFrom.getValue().getMinutes();
			final String toTime = edTimeBoxTo.getValue().getHours() + ":" + edTimeBoxTo.getValue().getMinutes();
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.EXPRESSDELIVERY);
			mplTimeSlots.setFromTime(fromTime);
			mplTimeSlots.setToTime(toTime);
			edTimeSlots.add(mplTimeSlots);
			edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));

			if (edpopup != null)
			{
				edpopup.setVisible(false);
			}
		}
	}

	/**
	 * add new TimeSlot to rdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.RETURNDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void rdTimeSlotsAdd() throws InterruptedException
	{
		if (rdTimeBoxFrom == null || rdTimeBoxTo == null)
		{
		}
		else
		{
			final String fromTime = rdTimeBoxFrom.getValue().getHours() + ":" + rdTimeBoxFrom.getValue().getMinutes();
			final String toTime = rdTimeBoxTo.getValue().getHours() + ":" + rdTimeBoxTo.getValue().getMinutes();
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.RETURNDELIVERY);
			mplTimeSlots.setFromTime(fromTime);
			mplTimeSlots.setToTime(toTime);
			rdTimeSlots.add(mplTimeSlots);
			rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));
			if (rdpopup != null)
			{
				rdpopup.setVisible(false);
			}
		}
	}

	/**
	 * persist the sdTimeSlots when save button Clicked
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.SCHEDULEDDELIVERY_ITEMSAVE, eventName = Events.ON_CLICK)
	public void sdTimeSlotsSave()
	{
		LOG.info("sd time slots save " + sdTimeSlots.toString());
		configarableParameterFacade.saveMplTimeSlots(sdTimeSlots, TataomsbackofficeConstants.SCHEDULEDDELIVERY);
		Messagebox.show("Scheduled Delivery Time Slots Saved Success");
	}

	/**
	 * persist the edTimeSlots when save button is Clicked
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.EXPRESSDELIVERY_ITEMSAVE, eventName = Events.ON_CLICK)
	public void edTimeSlotsSave()
	{
		LOG.info("ed timeslots save" + edTimeSlots.toString());
		configarableParameterFacade.saveMplTimeSlots(edTimeSlots, TataomsbackofficeConstants.EXPRESSDELIVERY);
		Messagebox.show("Express Delivery Time Slots Saved Success");
	}

	/**
	 * persist the rdTimeSlots when save button is Clicked
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.RETURNDELIVERY_ITEMSAVE, eventName = Events.ON_CLICK)
	public void rdTimeSlotsSave()
	{
		LOG.info("rd timeslots save" + rdTimeSlots.toString());
		configarableParameterFacade.saveMplTimeSlots(rdTimeSlots, TataomsbackofficeConstants.RETURNDELIVERY);
		Messagebox.show("Return Delivery Time Slots Saved Success");
	}
}
