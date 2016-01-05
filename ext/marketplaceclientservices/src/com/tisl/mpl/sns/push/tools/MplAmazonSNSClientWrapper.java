package com.tisl.mpl.sns.push.tools;

/*
 TCS
 */
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.sns.push.tools.MplSNSMessageGenerator.Platform;
import com.tisl.mpl.wsdto.PushNotificationData;


public class MplAmazonSNSClientWrapper
{

	private final AmazonSNS snsClient;

	public MplAmazonSNSClientWrapper()
	{
		snsClient = null;
	}

	public MplAmazonSNSClientWrapper(final AmazonSNS client)
	{
		this.snsClient = client;
	}

	private static final Logger LOG = Logger.getLogger(MplAmazonSNSClientWrapper.class);

	/**
	 * create platform application - app name
	 *
	 * @param applicationName
	 * @param platform
	 * @param principal
	 * @param credential
	 * @return CreatePlatformApplicationResult
	 */
	private CreatePlatformApplicationResult createPlatformApplication(final String applicationName, final Platform platform,
			final String principal, final String credential)
	{
		final CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(platform.name());
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	/**
	 * create platform endpoint - device key
	 *
	 * @param customData
	 * @param platformToken
	 * @param applicationArn
	 * @return CreatePlatformEndpointResult
	 */
	private CreatePlatformEndpointResult createPlatformEndpoint(final String customData, final String platformToken,
			final String applicationArn)
	{
		final CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		final String token = platformToken;
		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	/**
	 * delete a platform application - app name
	 *
	 * @param applicationArn
	 */
	private void deletePlatformApplication(final String applicationArn)
	{
		final DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	/**
	 * publishing message to the endpoint
	 *
	 * @param endpointArn
	 * @param platform
	 * @param attributesMap
	 * @param pushData
	 * @return PublishResult
	 */
	private PublishResult publish(final String endpointArn, final Platform platform,
			final Map<Platform, Map<String, MessageAttributeValue>> attributesMap, final PushNotificationData pushData)
	{
		final PublishRequest publishRequest = new PublishRequest();
		final Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
				.get(platform));
		if (notificationAttributes != null && !notificationAttributes.isEmpty())
		{
			publishRequest.setMessageAttributes(notificationAttributes);
		}
		publishRequest.setMessageStructure("json");
		// If the message attributes are not set in the requisite method,
		// notification is sent with default attributes
		String message = getPlatformSNSMessage(platform, pushData);
		final Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(platform.name(), message);
		message = MplSNSMessageGenerator.jsonify(messageMap);
		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTargetArn(endpointArn);

		// Display the message that will be sent to the endpoint/
		final StringBuilder builder = new StringBuilder(40);
		builder.append("{Message Attributes: ");
		for (final Map.Entry<String, MessageAttributeValue> entry : notificationAttributes.entrySet())
		{
			builder.append("(\"" + entry.getKey() + "\": \"" + entry.getValue().getStringValue() + "\"),");
		}
		builder.deleteCharAt(builder.length() - 1);
		//builder.append("}"); Avoid appending characters as strings in StringBuffer.append.
		builder.append('}');
		publishRequest.setMessage(message);
		return snsClient.publish(publishRequest);
	}

	/**
	 * send notifications internally calls 1 create platform application 2 create platform endpoint 3 publish message to
	 * the endpoint
	 *
	 * @param platform
	 * @param principal
	 * @param credential
	 * @param platformToken
	 * @param applicationName
	 * @param attrsMap
	 * @param pushData
	 */
	public void sendNotification(final Platform platform, final String principal, final String credential,
			final String platformToken, final String applicationName,
			final Map<Platform, Map<String, MessageAttributeValue>> attrsMap, final PushNotificationData pushData)
	{
		// Create Platform Application. This corresponds to an app on a
		// platform.
		final CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(applicationName, platform,
				principal, credential);

		// The Platform Application Arn can be used to uniquely identify the
		// Platform Application.
		final String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();

		// Create an Endpoint. This corresponds to an app on a device.
		final CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				"CustomData - Useful to store endpoint specific data", platformToken, platformApplicationArn);

		// Publish a push notification to an Endpoint.
		final PublishResult publishResult = publish(platformEndpointResult.getEndpointArn(), platform, attrsMap, pushData);
		LOG.debug("publishResult" + publishResult);
		// Delete the Platform Application since we will no longer be using it.
		deletePlatformApplication(platformApplicationArn);
	}

	/**
	 * @param platform
	 * @param pushData
	 * @return String
	 */
	private String getPlatformSNSMessage(final Platform platform, final PushNotificationData pushData)
	{
		switch (platform)
		{
			case APNS:
				return MplSNSMessageGenerator.getAppleMessage(pushData);
			case APNS_SANDBOX:
				return MplSNSMessageGenerator.getAppleMessage(pushData);
			case GCM:
				return MplSNSMessageGenerator.getAndroidMessage(pushData);
			default:
				throw new IllegalArgumentException(MarketplacecclientservicesConstants.PUSH_NOTIFICATIONS_PLATFORM_ERROR
						+ platform.name());
		}
	}

	/**
	 * get valid notification attributes
	 *
	 * @param notificationAttributes
	 * @return Map<String, MessageAttributeValue>
	 */
	public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
			final Map<String, MessageAttributeValue> notificationAttributes)
	{
		final Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();

		if (notificationAttributes == null)
		{
			return validAttributes;
		}

		for (final Map.Entry<String, MessageAttributeValue> entry : notificationAttributes.entrySet())
		{
			if (!MplSNSStringUtils.isBlank(entry.getValue().getStringValue()))
			{
				validAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return validAttributes;
	}
}
