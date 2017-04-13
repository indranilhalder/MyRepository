package com.hybris.oms.tata.tship.exceltocsv.service;

import java.io.File;
import java.io.IOException;

import com.hybris.oms.tata.tship.exceltocsv.pojo.LogisticPartner;
import com.hybris.oms.tata.tship.exceltocsv.pojo.PriorityMaster;



/**
 * @author techouts
 *
 */
public interface TshipExcelToCsvService
{
	public StringBuffer getData();

	public void setData(StringBuffer data);

	public String addCommasToBOF(String REPCOMMAVALUE);

	public String addCommasToEOF(String REPCOMMAVALUE);

	public void addLogisticPartnerData(String deliveryMode, LogisticPartner logisticPartner, final PriorityMaster priorityMaster)
			throws IOException;

	public void appendData(String deliveryMode, LogisticPartner logisticPartner, StringBuffer logisticdata,
			final PriorityMaster priorityMaster) throws IOException;

	public void appendFooterData(String deliveryMode, String fileNameTimeStamp);

	public void appendHeaderData(String deliveryMode);

	public void writeData(StringBuffer data, File outputFile, File validationFile) throws IOException;


}
