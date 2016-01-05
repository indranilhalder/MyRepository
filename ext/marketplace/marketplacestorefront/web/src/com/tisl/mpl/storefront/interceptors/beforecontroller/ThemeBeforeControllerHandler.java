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
package com.tisl.mpl.storefront.interceptors.beforecontroller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.context.ThemeSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ThemeResolver;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.interceptors.BeforeControllerHandler;


public class ThemeBeforeControllerHandler implements BeforeControllerHandler
{
	@Resource(name = "themeResolver")
	private ThemeResolver themeResolver;

	@Resource(name = "themeSource")
	private ThemeSource themeSource;


	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		try
		{
			themeSource.getTheme(themeResolver.resolveThemeName(request));
			return true;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}
}
