/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand;

import java.util.List;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandMcvidModel;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandService
{
	public List<BrandMasterModel> getFollowedBrands(final String gender);

	public boolean updateFollowedBrands(final String mcvId, final String brands, final boolean follow);

	public List<FollowedBrandMcvidModel> getUserFollowedMcvIds(final String mcvId);
}
