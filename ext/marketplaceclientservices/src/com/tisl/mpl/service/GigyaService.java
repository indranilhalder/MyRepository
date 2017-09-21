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


	/**
	 * @param customerModel
	 * @param isNewUser
	 * @return List
	 */

	public GigyaWsDTO gigyaLoginHelperforMobile(CustomerModel customerModel, boolean isNewUser);

	/*
	 * @param siteUID
	 * 
	 * @Desc used to check duplicate id's in gigya
	 */
	public int checkGigyaUID(final String siteUid);
	
	/*
	 * @param uid
	 * 
	 * @param emailId
	 * 
	 * @Desc used to validate the emailId
	 */
	public boolean validateUser(final String uid, final String emailId);
}
