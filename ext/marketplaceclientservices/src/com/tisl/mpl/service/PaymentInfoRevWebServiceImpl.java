/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;


/**
 * @author TCS
 *
 */
public class PaymentInfoRevWebServiceImpl
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private static final Logger LOG = Logger.getLogger(PaymentInfoRevWebServiceImpl.class);


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.PaymentInfoRevWebService#paymentInfoRev()
	 */

	public void paymentInfoRev(final String xmlString)
	{
		try
		{
			//24 hour format
			final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
			final Date curDate = new Date();
			final String strDate = sdf.format(curDate);
			final String folderPath = configurationService.getConfiguration().getString("paymentInfo.batchJob.folder.path");
			/** Create directory if not present */
			createDirectoryIfNeeded(folderPath);
			String fileName = configurationService.getConfiguration().getString("paymentInfo.batchJob.fileName");
			fileName = strDate + fileName;
			File xmlfile = new File(fileName);
			xmlfile = new File(folderPath + fileName);
			xmlfile.setReadable(true);
			xmlfile.setWritable(true);
			xmlfile.setExecutable(true);
			xmlfile.canRead();
			xmlfile.canWrite();
			xmlfile.canExecute();
			FileUtils.writeStringToFile(xmlfile, xmlString);
			LOG.debug("File created successfully!!!");
		}



		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
		}

	}

	private void createDirectoryIfNeeded(final String directoryName)
	{
		if (StringUtils.isNotEmpty(directoryName))
		{
			final File theDir = new File(directoryName);
			// if the directory does not exist, create it
			if (!theDir.exists())
			{
				LOG.debug("creating directory: " + directoryName);
				theDir.mkdirs();
			}
		}
	}


}
