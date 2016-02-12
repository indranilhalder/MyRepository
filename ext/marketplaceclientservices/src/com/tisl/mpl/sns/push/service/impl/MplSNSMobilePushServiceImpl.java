package com.tisl.mpl.sns.push.service.impl;

/*
 * TCS
 */
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.sns.push.dao.impl.MplSNSMobilePushDaoImpl;
import com.tisl.mpl.sns.push.service.MplSNSMobilePushService;
import com.tisl.mpl.sns.push.tools.MplAmazonSNSClientWrapper;
import com.tisl.mpl.sns.push.tools.MplSNSMessageGenerator.Platform;
import com.tisl.mpl.wsdto.PushNotificationData;


public class MplSNSMobilePushServiceImpl implements MplSNSMobilePushService
{

	@Autowired
	private MplSNSMobilePushDaoImpl mplSNSMobilePushDao;
	@Autowired
	private final MplAmazonSNSClientWrapper snsClientWrapper;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private ModelService modelService;
	private final String newline = "\n";

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public MplSNSMobilePushServiceImpl()
	{
		this.snsClientWrapper = new MplAmazonSNSClientWrapper(null);
	}

	public MplSNSMobilePushServiceImpl(final AmazonSNS snsClient)
	{
		this.snsClientWrapper = new MplAmazonSNSClientWrapper(snsClient);
	}

	private static final Logger LOG = Logger.getLogger(MplSNSMobilePushServiceImpl.class);
	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
	static
	{
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
		attributesMap.put(Platform.APNS_SANDBOX, null);
	}

	@Override
	/**
	 * send notifications to android and apple devices
	 *
	 * @param emailId
	 */
	public void setUpNotification(final String emailId, final PushNotificationData pushData)
	{
		try
		{
			AmazonSNS sns = null;

			final String proxyEnableStatus = getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.PROXYENABLED);
			final ClientConfiguration proxyConfig = new ClientConfiguration();

			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				proxyConfig.setProxyHost(getConfigurationService().getConfiguration().getString(
						MarketplacecclientservicesConstants.SOCIAL_DEV_PUSH_NOTIFICATION_PROXY));
				proxyConfig.setProxyPort(Integer.parseInt(getConfigurationService().getConfiguration().getString(
						MarketplacecclientservicesConstants.SOCIAL_DEV_LOGIN_PROXY_PORT)));

				//This will be migrated to local

				/*
				 * if (getConfigurationService().getConfiguration().getString(
				 * MarketplacecclientservicesConstants.PUSH_NOTIFICATION_LOCAL_SETUP) != null) { String isLocalSetupEnabled
				 * = getConfigurationService().getConfiguration().getString(
				 * MarketplacecclientservicesConstants.PUSH_NOTIFICATION_LOCAL_SETUP); isLocalSetupEnabled =
				 * (StringUtils.isNotEmpty(isLocalSetupEnabled)) ? isLocalSetupEnabled : "false"; if
				 * (isLocalSetupEnabled.equalsIgnoreCase("true")) { proxyConfig.setProxyUsername("INDIA\\559379");
				 * proxyConfig.setProxyPassword("Gomu@July"); } }
				 */


			}
			final AWSCredentials awsCreds = new BasicAWSCredentials(getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_ACCESS_KEY), getConfigurationService().getConfiguration()
					.getString(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_SECRET_KEY));
			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				sns = new AmazonSNSClient(awsCreds, proxyConfig);
			}
			else
			{
				sns = new AmazonSNSClient(awsCreds);
			}
			sns.setEndpoint(getConfigurationService().getConfiguration().getString(
					MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_AMAZON_SNS_URL));

			final MplSNSMobilePushServiceImpl notification = new MplSNSMobilePushServiceImpl(sns);
			CustomerModel customer = getModelService().create(CustomerModel.class);
			customer = getDeviceTokenForEmailId(emailId);
			String registrationId = null;
			String platform = null;
			if (null != customer.getDeviceKey())
			{
				registrationId = customer.getDeviceKey();
				LOG.debug("registrationId from customer mobile push notification ########################" + registrationId);
			}
			if (StringUtils.isNotEmpty(customer.getMobilePlatform()))
			{
				platform = customer.getMobilePlatform();
				LOG.debug("platform mobile push notification  ########################" + platform);
			}
			if (null != pushData && StringUtils.isNotEmpty(pushData.getMessage()))
			{
				LOG.debug("************ Push notification message ***********" + pushData.getMessage());
			}
			if (null != pushData && StringUtils.isNotEmpty(pushData.getOrderId()))
			{
				LOG.debug("************ Push notification orderID ***********" + pushData.getOrderId());
			}
			if (StringUtils.isNotEmpty(platform))
			{
				if (platform.equalsIgnoreCase(Platform.GCM.name()))
				{
					final String serverAPIKey = getConfigurationService().getConfiguration().getString(
							MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_SERVER_API_KEY);
					final String appName = getConfigurationService().getConfiguration().getString(
							MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APP_NAME);
					LOG.debug("******** Sending andorid push notifications ***************" + ":::::serverAPIKey::::" + serverAPIKey
							+ ":::appName:::" + appName);
					notification.sendAndroidAppNotification(serverAPIKey, registrationId, appName, pushData);
				}
				else
				{

					String deviceToken = null;
					if (null != customer.getDeviceKey())
					{
						deviceToken = customer.getDeviceKey();
					}
					else
					{
						if (null != getConfigurationService().getConfiguration().getString(
								MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_DEVICE_KEY))
						{
							deviceToken = getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_DEVICE_KEY);
						}
					}
					if (StringUtils.equalsIgnoreCase(platform, Platform.APNS.name())
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_CERTIFICATE))
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_PRIVATE_KEY))
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_APP_NAME)))
					{

						LOG.debug("******** Sending apple push notifications ***************"
								+ ":::::apns cert ::::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_CERTIFICATE)
								+ ":::: apns private key :::::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_PRIVATE_KEY)
								+ ":::apns appName:::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_APP_NAME));

						notification.sendAppleAppNotification(
								platform,
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_CERTIFICATE),
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_PRIVATE_KEY),
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_APP_NAME), deviceToken, pushData);
					}

					else if (StringUtils.equalsIgnoreCase(platform, Platform.APNS_SANDBOX.name())
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_CERTIFICATE))
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_PRIVATE_KEY))
							&& StringUtils.isNotEmpty(getConfigurationService().getConfiguration().getString(
									MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_APP_NAME)))
					{

						LOG.debug("******** Sending apple Sandox push notifications ***************"
								+ ":::::apns sandbox cert ::::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_CERTIFICATE)
								+ ":::: apns sandbox private key :::::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_PRIVATE_KEY)
								+ ":::apna sandbox appName:::"
								+ getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_APP_NAME));

						notification.sendAppleAppNotification(
								platform,
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_CERTIFICATE),
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_PRIVATE_KEY),
								getConfigurationService().getConfiguration().getString(
										MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_APNS_SANDBOX_APP_NAME), deviceToken,
								pushData);
					}
					else
					{
						LOG.debug("************* Invalid ios Platform or empty cert/private key data *******************");
					}
					LOG.debug("************* Push notification sent successfully. *******************");
				}
			}
		}
		catch (final AmazonServiceException ase)
		{
			LOG.error(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_ASE_ERROR + ase.getMessage() + newline
					+ ase.getStatusCode() + newline + ase.getErrorCode() + newline + ase.getErrorMessage() + newline
					+ ase.getErrorType() + newline + ase.getRequestId() + "************ Exception in sending push notifications : "
					+ ase);
		}
		catch (final AmazonClientException ace)
		{
			LOG.error(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_ACE_ERROR + ace.getMessage()
					+ "************ Exception in sending push notifications : " + ace);
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_CUST_DETAIL_ERROR + emailId + e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_ERROR + e);
		}
	}

	/**
	 * send app notifications for android
	 *
	 * @param serverAPIKey
	 * @param registrationId
	 * @param applicationName
	 * @param pushData
	 */
	private void sendAndroidAppNotification(final String serverAPIKey, final String registrationId, final String applicationName,
			final PushNotificationData pushData)
	{
		snsClientWrapper.sendNotification(Platform.GCM, "", serverAPIKey, registrationId, applicationName, attributesMap, pushData);
	}

	/**
	 * get cust details platform and device key for an emailid
	 *
	 * @param emailId
	 * @return CustomerModel
	 * @throws ClientEtailNonBusinessExceptions
	 */
	public CustomerModel getDeviceTokenForEmailId(final String emailId) throws ClientEtailNonBusinessExceptions
	{
		CustomerModel customer = getModelService().create(CustomerModel.class);
		customer = mplSNSMobilePushDao.getCustomerProfileDetail(emailId);
		return customer;
	}

	/**
	 * get cust details platform and device key for an uid
	 *
	 * @param uid
	 * @return CustomerModel
	 * @throws ClientEtailNonBusinessExceptions
	 */
	@Override
	public CustomerModel getCustForUId(final String uid) throws ClientEtailNonBusinessExceptions
	{
		CustomerModel customer = getModelService().create(CustomerModel.class);
		customer = mplSNSMobilePushDao.getCustomerProfileDetailForUid(uid);
		return customer;
	}

	/**
	 * send app notifications for apple
	 *
	 * @param certificate
	 *           should be in pem format with \n at the end of each line.
	 * @param privateKey
	 *           should be in pem format with \n at the end of each line.
	 * @param appleApplicationName
	 * @param deviceToken
	 *           This is 64 hex characters
	 */
	public void sendAppleAppNotification(final String platform, final String certificate, final String privateKey,
			final String appleApplicationName, final String deviceToken, final PushNotificationData pushData)
	{
		if (StringUtils.equalsIgnoreCase(platform, Platform.APNS.name()))
		{
			LOG.debug("********** APNS Push Notification *****************");
			snsClientWrapper.sendNotification(Platform.APNS, certificate, privateKey, deviceToken, appleApplicationName,
					attributesMap, pushData);
		}
		else if (StringUtils.equalsIgnoreCase(platform, Platform.APNS_SANDBOX.name()))
		{
			LOG.debug("********** APNS_SANDBOX Push Notification *****************");
			snsClientWrapper.sendNotification(Platform.APNS_SANDBOX, certificate, privateKey, deviceToken, appleApplicationName,
					attributesMap, pushData);
		}


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
}
