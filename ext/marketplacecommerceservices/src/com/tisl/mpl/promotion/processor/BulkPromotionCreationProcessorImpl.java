/**
 *
 */
package com.tisl.mpl.promotion.processor;

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
import com.tisl.mpl.marketplacecommerceservices.service.BulkPromotionCreationService;
import com.tisl.mpl.marketplacecommerceservices.service.BulkPromotionRestrictionService;


/**
 * @author TCS
 *
 */
public class BulkPromotionCreationProcessorImpl implements BulkPromotionCreationProcessor
{
	//private Integer errorPosition;
	private BulkPromotionCreationService bulkPromotionCreationService;
	private BulkPromotionRestrictionService bulkPromotionRestrictionService;



	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BulkPromotionCreationProcessorImpl.class.getName());

	/**
	 * @Descriptiion: Process uploaded .csv File to create Promotion/Restriction
	 * @param : input
	 * @param : output
	 * @param : flag
	 */
	@Override
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
			//@Description : To create Promotions in Bulk
			bulkPromotionCreationService.processUpdateForPromotions(reader, writer, map, errorPosition, headerRowIncluded);
		}
		else
		{
			//@Description : To create Promotion Restriction in Bulk
			bulkPromotionRestrictionService.processUpdateForPromoRestriction(reader, writer, map, errorPosition, headerRowIncluded);
		}

		writer.close();
	}

	/**
	 * @Descriptiion: Splits Data Based on Separator
	 * @param: output
	 */
	@Override
	public CSVWriter getCSVWriter(final OutputStream output) throws UnsupportedEncodingException
	{
		final CSVWriter csvWriter = new CSVWriter(output, "UTF-8");
		csvWriter.setFieldseparator(MarketplacecommerceservicesConstants.FIELD_SEPARATOR);
		return csvWriter;
	}


	/**
	 * @return the bulkPromotionCreationService
	 */
	public BulkPromotionCreationService getBulkPromotionCreationService()
	{
		return bulkPromotionCreationService;
	}

	/**
	 * @param bulkPromotionCreationService
	 *           the bulkPromotionCreationService to set
	 */
	public void setBulkPromotionCreationService(final BulkPromotionCreationService bulkPromotionCreationService)
	{
		this.bulkPromotionCreationService = bulkPromotionCreationService;
	}

	/**
	 * @return the bulkPromotionRestrictionService
	 */
	public BulkPromotionRestrictionService getBulkPromotionRestrictionService()
	{
		return bulkPromotionRestrictionService;
	}

	/**
	 * @param bulkPromotionRestrictionService
	 *           the bulkPromotionRestrictionService to set
	 */
	public void setBulkPromotionRestrictionService(final BulkPromotionRestrictionService bulkPromotionRestrictionService)
	{
		this.bulkPromotionRestrictionService = bulkPromotionRestrictionService;
	}

}
