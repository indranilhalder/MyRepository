/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import com.tisl.mpl.model.UserAccessModel;
import com.tisl.mpl.model.UserInstallerAccessModel;



/**
 * @author TCS
 *
 */
public class UserAccessTrackingServiceImpl implements UserAccessTrackingService
{

	@Resource
	private ModelService modelService;

	@Override
	public void saveUserAccess(final UserAccessModel userAccessModel)
	{
		if (null != userAccessModel && null != modelService)
		{
			modelService.save(userAccessModel);
		}
	}

	@Override
	public void saveUserInstallerAccess(final UserInstallerAccessModel userInstallerAccessModel)
	{
		if (null != userInstallerAccessModel && null != modelService)
		{
			modelService.save(userInstallerAccessModel);
		}
	}


}
