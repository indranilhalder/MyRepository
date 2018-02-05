/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;

import java.util.List;

import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.PriorityBrandsModel;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;


/**
 * @author TCS
 *
 */
public interface BrandDao
{
	public List<String> getAllBrandListForCurrentCatalogVersion(final CatalogVersionModel catalogVersion);


	public boolean checkEmailId(String emailId);

	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId);

	public List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(final String emailId);



	/**
	 * @param emailId
	 * @param isLuxury
	 * @return
	 */
	List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(String emailId, String isLuxury);

	public List<PriorityBrandsModel> priorityBrands(final String categoryCode);

	//added for pdp new ui start
	public BrandModel brandInfoPwa(final PK pk);
}
