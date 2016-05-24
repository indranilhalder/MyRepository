package com.tisl.mpl.sellerinfo.facades.impl;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.sellerinfo.facades.MplSellerInformationFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TECH
 * 
 */
public class MplSellerInformationFacadeImpl implements MplSellerInformationFacade
{

	private static final Logger LOG = Logger.getLogger(MplSellerInformationFacadeImpl.class);

	@Autowired
	private MplSellerInformationService mplSellerInformationService;
	
	@Autowired
	private MplSellerMasterService mplSellerMasterService;
	
	@Autowired
	private MplConfigFacade mplConfigFacade;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade#getSellerDetail(java.lang.String)
	 */
	@Override
	public SellerInformationModel getSellerDetail(final String aticleSKUID)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getSellerDetail method in facade");
		}
		return mplSellerInformationService.getSellerDetail(aticleSKUID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade#getSellerInformation(java.lang.String)
	 */
	@Override
	public List<SellerInformationModel> getSellerInformation(final String sellerID)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getSellerInformation method in facade");
		}
		return mplSellerInformationService.getSellerInformation(sellerID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade#getSellerInformationBySellerName(de
	 * .hybris.platform.catalog.model.CatalogVersionModel, java.lang.String)
	 */
	@Override
	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getSellerInformationBySellerName method in facade");
		}
		return mplSellerInformationService.getSellerInformationBySellerName(catalogVersion, sellerName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade#getActiveSellerRootCategoryBySellerId
	 * (java.lang.String)
	 */
	@Override
	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getActiveSellerRootCategoryBySellerId method in facade");
		}
		return mplSellerInformationService.getActiveSellerRootCategoryBySellerId(sellerId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade#getSellerInformationWithSellerMaster
	 * (java.lang.String)
	 */
	@Override
	public SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getSellerInformationWithSellerMaster method in facade");
		}
		return mplSellerInformationService.getSellerInformationWithSellerMaster(sellerID);
	}

	/**
	 * 
	 */
	@Override
	public SellerMasterModel getSellerMasterBySellerId(final String sellerID)
	{
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Feching Seller mastr model by seller Id :"+sellerID);
		}
		if (mplSellerMasterService !=null)
		{
			return mplSellerMasterService.getSellerMaster(sellerID);
		}
		return null;
	}

	/**
	 * get Seller Collection days by seller ID
	 */
	@Override
	public String getSellerColloctionDays(final String sellerId)
	{
		// get the sellerMaster model by sellerID
		final SellerMasterModel sellerMaster = getSellerMasterBySellerId(sellerId);
		String collectionDays = null;
		if (sellerMaster != null && StringUtils.isNotBlank(sellerMaster.getCollectionDays()))
		{
			//Check that the value stored in DB column is a valid integer and is > 0.
			try
			{
				final int collDays = Integer.parseInt(sellerMaster.getCollectionDays());
				if (collDays > 0)
				{
					LOG.info("Using collection days from SellerMaster Model for sellerId: " + sellerId);
					collectionDays = sellerMaster.getCollectionDays();
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.warn("Invalid Value of Collection Days: " + sellerMaster.getCollectionDays() + "; sellerId: " + sellerId);
				//LOG.error("Invalid Value of Collection Days: " + sellerMaster.getCollectionDays() + "; sellerId: " + sellerId);
			}
		}
		if (collectionDays == null)
		{
			//Get collection days from mplConfig
			if (mplConfigFacade != null)
			{
				LOG.info("Using Collection Days as set in mplconfig for seller: " + sellerId);
				try
				{
					collectionDays = mplConfigFacade.getCongigValue(MarketplaceFacadesConstants.COLLECTIONDAYS_CONFIG);
				}
				catch (final Exception e)
				{
					LOG.warn("Encountered error while fetching Collection Days from mplConfig. Using Default value. " + e.toString());
					//LOG.error("Encountered error while fetching Collection Days from mplConfig. Using Default value. " + e.toString());
				}
			}
		}
		//If could not retrieve a Collection days by any means. then Use the default value.
		if (collectionDays == null)
		{
			LOG.info("Using Default value of collection days for sellerId: " + sellerId);
			collectionDays = String.valueOf(MarketplaceFacadesConstants.DEFAULT_COLLECTION_DAYS);
		}

		return collectionDays;
	}
}
