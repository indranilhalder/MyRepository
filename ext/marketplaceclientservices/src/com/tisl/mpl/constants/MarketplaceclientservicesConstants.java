/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
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
 * Global class for all Marketplaceclientservices constants. You can add global constants for your extension into this
 * class.
 */
public final class MarketplaceclientservicesConstants extends GeneratedMarketplaceclientservicesConstants
{
	public static final String EXTENSIONNAME = "marketplaceclientservices";
	public static final String REFUNDURL = "Comerce_oms_refundinfo_url";
	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String GOOGLE_SHORT_URL_API_KEY = "googleApiKey".intern();
	public static final String GOOGLE_API_SHORT_URL = "mpl.google.shortener.url".intern();
	public static final String MPL_TRACK_ORDER_LONG_URL_FORMAT = "mpl.order.track.longurl.format".intern();
	public static final String TSHIP_CODE = "TSHIP";
	public static final String SSHIP_CODE = "SSHIP";
	public static final String FULFILLMENTTYPE_BOTH = "both";
	public final static String PROXYENABLED = "proxy.enabled";
	public final static String GENPROXY = "proxy.address";
	public final static String GENPROXYPORT = "proxy.port";
	public static final String LuxuryPrefix = "lux";
	public static final String LUX_TRACK_ORDER_LONG_URL_FORMAT = "lux.order.track.longurl.format".intern();
	public static final String SHORTURL_CONNECTION_TIMEOUT = "short.url.connection.timeout";
	public static final String SHORTURL_READ_TIMEOUT = "short.url.read.timeout";


	// QC Wallet
	public static final String QC_INITIALIZATION_URL = "http://qc3.qwikcilver.com/Qwikcilver/eGMS.RestApi/api/initialize";
	public static final String FORWARDING_ENTITY_ID = "ForwardingEntityId";
	public static final String FORWARDING_ENTITY_PASSWORD = "ForwardingEntityPassword";
	public static final String TERMINAL_ID = "TerminalId";
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	public static final String TRANSACTION_ID = "TransactionId";
	public static final String DATE_AT_CLIENT = "DateAtClient";
	public static final String IS_FORWARDING_ENTIRY_EXISTS = "IsForwardingEntityExists";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACQUIRERID = "AcquirerId";
	public static final String MERCHANT_OUTLET_NAME = "MerchantOutletName";
	public static final String ORGANIZATION_NAME = "OrganizationName";
	public static final String POS_ENTRY_MODE = "POSEntryMode";
	public static final String POS_TYPE_ID = "POSTypeId";
	public static final String POS_NAME = "POSName";
	public static final String TERM_APP_VERSION = "TermAppVersion";
	public static final String CURRENT_BATCH_NUMBER = "CurrentBatchNumber";

	public static final String ADD_TO_CARD_TO_WALLET = "http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/";
	public static final String GET_BALANCE_FOR_WALLET = "http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/";

	// QC Wallet

	// Pincode Serviceabilty constants
	/*
	 * public static final String URLFIRSTPHASE =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><PincodeServiceability><Pincode><replacePincode></Pincode>"
	 * ; public static final String URLSECONDPHASE =
	 * "<Item><USSID><replaceUssid></USSID><FulfilmentType>TSHIP</FulfilmentType><TransportMode>AIR</TransportMode><DeliveryMode><Type>HD</Type><Inventory>449</Inventory><isPincodeServiceable>Y</isPincodeServiceable><isCOD>N</isCOD><isPrepaidEligible>N</isPrepaidEligible><DeliveryDate>2015-07-21T15:54:04Z</DeliveryDate></DeliveryMode></Item>"
	 * ; public static final String URLTHIRDPHASE = "</PincodeServiceability>";
	 * 
	 * public static final String URLSOFTFIRSTPHASE =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><inventoryReservationResponseDTO>"; public static
	 * final String URLSOFTSECONDPHASE =
	 * "<Item><ussId>S000030000000000000001</ussId><reservationStatus>success</reservationStatus></Item>"; public static
	 * final String URLSOFTTHIRDPHASE = "</inventoryReservationResponseDTO>"; public static final String
	 * PIN_CODE_DELIVERY_MODE_OMS_URL = "oms.pincode.serviceabilility.url"; public static final String
	 * PIN_CODE_DELIVERY_MODE_OMS_MOCK = "oms.pincodeserviceability.realtimecall"; public static final String
	 * OMS_SOFT_RESERV_URL = "oms.softReservation.duration";
	 */
	private MarketplaceclientservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
