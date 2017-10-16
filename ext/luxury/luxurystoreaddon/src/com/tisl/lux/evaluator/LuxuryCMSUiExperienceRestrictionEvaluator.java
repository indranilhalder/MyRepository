/**
 *
 */
package com.tisl.lux.evaluator;

import de.hybris.platform.acceleratorcms.evaluator.CMSUiExperienceRestrictionEvaluator;
import de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.log4j.Logger;


/**
 * @author Madhavan
 *
 */
public class LuxuryCMSUiExperienceRestrictionEvaluator extends CMSUiExperienceRestrictionEvaluator
{

	private static final Logger LOG = Logger.getLogger(LuxuryCMSUiExperienceRestrictionEvaluator.class.getName());
	private SessionService sessionService;

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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.acceleratorcms.evaluator.CMSUiExperienceRestrictionEvaluator#evaluate(de.hybris.platform.
	 * acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel,
	 * de.hybris.platform.cms2.servicelayer.data.RestrictionData)
	 */
	@Override
	public boolean evaluate(final CMSUiExperienceRestrictionModel restriction, final RestrictionData context)
	{
		LOG.error("Inside LuxuryCMSUiExperienceRestrictionEvaluator");
		LOG.error("Detected UI " + getSessionService().getAttribute("detectedUI"));
		LOG.error("restriction.getUiExperience() " + restriction.getUiExperience());
		if (null != getSessionService().getAttribute("detectedUI"))
		{
			LOG.error("Returning - " + getSessionService().getAttribute("detectedUI").equals(restriction.getUiExperience()));
			return getSessionService().getAttribute("detectedUI").equals(restriction.getUiExperience());
		}

		return super.evaluate(restriction, context);
	}
}
