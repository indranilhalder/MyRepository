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
package com.tisl.lux.controllers;

/**
 */
public interface LuxurystoreaddonControllerConstants
{
	String ADDON_PREFIX = "addon:/luxurystoreaddon";
	public static final String LAST_USERNAME_WITH_ERROR_ATTEMPT = "lastUnameErrorAttempt";

	// implement here controller constants used by this extension
	interface Views
	{
		interface Fragments
		{
			interface Home
			{
				String LoginPanelFragment = ADDON_PREFIX + "/fragments/home/luxLoginPanel";
				String ForgotPasswordPanel = ADDON_PREFIX + "/fragments/home/forgottenPwd";
			}
		}
	}
}
