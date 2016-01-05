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
package com.tisl.mpl.oauth2;

import de.hybris.platform.core.model.user.UserModel;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;


/**
 * Filter sets current user by userService depending on current principal. <br>
 * This should happen only when there is a customer context. Anonymous credentials are also applicable, because special
 * 'anonymous' user is available for that purpose. Customer context is not available during client credential flow.
 *
 * @author hansa
 *
 */
public class HybrisOauth2UserFilter implements Filter
{
	private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	private static final String ROLE_CUSTOMERGROUP = "ROLE_CUSTOMERGROUP";
	private static final String ROLE_CUSTOMERMANAGERGROUP = "ROLE_CUSTOMERMANAGERGROUP";

	@Autowired
	private ExtendedUserServiceImpl userexService;


	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (containsRole(auth, ROLE_ANONYMOUS) || containsRole(auth, ROLE_CUSTOMERGROUP)
				|| containsRole(auth, ROLE_CUSTOMERMANAGERGROUP))
		{
			final UserModel userModel = userexService.getUserForUID((String) auth.getPrincipal());
			userexService.setCurrentUser(userModel);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
		// YTODO Auto-generated method stub
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException
	{
		// YTODO Auto-generated method stub
	}

	private static boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}
}
