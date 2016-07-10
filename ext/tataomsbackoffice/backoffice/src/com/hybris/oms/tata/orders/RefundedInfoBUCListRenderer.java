/**
 * 
 */
package com.hybris.oms.tata.orders;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.orderbuc.dto.RefundedInfoBUC;


/**
 * @author Saood
 * 
 */
public class RefundedInfoBUCListRenderer implements ListitemRenderer
{
	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{

		final RefundedInfoBUC refundedInfoBUC = (RefundedInfoBUC) value;
		listitem.setValue(value);

		addTextListcell(listitem, refundedInfoBUC.getRefundedAmt());
		addTextListcell(listitem, refundedInfoBUC.getRefundedBankTrxID());
		addTextListcell(listitem, refundedInfoBUC.getRefundedBankTrxStatus());
		addTextListcell(listitem, refundedInfoBUC.getRefundedBy());
		addTextListcell(listitem, refundedInfoBUC.getRefundTriggeredDate());
		addTextListcell(listitem, refundedInfoBUC.getRefundType());



	}

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
