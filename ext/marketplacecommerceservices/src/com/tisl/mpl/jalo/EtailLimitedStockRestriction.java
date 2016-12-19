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


public class EtailLimitedStockRestriction extends GeneratedEtailLimitedStockRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EtailLimitedStockRestriction.class.getName());

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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.promotions.jalo.AbstractPromotionRestriction#evaluate(de.hybris.platform.jalo.SessionContext,
	 * java.util.Collection, java.util.Date, de.hybris.platform.jalo.order.AbstractOrder)
	 */
	@Override
	public RestrictionResult evaluate(final SessionContext arg0, final Collection<Product> arg1, final Date arg2,
			final AbstractOrder arg3)
	{
		// YTODO Auto-generated method stub
		return RestrictionResult.ALLOW;
	}

}
