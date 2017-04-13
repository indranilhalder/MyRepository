/**
 *
 */
package com.hybris.oms.tata.renderer;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.sshiptxnresponseinfo.dto.SShipTxnResponseInfo;


/**
 * @author prabhakar
 *
 */
public class SShipTransactionInfoItemRenderer implements ListitemRenderer
{

	@Override
	public void render(final Listitem listitem, final Object value, final int index) throws Exception
	{
		// YTODO Auto-generated method stub

		final SShipTxnResponseInfo sshipInfo = (SShipTxnResponseInfo) value;
		// keeping the values in listitem
		listitem.setValue(value);
		addListcell(listitem, sshipInfo.getOrderId());
		addListcell(listitem, sshipInfo.getOrderLineId());
		addListcell(listitem, sshipInfo.getOrderLineStatus());
		addListcell(listitem, sshipInfo.getSellerId());
		addListcell(listitem, sshipInfo.getSlaveId());
		addListcell(listitem, String.valueOf(sshipInfo.getSellerPromisedTime().toString()));
	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}


}