package com.tisl.mpl.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;

import org.apache.log4j.Logger;


public class PaymentModeRestriction extends GeneratedPaymentModeRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PaymentModeRestriction.class.getName());

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
	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.order.AbstractOrder)
	 */
	@Override
	protected boolean isFulfilledInternal(final AbstractOrder arg0)
	{
		boolean checkFlag = false;
		if (null != arg0)
		{
			final User user = arg0.getUser();
			for (final Cart cart : user.getCarts())
			{
				if (cart != null && getPaymentModes().contains(cart.getPaymentMode()))
				{
					checkFlag = true;
				}
			}

		}
		return checkFlag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.product.Product)
	 */
	@Override
	protected boolean isFulfilledInternal(final Product arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}

}