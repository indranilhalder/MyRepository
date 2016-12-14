/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.hybris.oms.api.orderlogistics.OrderLogisticsFacade;
import com.hybris.oms.domain.lpawb.dto.LPAWBEditAuditDto;
import com.hybris.oms.domain.lpawb.dto.LPAWBUpdateTrackingInfo;


/**
 * this class is used for lp override and awb edit report purpose
 *
 * @author prabhakar
 *
 */
public class LPOverrideAndAwbEditReportWidgetController extends DefaultWidgetController
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LPOverrideAndAwbEditReportWidgetController.class);
	private String startDate;
	private String endDate;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private Listbox listBoxData;
	@WireVariable("orderLogisticsRestClient")
	private OrderLogisticsFacade orderLogisticsFacade;
	@Wire
	private Textbox txtOrderId;
	private Textbox txtTransactionId;
	private Listbox typeListbox;
	private Listbox flowTypeListbox;
	private List<LPAWBEditAuditDto> lpawbAuditReport;
	private static final String ISFORWARD = "F";
	private static final String ISFORWARD_LABEL = "ISFORWARD";
	private static final String ISRETURN = "R";
	private static final String ISRETURN_LABEL = "ISRETURN";

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		startDate = dateFormat.format(cal.getTime());
		endDate = dateFormat.format(new Date());

		getLpOverrideAndAwbEditReport(startDate, endDate);
	}

	private void getLpOverrideAndAwbEditReport(final String fromDate, final String toDate)
	{
		final LPAWBUpdateTrackingInfo lpAndAwbUpdateInfo = orderLogisticsFacade.getLPAWBUpdateTrackingInfoBetweenTwoDates(fromDate,
				toDate);
		lpawbAuditReport = lpAndAwbUpdateInfo.getLPAWBEditAudit();
		listBoxData.setModel(new ListModelList<LPAWBEditAuditDto>(lpawbAuditReport));
	}

	/**
	 *
	 * @param startendTime
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void editListView(final String startendDates)
	{
		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		LOG.info("Start Date " + startDate + "******* End Date " + endDate);
		getLpOverrideAndAwbEditReport(startDate, endDate);
	}

	@ViewEvent(componentID = "LpAndAwbReportsearch", eventName = Events.ON_CLICK)
	public void sshipOrderSearch()
	{

		String flowType = "BOTH";
		if (flowTypeListbox.getSelectedItem().getLabel().equals(ISFORWARD_LABEL))
		{
			flowType = ISFORWARD;
		}
		if (flowTypeListbox.getSelectedItem().getLabel().equals(ISRETURN_LABEL))
		{
			flowType = ISRETURN;
		}
		final LPAWBUpdateTrackingInfo lpAndAwbUpdateInfo = orderLogisticsFacade.getLpAwbReportsData(txtOrderId.getValue(),
				txtTransactionId.getValue(), typeListbox.getSelectedItem().getLabel(), flowType);
		lpawbAuditReport = lpAndAwbUpdateInfo.getLPAWBEditAudit();
		listBoxData.setModel(new ListModelList<LPAWBEditAuditDto>(lpawbAuditReport));
	}

	/**
	 * export csv file from listview
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getCsv() throws InterruptedException
	{

		exportToCsv(listBoxData, lpawbAuditReport,
				"SnL_Perf_Report_" + startDate.replace("-", "") + "_" + endDate.replace("-", ""));

	}

	public static void exportToCsv(final Listbox listbox, final List<LPAWBEditAuditDto> lpawbAuditReport, final String fileName)
			throws InterruptedException
	{
		final String saperator = ",";
		StringBuffer stringBuff = null;
		if (lpawbAuditReport == null || listbox.getItems() == null || lpawbAuditReport.isEmpty())
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
		final Iterator<LPAWBEditAuditDto> itr = lpawbAuditReport.iterator();
		StringBuffer cellLabel = null;
		while (itr.hasNext())
		{
			cellLabel = new StringBuffer("");
			final LPAWBEditAuditDto item = itr.next();
			cellLabel.append(item.getOrderId().concat(saperator));
			cellLabel.append(item.getTransactionId().concat(saperator));
			cellLabel.append(item.getTransactionType().concat(saperator));
			if (item.getIsSuccess())
			{
				cellLabel.append("true".concat(saperator));
			}
			else
			{
				cellLabel.append("false".concat(saperator));
			}
			if (item.getIsReturn())
			{
				cellLabel.append("true".concat(saperator));
			}
			else
			{
				cellLabel.append("false".concat(saperator));
			}
			if (item.getOldLogisticsID() != null)
			{
				cellLabel.append(item.getOldLogisticsID().concat(saperator));
			}
			else
			{
				cellLabel.append("".concat(saperator));
			}
			if (item.getNewLogisticsID() != null)
			{
				cellLabel.append(item.getNewLogisticsID().concat(saperator));
			}
			else
			{
				cellLabel.append("".concat(saperator));
			}
			if (item.getOldAWBNumber() != null)
			{
				cellLabel.append(item.getOldAWBNumber().concat(saperator));
			}
			else
			{
				cellLabel.append("".concat(saperator));
			}
			if (item.getNewAWBNumber() != null)
			{
				cellLabel.append(item.getNewAWBNumber().concat(saperator));
			}
			else
			{
				cellLabel.append("".concat(saperator));
			}
			stringBuff.append(cellLabel.toString().concat("\n"));
		}
		Filedownload.save(stringBuff.toString().getBytes(), "text/plain", fileName.concat(".csv"));
	}


}
