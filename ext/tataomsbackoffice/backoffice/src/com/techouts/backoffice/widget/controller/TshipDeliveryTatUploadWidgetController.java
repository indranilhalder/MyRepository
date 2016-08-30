/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
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
	private String forwardFileHeaderProperty = "";
	private String returnFileHeaderProperty = "";
	private static final String CSV = ".csv";
	private static final String lineSplitBy = ",";
	private static final String FORWARD_FiLE_PREFIX = "PFSDLogisticsServicebility_";
	private static final String RETURN_FiLE_PREFIX = "ReturnLogisticsServiceability_";
	private Set<String> forwardFileHeaders;
	private Set<String> returnFileHeaders;
	private static final Logger LOG = LoggerFactory.getLogger(TshipDeliveryTatUploadWidgetController.class);
	private static final String FILE_SEP = File.separator;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		returnOrderFilePath = filePathProviderService.getReturnOrderFileUploadPath();
		fragileOrderFilePath = filePathProviderService.getFragileOrderFileUploadPath();
		forwardFileHeaderProperty = filePathProviderService.getForwardFileHeaderProperty();
		forwardFileHeaders = getFileHeaders(forwardFileHeaderProperty);
		returnFileHeaderProperty = filePathProviderService.getReturnFileHeaderProperty();
		returnFileHeaders = getFileHeaders(returnFileHeaderProperty);
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
		if (event.getMedia().getName().startsWith(RETURN_FiLE_PREFIX))
		{
			final File saveNewFile = getFile(RETURN_FiLE_PREFIX, returnOrderFilePath);



			try
			{
				final String message = dataUploadService.dataUpload(event.getMedia(), saveNewFile);
				final File newFile = generateReturnLogistics(saveNewFile);
				bucFileComparitor.process(newFile.getParent(), newFile.getName(), returnFileHeaders, "ReturnLogisticsService.csv", 2);

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
		if (event.getMedia().getName().startsWith(FORWARD_FiLE_PREFIX))
		{
			final File saveNewFile = getFile(FORWARD_FiLE_PREFIX, fragileOrderFilePath);

			try
			{
				final String message = dataUploadService.dataUpload(event.getMedia(), saveNewFile);
				bucFileComparitor.process(fragileOrderFilePath, saveNewFile.getName(), forwardFileHeaders,
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
	private Set<String> getFileHeaders(final String fileHeaderProperty)
	{
		final Set<String> fileHeaders = new HashSet<String>();

		if ("null".equals(fileHeaderProperty) || "".equals(fileHeaderProperty) || fileHeaderProperty == null)
		{
			Messagebox.show("Unable to find " + fileHeaderProperty + "configaration inside local.properties File", "Error",
					Messagebox.OK, Messagebox.ERROR);
		}
		else
		{


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
		final String fileNameTimeStamp = prefix.concat(CSV);
		final File destFile = new File(filePath.trim(), fileNameTimeStamp);
		LOG.info("Now Media name is " + destFile.getName());
		return destFile;
	}

	private File generateReturnLogistics(final File file)
	{

		final File newFile = new File(file.getParent() + FILE_SEP + file.getName().replace(".csv", "_G_.csv"));
		BufferedReader bufferedReader = null;
		try
		{
			final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile));
			final PrintWriter printWriter = new PrintWriter(bufferedWriter);


			final ArrayList<String> fileData = new ArrayList<String>();
			bufferedReader = new BufferedReader(new FileReader(file));

			final String hedders = bufferedReader.readLine();

			final String[] logistics = stringToArray(hedders, ",");

			fileData.add(hedders.substring(0, getIndexOfOccurrence(hedders, 6, ',')) + ',' + "logisticsPriority");

			String record = bufferedReader.readLine();


			while (StringUtils.isNotBlank(record))
			{

				// To find sixth occurrence of ','(comma) and add the logisticsPriority data
				record = record.substring(0, getIndexOfOccurrence(record, 6, ',')) + ',' + getLogisticsPriority(logistics, record);
				fileData.add(record);
				record = bufferedReader.readLine();
			}
			for (final String newLine : fileData)
			{
				if (StringUtils.isNotBlank(newLine.trim()))
				{
					printWriter.println(newLine);
				}
			}
			printWriter.close();
			file.delete();
		}
		catch (final IOException e)
		{

			LOG.error(e.getMessage(), e);
		}
		finally
		{

			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage(), e);
				}
			}

		}
		return newFile;


	}

	private String getLogisticsPriority(final String[] logistics, final String record)
	{

		final String[] stringTokens = stringToArray(record, ",");
		final int tocknLength = stringTokens.length;
		String st = "";
		for (int i = 6; i < tocknLength; i++)
		{
			st = st + logistics[i] + ':' + stringTokens[i] + '#';
		}
		return st;

	}

	private int getIndexOfOccurrence(final String record, final int occurenceNo, final char delimiter)
	{
		int j = 0;
		for (int i = 0; i < occurenceNo; i++)
		{
			j = record.indexOf(delimiter, j + 1);
			if (j == -1)
			{
				break;
			}
		}
		return j;
	}

	private String[] stringToArray(final String record, final String delimiter)
	{
		final StringTokenizer st = new StringTokenizer(record, delimiter);
		final String[] strings = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++)
		{
			strings[i] = st.nextToken();
		}
		return strings;
	}
}