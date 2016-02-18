/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;

/**
 * @author 890223
 *
 */
public interface MarketplaceCallContextController extends CallContextController {

	public UserModel getCurrentUser() ;

	String changeAgentPassword(String newPassword, String confirmPassword);
}
