/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.tisl.mpl.core.model.MarketplaceDelistModel;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */

public interface MplDelistingDao
{

	/**
	 * @return List<StateModel>
	 */
	public List<SellerInformationModel> getAllUSSIDforSeller(final String sellerId);

	public List<SellerInformationModel> getModelforUSSID(final String ussid);

	public void delistSeller(final List<String> ussid, final String delisting, final String blockOMS);

	public void delistUSSID(final List<String> ussid, final String delisting, final String delist);

	public List<SellerInformationModel> getModelforUSSID(final String ussid, CatalogVersionModel catalogVersion);

	//TISPRD-207 Changes
	public List<MarketplaceDelistModel> findUnprocessedRecord();

}
