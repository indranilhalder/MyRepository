package com.tisl.mpl.core.cronjobs.delisting;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.tisl.mpl.core.model.MarketplaceDelistModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
//TISPRD-207 Changes
public class DelistingProcessorImpl implements DelistingProcessor
{

	@Autowired
	private MplDelistingService mplDelistingService;

	@Autowired
	private ModelService modelService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

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

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DelistingProcessorImpl.class.getName());


	//@Description: Variable Declaration for Mapping
	private static final Integer SELLERID = Integer.valueOf("0");
	private static final Integer DELISTING = Integer.valueOf("1");
	private static final Integer DELIST = Integer.valueOf("2");
	private static final Integer BLOCKOMS = Integer.valueOf("2");


	/**
	 * @Javadoc
	 * @Description:The process method is called by the Delisting Job in order to Retry for the Failed Records
	 */

	@Override
	public void process()
	{
		LOG.debug("Retry Mechanism Started");

		try
		{
			final List<MarketplaceDelistModel> delistingRecord = getMplDelistingService().findUnprocessedRecord();
			final List<MarketplaceDelistModel> savingList = new ArrayList<>();

			for (final MarketplaceDelistModel singleRecord : delistingRecord)
			{


				//Differentiating Between Seller and USSID

				if (StringUtils.isEmpty(singleRecord.getDelistUssid()))
				{


					LOG.debug("Retry Mechanism Started for Seller " + singleRecord.getSellerID());
					final Map<Integer, String> sellerList = new HashMap<>();
					sellerList.put(SELLERID, singleRecord.getSellerID());
					sellerList.put(DELISTING, singleRecord.getDelistingStatus());
					sellerList.put(BLOCKOMS, singleRecord.getIsBlockOMS());

					LOG.debug("Retry Mechanism Started for Seller " + singleRecord.getSellerID());

					singleRecord.setIsProcessed(processDatatoModelSeller(sellerList));
					savingList.add(singleRecord);


				}

				else
				{
					final Map<Integer, String> ussidList = new HashMap<>();

					//					final StringBuilder stBUssidList = new StringBuilder(100);
					LOG.debug("Retry Mechanism Started for Seller " + singleRecord.getSellerID() + " and USSID :"
							+ singleRecord.getDelistUssid());

					ussidList.put(SELLERID, singleRecord.getSellerID());
					ussidList.put(DELISTING, singleRecord.getDelistingStatus());
					ussidList.put(DELIST, singleRecord.getDelistUssid());
					singleRecord.setIsProcessed(processDatatoModelUssid(ussidList));
					savingList.add(singleRecord);

				}
			}
			LOG.debug("Updating Delisting Records");
			modelService.saveAll(savingList);

		}

		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException | EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
		}
	}

	/*
	 * @Javadoc
	 *
	 * @Description : Hot Folder Converter picks each line from the Seller-Delisting file in the form of Map and feeds to
	 * this method
	 *
	 * @param :line
	 *
	 * @return : isProcessed
	 */
	@Override
	public boolean processDatatoModelSeller(final Map<Integer, String> line)
	{
		LOG.debug("Processing Data in processDatatoModelSeller ");
		try
		{
			if (line.get(Integer.valueOf(SELLERID)) != null)
			{
				final String sellerID = line.get(Integer.valueOf(SELLERID));
				final List<SellerInformationModel> list = getMplDelistingService().getAllUSSIDforSeller(sellerID);

				if (CollectionUtils.isNotEmpty(list))
				{
					getMplDelistingService().delistSeller(list, line.get(Integer.valueOf(DELISTING)),
							line.get(Integer.valueOf(BLOCKOMS)));
					return true;

				}
				else
				{
					LOG.error(sellerID + " Not Found in database");
					return false;
				}
			}
			else
			{
				LOG.error("SellerId is null");

				return false;
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException | EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			return false;

		}
	}

	/*
	 * @Javadoc
	 *
	 * @Description : Hot Folder Converter picks each line from USSID delisting in the form of Map and feeds to this
	 * method
	 *
	 * @param :line
	 *
	 * @return : boolean
	 */

	@Override
	public boolean processDatatoModelUssid(final Map<Integer, String> line)
	{
		LOG.debug("Processing Data in processDatatoModelUssid ");
		boolean delistingSucccessful = true;
		try
		{
			if (line.get(Integer.valueOf(SELLERID)) != null)
			{
				final String ussidlistString = line.get(Integer.valueOf(DELIST));
				final String[] ussidlist = ussidlistString.split(":");

				for (final String ussid : ussidlist)
				{
					final List<SellerInformationModel> list = getMplDelistingService().getModelforUSSID(ussid);
					if (CollectionUtils.isNotEmpty(list))
					{
						delistingSucccessful = getMplDelistingService().delistUSSID(list, line.get(Integer.valueOf(DELISTING)))
								&& delistingSucccessful;
						//return true;
					}
					else
					{
						delistingSucccessful = false;
						LOG.error(ussid + " Not Found in database");
						//return false;
					}
				}

			}
			else
			{
				LOG.error("SellerId is null");
				delistingSucccessful = false;
				return delistingSucccessful;

			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException | EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			delistingSucccessful = false;
			return delistingSucccessful;

		}
		return delistingSucccessful;
	}


}
