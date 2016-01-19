/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TCS
 *
 */
public interface MplSellerInformationService
{
	SellerInformationModel getSellerDetail(final String aticleSKUID);

	SellerInformationModel getSellerInformation(final String sellerID);

	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName);

	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId);

	SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID);
}
