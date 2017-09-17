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
package com.tisl.mpl.storefront.security;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.service.MplQCInitServiceImpl;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.security.cookie.UserCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.UserTypeCookieGenerator;


/**
 * Success handler initializing user settings, restoring or merging the cart and ensuring the cart is handled correctly.
 * Cart restoration is stored in the session since the request coming in is that to j_spring_security_check and will be
 * redirected
 */
public class StorefrontAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private CustomerFacade customerFacade;
	private UiExperienceService uiExperienceService;
	private CartFacade cartFacade;
	private SessionService sessionService;
	private BruteForceAttackCounter bruteForceAttackCounter;
	private Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel;
	private List<String> restrictedPages;

	private static final String CHECKOUT_URL = "/checkout";
	private static final String CART_MERGED = "cartMerged";
	private static final String LOGIN_SUCCESS = "loginSuccess";

	private static final String ANONYMOUS = "session";
	private static final String REGISTERED = "site_user";

	private static final String FACEBOOKUSER = "facebook";
	private static final String FACEBOOK_LOGIN = "FACEBOOK_LOGIN";

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

	@Autowired
	private ExtendedUserService extUserService;

	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;

	@Resource(name = "mplQCInitService")
	private MplQCInitServiceImpl mplQCInitService;
	


	/**
	 * @return the mplQCInitService
	 */
	public MplQCInitServiceImpl getMplQCInitService()
	{
		return mplQCInitService;
	}

	/**
	 * @param mplQCInitService the mplQCInitService to set
	 */
	public void setMplQCInitService(MplQCInitServiceImpl mplQCInitService)
	{
		this.mplQCInitService = mplQCInitService;
	}

	/**
	 * @return the productDetailsHelper
	 */
	public ProductDetailsHelper getProductDetailsHelper()
	{
		return productDetailsHelper;
	}

	/**
	 * @param productDetailsHelper
	 *           the productDetailsHelper to set
	 */
	public void setProductDetailsHelper(final ProductDetailsHelper productDetailsHelper)
	{
		this.productDetailsHelper = productDetailsHelper;
	}

	private static final Logger LOG = Logger.getLogger(StorefrontAuthenticationSuccessHandler.class);


	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	@SuppressWarnings("boxing")
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{

		getCustomerFacade().loginSuccess();
		
		getMplQCInitService().init();
		//Microsite POC
		//This is present in the filter but in case the login request comes from a microsite,the redirect url will be the microsite url and hence not pass through the filter.
		//Hence updating the cookie here.
		updateUserDetailsCookie(request, response);

		final HttpSession session = request.getSession();

		session.setAttribute(LOGIN_SUCCESS, Boolean.TRUE);



		/* Gigya code commented for non existence in Release1 */


		//Code Start For Gigya Integration

		//Switch to control Gigya Services
		final String gigyaServiceSwitch = getConfigurationService().getConfiguration().getString(MessageConstants.USE_GIGYA);

		if (gigyaServiceSwitch != null && !gigyaServiceSwitch.equalsIgnoreCase(MessageConstants.NO))
		{
			final CustomerModel customerModel = (CustomerModel) extUserService.getCurrentUser();

			final Cookie ratingReviewCookie = productDetailsHelper.ratingReviewHelper(customerModel, false);

			if (ratingReviewCookie != null)
			{
				response.addCookie(ratingReviewCookie);

			}
			else
			{
				LOG.debug(MessageConstants.COOKIE_ERROR);
			}
		}
		else
		{
			LOG.debug(MessageConstants.GIGYA_ERROR);
		} //Code
		  // End For Gigya Integration

		//final UserModel userModel = extUserService.getUserForUID(authentication.getName());
		//		final CustomerModel customer = extUserService.setCurrentUser(userModel);

		final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		final CustomerModel customer = extUserService.getUserForUid(username);

		if (username != null && !username.isEmpty() && customer.getIsTemporaryPasswordChanged() != null
				&& customer.getIsTemporaryPasswordChanged().equals(Boolean.FALSE))
		{
			super.clearAuthenticationAttributes(request);
			response.sendRedirect(request.getContextPath() + ControllerConstants.Views.responsive.Account.ChangePassword);
			return;
		}
		request.setAttribute(CART_MERGED, Boolean.FALSE);

		if (!getCartFacade().hasEntries())
		{
			getSessionService().setAttribute(WebConstants.CART_RESTORATION_SHOW_MESSAGE, Boolean.TRUE);
			try
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION, getCartFacade().restoreSavedCart(null));
			}
			catch (final CommerceCartRestorationException e)
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS,
						WebConstants.CART_RESTORATION_ERROR_STATUS);
			}
		}
		else if (getCartFacade().hasSessionCart() && !getCartFacade().getSessionCart().getEntries().isEmpty()
				&& getMostRecentSavedCart(getCartFacade().getSessionCart()) != null)
		{
			getSessionService().setAttribute(WebConstants.CART_RESTORATION_SHOW_MESSAGE, Boolean.TRUE);
			try
			{
				getSessionService().setAttribute(
						WebConstants.CART_RESTORATION,
						getCartFacade().restoreCartAndMerge(getMostRecentSavedCart(getCartFacade().getSessionCart()).getGuid(),
								getCartFacade().getSessionCart().getGuid()));
				request.setAttribute(CART_MERGED, Boolean.TRUE);
			}
			catch (final CommerceCartRestorationException e)
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS,
						WebConstants.CART_RESTORATION_ERROR_STATUS);
			}
			catch (final CommerceCartMergingException e)
			{
				LOG.error("User saved cart could not be merged");
			}
		}

		getBruteForceAttackCounter().resetUserCounter(getCustomerFacade().getCurrentCustomer().getUid());
		/**
		 * Failed login attempts are cleared for successful logins
		 */
		if (null != sessionService.getAttribute(ModelAttributetConstants.COUNT))
		{
			final int failedCounts = sessionService.getAttribute(ModelAttributetConstants.COUNT);
			if (failedCounts > 0)
			{
				sessionService.setAttribute(ModelAttributetConstants.COUNT, 0);
			}
		}
		/**
		 * AJAX response on successful authentication echoing SavedRequest request URI
		 */
		PrintWriter pw = null;
		final String xForwarded = request.getHeader("X-Requested-With");
		final String referer = request.getHeader("referer");
		String referringController = null;
		//		if (StringUtils.isNotEmpty((String) sessionService.getAttribute(ModelAttributetConstants.FORGET_PASSWORD_UPDATE_SUCCESS)))
		//		{
		//			if (sessionService.getAttribute(ModelAttributetConstants.FORGET_PASSWORD_UPDATE_SUCCESS).equals(
		//					ModelAttributetConstants.TRUE))
		//			{
		//				pw = response.getWriter();
		//				pw.print(request.getContextPath());
		//				sessionService.removeAttribute(ModelAttributetConstants.FORGET_PASSWORD_UPDATE_SUCCESS);
		//			}
		//		}
		//		else
		if (null != xForwarded && xForwarded.equalsIgnoreCase("XMLHttpRequest"))
		{
			response.setContentType("text/html");
			//response.setHeader("Access-Control-Allow-Origin", "*");
			final SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
			if (null != referer)
			{
				referringController = referer.substring(referer.lastIndexOf('/'), referer.length());

			}
			pw = response.getWriter();

			if (null != savedRequest)
			{
				/*
				 * Expression matching the referring URL to be login page then redirect with value stored in saved request.
				 */
				if (null != referringController && referringController.contains(RequestMappingUrlConstants.LINK_LOGIN))
				{
					pw.print(request.getContextPath());
				}
				else if (null != referringController
						&& referringController.equalsIgnoreCase(RequestMappingUrlConstants.LINK_CHECKOUT))
				{
					pw.print(request.getContextPath() + RequestMappingUrlConstants.LINK_CHECKOUT);
				}
				else
				{
					pw.print(307);
				}

			}
			else if (null != referringController && referringController.equalsIgnoreCase(RequestMappingUrlConstants.LINK_CHECKOUT))
			{
				pw.print(request.getContextPath() + RequestMappingUrlConstants.LINK_CHECKOUT);
			}
			else
			{
				pw.print(307);
			}
			pw.close();
		}
		else
		{
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
		/** Added for UF-93 for Remember Me functionality **/
		String rememberMe = "false";
		if (null != request.getParameter("j_RememberMe"))
		{
			rememberMe = request.getParameter("j_RememberMe");
			LOG.error("StorefrontAuthenticationSuccessHandler.onAuthenticationSuccess() - Found 'j_RememberMe' in request:: "
					+ rememberMe);
		}
		if (null != request.getSession())
		{
			request.getSession().setAttribute("rememberMe", rememberMe);
			LOG.error("StorefrontAuthenticationSuccessHandler.onAuthenticationSuccess() - After setting in Session 'j_RememberMe' ::"
					+ request.getSession().getAttribute("rememberMe")
					+ " SessionId: "
					+ request.getSession().getId()
					+ " SessionTimeout: " + request.getSession().getMaxInactiveInterval());
		}
		/** Added for UF-93 Ends **/

	}

	/**
	 * @param request
	 * @param response
	 */
	private void updateUserDetailsCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		String userId = null;
		String userType = null;
		boolean userCookieSet = false;
		boolean userTypeCookieSet = false;
		final UserModel currentUser = extUserService.getCurrentUser();
		final CustomerModel currCust = (CustomerModel) extUserService.getCurrentUser();

		//TISLUX-1352
		if (request.getCookies() != null && null != getUserCookieGenerator() && null != getUserCookieGenerator().getCookieName()
				&& null != getUserTypeCookieGenerator() && null != getUserTypeCookieGenerator().getCookieName())
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
					if (extUserService.isAnonymousUser(currentUser))
					{
						userType = ANONYMOUS;
					}
					else
					{
						if (null != currCust && null != currCust.getType())
						{
							if (currCust.getType().toString().equals(FACEBOOK_LOGIN))
							{
								userType = FACEBOOKUSER;
							}
							else
							{
								userType = REGISTERED;
							}
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
			//TISLUX-1352
			if (null != getUserCookieGenerator())
			{
				getUserCookieGenerator().addCookie(response, currentUser.getUid());
			}
			if (extUserService.isAnonymousUser(currentUser))
			{
				userType = ANONYMOUS;
			}
			else
			{
				userType = REGISTERED;
			}
			//TISLUX-1352
			if (null != getUserCookieGenerator())
			{
				getUserTypeCookieGenerator().addCookie(response, userType);
			}
		}

	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
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

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	/*
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler#
	 * isAlwaysUseDefaultTargetUrl()
	 */
	@Override
	protected boolean isAlwaysUseDefaultTargetUrl()
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (getForceDefaultTargetForUiExperienceLevel().containsKey(uiExperienceLevel))
		{
			return Boolean.TRUE.equals(getForceDefaultTargetForUiExperienceLevel().get(uiExperienceLevel));
		}
		else
		{
			return false;
		}
	}

	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		String targetUrl = super.determineTargetUrl(request, response);
		if (CollectionUtils.isNotEmpty(getRestrictedPages()))
		{
			for (final String restrictedPage : getRestrictedPages())
			{
				// When logging in from a restricted page, return user to homepage.
				if (targetUrl.contains(restrictedPage))
				{
					targetUrl = super.getDefaultTargetUrl();
				}
			}
		}
		/*
		 * If the cart has been merged and the user logging in through checkout, redirect to the cart page to display the
		 * new cart
		 */
		if (StringUtils.equals(targetUrl, CHECKOUT_URL) && ((Boolean) request.getAttribute(CART_MERGED)).booleanValue())
		{
			// targetUrl = CART_URL; Commented for Checkout journey , it will redirect to Delivery mode selection page
			targetUrl = CHECKOUT_URL;
		}

		return targetUrl;
	}

	/**
	 * Determine the most recent saved cart of a user for the site that is not the current session cart. The current
	 * session cart is already owned by the user and for the merging functionality to work correctly the most recently
	 * saved cart must be determined. getCartsForCurrentUser() returns an ordered by modified time list of the saved
	 * carts of the user.
	 *
	 * @param currentCart
	 * @return most recently saved cart
	 */
	protected CartData getMostRecentSavedCart(final CartData currentCart)
	{
		final List<CartData> userCarts = getCartFacade().getCartsForCurrentUser();
		if (CollectionUtils.isNotEmpty(userCarts))
		{
			for (final CartData cart : userCarts)
			{
				if (!StringUtils.equals(cart.getGuid(), currentCart.getGuid()))
				{
					return cart;
				}
			}
		}
		return null;
	}

	protected Map<UiExperienceLevel, Boolean> getForceDefaultTargetForUiExperienceLevel()
	{
		return forceDefaultTargetForUiExperienceLevel;
	}

	@Required
	public void setForceDefaultTargetForUiExperienceLevel(
			final Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel)
	{
		this.forceDefaultTargetForUiExperienceLevel = forceDefaultTargetForUiExperienceLevel;
	}

	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}



}
