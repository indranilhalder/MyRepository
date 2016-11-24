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

import de.hybris.platform.acceleratorstorefrontcommons.history.BrowseHistory;
import de.hybris.platform.acceleratorstorefrontcommons.history.BrowseHistoryEntry;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.CookieGenerator;

import com.tisl.mpl.storefront.security.cookie.CartRestoreCookieGenerator;


/**
 * Filter that initializes the session for the marketplacestorefront. This is a spring configured filter that is
 * executed by the PlatformFilterChain.
 */
public class StorefrontFilter extends OncePerRequestFilter
{
	public static final String AJAX_REQUEST_HEADER_NAME = "X-Requested-With";
	public static final String ORIGINAL_REFERER = "originalReferer";
	public static final String ISLUX = "isLux"; //Sonar fix

	private StoreSessionFacade storeSessionFacade;
	private BrowseHistory browseHistory;
	private CookieGenerator cookieGenerator;
	private CartRestoreCookieGenerator cartRestoreCookieGenerator;
	/*
	 * @Resource private ConfigurationService configurationService;
	 */
	private static final Logger LOG = Logger.getLogger(StorefrontFilter.class);

	//@Resource(name = "mediaService")
	//private MediaService mediaService;

	//@Resource(name = "catalogVersionService")
	//private CatalogVersionService catalogVersionService;


	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException
	{
		final HttpSession session = request.getSession();
		final String queryString = request.getQueryString();
		LOG.debug("Inside doFilterInternal");
		if (isSessionNotInitialized(session, queryString))
		{
			initDefaults(request);

			markSessionInitialized(session);
		}

		// For secure requests ensure that the JSESSIONID cookie is visible to insecure requests
		if (isRequestSecure(request))
		{
			fixSecureHttpJSessionIdCookie(request, response);
		}
		// TISLUX-1865
		setLuxuryCookie(request, response);


		if (isGetMethod(request))
		{
			if (StringUtils.isBlank(request.getHeader(AJAX_REQUEST_HEADER_NAME)))
			{
				final String requestURL = request.getRequestURL().toString();
				session.setAttribute(ORIGINAL_REFERER, StringUtils.isNotBlank(queryString) ? requestURL + "?" + queryString
						: requestURL);
			}

			getBrowseHistory().addBrowseHistoryEntry(new BrowseHistoryEntry(request.getRequestURI(), null));
		}
		//Fix for TISPT-113
		//getSEOAttributes(request);

		filterChain.doFilter(request, response);
	}

	/**
	 * @param request
	 * @param response
	 */
	private void setLuxuryCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		Cookie isLuxCookie = null;
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals(ISLUX))
				{
					isLuxCookie = cookie;
					break;
				}
			}
		}

		if (!request.getRequestURI().contains("login"))
		{
			if (null != request.getParameter(ISLUX) && "true".equalsIgnoreCase(request.getParameter(ISLUX)))
			{
				if (null == isLuxCookie)
				{
					isLuxCookie = new Cookie(ISLUX, "true");
					isLuxCookie.setPath("/");
					response.addCookie(isLuxCookie);
				}
				else
				{
					isLuxCookie.setValue("true");
					isLuxCookie.setPath("/");
					response.addCookie(isLuxCookie);
				}

			}

		}
		if (request.getRequestURI().contains("logout") && cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals("mpl-cart"))
				{
					cookie.setValue("");
					if (StringUtils.isNotEmpty(getCartRestoreCookieGenerator().getCustomDomain()))
					{
						cookie.setDomain(getCartRestoreCookieGenerator().getCustomDomain());
						cookie.setPath("/");
						//cookie.setMaxAge(0);
					}
					response.addCookie(cookie);
					break;
				}
			}
		}

	}

	//Fix for TISPT-113
	/**
	 * @param request
	 */
	//	private void getSEOAttributes(final HttpServletRequest request)
	//	{
	//		final String mediaCode = configurationService.getConfiguration().getString(MessageConstants.MEDIA_CODE).trim();
	//		String seoMediaURL = null;
	//		final String imageHost = configurationService.getConfiguration().getString(MessageConstants.MEDIA_HOST).trim();
	//		if (null != mediaCode)
	//		{
	//			final MediaModel media = getMediaByCode(mediaCode);
	//			try
	//			{
	//				if (null != media)
	//				{
	//					seoMediaURL = media.getURL2();
	//				}
	//			}
	//			catch (final Exception ex)
	//			{
	//				LOG.error("Exception at getSEOAttributes::::::::::::::", ex);
	//			}
	//			final StringBuilder sb = new StringBuilder();
	//			final String fullURL = sb.append(imageHost).append(seoMediaURL).toString();
	//			request.setAttribute(ModelAttributetConstants.SEO_MEDIA_URL, fullURL);
	//		}
	//	}

	//	protected MediaModel getMediaByCode(final String mediaCode)
	//	{
	//		if (StringUtils.isNotEmpty(mediaCode))
	//		{
	//			final CatalogVersionModel catalogVersionModel = catalogVersionService
	//					.getSessionCatalogVersionForCatalog("mplContentCatalog");
	//			try
	//			{
	//				final MediaModel media = mediaService.getMedia(catalogVersionModel, mediaCode);
	//				if (media != null)
	//				{
	//					return media;
	//				}
	//			}
	//			catch (final Exception ex)
	//			{
	//				LOG.error("Exception at getMediaByCode::::::::::::::", ex);
	//			}
	//		}
	//		return null;
	//	}

	protected boolean isGetMethod(final HttpServletRequest httpRequest)
	{
		return "GET".equalsIgnoreCase(httpRequest.getMethod());
	}

	protected boolean isRequestSecure(final HttpServletRequest httpRequest)
	{
		return httpRequest.isSecure();
	}

	protected boolean isSessionNotInitialized(final HttpSession session, final String queryString)
	{
		return session.isNew() || StringUtils.contains(queryString, CMSFilter.CLEAR_CMSSITE_PARAM)
				|| !isSessionInitialized(session);
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	@Required
	public void setBrowseHistory(final BrowseHistory browseHistory)
	{
		this.browseHistory = browseHistory;
	}

	protected void initDefaults(final HttpServletRequest request)
	{
		final StoreSessionFacade storeSessionFacade = getStoreSessionFacade();

		storeSessionFacade.initializeSession(Collections.list(request.getLocales()));
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	protected BrowseHistory getBrowseHistory()
	{
		return browseHistory;
	}


	protected boolean isSessionInitialized(final HttpSession session)
	{
		return session.getAttribute(this.getClass().getName()) != null;
	}

	protected void markSessionInitialized(final HttpSession session)
	{
		session.setAttribute(this.getClass().getName(), "initialized");
	}

	protected void fixSecureHttpJSessionIdCookie(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse)
	{
		final HttpSession session = httpServletRequest.getSession(false);
		if (session != null)
		{
			getCookieGenerator().addCookie(httpServletResponse, session.getId());
		}

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

	/**
	 * @return the cartRestoreCookieGenerator
	 */
	public CartRestoreCookieGenerator getCartRestoreCookieGenerator()
	{
		return cartRestoreCookieGenerator;
	}

	/**
	 * @param cartRestoreCookieGenerator
	 *           the cartRestoreCookieGenerator to set
	 */
	public void setCartRestoreCookieGenerator(final CartRestoreCookieGenerator cartRestoreCookieGenerator)
	{
		this.cartRestoreCookieGenerator = cartRestoreCookieGenerator;
	}
}
