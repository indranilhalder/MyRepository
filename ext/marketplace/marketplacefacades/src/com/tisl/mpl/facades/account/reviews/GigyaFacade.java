/**
 *
 */
package com.tisl.mpl.facades.account.reviews;

import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.wsdto.GigyaWsDTO;


/**
 * @author TCS
 *
 */
public interface GigyaFacade
{

	public void notifyGigya(final String siteUid, final String gigyaUid, final String fName, final String lName,
			final String eMail, String gigyaMethod);

	/**
	 * @param customerModel
	 * @param isNewUser
	 * @return List
	 */
	public GigyaWsDTO gigyaLoginHelper(CustomerModel customerModel, boolean isNewUser);
}
