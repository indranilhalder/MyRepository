/**
 *
 */
package com.hybris.oms.tata.pincodelogistics;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.buc.report.SellerAndLogisticsPerfRpt.dto.SellerAndLogisticsPerfRpt;


/**
 * @author Pradeep
 * 
 */
public class PincodeLogisticsListRenderer implements ListitemRenderer
{



	/**
	 * The method to implements of a renderer, will be called by listbox automatically while render items
	 */
	public void render(final Listitem listitem, final Object value, final int index)
	{
		final SellerAndLogisticsPerfRpt sellerLRpt = (SellerAndLogisticsPerfRpt) value;

		// keep value in listitem
		listitem.setValue(value);
		addListcell(listitem, sellerLRpt.getSellerId());
		addListcell(listitem, sellerLRpt.getOrderId());
		addListcell(listitem, sellerLRpt.getTransactionId());

		if (sellerLRpt.getOrderSubDateTime() != null)
		{
			addListcell(listitem, sellerLRpt.getOrderSubDateTime().toString());
		}
		else
		{
			addListcell(listitem, "");
		}

		addListcell(listitem, sellerLRpt.getCustomerId());
		addListcell(listitem, sellerLRpt.getEmail());
		addListcell(listitem, sellerLRpt.getMobileNo());
		addListcell(listitem, String.valueOf(sellerLRpt.getTatConf2Hotc()));
		addListcell(listitem, String.valueOf(sellerLRpt.getActualTatOrder2Hotc()));
		addListcell(listitem, String.valueOf(sellerLRpt.getTatConfHotc2Delvd()));
		addListcell(listitem, String.valueOf(sellerLRpt.getActualTatHotc2Delvd()));



	}

	private void addListcell(final Listitem listitem, final String value)

	{

		final Listcell listCell = new Listcell();
		if (value != null)
		{
			listCell.setLabel(value);
		}
		else
		{
			listCell.setLabel("");
		}
		listCell.setParent(listitem);
	}
}
