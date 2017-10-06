/**
 *
 */
package com.tisl.lux.device.impl;

import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.acceleratorfacades.device.data.UiExperienceData;
import de.hybris.platform.acceleratorfacades.device.impl.DefaultDeviceDetectionFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author Madhavan
 *
 */
public class LuxuryDeviceDetectionFacade extends DefaultDeviceDetectionFacade
{

	private static final Logger LOG = Logger.getLogger(LuxuryDeviceDetectionFacade.class.getName());

	private SessionService sessionService;

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorfacades.device.impl.DefaultDeviceDetectionFacade#initializeRequest(javax.servlet
	 * .http.HttpServletRequest)
	 */
	@Override
	public void initializeRequest(final HttpServletRequest request)
	{
		// YTODO Auto-generated method stub
		super.initializeRequest(request);

		//if (getCurrentDetectedDevice() == null || "true".equals(request.getParameter("clear")))
		//{
		final DeviceData deviceData = getRequestDeviceDataConverter().convert(request);
		final UiExperienceData uiExperienceData = getDeviceDataUiExperienceDataConverter().convert(deviceData);

		if (uiExperienceData != null && uiExperienceData.getLevel() != null)
		{
			sessionService.setAttribute("detectedUI", uiExperienceData.getLevel());
			LOG.error("Detected UI - " + uiExperienceData.getLevel());
		}
		//}

	}
}
