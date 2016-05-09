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

	/**
	 * notify gigya for social registration and login through mobile
	 *
	 * @param uid
	 * @param gigyaUid
	 * @param fName
	 * @param lName
	 * @param eMail
	 * @param gigyaMethod
	 * @return GigyaWsDTO
	 */
	public GigyaWsDTO notifyGigyaforMobile(String uid, String gigyaUid, String fName, String lName, String eMail,
			String gigyaMethod);

	/**
	 * @param uid
	 * @param uid2
	 * @param firstName
	 * @param lastName
	 * @param login
	 * @param gigyaMethod
	 * @param timestamp
	 * @param signature
	 * @return
	 */
	public GigyaWsDTO notifyGigyaforMobilewithSig(String uid, String gigyauid, String firstName, String lName, String login,
			String gigyaMethod, String timestamp, String signature);
}
