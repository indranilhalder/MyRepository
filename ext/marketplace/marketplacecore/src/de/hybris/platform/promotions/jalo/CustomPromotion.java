/**
 *
 */
package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;



/**
 * @author 168108
 *
 */
public class CustomAbstractPromotion extends AbstractPromotion
{
	private static final Logger LOG = Logger.getLogger(CustomAbstractPromotion.class);

	public void findOrCreateImmutableCloneNew(final SessionContext ctx, final AbstractPromotion promotion)
	{
		try
		{
			promotion.findOrCreateImmutableClone(ctx);
		}
		catch (final Exception e)
		{
			LOG.error("Error in promotion.findOrCreateImmutableClone(ctx)" + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotion#evaluate(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.result.PromotionEvaluationContext)
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext arg0, final PromotionEvaluationContext arg1)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.promotions.jalo.AbstractPromotion#getResultDescription(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.jalo.PromotionResult, java.util.Locale)
	 */
	@Override
	public String getResultDescription(final SessionContext arg0, final PromotionResult arg1, final Locale arg2)
	{
		// YTODO Auto-generated method stub
		return null;
	}
}
