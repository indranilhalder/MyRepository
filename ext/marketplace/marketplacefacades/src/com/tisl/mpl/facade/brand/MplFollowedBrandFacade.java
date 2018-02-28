/**
 *
 */
package com.tisl.mpl.facade.brand;

import java.util.List;

import com.tisl.mpl.core.model.FollowedBrandMcvidModel;
import com.tisl.mpl.wsdto.FollowedBrandWsDto;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandFacade
{
	public List<FollowedBrandWsDto> getFollowedBrands(final String gender);

	public boolean updateFollowedBrands(final String mcvId, final String brands, final boolean follow);

	public List<FollowedBrandMcvidModel> getUserFollowedMcvIds(final String mcvId);

	public List<FollowedBrandWsDto> getCustomerFollowedBrands(final String userId);

}
