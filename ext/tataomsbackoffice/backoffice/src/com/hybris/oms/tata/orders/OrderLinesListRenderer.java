/**
 *
 */
package com.hybris.oms.tata.orders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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
		if (orderLineBUC.getDeliveryDate() == null)
		{
			addTextListcell(listitem, "");
		}
		else
		{
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			addTextListcell(listitem, dateFormat.format(orderLineBUC.getDeliveryDate()).toString());
		}
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