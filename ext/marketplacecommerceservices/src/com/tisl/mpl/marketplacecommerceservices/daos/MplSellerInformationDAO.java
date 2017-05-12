/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.List;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TCS
 *
 */
public interface MplSellerInformationDAO
{
	//TISEE-5429 , TISEE-5458
	SellerInformationModel getSellerInforationDetails(final String articleSKUID, CatalogVersionModel onlineCatalog);

	List<SellerInformationModel> getSellerInformation(final String sellerID);

	SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID);

	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName);

	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId);

	public String getparentFulfillmenttype(String transactionId);

	//TPR-4471
	public SellerInformationModel getSellerInformationBySellerID(CatalogVersionModel catalogVersion, String sellerID);

	public ContentPageModel getContentPageBySellerID(String sellerID);
}
