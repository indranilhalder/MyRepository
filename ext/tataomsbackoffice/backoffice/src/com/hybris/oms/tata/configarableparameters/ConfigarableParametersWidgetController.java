/**
 *
 */
package com.hybris.oms.tata.configarableparameters;

import de.hybris.platform.util.localization.Localization;

import java.util.Date;
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

	private Div sdEditpopup;
	@Wire
	private Timebox sdEditTimeBoxFrom;
	@Wire
	private Timebox sdEditTimeBoxTo;

	private Div edEditpopup;
	@Wire
	private Timebox edEditTimeBoxFrom;
	@Wire
	private Timebox edEditTimeBoxTo;
	private Div rdEditpopup;
	@Wire
	private Timebox rdEditTimeBoxFrom;
	@Wire
	private Timebox rdEditTimeBoxTo;

	private static final String TIME_SPLIT = ":";

	Set<MplTimeSlotsData> sdTimeSlots;
	Set<MplTimeSlotsData> edTimeSlots;
	Set<MplTimeSlotsData> rdTimeSlots;

	private MplTimeSlotsData previousSdTimeSlotsData;
	private MplTimeSlotsData previousEdTimeSlotsData;
	private MplTimeSlotsData previousRdTimeSlotsData;


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
		final double sdChargeValue = configarableParameterFacade.getScheduledCharge();
		sdCharge.setValue(sdChargeValue);

	}

	/*
	 * this method is used save mplBucConfigaration airdays,surfacedays ,edcharge ,sd charge fields
	 */
	@ViewEvent(componentID = TataomsbackofficeConstants.SAVEMPLBUCCONFIG, eventName = Events.ON_CLICK)
	public void saveMplBucConfigaration()
	{
		final Double sd = sdCharge.getValue();

		if (sd == null || sd < 0)
		{
			sdChargeMessage.setVisible(true);
		}
		else
		{
			final MplBUCConfigurationsData mplBucConfigData = new MplBUCConfigurationsData();
			mplBucConfigData.setSdCharge(sd);
			configarableParameterFacade.saveMplBUCConfigurations(mplBucConfigData);
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.sdcharge.savemessage"));
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
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.remove.confirm.msg"),
					Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData sdTimeSlotsData = sdListbox.getSelectedItem().getValue();
								sdTimeSlots.remove(sdTimeSlotsData);
								configarableParameterFacade.saveMplTimeSlots(sdTimeSlots, TataomsbackofficeConstants.SCHEDULEDDELIVERY);
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.hd.delete"));
								sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
							}
							else
							{
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.cancel.confirm.msg"),
										Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK,
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
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.remove.confirm.msg"),
					Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData edTimeSlotsData = edListbox.getSelectedItem().getValue();
								edTimeSlots.remove(edTimeSlotsData);
								configarableParameterFacade.saveMplTimeSlots(edTimeSlots, TataomsbackofficeConstants.EXPRESSDELIVERY);
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.ed.delete"));
								edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));
							}
							else
							{
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.cancel.confirm.msg"),
										Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK,
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
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.remove.confirm.msg"),
					Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final MplTimeSlotsData rdTimeSlotsData = rdListbox.getSelectedItem().getValue();
								rdTimeSlots.remove(rdTimeSlotsData);
								configarableParameterFacade.saveMplTimeSlots(rdTimeSlots, TataomsbackofficeConstants.RETURNDELIVERY);
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.rd.delete"));
								rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));
							}
							else
							{
								Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.cancel.confirm.msg"),
										Localization.getLocalizedString("configurableParamWidget.msgbox.removeheader"), Messagebox.OK,
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
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = TataomsbackofficeConstants.SCHEDULEDDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void sdTimeSlotsAdd() throws InterruptedException
	{

		if (sdTimeBoxFrom == null || sdTimeBoxTo == null)
		{
		}
		else
		{

			sdTimeSlotsAddAndEdit(sdTimeBoxFrom, sdTimeBoxTo, sdpopup, "configurableParamWidget.item.hd.save", false);
		}
	}

	/**
	 * add new TimeSlot to edlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = TataomsbackofficeConstants.EXPRESSDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void edTimeSlotsAdd() throws InterruptedException
	{
		if (edTimeBoxFrom == null || edTimeBoxTo == null)
		{
		}
		else
		{
			edTimeSlotsAddAndEdit(edTimeBoxFrom, edTimeBoxTo, edpopup, "configurableParamWidget.item.ed.save", false);
		}
	}

	/**
	 * add new TimeSlot to rdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = TataomsbackofficeConstants.RETURNDELIVERY_POPUP_ITEMADD, eventName = Events.ON_CLICK)
	public void rdTimeSlotsAdd() throws InterruptedException
	{
		if (rdTimeBoxFrom == null || rdTimeBoxTo == null)
		{
		}
		else
		{
			rdTimeSlotsAddAndEdit(rdTimeBoxFrom, rdTimeBoxTo, rdpopup, "configurableParamWidget.item.rd.save", false);
		}
	}

	private boolean validateTimeSlotsUniqueCheck(final Set<MplTimeSlotsData> setOfTimeSLots, final String fromTime,
			final String toTime)
	{

		for (final MplTimeSlotsData mplTimeSlotsData : setOfTimeSLots)
		{
			if (mplTimeSlotsData.getFromTime().equals(fromTime) && mplTimeSlotsData.getToTime().equals(toTime))
			{
				return true;
			}
		}

		return false;

	}

	/**
	 * common logic sd item and edit
	 *
	 * @throws InterruptedException
	 */
	public void sdTimeSlotsAddAndEdit(final Timebox sdTimeBoxFrom, final Timebox sdTimeBoxTo, final Div sdpopup,
			final String messageDisplay, final boolean isUpdate)
	{

		final String fromMinitues = (sdTimeBoxFrom.getValue().getMinutes() <= 9) ? "0" + sdTimeBoxFrom.getValue().getMinutes()
				: "" + sdTimeBoxFrom.getValue().getMinutes();

		final String toTimeMinitues = (sdTimeBoxTo.getValue().getMinutes() <= 9) ? "0" + sdTimeBoxTo.getValue().getMinutes()
				: "" + sdTimeBoxTo.getValue().getMinutes();
		final StringBuilder fromTime = new StringBuilder();
		fromTime.append(sdTimeBoxFrom.getValue().getHours());
		fromTime.append(":");
		fromTime.append(fromMinitues);

		final StringBuilder toTime = new StringBuilder();
		toTime.append(sdTimeBoxTo.getValue().getHours());
		toTime.append(":");
		toTime.append(toTimeMinitues);
		if (validateTimeSlotsUniqueCheck(sdTimeSlots, fromTime.toString(), toTime.toString()))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.unique.validate"));
		}
		else
		{
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.SCHEDULEDDELIVERY);
			mplTimeSlots.setFromTime(fromTime.toString());
			mplTimeSlots.setToTime(toTime.toString());
			if (isUpdate && previousSdTimeSlotsData != null && sdTimeSlots.contains(previousSdTimeSlotsData))
			{
				sdTimeSlots.remove(previousSdTimeSlotsData);
			}
			sdTimeSlots.add(mplTimeSlots);
			configarableParameterFacade.saveMplTimeSlots(sdTimeSlots, TataomsbackofficeConstants.SCHEDULEDDELIVERY);
			Messagebox.show(Localization.getLocalizedString(messageDisplay));
		}

		sdListbox.setModel(new ListModelList<MplTimeSlotsData>(sdTimeSlots));
		if (sdpopup != null)
		{
			sdpopup.setVisible(false);
		}
	}

	/**
	 * common logic to ed item add and update
	 *
	 * @param edTimeBoxFrom
	 * @param edTimeBoxTo
	 * @param edpopup
	 * @param messageDisplay
	 */
	public void edTimeSlotsAddAndEdit(final Timebox edTimeBoxFrom, final Timebox edTimeBoxTo, final Div edpopup,
			final String messageDisplay, final boolean isUpdate)
	{
		final String fromMinitues = (edTimeBoxFrom.getValue().getMinutes() <= 9) ? "0" + edTimeBoxFrom.getValue().getMinutes()
				: "" + edTimeBoxFrom.getValue().getMinutes();

		final String toTimeMinitues = (edTimeBoxTo.getValue().getMinutes() <= 9) ? "0" + edTimeBoxTo.getValue().getMinutes()
				: "" + edTimeBoxTo.getValue().getMinutes();

		final StringBuilder fromTime = new StringBuilder();
		fromTime.append(edTimeBoxFrom.getValue().getHours());
		fromTime.append(":");
		fromTime.append(fromMinitues);

		final StringBuilder toTime = new StringBuilder();
		toTime.append(edTimeBoxTo.getValue().getHours());
		toTime.append(":");
		toTime.append(toTimeMinitues);

		if (validateTimeSlotsUniqueCheck(edTimeSlots, fromTime.toString(), toTime.toString()))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.unique.validate"));
		}
		else
		{
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.EXPRESSDELIVERY);
			mplTimeSlots.setFromTime(fromTime.toString());
			mplTimeSlots.setToTime(toTime.toString());
			if (isUpdate && previousEdTimeSlotsData != null && edTimeSlots.contains(previousEdTimeSlotsData))
			{
				edTimeSlots.remove(previousEdTimeSlotsData);
			}
			edTimeSlots.add(mplTimeSlots);
			configarableParameterFacade.saveMplTimeSlots(edTimeSlots, TataomsbackofficeConstants.EXPRESSDELIVERY);
			Messagebox.show(Localization.getLocalizedString(messageDisplay));
		}
		edListbox.setModel(new ListModelList<MplTimeSlotsData>(edTimeSlots));


		if (edpopup != null)
		{
			edpopup.setVisible(false);
		}
	}

	/**
	 * common logic rd add and update
	 *
	 * @param rdTimeBoxFrom
	 * @param rdTimeBoxTo
	 * @param rdpopup
	 * @param messageDisplay
	 */
	public void rdTimeSlotsAddAndEdit(final Timebox rdTimeBoxFrom, final Timebox rdTimeBoxTo, final Div rdpopup,
			final String messageDisplay, final boolean isUpdate)
	{

		final String fromMinitues = (rdTimeBoxFrom.getValue().getMinutes() <= 9) ? "0" + rdTimeBoxFrom.getValue().getMinutes()
				: "" + rdTimeBoxFrom.getValue().getMinutes();
		final String toTimeMinitues = (rdTimeBoxTo.getValue().getMinutes() <= 9) ? "0" + rdTimeBoxTo.getValue().getMinutes()
				: "" + rdTimeBoxTo.getValue().getMinutes();
		final StringBuilder fromTime = new StringBuilder();
		fromTime.append(rdTimeBoxFrom.getValue().getHours());
		fromTime.append(":");
		fromTime.append(fromMinitues);

		final StringBuilder toTime = new StringBuilder();
		toTime.append(rdTimeBoxTo.getValue().getHours());
		toTime.append(":");
		toTime.append(toTimeMinitues);

		if (validateTimeSlotsUniqueCheck(rdTimeSlots, fromTime.toString(), toTime.toString()))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.item.unique.validate"));
		}
		else
		{
			final MplTimeSlotsData mplTimeSlots = new MplTimeSlotsData();
			mplTimeSlots.setTimeslotType(TataomsbackofficeConstants.RETURNDELIVERY);
			mplTimeSlots.setFromTime(fromTime.toString());
			mplTimeSlots.setToTime(toTime.toString());
			if (isUpdate && previousRdTimeSlotsData != null && rdTimeSlots.contains(previousRdTimeSlotsData))
			{
				rdTimeSlots.remove(previousRdTimeSlotsData);
			}
			rdTimeSlots.add(mplTimeSlots);
			configarableParameterFacade.saveMplTimeSlots(rdTimeSlots, TataomsbackofficeConstants.RETURNDELIVERY);
			Messagebox.show(Localization.getLocalizedString(messageDisplay));
		}
		rdListbox.setModel(new ListModelList<MplTimeSlotsData>(rdTimeSlots));
		if (rdpopup != null)
		{
			rdpopup.setVisible(false);
		}
	}


	/**
	 * Sd Time Slots update
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "sdItemEdit", eventName = Events.ON_CLICK)
	public void sdTimeSlotsUpdate() throws InterruptedException
	{
		if (sdListbox.getSelectedItem() == null || sdListbox.getSelectedItem().equals(""))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{
			final MplTimeSlotsData mplTimeSlotsData = sdListbox.getSelectedItem().getValue();
			previousSdTimeSlotsData = mplTimeSlotsData;
			final String fromTime = mplTimeSlotsData.getFromTime();
			final String fromTimeHoursAndMins[] = fromTime.split(TIME_SPLIT);
			final int fromTimeHours = Integer.parseInt(fromTimeHoursAndMins[0]);
			final int fromTimeMins = Integer.parseInt(fromTimeHoursAndMins[1]);
			final Date fromDate = new Date();
			fromDate.setHours(fromTimeHours);
			fromDate.setMinutes(fromTimeMins);
			sdEditTimeBoxFrom.setFormat("short");
			sdEditTimeBoxFrom.setValue(fromDate);

			final String toTime = mplTimeSlotsData.getToTime();
			final String toTimeHoursAndMins[] = toTime.split(TIME_SPLIT);
			final int toTimeHours = Integer.parseInt(toTimeHoursAndMins[0]);
			final int toTimeMins = Integer.parseInt(toTimeHoursAndMins[1]);
			final Date toTimeDate = new Date();
			toTimeDate.setHours(toTimeHours);
			toTimeDate.setMinutes(toTimeMins);
			sdEditTimeBoxTo.setFormat("short");
			sdEditTimeBoxTo.setValue(toTimeDate);
			sdEditpopup.setVisible(true);
		}
	}

	/**
	 * ed time slots update
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "edItemEdit", eventName = Events.ON_CLICK)
	public void edTimeSlotsUpdate() throws InterruptedException
	{
		if (edListbox.getSelectedItem() == null || edListbox.getSelectedItem().equals(""))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{
			final MplTimeSlotsData mplTimeSlotsData = edListbox.getSelectedItem().getValue();
			previousEdTimeSlotsData = mplTimeSlotsData;
			final String fromTime = mplTimeSlotsData.getFromTime();
			final String fromTimeHoursAndMins[] = fromTime.split(TIME_SPLIT);
			final int fromTimeHours = Integer.parseInt(fromTimeHoursAndMins[0]);
			final int fromTimeMins = Integer.parseInt(fromTimeHoursAndMins[1]);
			final Date fromDate = new Date();
			fromDate.setHours(fromTimeHours);
			fromDate.setMinutes(fromTimeMins);
			edEditTimeBoxFrom.setFormat("short");
			edEditTimeBoxFrom.setValue(fromDate);

			final String toTime = mplTimeSlotsData.getToTime();
			final String toTimeHoursAndMins[] = toTime.split(TIME_SPLIT);
			final int toTimeHours = Integer.parseInt(toTimeHoursAndMins[0]);
			final int toTimeMins = Integer.parseInt(toTimeHoursAndMins[1]);
			final Date toTimeDate = new Date();
			toTimeDate.setHours(toTimeHours);
			toTimeDate.setMinutes(toTimeMins);
			edEditTimeBoxTo.setFormat("short");
			edEditTimeBoxTo.setValue(toTimeDate);
			edEditpopup.setVisible(true);
		}
	}

	/**
	 * rd time slots update
	 *
	 * @event ON_Click
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "rdItemEdit", eventName = Events.ON_CLICK)
	public void rdTimeSlotsUpdate() throws InterruptedException
	{
		if (rdListbox.getSelectedItem() == null || rdListbox.getSelectedItem().equals(""))
		{
			Messagebox.show(Localization.getLocalizedString("configurableParamWidget.mesbox.empty.item"), "", Messagebox.OK,
					Messagebox.INFORMATION);
		}
		else
		{

			final MplTimeSlotsData mplTimeSlotsData = rdListbox.getSelectedItem().getValue();
			previousRdTimeSlotsData = mplTimeSlotsData;
			final String fromTime = mplTimeSlotsData.getFromTime();
			final String fromTimeHoursAndMins[] = fromTime.split(TIME_SPLIT);
			final int fromTimeHours = Integer.parseInt(fromTimeHoursAndMins[0]);
			final int fromTimeMins = Integer.parseInt(fromTimeHoursAndMins[1]);
			final Date fromDate = new Date();
			fromDate.setHours(fromTimeHours);
			fromDate.setMinutes(fromTimeMins);
			rdEditTimeBoxFrom.setFormat("short");
			rdEditTimeBoxFrom.setValue(fromDate);

			final String toTime = mplTimeSlotsData.getToTime();
			final String toTimeHoursAndMins[] = toTime.split(TIME_SPLIT);
			final int toTimeHours = Integer.parseInt(toTimeHoursAndMins[0]);
			final int toTimeMins = Integer.parseInt(toTimeHoursAndMins[1]);
			final Date toTimeDate = new Date();
			toTimeDate.setHours(toTimeHours);
			toTimeDate.setMinutes(toTimeMins);
			rdEditTimeBoxTo.setFormat("short");
			rdEditTimeBoxTo.setValue(toTimeDate);
			rdEditpopup.setVisible(true);
		}
	}

	/**
	 * update TimeSlot to sdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = "sdPopupEditSave", eventName = Events.ON_CLICK)
	public void sdTimeSlotsEditAndSave() throws InterruptedException
	{

		if (sdEditTimeBoxFrom == null || sdEditTimeBoxTo == null)
		{
		}
		else
		{

			sdTimeSlotsAddAndEdit(sdEditTimeBoxFrom, sdEditTimeBoxTo, sdEditpopup, "configurableParamWidget.item.hd.update", true);
		}
	}

	/**
	 * update new TimeSlot to edlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = "edPopupEditSave", eventName = Events.ON_CLICK)
	public void edTimeSlotsEditAndSave() throws InterruptedException
	{
		if (edEditTimeBoxFrom == null || edEditTimeBoxTo == null)
		{
		}
		else
		{
			edTimeSlotsAddAndEdit(edEditTimeBoxFrom, edEditTimeBoxTo, edEditpopup, "configurableParamWidget.item.ed.update", true);
		}
	}

	/**
	 * update new TimeSlot to rdlistbox when popup add button clicked
	 *
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	@ViewEvent(componentID = "rdPopupEditSave", eventName = Events.ON_CLICK)
	public void rdTimeSlotsEditAndSave() throws InterruptedException
	{
		if (rdEditTimeBoxFrom == null || rdEditTimeBoxTo == null)
		{
		}
		else
		{
			rdTimeSlotsAddAndEdit(rdEditTimeBoxFrom, rdEditTimeBoxTo, rdEditpopup, "configurableParamWidget.item.rd.update", true);
		}
	}

}
