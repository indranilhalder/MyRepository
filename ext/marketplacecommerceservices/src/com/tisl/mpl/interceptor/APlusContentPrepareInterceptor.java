/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.businesscontentimport.BusinessContentImportUtility;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.APlusContentModel;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class APlusContentPrepareInterceptor implements PrepareInterceptor
{
	@SuppressWarnings("unused")
	private static final String ERROR_PREFIX = "ERROR_";
	@SuppressWarnings("unused")
	private static final String PROCESSED_PREFIX = "PROCESSED_";

	private static final String LUX = "lux";

	@Autowired
	private ConfigurationService configurationService;
	@Resource
	BusinessContentImportUtility businessContentImportUtil;
	private static final Logger LOG = Logger.getLogger(APlusContentPrepareInterceptor.class); //critical SONAR FIX
	@Autowired
	MediaService mediaService;



	/**
	 * @Description This method will skip the cron job process and execute the content creation before uploading csv
	 */
	@Override
	public void onPrepare(final Object o, final InterceptorContext its) throws InterceptorException
	{
		if (o instanceof APlusContentModel)
		{
			try
			{
				//calling the uploadCSVContent with the input stream and the real file name from Media Model
				final APlusContentModel aPlusModel = (APlusContentModel) o;
				final String siteToLoad = aPlusModel.getSiteToLoad().getCode().toString();

				LOG.debug("Site to Load:" + siteToLoad);

				if (!aPlusModel.getCsvFile().getMime().equals("text/csv"))
				{
					throw new InterceptorException("Please upload a valid CSV file. MIME type is not text/csv. Current MINE type is "
							+ aPlusModel.getCsvFile().getMime());
				}
				else if (StringUtils.isEmpty(aPlusModel.getCsvFile().getURL()))
				{
					throw new InterceptorException("Please upload file");
				}
				final InputStream inputStream = mediaService.getStreamFromMedia(aPlusModel.getCsvFile());

				String error = null;
				if (null != siteToLoad && siteToLoad.equals(LUX))
				{
					LOG.debug("inside Lux Condition: Selected Site is ==" + siteToLoad);
					error = this.uploadCSVContent(inputStream, aPlusModel.getCsvFile().getRealFileName(), siteToLoad);
				}
				else
				{
					LOG.debug("Selected Site is ==" + siteToLoad);
					error = this.uploadCSVContent(inputStream, aPlusModel.getCsvFile().getRealFileName());
				}
				if (StringUtils.isNotEmpty(error))
				{
					aPlusModel.setCsvFileError(error);
				}

			}
			catch (final Exception e)
			{
				LOG.error("onPrepare A Plus Content exception " + e.getMessage());
				throw new InterceptorException("IO Exception/ File handling exception has occured" + e.getMessage());
			}

		}

	}

	private String uploadCSVContent(final InputStream inputStream, final String inputFileName)
	{
		return uploadCSVContent(inputStream, inputFileName, null);
	}

	/**
	 * @Descriptiion: To Process and create Content Data
	 *
	 */
	private String uploadCSVContent(final InputStream inputStream, final String inputFileName, final String site)
	{
		String error = null;
		final File rootFolder = new File(
				configurationService.getConfiguration().getString("businessConetnt.rootFolderLocation", ""));
		final File errorFolder = new File(
				configurationService.getConfiguration().getString("businessConetnt.errorFolderLocation", ""));
		final String fileNamePrefix = configurationService.getConfiguration().getString("businessConetnt.fileNamePrefix",
				"content");

		isFolderExist(rootFolder);
		isFolderExist(errorFolder);
		boolean flag = false;

		String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}
		final File errorFile = new File(rootFolder, ERROR_PREFIX + inputFileName);
		try
		{
			final OutputStream output = new BufferedOutputStream(new FileOutputStream(errorFile));
			if (inputFileName.startsWith(fileNamePrefix))
			{
				flag = true;
			}
			if (null != site && site.equals(LUX))
			{
				LOG.debug("inside lux - Selected Site is ==" + site);
				error = businessContentImportUtil.processFile(inputStream, output, flag, site);
			}
			else
			{
				LOG.debug("Selected Site is ==" + site);
				error = businessContentImportUtil.processFile(inputStream, output, flag);
			}

			inputStream.close();
			output.close();
			if (errorFile.exists() && errorFile.length() == 0)
			{
				errorFile.delete();
			}
			else
			{
				final File testFile = new File(errorFolder, ERROR_PREFIX + datePrefix + inputStream);
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
		return error;
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
