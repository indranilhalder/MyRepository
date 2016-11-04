/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.reports.ReportsGenerateFacade;
import com.hybris.oms.domain.buc.reports.dto.ReportRequest;
import com.hybris.oms.domain.buc.reports.dto.TSHIPvsSSHIPReport;


/**
 * this class is used for to Generate TSHIP vs SSHip Assignment Report
 *
 * @author prabhakar
 */
public class TshipVsShipAssignmentReportWidgetController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(TshipVsShipAssignmentReportWidgetController.class);

	@Wire
	private Label lblTshipRequests;
	@Wire
	private Label lblSshipRequests;
	@Wire
	private Label lblTshipAssignments;
	@Wire
	private Label lblSshipAssignments;
	@Wire
	private Label lblTshipAssignmentPercent;
	@Wire
	private Label lblSshipAssignmentPercent;
	@Wire
	private Label lblTshipFailure;
	@Wire
	private Label lblSshipFailure;
	@Wire
	private Label lblTshipFailurePercent;
	@Wire
	private Label lblSshipFailurePercent;

	@WireVariable("reportRequestRestClient")
	private ReportsGenerateFacade reportsGenarateFacade;

	private String startDate;
	private String endDate;

	TSHIPvsSSHIPReport report;

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		final ReportRequest reportRequest = new ReportRequest();
		reportRequest.setFromDate(cal.getTime());
		reportRequest.setToDate(new Date());
		getTshipVsSshipReport(reportRequest);
	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SuppressWarnings("deprecation")
	@SocketEvent(socketId = "startendDates")
	public void getTshipVSshipReportBySocketEvent(final String startendDates)
	{
		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


		LOG.info(" inside sockent Start Date " + startDate + "******* End Date " + endDate);
		final ReportRequest reportRequest = new ReportRequest();
		try
		{

			reportRequest.setFromDate(dateFormat.parse(startDate));
			reportRequest.setToDate(dateFormat.parse(endDate));
		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}
		getTshipVsSshipReport(reportRequest);

	}

	private void getTshipVsSshipReport(final ReportRequest request)
	{
		LOG.info(" inside getTship vs Sship report");
		if (reportsGenarateFacade != null)
		{
			report = reportsGenarateFacade.getTSHIPvsSSHIP(request);
		}
		else
		{
			LOG.info("rest Client not available some internal problem");
		}
		if (report != null)
		{

			lblTshipRequests.setValue(report.getRequestsForTSHIP());
			lblSshipRequests.setValue(report.getRequestsForSSHIP());
			lblTshipAssignments.setValue(report.getAssignmentsForTSHIP());
			lblSshipAssignments.setValue(report.getAssignmentsForSSHIP());

			final int tshiRequestvalue = Integer.parseInt(report.getRequestsForTSHIP());
			final int sshipRequestvalue = Integer.parseInt(report.getRequestsForSSHIP());
			final int sshipAssignment = Integer.parseInt(report.getAssignmentsForSSHIP());

			final int tshipAssignment = Integer.parseInt(report.getAssignmentsForTSHIP());

			final int tshipFailure = Integer.parseInt(report.getRequestsForTSHIP().trim())
					- Integer.parseInt(report.getAssignmentsForTSHIP().trim());

			final int sshipFailure = Integer.parseInt(report.getRequestsForSSHIP().trim())
					- Integer.parseInt(report.getAssignmentsForSSHIP().trim());
			lblTshipFailure.setValue("" + tshipFailure);
			lblSshipFailure.setValue("" + sshipFailure);




			try
			{
				final double tshipAssignmentPercent = (Integer.parseInt(report.getAssignmentsForTSHIP())
						/ Integer.parseInt(report.getRequestsForTSHIP())) * 100;
				lblTshipAssignmentPercent.setValue("" + tshipAssignmentPercent + "%");
			}
			catch (final ArithmeticException exception)
			{
				lblTshipAssignmentPercent.setValue(report.getAssignmentsForTSHIP() + "%");
				LOG.info("tship  Assignments  zero ");
			}
			try
			{
				final double sshipAssignmentPercent = (Integer.parseInt(report.getAssignmentsForSSHIP().trim())
						/ Integer.parseInt(report.getRequestsForSSHIP().trim())) * 100;
				lblSshipAssignmentPercent.setValue("" + sshipAssignmentPercent + "%");
			}
			catch (final ArithmeticException exception)
			{
				lblSshipAssignmentPercent.setValue(report.getAssignmentsForSSHIP() + "%");
				LOG.info("sship Assignments % zero ");
			}
			try
			{
				final double tshipFailurePercent = (tshipFailure / Integer.parseInt(report.getRequestsForTSHIP().trim())) * 100;

				LOG.info("tship Failure percentage" + tshipFailurePercent);
				lblTshipFailurePercent.setValue("" + tshipFailurePercent + "%");
			}
			catch (final ArithmeticException exception)
			{

				lblTshipFailurePercent.setValue(report.getRequestsForTSHIP() + "%");
				LOG.info("tship Requests  zero so there is no Failure ");
			}

			try
			{
				final double sshipFailurePercent = (sshipFailure / Integer.parseInt(report.getRequestsForSSHIP().trim())) * 100;
				LOG.info("sship Failure percentage" + sshipFailurePercent);
				lblSshipFailurePercent.setValue("" + sshipFailurePercent + "%");

			}
			catch (final ArithmeticException exception)
			{

				lblSshipFailurePercent.setValue(report.getRequestsForSSHIP() + "%");
				LOG.info("sship Assingments zero ");
			}

		}
	}

}