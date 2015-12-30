/**
 * 
 */
package com.hybris.oms.tata.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.orderbuc.dto.BUCStatusRecords;


/**
 * @author Nagarjuna
 * 
 */
public class BusStatusRecordsListRenderer implements ListitemRenderer
{


	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{

		final BUCStatusRecords bucStaterecords = (BUCStatusRecords) value;

		final Logger LOG = LoggerFactory.getLogger(BusStatusRecordsListRenderer.class);

		// keep value in listitem
		listitem.setValue(value);

		LOG.info("**********************" + bucStaterecords.getDate());

		addTextListcell(listitem, bucStaterecords.getDate());
		addTextListcell(listitem, bucStaterecords.getTime());
		addTextListcell(listitem, bucStaterecords.getLocation());
		addTextListcell(listitem, bucStaterecords.getStatusDescription());

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
