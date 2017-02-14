package com.tisl.mpl.jalo;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.List;

import org.apache.log4j.Logger;


public class ChannelRestriction extends GeneratedChannelRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ChannelRestriction.class.getName());

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
	protected boolean isFulfilledInternal(final AbstractOrder paramAbstractOrder)
	{
		// YTODO Auto-generated method stub
		final List<EnumerationValue> channel = super.getChannel();
		boolean isValid = false;
		Object cartChannel = null;
		try
		{
			if (paramAbstractOrder instanceof Cart)
			{
				cartChannel = paramAbstractOrder.getAttribute(CartModel.CHANNEL);
				LOG.debug("CartChannel for Cart" + cartChannel);

			}
			if (paramAbstractOrder instanceof Order)
			{
				cartChannel = paramAbstractOrder.getAttribute(OrderModel.SALESAPPLICATION);

				LOG.debug("CartChannel for Order" + cartChannel);
			}
		}
		catch (final Exception ex)
		{
			LOG.debug("Exception");
		}
		isValid = checkChannelFound(channel, cartChannel);

		return isValid;
	}

	/**
	 * @param channel
	 * @param cartChannel
	 */
	private boolean checkChannelFound(final List<EnumerationValue> channel, final Object cartChannel)
	{
		// YTODO Auto-generated method stub
		boolean checkFlag = false;
		for (final EnumerationValue enumChannel : channel)
		{
			/*
			 * enumChannel.getCode(); cartChannel.toString();
			 */
			if ((cartChannel != null) && (enumChannel.equals(cartChannel)))
			{
				checkFlag = true;
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
	protected boolean isFulfilledInternal(final Product paramProduct)
	{
		// YTODO Auto-generated method stub
		return false;
	}
}
