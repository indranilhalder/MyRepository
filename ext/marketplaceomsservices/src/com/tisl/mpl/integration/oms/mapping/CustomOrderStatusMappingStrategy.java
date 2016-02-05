/**
 *
 */
package com.tisl.mpl.integration.oms.mapping;

/**
 * @author TCS
 *
 */
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.integration.oms.OrderWrapper;
import de.hybris.platform.integration.oms.ShipmentWrapper;
import de.hybris.platform.integration.oms.mapping.OmsHybrisEnumMappingStrategy;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.shipping.Shipment;


public class CustomOrderStatusMappingStrategy implements OmsHybrisEnumMappingStrategy<OrderStatus, Order>
{
	private OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> consignmentStatusMappingStrategy;

	public OrderStatus getHybrisEnumFromDto(final Order order)
	{
		final OrderWrapper orderWrapper = new OrderWrapper(order);
		if (orderWrapper.getShipments().size() > 0)
		{
			final ConsignmentStatus commonStatus = allShipmentCommonStatus(orderWrapper);
			if ((commonStatus != null) && (commonStatus.equals(ConsignmentStatus.CANCELLED)))
			{
				return OrderStatus.CANCELLED;
			}

			if (isCompleted(orderWrapper))
			{
				return OrderStatus.COMPLETED;
			}
			if ((orderWrapper.getShipments().size() > 1) && (commonStatus == null))
			{
				return OrderStatus.ORDER_SPLIT;
			}
			int count = 0;
			String orderStatus = null;
			for (final OrderLine line : orderWrapper.getOrder().getOrderLines())
			{
				if (count == 0)
				{
					orderStatus = line.getSellerOrderStatus();
					count++;
				}
				else if (line.getSellerOrderStatus().equalsIgnoreCase(orderStatus))
				{
					orderStatus = line.getSellerOrderStatus();
					count++;
				}
				else
				{
					orderStatus = null;
				}
			}
			if (orderStatus != null && orderStatus.equalsIgnoreCase("PYMTSCSS"))
			{

				return OrderStatus.PAYMENT_CAPTURED;
			}
			if (orderStatus != null && orderStatus.equalsIgnoreCase("PYMTFLD"))
			{

				return OrderStatus.PAYMENT_FAILED;
			}
			if (orderStatus != null && orderStatus.equalsIgnoreCase("PYMTVRFN"))
			{

				return OrderStatus.RMS_VERIFICATION_PENDING;
			}

		}
		return null;
	}

	protected boolean isCompleted(final OrderWrapper order)
	{
		for (final ShipmentWrapper shipment : order.getShipments())
		{
			final ConsignmentStatus consignmentStatus = getConsignmentStatusMappingStrategy().getHybrisEnumFromDto(
					shipment.getShipment());

			if ((!(ConsignmentStatus.PICKUP_COMPLETE.equals(consignmentStatus)))
					&& (!(ConsignmentStatus.SHIPPED.equals(consignmentStatus))))
			{
				return false;
			}
		}

		return true;
	}

	protected ConsignmentStatus allShipmentCommonStatus(final OrderWrapper order)
	{
		final Set commonStatus = new HashSet();
		ConsignmentStatus shipmentStatus = null;

		for (final ShipmentWrapper shipment : order.getShipments())
		{
			shipmentStatus = getConsignmentStatusMappingStrategy().getHybrisEnumFromDto(shipment.getShipment());
			commonStatus.add(shipmentStatus);
			if (commonStatus.size() > 1)
			{
				return null;
			}
		}

		return shipmentStatus;
	}

	protected OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> getConsignmentStatusMappingStrategy()
	{
		return this.consignmentStatusMappingStrategy;
	}

	@Required
	public void setConsignmentStatusMappingStrategy(
			final OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> consignmentStatusMappingStrategy)
	{
		this.consignmentStatusMappingStrategy = consignmentStatusMappingStrategy;
	}
}
