/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gigya.json.JSONObject;
import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.SigUtils;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.GigyaService;


/**
 * @author TCS
 *
 */
public class GigyaServiceImpl implements GigyaService
{
	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(GigyaServiceImpl.class);

	private String apikey;
	private String secretkey;
	private String use;
	private String domain;
	private String proxyPort;
	private String proxyEnabled;
	private String proxyAddress;

	public String getApikey()
	{
		return apikey;
	}




	public void setApikey(final String apikey)
	{
		this.apikey = apikey;
	}




	public String getSecretkey()
	{
		return secretkey;
	}




	public void setSecretkey(final String secretkey)
	{
		this.secretkey = secretkey;
	}




	public String getUse()
	{
		return use;
	}




	public void setUse(final String use)
	{
		this.use = use;
	}




	public String getDomain()
	{
		return domain;
	}




	public void setDomain(final String domain)
	{
		this.domain = domain;
	}




	public String getProxyPort()
	{
		return proxyPort;
	}




	public void setProxyPort(final String proxyPort)
	{
		this.proxyPort = proxyPort;
	}




	public String getProxyEnabled()
	{
		return proxyEnabled;
	}




	public void setProxyEnabled(final String proxyEnabled)
	{
		this.proxyEnabled = proxyEnabled;
	}




	public String getProxyAddress()
	{
		return proxyAddress;
	}




	public void setProxyAddress(final String proxyAddress)
	{
		this.proxyAddress = proxyAddress;
	}






	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}




	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}




	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}

	SocketAddress addr = null;
	Proxy proxy = null;

	void setProxy()
	{
		final int portproxy = Integer.parseInt(getProxyPort());
		addr = new InetSocketAddress(getProxyAddress(), portproxy);
		proxy = new Proxy(Proxy.Type.HTTP, addr);
	}

	/*
	 * This method helps in Logging the User in the Gigya Side and Registers New User
	 *
	 * @param CustomerModel customerModel
	 *
	 *
	 * @return List<String> cookieData
	 */
	@Override
	public List<String> gigyaLoginHelper(final CustomerModel customerModel, final boolean isNewUser)
	{
		final List<String> cookieData = new ArrayList<>();
		try
		{
			// Define the API-Key and Secret key .

			final String gigyaMethod = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.METHOD_NOTIFY_LOGIN);

			final String proxyEnabledStatus = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.PROXYENABLED);

			final GSObject userAction = new GSObject();
			String firstName = null;
			String lastName = null;
			final JSONObject loginUserInfo = new JSONObject();
			String emailId = null;

			if (getSecretkey() != null && getApikey() != null)
			{

				if (customerModel.getFirstName().length() > 0 && !(customerModel.getFirstName().equals(" "))
						&& !(" ".equals(customerModel.getFirstName())))
				{
					firstName = customerModel.getFirstName();
					LOG.debug("USER FIRSTNAME:- " + firstName);
				}
				else
				{
					emailId = customerModel.getOriginalUid(); // final
					final String splitList[] = emailId.split(MarketplacecclientservicesConstants.SPLIT_AT);
					firstName = splitList[0];
					if (firstName.contains("."))
					{
						firstName = firstName.replace('.', ' ');
					}

					LOG.debug("USER EMAIL:- " + emailId + " ," + firstName);
				}

				if (customerModel.getLastName().length() > 0 && !(customerModel.getLastName().equals(" "))
						&& !(" ".equals(customerModel.getLastName())))
				{
					lastName = customerModel.getLastName();
					LOG.debug("USER LASTNAME:- " + lastName);
				}


				userAction.put(MarketplacecclientservicesConstants.FIRST_NAME, firstName);
				userAction.put(MarketplacecclientservicesConstants.LAST_NAME, lastName);

				if (firstName != null || emailId != null || lastName != null)
				{
					loginUserInfo.put("firstName", firstName);
					loginUserInfo.put("email", emailId);
					loginUserInfo.put("lastName", lastName);
				}

				//Defining the request
				final GSRequest request = new GSRequest(getApikey(), getSecretkey(), gigyaMethod);
				if (proxyEnabledStatus.equalsIgnoreCase("true"))
				{
					setProxy();
					request.setProxy(proxy);
				}

				// Adding parameters
				request.setParam(MarketplacecclientservicesConstants.PARAM_SITEUID, customerModel.getUid());
				request.setParam(MarketplacecclientservicesConstants.PARAM_ISNEWUSER, isNewUser);
				request.setParam(MarketplacecclientservicesConstants.PARAM_USERINFO, userAction);
				request.setUseHTTPS(MarketplacecclientservicesConstants.PARAM_USEHTTPS);
				request.setAPIDomain(getDomain());

				if (loginUserInfo.toString() != null)
				{
					request.setParam("userInfo", loginUserInfo.toString());
				}

				// Step 3 - Sending the request
				LOG.debug(MarketplacecclientservicesConstants.WAIT_RESPONSE);
				final GSResponse response = request.send();

				// Step 4 - handling the request's response.

				if (response != null)
				{
					if (response.getErrorCode() == 0)

					{
						LOG.debug(response.getResponseText());
						final GSObject responsedata = response.getData();

						cookieData.add((String) responsedata.get(MarketplacecclientservicesConstants.RESPONSE_PARAM_COOKIENAME));
						cookieData.add((String) responsedata.get(MarketplacecclientservicesConstants.RESPONSE_PARAM_COOKIEVALUE));
						cookieData.add((String) responsedata.get(MarketplacecclientservicesConstants.RESPONSE_PARAM_COOKIEDOMAIN));
						cookieData.add((String) responsedata.get(MarketplacecclientservicesConstants.RESPONSE_PARAM_COOKIEPATH));

					}
					else
					{
						LOG.error(MarketplacecclientservicesConstants.ERROR_RESPONSE + response.getLog());
					}

				}
				else
				{
					LOG.debug(MarketplacecclientservicesConstants.NULL_RESPONSE);
				}
			}
			else
			{
				LOG.error(MarketplacecclientservicesConstants.CHECK_PROPERTIES_FILE);

			}
		}
		catch (final GSKeyNotFoundException e)
		{
			LOG.error(MarketplacecclientservicesConstants.KEY_NOT_FOUND, e);
			return cookieData;
		}

		catch (final Exception ex)
		{
			LOG.error("Exception", ex);

		}

		return cookieData;



	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.GigyaService#RatingLogoutHelper(de.hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public void ratingLogoutHelper(final CustomerModel customerModel)
	{

		try
		{
			// Define the API-Key and Secret key .

			final String gigyaMethod = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.METHOD_LOGOUT);

			final String proxyEnabledStatus = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.PROXYENABLED);


			if (getSecretkey() != null && getApikey() != null)

			{
				//Defining the request
				final GSRequest request = new GSRequest(getApikey(), getSecretkey(), gigyaMethod);
				if (proxyEnabledStatus.equalsIgnoreCase("true"))
				{
					setProxy();
					request.setProxy(proxy);
				}
				// Adding parameters
				request.setParam("UID", customerModel.getUid()); // set the "uid" parameter to user's ID
				request.setUseHTTPS(MarketplacecclientservicesConstants.PARAM_USEHTTPS);
				request.setAPIDomain(getDomain());

				// Step 3 - Sending the request
				LOG.debug(MarketplacecclientservicesConstants.WAIT_RESPONSE);
				final GSResponse response = request.send();

				// Step 4 - handling the request's response.
				if (response != null)
				{
					if (response.getErrorCode() == 0)
					{ // SUCCESS! response status = OK
						LOG.debug(response.getResponseText());

					}
					else
					{
						response.getResponseText();
					}

				}

				else
				{
					LOG.debug(MarketplacecclientservicesConstants.NULL_RESPONSE);
				}

			}

			else
			{
				LOG.error(MarketplacecclientservicesConstants.CHECK_PROPERTIES_FILE);
			}
		}

		catch (final Exception ex)
		{
			LOG.error("Exception" + ex);
			LOG.error(MarketplacecclientservicesConstants.KEY_NOT_FOUND + ex);

		}


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.GigyaService#validateSignature(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateSignature(final String uid, final String timestamp, final String signature)
	{
		try
		{
			return SigUtils.validateUserSignature(uid, timestamp, getSecretkey(), signature);
		}
		catch (final InvalidKeyException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final UnsupportedEncodingException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.GigyaService#notifyGigyaOfRegistration(java.lang.String, java.lang.String)
	 */
	@Override
	public void notifyGigyaOfRegistration(final String siteUid, final String gigyaUid, final String firstName,
			final String lastName)
	{


		try
		{
			// Define the API-Key and Secret key .
			final String gigyaMethod = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.METHOD_NOTIFY_REGISTRATION);

			final String proxyEnabledStatus = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.PROXYENABLED);


			final JSONObject loginUserInfo = new JSONObject();

			if (getSecretkey() != null && getApikey() != null)

			{
				//Defining the request
				final String method = gigyaMethod;
				final GSRequest request = new GSRequest(getApikey(), getSecretkey(), method);
				if (proxyEnabledStatus.equalsIgnoreCase("true"))
				{
					setProxy();
					request.setProxy(proxy);
				}
				request.setParam("siteUID", siteUid);
				request.setParam("UID", gigyaUid);
				request.setUseHTTPS(MarketplacecclientservicesConstants.PARAM_USEHTTPS);
				request.setAPIDomain(getDomain());
				if (firstName != null || lastName != null)
				{
					loginUserInfo.put("firstName", firstName);
					loginUserInfo.put("lastName", lastName);
				}

				if (loginUserInfo.toString() != null)
				{
					request.setParam("userInfo", loginUserInfo.toString());
				}


				LOG.debug("notifyGigyaOfRegistration,UID:-" + siteUid);
				LOG.debug("notifyGigyaOfRegistration,GIGYA UID:-" + gigyaUid);


				// Step 3 - Sending the request
				LOG.debug(MarketplacecclientservicesConstants.WAIT_RESPONSE);
				final GSResponse response = request.send();

				// Step 4 - handling the request's response.
				if (response != null)
				{
					if (response.getErrorCode() == 0)
					{ // SUCCESS! response status = OK
						LOG.debug(response.getResponseText());

					}
					else
					{
						response.getResponseText();
					}

				}

				else
				{
					LOG.debug(MarketplacecclientservicesConstants.NULL_RESPONSE);
				}

			}

			else
			{
				LOG.error(MarketplacecclientservicesConstants.CHECK_PROPERTIES_FILE);
			}
		}

		catch (final Exception ex)
		{
			LOG.error("Exception" + ex);
			LOG.error(MarketplacecclientservicesConstants.KEY_NOT_FOUND + ex);

		}
	}




}
