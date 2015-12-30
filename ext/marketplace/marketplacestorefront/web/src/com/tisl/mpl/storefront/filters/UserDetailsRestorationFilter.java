/**
 *
 */
package com.tisl.mpl.storefront.filters;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tisl.mpl.storefront.security.cookie.UserCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.UserTypeCookieGenerator;


/**
 * @author TCS
 *
 */
public class UserDetailsRestorationFilter extends OncePerRequestFilter
{
	private UserCookieGenerator userCookieGenerator;

	/**
	 * @return the userCookieGenerator
	 */
	public UserCookieGenerator getUserCookieGenerator()
	{
		return userCookieGenerator;
	}

	/**
	 * @param userCookieGenerator
	 *           the userCookieGenerator to set
	 */
	public void setUserCookieGenerator(final UserCookieGenerator userCookieGenerator)
	{
		this.userCookieGenerator = userCookieGenerator;
	}

	/**
	 * @return the userTypeCookieGenerator
	 */
	public UserTypeCookieGenerator getUserTypeCookieGenerator()
	{
		return userTypeCookieGenerator;
	}

	/**
	 * @param userTypeCookieGenerator
	 *           the userTypeCookieGenerator to set
	 */
	public void setUserTypeCookieGenerator(final UserTypeCookieGenerator userTypeCookieGenerator)
	{
		this.userTypeCookieGenerator = userTypeCookieGenerator;
	}

	private UserTypeCookieGenerator userTypeCookieGenerator;
	private UserService userService;
	private SessionService sessionService;
	private final String ANONYMOUS = "anonymous";
	private final String REGISTERED = "registered";
	private static final Logger LOG = Logger.getLogger(UserDetailsRestorationFilter.class.getName());

	private ConfigurationService configurationService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException
	{
		String userId = null;
		String userType = null;
		boolean userCookieSet = false;
		boolean userTypeCookieSet = false;
		final UserModel currentUser = getUserService().getCurrentUser();
		final CustomerModel currCust = (CustomerModel) getUserService().getCurrentUser();
		final String isIAEnabled = getConfigurationService().getConfiguration().getString("ia.enabled");
		request.setAttribute("isIAEnabled", isIAEnabled);
		final String rootEPForHttp = getConfigurationService().getConfiguration().getString("ia.rootEP.http");
		request.setAttribute("rootEPForHttp", rootEPForHttp);
		final String rootEPForHttps = getConfigurationService().getConfiguration().getString("ia.rootEP.https");
		request.setAttribute("rootEPForHttps", rootEPForHttps);
		final String ecompanyForIA = getConfigurationService().getConfiguration().getString("ia.ecompany");
		request.setAttribute("ecompanyForIA", ecompanyForIA);
		final String DamMediaHost = getConfigurationService().getConfiguration().getString("media.dammedia.host");
		request.setAttribute("DamMediaHost", DamMediaHost);
		final String mplStaticResourceHost = getConfigurationService().getConfiguration()
				.getString("marketplace.static.resource.host");
		request.setAttribute("mplStaticResourceHost", mplStaticResourceHost);

		if (request.getCookies() != null)
		{
			final String userCookieName = getUserCookieGenerator().getCookieName();
			final String userTypeCookieName = getUserTypeCookieGenerator().getCookieName();
			for (final Cookie cookie : request.getCookies())
			{
				if (userCookieName.equals(cookie.getName()))
				{
					userId = currentUser.getUid();
					cookie.setValue(userId);
					cookie.setMaxAge(60 * 60 * 24);// setting 1 day life time for cookies
					getUserCookieGenerator().addCookie(response, userId);
					userCookieSet = true;
				}

				if (userTypeCookieName.equals(cookie.getName()))
				{
					if (getUserService().isAnonymousUser(currentUser))
					{
						userType = ANONYMOUS;
					}
					else
					{
						if (null != currCust && null != currCust.getType())
						{
							userType = currCust.getType().toString();
						}
						else
						{
							userType = "REGISTERED";
						}
					}
					cookie.setValue(userType);
					cookie.setMaxAge(60 * 60 * 24);// setting 1 day life time for cookies
					getUserTypeCookieGenerator().addCookie(response, userType);
					userTypeCookieSet = true;
				}
			}
		}


		if (!userTypeCookieSet && !userCookieSet)
		{
			LOG.info("generating new Cookies");
			getUserCookieGenerator().addCookie(response, currentUser.getUid());
			if (getUserService().isAnonymousUser(currentUser))
			{
				userType = ANONYMOUS;
			}
			else
			{
				userType = REGISTERED;
			}
			getUserTypeCookieGenerator().addCookie(response, userType);
		}

		filterChain.doFilter(request, response);
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}






	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


}

