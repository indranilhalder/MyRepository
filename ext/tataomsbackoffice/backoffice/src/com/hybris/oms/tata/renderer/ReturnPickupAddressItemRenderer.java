/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;


/**
 * @author TO-OW101
 *
 */
public class ReturnPickupAddressItemRenderer implements ListitemRenderer
{


	@Override
	public void render(final Listitem listitem, final Object value, final int index) throws Exception
	{
		final MplReturnPickUpAddressInfoModel returnPickUp = (MplReturnPickUpAddressInfoModel) value;
		// keeping the values in listitem
		listitem.setValue(value);
		addListcell(listitem, returnPickUp.getOrderId());
		addListcell(listitem, returnPickUp.getTransactionId());
		addListcell(listitem, returnPickUp.getPincode());
		addListcell(listitem, returnPickUp.getCreationtime().toString());
		addListcell(listitem, returnPickUp.getStatus());
	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}


}
