/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.brand;

import java.util.List;

import com.tisl.mpl.core.model.BrandMasterModel;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandDao
{
	public List<BrandMasterModel> getFollowedBrands(String gender);
}
