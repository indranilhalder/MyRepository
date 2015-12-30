/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface BrandDao
{
	public List<String> getAllBrandListForCurrentCatalogVersion(final CatalogVersionModel catalogVersion);


	public boolean checkEmailId(String emailId);
}
