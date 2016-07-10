/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;



import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelStateMappingStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author 666220
 *
 */
public class MarketPlaceDefaultOrderCancelStateMappingStrategy extends DefaultOrderCancelStateMappingStrategy

{

	List<String> nonCancellabelStates;

	List<OrderStatus> nonCancellabelOrdeStatus = Arrays.asList(OrderStatus.RMS_VERIFICATION_FAILED, OrderStatus.PAYMENT_FAILED,
			OrderStatus.RMS_VERIFICATION_PENDING, OrderStatus.ORDER_CANCELLED, OrderStatus.REFUND_IN_PROGRESS,
			OrderStatus.CANCELLATION_INITIATED, OrderStatus.PACKED);


	@Override
	public OrderCancelState getOrderCancelState(final OrderModel order)
	{
		if (CollectionUtils.isNotEmpty(order.getChildOrders()) || nonCancellabelOrdeStatus.contains(order.getStatus()))
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		return super.getOrderCancelState(order);

	}

	@Override
	protected OrderCancelState checkConsignments(final Collection<ConsignmentModel> consignments)
	{
		boolean cancelable = false;
		for (final ConsignmentModel consignment : consignments)
		{
			if (!nonCancellabelStates.contains(consignment.getStatus().getCode().toUpperCase()))
			{
				cancelable = true;
				break;
			}
		}
		if (!cancelable)
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		return OrderCancelState.PENDINGORHOLDINGAREA;
	}

	public List<String> getNonCancellabelStates()
	{
		return nonCancellabelStates;
	}

	public void setNonCancellabelStates(final List<String> nonCancellabelStates)
	{
		this.nonCancellabelStates = nonCancellabelStates;
	}



}
