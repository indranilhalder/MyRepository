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

import com.tisl.mpl.core.model.LuxEnhancedSearchBoxComponentModel;


/**
 */
public interface LuxurystoreaddonControllerConstants
{
	String ADDON_PREFIX = "addon:/luxurystoreaddon";
	public static final String LAST_USERNAME_WITH_ERROR_ATTEMPT = "lastUnameErrorAttempt";;
	public static final String LUXURYLOGINPAGE = "luxuryLoginPage";

	// implement here controller constants used by this extension
	interface Views
	{
		interface Fragments
		{
			interface Home
			{
				String LoginPanelFragment = ADDON_PREFIX + "/fragments/home/luxLoginPanel";
				String ForgotPasswordPanel = ADDON_PREFIX + "/fragments/home/forgottenPwd";
				String RegisterFragment = ADDON_PREFIX + "/fragments/home/luxRegister";
			}
		}

		interface Pages
		{

			interface Home
			{
				String LuxuryLoginPage = ADDON_PREFIX + "/pages/home/luxLoginPanel";
			}
		}
	}

	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";
			String LuxEnhancedSearchBoxComponent = _Prefix + LuxEnhancedSearchBoxComponentModel._TYPECODE + _Suffix;

		}
	}
}
