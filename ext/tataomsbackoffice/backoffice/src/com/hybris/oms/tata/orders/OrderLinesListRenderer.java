/**
 * 
 */
package com.hybris.oms.tata.orders;

import java.util.Date;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.orderbuc.dto.OrderLineBUC;


/**
 * @author Saood
 * 
 */
public class OrderLinesListRenderer implements ListitemRenderer
{
	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{

		final OrderLineBUC orderLineBUC = (OrderLineBUC) value;

		// keep value in listitem
		listitem.setValue(value);

		addTextListcell(listitem, orderLineBUC.getUssid());
		addTextListcell(listitem, orderLineBUC.getTransactionLineStatus());
		addTextListcell(listitem, Double.toString(orderLineBUC.getUnitpricevalue()));
		addTextListcell(listitem, Double.toString(orderLineBUC.getApportionedPrice()));
		addTextListcell(listitem, Double.toString(orderLineBUC.getApportionedCODPrice()));
		addTextListcell(listitem, orderLineBUC.getIsallocated());
		//addTextListcell(listitem, orderLineBUC.getRemarks());
		addTextListcell(listitem, orderLineBUC.getRejectionRequestID());
		addTextListcell(listitem, orderLineBUC.getRejectionReasonCode());
		addTextListcell(listitem, orderLineBUC.getIsaFreebie());
		addTextListcell(listitem, String.valueOf(orderLineBUC.getIsCOD()));
		addTextListcell(listitem, orderLineBUC.getDeliveryType());
		addTextListcell(listitem, orderLineBUC.getTransportMode());
		addTextListcell(listitem, orderLineBUC.getFulfillmentType());
		addTextListcell(listitem, orderLineBUC.getLogisticProviderName());
		addTextListcell(listitem, dateToString(orderLineBUC.getDeliveryDate()));
		addTextListcell(listitem, dateToString(orderLineBUC.getOrderStatusTimeStamp()));
		addTextListcell(listitem, orderLineBUC.getReceivedBy());

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

	private String dateToString(final Date date)
	{
		if (date == null)
		{
			return "";
		}
		return String.valueOf(date);

	}


}