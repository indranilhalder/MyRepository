/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.deliveryreports.ScheduledDeliveryReportFacade;
import com.hybris.oms.domain.deliveredvspromised.dto.DeliveryReport;
import com.hybris.oms.domain.deliveredvspromised.dto.DeliveryTrackingReport;
import com.hybris.oms.domain.deliveredvspromised.dto.ScheduledDeliveryTrackingReport;
import com.techouts.backoffice.render.LogistcsDeliveryReportItemRenderer;


/**
 * @author TOOW10
 *
 */
public class LogisticsDeliveryReportWidgetController extends DefaultWidgetController
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LogisticsDeliveryReportWidgetController.class);
	@Wire
	private Textbox txtOrderId;
	@Wire
	private Datebox selectedDateValue;
	private List<DeliveryTrackingReport> listOfTrackingReport;

	private Listbox listBoxData;
	@WireVariable("scheduledDeliveryReport")
	private ScheduledDeliveryReportFacade scheduledDeliveryReportFacade;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		selectedDateValue.setValue(new Date());
	}

	private void getLogisticsDeliveryReportData(final DeliveryReport deliveryReport)
	{
		final ScheduledDeliveryTrackingReport scheduledDeliveryTrackingReport = scheduledDeliveryReportFacade
				.getScheduledDeliveryReport(deliveryReport);
		listOfTrackingReport = scheduledDeliveryTrackingReport.getDeliverytrackingReport();
		listBoxData.setModel(new ListModelList<DeliveryTrackingReport>(listOfTrackingReport));
		listBoxData.setItemRenderer(new LogistcsDeliveryReportItemRenderer());
	}

	@ViewEvent(componentID = "dateAndTypeSearchButton", eventName = Events.ON_CLICK)
	public void searchByDateAndType()
	{
		final Date dateParam = selectedDateValue.getValue();
		final DeliveryReport deliveryReport = new DeliveryReport();
		deliveryReport.setDate(dateParam);
		getLogisticsDeliveryReportData(deliveryReport);
	}

	@ViewEvent(componentID = "orderIdSearchButton", eventName = Events.ON_CLICK)
	public void searchByOrderId()
	{
		final DeliveryReport deliveryReport = new DeliveryReport();
		if (txtOrderId != null)
		{
			deliveryReport.setOrderId(txtOrderId.getValue());
			getLogisticsDeliveryReportData(deliveryReport);
		}
	}

	/**
	 * export csv file from listview
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getCsv() throws InterruptedException
	{

		exportToCsv(listBoxData, listOfTrackingReport, "LodisticsDeliveryData_".concat(selectedDateValue.getValue().toString()));

	}

	public static void exportToCsv(final Listbox listbox, final List<DeliveryTrackingReport> listOfTrackingReport,
			final String fileName) throws InterruptedException
	{
		final String saperator = ",";
		StringBuffer stringBuff = null;
		if (listOfTrackingReport == null || listbox.getItems() == null || listOfTrackingReport.isEmpty())
		{
			LOG.info("**************************** Lp awb tracking List Is Empty***************************");
			Messagebox.show("List is empty", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		stringBuff = new StringBuffer("");
		for (final Object head : listbox.getHeads())
		{
			final StringBuffer headLabel = new StringBuffer();
			for (final Object header : ((Listhead) head).getChildren())
			{
				headLabel.append(((Listheader) header).getLabel().concat(saperator));
			}
			stringBuff.append(headLabel.append("\n").toString());
		}
		final Iterator<DeliveryTrackingReport> deliveryTrackItemIterator = listOfTrackingReport.iterator();
		StringBuffer cellLabel = null;
		while (deliveryTrackItemIterator.hasNext())
		{
			cellLabel = new StringBuffer("");
			final DeliveryTrackingReport item = deliveryTrackItemIterator.next();
			cellLabel.append(item.getOrderId().concat(saperator));
			cellLabel.append(item.getOrderLineId().concat(saperator));
			cellLabel.append(item.getPromisedDate().concat(saperator));
			cellLabel.append(String.valueOf(item.getDeliveryDate()).concat(saperator));
			cellLabel.append(String.valueOf(item.getDeliveryAttempt()).concat(saperator));
			cellLabel.append(String.valueOf(item.getOrderToShip()).concat(saperator));
			cellLabel.append(String.valueOf(item.getShipToDelivery()).concat(saperator));
			cellLabel.append(item.getFulfillmentType().concat(saperator));
			stringBuff.append(cellLabel.toString().concat("\n"));
		}
		Filedownload.save(stringBuff.toString().getBytes(), "text/plain", fileName.concat(".csv"));
	}

}
