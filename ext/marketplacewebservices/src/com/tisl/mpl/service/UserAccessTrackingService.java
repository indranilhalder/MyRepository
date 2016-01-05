/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.model.UserAccessModel;
import com.tisl.mpl.model.UserInstallerAccessModel;


/**
 * @author TCS
 *
 */
public interface UserAccessTrackingService
{

	public void saveUserAccess(UserAccessModel userAccessModel);

	public void saveUserInstallerAccess(UserInstallerAccessModel userInstallerAccessModel);
}
