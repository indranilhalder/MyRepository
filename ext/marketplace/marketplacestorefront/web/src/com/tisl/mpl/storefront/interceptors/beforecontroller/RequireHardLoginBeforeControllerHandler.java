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
package com.tisl.mpl.storefront.interceptors.beforecontroller;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.CookieGenerator;

import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.interceptors.BeforeControllerHandler;


/**
 */
public class RequireHardLoginBeforeControllerHandler implements BeforeControllerHandler
{
	private static final Logger LOG = Logger.getLogger(RequireHardLoginBeforeControllerHandler.class);

	public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";

	private String loginUrl;
	private String luxuryLoginUrl;
	private String loginAndCheckoutUrl;
	private String luxuryLoginAndCheckoutUrl;
	private RedirectStrategy redirectStrategy;
	private CookieGenerator cookieGenerator;
	private UserService userService;
	private SessionService sessionService;
	private CartService cartService;
	@Autowired
	private CommonUtils commonUtils;

	protected String getLoginUrl()
	{
		return loginUrl;
	}

	@Required
	public void setLoginUrl(final String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	public String getLuxuryLoginAndCheckoutUrl()
	{
		return luxuryLoginAndCheckoutUrl;
	}

	@Required
	public void setLuxuryLoginAndCheckoutUrl(final String luxuryLoginAndCheckoutUrl)
	{
		this.luxuryLoginAndCheckoutUrl = luxuryLoginAndCheckoutUrl;
	}

	protected String getLuxuryLoginUrl()
	{
		return luxuryLoginUrl;
	}

	@Required
	public void setLuxuryLoginUrl(final String luxuryLoginUrl)
	{
		this.luxuryLoginUrl = luxuryLoginUrl;
	}

	protected RedirectStrategy getRedirectStrategy()
	{
		return redirectStrategy;
	}

	@Required
	public void setRedirectStrategy(final RedirectStrategy redirectStrategy)
	{
		this.redirectStrategy = redirectStrategy;
	}

	protected CookieGenerator getCookieGenerator()
	{
		return cookieGenerator;
	}

	@Required
	public void setCookieGenerator(final CookieGenerator cookieGenerator)
	{
		this.cookieGenerator = cookieGenerator;
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

	public String getLoginAndCheckoutUrl()
	{
		return loginAndCheckoutUrl;
	}

	@Required
	public void setLoginAndCheckoutUrl(final String loginAndCheckoutUrl)
	{
		this.loginAndCheckoutUrl = loginAndCheckoutUrl;
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

	public CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		try
		{
			// We only care if the request is secure
			//			if (request.isSecure())
			//			{
			//				// Check if the handler has our annotation
			//				final RequireHardLogIn annotation = findAnnotation(handler, RequireHardLogIn.class);
			//				if (annotation != null)
			//				{
			//					final String guid = (String) request.getSession().getAttribute(SECURE_GUID_SESSION_KEY);
			//					boolean redirect = true;
			//
			//					if (((!getUserService().isAnonymousUser(getUserService().getCurrentUser()) || checkForAnonymousCheckout()) && checkForGUIDCookie(
			//							request, response, guid)))
			//					{
			//						redirect = false;
			//					}
			//
			//					if (redirect)
			//					{
			//						LOG.warn((guid == null ? "missing secure token in session" : "no matching guid cookie") + ", redirecting");
			//						getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(request));
			//						return false;
			//					}
			//				}
			//			}
			//Deeply nested if..then statements are hard to read
			if (null != findAnnotation(handler, RequireHardLogIn.class))
			{
				final String guid = (String) request.getSession().getAttribute(SECURE_GUID_SESSION_KEY);
				if (!(((!getUserService().isAnonymousUser(getUserService().getCurrentUser()) || checkForAnonymousCheckout()) && checkForGUIDCookie(
						request, response, guid))))
				{
					boolean redirect = true;
					if (keepLoginAlive(request))
					{
						LOG.info("Keep Alive cookie is active.. Hence not redirecting....");

					}
					else
					{
						LOG.warn((guid == null ? "missing secure token in session" : "no matching guid cookie") + ", redirecting");
						getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(request));
						redirect = false;
					}

					return redirect;
				}
			}
			return true;
		}
		catch (final IOException e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}


	protected boolean checkForGUIDCookie(final HttpServletRequest request, final HttpServletResponse response, final String guid)
	{////Deeply nested if..then statements are hard to read
		//		if (guid != null && request.getCookies() != null)
	 //		{
	 //			final String guidCookieName = getCookieGenerator().getCookieName();
	 //			if (guidCookieName != null)
	 //			{
	 //				for (final Cookie cookie : request.getCookies())
	 //				{
	 //					if (guidCookieName.equals(cookie.getName()))
	 //					{
	 //						if (guid.equals(cookie.getValue()))
	 //						{
	 //							return true;
	 //						}
	 //						else
	 //						{
	 //							LOG.info("Found secure cookie with invalid value. expected [" + guid + "] actual [" + cookie.getValue()
	 //									+ "]. removing.");
	 //							getCookieGenerator().removeCookie(response);
	 //						}
	 //					}
	 //				}
	 //			}
	 //		}


		if (guid != null && request.getCookies() != null && getCookieGenerator().getCookieName() != null)
		{
			final String guidCookieName = getCookieGenerator().getCookieName();
			for (final Cookie cookie : request.getCookies())
			{
				if (guidCookieName.equals(cookie.getName()))
				{
					if (guid.equals(cookie.getValue()))
					{
						return true;
					}
					else
					{
						LOG.info("Found secure cookie with invalid value. expected [" + guid + "] actual [" + cookie.getValue()
								+ "]. removing.");
						getCookieGenerator().removeCookie(response);
					}
				}
			}
		}
		return false;
	}

	protected boolean checkForAnonymousCheckout()
	{
		if (Boolean.TRUE.equals(getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT)))
		{
			if (getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID) == null)
			{
				getSessionService().setAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID,
						StringUtils.substringBefore(getCartService().getSessionCart().getUser().getUid(), "|"));
			}
			return true;
		}
		return false;
	}

	protected String getRedirectUrl(final HttpServletRequest request)
	{
		if (commonUtils.isLuxurySite() && request != null && !request.getServletPath().contains("checkout"))
		{
			return getLuxuryLoginUrl();
		}
		else if (commonUtils.isLuxurySite() && request != null && request.getServletPath().contains("checkout"))
		{
			return getLuxuryLoginAndCheckoutUrl();
		}

		if (request != null && request.getServletPath().contains("checkout"))
		{
			return getLoginAndCheckoutUrl();
		}
		else
		{
			return getLoginUrl();
		}
	}

	protected <T extends Annotation> T findAnnotation(final HandlerMethod handlerMethod, final Class<T> annotationType)
	{
		// Search for method level annotation
		final T annotation = handlerMethod.getMethodAnnotation(annotationType);
		if (annotation != null)
		{
			return annotation;
		}

		// Search for class level annotation
		return AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), annotationType);
	}

	//POC Check if the keep alive cookie is present or not
	protected boolean keepLoginAlive(final HttpServletRequest request)
	{
		boolean keepLoginAlive = false;
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (final Cookie cookie : request.getCookies())
			{
				if (cookie.getName().equals("keepAlive")
						&& isKeepAliveValueSameWithJsessionId(cookie.getValue(), request.getSession().getId()))
				{
					keepLoginAlive = true;
					LOG.info("Found KEEP ALIVE cookie......");
					break;
				}

			}
		}
		return keepLoginAlive; //Sonar fix
	}

	private boolean isKeepAliveValueSameWithJsessionId(final String keepAliveEncodedCookievalue, final String sessionId)
	{
		try
		{
			if (keepAliveEncodedCookievalue != null && sessionId != null)
			{
				final String keepAliveDecodedValue = new String(Base64.decodeBase64(keepAliveEncodedCookievalue));
				if (keepAliveDecodedValue != null && sessionId.equalsIgnoreCase(keepAliveDecodedValue))
				{
					return true;
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception in keepAliveValueSamewithJsessionId()" + e);
		}
		return false;
	}
}
