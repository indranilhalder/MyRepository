/**
 * 
 */
package com.hybris.oms.tata.orders;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.orderbuc.dto.OrderBUC;




/**
 * @author Saood
 * 
 */
public class OrderListRenderer implements ListitemRenderer
{

	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final OrderBUC orderBUC = (OrderBUC) value;

		// keep value in listitem
		listitem.setValue(value);

		addListcell(listitem, orderBUC.getOrderId());
		addListcell(listitem, orderBUC.getOriginalOrderRefNo());
		addListcell(listitem, String.valueOf(orderBUC.getSubmissionDateTime()));
		addListcell(listitem, String.valueOf(orderBUC.getIssueDate()));
		addListcell(listitem, orderBUC.getOrderType());
		addListcell(listitem, orderBUC.getChannel());


	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setStyle("text-align:center");
		listCell.setStyle("text-align:center");
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

}
