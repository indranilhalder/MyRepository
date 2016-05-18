package com.hybris.oms.tata.pincodesupload;

/**
 *
 * This class is for uploading pincodes by using excel sheet or csv
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
import org.zkoss.zk.ui.WrongValueException;
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
 * @author irfan
 */

@SuppressWarnings("serial")
public class PincodesUploadController extends DefaultWidgetController
{

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	private Label error;
	private Textbox text;
	private Media media;
	private String pinCodeuploadFilePath = "";
	private static final String CSV = ".csv";
	final static String[] LIST_OF_ERROR =
	{ "pinCodeupload FilePath" };

	private static final Logger LOG = LoggerFactory.getLogger(PincodesUploadController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		text.setText("");
		pinCodeuploadFilePath = filePathProviderService.getPincodesUploadPath();
	}

	@ViewEvent(componentID = "upload_button", eventName = Events.ON_UPLOAD)
	public void selectUploadPincodes(final UploadEvent uploadEvent) throws InterruptedException
	{
		text.setText("");
		error.setVisible(false);
		media = uploadEvent.getMedia();
		final String fileName = media.getName();

		if (fileName.endsWith(CSV) || fileName.endsWith(".xlsx"))
		{
			if (fileName.endsWith(CSV))
			{
				LOG.info("its csv File..........");

				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, pinCodeuploadFilePath))
				{
					return;
				}

				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				else
				{
					LOG.info(media.getStringData().toString());
					text.setText(fileName);
					try
					{
						if (media != null)
						{
							final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
							final String TimeStamp = simpleDateFormat.format(new Date());
							final String fileNameTimeStamp = "".concat("pincode-".concat(TimeStamp).concat(CSV));
							final File dest = new File(pinCodeuploadFilePath.trim(), fileNameTimeStamp);
							LOG.info("Now Media name is " + dest.getName());
							final String s = media.getStringData();
							FileUtils.writeStringToFile(dest, s);
							Messagebox.show("csv File uploaded successfully");
						}
					}
					catch (final Exception e1)
					{
						LOG.error("The media is null......" + e1.getMessage());
					}
				}
			}
			else
			{
				LOG.info("its  Xlsx File..........");

				if (!filePathProviderService.propertyFilePathValidation(LIST_OF_ERROR, pinCodeuploadFilePath))
				{
					return;
				}
				try
				{
					if (media.getStreamData().read() == -1)
					{
						LOG.info("File is Empty ...");
						Messagebox.show("File is Empty ");
						return;
					}
					else
					{
						LOG.info("Data.. " + media.getStreamData());
						text.setText(fileName);
						try
						{
							if (media != null)
							{
								final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
								final String TimeStamp = simpleDateFormat.format(new Date());
								final String fileNameTimeStamp = "".concat("pincode-".concat(TimeStamp).concat(CSV));
								final File newdest = new File(pinCodeuploadFilePath.trim(), fileNameTimeStamp);
								LOG.info("Now Media name is " + newdest.getName());
								FileUtils.copyInputStreamToFile(media.getStreamData(), newdest);
								Messagebox.show("xls File uploaded successfully ");
							}
						}
						catch (final Exception e1)
						{
							LOG.error("The media is null......" + e1.getMessage());
						}
					}
				}
				catch (WrongValueException | IOException e)
				{
					LOG.error("The media is null......" + e.getMessage());
				}
			}
		}
		else
		{
			error.setVisible(true);
		}
	}
}
