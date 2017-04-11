/**
 *
 */
package com.techouts.backoffice.render;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.hybris.oms.domain.deliveredvspromised.dto.DeliveryTrackingReport;


/**
 * @author TOOW10
 *
 */
public class LogistcsDeliveryReportItemRenderer implements ListitemRenderer<DeliveryTrackingReport>
{


	@Override
	public void render(final Listitem listitem, final DeliveryTrackingReport trackReport, final int index) throws Exception
	{
		listitem.setValue(trackReport);
		addListcell(listitem, trackReport.getOrderId());
		addListcell(listitem, trackReport.getOrderLineId());
		addListcell(listitem, trackReport.getPromisedDate());
		addListcell(listitem, String.valueOf(trackReport.getDeliveryDate()));
		addListcell(listitem, String.valueOf(trackReport.getDeliveryAttempt()));
		addListcell(listitem, String.valueOf(trackReport.getOrderToShip()));
		addListcell(listitem, String.valueOf(trackReport.getShipToDelivery()));
		addListcell(listitem, trackReport.getFulfillmentType());

	}

	private void addListcell(final Listitem listitem, final String value)
	{
		final Listcell listCell = new Listcell();
		final Label listLabel = new Label(value);
		listLabel.setParent(listCell);
		listCell.setParent(listitem);
	}

}
