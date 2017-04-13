/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.deliverdvspromised.report.DeliveredVsPromisedRestClient;
import com.hybris.oms.domain.deliveredvspromised.dto.DeliveredVsPromised;
import com.hybris.oms.domain.deliveredvspromised.dto.DeliveredVsPromisedReport;
import com.hybris.oms.domain.sshiptxninfo.dto.SShipTxnInfo;
import com.hybris.oms.tata.renderer.DeliveredVsPromisedRenderer;


/**
 * @author prabhakar
 *
 */
public class DeliveryDateAgainstPromisedDateController extends DefaultWidgetController
{
	/**
	 * @author techouts
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryDateAgainstPromisedDateController.class);
	private String startDate;
	private String endDate;
	@WireVariable("deliveredVsPromisedReport")
	private DeliveredVsPromisedRestClient reportsGenarateFacade;
	DeliveredVsPromisedReport report;
	@Wire
	private Textbox txtSellerId;
	@Wire
	private Textbox txtSlaveId;
	@Wire
	private Textbox txtOrderId;
	@Wire
	private Textbox txtTransactionId;
	private Listbox listBoxData;
	private Date fromDate;
	private Date toDate;
	private List<DeliveredVsPromised> listOfSshipResponse;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		final SShipTxnInfo requestDto = new SShipTxnInfo();
		requestDto.setFromDate(cal.getTime());
		requestDto.setToDate(new Date());
		startDate = dateFormat.format(cal.getTime());
		endDate = dateFormat.format(new Date());
		displayDeliveredVsPromisedData(requestDto);
	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void getDeliveredVsPromisedReport(final String startendDates)
	{


		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		LOG.info(" inside sockent Start Date " + startDate + "******* End Date " + endDate);
		final SShipTxnInfo reportRequest = new SShipTxnInfo();
		try
		{

			fromDate = dateFormat.parse(startDate);
			toDate = dateFormat.parse(endDate);
			reportRequest.setFromDate(fromDate);
			reportRequest.setToDate(toDate);
		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}
		displayDeliveredVsPromisedData(reportRequest);
	}

	@ViewEvent(componentID = "orderSearch", eventName = Events.ON_CLICK)
	public void deliveredVsPromisedOrderSearch()
	{
		LOG.info("inside deliveredVsPromisedOrderSearch");
		final SShipTxnInfo shipTxnInfo = new SShipTxnInfo();
		int count = 0;
		if (txtSellerId != null && StringUtils.isNotEmpty(txtSellerId.getValue()))
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("txt SellerId {}", txtSellerId.getValue());
			}
			++count;
			shipTxnInfo.setSellerId(txtSellerId.getValue().trim());
			shipTxnInfo.setFromDate(fromDate);
			shipTxnInfo.setToDate(toDate);
		}
		if (txtSlaveId != null && StringUtils.isNotEmpty(txtSlaveId.getValue()))
		{
			LOG.info("txt slave id" + txtSlaveId.getValue());
			++count;
			shipTxnInfo.setSlaveId(txtSlaveId.getValue().trim());
			shipTxnInfo.setFromDate(fromDate);
			shipTxnInfo.setToDate(toDate);
		}
		if (txtTransactionId != null && StringUtils.isNotEmpty(txtTransactionId.getValue()))
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("txtOrderLine Id is {}", txtTransactionId.getValue());
			}
			++count;
			shipTxnInfo.setOrderLineId(txtTransactionId.getValue().trim());
		}


		if (txtOrderId != null && StringUtils.isNotEmpty(txtOrderId.getValue()))
		{
			LOG.info("txt order id {}", txtOrderId.getValue());
			++count;
			shipTxnInfo.setOrderId(txtOrderId.getValue().trim());
		}
		if (count > 0)
		{
			listBoxData.setMultiple(Boolean.TRUE);
			displayDeliveredVsPromisedData(shipTxnInfo);
		}
		else
		{
			Messagebox.show("Atleast one field is mandatory");
		}
	}

	private void displayDeliveredVsPromisedData(final SShipTxnInfo shipTxnInfo)
	{
		final DeliveredVsPromisedReport deliveredVsPromisedReport = reportsGenarateFacade.getDeliveredVsPromisedTxns(shipTxnInfo);
		listOfSshipResponse = deliveredVsPromisedReport.getDeliveredVsPromised();
		listBoxData.setModel(new ListModelList<DeliveredVsPromised>(listOfSshipResponse));
		listBoxData.setItemRenderer(new DeliveredVsPromisedRenderer());
	}

	/**
	 * export csv file from listview
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getCsv() throws InterruptedException
	{

		exportToCsv(listBoxData, listOfSshipResponse,
				"DeliveryDateAgainstPromisedReport_" + startDate.replace("-", "") + "_" + endDate.replace("-", ""));

	}

	public static void exportToCsv(final Listbox listbox, final List<DeliveredVsPromised> listOfTrackingReport,
			final String fileName) throws InterruptedException
	{
		final String saperator = ",";
		final StringBuffer stringBuff = new StringBuffer("");

		if (listOfTrackingReport == null || listbox.getItems() == null || listOfTrackingReport.isEmpty())
		{
			LOG.info(
					"****************************Delivery Date against promised date Report List Is Empty***************************");
			Messagebox.show("List is empty", "Empty Data", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		for (final Object head : listbox.getHeads())
		{
			for (final Object header : ((Listhead) head).getChildren())
			{
				stringBuff.append(((Listheader) header).getLabel().concat(saperator));
			}
			stringBuff.append("\n");
		}
		listOfTrackingReport.forEach(new Consumer<DeliveredVsPromised>()
		{
			@Override
			public void accept(final DeliveredVsPromised deliveredVsPromised)
			{

				stringBuff.append(deliveredVsPromised.getOrderId().concat(saperator));
				stringBuff.append(deliveredVsPromised.getOrderLineId().concat(saperator));
				stringBuff.append((deliveredVsPromised.getPromisedDate() == null) ? "".concat(saperator)
						: deliveredVsPromised.getPromisedDate().toString().concat(saperator));
				stringBuff.append((deliveredVsPromised.getDeliveryAttempt() == null) ? "".concat(saperator)
						: deliveredVsPromised.getDeliveryAttempt().toString().concat(saperator));
				stringBuff.append((deliveredVsPromised.getDeliveredDate() == null) ? "".concat(saperator)
						: deliveredVsPromised.getDeliveredDate().toString().concat(saperator));
				stringBuff.append(deliveredVsPromised.getSlaveId().concat(saperator));
				stringBuff.append(deliveredVsPromised.getSellerId().concat(saperator));
				stringBuff.append("\n");
			}
		});
		Filedownload.save(stringBuff.toString().getBytes(), "text/plain", fileName.concat(".csv"));
	}
}

