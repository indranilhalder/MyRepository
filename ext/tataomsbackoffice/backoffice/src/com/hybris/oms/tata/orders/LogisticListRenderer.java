/**
 *
 */
package com.hybris.oms.tata.orders;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.logistics.dto.Logistics;


/**
 * @author TOOW10
 *
 */
public class LogisticListRenderer implements ListitemRenderer
{


	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final Logistics logistic = (Logistics) value;
		// keep value in listitem
		listitem.setValue(value);
		addTextListcell(listitem, logistic.getLogisticsid());
	}

	/**
	 *
	 * @param listitem
	 *           list item of listbox
	 * @param value
	 *           is the cell label value
	 */
	private void addTextListcell(final Listitem listitem, String value)
	{
		final Listcell listCell = new Listcell();
		if (value == null)
		{
			value = "";
		}
		final Label listLabel = new Label(value);
		listLabel.setStyle("text-align:center");
		listCell.setStyle("text-align:center");
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}
}
