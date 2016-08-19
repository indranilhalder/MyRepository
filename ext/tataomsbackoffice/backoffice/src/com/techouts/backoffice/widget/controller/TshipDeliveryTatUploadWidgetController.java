/**
 *
 */
package com.techouts.backoffice.widget.controller;

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

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.pincodesupload.PincodesUploadController;
import com.hybris.oms.tata.services.FilePathProviderService;
import com.hybris.oms.tata.util.DataUploadService;


/**
 * @author prabhakar
 *
 */
public class TshipDeliveryTatUploadWidgetController extends DefaultWidgetController
{


	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	@WireVariable
	private DataUploadService dataUploadService;

	private Label returnOrderEmptyFileError;
	private Label fragileOrderEmptyFileError;
	private Media media;

	private String returnOrderFilePath = "";
	private String fragileOrderFilePath = "";
	private static final String CSV = ".csv";



	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		returnOrderFilePath = filePathProviderService.getReturnOrderFileUploadPath();
		fragileOrderFilePath = filePathProviderService.getFragileOrderFileUploadPath();
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "returnOrderupload", eventName = Events.ON_UPLOAD)
	public void returnOrderUploadEvent(final UploadEvent event)
	{
		if ("null".equals(returnOrderFilePath) || "".equals(returnOrderFilePath) || returnOrderFilePath == null)
		{
			Messagebox.show("Unable to find Pincode file path config inside PropertyFile", "Error", Messagebox.OK, Messagebox.ERROR);
		}
		else
		{
			DataUplaod(event.getMedia(), getFile("returnOrder-", returnOrderFilePath), returnOrderEmptyFileError);
		}
	}

	@ViewEvent(componentID = "frigileOrderupload", eventName = Events.ON_UPLOAD)
	public void landmarkUploadEvent(final UploadEvent event)
	{

		if ("null".equals(fragileOrderFilePath) || "".equals(fragileOrderFilePath) || fragileOrderFilePath == null)
		{
			Messagebox.show("Unable to find landmark file path config inside PropertyFile", "Error", Messagebox.OK,
					Messagebox.ERROR);
		}
		else
		{
			DataUplaod(event.getMedia(), getFile("fragileOrder-", fragileOrderFilePath), fragileOrderEmptyFileError);
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