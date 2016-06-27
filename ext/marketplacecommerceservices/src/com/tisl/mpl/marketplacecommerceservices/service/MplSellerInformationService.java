/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.OrderEntryModel;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TCS
 *
 */
public interface MplSellerInformationService
{
	SellerInformationModel getSellerDetail(final String aticleSKUID);

	List<SellerInformationModel> getSellerInformation(final String sellerID);

	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName);

	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId);

	SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID);
	
	public String getFullfillmentTypeOfParent(OrderEntryModel orderEntry) throws EtailNonBusinessExceptions;
}
