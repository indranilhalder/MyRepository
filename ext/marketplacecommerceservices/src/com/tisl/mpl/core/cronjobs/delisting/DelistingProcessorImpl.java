package com.tisl.mpl.core.cronjobs.delisting;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class DelistingProcessorImpl implements DelistingProcessor
{

	@Autowired
	private MplDelistingService mplDelistingService;

	@Autowired
	private DelistingErrorHandling delistErrorHandling;



	/**
	 * @return the mplDelistingService
	 */
	public MplDelistingService getMplDelistingService()
	{
		return mplDelistingService;
	}

	/**
	 * @param mplDelistingService
	 *           the mplDelistingService to set
	 */
	public void setMplDelistingService(final MplDelistingService mplDelistingService)
	{
		this.mplDelistingService = mplDelistingService;
	}

	/**
	 * @return the delistErrorHandling
	 */
	public DelistingErrorHandling getDelistErrorHandling()
	{
		return delistErrorHandling;
	}

	/**
	 * @param delistErrorHandling
	 *           the delistErrorHandling to set
	 */
	public void setDelistErrorHandling(final DelistingErrorHandling delistErrorHandling)
	{
		this.delistErrorHandling = delistErrorHandling;
	}

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DelistingProcessorImpl.class.getName());

	/**
	 * @Descriptiion: Process uploaded .csv File to create Promotion/Restriction
	 * @param : input
	 * @param : output
	 * @param : flag
	 */
	//@Description: Variable Declaration for Promotions
	private static final int SELLERID = 0;
	private static final int DELISTING = 1;
	private static final int DELIST = 2;
	private static final int BLOCKOMS = 2;


	/**
	 * @Description: For Creating Promotion in bulk
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */

	public void process(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded, final boolean flag)
	{
		LOG.debug("Checking For Errors");


		while (reader.readNextLine())
		{
			final Map<Integer, String> line = reader.getLine();

			boolean errorline = false;

			final String sellerId = line.get(Integer.valueOf(SELLERID));

			final String delisting = line.get(Integer.valueOf(DELISTING));

			if (sellerId.isEmpty())
			{
				LOG.error("Seller ID is Empty for Line " + reader.getCurrentLineNumber());
				errorline = true;

			}
			else if (delisting.isEmpty())
			{
				if (!delisting.equalsIgnoreCase("Y") && !delisting.equalsIgnoreCase("N"))
				{
					LOG.error("Delisting Data not in Sync for line number" + reader.getCurrentLineNumber());
					errorline = true;
				}
			}

			if (!errorline)
			{

				if (flag)
				{
					processDatatoModelSeller(line, writer);

				}
				else
				{
					processDatatoModelUssid(line, writer);

				}

			}

		}


	}

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
		process(reader, writer, map, errorPosition, headerRowIncluded, flag);
		reader.close();
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

	private void processDatatoModelSeller(final Map<Integer, String> line, final CSVWriter writer)
	{
		LOG.debug("Processing Data in processDatatoModelSeller ");
		boolean isIncorrectCode = false;
		try
		{
			if (line.get(Integer.valueOf(SELLERID)) != null)
			{
				final String sellerID = line.get(Integer.valueOf(SELLERID));
				final List<SellerInformationModel> list = getMplDelistingService().getAllUSSIDforSeller(sellerID);

				if (list != null && !list.isEmpty())
				{
					getMplDelistingService().delistSeller(list, line.get(Integer.valueOf(DELISTING)),
							line.get(Integer.valueOf(BLOCKOMS)));
				}
				else
				{
					LOG.error(sellerID + " Not Found in database");
				}
			}
			else
			{
				isIncorrectCode = true;
				final List<Integer> errorColumnList = errorListData(line, isIncorrectCode);
				LOG.error("SellerId is null");
				try
				{
					getDelistErrorHandling().writeErrorData(writer, line, errorColumnList, line,
							MarketplacecommerceservicesConstants.ERRORMESSAGE);
				}
				catch (final IOException exception)
				{
					LOG.error(exception.getMessage());
				}
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(line, isIncorrectCode);
			LOG.error(exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
		}
	}



	private void processDatatoModelUssid(final Map<Integer, String> line, final CSVWriter writer)
	{
		LOG.debug("Processing Data in processDatatoModelUssid ");
		boolean isIncorrectCode = false;
		try
		{
			if (line.get(Integer.valueOf(SELLERID)) != null)
			{
				final String ussidlistString = line.get(Integer.valueOf(DELIST));
				final String[] ussidlist = ussidlistString.split(":");

				for (final String ussid : ussidlist)
				{
					final List<SellerInformationModel> list = getMplDelistingService().getModelforUSSID(ussid);
					if (list != null && !list.isEmpty())
					{
						getMplDelistingService().delistUSSID(list, line.get(Integer.valueOf(DELISTING)));
					}
					else
					{
						LOG.error(ussid + " Not Found in database");
					}
				}

			}
			else
			{
				isIncorrectCode = true;
				final List<Integer> errorColumnList = errorListData(line, isIncorrectCode);
				LOG.error("SellerId is null");
				try
				{
					getDelistErrorHandling().writeErrorData(writer, line, errorColumnList, line,
							MarketplacecommerceservicesConstants.ERRORMESSAGE);
				}
				catch (final IOException exception)
				{
					LOG.error(exception.getMessage());
				}

			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(line, isIncorrectCode);
			LOG.error(exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);

		}
	}

	/**
	 * @Description : Populate error entry to file
	 * @param line
	 * @param writer
	 * @param errorColumnList
	 */
	private void populateErrorEntry(final Map<Integer, String> line, final CSVWriter writer, final List<Integer> errorColumnList)
	{
		try
		{
			getDelistErrorHandling().writeErrorData(writer, line, errorColumnList, line,
					MarketplacecommerceservicesConstants.ERRORMESSAGE);
		}
		catch (final IOException e)
		{
			LOG.debug(e.getMessage());
		}
	}

	/**
	 * @Description: Validates Data Uploaded
	 * @param line
	 * @param isIncorrectCode
	 * @return data
	 */
	private List<Integer> errorListData(final Map<Integer, String> line, final boolean isIncorrectCode)
	{
		final List<Integer> data = new ArrayList<>();

		if (isIncorrectCode)
		{
			data.add(Integer.valueOf(SELLERID));
		}
		return data;
	}

}
