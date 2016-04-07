/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.facades.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.facades.MplSellerInformationFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
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

}
