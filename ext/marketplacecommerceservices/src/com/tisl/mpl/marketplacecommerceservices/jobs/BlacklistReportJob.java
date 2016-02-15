/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BlacklistModel;
import com.tisl.mpl.data.BlacklistReportData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.model.MplBlackListingJobModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class BlacklistReportJob extends AbstractJobPerformable<MplBlackListingJobModel>
{
	@Resource
	private BlacklistService blacklistService;
	@Autowired
	private ConfigurationService configurationService;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final String FILE_HEADER = MarketplacecommerceservicesConstants.CSV_FILE_HEADER;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BlacklistReportJob.class.getName());

	/**
	 * @Descriptiion: CronJob to generate COD Blacklist Report
	 * @param: arg0
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final MplBlackListingJobModel cronModel)
	{
		try
		{
			if (null != cronModel.getStartDate() && null != cronModel.getEndDate())
			{
				fetchSpecificDetails(cronModel.getStartDate(), cronModel.getEndDate());
			}
			else
			{
				fetchAllDetails();
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
	private void fetchSpecificDetails(final Date startDate, final Date endDate)
	{
		final List<BlacklistModel> customers = getBlacklistService().getSpecificBlacklistedCustomers(startDate, endDate);

		if (null != customers)
		{
			//put customer data in the POJO class
			putDataForBlacklist(customers);
		}
	}

	/**
	 * This Method fetches all details in Blacklist Model
	 *
	 */
	private void fetchAllDetails()
	{
		final List<BlacklistModel> customers = getBlacklistService().getAllBlacklistedCustomers();

		if (null != customers)
		{
			//put customer data in the POJO class
			putDataForBlacklist(customers);
		}
	}

	/**
	 * This method takes the list of blacklisted customers and sets in the POJO class which is in turn set in the CSV
	 * file to be generated in a specified location
	 *
	 * @param customers
	 */
	void putDataForBlacklist(final List<BlacklistModel> customers)
	{
		final List<BlacklistReportData> dataList = new ArrayList<BlacklistReportData>();

		//iterating through the list and adding the rows in the list of the pojo class
		for (final BlacklistModel b : customers)
		{
			final BlacklistReportData data = new BlacklistReportData();

			if (null != b.getCreationtime())
			{
				data.setDate(b.getCreationtime().toString());
			}
			else
			{
				data.setDate(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			if (null != b.getCustomerId() && null != b.getCustomerId().getUid())
			{
				data.setCustomerId(b.getCustomerId().getUid());
			}
			else
			{
				data.setCustomerId(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			if (null != b.getEmailID())
			{
				data.setEmail(b.getEmailID());
			}
			else
			{
				data.setEmail(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			if (null != b.getIpAddress())
			{
				data.setIpaddress(b.getIpAddress());
			}
			else
			{
				data.setIpaddress(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			if (null != b.getCustomerName())
			{
				data.setName(b.getCustomerName());
			}
			else
			{
				data.setName(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			if (null != b.getPhoneNumber())
			{
				data.setPhoneNo(b.getPhoneNumber());
			}
			else
			{
				data.setPhoneNo(MarketplacecommerceservicesConstants.EMPTYSTRING);
			}

			dataList.add(data);
		}

		FileWriter fileWriter = null;
		final File rootFolder1 = new File(
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.FILE_LOCATION),
				MarketplacecommerceservicesConstants.REPORT);
		try
		{
			fileWriter = new FileWriter(rootFolder1);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER);

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (final BlacklistReportData report : dataList)
			{
				fileWriter.append(report.getDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getCustomerId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getEmail());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getIpaddress());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getPhoneNo());
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
				LOG.error("Error while flushing/closing fileWriter !!!");
			}
		}

	}

	/**
	 * @return the blacklistService
	 */
	public BlacklistService getBlacklistService()
	{
		return blacklistService;
	}

	/**
	 * @param blacklistService
	 *           the blacklistService to set
	 */
	public void setBlacklistService(final BlacklistService blacklistService)
	{
		this.blacklistService = blacklistService;
	}




}
