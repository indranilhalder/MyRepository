package com.hybris.oms.tata.pincodesupload;

/**
 *
 * This class is for uploading pincodes by taking excel file or csv file
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.services.FilePathProviderService;



/**
 *
 * @author techouts
 */

@SuppressWarnings("serial")
public class PincodesUploadController extends DefaultWidgetController
{

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;
	private static final String PINCODE_FILENAME = "Pincode-";
	private static final String LANDMARKS_FILENAME = "landMarks-";
	private static final String DATE_FORMAT = "ddMMyyyyHHss";
	private Label pincodeError;
	private Label landmarkError;
	private Textbox pincodeText;
	private Textbox landmarkText;
	private String pinCodeuploadFilePath = "";
	private String landmarkUploadFilePath = "";
	private static final String CSV = ".csv";
	final static String[] LIST_OF_ERROR =
	{ "pinCodeupload FilePath" };
	final static String[] LIST_OF_LANDMARKERROR =
	{ "landmarUpload FilePath" };
	private Media media;
	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		pincodeText.setText("");
		landmarkText.setText("");
		pinCodeuploadFilePath = filePathProviderService.getPincodesUploadPath();
		landmarkUploadFilePath = filePathProviderService.getLandmarkUploadPath();
		pincodeError.setVisible(false);
		landmarkError.setVisible(false);
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "pincode-upload", eventName = Events.ON_UPLOAD)
	public void pincodesUploadsEvent(final UploadEvent uploadEvent)
	{
		pincodeText.setText("");
		pincodeError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			try
			{
				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, pinCodeuploadFilePath))
				{
					return;
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("pinCodeuploadFilePath not found" + e.getMessage());
			}
			if (media.isBinary())
			{
				try
				{
					if (media.getStreamData().read() == -1)
					{
						LOG.info("File is Empty ...");
						Messagebox.show("File is Empty ");
						return;
					}
				}
				catch (final IOException e1)
				{
					LOG.error("unable to read null media " + e1.getMessage());
				}
				try
				{
					LOG.info(media.getStreamData().toString());
					FileUtils.copyInputStreamToFile(media.getStreamData(), getFile(PINCODE_FILENAME, pinCodeuploadFilePath));
					pincodeText.setText(fileName);
					Messagebox.show("File uploaded successfully");
				}
				catch (final IOException e)
				{
					LOG.error("unable to copyInputStreamToFile" + e.getMessage());
				}
			}
			else
			{
				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				try
				{
					LOG.info(media.getStringData().toString());
					FileUtils.writeStringToFile(getFile(PINCODE_FILENAME, pinCodeuploadFilePath), media.getStringData());
					pincodeText.setText(fileName);
					Messagebox.show("File uploaded successfully");
				}
				catch (final IOException e)
				{
					LOG.error("unable to writeStringToFile" + e.getMessage());
				}
			}
		}
		else
		{
			pincodeError.setVisible(true);
		}
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "landmark_upload", eventName = Events.ON_UPLOAD)
	public void landMarksUploadEvent(final UploadEvent uploadEvent)
	{
		landmarkText.setText("");
		landmarkError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();

		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			try
			{
				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_LANDMARKERROR, landmarkUploadFilePath))
				{
					return;
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("landmarkUploadFilePath not found" + e.getMessage());
			}

			if (media.isBinary())
			{
				try
				{
					if (media.getStreamData().read() == -1)
					{
						LOG.info("File is Empty ...");
						Messagebox.show("File is Empty ");
						return;
					}
				}
				catch (final IOException e1)
				{
					LOG.error("unable to read null media " + e1.getMessage());
				}
				try
				{
					FileUtils.copyInputStreamToFile(media.getStreamData(), getFile(LANDMARKS_FILENAME, landmarkUploadFilePath));
					landmarkText.setText(fileName);
					Messagebox.show("File uploaded successfully");
				}
				catch (final IOException e)
				{
					LOG.error("unable to copyInputStreamToFile" + e.getMessage());
				}
			}
			else
			{
				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				try
				{
					LOG.info(media.getStringData().toString());
					FileUtils.writeStringToFile(getFile(LANDMARKS_FILENAME, landmarkUploadFilePath), media.getStringData());
					landmarkText.setText(fileName);
					Messagebox.show("File uploaded successfully");
				}
				catch (final IOException e)
				{
					LOG.error("unable to writeStringToFile" + e.getMessage());
				}
			}
		}
		else
		{
			landmarkError.setVisible(true);
		}
	}

	/**
	 * this method is used for getting PincodeFormat file.
	 *
	 * @return File
	 */
	public File getFile(final String fileName, final String filePathLocation)
	{
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		final String TimeStamp = simpleDateFormat.format(new Date());
		final String fileNameTimeStamp = fileName.concat(TimeStamp).concat(CSV);
		final File destFile = new File(filePathLocation.trim(), fileNameTimeStamp);
		LOG.info("Now Media name is " + destFile.getName());
		return destFile;
	}
}
