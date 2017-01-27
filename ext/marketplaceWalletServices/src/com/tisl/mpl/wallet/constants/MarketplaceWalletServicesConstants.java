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
package com.tisl.mpl.wallet.constants;

/**
 * Global class for all MarketplaceWalletServices constants. You can add global constants for your extension into this
 * class.
 */
public final class MarketplaceWalletServicesConstants extends GeneratedMarketplaceWalletServicesConstants
{
	public static final String EXTENSIONNAME = "marketplaceWalletServices";

	private MarketplaceWalletServicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public final static String MRUPEE_MERCHANT_CODE = "payment.mRupee.merchantID".intern();

	public final static String MRUPEE_CHECKSUM = "payment.mRupee.checkSum".intern();

	public final static String MRUPEE_NARRATION = "payment.mRupee.narration".intern();

	public final static String MRUPEERETURNURL = "payment.mRupee.returnUrl".intern();
	// implement here constants used by this extension

	public final static String PROXYENABLED = "proxy.enabled";
	public final static String GENPROXY = "proxy.address";
	public final static String GENPROXYPORT = "proxy.port";
}
