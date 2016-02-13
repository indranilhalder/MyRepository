/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.PromotionalReportData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionalReportCreationService;
import com.tisl.mpl.model.PromotionalReportCreationJobModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class PromotionalReportJob extends AbstractJobPerformable<PromotionalReportCreationJobModel>
{
	@Resource
	private PromotionalReportCreationService promotionalReportCreationService;
	@Autowired
	private ConfigurationService configurationService;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String TILDE_DELIMITER = "~";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header
	private static final String FILE_HEADER = MarketplacecommerceservicesConstants.PROMOTIONS_CSV_FILE_HEADER;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionalReportJob.class.getName());

	/**
	 * @return the promotionalReportCreationService
	 */
	public PromotionalReportCreationService getPromotionalReportCreationService()
	{
		return promotionalReportCreationService;
	}

	/**
	 * @param promotionalReportCreationService
	 *           the promotionalReportCreationService to set
	 */
	public void setPromotionalReportCreationService(final PromotionalReportCreationService promotionalReportCreationService)
	{
		this.promotionalReportCreationService = promotionalReportCreationService;
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

	/**
	 * @Descriptiion: CronJob to generate Promotional Report
	 * @param: arg0
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final PromotionalReportCreationJobModel arg0)
	{
		try
		{
			final Date StartDate = arg0.getStartDate();
			final Date endDate = arg0.getEndDate();

			//getting all the blacklisted customer details
			final Set<Map<AbstractPromotionModel, SavedValuesModel>> promotionWithCreatedBymap = promotionalReportCreationService
					.getAllPromotions(StartDate, endDate);

			if (null != promotionWithCreatedBymap)
			{
				//put customer data in the POJO class
				writeDataIntoCsv(promotionWithCreatedBymap);
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
	 * @Desctiption This method takes the list of blacklisted customers and sets in the POJO class which is in turn set
	 *              in the CSV file to be generated in a specified location
	 *
	 * @param promotions
	 *           list
	 */
	void writeDataIntoCsv(final Set<Map<AbstractPromotionModel, SavedValuesModel>> promotionWithCreatedByMapSet)
	{
		final List<PromotionalReportData> dataList = new ArrayList<PromotionalReportData>();

		for (final Map<AbstractPromotionModel, SavedValuesModel> entry : promotionWithCreatedByMapSet)
		{
			for (final Map.Entry<AbstractPromotionModel, SavedValuesModel> mapEntry : entry.entrySet())
			{
				final AbstractPromotionModel promotion = mapEntry.getKey();
				final SavedValuesModel savedValues = mapEntry.getValue();

				final PromotionalReportData data = new PromotionalReportData();

				if (null != promotion)
				{
					data.setUser(savedValues.getUser().getName());
					data.setPromoCode(promotion.getCode());
					//					data.setCreationTime(promotion.getCreationtime());
					//					data.setModifiedTime(promotion.getModifiedtime());
					//////////////////////////////////////////////////////////////////
					data.setTimestamp(savedValues.getTimestamp());
					data.setModificationType(savedValues.getModificationType().getCode());
					data.setStartDate(promotion.getStartDate());
					data.setEndDate(promotion.getEndDate());
					final List<String> modifiedAttributesList = new ArrayList<String>();
					final Set<SavedValueEntryModel> savedValueEntries = savedValues.getSavedValuesEntries();
					for (final SavedValueEntryModel savedValueEntry : savedValueEntries)
					{
						final String modifiedAttribute = savedValueEntry.getModifiedAttribute();
						modifiedAttributesList.add(modifiedAttribute);

					}
					data.setModifiedAttributesList(modifiedAttributesList);
				}
				dataList.add(data);
			}

		}

		FileWriter fileWriter = null;
		final File rootFolder1 = new File(configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.PROMOTIONS_REPORT_FILE_LOCATION),
				MarketplacecommerceservicesConstants.PROMOTION_REPORT + System.currentTimeMillis());
		try
		{
			rootFolder1.getParentFile().mkdirs();
			fileWriter = new FileWriter(rootFolder1);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER);

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for (final PromotionalReportData data : dataList)
			{
				fileWriter.append(data.getUser());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getPromoCode());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getTimestamp().toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getModificationType());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getStartDate().toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getEndDate().toString());
				fileWriter.append(COMMA_DELIMITER);
				final List<String> modifiedAttributesList = data.getModifiedAttributesList();
				for (final String modifiedAttribute : modifiedAttributesList)
				{
					fileWriter.append(modifiedAttribute);
					fileWriter.append(TILDE_DELIMITER);
				}


				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in CsvFileWriter !!!");
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
}