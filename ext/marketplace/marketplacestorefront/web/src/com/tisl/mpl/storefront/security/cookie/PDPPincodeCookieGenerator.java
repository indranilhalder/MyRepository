package com.tisl.mpl.storefront.security.cookie;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class PDPPincodeCookieGenerator extends EnhancedCookieGenerator
{
	private static final Logger LOG = Logger.getLogger(PDPPincodeCookieGenerator.class);

	@Override
	protected Cookie createCookie(final String cookieValue)
	{
		final Cookie cookie = new Cookie(getCookieName(), cookieValue);
		if (getCookieDomain() != null)
		{
			LOG.info("Setting cookie with name:::" + getCookieName() + "with domain :::" + getCookieDomain());
			cookie.setDomain(getCookieDomain());
		}
		cookie.setPath(getCookiePath());
		return cookie;
	}

	@SuppressWarnings("boxing")
	@Override
	protected void setEnhancedCookie(final Cookie cookie)
	{
		cookie.setMaxAge(getCookieMaxAge());
	}
}
