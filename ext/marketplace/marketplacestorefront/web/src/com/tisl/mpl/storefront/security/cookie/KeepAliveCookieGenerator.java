/**
 *
 */
package com.tisl.mpl.storefront.security.cookie;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;


/**
 * @author 765463
 *
 */
public class KeepAliveCookieGenerator extends EnhancedCookieGenerator
{
	private static final Logger LOG = Logger.getLogger(KeepAliveCookieGenerator.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	protected Cookie createCookie(final String cookieValue)
	{
		final Cookie cookie = new Cookie(getCookieName(), cookieValue);
		if (getCookieDomain() != null)
		{
			LOG.error("here Setting cookie with name:::" + getCookieName() + "with domain :::" + getCookieDomain());
			cookie.setDomain(getCookieDomain());
		}
		cookie.setPath(getCookiePath());
		return cookie;
	}

	@Override
	protected void setEnhancedCookie(final Cookie cookie)
	{
		//sonar fix
		//		final int str = Integer.parseInt(configurationService.getConfiguration()
		//				.getString(MessageConstants.LOGIN_COOKIE_EXPIRY_DAY));
		//		final int expiryTime = 60 * 60 * 24 * str; // 24h in seconds
		cookie.setMaxAge(Integer.parseInt(configurationService.getConfiguration().getString("default.session.timeout")));
		LOG.error("here Setting cookie age::: shared.cookies.domain");
	}

	@Override
	protected void setCustomDomain(final Cookie cookie)
	{
		// If cookie doesnot use the default domain
		//		if (!isUseDefaultDomain() && StringUtils.isNotEmpty(getCustomDomain()))
		//		{
		cookie.setDomain(configurationService.getConfiguration().getString(DOMAIN));
		LOG.error("here Setting cookie domain::: shared.cookies.domain");
		//		}

	}


}
