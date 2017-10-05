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
package com.tisl.mpl.storefront.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.storefront.security.cookie.PDPPincodeCookieGenerator;


public class PDPPincodeRestorationFilter extends OncePerRequestFilter
{

	private PDPPincodeCookieGenerator pdpPincodeCookieGenerator;
	private MplProductFacade mplProductFacade;

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		//TPR-6654
		if (getMplProductFacade().getPDPPincodeSession() == null)
		{
			final Cookie[] cookies = request.getCookies();
			if (cookies != null)
			{
				for (final Cookie cookie : cookies)
				{
					if (getPdpPincodeCookieGenerator().getCookieName().equals(cookie.getName()))
					{
						getMplProductFacade().setPDPPincodeSession(cookie.getValue());
						break;
					}

				}
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * @return the pdpPincodeCookieGenerator
	 */
	public PDPPincodeCookieGenerator getPdpPincodeCookieGenerator()
	{
		return pdpPincodeCookieGenerator;
	}

	/**
	 * @param pdpPincodeCookieGenerator
	 *           the pdpPincodeCookieGenerator to set
	 */
	@Required
	public void setPdpPincodeCookieGenerator(final PDPPincodeCookieGenerator pdpPincodeCookieGenerator)
	{
		this.pdpPincodeCookieGenerator = pdpPincodeCookieGenerator;
	}

	/**
	 * @return the mplProductFacade
	 */
	public MplProductFacade getMplProductFacade()
	{
		return mplProductFacade;
	}

	/**
	 * @param mplProductFacade
	 *           the mplProductFacade to set
	 */
	@Required
	public void setMplProductFacade(final MplProductFacade mplProductFacade)
	{
		this.mplProductFacade = mplProductFacade;
	}
}
