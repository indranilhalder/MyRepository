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
package com.tisl.mpl.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tisl.mpl.context.ContextInformationLoader;
import com.tisl.mpl.exceptions.InvalidResourceException;


/**
 * Request filter that resolves base site uid from restful request mapping, i.e<br>
 * <i>/rest/v1/mysite/cart</i>, or <br>
 * <i>/rest/v1/mysite/customers/current</i><br>
 * would try to set base site with uid=mysite as a current site.<br>
 *
 * One should define the path which is expected to be before the site resource in the project properties file (
 * <b>commercewebservices.rootcontext</b>).<br>
 * Default and fallback value equals to <i>/rest/v1/</i><br>
 *
 * The method uses also a comma separated list of url special characters that are used to parse the site id resource.
 * You can reconfigure it in properties file (<b>commercewebservices.url.special.characters</b>). The default and
 * fallback value is equal to <i>"?,/</i>".
 *
 * Filter will throw {@link ServletException} if it fails to find the site which is in the resource url.<br>
 * However, you can configure exceptions that doesnt require the site mapping in the resource path. You can configure
 * them in a spring bean called 'baseFilterResourceExceptions'.<br>
 *
 * @author KKW
 *
 */
public class BaseSiteFilter extends OncePerRequestFilter
{
	//	private static final Logger LOG = Logger.getLogger(BaseSiteFilter.class);

	private ContextInformationLoader contextInformationLoader;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		processRequest(request);
		filterChain.doFilter(request, response);
	}

	protected void processRequest(final HttpServletRequest httpRequest) throws InvalidResourceException
	{
		getContextInformationLoader().initializeSiteFromRequest(httpRequest);
	}

	public ContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

	@Required
	public void setContextInformationLoader(final ContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}
}
