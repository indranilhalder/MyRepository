/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand;

import java.util.List;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandMcvidModel;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandDao
{
	public List<BrandMasterModel> getFollowedBrands(String gender);

	public List<BrandMasterModel> getBrands(String brands);

	public List<FollowedBrandMcvidModel> getMcvIdBrands(final String mcvId);
}
