/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
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

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BulkPromotionCreationJobModel;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class BusinessContentImportJob extends AbstractJobPerformable<CronJobModel>
{

	private static final String FILE = "File ";
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BusinessContentImportJob.class.getName());

	@SuppressWarnings("unused")
	private static final String ERROR_PREFIX = "ERROR_";
	@SuppressWarnings("unused")
	private static final String PROCESSED_PREFIX = "PROCESSED_";

	@Autowired
	private ConfigurationService configurationService;

	@Resource
	BusinessContentImportUtility businessContentImportUtil;


	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		try
		{
			LOG.debug("Processing to create Buisness Content in Bulk");


			//Description: To upload and create Content Data
			uploadCSVContent();


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
	 * @Descriptiion: To Process and create Content Data
	 *
	 */
	private void uploadCSVContent()
	{
		final File rootFolder1 = new File(configurationService.getConfiguration().getString("businessConetnt.rootFolderLocation",
				""));
		final File errorFolder = new File(configurationService.getConfiguration().getString("businessConetnt.errorFolderLocation",
				""));
		final File archiveFolder = new File(configurationService.getConfiguration().getString(
				"businessConetnt.archiveFolderLocation", ""));
		final String fileNamePrefix = configurationService.getConfiguration()
				.getString("businessConetnt.fileNamePrefix", "content");
		final String fileExtension = configurationService.getConfiguration().getString("businessConetnt.fileExtension", "csv");
		isFolderExist(rootFolder1);
		isFolderExist(errorFolder);
		isFolderExist(archiveFolder);
		//isFolderExist(archiveFolder);
		boolean flag = false;
		if (rootFolder1.exists())
		{
			for (final File inputFile : rootFolder1.listFiles())
			{
				if (inputFile.getName().startsWith(fileNamePrefix) && inputFile.getName().endsWith(fileExtension))
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
						if (inputFile.getName().equals("content"))
						{
							flag = true;
						}
						businessContentImportUtil.processFile(input, output, flag);
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is processed. " + "\n");
						input.close();
						output.close();
						if (errorFile.exists() && errorFile.length() == 0)
						{
							errorFile.delete();

						}
						else
						{
							final File testFile = new File(errorFolder, ERROR_PREFIX + datePrefix + inputFile.getName());
							FileUtils.moveFileToDirectory(errorFile, testFile, true);

						}

						final File processedFile = new File(archiveFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
						FileUtils.moveFileToDirectory(inputFile, processedFile, true);
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
	private void uploadProduct(final BulkPromotionCreationJobModel cronJobModel)
	{
		final File rootFolder1 = new File(configurationService.getConfiguration().getString("businessConetnt.rootFolderLocation",
				""));
		final File errorFolder = new File(configurationService.getConfiguration().getString("businessConetnt.errorFolderLocation",
				""));
		final File archiveFolder = new File(configurationService.getConfiguration().getString(
				"businessConetnt.archiveFolderLocation", ""));
		final String fileNamePrefix = configurationService.getConfiguration().getString(
				"businessConetnt.fileNamePrefix.ProductUpload", "content");
		final String fileExtension = configurationService.getConfiguration().getString("businessConetnt.fileExtension", "csv");
		isFolderExist(rootFolder1);
		isFolderExist(errorFolder);
		isFolderExist(archiveFolder);
		isFolderExist(archiveFolder);
		boolean flag = false;
		if (rootFolder1.exists())
		{
			for (final File inputFile : rootFolder1.listFiles())
			{
				if (inputFile.getName().startsWith(fileNamePrefix) && inputFile.getName().endsWith(fileExtension))
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
						flag = false;
						businessContentImportUtil.processFile(input, output, flag);
						LOG.debug(FILE + inputFile.getAbsolutePath() + " is processed. " + "\n");
						input.close();
						output.close();
						if (errorFile.exists() && errorFile.length() == 0)
						{
							errorFile.delete();
							final File processedFile = new File(archiveFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							inputFile.renameTo(processedFile);
						}
						else
						{
							final File processedFile = new File(archiveFolder, PROCESSED_PREFIX + datePrefix + inputFile.getName());
							FileUtils.moveFileToDirectory(inputFile, processedFile, true);

							final File testFile = new File(errorFolder, ERROR_PREFIX + datePrefix + inputFile.getName());
							FileUtils.moveFileToDirectory(errorFile, testFile, true);

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
