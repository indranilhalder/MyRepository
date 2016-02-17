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
package com.tisl.mpl.juspay.constants;

/**
 * Global class for all MarketplaceJuspayServices constants. You can add global constants for your extension into this
 * class.
 */
public final class MarketplaceJuspayServicesConstants extends GeneratedMarketplaceJuspayServicesConstants
{
	public static final String EXTENSIONNAME = "marketplaceJuspayServices";

	private MarketplaceJuspayServicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public final static String DEVELOPMENT = "http://local.api.juspay.in";
	public final static String PRODUCTION = "https://api.juspay.in";
	public final static String SANDBOX = "https://sandbox.juspay.in";
	public final static String PROXYENABLED = "proxy.enabled";
	public final static String GENPROXY = "proxy.address";
	public final static String GENPROXYPORT = "proxy.port";

	public final static String AMOUNT = "amount";
	public final static String CUSTOMERID = "customer_id";
	public final static String ORDERID = "order_id";
	public final static String STATUS = "status";
	public final static String CARDTOKEN = "card_token";
	public final static String CARDNUMBER = "card_number";
	public final static String CARDREF = "card_reference";
	public final static String NAMEONCARD = "name_on_card";
	public final static String MERCHANTID = "merchantId";
	public final static String VERSION = "payment.juspay.version";
}
