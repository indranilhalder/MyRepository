/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tul.mpl.controller.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tul.mpl.exception.EtailBusinessExceptions;
import com.tul.mpl.exception.EtailNonBusinessExceptions;
import com.tul.mpl.storefront.constants.ControllerConstants;
import com.tul.mpl.storefront.constants.MessageConstants;
import com.tul.mpl.storefront.constants.ModelAttributetConstants;
import com.tul.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tul.mpl.util.ExceptionUtil;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_LOGIN)
public class LoginPageController
{
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ConfigurationService configurationService;
	private HttpSessionRequestCache httpSessionRequestCache;
	private static final Logger LOG = Logger.getLogger(LoginPageController.class);


	/**
	 * @description this method is called for load default login page
	 * @return String
	 */
	protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		String returnPage = null;
		try
		{


			returnPage = getView();
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			returnPage = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnPage = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			returnPage = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
		}
		return returnPage;
	}

	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Resource(name = ModelAttributetConstants.HTTP_SESSION_REQUEST_CACHE)
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}


	public boolean isUserLoggedIn()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && !authentication.getName().equalsIgnoreCase(ModelAttributetConstants.ANONYMOUS)
				&& !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, RequestMappingUrlConstants.LINK_LOGIN))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}





}
