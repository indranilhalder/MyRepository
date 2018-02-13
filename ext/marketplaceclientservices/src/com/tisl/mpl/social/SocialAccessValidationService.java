/**
 *
 */
package com.tisl.mpl.social;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.httpclient.auth.AuthChallengeException;


/**
 * @author TCS
 *
 */
public interface SocialAccessValidationService
{
	public String verifyFbAccessToken(final String fbAccessToken, final String userID) throws AuthChallengeException,
			MalformedURLException, IOException, Exception;

	public String verifyGoogleAccessToken(final String googleAccessToken, final String userID) throws AuthChallengeException,
			MalformedURLException, IOException, Exception;
}
