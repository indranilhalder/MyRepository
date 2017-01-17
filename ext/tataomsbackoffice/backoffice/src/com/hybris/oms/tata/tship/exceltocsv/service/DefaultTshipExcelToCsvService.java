package com.hybris.oms.tata.tship.exceltocsv.service;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hybris.oms.tata.tship.exceltocsv.pojo.LogisticPartner;
import com.hybris.oms.tata.tship.exceltocsv.pojo.PriorityMaster;
import com.hybris.oms.tata.tship.exceltocsv.util.TshipExcelConstants;


/**
 *
 * This class is used to generate output file.
 *
 */
public class DefaultTshipExcelToCsvService implements TshipExcelToCsvService
{

	private StringBuffer headerData;
	private StringBuffer data;
	private StringBuffer data1;
	private StringBuffer data2;
	private StringBuffer data3;
	private StringBuffer data4;
	private StringBuffer data5;
	private StringBuffer data6;
	private StringBuffer data7;
	private StringBuffer data8;
	private StringBuffer data9;
	private StringBuffer data10;
	private StringBuffer data11;
	private StringBuffer data12;
	private StringBuffer data13;
	private StringBuffer data14;
	private StringBuffer data15;
	private StringBuffer data16;
	private StringBuffer data17;
	private StringBuffer footerData;


	public StringBuffer getData()
	{
		return data;
	}


	public void setData(final StringBuffer data)
	{
		this.data = data;
	}


	/**
	 *
	 * @param deliveryMode
	 * @param logisticPartner
	 * @param priorityMaster
	 * @throws IOException
	 */
	public void addLogisticPartnerData(final String deliveryMode, final LogisticPartner logisticPartner,
			final PriorityMaster priorityMaster) throws IOException
	{
		final int codcolumn = Integer.parseInt(TshipExcelConstants.COD_COLUMN_INDEX);
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		final String logisticName = logisticPartner.getName();
		if (logisticPartner.getIndex() >= codcolumn && logisticPartner.getIndex() < surfaceModeStartingIndex)
		{
			if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.BLUDART_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.GOJAVAS_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data1, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.FEDEX_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data2, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.GATI_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data3, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.SHADOW_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data4, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.NUVOEX_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data5, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.ECOMEXPRESS_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data6, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.DELHIVERY_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data7, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.EKART_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data8, priorityMaster);

			}


		} // this if condition is for getting air mode logistic Partners data.
		else
		{
			if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.BLUDART_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data9, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.GOJAVAS_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data10, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.FEDEX_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data11, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.GATI_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data12, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.SHADOW_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data13, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.NUVOEX_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data14, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.ECOMEXPRESS_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data15, priorityMaster);

			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.DELHIVERY_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data16, priorityMaster);
			}
			else if (logisticPartner.getName().equalsIgnoreCase(TshipExcelConstants.EKART_OUTPUT_NAME)
					&& !logisticPartner.getPrepaidPriority().equals(",") || !logisticPartner.getCodPriority().equals(","))
			{
				appendData(deliveryMode, logisticPartner, data17, priorityMaster);

			}
		} //this condition is for getting surface mode logistic partner data.
	}


	/**
	 * This method adds all logistic partner data to string buffer.
	 *
	 * @param deliveryMode
	 * @param logisticPartner
	 * @param data
	 * @param priorityMaster
	 * @throws IOException
	 */
	public void appendData(final String deliveryMode, final LogisticPartner logisticPartner, final StringBuffer logisticdata,
			final PriorityMaster priorityMaster) throws IOException
	{
		final int surfaceModeStartingIndex = Integer.parseInt(TshipExcelConstants.SURFACE_MODE_START_INDEX);
		logisticdata.append(deliveryMode + TshipExcelConstants.ADD_COMMA);
		logisticdata.append(priorityMaster.getPincode());
		if (logisticPartner.getIndex() < surfaceModeStartingIndex)
		{
			logisticdata.append(TshipExcelConstants.AIR_MODE + ",");
		}
		else
		{
			logisticdata.append(TshipExcelConstants.SURFACE_MODE + ",");
		}
		logisticdata.append(logisticPartner.getName());
		logisticdata.append(logisticPartner.getCod());
		logisticdata.append(logisticPartner.getPrepaidLimit());
		logisticdata.append(logisticPartner.getCodLimit());
		logisticdata.append(logisticPartner.getCarea());
		logisticdata.append(logisticPartner.getCscrcd());
		logisticdata.append(logisticPartner.getCloctype());
		logisticdata.append(logisticPartner.getNewzone());
		logisticdata.append(logisticPartner.getTatPrepaid());
		logisticdata.append(logisticPartner.getTatCode());
		logisticdata.append(logisticPartner.getFormRequired());
		//logisticdata.append(logisticPartner.getIsReturnPincode());
		logisticdata.append(logisticPartner.getPickUp());
		logisticdata.append(logisticPartner.getCodPriority());
		logisticdata.append(logisticPartner.getPrepaidPriority());

		//if(logisticPartner.getAdjCodLimit()!=null)
		logisticdata.append("" + ",");
		//else
		//logisticdata.append("0"+",");
		//if(logisticPartner.getAdjPrepaidLimit()!=null)
		logisticdata.append("" + ",");
		//else
		//logisticdata.append("0"+",");

		logisticdata.append(priorityMaster.getCity());
		logisticdata.append(priorityMaster.getState());
		logisticdata.append("IN");
		logisticdata.append("\r\n");

	}


	/**
	 *
	 * @param data
	 * @param outputFile
	 * @param validationFile
	 * @throws IOException
	 */
	public void writeData(final StringBuffer data, final File outputFile, final File validationFile) throws IOException
	{
		if (validationFile.length() == 0)
		{
			final FileWriter bwr = new FileWriter(outputFile);
			bwr.write(headerData.toString().concat(data.toString()
					.concat(data1.toString().concat(data2.toString().concat(data3.toString().concat(data4.toString()
							.concat(data5.toString().concat(data6.toString().concat(data7.toString().concat(data8.toString()
									.concat(data9.toString().concat(data10.toString()).concat(data11.toString()).concat(data12.toString()
											.concat(data13.toString().concat(data14.toString().concat(data15.toString().concat(
													data16.toString().concat(data17.toString().concat(footerData.toString()))))))))))))))))));
			bwr.flush();
			bwr.close();
		}
	}

	/**
	 * THis method appends header columns to the output file.
	 *
	 * @param deliveryMode
	 */
	public void appendHeaderData(final String deliveryMode)
	{
		headerData = new StringBuffer();
		data = new StringBuffer();
		data1 = new StringBuffer();
		data2 = new StringBuffer();
		data3 = new StringBuffer();
		data4 = new StringBuffer();
		data5 = new StringBuffer();
		data6 = new StringBuffer();
		data7 = new StringBuffer();
		data8 = new StringBuffer();
		data9 = new StringBuffer();
		data10 = new StringBuffer();
		data11 = new StringBuffer();
		data12 = new StringBuffer();
		data13 = new StringBuffer();
		data14 = new StringBuffer();
		data15 = new StringBuffer();
		data16 = new StringBuffer();
		data17 = new StringBuffer();
		final String headerColumns = TshipExcelConstants.HEADER_COLUMNS + TshipExcelConstants.NEW_LINE;
		headerData.append(headerColumns);
		String result = null;
		if (deliveryMode.equalsIgnoreCase(TshipExcelConstants.DATA_MODE_TYPE1))
		{
			final String STATICDATAHD = TshipExcelConstants.STATICDATA_HD;
			headerData.append(STATICDATAHD);
			result = addCommasToBOF(TshipExcelConstants.ADD_COMMA);
			headerData.append(result);
			headerData.append(TshipExcelConstants.COUNTRY);
			headerData.append(TshipExcelConstants.NEW_LINE);
		}
		else if (deliveryMode.equalsIgnoreCase(TshipExcelConstants.DATA_MODE_TYPE2))
		{
			final String STATICDATAED = TshipExcelConstants.STATICDATA_ED;
			headerData.append(STATICDATAED);
			result = addCommasToBOF(TshipExcelConstants.ADD_COMMA);
			headerData.append(result);
			headerData.append(TshipExcelConstants.COUNTRY);
			headerData.append(TshipExcelConstants.NEW_LINE);
		}

	}

	/**
	 * This method appends EOF and fileNameTimeStamp and followed by commas at the foorter to the output file.
	 *
	 * @param deliveryMode
	 * @param fileNameTimeStamp
	 */
	public void appendFooterData(final String deliveryMode, final String fileNameTimeStamp)
	{
		footerData = new StringBuffer();
		String result = null;
		footerData
				.append(deliveryMode + TshipExcelConstants.ADD_COMMA + "EOF," + fileNameTimeStamp + TshipExcelConstants.ADD_COMMA);
		result = addCommasToEOF(TshipExcelConstants.ADD_COMMA);
		footerData.append(result);
		footerData.append("IN");
	}

	/**
	 *
	 * @param REPCOMMAVALUE
	 * @return
	 */
	public String addCommasToBOF(final String REPCOMMAVALUE)
	{
		final String commasCountOfBOF = TshipExcelConstants.COMMAS_COUNTOF_BOF;
		final int commasCount = Integer.parseInt(commasCountOfBOF);
		final StringBuffer staticDataHd = new StringBuffer();
		for (int i = 0; i < commasCount; i++)
		{
			staticDataHd.append(TshipExcelConstants.ADD_COMMA);
		}
		return staticDataHd.toString();
	}

	/**
	 *
	 * @param REPCOMMAVALUE
	 * @return
	 */
	public String addCommasToEOF(final String REPCOMMAVALUE)
	{
		final String commasCountOfEOF = TshipExcelConstants.COMMAS_COUNTOF_EOF;
		final int commasCount = Integer.parseInt(commasCountOfEOF);
		final StringBuffer staticDataEd = new StringBuffer();
		for (int i = 0; i < commasCount; i++)
		{
			staticDataEd.append(TshipExcelConstants.ADD_COMMA);
		}
		return staticDataEd.toString();
	}


}