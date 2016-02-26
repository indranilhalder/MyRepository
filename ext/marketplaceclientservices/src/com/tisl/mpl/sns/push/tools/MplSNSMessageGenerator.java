package com.tisl.mpl.sns.push.tools;

/*
 *TCS
 */
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.tisl.mpl.wsdto.PushNotificationData;


public class MplSNSMessageGenerator
{

	/*
	 * This message is delivered if a platform specific message is not specified for the end point. It must be set. It is
	 * received by the device as the value of the key "default".
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger LOG = Logger.getLogger(MplSNSMessageGenerator.class);

	public static enum Platform
	{
		// Apple Push Notification Service
		APNS,
		// Sandbox version of Apple Push Notification Service
		APNS_SANDBOX,
		// Google Cloud Messaging
		GCM;
	}

	/**
	 * create json of the message
	 *
	 * @param message
	 * @return String
	 */
	public static String jsonify(final Object message)
	{
		try
		{
			return objectMapper.writeValueAsString(message);
		}
		catch (final Exception e)
		{
			//e.printStackTrace();
			LOG.error("Exception ", e);
			throw (RuntimeException) e;
		}
	}

	/**
	 * creating the message to be sent to android devices
	 *
	 * @param pushData
	 * @return Map<String, String>
	 */
	private static Map<String, String> getData(final PushNotificationData pushData)
	{
		final Map<String, String> payload = new HashMap<String, String>();
		if (null != pushData.getMessage())
		{
			payload.put("message", pushData.getMessage());
			payload.put("orderId", pushData.getOrderId());
		}
		return payload;
	}

	/**
	 * frame messages to be sent to iOs
	 *
	 * @param pushData
	 * @return String
	 */
	public static String getAppleMessage(final PushNotificationData pushData)
	{
		final Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		final Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("alert", pushData.getMessage());
		//appMessageMap.put("badge", new Integer(1)); Avoid instantiating Integer objects. Call Integer.valueOf() instead.
		appMessageMap.put("badge", Integer.valueOf(1));
		appMessageMap.put("sound", "default");
		appleMessageMap.put("aps", appMessageMap);
		appleMessageMap.put("orderId", pushData.getOrderId());
		return jsonify(appleMessageMap);
	}


	/**
	 * frame messages to be sent to android device
	 *
	 * @param pushData
	 * @return String
	 */
	public static String getAndroidMessage(final PushNotificationData pushData)
	{
		final Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		androidMessageMap.put("collapse_key", "Welcome");
		androidMessageMap.put("data", getData(pushData));
		androidMessageMap.put("delay_while_idle", Boolean.TRUE);//new Boolean(true)
		//androidMessageMap.put("time_to_live", new Integer(1000)); Avoid instantiating Integer objects. Call Integer.valueOf() instead.
		androidMessageMap.put("time_to_live", Integer.valueOf(1000));
		androidMessageMap.put("dry_run", Boolean.FALSE);//new Boolean(false)
		return jsonify(androidMessageMap);
	}

}