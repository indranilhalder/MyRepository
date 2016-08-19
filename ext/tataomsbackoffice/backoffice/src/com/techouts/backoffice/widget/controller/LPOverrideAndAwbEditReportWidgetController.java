/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hybris.cockpitng.annotations.SocketEvent;
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
		final List<LPAWBEditAuditDto> lpawbauditdto = lpAndAwbUpdateInfo.getLPAWBEditAudit();
		LOG.info("lp audit" + lpawbauditdto.toString());

		listBoxData.setModel(new ListModelList<LPAWBEditAuditDto>(lpawbauditdto));
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

}
