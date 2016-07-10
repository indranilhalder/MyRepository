package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.logisticscalendar.dto.LogisticsCalendar;







public class LogisticscalendarListItemRenderer implements ListitemRenderer
{
	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final LogisticsCalendar logisticsCalendar = (LogisticsCalendar) value;

		// keep value in listitem
		listitem.setValue(value);

		addListcell(listitem, logisticsCalendar.getLogisticsid());
		addListcell(listitem, logisticsCalendar.getLogisticsstarttime());
		addListcell(listitem, logisticsCalendar.getLogisticsendtime());

		addListCellWorkingDays(listitem, logisticsCalendar.getLogisticsworkingdays());


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

	private void addListCellWorkingDays(final Listitem listitem, final String value)
	{

		final Listcell listCell = new Listcell();
		final Label listLabel;
		if (value == null || value.equals(""))
		{
			listLabel = new Label("");
		}
		else
		{
			listLabel = new Label(value.replace("0", "Sunday").replace("1", "Monday").replace("2", "Tuesday")
					.replace("3", "Wednesday").replace("4", "Thursday").replace("5", "Friday").replace("6", "Saturday"));
		}
		listLabel.setParent(listCell);
		listCell.setParent(listitem);

	}





}