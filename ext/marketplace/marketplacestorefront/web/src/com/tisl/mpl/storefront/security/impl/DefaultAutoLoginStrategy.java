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

import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.storefront.constants.MessageConstants;


/**
 * Default implementation of {@link AutoLoginStrategy}
 */
public class DefaultAutoLoginStrategy implements AutoLoginStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultAutoLoginStrategy.class);

	private AuthenticationManager authenticationManager;
	private CustomerFacade customerFacade;
	private GUIDCookieStrategy guidCookieStrategy;
	private RememberMeServices rememberMeServices;

	@Autowired
	private ExtendedUserService extUserService;

	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;

	@Autowired
	private ExtendedUserService userService;
	@Autowired
	private ModelService modelService;


	private static final String CART_MERGED = "cartMerged";

	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
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

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	@Override
	public void login(final String username, final String password, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		UsernamePasswordAuthenticationToken token = null;
		token = new UsernamePasswordAuthenticationToken(username, password);
		token.setDetails(new WebAuthenticationDetails(request));
		LOG.debug("Method login USERNAME " + username);
		LOG.debug("Method login PASSWORD " + password);

		try
		{
			if (password != null && !password.equalsIgnoreCase("TATACLiQSocialLogin"))
			{
				final Authentication authentication = getAuthenticationManager().authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				getCustomerFacade().loginSuccess();
				getGuidCookieStrategy().setCookie(request, response);
				generateAttribute(request);
				getRememberMeServices().loginSuccess(request, response, token);

				LOG.debug("Method login SITE USER");
				/* TPR-6654 start */
				final CustomerModel customerModel = (CustomerModel) extUserService.getCurrentUser();
				final AddressModel address = customerModel.getDefaultShipmentAddress();
				if (address != null && address.getPostalcode() != null)
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, address.getPostalcode());
				}
				/* TPR-6654 end */

				//Start Gigya Integration
				final String gigyaServiceSwitch = getConfigurationService().getConfiguration().getString(MessageConstants.USE_GIGYA);

				if (gigyaServiceSwitch != null && !gigyaServiceSwitch.equalsIgnoreCase(MessageConstants.NO))
				{
					if (customerModel.getType().equals(CustomerType.REGISTERED))
					{

						final Cookie ratingReviewCookie = productDetailsHelper.ratingReviewHelper(customerModel, true);

						if (ratingReviewCookie != null)
						{
							response.addCookie(ratingReviewCookie);

						}
						else
						{
							LOG.debug(MessageConstants.COOKIE_ERROR);
						}
					}
				}
				else
				{
					LOG.debug(MessageConstants.GIGYA_ERROR);
				}
				//Code End For Gigya Integration
			}
			else
			{

				final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password,
						AuthorityUtils.createAuthorityList("ROLE_CUSTOMERGROUP"));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				getCustomerFacade().loginSuccess();
				getGuidCookieStrategy().setCookie(request, response);
				generateAttribute(request);
				/**
				 * New Code Added TISPRD-687
				 */

				final UserModel userModel = userService.getUserForUID(username);
				getRememberMeServices().loginSuccess(request, response, token);
				// finally, set user in session

				final User user = modelService.getSource(userModel);
				JaloSession.getCurrentSession().setUser(user);
				LOG.debug("Method login SOCIAL RETURN USER USERNAME " + username);
				LOG.debug("Method login SOCIAL RETURN USER PASSWORD " + password);
				/* TPR-6654 start */
				final CustomerModel customerModel = (CustomerModel) extUserService.getCurrentUser();
				final AddressModel address = customerModel.getDefaultShipmentAddress();
				if (address != null && address.getPostalcode() != null)
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, address.getPostalcode());
				}
				/* TPR-6654 end */
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
						&& getMostRecentSavedCart(getCartFacade().getSessionCart()) != null)//, userModel.getCarts()) != null
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

				/**
				 * New Code Ends TISPRD-687
				 */

			}


		}
		catch (final Exception e)
		{
			SecurityContextHolder.getContext().setAuthentication(null);
			LOG.error("Failure during autoLogin", e);
		}
	}

	protected CartFacade getCartFacade()
	{
		return Registry.getApplicationContext().getBean("cartFacade", CartFacade.class);
	}

	protected SessionService getSessionService()
	{
		return Registry.getApplicationContext().getBean("sessionService", SessionService.class);
	}



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



	protected AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	@Required
	public void setAuthenticationManager(final AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
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

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected RememberMeServices getRememberMeServices()
	{
		return rememberMeServices;
	}

	@Required
	public void setRememberMeServices(final RememberMeServices rememberMeServices)
	{
		this.rememberMeServices = rememberMeServices;
	}

	private void generateAttribute(final HttpServletRequest request)
	{
		LOG.info("Login Attribute Setting:");
		final HttpSession session = request.getSession();
		session.setAttribute("LOGIN", MarketplacecommerceservicesConstants.firstTimeUserSessionVal);
	}
}
