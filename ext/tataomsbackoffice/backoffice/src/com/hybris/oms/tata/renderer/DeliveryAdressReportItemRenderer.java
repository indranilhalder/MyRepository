/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;


/**
 * @author prabhakar
 *
 */
public class DeliveryAdressReportItemRenderer implements ListitemRenderer
{

	@Override
	public void render(final Listitem listitem, final Object value, final int index) throws Exception
	{
		// YTODO Auto-generated method stub

		final MplDeliveryAddressReportData deliveryAddressData = (MplDeliveryAddressReportData) value;
		// keeping the values in listitem
		listitem.setValue(value);
		addListcell(listitem, deliveryAddressData.getOrderId());
		addListcell(listitem, String.valueOf(deliveryAddressData.getTotalRequestCount()));
		addListcell(listitem, String.valueOf(deliveryAddressData.getFailureRequsetCount()));


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}


}
