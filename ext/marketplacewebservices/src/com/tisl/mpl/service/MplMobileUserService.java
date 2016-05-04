/**
 *
 */
package com.tisl.mpl.service;

import java.io.UnsupportedEncodingException;

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
	public MplUserResultWsDto registerNewMplUser(final String login, final String password)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	public MplUserResultWsDto loginUser(final String login, final String password)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions;

	//	public MplUserResultWsDto socialGoogleRegistration(final String accessToken, final String emailId, final String socialUserId);
	//
	//	public MplUserResultWsDto socialFbRegistration(final String accessToken, final String emailId);
	//
	//	public MplUserResultWsDto loginSocialGoogleUser(final String accessToken, final String login, final String userId);
	//
	//	public MplUserResultWsDto loginSocialFbUser(final String accessToken, final String login);

	public FetchNewsLetterSubscriptionWsDTO fetchMplPreferenceContents();

	public MplUserResultWsDto socialGoogleRegistration(final String emailId, final String uid);

	public MplUserResultWsDto socialFbRegistration(final String emailId, final String uid);

	//	public MplUserResultWsDto loginSocialGoogleUser(final String login, final String uid) throws UnsupportedEncodingException;
	//
	//	public MplUserResultWsDto loginSocialFbUser(final String login, final String uid) throws UnsupportedEncodingException;

	/**
	 * @param emailId
	 * @param uid
	 * @param timestamp
	 * @param signature
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public MplUserResultWsDto loginSocialFbUser(String emailId, String uid, String timestamp, String signature)
			throws UnsupportedEncodingException;

	/**
	 * @param emailId
	 * @param uid
	 * @param timestamp
	 * @param signature
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public MplUserResultWsDto loginSocialGoogleUser(String emailId, String uid, String timestamp, String signature)
			throws UnsupportedEncodingException;

}
