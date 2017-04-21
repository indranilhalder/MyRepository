package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


public class PaymentModeRestriction extends GeneratedPaymentModeRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PaymentModeRestriction.class.getName());

	/**
	 * @param ctx
	 * @param type
	 * @param allAttributes
	 * @exception JaloBusinessException
	 */
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

	///////////////////TPR-4461///////////////////
	/**
	 * Coupon Payment Specific Evaluation Logic
	 *
	 * @param cart
	 */


	@Override
	protected boolean isFulfilledInternal(final AbstractOrder cart)
	{
		return true;//by default returns true irrespective of any particularly selected payment mode
	}


	/**
	 * @param arg0
	 */
	@Override
	protected boolean isFulfilledInternal(final Product arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}



}