/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.constants;

/**
 * Global class for all Marketplaceomsorders constants. You can add global constants for your extension into this class.
 */
public final class MarketplaceomsordersConstants extends GeneratedMarketplaceomsservicesConstants
{
	public static final String EXTENSIONNAME = "marketplaceomsorders";

	public static final String COD = "COD";
	public static final String PREPAID = "PREPAID";
	public static final String HOMEDELIVERY = "HD";
	public static final String EXPRESSDELIVERY = "ED";
	public static final String HOMEDELIVERYCODE = "home-delivery";
	public static final String EXPRESSDELIVERYCODE = "express-delivery";
	public static final String TSHIP = "TSHIP";
	public static final String AIR = "AIR";

	public static final String PAYMENTMETHOD_CREDIT_CARD = "Credit Card";
	public static final String PAYMENTMETHOD_DEBIT_CARD = "Debit Card";
	public static final String PAYMENTMETHOD_EMI = "EMI";
	public static final String PAYMENTMETHOD_NETBANKING = "NetBanking";
	public static final String PAYMENTMETHOD_COD = "COD";
	public static final String PAYMENTMETHOD_WALLET = "Wallet";

	public static final String REFUND_TYPE_CODE="S";
	public static final String TICKET_TYPE_CODE="C";
	public static final String REASON_CODE="01";
	public static final String TICKET_SUB_TYPE_CODE="UCP";
	public static final String EMPTY = "".intern();
	
	public static final String CUSTOMER_NAME = "Customer".intern();
	public static final String STORE_NAME = "Store".intern();
	public static final String ORDER_ID = "Order".intern();
	
	public static final String ORDER_ERROR = "Order not found in current BaseStore";
	public static final String ORDER_CURRENCY_ERROR = "source order currency must not be null";
	
	
	public static final String SMS_VARIABLE_ZERO = "{0}";
	public static final String SMS_VARIABLE_ONE = "{1}";
	public static final String SMS_VARIABLE_TWO = "{2}";
	public static final String PUSH_MESSAGE_ORDER_CANCELLED = "Hi! We have initiated a refund of INR {0} to you for the {1} item(s) you cancelled.The reason for this cancellation is {2}. To know more, check out the Order History section of the app.";

	public static final String FAILURE_FLAG = "FAILURE".intern();
	
	public static final String E0000 = "E0000";
	public static final String E0007 = "E0007";
	
	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String ORDER_STATUS_COLLECTED = "COLLECTED";
	
	public static final String REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE = "You'll courier this item back to us. Well, how kind of you! "
			.intern();
	public static final String REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC = "Only Self-Courier is available. Click on 'Continue' to proceed"
			.intern();
	public static final String REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE = "We'll send our man to pick up this item. Check our return policy here."
			.intern();
	public static final String REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC = "Reverse Logistics is available. Click on 'Continue' to proceed"
			.intern();
	public static final String SMS_SENDER_ID = "marketplace.sms.sender.name".intern();
	public static final String SMS_MESSAGE_ORDER_DELIVERY = "And it's done! You received {0} item(s) of your order # {1} today. We had tons of fun serving you. Thanks! To shop on the move, download our app at {2} .";
	
	public static final String SMS_SERVICE_APP_DWLD_URL = "marketplace.sms.app.download.url";
	
	public static final String ORDER_COLLECTED_SMS = "Hi {0},Your {1}  has been successfully collected from {2} on {3}";
	public static final String SMS_VARIABLE_ZERO_ORD_COLLECTED = "{0}";
	public static final String SMS_VARIABLE_ONE_ORD_COLLECTED = "{1}";
	public static final String SMS_VARIABLE_TWO_ORD_COLLECTED = "{2}";
	public static final String SMS_VARIABLE_THREE_ORD_COLLECTED = "{3}";
	
	
	public static final String ORDER_COLLECTED_SMS_CUSTOMER = "Hi {0},{1}  has successfully collected your package from {2} on {3} with {4}";
	public static final String SMS_VARIABLE_ZERO_ORD_COLLECTED_CUSTOMER = "{0}";
	public static final String SMS_VARIABLE_ONE_ORD_COLLECTED_CUSTOMER = "{1}";
	public static final String SMS_VARIABLE_TWO_ORD_COLLECTED_CUSTOMER = "{2}";
	public static final String SMS_VARIABLE_THREE_ORD_COLLECTED_CUSTOMER = "{3}";
	public static final String SMS_VARIABLE_FOUR_ORD_COLLECTED_CUSTOMER = "{4}";
	
	private MarketplaceomsordersConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
