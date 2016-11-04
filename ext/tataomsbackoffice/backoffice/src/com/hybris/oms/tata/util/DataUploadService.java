/**
 *
 */
package com.hybris.oms.tata.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;


/**
 * @author prabhakar utiltiy class to copy the CSV data to file
 */
public class DataUploadService
{

	private static final Logger LOG = LoggerFactory.getLogger(DataUploadService.class);

	/**
	 *
	 * @param media
	 * @param file
	 *
	 * @throws IOException
	 */
	public String dataUpload(final Media media, final File file) throws IOException
	{
		if (media.getStringData().length() == 0)
		{
			LOG.info("File is Empty ...");
			return "File is Empty ";
		}
		else
		{
			LOG.info(media.getStringData().toString());
			FileUtils.writeStringToFile(file, media.getStringData());
			return "File uploaded successfully";
		}

	}
}
