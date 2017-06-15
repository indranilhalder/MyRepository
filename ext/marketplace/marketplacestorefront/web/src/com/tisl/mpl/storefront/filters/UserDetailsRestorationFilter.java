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

import com.tisl.mpl.storefront.constants.MessageConstants;
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
	//private final String ANONYMOUS = "anonymous";
	//private final String REGISTERED = "registered";
	//changes done as per IA requirement
	private final String ANONYMOUS = "session";
	private final String REGISTERED = "site_user";
	private final String FACEBOOKUSER = "facebook";
	private final String FACEBOOK_LOGIN = "FACEBOOK_LOGIN";
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
		String userLoginType = null; //TPR-668
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
		final String mplStaticResourceHost = getConfigurationService().getConfiguration().getString(
				"marketplace.static.resource.host");
		request.setAttribute("mplStaticResourceHost", mplStaticResourceHost);

		if (request.getCookies() != null)
		{
			LOG.info("logged in status " + currentUser.getUid());
			final String userCookieName = getUserCookieGenerator().getCookieName();
			final String userTypeCookieName = getUserTypeCookieGenerator().getCookieName();
			for (final Cookie cookie : request.getCookies())
			{
				if (userCookieName.equals(cookie.getName()))
				{
					LOG.info("inside mpl user " + cookie.getDomain());
					userId = currentUser.getUid();
					cookie.setValue(userId);
					cookie.setMaxAge(60 * 60 * 24);// setting 1 day life time for cookies
					//SISA FIX
					cookie.setSecure(true);
					getUserCookieGenerator().addCookie(response, userId);
					userCookieSet = true;
				}

				if (userTypeCookieName.equals(cookie.getName()))
				{
					LOG.info("inside mpl user  type " + cookie.getDomain());
					if (getUserService().isAnonymousUser(currentUser))
					{
						userType = ANONYMOUS;
						userLoginType = MessageConstants.GUESTUSER;
					}
					else
					{
						if (null != currCust && null != currCust.getType())
						{
							if (currCust.getType().toString().equals(FACEBOOK_LOGIN))
							{
								userType = FACEBOOKUSER;
								userLoginType = FACEBOOKUSER;
							}
							else if (currCust.getType().toString().equals(MessageConstants.GOOGLE_LOGIN)) // TPR-668
							{
								userLoginType = MessageConstants.GOOGLEUSER;
							}
							else
							{
								userType = REGISTERED;
								userLoginType = MessageConstants.EMAILUSER;
							}
						}
					}
					cookie.setValue(userType);
					cookie.setMaxAge(60 * 60 * 24);// setting 1 day life time for cookies
					//SISA FIX
					cookie.setSecure(true);
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
		request.setAttribute(MessageConstants.USER_LOGIN_TYPE, userLoginType);
		updateKeepAliveCookie(request, response);
		LOG.info("after updateKeepAlive Method :value of userlogintype  in request is " + request.getAttribute(userLoginType));

		filterChain.doFilter(request, response);
		LOG.info("after filter chain  Method : value in userlogintype request is " + request.getAttribute(userLoginType));
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


	/**
	 * @param request
	 * @param response
	 */
	private void updateKeepAliveCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		LOG.info("For any request coming we should update the Keep Alive cookie if present");
		final String sessionTimeout = getConfigurationService().getConfiguration().getString("default.session.timeout");
		/** Added for UF-93 **/
		int sessionTimeoutvalue;
		String rememberMe = "";
		if (request != null && request.getSession() != null)
		{
			rememberMe = (String) request.getSession().getAttribute("rememberMe");
		}
		if ("true".equalsIgnoreCase(rememberMe))
		{
			final String rememberMesessionTimeout = getConfigurationService().getConfiguration().getString(
					"rememberMe.session.timeout");
			sessionTimeoutvalue = (Integer.valueOf(rememberMesessionTimeout)).intValue();
		}
		else
		{
			sessionTimeoutvalue = (Integer.valueOf(sessionTimeout)).intValue();
		}
		/** End for UF-93 **/
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals("keepAlive"))
				{
					LOG.info("Found the Keep Alive Cookie. Hence adding back to response with new expiry timeout");
					LOG.info("Cookie domain :::" + cookie.getDomain());
					cookie.setMaxAge(sessionTimeoutvalue);
					cookie.setPath("/");
					//cookie.setSecure(true);
					cookie.setDomain(getConfigurationService().getConfiguration().getString("shared.cookies.domain"));
					response.addCookie(cookie);
					request.getSession().setMaxInactiveInterval(sessionTimeoutvalue); // UF-93 Added to set the session to same interval as KeepAlive Cookie
					LOG.error("UserDetailsRestorationFilterRemember.updateKeepAliveCookie() - RememberMe is : "
							+ request.getSession().getAttribute("rememberMe") + " Keepalive : " + sessionTimeoutvalue / 60
							+ "cookie.getName: " + cookie.getName() + "cookie.getMaxAge(): " + cookie.getMaxAge() + " cookie.getDomain"
							+ cookie.getDomain() + "SessionId: " + request.getSession().getId() + " SessionTimeout : "
							+ request.getSession().getMaxInactiveInterval() / 60 + "min");
				}
			}
		}

	}
}
