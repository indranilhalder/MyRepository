/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public interface MplDelistingService
{

	/**
	 * @return List<StateModel>
	 */
	public List<SellerInformationModel> getAllUSSIDforSeller(final String sellerId);

	public List<SellerInformationModel> getModelforUSSID(final String ussid);

	public void delistSeller(final List<SellerInformationModel> ussid, final String delisting, final String blockOMS);

	public void delistUSSID(final List<SellerInformationModel> ussid, final String delisting);

	public List<SellerInformationModel> getModelforUSSID(final String ussid, CatalogVersionModel catalogVersion);


}
