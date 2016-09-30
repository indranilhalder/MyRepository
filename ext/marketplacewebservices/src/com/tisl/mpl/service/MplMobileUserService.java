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
	public MplUserResultWsDto registerNewMplUser(final String login, final String password, final boolean tataTreatsEnable)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	public MplUserResultWsDto loginUser(final String login, final String password) throws EtailNonBusinessExceptions,
			EtailBusinessExceptions;

	public MplUserResultWsDto loginSocialUser(final String emailId, final String socialMedia);

	public FetchNewsLetterSubscriptionWsDTO fetchMplPreferenceContents();

	public MplUserResultWsDto socialMediaRegistration(final String emailId, final String socialMedia,
			final boolean tataTreatsEnable);

}
