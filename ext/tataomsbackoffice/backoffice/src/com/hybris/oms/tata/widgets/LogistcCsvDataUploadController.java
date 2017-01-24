/**
 *
 */
package com.hybris.oms.tata.widgets;

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
import com.hybris.oms.tata.pincodesupload.PincodesUploadController;
import com.hybris.oms.tata.services.FilePathProviderService;


/**
 *
 */
public class LogistcCsvDataUploadController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	private Label emptyFileError;
	private Textbox textbox;
	private Media media;
	private String shipmentStatusUploadFilePath = "";
	private static final String CSV = ".csv";
	final static String[] LIST_OF_ERROR =
	{ "Bulk Upload Shipment Status FilePath" };

	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		textbox.setText("");
		emptyFileError.setVisible(false);
		shipmentStatusUploadFilePath = filePathProviderService.getShipmentStatusUpload();
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "upload_button", eventName = Events.ON_UPLOAD)
	public void selectUploadPincodes(final UploadEvent uploadEvent)
	{
		textbox.setText("");
		emptyFileError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();

		if (fileName.endsWith(CSV) && fileName.startsWith("ShipmentStatus"))
		{
			try
			{
				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, shipmentStatusUploadFilePath))
				{
					return;
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("shipmentStatusUploadFilePath not found" + e.getMessage());
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
					FileUtils.copyInputStreamToFile(media.getStreamData(), getFile());
					textbox.setText(fileName);
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
					FileUtils.writeStringToFile(getFile(), media.getStringData());
					textbox.setText(fileName);
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
			emptyFileError.setVisible(true);
		}
	}

	/**
	 * this method is used for getting Shipment status upload Format file.
	 *
	 * @return File
	 */
	public File getFile()
	{
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		final String TimeStamp = simpleDateFormat.format(new Date());
		final String fileNameTimeStamp = "ShipmentStatus-".concat(TimeStamp).concat(CSV);
		final File destFile = new File(shipmentStatusUploadFilePath.trim(), fileNameTimeStamp);
		LOG.info("Now Media name is " + destFile.getName());
		return destFile;
	}

}
