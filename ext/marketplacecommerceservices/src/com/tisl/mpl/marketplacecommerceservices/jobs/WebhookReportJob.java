/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.data.MplWebhookReportData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebhookReportService;
import com.tisl.mpl.model.MplWebhookReportJobModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class WebhookReportJob extends AbstractJobPerformable<MplWebhookReportJobModel>
{

	@Autowired
	private MplWebhookReportService mplWebhookReportService;
	@Autowired
	private ConfigurationService configurationService;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final String FILE_HEADER = MarketplacecommerceservicesConstants.WEBHOOKREPORT_CSV_FILE_HEADER;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(WebhookReportJob.class.getName());

	/**
	 * @Descriptiion: CronJob to generate Report to track Refund Process
	 * @param: mplWebhookReportJobModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final MplWebhookReportJobModel mplWebhookReportJobModel)
	{
		LOG.debug("Inside Webhook Report Job:::::::::::::::::::::::::::");
		try
		{
			if (null != mplWebhookReportJobModel.getStartDate() && null != mplWebhookReportJobModel.getEndDate())
			{
				fetchSpecificDetails(mplWebhookReportJobModel.getStartDate(), mplWebhookReportJobModel.getEndDate());
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	/**
	 * This Method fetches all details in Blacklist Model within the prescribed date Range
	 *
	 * @param startDate
	 * @param endDate
	 */
	private void fetchSpecificDetails(final Date startDate, final Date endDate) throws EtailNonBusinessExceptions
	{
		final List<MplPaymentAuditEntryModel> auditEntries = getMplWebhookReportService().getSpecificAuditEntries(startDate,
				endDate);
		if (null != auditEntries)
		{
			//put customer data in the POJO class
			putDataForRefundAuditReport(auditEntries);
		}
	}

	/**
	 * This method takes the list of blacklisted customers and sets in the POJO class which is in turn set in the CSV
	 * file to be generated in a specified location
	 *
	 * @param auditEntries
	 */
	void putDataForRefundAuditReport(final List<MplPaymentAuditEntryModel> auditEntries)
	{
		final List<MplWebhookReportData> dataList = new ArrayList<MplWebhookReportData>();

		//iterating through the list and adding the rows in the list of the pojo class
		for (final MplPaymentAuditEntryModel auditEntry : auditEntries)
		{
			RefundTransactionMappingModel rtModel = modelService.create(RefundTransactionMappingModel.class);
			String orderId = MarketplacecommerceservicesConstants.EMPTYSTRING;
			String refAmount = MarketplacecommerceservicesConstants.EMPTYSTRING;
			try
			{
				if (StringUtils.isNotEmpty(auditEntry.getRefundReqId()))
				{
					final String juspayRefId = auditEntry.getRefundReqId();
					rtModel = getMplWebhookReportService().fetchRefundType(juspayRefId);

					if (null != rtModel.getRefundedOrderEntry())
					{
						orderId = (null != rtModel.getRefundedOrderEntry().getOrder()) ? rtModel.getRefundedOrderEntry().getOrder()
								.getCode() : MarketplacecommerceservicesConstants.EMPTYSTRING;
						refAmount = (null != rtModel.getRefundedOrderEntry().getTotalPrice()) ? rtModel.getRefundedOrderEntry()
								.getTotalPrice().toString() : MarketplacecommerceservicesConstants.EMPTYSTRING;
					}
				}
			}
			catch (final ModelNotFoundException e)
			{
				LOG.error("Error while fetching Refund Data", e);
			}

			final MplWebhookReportData data = new MplWebhookReportData();
			data.setOrderId(orderId);
			data.setAmount(refAmount);
			if (null != auditEntry.getAuditId())
			{
				data.setAuditId(auditEntry.getAuditId());
			}
			else
			{
				data.setAuditId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}
			if (null != auditEntry.getStatus())
			{
				data.setStatus(auditEntry.getStatus().toString());
			}
			else
			{
				data.setStatus(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}
			if (null != auditEntry.getRefundReqId())
			{
				data.setRefundReqId(auditEntry.getRefundReqId());
			}
			else
			{
				data.setRefundReqId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}
			//			if (null != auditEntry.getResponseDate())
			//			{
			//				data.setResponseDate(auditEntry.getResponseDate());
			//			}
			//			else
			//			{
			//				data.setResponseDate(new Date());
			//			}
			//			if (null != rtModel.getRefundType())
			//			{
			//				data.setRefundType(rtModel.getRefundType().toString());
			//			}
			//			else
			//			{
			//				data.setRefundType(MarketplacecommerceservicesConstants.EMPTYSTRING);
			//			}

			dataList.add(data);
		}

		FileWriter fileWriter = null;
		final File rootFolder1 = new File(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.WEBHOOKREPORTPATH), MarketplacecommerceservicesConstants.WEBHOOKREPORT);
		try
		{
			fileWriter = new FileWriter(rootFolder1);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER);

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (final MplWebhookReportData report : dataList)
			{
				fileWriter.append(report.getOrderId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getAuditId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getAmount());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getStatus());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getRefundReqId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getResponseDate().toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getRefundType());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in CsvFileWriter !!!", e);
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException e)
			{
				LOG.error("Error while flushing/closing fileWriter !!!", e);
			}
		}

	}

	/**
	 * @return the mplWebhookReportService
	 */
	public MplWebhookReportService getMplWebhookReportService()
	{
		return mplWebhookReportService;
	}


	/**
	 * @param mplWebhookReportService
	 *           the mplWebhookReportService to set
	 */
	public void setMplWebhookReportService(final MplWebhookReportService mplWebhookReportService)
	{
		this.mplWebhookReportService = mplWebhookReportService;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
