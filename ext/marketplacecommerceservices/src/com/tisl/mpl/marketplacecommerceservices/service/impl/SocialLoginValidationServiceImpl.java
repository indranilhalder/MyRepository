/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;



//import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.commons.httpclient.auth.AuthChallengeException;
import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.service.SocialLoginValidationService;
import com.tisl.mpl.social.SocialAccessValidationService;


/**
 * @author TCS
 *
 */

public class SocialLoginValidationServiceImpl implements SocialLoginValidationService
{
	private static final Logger LOG = Logger.getLogger(SocialLoginValidationServiceImpl.class);
	//@Resource(name = "configurationService")
	//private ConfigurationService configurationService;
	@Resource(name = "socialAccessValidationService")
	SocialAccessValidationService socialAccessValidationService;

	/**
	 *
	 * Method to validate the facebook access token for a user
	 *
	 * @param fbAccessToken
	 * @param userID
	 * @return success or failure boolean flag
	 * @throws AuthChallengeException
	 */
	@Override
	public boolean checkFacebookAccessToken(final String fbAccessToken, final String userID) throws AuthChallengeException
	{
		LOG.info("Starting to inspect the FB access token.....");
		boolean checkResult = false;
		String result = null;
		try
		{
			result = socialAccessValidationService.verifyFbAccessToken(fbAccessToken, userID);

			//IQA code Review fix
			if ("success".equalsIgnoreCase(result))
			//if (result.equalsIgnoreCase("success"))
			{
				checkResult = true;
			}
			LOG.info("Finished to inspect the FB access token.....");

		}
		catch (final Exception ex)
		{
			LOG.error(ex);
		}
		return checkResult;
	}

	@Override
	public boolean checkGoogleAccessToken(final String googleAccessToken, final String userID) throws AuthChallengeException
	{
		LOG.info("Starting to inspect the FB access token.....");
		boolean checkResult = false;
		String result = null;
		try
		{
			result = socialAccessValidationService.verifyGoogleAccessToken(googleAccessToken, userID);
			//IQA code Review fix
			if ("success".equalsIgnoreCase(result))
			//if (result.equalsIgnoreCase("success"))
			{
				checkResult = true;
			}
			LOG.info("Finished to inspect the FB access token.....");

		}
		catch (final Exception ex)
		{
			LOG.error(ex);
		}
		return checkResult;
	}
}