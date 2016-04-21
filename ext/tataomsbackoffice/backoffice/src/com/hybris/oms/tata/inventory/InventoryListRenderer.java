/**
 *
 */
package com.hybris.oms.tata.inventory;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.inventorySearchByUssid.dto.InventorySearchByUssid;


/**
 * @author Saood
 *
 */
public class InventoryListRenderer implements ListitemRenderer
{
	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final InventorySearchByUssid inventorySearchByUssid = (InventorySearchByUssid) value;

		// keep value in listitem
		listitem.setValue(value);

		addListcell(listitem, inventorySearchByUssid.getSlaveId());
		addListcell(listitem, String.valueOf(inventorySearchByUssid.getInventoryQuantity()));
		addListcell(listitem, String.valueOf(inventorySearchByUssid.getOnHandQuantity()));
		addListcell(listitem, String.valueOf(inventorySearchByUssid.getReservedQuantity()));
		addListcell(listitem, String.valueOf(inventorySearchByUssid.getAllocatedQuantity()));
		addListcell(listitem, String.valueOf(inventorySearchByUssid.getIntransitQuantity()));


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}


}
