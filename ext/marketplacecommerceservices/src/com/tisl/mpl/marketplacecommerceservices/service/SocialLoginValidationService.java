/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import org.apache.commons.httpclient.auth.AuthChallengeException;




/**
 * @author TCS
 *
 */

public interface SocialLoginValidationService
{
	/**
	 *
	 * Method to validate the facebook access token for a user
	 *
	 * @param fbAccessToken
	 * @param userID
	 * @return success or failure boolean flag
	 * @throws AuthChallengeException
	 */
	public boolean checkFacebookAccessToken(String fbAccessToken, String userID) throws AuthChallengeException;

	/**
	 *
	 * Method to validate the google access token for a user
	 *
	 * @param googleAccessToken
	 * @param userID
	 * @return success or failure boolean flag
	 * @throws AuthChallengeException
	 */
	public boolean checkGoogleAccessToken(String googleAccessToken, String userID) throws AuthChallengeException;
}