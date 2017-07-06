/**
 *
 */
package com.tisl.lux.evaluator;

import de.hybris.platform.acceleratorcms.evaluator.CMSUiExperienceRestrictionEvaluator;
import de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.servicelayer.session.SessionService;


/**
 * @author Madhavan
 *
 */
public class LuxuryCMSUiExperienceRestrictionEvaluator extends CMSUiExperienceRestrictionEvaluator
{

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
		if (null != getSessionService().getAttribute("detectedUI"))
		{
			return getSessionService().getAttribute("detectedUI").equals(restriction.getUiExperience());
		}

		return super.evaluate(restriction, context);
	}

}
