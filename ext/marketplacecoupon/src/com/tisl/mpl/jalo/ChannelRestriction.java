//package com.tisl.mpl.jalo;
//
//import de.hybris.platform.core.model.order.CartModel;
//import de.hybris.platform.core.model.order.OrderModel;
//import de.hybris.platform.jalo.Item;
//import de.hybris.platform.jalo.JaloBusinessException;
//import de.hybris.platform.jalo.SessionContext;
//import de.hybris.platform.jalo.enumeration.EnumerationValue;
//import de.hybris.platform.jalo.order.AbstractOrder;
//import de.hybris.platform.jalo.order.Order;
//import de.hybris.platform.jalo.product.Product;
//import de.hybris.platform.jalo.type.ComposedType;
//
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//
//public class ChannelRestriction extends GeneratedChannelRestriction
//{
//	@SuppressWarnings("unused")
//	private final static Logger LOG = Logger.getLogger(ChannelRestriction.class.getName());
//
//	@Override
//	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
//			throws JaloBusinessException
//	{
//		// business code placed here will be executed before the item is created
//		// then create the item
//		final Item item = super.createItem(ctx, type, allAttributes);
//		// business code placed here will be executed after the item was created
//		// and return the item
//		return item;
//	}
//
//	@Override
//	protected boolean isFulfilledInternal(final AbstractOrder cart)
//	{
//		final List<EnumerationValue> channel = super.getChannel();
//		final Boolean positive = super.isPositive();
//		boolean isValid = false;
//		Object cartChannel = null;
//		try
//		{
//			if (cart instanceof de.hybris.platform.jalo.order.Cart)
//			{
//				cartChannel = cart.getAttribute(CartModel.CHANNEL);
//			}
//			else if (cart instanceof Order)
//			{
//				cartChannel = cart.getAttribute(OrderModel.SALESAPPLICATION);
//			}
//		}
//		catch (final Exception ex)
//		{
//			LOG.error(ex.getMessage());
//		}
//
//		if (positive.booleanValue())
//		{
//			isValid = checkChannelIncl(channel, cartChannel);
//		}
//		else
//		{
//			isValid = checkChannelExcl(channel, cartChannel);
//		}
//
//		return isValid;
//	}
//
//	@Override
//	protected boolean isFulfilledInternal(final Product arg0)
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//
//	protected boolean checkChannelIncl(final List<EnumerationValue> channel, final Object cartChannel)
//	{
//		boolean checkFlag = false;
//		if (null != cartChannel && channel.contains(cartChannel))
//		{
//			LOG.debug("The channel of the cart is " + cartChannel);
//			checkFlag = true;
//		}
//		return checkFlag;
//	}
//
//
//	protected boolean checkChannelExcl(final List<EnumerationValue> channel, final Object cartChannel)
//	{
//		boolean checkFlag = false;
//		if (null != cartChannel && !channel.contains(cartChannel))
//		{
//			LOG.debug("The channel of the cart is " + cartChannel);
//			checkFlag = true;
//		}
//		return checkFlag;
//	}
//
//}