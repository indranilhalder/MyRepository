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
package com.tisl.mpl.storefront.security.cookie;


import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.CookieGenerator;

import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;


/**
 * Enhanced {@link CookieGenerator} sets additionally header attribute {@value #HEADER_COOKIE}
 */
public class EnhancedCookieGenerator extends CookieGenerator
{
	public static final String HEADER_COOKIE = "Set-Cookie";
	public static final boolean DEFAULT_HTTP_ONLY = false;
	public static final boolean DEFAULT_COOKIE_PATH = true;

	private boolean useDefaultPath = DEFAULT_COOKIE_PATH;
	private boolean httpOnly = DEFAULT_HTTP_ONLY;
	private boolean useDefaultDomain = true;

	/**
	 * @param useDefaultDomain
	 *           the useDefaultDomain to set
	 */
	public void setUseDefaultDomain(final boolean useDefaultDomain)
	{
		this.useDefaultDomain = useDefaultDomain;
	}

	/**
	 * @return the customDomain
	 */
	public String getCustomDomain()
	{
		return customDomain;
	}

	/**
	 * @param customDomain
	 *           the customDomain to set
	 */
	public void setCustomDomain(final String customDomain)
	{
		this.customDomain = customDomain;
	}

	/**
	 * @return the useDefaultDomain
	 */
	public boolean isUseDefaultDomain()
	{
		return useDefaultDomain;
	}

	private String customDomain;

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(ModelAttributetConstants.CONFIGURATION_SERVICE, ConfigurationService.class);
	}

	protected boolean isHttpOnly()
	{
		return httpOnly;
	}

	/**
	 * Marker to choose between only cookie based session and http header as addition
	 */
	public void setHttpOnly(final boolean httpOnly)
	{
		this.httpOnly = httpOnly;
	}

	protected boolean canUseDefaultPath()
	{
		return useDefaultPath;
	}

	/**
	 * Adjusts either dynamic {@link Cookie#setPath(String)} or static assignment. If true a cookie path is calculated by
	 * {@link #setEnhancedCookiePath(Cookie)} method.
	 */
	public void setUseDefaultPath(final boolean useDefaultPath)
	{
		this.useDefaultPath = useDefaultPath;
	}

	@Override
	public void addCookie(final HttpServletResponse response, final String cookieValue)
	{
		super.addCookie(new HttpServletResponseWrapper(response)
		{
			@Override
			public void addCookie(final Cookie cookie)
			{
				setEnhancedCookiePath(cookie);
				setCustomDomain(cookie);
				setEnhancedCookie(cookie);
				cookie.setPath("/"); //TISPT-307

				if (isHttpOnly())
				{
					// Custom code to write the cookie including the httpOnly flag
					final StringBuffer headerBuffer = new StringBuffer(100);
					ServerCookie.appendCookieValue(headerBuffer, cookie.getVersion(), cookie.getName(), cookie.getValue(),
							cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(), true);
					response.addHeader(HEADER_COOKIE, headerBuffer.toString());
				}
				else
				{
					// Write the cookie as normal
					super.addCookie(cookie);
				}
			}
		}, cookieValue);
	}

	/**
	 * Sets dynamically the {@link Cookie#setPath(String)} value using available
	 * {@link HttpServletRequest#getContextPath()}.
	 */
	protected void setEnhancedCookiePath(final Cookie cookie)
	{
		if (!canUseDefaultPath())
		{
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			cookie.setPath(request.getContextPath());
		}
	}

	/**
	 *
	 * Sets dynamically the {@link Cookie#setPath(String)} value using available
	 * {@link HttpServletRequest#getContextPath()}.
	 */
	protected void setEnhancedCookie(final Cookie cookie)
	{
		final int str = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				MessageConstants.LOGIN_COOKIE_EXPIRY_DAY));
		final int expiryTime = 60 * 60 * 24 * str; // 24h in seconds
		cookie.setMaxAge(expiryTime);
	}

	/**
	 * @param cookie
	 */
	protected void setCustomDomain(final Cookie cookie)
	{
		// If cookie doesnot use the default domain
		if (!isUseDefaultDomain() && StringUtils.isNotEmpty(getCustomDomain()))
		{
			cookie.setDomain(getCustomDomain());
		}

	}
}
