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
package com.tisl.mpl.coupon.constants;

/**
 * Global class for all Marketplacecoupon constants. You can add global constants for your extension into this class.
 */
public final class MarketplacecouponConstants extends GeneratedMarketplacecouponConstants
{
	public static final String EXTENSIONNAME = "marketplacecoupon";

	public static final String VOUCHERWITHINDATEQUERY = "select {d.voucher} from {DateRestriction as d} where sysdate>={d.startdate} and sysdate<={d.enddate}";
	public static final String COUPONCODE = "couponCode".intern();
	public static final String COUPONVALUE = "couponValue".intern();
	public static final String ZEROPOINTZEROONE = "0.01".intern();


	private MarketplacecouponConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
