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
package com.tisl.mpl.storefront.security;

import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.tisl.mpl.storefront.constants.ModelAttributetConstants;


public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{
	private BruteForceAttackCounter bruteForceAttackCounter;
	@Autowired
	private SessionService sessionService;
	private static final Logger LOG = Logger.getLogger(LoginAuthenticationFailureHandler.class);

	@SuppressWarnings("boxing")
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{
		/**
		 * AJAX handling for failed Authentication.
		 */
		int totalFailedLogins = 0;
		PrintWriter pw = null;
		final String xForwarded = request.getHeader("X-Requested-With");

		if (null != xForwarded && xForwarded.equalsIgnoreCase("XMLHttpRequest"))
		{
			response.setContentType("text/html");
			try
			{
				if (null != sessionService.getAttribute(ModelAttributetConstants.COUNT))
				{
					int unSuccessfulcount = sessionService.getAttribute(ModelAttributetConstants.COUNT);
					unSuccessfulcount = unSuccessfulcount + 1;
					sessionService.setAttribute(ModelAttributetConstants.COUNT, unSuccessfulcount);
					totalFailedLogins = sessionService.getAttribute(ModelAttributetConstants.COUNT);
				}
				else
				{
					sessionService.setAttribute(ModelAttributetConstants.COUNT, totalFailedLogins);
				}
				pw = response.getWriter();

				if (totalFailedLogins >= 5)
				{
					pw.print(ModelAttributetConstants.INVALID_CREDENTIAL_CAPTCHA);
				}
				else
				{
					pw.print(0);
				}
			}
			catch (final Exception e)
			{
				LOG.error(e);
			}
			finally
			{
				pw.close();
			}
		}
		else
		{
			if (null != sessionService.getAttribute(ModelAttributetConstants.COUNT))
			{
				int unSuccessfulcount = sessionService.getAttribute(ModelAttributetConstants.COUNT);
				unSuccessfulcount = unSuccessfulcount + 1;
				sessionService.setAttribute(ModelAttributetConstants.COUNT, unSuccessfulcount);
				sessionService.setAttribute(ModelAttributetConstants.ERROR_ATTEPT, unSuccessfulcount);
				totalFailedLogins = sessionService.getAttribute(ModelAttributetConstants.COUNT);
			}
			else
			{
				totalFailedLogins = totalFailedLogins + 1;
				sessionService.setAttribute(ModelAttributetConstants.COUNT, totalFailedLogins);
				sessionService.setAttribute(ModelAttributetConstants.ERROR_ATTEPT, totalFailedLogins);
			}
			bruteForceAttackCounter.registerLoginFailure(request.getParameter("j_username"));

			// Store the j_username in the session
			request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", request.getParameter("j_username"));
			super.onAuthenticationFailure(request, response, exception);
		}
	}

	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}
}
