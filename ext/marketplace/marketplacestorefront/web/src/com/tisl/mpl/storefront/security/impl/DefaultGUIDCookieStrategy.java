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
package com.tisl.mpl.storefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.web.util.CookieGenerator;

import com.tisl.mpl.storefront.interceptors.beforecontroller.RequireHardLoginBeforeControllerHandler;
import com.tisl.mpl.storefront.security.cookie.KeepAliveCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.LuxuryEmailCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.LuxuryUserCookieGenerator;


/**
 * Default implementation of {@link GUIDCookieStrategy}
 */
public class DefaultGUIDCookieStrategy implements GUIDCookieStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultGUIDCookieStrategy.class);

	private final SecureRandom random;
	private final MessageDigest sha;

	private CookieGenerator cookieGenerator;

	private KeepAliveCookieGenerator keepAliveCookieGenerator;

	private LuxuryEmailCookieGenerator luxuryEmailCookieGenerator;

	private UserService userService;

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

	/**
	 * @return the luxuryEmailCookieGenerator
	 */
	public LuxuryEmailCookieGenerator getLuxuryEmailCookieGenerator()
	{
		return luxuryEmailCookieGenerator;
	}

	/**
	 * @param luxuryEmailCookieGenerator
	 *           the luxuryEmailCookieGenerator to set
	 */
	public void setLuxuryEmailCookieGenerator(final LuxuryEmailCookieGenerator luxuryEmailCookieGenerator)
	{
		this.luxuryEmailCookieGenerator = luxuryEmailCookieGenerator;
	}

	/**
	 * @return the luxuryUserCookieGenerator
	 */
	public LuxuryUserCookieGenerator getLuxuryUserCookieGenerator()
	{
		return luxuryUserCookieGenerator;
	}

	/**
	 * @param luxuryUserCookieGenerator
	 *           the luxuryUserCookieGenerator to set
	 */
	public void setLuxuryUserCookieGenerator(final LuxuryUserCookieGenerator luxuryUserCookieGenerator)
	{
		this.luxuryUserCookieGenerator = luxuryUserCookieGenerator;
	}

	private LuxuryUserCookieGenerator luxuryUserCookieGenerator;

	/**
	 * @return the keepAliveCookieGenerator
	 */
	public KeepAliveCookieGenerator getKeepAliveCookieGenerator()
	{
		return keepAliveCookieGenerator;
	}

	/**
	 * @param keepAliveCookieGenerator
	 *           the keepAliveCookieGenerator to set
	 */
	public void setKeepAliveCookieGenerator(final KeepAliveCookieGenerator keepAliveCookieGenerator)
	{
		this.keepAliveCookieGenerator = keepAliveCookieGenerator;
	}

	public DefaultGUIDCookieStrategy() throws NoSuchAlgorithmException
	{
		random = SecureRandom.getInstance("SHA1PRNG");
		sha = MessageDigest.getInstance("SHA-1");
		Assert.notNull(random);
		Assert.notNull(sha);
	}

	@Override
	public void setCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		//Commenting code, due to secure requests received only.
		/*
		 * if (!request.isSecure()) { // We must not generate the cookie for insecure requests, otherwise there is not
		 * point doing this at all throw new IllegalStateException("Cannot set GUIDCookie on an insecure request!"); }
		 */

		final String guid = createGUID();

		getCookieGenerator().addCookie(response, guid);
		//POC Add the Keep Alive Cookie on login
		getKeepAliveCookieGenerator().addCookie(response, createGUID());

		//Set the luxury site cookies
		if (userService.getCurrentUser() != null)
		{
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			if (StringUtils.isNotEmpty(customer.getOriginalUid()))
			{
				LOG.info("Adding cookie for luxury cookie");
				getLuxuryEmailCookieGenerator().addCookie(response, customer.getOriginalUid());
			}
		}
		//To be added
		getLuxuryUserCookieGenerator().addCookie(response, null);
		request.getSession().setAttribute(RequireHardLoginBeforeControllerHandler.SECURE_GUID_SESSION_KEY, guid);

		if (LOG.isInfoEnabled())
		{
			LOG.info("Setting guid cookie and session attribute: " + guid);
		}
	}

	@Override
	public void deleteCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		/*
		 * if (!request.isSecure()) { LOG.error(
		 * "Cannot remove secure GUIDCookie during an insecure request. I should have been called from a secure page."); }
		 * else {
		 */
		// Its a secure page, we can delete the cookie
		getCookieGenerator().removeCookie(response);
		//Delete the Keep alive cookie
		getKeepAliveCookieGenerator().removeCookie(response);
		getLuxuryUserCookieGenerator().removeCookie(response);
		getLuxuryEmailCookieGenerator().removeCookie(response);
		//}
	}

	protected String createGUID()
	{
		final String randomNum = String.valueOf(getRandom().nextInt());
		final byte[] result = getSha().digest(randomNum.getBytes());
		return String.valueOf(Hex.encodeHex(result));
	}

	protected CookieGenerator getCookieGenerator()
	{
		return cookieGenerator;
	}

	/**
	 * @param cookieGenerator
	 *           the cookieGenerator to set
	 */
	@Required
	public void setCookieGenerator(final CookieGenerator cookieGenerator)
	{
		this.cookieGenerator = cookieGenerator;
	}


	protected SecureRandom getRandom()
	{
		return random;
	}

	protected MessageDigest getSha()
	{
		return sha;
	}
}
