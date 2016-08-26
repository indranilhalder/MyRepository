/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import com.csvreader.CsvReader;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.services.FilePathProviderService;
import com.hybris.oms.tata.util.DataUploadService;
import com.techouts.dataonboarding.util.BUCFileComparitor;


/**
 * @author prabhakar
 *
 */
public class TshipDeliveryTatUploadWidgetController extends DefaultWidgetController
{
	private static final long serialVersionUID = 1L;
	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;
	@WireVariable
	private DataUploadService dataUploadService;
	@WireVariable
	private BUCFileComparitor bucFileComparitor;
	private String returnOrderFilePath = "";
	private String fragileOrderFilePath = "";
	private String fileHeaderProperty = "";
	private static final String CSV = ".csv";
	private static final String lineSplitBy = ",";
	private static final String forwardFilePrefix = "PFSDLogisticsServicebility_";
	private Set<String> fileHeaders;
	private static final Logger LOG = LoggerFactory.getLogger(TshipDeliveryTatUploadWidgetController.class);

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		returnOrderFilePath = filePathProviderService.getReturnOrderFileUploadPath();
		fragileOrderFilePath = filePathProviderService.getFragileOrderFileUploadPath();
		fileHeaderProperty = filePathProviderService.getFileHeaderProperty();
		fileHeaders = getFileHeaders();
	}

	/**
	 * this method will call while uploading file
	 */
	@ViewEvent(componentID = "returnOrderupload", eventName = Events.ON_UPLOAD)
	public void returnOrderUploadEvent(final UploadEvent event)
	{
		if ("null".equals(returnOrderFilePath) || "".equals(returnOrderFilePath) || returnOrderFilePath == null)
		{
			Messagebox.show("Unable to find return order  file path config inside PropertyFile", "Error", Messagebox.OK,
					Messagebox.ERROR);
			return;
		}
		if (event.getMedia().getName().startsWith(forwardFilePrefix))
		{
			final File saveNewFile = getFile(forwardFilePrefix, returnOrderFilePath);

			try
			{
				final String message = dataUploadService.dataUpload(event.getMedia(), saveNewFile);
				bucFileComparitor.process(returnOrderFilePath, saveNewFile.getName(), fileHeaders,
						getPreviousFileName(returnOrderFilePath, saveNewFile.getName()), 2);

				Messagebox.show(message);
			}
			catch (final Exception e)
			{
				Messagebox.show("Unable to upload file due to network slow try again");
			}
		}
		else
		{
			Messagebox.show("File name should strats with PFSDLogisticsServicebility_", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
	}

	@ViewEvent(componentID = "frigileOrderupload", eventName = Events.ON_UPLOAD)
	public void frigileOrderUploadEvent(final UploadEvent event)
	{

		if ("null".equals(fragileOrderFilePath) || "".equals(fragileOrderFilePath) || fragileOrderFilePath == null)
		{
			Messagebox.show("Unable to find frigile order  file path config inside PropertyFile", "Error", Messagebox.OK,
					Messagebox.ERROR);
			return;
		}
		if (event.getMedia().getName().startsWith(forwardFilePrefix))
		{
			final File saveNewFile = getFile(forwardFilePrefix, fragileOrderFilePath);

			try
			{
				final String message = dataUploadService.dataUpload(event.getMedia(), saveNewFile);
				bucFileComparitor.process(fragileOrderFilePath, saveNewFile.getName(), fileHeaders,
						getPreviousFileName(fragileOrderFilePath, saveNewFile.getName()), 2);

				Messagebox.show(message);
			}
			catch (final Exception e)
			{
				Messagebox.show("Unable to upload file due to network slow try again");
			}
		}
		else
		{
			Messagebox.show("File name should strats with PFSDLogisticsServicebility_", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}

	}

	/**
	 * this method is used to get the previous file name
	 *
	 * @param parentFolder
	 * @param csvfilename
	 * @return previous file name
	 */

	private String getPreviousFileName(final String parentFolder, final String csvfilename)
	{
		final File csvFile = new File(parentFolder, csvfilename);
		FileReader fileReader;
		String filename = null;
		try
		{
			fileReader = new FileReader(csvFile);
			final CsvReader tmpread = new CsvReader(fileReader);
			tmpread.readHeaders();
			tmpread.readRecord();
			final String logisticsid = tmpread.get("logisticsid");
			final String deliverymode = tmpread.get("deliverymode");
			final String transportmode = tmpread.get("transportmode");
			// creating filename to find previous records
			filename = logisticsid.concat("_").concat(deliverymode).concat("_").concat(transportmode).concat(".CSV");
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return filename;
	}


	/**
	 * this method is used to send list of FileHeaders
	 *
	 * @return set of fileHeaders
	 */
	private Set<String> getFileHeaders()
	{
		if ("null".equals(fileHeaderProperty) || "".equals(fileHeaderProperty) || fileHeaderProperty == null)
		{
			Messagebox.show("Unable to find heders configaration inside PropertyFile", "Error", Messagebox.OK, Messagebox.ERROR);
		}
		else
		{
			fileHeaders = new HashSet<String>();

			final String[] headers = fileHeaderProperty.split(lineSplitBy);
			for (final String header : headers)
			{
				fileHeaders.add(header);
			}
		}
		return fileHeaders;
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