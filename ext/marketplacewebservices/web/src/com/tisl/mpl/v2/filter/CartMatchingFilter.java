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
package com.tisl.mpl.v2.filter;

import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * Filter that puts cart from the requested url into the session.
 */
public class CartMatchingFilter extends AbstractUrlMatchingFilter
{
	private String regexp;
	private CartLoaderStrategy cartLoaderStrategy;
	private static final String GETTOPTWOWISHLIST = "getTopTwoWishlistForUser";
	private static final String SOFTCARTRESERVATIONFORPAYMENT = "softReservationForPayment";
	private static final String APPLYCOUPON = "applyCoupons";
	private static final String APPLYCARTCOUPON = "applyCartCoupons";
	private static final String RELEASECOUPON = "releaseCoupons";
	private static final String RELEASECARTCOUPON = "releaseCartCoupons";
	private static final String ORDER_SUMMARY = "displayOrderSummary";
	private static final String RESEND_OTP_COD = "resendOtpforcod";
	@Autowired
	private UserService userService;

	private static final Logger LOG = Logger.getLogger(CartMatchingFilter.class);

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("CartMatchingFilter Step 1 >>  Request path URI " + request.getRequestURI() + " Path info "
					+ request.getPathInfo());
		}

		if (matchesUrl(request, regexp) && null != getValue(request, regexp)
				&& !request.getRequestURI().contains(GETTOPTWOWISHLIST)
				&& !request.getRequestURI().contains(SOFTCARTRESERVATIONFORPAYMENT) && !request.getRequestURI().contains(APPLYCOUPON)
				&& !request.getRequestURI().contains(RELEASECARTCOUPON) && !request.getRequestURI().contains(APPLYCARTCOUPON)
				&& !request.getRequestURI().contains(RELEASECOUPON) && !request.getRequestURI().contains(ORDER_SUMMARY)
				&& !request.getRequestURI().contains(RESEND_OTP_COD))
		{

			final String cartId = getValue(request, regexp);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(" CartMatchingFilter Step 2 >> Cart Id  " + cartId);
			}
			if (getUserService().getCurrentUser() != null && getUserService().getCurrentUser().getUid() != null)
			{
				LOG.debug(" CartMatchingFilter Step 3 >> user Id  " + getUserService().getCurrentUser().getUid());
			}
			cartLoaderStrategy.loadCart(cartId);
		}

		filterChain.doFilter(request, response);
	}

	protected String getRegexp()
	{
		return regexp;
	}

	@Required
	public void setRegexp(final String regexp)
	{
		this.regexp = regexp;
	}

	public CartLoaderStrategy getCartLoaderStrategy()
	{
		return cartLoaderStrategy;
	}

	@Required
	public void setCartLoaderStrategy(final CartLoaderStrategy cartLoaderStrategy)
	{
		this.cartLoaderStrategy = cartLoaderStrategy;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
