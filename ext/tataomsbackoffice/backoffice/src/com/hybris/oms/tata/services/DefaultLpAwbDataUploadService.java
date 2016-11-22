/**
 *
 */
package com.hybris.oms.tata.services;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.techouts.backoffice.exception.InvalidFileNameException;


/**
 * this class is Used For Lp Awb BuklUpload Purpose
 *
 * @author Techouts
 */
public class DefaultLpAwbDataUploadService implements LpAwbDataUploadService
{
	private Media media;
	private static final String CSV = ".csv";
	private static final String DATE_FORMAT = "ddMMyyyyHHmm";
	private static final String[] PROPERTY_FILE_ERRORS =
	{ "Lp Awb Forward File Path", "Lp Awb Return File Path" };
	private static final Logger LOG = Logger.getLogger(DefaultLpAwbDataUploadService.class);
	@Resource(name = "filePathProviderService")
	private FilePathProviderService filePathProviderService;

	@Override
	public void lpAwbBulkUploadCommon(final UploadEvent uploadEvent)
	{
		try
		{
			if (!filePathProviderService.propertyFilePathValidation(PROPERTY_FILE_ERRORS,
					TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH, TataomsbackofficeConstants.LPAWB_RETURN_FILE_PATH))
			{
				return;
			}
			final String destinationFile;
			final String destFilePath;
			media = uploadEvent.getMedia();
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
			final String TimeStamp = simpleDateFormat.format(new Date());
			if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX))
			{
				destinationFile = TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX.concat(TimeStamp).concat(CSV);
				destFilePath = TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH;
			}
			else if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_RETURN_FiLE_PREFIX))
			{
				destinationFile = TataomsbackofficeConstants.LPAWB_RETURN_FiLE_PREFIX.concat(TimeStamp).concat(CSV);
				destFilePath = TataomsbackofficeConstants.LPAWB_RETURN_FILE_PATH;
			}
			else
			{
				throw new InvalidFileNameException("Invalid file prefix name");
			}
			if (media.isBinary())
			{
				if (media.getStreamData().read() == -1)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				FileUtils.copyInputStreamToFile(media.getStreamData(), new File(destFilePath.trim(), destinationFile));
				Messagebox.show("File uploaded successfully");
			} //end is Binary Check
			else
			{
				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				FileUtils.writeStringToFile(new File(destFilePath.trim(), destinationFile), media.getStringData());
				Messagebox.show("File uploaded successfully");
			}
		}
		catch (final InvalidFileNameException e)
		{
			Messagebox.show(e.getMessage());
		}
		catch (final Exception message)
		{
			LOG.error(message.toString());
		}
	}

	/**
	 * @param filePathProviderService
	 *           the filePathProviderService to set
	 */
	public void setFilePathProviderService(final FilePathProviderService filePathProviderService)
	{
		this.filePathProviderService = filePathProviderService;
	}

}
