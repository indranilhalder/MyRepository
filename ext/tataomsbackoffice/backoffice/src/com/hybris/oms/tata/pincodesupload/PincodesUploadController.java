package com.hybris.oms.tata.pincodesupload;

/**
 *
 * This class is for uploading pincodes by taking excel file or csv file
 */

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.hybris.oms.tata.util.DataUploadService;



/**
 * @author irfan
 * @author prabhakar modified on 29-06-2016
 */

@SuppressWarnings("serial")
public class PincodesUploadController extends DefaultWidgetController
{

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

@WireVariable
	private DataUploadService dataUploadService;

	private Label pincodeError;
	private Label landmarkError;

	private Label error;
	private Textbox text;
	private Media media;
	private String pinCodeuploadFilePath = "";
	private String landmarkUploadFilePath = "";
	private static final String CSV = ".csv";
	final static String[] LIST_OF_ERROR =
	{ "pinCodeupload FilePath" };

	private final String message = "";

	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		text.setText("");
		pinCodeuploadFilePath = filePathProviderService.getPincodesUploadPath();
		landmarkUploadFilePath = filePathProviderService.getLandmarkUploadPath();
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "upload_button", eventName = Events.ON_UPLOAD)
	public void pincodeUploadEvent(final UploadEvent event)
	{
		if ("null".equals(pinCodeuploadFilePath) || "".equals(pinCodeuploadFilePath) || pinCodeuploadFilePath == null)
		{
			Messagebox.show("Unable to find Pincode file path config inside PropertyFile", "Error", Messagebox.OK, Messagebox.ERROR);
		}
		else
		{
			DataUplaod(event.getMedia(), getFile("Pincodes-", pinCodeuploadFilePath), pincodeError);
		}
	}

	@ViewEvent(componentID = "landmark_upload", eventName = Events.ON_UPLOAD)
	public void landmarkUploadEvent(final UploadEvent event)
	{

		if ("null".equals(landmarkUploadFilePath) || "".equals(landmarkUploadFilePath) || landmarkUploadFilePath == null)
		{
			Messagebox
					.show("Unable to find landmark file path config inside PropertyFile", "Error", Messagebox.OK, Messagebox.ERROR);
		}
		else
		{
			DataUplaod(event.getMedia(), getFile("landMarks-", landmarkUploadFilePath), landmarkError);
		}
	}

	/**
	 *
	 * @param media
	 * @param uploadeFilePath
	 * @param prefix
	 */
	private void DataUplaod(final Media media, final File file, final Label error)
	{

		final String fileName = media.getName();

		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			try
			{
				Messagebox.show(dataUploadService.dataUpload(media, file));
			}
			catch (final IOException e)
			{
				Messagebox.show("Unable to upload file try again");
			}
		}
		else
		{
			error.setVisible(true);
		}
	}
	/**
	 * this method is used for getting PincodeFormat file.
	 *
	 * @return File
	 */
	public File getFile(final String prefix, final String filePath)
	{
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
		final String TimeStamp = simpleDateFormat.format(new Date());
		final String fileNameTimeStamp = prefix.concat(TimeStamp).concat(CSV);
		final File destFile = new File(filePath.trim(), fileNameTimeStamp);
		LOG.info("Now Media name is " + destFile.getName());
		return destFile;
	}
}
