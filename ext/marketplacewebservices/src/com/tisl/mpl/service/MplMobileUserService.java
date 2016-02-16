/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.FetchNewsLetterSubscriptionWsDTO;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */
public interface MplMobileUserService
{
	public MplUserResultWsDto registerNewMplUser(final String login, final String password) throws EtailBusinessExceptions,
			EtailNonBusinessExceptions;

	public MplUserResultWsDto loginUser(final String login, final String password) throws EtailNonBusinessExceptions,
			EtailBusinessExceptions;

	public MplUserResultWsDto socialGoogleRegistration(final String accessToken, final String emailId, final String socialUserId);

	public MplUserResultWsDto socialFbRegistration(final String accessToken, final String emailId);

	public MplUserResultWsDto loginSocialGoogleUser(final String accessToken, final String login, final String userId);

	public MplUserResultWsDto loginSocialFbUser(final String accessToken, final String login);

	public FetchNewsLetterSubscriptionWsDTO fetchMplPreferenceContents();

}
