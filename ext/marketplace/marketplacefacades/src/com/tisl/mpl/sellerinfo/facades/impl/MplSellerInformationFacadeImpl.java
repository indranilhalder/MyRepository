package com.tisl.mpl.sellerinfo.facades.impl;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

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
	public String getSellerColloctionDays(String sellerId)
	{
	  // get the sellerMaster model form 
		final SellerMasterModel sellerMaster = getSellerMasterBySellerId(sellerId);
		String collectionDays=null;
		if (sellerMaster != null)
		{
			collectionDays = sellerMaster.getCollectionDays();
		}
		else 
		{
			// read default collection days from configuration 
			if(mplConfigFacade != null)
			{
				collectionDays =mplConfigFacade.getCongigValue(MarketplaceFacadesConstants.COLLECTIONDAYS_CONFIG);
			}
			
		}
		return collectionDays;
	}

}
