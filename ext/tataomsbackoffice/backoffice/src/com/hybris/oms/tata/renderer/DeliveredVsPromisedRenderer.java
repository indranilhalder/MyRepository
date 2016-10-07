/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.deliveredvspromised.dto.DeliveredVsPromised;


/**
 * @author techouts
 *
 */
public class DeliveredVsPromisedRenderer implements ListitemRenderer
{
	@Override
	public void render(final Listitem listitem, final Object value, final int index) throws Exception
	{
		// YTODO Auto-generated method stub

		final DeliveredVsPromised deliveredVsPromised = (DeliveredVsPromised) value;
		// keeping the values in listitem
		listitem.setValue(value);
		addListcell(listitem, deliveredVsPromised.getOrderId());
		addListcell(listitem, deliveredVsPromised.getOrderLineId());
		addListcell(listitem, String.valueOf(deliveredVsPromised.getPromisedDate()));
		addListcell(listitem, String.valueOf(deliveredVsPromised.getDeliveredDate()));
		addListcell(listitem, deliveredVsPromised.getSlaveId());
		addListcell(listitem, deliveredVsPromised.getSellerId());
	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

}
