/**
 *
 */
package com.tisl.mpl.facade.brand;

import java.util.List;

import com.tisl.mpl.wsdto.FollowedBrandWsDto;


/**
 * @author TCS
 *
 */
public interface MplFollowedBrandFacade
{
	public List<FollowedBrandWsDto> getFollowedBrands(final String gender);
}
