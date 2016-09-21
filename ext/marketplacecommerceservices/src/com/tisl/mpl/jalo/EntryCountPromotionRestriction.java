package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;


public class EntryCountPromotionRestriction extends GeneratedEntryCountPromotionRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EntryCountPromotionRestriction.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}



	/**
	 * This method sets restriction description TPR-969
	 *
	 */
	@Override
	protected Object[] getDescriptionPatternArguments(final SessionContext ctx)
	{
		return new Object[]
		{ getRestrictionType(ctx), getEntryQualifyingCount(ctx) };
	}


	/**
	 * This method evaluates Entry count restriction. Allowed in restriction. Logic is handled in PromotionManager.
	 * TPR-969
	 *
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return RestrictionResult
	 *
	 */
	@Override
	public RestrictionResult evaluate(final SessionContext arg0, final Collection<Product> arg1, final Date arg2,
			final AbstractOrder arg3)
	{
		return RestrictionResult.ALLOW;
	}

}
