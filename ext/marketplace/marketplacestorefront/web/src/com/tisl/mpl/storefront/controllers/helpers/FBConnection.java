package com.tisl.mpl.storefront.controllers.helpers;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;


/**
 *
 * @author TCS
 *
 */
public class FBConnection
{
	private static final Logger LOG = Logger.getLogger(FBConnection.class);

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(ModelAttributetConstants.CONFIGURATION_SERVICE, ConfigurationService.class);
	}

	private static String accessToken = ModelAttributetConstants.EMPTY;
	private static final String n = "\n";

	/**
	 *
	 * @return String
	 */
	public String getFBAuthUrl()
	{
		String fbLoginUrl = ModelAttributetConstants.EMPTY;
		String REDIRECT_URI = ModelAttributetConstants.EMPTY;
		String FB_APP_ID = ModelAttributetConstants.EMPTY;
		try
		{
			FB_APP_ID = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_FB_APP_ID);
			REDIRECT_URI = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_CALLBACK_URI);
			fbLoginUrl = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_FB_URI_DIALOG_OAUTH2)
					+ MessageConstants.CLIENT_ID + FB_APP_ID + MessageConstants.AMPERSAND_REDIRECT_URI
					+ URLEncoder.encode(REDIRECT_URI, MessageConstants.UTF_8) + MessageConstants.AMPERSAND_SCOPE_EQUALS_TO_EMAIL;
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.debug(e);
		}
		return fbLoginUrl;
	}

	public String getFBGraphUrl(final String code)
	{
		String fbGraphUrl = ModelAttributetConstants.EMPTY;
		String REDIRECT_URI = ModelAttributetConstants.EMPTY;
		String FB_APP_ID = ModelAttributetConstants.EMPTY;
		String FB_APP_SECRET = ModelAttributetConstants.EMPTY;
		try
		{
			FB_APP_SECRET = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_FB_SECRET_ID);
			FB_APP_ID = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_FB_APP_ID);
			REDIRECT_URI = getConfigurationService().getConfiguration().getString(MessageConstants.SOCIAL_LOGIN_CALLBACK_URI);
			fbGraphUrl = getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_FB_URI_OAUTH_ACCESS_TOKEN)
					+ MessageConstants.CLIENT_ID
					+ FB_APP_ID
					+ MessageConstants.AMPERSAND_REDIRECT_URI
					+ URLEncoder.encode(REDIRECT_URI, MessageConstants.UTF_8)
					+ MessageConstants.AMPERSAND_CLIENT_SECRET
					+ FB_APP_SECRET + MessageConstants.AMPERSAND_CODE_IS_EQUALS + code;
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.debug(e);
		}
		return fbGraphUrl;
	}

	public String getAccessToken(final String code)
	{
		URL fbGraphURL = null;
		try
		{
			fbGraphURL = new URL(getFBGraphUrl(code));
		}
		catch (final MalformedURLException e)
		{
			LOG.debug(e);
			LOG.debug(MessageConstants.INVALID_CODE_RECEIVED);
			throw new EtailNonBusinessExceptions(e);
		}

		HttpURLConnection fbConnection;
		StringBuffer b = null;
		try
		{

			final SocketAddress addr = new InetSocketAddress(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY), Integer.parseInt(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY_PORT)));
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			fbConnection = (HttpURLConnection) fbGraphURL.openConnection(proxy);
			LOG.info(MessageConstants.RESPONSE_CODE_FROM_FB + fbConnection.getResponseCode());
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(fbConnection.getInputStream()));
			String inputLine;
			b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
			{
				b.append(inputLine + n);
			}
			in.close();



		}
		catch (final IOException e)
		{
			LOG.debug(e);
			LOG.debug(MessageConstants.UNABLE_TO_CONNECT_WITH_FACEBOOK);
			throw new EtailNonBusinessExceptions(e);
		}
		accessToken = b.toString();

		if (accessToken.charAt(0) == '{')
		{
			LOG.debug(MessageConstants.ERROR_ACCESS_TOKEN_INVALID);
			throw new RuntimeException(accessToken);
		}
		return accessToken;
	}
}
