package com.hybris.oms.tata.tship.exceltocsv.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




/**
 *
 * This class is used to get Logistic Partner names from properties file
 *
 */
public class TshipLogisticPartnerUtil
{

	private static ArrayList<String> logisticPartnerNames = new ArrayList<String>();
	private static Map<String, String> logisticPartners = new HashMap<String, String>();

	static
	{
		logisticPartnerNames.add(TshipExcelConstants.BLUDART_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.GOJAVAS_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.FEDEX_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.GATI_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.SHADOW_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.NUVOEX_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.ECOMEXPRESS_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.DELHIVERY_OUTPUT_NAME);
		logisticPartnerNames.add(TshipExcelConstants.EKART_OUTPUT_NAME);

		logisticPartners.put(TshipExcelConstants.BLUEDART_HEADER_NAME, TshipExcelConstants.BLUDART_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.GOJAVAS_HEADER_NAME, TshipExcelConstants.GOJAVAS_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.FEDEX_HEADER_NAME, TshipExcelConstants.FEDEX_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.GATI_HEADER_NAME, TshipExcelConstants.GATI_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.SHADOW_HEADER_NAME, TshipExcelConstants.SHADOW_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.NUVOEX_HEADER_NAME, TshipExcelConstants.NUVOEX_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.ECOMEXPRESS_HEADER_NAME, TshipExcelConstants.ECOMEXPRESS_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.DELHIVERY_HEADER_NAME, TshipExcelConstants.DELHIVERY_OUTPUT_NAME);
		logisticPartners.put(TshipExcelConstants.EKART_HEADER_NAME, TshipExcelConstants.EKART_OUTPUT_NAME); //System.out.println("lp"+logisticPartnerNames);

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