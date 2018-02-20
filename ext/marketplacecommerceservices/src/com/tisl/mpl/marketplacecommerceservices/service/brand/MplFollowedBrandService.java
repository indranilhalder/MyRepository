/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand;

import java.util.List;

import com.tisl.mpl.core.model.BrandMasterModel;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandService
{
	public List<BrandMasterModel> getFollowedBrands(final String gender);
}
