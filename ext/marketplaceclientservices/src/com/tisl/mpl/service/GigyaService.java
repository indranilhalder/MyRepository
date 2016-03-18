/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.wsdto.GigyaWsDTO;


/**
 * @author TCS
 *
 */
public interface GigyaService
{

	public List<String> gigyaLoginHelper(final CustomerModel customerModel, final boolean isNewUser);

	public void ratingLogoutHelper(final CustomerModel customerModel);

	public boolean validateSignature(final String uid, final String timestamp, final String signature);

	public void notifyGigya(final String siteUid, final String gigyaUid, final String fName, final String lName,
			final String eMail, String gigyaMethod);

	public GigyaWsDTO gigyaLoginHelperforMobile(CustomerModel customerModel, boolean isNewUser);
}
