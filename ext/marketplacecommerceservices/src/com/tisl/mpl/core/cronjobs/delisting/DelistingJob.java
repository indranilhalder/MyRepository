/**
 *
 */
package com.tisl.mpl.core.cronjobs.delisting;

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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.DelistingJobHelperModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class DelistingJob extends AbstractJobPerformable<DelistingJobHelperModel>
{ //DelistingJobHelperModel

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DelistingJob.class.getName());

	@SuppressWarnings("unused")
	private static final String ERROR_PREFIX = "ERROR_";
	@SuppressWarnings("unused")
	private static final String PROCESSED_PREFIX = "PROCESSED_";

	@Autowired
	private DelistingProcessor mplDelistingProcessor;

	@Autowired
	private ConfigurationService configurationService;



	/**
	 * @return the mplDelistingProcessor
	 */
	public DelistingProcessor getMplDelistingProcessor()
	{
		return mplDelistingProcessor;
	}

	/**
	 * @param mplDelistingProcessor
	 *           the mplDelistingProcessor to set
	 */
	public void setMplDelistingProcessor(final DelistingProcessor mplDelistingProcessor)
	{
		this.mplDelistingProcessor = mplDelistingProcessor;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final DelistingJobHelperModel delisting)
	{
		try
		{
			LOG.debug("Delisting Job Starts...");
			//LOG.debug("Uploaded File extension:" + delisting.getFileExtension());
			final File rootFolder = new File(getConfigurationService().getConfiguration().getString(
					"cronjob.delisting.feed.path.main", ""));
			final File archiveFolder = new File(getConfigurationService().getConfiguration().getString(
					"cronjob.delisting.feed.path.archive", ""));
			final File errorFolder = new File(getConfigurationService().getConfiguration().getString(
					"cronjob.delisting.feed.delisting.processedFiles.path", ""));
			isFolderExist(rootFolder);
			isFolderExist(archiveFolder);


			uploadData(delisting, rootFolder, errorFolder, archiveFolder);


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
	 * // * @Description : Generate Folder if not present // * @param file //
	 */
	private void isFolderExist(final File file)
	{
		if (null != file)
		{
			if (!file.exists())
			{
				file.mkdir();
				LOG.debug("Generated Folder:" + file.getName());
			}
		}
	}



	private void uploadData(final DelistingJobHelperModel sellerDelisting, final File rootFolder, final File errorFolder,
			final File archiveFolder)
	{
		if (rootFolder.exists())
		{
			boolean flag = true;
			for (final File inputFile : rootFolder.listFiles())
			{
				if (inputFile.getName().startsWith(sellerDelisting.getFileNamePrefix())
						&& inputFile.getName().endsWith(sellerDelisting.getFileExtension()))
				{
					//					String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
					//					if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
					//					{
					//						datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
					//					}
					final File errorFile = new File(rootFolder, ERROR_PREFIX + inputFile.getName());
					try
					{
						final FileInputStream input = new FileInputStream(inputFile);
						final OutputStream output = new BufferedOutputStream(new FileOutputStream(errorFile));
						if (inputFile.getName().startsWith(
								getConfigurationService().getConfiguration().getString("cronjob.delisting.filename.seller", "")))
						{
							LOG.debug("File " + inputFile.getAbsolutePath() + " is being processed. " + "\n");
							flag = true;
							getMplDelistingProcessor().processFile(input, output, flag);

						}
						else if (inputFile.getName().startsWith(
								getConfigurationService().getConfiguration().getString("cronjob.delisting.filename.ussid", "")))
						{
							LOG.debug("File " + inputFile.getAbsolutePath() + " is being processed. " + "\n");
							flag = false;
							getMplDelistingProcessor().processFile(input, output, flag);
						}
						else
						{
							LOG.error("Can process files starting with File Name:" + inputFile.getName());
						}

						errorFile.delete();
						input.close();
						output.close();

						FileUtils.moveFileToDirectory(inputFile, archiveFolder, true);



					}


					catch (final FileNotFoundException e)
					{
						LOG.error("Cannot find file for Delisting. " + e.getLocalizedMessage());
					}
					catch (final IOException e)
					{
						LOG.error("Exception closing file handle. " + e.getLocalizedMessage());
					}
				}

				else
				{
					if (!archiveFolder.getName().equalsIgnoreCase(inputFile.getName()))
					{
						LOG.error("Could not able to Process File" + inputFile.getName() + " as File  Prefix doesnot exist");

					}
				}
			}
		}
	}
}
