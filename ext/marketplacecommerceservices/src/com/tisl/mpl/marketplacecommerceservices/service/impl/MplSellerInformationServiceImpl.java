/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.order.OrderEntryModel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;
import com.tisl.mpl.util.CatalogUtils;


/**
 * @author TCS
 *
 */
public class MplSellerInformationServiceImpl implements MplSellerInformationService
{
	private static final Logger LOG = Logger.getLogger(MplSellerInformationServiceImpl.class);
	@Autowired
	private MplSellerInformationDAO mplSellerInformationDAO;

	@Autowired
	private CatalogUtils catalogUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getSellerDetail(java.lang.String)
	 */
	@Override
	public SellerInformationModel getSellerDetail(final String aticleSKUID) throws EtailNonBusinessExceptions
	{
		if (aticleSKUID != null)
		{
			//TISEE-5429 , TISEE-5458
			final CatalogVersionModel onlineCatalog = catalogUtils.getSessionCatalogVersionForProduct();


			/*
			 * catalogService.getCatalogVersion( MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
			 * MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
			 */

			return mplSellerInformationDAO.getSellerInforationDetails(aticleSKUID, onlineCatalog);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getSellerDetail(java.lang.String)
	 */
	@Override
	public List<SellerInformationModel> getSellerInformation(final String sellerID)
	{
		if (sellerID != null)
		{
			//return mplSellerInformationDAO.getSellerInformation(sellerID);
			return getMplSellerInformationDAO().getSellerInformation(sellerID);
		}
		return null;
	}

	@Override
	public SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID)
	{

		if (sellerID != null)
		{
			//return mplSellerInformationDAO.getSellerInformation(sellerID);
			return getMplSellerInformationDAO().getSellerInformationWithSellerMaster(sellerID);
		}
		return null;
	}

	@Override
	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName)
	{
		if (sellerName != null)
		{
			//return mplSellerInformationDAO.getSellerInformationBySellerName(catalogVersion, sellerName);
			return getMplSellerInformationDAO().getSellerInformationBySellerName(catalogVersion, sellerName);
		}
		return null;
	}

	@Override
	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId)
	{
		if (sellerId != null)
		{
			//return mplSellerInformationDAO.getActiveSellerRootCategoryBySellerId(sellerId);
			return getMplSellerInformationDAO().getActiveSellerRootCategoryBySellerId(sellerId);
		}
		return null;
	}

	/**
	 * @return the mplSellerInformationDAO
	 */
	public MplSellerInformationDAO getMplSellerInformationDAO()
	{
		return mplSellerInformationDAO;
	}

	/**
	 * @param mplSellerInformationDAO
	 *           the mplSellerInformationDAO to set
	 */
	public void setMplSellerInformationDAO(final MplSellerInformationDAO mplSellerInformationDAO)
	{
		this.mplSellerInformationDAO = mplSellerInformationDAO;
	}

	/**
	 * get Fulfillment type for FreeBie Parent Transaction
	 *
	 * @author TECHOUTS
	 * @param orderEntry
	 * @return String
	 */
	@Override
	public String getFullfillmentTypeOfParent(final OrderEntryModel orderEntry) throws EtailBusinessExceptions
	{
		LOG.info("Inside getFullfillmentTypeOfParent method");
		String parentFulfillmentType = StringUtils.EMPTY;
		try
		{
			if (null != orderEntry && null != orderEntry.getParentTransactionID())
			{
				LOG.debug("parent transaction Id for" + orderEntry.getTransactionID() + " is : "
						+ orderEntry.getParentTransactionID());

				parentFulfillmentType = mplSellerInformationDAO.getparentFulfillmenttype(orderEntry.getParentTransactionID());

				LOG.debug(" Parent Fulfillment Type for " + orderEntry.getTransactionID() + " is :" + parentFulfillmentType);
			}
			return parentFulfillmentType;
		}
		catch (final EtailBusinessExceptions e)
		{
			throw new EtailBusinessExceptions(e.getErrorCode(), e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured " + e.getMessage());
			throw new EtailBusinessExceptions(e.getMessage(), e);
		}
	}

	@Override
	public SellerInformationModel getSellerInformationBySellerID(final CatalogVersionModel catalogVersion, final String sellerID)
	{
		if (sellerID != null)
		{
			return getMplSellerInformationDAO().getSellerInformationBySellerID(catalogVersion, sellerID);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getContentPageBySellerID(de.hybris
	 * .platform.catalog.model.CatalogVersionModel, java.lang.String)
	 */
	@Override
	public ContentPageModel getContentPageBySellerID(final String sellerId)
	{
		return getMplSellerInformationDAO().getContentPageBySellerID(sellerId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getSellerIdByUssid(java.lang.String)
	 */
	@Override
	public String getSellerIdByUssid(final String selectedUSSID)
	{
		String sellerID = null;
		final SellerInformationModel sellerModel = getSellerDetail(selectedUSSID);
		if (sellerModel != null)
		{
			sellerID = sellerModel.getSellerID();
		}
		return sellerID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getSellerDetail(java.lang.String,
	 * de.hybris.platform.catalog.model.CatalogVersionModel)
	 */
	@Override
	public SellerInformationModel getSellerDetail(final String selectedUSSID, final CatalogVersionModel onlineCatalog)
	{
		if (selectedUSSID != null)
		{
			return mplSellerInformationDAO.getSellerInforationDetails(selectedUSSID, onlineCatalog);
		}
		return null;
	}

}
