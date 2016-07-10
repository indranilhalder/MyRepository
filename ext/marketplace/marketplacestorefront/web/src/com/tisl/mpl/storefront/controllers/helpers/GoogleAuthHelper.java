package com.tisl.mpl.storefront.controllers.helpers;

/**
 *
 */



import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;



/**
 * A helper class for Google's OAuth2 authentication API.
 *
 * @version 20150604
 * @author TCS
 */
public final class GoogleAuthHelper
{
	private static final Logger LOG = Logger.getLogger(GoogleAuthHelper.class);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static Proxy proxy = null;
	private static HttpTransport HTTP_TRANSPORT = null;
	private String stateToken;
	private final GoogleAuthorizationCodeFlow flow;

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(ModelAttributetConstants.CONFIGURATION_SERVICE, ConfigurationService.class);
	}

	/**
	 * Constructor initializes the Google Authorization Code Flow with CLIENT ID, SECRET, and SCOPE
	 */
	public GoogleAuthHelper()
	{
		final String CLIENT_ID = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_GOOGLEAPI_CLIENT_ID);
		final String CLIENT_SECRET = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_GOOGLEAPI_CLIENT_SECRET);
		final String SCOPE_PROFILE = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_PROFILE);
		final String SCOPE_EMAIL = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_EMAIL);
		final String SCOPE_CONCAT = SCOPE_PROFILE + ";" + SCOPE_EMAIL;
		final Collection<String> SCOPE = Arrays.asList(SCOPE_CONCAT.split("\\;"));
		if ((null != getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_PROXY_ENABLED))
				&& getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_PROXY_ENABLED)
						.equalsIgnoreCase(MessageConstants.TRUE_FLAG))
		{
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY), Integer.parseInt(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY_PORT))));
			Authenticator.setDefault(new ProxyAuth(MessageConstants.SITEADMIN_UID, MessageConstants.SITEADMIN_PWD));
			HTTP_TRANSPORT = new NetHttpTransport.Builder().setProxy(proxy).build();
		}
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE)
				.setAccessType("offline").build();

		generateStateToken();
	}

	/**
	 * Builds a login URL based on client ID, secret, callback URI, and scope
	 */
	public String buildLoginUrl()
	{
		final String CALLBACK_URI = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_CALLBACK_URI);
		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token
	 */
	private void generateStateToken()
	{

		final SecureRandom sr1 = new SecureRandom();

		stateToken = "google;" + sr1.nextInt();

	}

	/**
	 * Accessor for state token
	 */
	public String getStateToken()
	{
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 *
	 * @return JSON formatted user profile information
	 * @param authCode
	 *           authentication code provided by google
	 */
	public String getUserInfoJson(final String authCode) throws IOException
	{
		Credential credential = null;
		credential = flow.loadCredential(authCode);
		if (credential == null)
		{
			Authenticator.setDefault(new ProxyAuth(MessageConstants.SITEADMIN_UID, MessageConstants.SITEADMIN_PWD));
			LOG.info(MessageConstants.INSIDE_METHOD_GOOGLE_CREDENTIAL);
			final String CALLBACK_URI = getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_CALLBACK_URI);
			final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
			credential = flow.createAndStoreCredential(response, authCode);
		}

		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final String USER_INFO_URL = getConfigurationService().getConfiguration().getString(
				MessageConstants.SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_OAUTH2);
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType(MessageConstants.APPLICATION_JSON);
		final String jsonIdentity = request.execute().parseAsString();

		return jsonIdentity;

	}




}

class ProxyAuth extends Authenticator
{
	private final String user, password;

	public ProxyAuth(final String user, final String password)
	{
		this.user = user;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(user, password.toCharArray());
	}
}
