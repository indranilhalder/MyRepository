<<<<<<< HEAD
package com.tisl.mpl.sellerinfo.facades;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TECH
 * 
 */
public interface MplSellerInformationFacade
{
	SellerInformationModel getSellerDetail(final String aticleSKUID);

	List<SellerInformationModel> getSellerInformation(final String sellerID);

	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName);

	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId);

	SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID);
	
	SellerMasterModel getSellerMasterBySellerId(final String sellerID);
	
	String getSellerColloctionDays(final String sellerId);
}
=======
package com.tisl.mpl.sellerinfo.facades;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TECH
 * 
 */
public interface MplSellerInformationFacade
{
	SellerInformationModel getSellerDetail(final String aticleSKUID);

	List<SellerInformationModel> getSellerInformation(final String sellerID);

	public SellerInformationModel getSellerInformationBySellerName(final CatalogVersionModel catalogVersion,
			final String sellerName);

	public SellerSalesCategoryModel getActiveSellerRootCategoryBySellerId(final String sellerId);

	SellerInformationModel getSellerInformationWithSellerMaster(final String sellerID);
	
	SellerMasterModel getSellerMasterBySellerId(final String sellerID);
	
	String getSellerColloctionDays(final String sellerId);
}
>>>>>>> BRANCH_TCS-HYCOMM-R2_1PS-BN-205
