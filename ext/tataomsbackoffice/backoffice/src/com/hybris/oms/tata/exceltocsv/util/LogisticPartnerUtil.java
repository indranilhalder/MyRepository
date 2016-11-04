package com.hybris.oms.tata.exceltocsv.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




/**
 *
 * This class is used to get Logistic Partner names from properties file
 *
 */
public class LogisticPartnerUtil
{

	private static ArrayList<String> logisticPartnerNames = new ArrayList<String>();
	private static Map<String, String> logisticPartners = new HashMap<String, String>();

	static
	{
		logisticPartnerNames.add(ExcelConstants.BLUDART_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.GOJAVAS_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.FEDEX_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.GATI_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.SHADOW_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.NUVOEX_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.ECOMEXPRESS_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.DELHIVERY_OUTPUT_NAME);
		logisticPartnerNames.add(ExcelConstants.EKART_OUTPUT_NAME);

		logisticPartners.put(ExcelConstants.BLUEDART_HEADER_NAME, ExcelConstants.BLUDART_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.GOJAVAS_HEADER_NAME, ExcelConstants.GOJAVAS_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.FEDEX_HEADER_NAME, ExcelConstants.FEDEX_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.GATI_HEADER_NAME, ExcelConstants.GATI_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.SHADOW_HEADER_NAME, ExcelConstants.SHADOW_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.NUVOEX_HEADER_NAME, ExcelConstants.NUVOEX_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.ECOMEXPRESS_HEADER_NAME, ExcelConstants.ECOMEXPRESS_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.DELHIVERY_HEADER_NAME, ExcelConstants.DELHIVERY_OUTPUT_NAME);
		logisticPartners.put(ExcelConstants.EKART_HEADER_NAME, ExcelConstants.EKART_OUTPUT_NAME); //System.out.println("lp"+logisticPartnerNames);

	}

	/**
	 *
	 * @param sheet
	 * @param cellContent
	 * @param row
	 * @param writer
	 * @return
	 * @throws IOException
	 */
	public static String findLogisticPartnerName(final String logisticPartnerName)
	{
		//System.out.println("lp by name"+logisticPartners.get(logisticPartnerName));
		return logisticPartners.get(logisticPartnerName);
	}//

	public static boolean containsLogisticPartnerName(final String logisticPartnerName)
	{
		return logisticPartnerNames.contains(logisticPartnerName.toUpperCase());
	}
}