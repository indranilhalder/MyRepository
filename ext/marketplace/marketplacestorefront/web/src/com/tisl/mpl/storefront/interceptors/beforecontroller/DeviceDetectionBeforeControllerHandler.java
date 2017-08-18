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

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.method.HandlerMethod;

import com.tisl.mpl.storefront.interceptors.BeforeControllerHandler;


/**
 * An interceptor to set up the request the detected device information.
 */
public class DeviceDetectionBeforeControllerHandler implements BeforeControllerHandler
{
	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	private UiExperienceService uiExperienceService;

	private BaseSiteService baseSiteService;

	private ConfigurationService configurationService;

	private SessionService sessionService;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		if (getUiExperienceService().getDetectedUiExperienceLevel() == null)
		{
			//		System.out.println("getDetectedUiExperienceLevel is null");
			getUiExperienceService().setDetectedUiExperienceLevel(UiExperienceLevel.DESKTOP);
		}

		if (getBaseSiteService().getCurrentBaseSite().getUid()
				.equals(configurationService.getConfiguration().getProperty("luxury.site.id")))
		{
			// Detect the device information for the current request
			deviceDetectionFacade.initializeRequest(request);
			getUiExperienceService().setDetectedUiExperienceLevel(UiExperienceLevel.RESPONSIVE);
		}



		return true;
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

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

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

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
