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


public class ExcludeManufacturesRestriction extends GeneratedExcludeManufacturesRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ExcludeManufacturesRestriction.class.getName());

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
	 * @Description: Exclude Brand Based Restriction evaluation Method for Promotions
	 * @param:SessionContext ctx
	 * @param:Collection<Product> products
	 * @param:Date date
	 * @param:AbstractOrder order
	 * @return:RestrictionResult
	 */
	@Override
	public RestrictionResult evaluate(final SessionContext ctx, final Collection<Product> products, final Date date,
			final AbstractOrder order)
	{
		return RestrictionResult.ALLOW;
	}

}
