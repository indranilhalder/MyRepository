/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.impl.denialstrategies.AbstractCancelDenialStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;


/**
 * This class is used for checking whether the order is fully cancellable or not.
 *
 * @author 1006687
 *
 */
public class MarketPlaceOrderCancellationStateStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{

	private OrderCancelStateMappingStrategy stateMappingStrategy;

	@Override
	public OrderCancelDenialReason getCancelDenialReason(final OrderCancelConfigModel paramOrderCancelConfigModel,
			final OrderModel orderModel, final PrincipalModel paramPrincipalModel, final boolean paramBoolean1,
			final boolean paramBoolean2)
	{
		//Calling strategy to check order full cancellation possibility based on order and consignment status.
		final OrderCancelState orderCancelState = stateMappingStrategy.getOrderCancelState(orderModel);
		if (orderCancelState == null || orderCancelState.equals(OrderCancelState.PENDINGORHOLDINGAREA))
		{
			return null;
		}
		return getReason();
	}

	/**
	 * @return the stateMappingStrategy
	 */
	public OrderCancelStateMappingStrategy getStateMappingStrategy()
	{
		return stateMappingStrategy;
	}

	/**
	 * @param stateMappingStrategy
	 *           the stateMappingStrategy to set
	 */
	public void setStateMappingStrategy(final OrderCancelStateMappingStrategy stateMappingStrategy)
	{
		this.stateMappingStrategy = stateMappingStrategy;
	}



}
