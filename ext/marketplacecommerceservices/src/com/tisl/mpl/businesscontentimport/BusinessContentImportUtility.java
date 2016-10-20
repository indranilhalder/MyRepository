/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class BusinessContentImportUtility
{
	private BusinessContentImportService businessContentImportService;




	/**
	 * @return the businessContentImportService
	 */
	public BusinessContentImportService getBusinessContentImportService()
	{
		return businessContentImportService;
	}

	/**
	 * @param businessContentImportService
	 *           the businessContentImportService to set
	 */
	public void setBusinessContentImportService(final BusinessContentImportService businessContentImportService)
	{
		this.businessContentImportService = businessContentImportService;
	}

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BusinessContentImportUtility.class.getName());

	/**
	 * @Descriptiion: Process uploaded .csv File to create Content
	 * @param : input
	 * @param : output
	 * @param : flag
	 */

	public void processFile(final InputStream input, final OutputStream output, final boolean flag) throws IOException
	{
		final CSVWriter writer = getCSVWriter(output);
		final CSVReader reader = new CSVReader(input, "UTF-8");
		reader.setFieldSeparator(new char[]
		{ MarketplacecommerceservicesConstants.FIELD_SEPARATOR });
		final Map<Integer, String> map = new HashMap<Integer, String>();
		final boolean headerRowIncluded = false;
		final Integer errorPosition = Integer.valueOf(0);

		if (flag)
		{
			//@Description : To create Contents in Bulk
			businessContentImportService.processUpdateForContentImport(reader, writer, map, errorPosition, headerRowIncluded);
		}
		else
		{
			//To-Do
			//Create Product Upload
			businessContentImportService.processUpdateForProductMappingImport(reader, writer, map, errorPosition, headerRowIncluded);
		}

		writer.close();
	}

	/**
	 * @Descriptiion: Splits Data Based on Separator
	 * @param: output
	 */

	public CSVWriter getCSVWriter(final OutputStream output) throws UnsupportedEncodingException
	{
		final CSVWriter csvWriter = new CSVWriter(output, "UTF-8");
		csvWriter.setFieldseparator(MarketplacecommerceservicesConstants.FIELD_SEPARATOR);
		return csvWriter;
	}
}
