/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.logistics.dto.Logistics;


/**
 * @author Saood
 * 
 */
public class LogisticsContactsListItemRenderer implements ListitemRenderer
{
	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final Logistics logistics = (Logistics) value;

		// keeping the values in listitem
		listitem.setValue(value);

		addListcell(listitem, logistics.getLogisticsid());
		addListcell(listitem, logistics.getLogisticname());
		addListcell(listitem, String.valueOf(logistics.getActive()));
		addListcell(listitem, logistics.getAddress());
		addListcell(listitem, logistics.getCName());
		addListcell(listitem, logistics.getCPhone());
		addListcell(listitem, logistics.getCEmail());
		addListcell(listitem, logistics.getCFax());


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}






}