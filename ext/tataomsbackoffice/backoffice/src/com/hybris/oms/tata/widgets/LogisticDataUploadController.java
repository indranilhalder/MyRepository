/**
 *@author Saood
 *
 *This file is       for  converting the Excel to CSV in required format
 *
 */
package com.hybris.oms.tata.widgets;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.io.Files;
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
import com.hybris.oms.api.logisticserviceability.LogisticServiceabilityFacade;
import com.hybris.oms.buc.bulkupload.LogisticsServBulkUpldErrFacade;
import com.hybris.oms.tata.exceltocsv.main.ExcelToCsvConverter;
import com.hybris.oms.tata.services.CsvFileToZipFileService;
import com.hybris.oms.tata.services.FilePathProviderService;


/**
 * This class is for uploading a bulk data of logistics by using excel sheet
 *
 *
 */
@SuppressWarnings("serial")
public class LogisticDataUploadController extends DefaultWidgetController
{
	private Label homeEmptyFileError;
	private Label expressEmptyFileError;

	private static final String HD_FILE_NAME = "LogisticsServiceabilityData_HD";
	private static final String ED_FILE_NAME = "LogisticsServiceabilityData_ED";
	private static final String COM_FILE_NAME = "LogisticsServiceabilityData_";
	private static final String MIL_SEC = "  milliseconds";

	private String tmpFilePath = "";
	private String validationErrorPath = "";
	private String tplCSVInboundPath = "";
	private String tplCSVOutboundPath = "";
	private static final String CSV = ".csv";

	private static final String[] PROPERTY_FILE_ERRORS =
	{ "Temporary file path", "Validation_Error file path", "Tpl Inbound file path", "Tpl Outbound file path" };
	private static final Logger LOG = LoggerFactory.getLogger(LogisticDataUploadController.class.getName());

	@Autowired
	private LogisticServiceabilityFacade logisticServiceabilityFacade;

	@WireVariable("logisticsServBulkUpldErrRestClient")
	private LogisticsServBulkUpldErrFacade logisticsServBulkUpldErrFacade;

	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	@WireVariable("csvFileToZipFileService")
	private CsvFileToZipFileService csvFileToZipFileService;
	@WireVariable("excelToCsvConverter")
	private ExcelToCsvConverter excelToCsvConverter;



	public void show()
	{
		LOG.info("I'm from LogisticDataUploadController");
	}




	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		homeEmptyFileError.setValue("");
		expressEmptyFileError.setValue("");
		if (filePathProviderService != null)
		{
			tmpFilePath = filePathProviderService.getPinMstrTmpPath();
			validationErrorPath = filePathProviderService.getValidationErrFilePath();
			tplCSVInboundPath = filePathProviderService.getTplInbndCsvPath();
			tplCSVOutboundPath = filePathProviderService.getTplOutbndCsvPath();
		}
		else
		{
			LOG.error("filePathProviderService is con't be null");

		}

	}

	private Textbox homeExcelFileName;

	private Textbox expressExcelFileName;

	private Media media;

	@ViewEvent(componentID = "home_uploadzip", eventName = Events.ON_UPLOAD)
	public void selectHomeUploadZip(final UploadEvent uploadEvent) throws InterruptedException
	{

		if (!filePathProviderService.propertyFilePathValidation(PROPERTY_FILE_ERRORS, tmpFilePath, validationErrorPath,
				tplCSVInboundPath, tplCSVOutboundPath))
		{
			return;
		}
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		//	final String fileName1 = fileName.substring(0, fileName.length() - 5);
		final String fileName1 = "TempLogServFile_HD" + CSV;
		LOG.info("**********Filename1 is**********" + fileName1);

		// this is the temp path for generating xlsx file
		final File dest = new File(tmpFilePath.trim(), fileName);

		expressExcelFileName.setText("");
		expressEmptyFileError.setValue("");
		homeExcelFileName.setText("");
		if (fileName.endsWith(".xlsx") && fileName.contains(COM_FILE_NAME))
		{
			homeExcelFileName.setText(fileName);
			homeEmptyFileError.setValue("");
			final String fileNameTimeStamp = fileName.concat("/".concat(new Date().toString()));
			try
			{
				if (media != null)
				{
					LOG.info("The Media name is " + media.getName());
				}

			}
			catch (final Exception e1)
			{
				LOG.info("The media is null" + e1.getMessage());

			}

			//final File inputFile = new File(dest);
			final File tmpTplCSVInboundFile = new File(tplCSVInboundPath.trim(), fileName1);
			final File tplCSVInboundFile = new File(tplCSVInboundPath.trim(), HD_FILE_NAME + CSV);
			//	final File tmptplCSVOutboundFile = new File(tplCSVOutboundPath.trim(), fileName1);
			final File tplCSVOutboundFile = new File(tplCSVOutboundPath.trim(), HD_FILE_NAME + CSV);

			final File validationErrorFile = new File(validationErrorPath.trim(), "ValidationError.csv");

			try
			{
				LOG.info("Start Copying of HD file from another system to Commerce Server");
				final long copyStartTime = System.nanoTime();
				Files.copy(dest, media.getStreamData());
				LOG.info("Total Time taken HD file for copying from another system To commerce   "
						+ (System.nanoTime() - copyStartTime) / 1e6 + MIL_SEC);
				LOG.info("End of Copying of HD file from another system to Commerce Server");
			}
			catch (final IOException ioexception)
			{

				LOG.info("**********IOException************" + ioexception.toString());
			}

			try
			{

				excelToCsvConverter.convertExcelToCsv("HD", dest, tmpTplCSVInboundFile, validationErrorFile, fileNameTimeStamp);

				LOG.info("End Of Converting the file from xlsx file to required CSV format in TempLogServFile");
				if (validationErrorFile.length() == 0)
				{

					final String csvData = csvFileToZipFileService.convertCSVFileToString(tmpTplCSVInboundFile);

					LOG.info("************CALLING DELETE WEBSERVICES**************" + csvData);
					logisticServiceabilityFacade.deleteOlderRecords(csvData);
					LOG.info("************AFTER CALLING DELETE WEBSERVICES**************" + csvData);

					LOG.info("StartCopying TempLogServFile as LogisticsServiceabilityData_HD_datetime format in outbound");
					FileUtils.copyFile(tmpTplCSVInboundFile, tplCSVOutboundFile);
					LOG.info("End Copying TempLogServFile as LogisticsServiceabilityData_HD_datetime format in outbound Folder");

					LOG.info("Start renaming TempLogServFile as LogisticsServiceabilityData_HD_datetime format to inbound folder");
					tmpTplCSVInboundFile.renameTo(tplCSVInboundFile);
					LOG.info("End of renaming TempLogServFile as LogisticsServiceabilityData_HD_datetime format to inbound folder ");
					Messagebox.show("File uploaded successfully");

				}
				if (validationErrorFile.length() > 0)
				{
					try
					{
						csvFileToZipFileService.convert(validationErrorFile);
						logisticsServBulkUpldErrFacade.sendBulkUploadErrorEmail();

					}
					catch (final Exception excp)
					{
						LOG.info("An Error occured while sending error as an email attachment");
					}
					homeEmptyFileError.setValue("Error while uploading File and the error is send as an attachment to the admin");
				}

			}
			catch (final Exception exp)
			{
				LOG.error(exp.getMessage());
				homeEmptyFileError.setValue("Error while uploading File");
			}
		}
		else
		{
			homeEmptyFileError.setValue("Not an LogisticsServiceabilityData_ DateTime format file");
		}

	}


	@ViewEvent(componentID = "express_uploadzip", eventName = Events.ON_UPLOAD)
	public void selectExpressUploadZip(final UploadEvent uploadEvent) throws InterruptedException
	{

		if (!filePathProviderService.propertyFilePathValidation(PROPERTY_FILE_ERRORS, tmpFilePath, validationErrorPath,
				tplCSVInboundPath, tplCSVOutboundPath))
		{
			return;
		}
		media = uploadEvent.getMedia();
		final String fileName = media.getName();
		final String fileName1 = "TempLogServFile_ED" + CSV;
		LOG.info("**********Filename1 is**********" + fileName1);

		// this is the temp path for generating xlsx file
		final File dest = new File(tmpFilePath.trim(), fileName);

		homeExcelFileName.setText("");
		homeEmptyFileError.setValue("");
		expressExcelFileName.setText("");
		if (fileName.endsWith(".xlsx") && fileName.contains(COM_FILE_NAME))
		{
			expressExcelFileName.setText(fileName);
			expressEmptyFileError.setValue("");
			final String fileNameTimeStamp = fileName.concat("/".concat(new Date().toString()));
			try
			{
				if (media != null)
				{
					LOG.info("The Media name is " + media.getName());
				}

			}
			catch (final Exception e1)
			{
				LOG.info("the media is null" + e1.getMessage());

			}

			//final File inputFile = new File(dest);
			final File tmpTplCSVInboundFile = new File(tplCSVInboundPath.trim(), fileName1);
			final File tplCSVInboundFile = new File(tplCSVInboundPath.trim(), ED_FILE_NAME + CSV);
			final File tplCSVOutboundFile = new File(tplCSVOutboundPath.trim(), ED_FILE_NAME + CSV);
			final File validationErrorFile = new File(validationErrorPath.trim(), "ValidationError.csv");

			try
			{
				LOG.info("Start Copying of ED file from another system to Commerce Server");
				final long copyStartTime = System.nanoTime();
				Files.copy(dest, media.getStreamData());
				LOG.info("Total Time taken ED file for copying from host system To commerce  "
						+ (System.nanoTime() - copyStartTime) / 1e6 + MIL_SEC);
				LOG.info("End of Copying of ED file from another system to Commerce Server");
			}
			catch (final IOException ioexception)
			{

				LOG.info("**********IOException************" + ioexception.toString());
			}
			try
			{
				excelToCsvConverter.convertExcelToCsv("ED", dest, tmpTplCSVInboundFile, validationErrorFile, fileNameTimeStamp);

				LOG.info("End Of Converting the file from xlsx file to required CSV format in TempLogServFile");

				if (validationErrorFile.length() == 0)
				{

					final String csvData = csvFileToZipFileService.convertCSVFileToString(tmpTplCSVInboundFile);

					LOG.info("************CALLING DELETE WEBSERVICES**************" + csvData);
					logisticServiceabilityFacade.deleteOlderRecords(csvData);
					LOG.info("************AFTER CALLING DELETE WEBSERVICES**************" + csvData);


					final long starttime = System.nanoTime();
					LOG.info("StartCopying TempLogServFile as LogisticsServiceabilityData_ED_datetime format in outbound");
					FileUtils.copyFile(tmpTplCSVInboundFile, tplCSVOutboundFile);
					final long endtime = System.nanoTime();
					final double difference = (endtime - starttime) / 1e6;
					LOG.info("End Copying TempLogServFile as LogisticsServiceabilityData_ED_datetime format in outbound Folder in "
							+ difference + MIL_SEC);


					final long startRenTShipTime = System.nanoTime();
					LOG.info("Start renaming TempLogServFile as LogisticsServiceabilityData_ED_datetime format");
					tmpTplCSVInboundFile.renameTo(tplCSVInboundFile);
					final long endRenTime = System.nanoTime();
					final double difference1 = (endRenTime - startRenTShipTime) / 1e6;
					LOG.info("End of renaming TempLogServFile as LogisticsServiceabilityData_ED_datetime format" + difference1
							+ "milliseconds");
					Messagebox.show("File uploaded successfully");

				}
				if (validationErrorFile.length() > 0)
				{
					try
					{
						csvFileToZipFileService.convert(validationErrorFile);
						logisticsServBulkUpldErrFacade.sendBulkUploadErrorEmail();

					}
					catch (final Exception excp)
					{
						LOG.info("An Error occured while sending error as an email attachment");
					}
					expressEmptyFileError.setValue("Error while uploading File and the error is send as an attachment to the admin");
				}

			}
			catch (final Exception exp)
			{
				LOG.error(exp.getMessage());
				expressEmptyFileError.setValue("Error while uploading File");
			}
		}
		else
		{
			expressEmptyFileError.setValue("Not an LogisticsServiceabilityData_ DateTime format file");
		}

	}

}
