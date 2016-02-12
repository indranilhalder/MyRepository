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
package com.tisl.mpl.storefront.util;

import de.hybris.platform.util.Config;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * A Spring MVC <code>HandlerInterceptor</code> which is responsible to enforce CSRF token validity on incoming posts
 * requests. The interceptor should be registered with Spring MVC servlet using the following syntax:
 *
 * <pre>
 *   &lt;mvc:interceptors&gt;
 *        &lt;bean class="com.eyallupu.blog.springmvc.controller.csrf.CSRFHandlerInterceptor"/&gt;
 *   &lt;/mvc:interceptors&gt;
 * </pre>
 *
 * @see CSRFRequestDataValueProcessor
 */
public class CSRFHandlerInterceptor extends HandlerInterceptorAdapter
{
	private final String CSRF_ALLOWED_URLS = "csrf.allowed.url.patterns";

	private static final Logger LOG = Logger.getLogger(CSRFHandlerInterceptor.class);

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{

		if (null != request.getServletPath())
		{
			final String path = request.getServletPath();
			if (path.contains("testhook") || path.contains("webhook"))
			{
				LOG.debug("NO CSRF check for webhook/testhook");
				return true;
			}
		}
		LOG.debug("request.getServletPath() ===> " + request.getServletPath());
		if (shouldCheckCSRFTokenForRequest(request))
		{
			// This is a POST request - need to check the CSRF token
			final String sessionToken = CSRFTokenManager.getTokenForSession(request.getSession());
			final String requestToken = CSRFTokenManager.getTokenFromRequest(request);
			if (sessionToken.equals(requestToken))
			{
				return true;
			}
			else
			{
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
				return false;
			}
		}
		else
		{
			{
				// Not a POST - allow the request
				return true;
			}
		}
	}

	protected boolean shouldCheckCSRFTokenForRequest(final HttpServletRequest request)
	{
		return ("POST").equalsIgnoreCase(request.getMethod()) && !isCSRFExemptUrl(request.getServletPath());
	}

	protected boolean isCSRFExemptUrl(final String servletPath)
	{
		if (servletPath != null)
		{
			final String allowedUrlPatterns = Config.getParameter(CSRF_ALLOWED_URLS);
			final Set<String> allowedUrls = StringUtils.commaDelimitedListToSet(allowedUrlPatterns);
			LOG.debug("allowedUrls " + allowedUrls);
			for (final String pattern : allowedUrls)
			{
				if (servletPath.matches(pattern))
				{
					return true;
				}
			}
		}
		return false;
	}
}
