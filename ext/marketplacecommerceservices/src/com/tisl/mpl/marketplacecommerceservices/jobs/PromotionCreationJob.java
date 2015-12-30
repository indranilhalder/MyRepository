/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BulkPromotionCreationJobModel;
import com.tisl.mpl.promotion.processor.BulkPromotionCreationProcessor;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class PromotionCreationJob extends AbstractJobPerformable<BulkPromotionCreationJobModel>
{

	private static final String FILE = "File ";
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());

	@SuppressWarnings("unused")
	private static String ERROR_PREFIX = "ERROR_";
	@SuppressWarnings("unused")
	private static String PROCESSED_PREFIX = "PROCESSED_";

	@Autowired
	private ConfigurationService configurationService;

	private BulkPromotionCreationProcessor bulkPromotionCreationProcessor;

	/**
	 * @return the bulkPromotionCreationProcessor
	 */
	public BulkPromotionCreationProcessor getBulkPromotionCreationProcessor()
	{
		return bulkPromotionCreationProcessor;
	}

	/**
	 * @param bulkPromotionCreationProcessor
	 *           the bulkPromotionCreationProcessor to set
	 */
	public void setBulkPromotionCreationProcessor(final BulkPromotionCreationProcessor bulkPromotionCreationProcessor)
	{
		this.bulkPromotionCreationProcessor = bulkPromotionCreationProcessor;
	}

	/**
	 * @Descriptiion: CronJob to read the uploaded Promotion and Restriction csv
	 * @param: bulkPromoCronModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final BulkPromotionCreationJobModel bulkPromoCronModel)
	{
		try
		{
			LOG.debug("Processing to create Promotion in Bulk");
			LOG.debug("Uploaded File extension:" + bulkPromoCronModel.getFileExtension());

			//Description: To upload and create Promotion Data
			uploadPromotionData(bulkPromoCronModel);
			uploadRestrictionDataFile(bulkPromoCronModel);

		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @Descriptiion: To Process and create Promotion Data
	 * @param bulkPromoCronModel
	 */
	private void uploadPromotionData(final BulkPromotionCreationJobModel bulkPromoCronModel)
	{
		final File rootFolder1 = new File(configurationService.getConfiguration().getString("cronjob.promotion.feed.path", ""));
		final File errorFolder = new File(configurationService.getConfiguration().getString(
				"cronjob.promotion.feed.promotion.errorFiles.path", ""));
		final File processedFolder = new File(configurationService.getConfiguration().getString(
				"cronjob.promotion.feed.promotion.processedFiles.path", ""));
		isFolderExist(rootFolder1);
		isFolderExist(errorFolder);
		isFolderExist(processedFolder);
		boolean flag = false;
		if (rootFolder1.exists())
		{
			for (final File inputFile : rootFolder1.listFiles())
			{
				if (inputFile.getName().startsWith(bulkPromoCronModel.getFileNamePrefix())
						&& inputFile.getName().endsWith(bulkPromoCronModel.getFileExtension()))
				{
					String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
					if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
					{
						datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
					}
					final File errorFile = new File(rootFolder1, ERROR_PREFIX + inputFile.getName());
					try
					{
						final FileInputStream input = new FileInputStream(inputFile);
						final OutputStream output = new BufferedOutputStream(new FileOutputStream(errorFile));
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is being processed. " + "\n");
						flag = true;
						bulkPromotionCreationProcessor.processFile(input, output, flag);
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is processed. " + "\n");
						input.close();
						output.close();
						if (errorFile.exists() && errorFile.length() == 0)
						{
							errorFile.delete();
							final File processedFile = new File(processedFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							inputFile.renameTo(processedFile);
						}
						else
						{
							final File processedFile = new File(processedFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							inputFile.renameTo(processedFile);
							final File testFile = new File(errorFolder, ERROR_PREFIX + datePrefix + inputFile.getName());
							errorFile.renameTo(testFile);
						}
					}
					catch (final FileNotFoundException e)
					{
						LOG.error("Cannot find file for batch update. " + e.getLocalizedMessage());
					}
					catch (final IOException e)
					{
						LOG.error("Exception closing file handle. " + e.getLocalizedMessage());
					}
				}
			}
		}
	}


	/**
	 * @Description: Read uploaded .csv File to create Promotion Restriction
	 * @param cronJobModel
	 */
	private void uploadRestrictionDataFile(final BulkPromotionCreationJobModel cronJobModel)
	{
		final File rootFolder2 = new File(configurationService.getConfiguration().getString(
				"cronjob.promotion.feedRestriction.path", ""));
		final File errorFolder = new File(configurationService.getConfiguration().getString(
				"cronjob.promotion.feed.promotion.errorFiles.path", ""));
		final File processedFolder = new File(configurationService.getConfiguration().getString(
				"cronjob.promotion.feed.promotion.processedFiles.path", ""));
		isFolderExist(rootFolder2);
		isFolderExist(errorFolder);
		isFolderExist(processedFolder);
		boolean flag = false;
		if (rootFolder2.exists())
		{
			for (final File inputFile : rootFolder2.listFiles())
			{
				if (inputFile.getName().startsWith(
						configurationService.getConfiguration().getString("cronjob.promotion.feedRestriction.prefix", ""))
						&& inputFile.getName().endsWith(cronJobModel.getFileExtension()))
				{
					String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
					if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
					{
						datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
					}
					final File errorFile = new File(rootFolder2, ERROR_PREFIX + inputFile.getName());
					try
					{
						final FileInputStream input = new FileInputStream(inputFile);
						final OutputStream output = new BufferedOutputStream(new FileOutputStream(errorFile));
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is being processed. " + "\n");
						flag = false;
						bulkPromotionCreationProcessor.processFile(input, output, flag);
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is processed. " + "\n");
						input.close();
						output.close();
						if (errorFile.exists() && errorFile.length() == 0)
						{
							errorFile.delete();
							final File processedFile = new File(processedFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							inputFile.renameTo(processedFile);
						}
						else
						{
							final File processedFile = new File(processedFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							inputFile.renameTo(processedFile);
							final File testFile = new File(errorFolder, ERROR_PREFIX + datePrefix + inputFile.getName());
							errorFile.renameTo(testFile);
						}
					}
					catch (final FileNotFoundException e)
					{
						LOG.error("Cannot find file for batch update. " + e.getLocalizedMessage());
					}
					catch (final IOException e)
					{
						LOG.error("Exception closing file handle. " + e.getLocalizedMessage());
					}
				}
			}
		}
	}

	/**
	 * @Description : Generate Folder if not present
	 * @param file
	 */
	private void isFolderExist(final File file)
	{
		if (null != file && !file.exists())
		{
			file.mkdir();
			LOG.debug("Generated Folder:" + file.getName());
		}
	}

}
