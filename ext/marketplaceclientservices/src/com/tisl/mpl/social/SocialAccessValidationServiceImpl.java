/**
 *
 */
package com.tisl.mpl.social;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.auth.AuthChallengeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteAccessException;

import com.gigya.json.JSONObject;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;



/**
 * @author TCS
 *
 */
public class SocialAccessValidationServiceImpl implements SocialAccessValidationService
{
	private static final Logger LOG = Logger.getLogger(SocialAccessValidationServiceImpl.class);
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.social.SocialAccessValidationService#verifyFbAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public String verifyFbAccessToken(final String fbAccessToken, final String userID) throws AuthChallengeException,
			MalformedURLException, IOException, Exception
	{
		String output = null;
		final HttpsURLConnection conn;
		try
		{
			//Checking if the social access token auth is enabled or not
			if (Boolean.valueOf(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.SOCIAL_AUTH_ENABLE)).equals(
					Boolean.TRUE))
			{
				final StringBuilder urlQuery = new StringBuilder();
				urlQuery.append("https://graph.facebook.com/v2.12/me?fields=email&access_token=");
				urlQuery.append(fbAccessToken);
				final URL url = new URL(urlQuery.toString());
				Proxy proxy = null;
				if (Boolean.valueOf(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.PROXYENABLED)).equals(
						Boolean.TRUE))
				{
					final String proxyAddress = configurationService.getConfiguration().getString("proxy.address");
					final String proxyPort = configurationService.getConfiguration().getString("proxy.port");
					proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress.trim(), Integer.parseInt(proxyPort.trim())));
					conn = (HttpsURLConnection) url.openConnection(proxy);
				}
				else
				{
					conn = (HttpsURLConnection) url.openConnection();
				}
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200)
				{
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				final String message = org.apache.commons.io.IOUtils.toString(br);
				final JSONObject json = new JSONObject(message.toString());
				final String emailID = json.getString("email");
				if (emailID.equalsIgnoreCase(userID))
				{
					output = "success";
				}
				else
				{
					output = "failure";
				}
				br.close();
				conn.disconnect();
			}
			else
			{
				LOG.info("Social access token verification is not enabled ................");
			}

		}
		catch (final RemoteAccessException rae)
		{
			LOG.error("Error = " + rae.getMessage(), rae);
		}
		catch (final MalformedURLException e)
		{
			LOG.error(e);
			e.printStackTrace();
			throw new MalformedURLException();
		}
		catch (final IOException e)
		{
			LOG.error(e);
			e.printStackTrace();
			throw new IOException(e);
		}
		catch (final Exception ex)
		{
			LOG.error("Error = " + ex.getMessage(), ex);
			throw new Exception(ex);
		}
		return output;
	}

	@Override
	public String verifyGoogleAccessToken(final String googleAccessToken, final String userID) throws AuthChallengeException,
			MalformedURLException, IOException, Exception
	{
		String output = null;
		final HttpsURLConnection conn;
		final BufferedReader br;
		try
		{
			//Checking if the social access token auth is enabled or not
			if (Boolean.valueOf(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.SOCIAL_AUTH_ENABLE)).equals(
					Boolean.TRUE))
			{
				final StringBuilder urlQuery = new StringBuilder();
				urlQuery.append("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=");
				urlQuery.append(googleAccessToken);
				final URL url = new URL(urlQuery.toString());
				Proxy proxy = null;
				if (Boolean.valueOf(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.PROXYENABLED)).equals(
						Boolean.TRUE))
				{
					final String proxyAddress = configurationService.getConfiguration().getString("proxy.address");
					final String proxyPort = configurationService.getConfiguration().getString("proxy.port");
					proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress.trim(), Integer.parseInt(proxyPort.trim())));
					conn = (HttpsURLConnection) url.openConnection(proxy);
				}
				else
				{
					conn = (HttpsURLConnection) url.openConnection();
				}

				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200)
				{
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				final String message = org.apache.commons.io.IOUtils.toString(br);
				final JSONObject json = new JSONObject(message.toString());
				final String emailID = json.getString("email");
				if (StringUtils.isNotEmpty(emailID) && emailID.equalsIgnoreCase(userID))
				{
					output = "success";
				}
				else
				{
					output = "failure";
				}
				br.close();
				conn.disconnect();
			}
			else
			{
				LOG.info("Social access token verification is not enabled ................");
			}

		}
		catch (final RemoteAccessException rae)
		{
			LOG.error("Error = " + rae.getMessage(), rae);
		}
		catch (final MalformedURLException e)
		{
			LOG.error(e);
			e.printStackTrace();
			throw new MalformedURLException();
		}
		catch (final IOException e)
		{
			LOG.error(e);
			e.printStackTrace();
			throw new IOException(e);
		}
		catch (final Exception ex)
		{
			LOG.error("Error = " + ex.getMessage(), ex);
			throw new Exception(ex);
		}
		return output;
	}
}
