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

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;

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
	//@Resource(name = "deviceDetectionFacade")
	//private DeviceDetectionFacade deviceDetectionFacade;

	private UiExperienceService uiExperienceService;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		if (getUiExperienceService().getDetectedUiExperienceLevel() == null)
		{
			getUiExperienceService().setDetectedUiExperienceLevel(UiExperienceLevel.DESKTOP);
		}
		// Detect the device information for the current request
		//deviceDetectionFacade.initializeRequest(request);
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
}
