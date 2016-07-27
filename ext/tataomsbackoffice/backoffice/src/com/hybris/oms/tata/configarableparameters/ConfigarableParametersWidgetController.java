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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timebox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.facade.ConfigarableParameterFacade;
import com.hybris.oms.tata.jalo.MplTimeSlots;


/**
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
	private Intbox airDeleveryDays;
	@Wire
	private Intbox surDeleveryDays;
	@Wire
	private Doublebox sdCharge;
	@Wire
	private Doublebox edCharge;
	@Wire
	private Label airDaysError;
	@Wire
	private Label surfaceDaysError;
	@Wire
	private Label sdChargeError;
	@Wire
	private Label edChargeError;
	@Wire
	private Label successLabel;
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
			if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("SD"))
			{
				sdTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("ED"))
			{
				edTimeSlots.add(mplTimeSlots);
			}
			else if (mplTimeSlots.getTimeslotType().equalsIgnoreCase("RD"))
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

	@ViewEvent(componentID = "saveMplBucConfigaration", eventName = Events.ON_CLICK)
	public void saveMplBucConfigaration()
	{
		final Integer airDays = airDeleveryDays.getValue();
		final Integer surDays = surDeleveryDays.getValue();
		final Double sd = sdCharge.getValue();
		final Double ed = edCharge.getValue();
		if (airDays == null || airDays <= 0)
		{
			airDaysError.setVisible(true);
		}
		else if (surDays == null || surDays <= 0)
		{
			surfaceDaysError.setVisible(true);

		}
		else if (sd == null || sd <= 0)
		{
			sdChargeError.setVisible(true);
		}
		else if (ed == null || ed <= 0)
		{
			edChargeError.setVisible(true);
		}
		else
		{
			final MplBUCConfigurationsData mplBucConfigData = new MplBUCConfigurationsData();
			mplBucConfigData.setAirDeliveryBuffer(airDays);
			mplBucConfigData.setSurDeliveryBuffer(surDays);
			mplBucConfigData.setSdCharge(sd);
			mplBucConfigData.setEdCharge(ed);
			configarableParameterFacade.saveMplBUCConfigurations(mplBucConfigData);
			successLabel.setVisible(true);

			surDeleveryDays.setValue(0);
			airDeleveryDays.setValue(0);
			sdCharge.setValue(0.0);
			edCharge.setValue(0.0);

		}

	}

	/**
	 * This is a method to capture the event which is generated at the time of clicking of the Delete symbol.
	 *
	 * @event ON_Click
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "sdItemDelete", eventName = Events.ON_CLICK)
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
	@ViewEvent(componentID = "edItemDelete", eventName = Events.ON_CLICK)
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
								edListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
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
	@ViewEvent(componentID = "rdItemDelete", eventName = Events.ON_CLICK)
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
								rdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
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
	@ViewEvent(componentID = "sdPopupSave", eventName = Events.ON_CLICK)
	public void sdTimeSlotsAdd() throws InterruptedException
	{
		if (sdTimeBoxFrom == null || sdTimeBoxTo == null)
		{
		}
		else
		{
			final String fromTime = sdTimeBoxFrom.getValue().getHours() + ":" + sdTimeBoxFrom.getValue().getMinutes();
			final String toTime = sdTimeBoxTo.getValue().getHours() + ":" + sdTimeBoxTo.getValue().getMinutes();
			if (sdpopup != null)
			{
				sdpopup.setVisible(false);
			}

		}

	}

	private void addTimeSlots(final String type, final String fromTime, final String toTime)
	{
		final MplTimeSlots mplTimeSlots = new MplTimeSlots();
		mplTimeSlots.setTimeslotType(type);
		//mplTimeSlots.setFromTime(fromTime);

		if (type.equalsIgnoreCase("SD"))
		{

		}

	}

	/**
	 * add new TimeSlot to edlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "", eventName = Events.ON_CLICK)
	public void edTimeSlotsAdd() throws InterruptedException
	{

	}

	/**
	 * add new TimeSlot to rdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "", eventName = Events.ON_CLICK)
	public void rdTimeSlotsAdd() throws InterruptedException
	{

	}

	/**
	 * persist the sdTimeSlots when save button Clicked
	 */
	@ViewEvent(componentID = "", eventName = Events.ON_CLICK)
	public void sdTimeSlotsSave()
	{

		//it gos db
	}

	/**
	 * persist the edTimeSlots when save button is Clicked
	 */
	@ViewEvent(componentID = "", eventName = Events.ON_CLICK)
	public void edTimeSlotsSave()
	{
		// it gos db
	}

	/**
	 * persist the rdTimeSlots when save button is Clicked
	 */
	@ViewEvent(componentID = "", eventName = Events.ON_CLICK)
	public void rdTimeSlotsSave()
	{
		// it gos db
	}
}
