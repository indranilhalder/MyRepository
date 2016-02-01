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
package com.tisl.mpl.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.interceptors.BeforeViewHandler;


/**
 */
public class ConfigGranuleBeforeViewHandler implements BeforeViewHandler
{
	private SiteConfigService siteConfigService;

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		try
		{
			modelAndView.addObject("granuleEnabled",
					Boolean.valueOf(getSiteConfigService().getBoolean("storefront.granule.enabled", false)));
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}
}
