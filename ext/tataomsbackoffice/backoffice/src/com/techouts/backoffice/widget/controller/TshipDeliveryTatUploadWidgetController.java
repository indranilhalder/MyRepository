/**
 *
 */
package com.techouts.backoffice.widget.controller;

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
 * @author prabhakar
 *
 */
public class TshipDeliveryTatUploadWidgetController extends DefaultWidgetController
{

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;
	private static final String PRECIOUS_FRAZILE_FiLE_PREFIX = "PFSDLogisticsServicebility_";
	private static final String RETURN_FiLE_PREFIX = "ReturnLogisticsServiceability_";
	private static final String DATE_FORMAT = "ddMMyyyyHHss";
	private Label pfsdError;
	private Label returnError;
	private Textbox pfsdText;
	private Textbox returnText;
	private String fragileOrderFilePath = "";
	private String returnOrderFilePath = "";
	private static final String CSV = ".csv";
	final static String[] LIST_OF_ERROR =
	{ "Precious fragile FilePath", "Return Order FilePath" };
	private Media media;
	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		pfsdText.setText("");
		returnText.setText("");
		fragileOrderFilePath = filePathProviderService.getFragileOrderFileUploadPath();
		returnOrderFilePath = filePathProviderService.getReturnOrderFileUploadPath();
		pfsdError.setVisible(false);
		returnError.setVisible(false);
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "preciousfragile-upload", eventName = Events.ON_UPLOAD)
	public void pincodesUploadsEvent(final UploadEvent uploadEvent)
	{
		pfsdText.setText("");
		pfsdError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			try
			{
				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, new String[]
				{ fragileOrderFilePath, returnOrderFilePath }))
				{
					return;
				}
				if (!fileName.startsWith(PRECIOUS_FRAZILE_FiLE_PREFIX))
				{
					pfsdError.setValue("Precious Fragile File Name Should Starts With " + PRECIOUS_FRAZILE_FiLE_PREFIX + "");
					pfsdError.setVisible(true);
					return;
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("fragileOrderFilePath not found" + e.getMessage());
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
					FileUtils.copyInputStreamToFile(media.getStreamData(),
							getFile(PRECIOUS_FRAZILE_FiLE_PREFIX, fragileOrderFilePath));
					pfsdText.setText(fileName);
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
					FileUtils.writeStringToFile(getFile(PRECIOUS_FRAZILE_FiLE_PREFIX, fragileOrderFilePath), media.getStringData());
					pfsdText.setText(fileName);
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
			pfsdError.setVisible(true);
		}
	}

	/**
	 * this method will call while uploading file
	 *
	 * @param uploadEvent
	 */
	@ViewEvent(componentID = "return_upload", eventName = Events.ON_UPLOAD)
	public void landMarksUploadEvent(final UploadEvent uploadEvent)
	{
		returnText.setText("");
		returnError.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();

		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			try
			{
				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, new String[]
				{ fragileOrderFilePath, returnOrderFilePath }))
				{
					return;
				}
				if (!fileName.startsWith(RETURN_FiLE_PREFIX))
				{
					returnError.setValue("Return File Name Should Starts With " + RETURN_FiLE_PREFIX + "");
					returnError.setVisible(true);
					return;
				}
			}
			catch (final InterruptedException e)
			{
				LOG.error("returnUploadFilePath not found" + e.getMessage());
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
					FileUtils.copyInputStreamToFile(media.getStreamData(), getFile(RETURN_FiLE_PREFIX, returnOrderFilePath));
					returnText.setText(fileName);
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
					FileUtils.writeStringToFile(getFile(RETURN_FiLE_PREFIX, returnOrderFilePath), media.getStringData());
					returnText.setText(fileName);
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
			returnError.setVisible(true);
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