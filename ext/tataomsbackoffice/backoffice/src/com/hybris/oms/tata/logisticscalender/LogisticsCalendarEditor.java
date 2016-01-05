package com.hybris.oms.tata.logisticscalender;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.logisticscalendar.LogisticsCalendarFacade;
import com.hybris.oms.domain.logisticscalendar.dto.LogisticsCalendar;


/**
 * This class is to show the editorial area in logistic widget
 * 
 */
public class LogisticsCalendarEditor extends DefaultWidgetController
{
	private static final Logger LOG = LoggerFactory.getLogger(LogisticsCalendarEditor.class);

	@Autowired
	private LogisticsCalendarFacade logisticsCalendarRestClient;
	Grid createGrid;
	Grid editorGrid;

	Textbox logisticsId;
	Timebox startTime;
	Timebox endTime;
	Listbox workingDays;


	Textbox logisticsIdEdit;
	Timebox startTimeEdit;
	Timebox endTimeEdit;
	Listbox workingDaysEdit;




	@Override
	public void initialize(final Component comp)
	{

		super.initialize(comp);
		doFormEmpty(logisticsId, startTime, endTime, workingDays);


	}


	/**
	 * method to activate create and deactivate the update in editor area while selecting add symbol
	 * 
	 * @param status
	 * @throws InterruptedException
	 */
	@SocketEvent(socketId = "isactivecreate")
	public void windowActiveCreate(final Boolean status) throws InterruptedException
	{
		createGrid.setVisible(status.booleanValue());
		editorGrid.setVisible(false);

	}

	/**
	 * method to activate edit and deactivate the create in editor area while selecting edit symbol
	 * 
	 * @param status
	 * @throws InterruptedException
	 * @throws ParseException
	 * @throws WrongValueException
	 */
	@SocketEvent(socketId = "isactiveedit")
	public void windowActiveEdit(final Object selectedItem) throws InterruptedException, WrongValueException, ParseException
	{
		final LogisticsCalendar logisticsCalendar = (LogisticsCalendar) selectedItem;
		LOG.info("***********************" + logisticsCalendar.getLogisticsid());
		createGrid.setVisible(false);
		editorGrid.setVisible(true);
		doFormEmpty(logisticsIdEdit, startTimeEdit, endTimeEdit, workingDaysEdit);
		logisticsIdEdit.setValue(logisticsCalendar.getLogisticsid());
		startTimeEdit.setValue(changeToDate(logisticsCalendar.getLogisticsstarttime()));
		endTimeEdit.setValue(changeToDate(logisticsCalendar.getLogisticsendtime()));
		if (logisticsCalendar.getLogisticsworkingdays() == null || logisticsCalendar.getLogisticsworkingdays().equals(""))
		{
			return;
		}
		final List<Listitem> workingDaysList = workingDaysEdit.getItems();
		@SuppressWarnings("rawtypes")
		final Iterator<Listitem> iterator = workingDaysList.iterator();

		while (iterator.hasNext())
		{

			final Listitem temp = iterator.next();
			final Checkbox workingDaysCheckbox = (Checkbox) temp.getFirstChild().getFirstChild();

			final String[] split_workingdays = logisticsCalendar.getLogisticsworkingdays().split(",");

			for (int i = 0; i < split_workingdays.length; i++)
			{

				if (split_workingdays[i] != null && workingDaysCheckbox.getValue().equals(split_workingdays[i]))
				{
					workingDaysCheckbox.setChecked(true);
				}

			}

		}

	}



	/**
	 * method to save the newly create Logistics calendar in database
	 * 
	 * and generate one socket event for updating the list view
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "addSave", eventName = Events.ON_CLICK)
	public void addSave() throws InterruptedException
	{
		//calling webservices to create a new Logistics Calendar



		if (startEndTimeComparator(startTime.getValue(), endTime.getValue()))
		{
			try
			{
				logisticsCalendarRestClient.createLogisticsCalendar(getLogisticsCalFormData(logisticsId, startTime, endTime,
						workingDays));
				Messagebox.show("Logistics Calendar created Successfully");
				sendOutput("refreshlogisticslist", Boolean.TRUE);
				initialize(createGrid);

			}
			catch (final Exception e)
			{
				sendOutput("refreshlogisticslist", Boolean.FALSE);
				LOG.error("Unable to create New LogisticsCalendar  Error : " + e.getMessage());
			}
		}



	}


	/**
	 * method to update Logistics calendar in database and generate one socket event for updating the list view
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "updateSave", eventName = Events.ON_CLICK)
	public void updateSave() throws InterruptedException
	{
		if (startEndTimeComparator(startTimeEdit.getValue(), endTimeEdit.getValue()))
		{
			try
			{
				logisticsCalendarRestClient.updateLogisticsCalendar(getLogisticsCalFormData(logisticsIdEdit, startTimeEdit,
						endTimeEdit, workingDaysEdit));
				Messagebox.show("Logistics Calendar updated Successfully");
				//write a method to empty the fields
			}
			catch (final Exception e)
			{
				sendOutput("refreshlogisticslist_edit", Boolean.FALSE);
				LOG.error("Unable to update the  LogisticsCalendar, Error : " + e.getMessage());

			}

			sendOutput("refreshlogisticslist_edit", Boolean.TRUE);
		}

	}

	/**
	 * This socket event after delete the list item in listview disable the editor and create grids.
	 * 
	 * 
	 * */

	@SocketEvent(socketId = "calendar_disble_contact_editor")
	public void disableAll(final Boolean selectedItem)
	{

		createGrid.setVisible(false);
		editorGrid.setVisible(false);
	}

	public String changeDateFormatToStringFormat(final Date date)
	{
		final DateFormat formatter = new SimpleDateFormat("hh:mm a");

		return formatter.format(date);
	}

	public LogisticsCalendar getLogisticsCalFormData(final Textbox logisticsCalId, final Timebox logisticsCalStartTime,
			final Timebox logisticsCalEndTime, final Listbox logisticsWorkdays) throws InterruptedException
	{
		StringBuffer builder;
		final LogisticsCalendar logisticsCalendar = new LogisticsCalendar();
		logisticsCalendar.setLogisticsid(logisticsCalId.getValue());
		//System.out.println("**************&&&&&&&&&logisticsCalId.getValue()&&&&&&&&&&***************" + logisticsCalId.getValue());
		logisticsCalendar.setLogisticsstarttime(changeDateFormatToStringFormat(logisticsCalStartTime.getValue()));
		logisticsCalendar.setLogisticsendtime(changeDateFormatToStringFormat(logisticsCalEndTime.getValue()));
		final List<Listitem> workingDaysList = logisticsWorkdays.getItems();
		@SuppressWarnings("rawtypes")
		final Iterator<Listitem> iterator = workingDaysList.iterator();
		builder = new StringBuffer("");
		while (iterator.hasNext())
		{
			final Listitem temp = iterator.next();
			final Checkbox workingDaysCheckbox = (Checkbox) temp.getFirstChild().getFirstChild();
			if (workingDaysCheckbox.isChecked())
			{


				builder.append(workingDaysCheckbox.getValue().toString().concat(","));

			}
		}

		if (builder.length() > 1)
		{
			logisticsCalendar.setLogisticsworkingdays(builder.toString().substring(0, builder.length() - 1).trim());
		}


		return logisticsCalendar;
	}

	public void doFormEmpty(final Textbox logisticsCalId, final Timebox logisticsCalStartTime, final Timebox logisticsCalEndTime,
			final Listbox logisticsWorkdays)

	{

		LOG.info("***************************************initialized method");
		logisticsCalId.setValue("");
		logisticsCalStartTime.setValue(null);
		logisticsCalEndTime.setValue(null);
		final List<Listitem> workingDaysList = logisticsWorkdays.getItems();
		@SuppressWarnings("rawtypes")
		final Iterator<Listitem> iterator = workingDaysList.iterator();
		while (iterator.hasNext())
		{
			final Listitem temp = iterator.next();
			final Checkbox workingDaysCheckbox = (Checkbox) temp.getFirstChild().getFirstChild();
			workingDaysCheckbox.setChecked(false);



		}

	}


	public Date changeToDate(final String date) throws ParseException
	{
		final DateFormat formatter = new SimpleDateFormat("hh:mm a");
		final Date date1 = formatter.parse(date);
		LOG.info("date1" + date1);
		final String formatedDate = formatter.format(date1);
		LOG.info("formatedDate" + formatedDate);
		final Date date2 = formatter.parse(formatedDate);
		LOG.info("date2" + date2);
		return date2;
	}


	/**
	 * 
	 * @param startTm
	 * @param endTm
	 * @return if start date greater than end date return true,otherwise return false
	 * @throws InterruptedException
	 */
	public boolean startEndTimeComparator(final Date startTm, final Date endTm) throws InterruptedException
	{

		boolean status = true;
		if (startTm == null || endTm == null)
		{
			status = false;
			Messagebox.show("Please fill all the fields", "Error", Messagebox.OK, Messagebox.ERROR);


		}

		else if (endTm.getTime() <= startTm.getTime())
		{
			status = false;
			Messagebox.show("Invalid end time. End Time must be greater than start time.", "Error", Messagebox.OK, Messagebox.ERROR);

		}


		return status;
	}

}
