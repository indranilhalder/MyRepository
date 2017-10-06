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

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.security.cookie.CartRestoreCookieGenerator;


public class StorefrontLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{
	private GUIDCookieStrategy guidCookieStrategy;
	@Resource(name = "cartRestoreCookieGenerator")
	private CartRestoreCookieGenerator cartRestoreCookieGenerator;
	private List<String> restrictedPages;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;


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

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	protected List<String> getRestrictedPages()
	{
		return restrictedPages;
	}

	public void setRestrictedPages(final List<String> restrictedPages)
	{
		this.restrictedPages = restrictedPages;
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	/* Gigya code commented for non existence in Release1 */

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException

	{
		final String gigyaServiceSwitch = getConfigurationService().getConfiguration().getString(MessageConstants.USE_GIGYA);

		if (gigyaServiceSwitch != null && !gigyaServiceSwitch.equalsIgnoreCase(MessageConstants.NO))
		{
			if (authentication != null)
			{
				if (authentication.getName() != null)
				{
					productDetailsHelper.ratingReviewLogout(authentication.getName());
				}
			}
		}
		getGuidCookieStrategy().deleteCookie(request, response);
		//For TPR-6334 : creating cookie object
		final Cookie dtmLoginCookie = new Cookie(MessageConstants.DTM_SIGNOUT_STATUS, "success");
		dtmLoginCookie.setPath(ModelAttributetConstants.SLASH);
		response.addCookie(dtmLoginCookie);

		// Delegate to default redirect behaviour
		super.onLogoutSuccess(request, response, authentication);
	}



	@Override
	protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response)
	{
		String targetUrl = super.determineTargetUrl(request, response);

		for (final String restrictedPage : getRestrictedPages())
		{
			// When logging out from a restricted page, return user to homepage.
			if (targetUrl.contains(restrictedPage))
			{
				targetUrl = super.getDefaultTargetUrl();
			}
		}

		//		if (targetUrl.contains(ModelAttributetConstants.STORE_URL_OLD))
		//		{
		//			final String newreqUri = targetUrl.replaceAll(ModelAttributetConstants.STORE_URL_OLD, "/");
		//
		//
		//			targetUrl = newreqUri;
		//		}

		return targetUrl;
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
