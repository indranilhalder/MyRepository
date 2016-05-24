/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import com.hybris.oms.domain.shipping.Shipment;

/**
 * @author Tech
 *
 */
public class SendUnColletedOrderToCRMEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	private final Shipment shipment;
	private final ConsignmentModel consignmentModel;
	private final OrderModel orderModel;
	private final ConsignmentStatus shipmentNewStatus;
	
	public SendUnColletedOrderToCRMEvent(final Shipment shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel, final ConsignmentStatus shipmentNewStatus)
	{

		this.shipment = shipment;
		this.consignmentModel = consignmentModel;
		this.orderModel = orderModel;
		this.shipmentNewStatus = shipmentNewStatus;
	}

	/**
	 * @return the shipment
	 */
	public Shipment getShipment()
	{
		return shipment;
	}

	/**
	 * @return the consignmentModel
	 */
	public ConsignmentModel getConsignmentModel()
	{
		return consignmentModel;
	}

	/**
	 * @return the orderModel
	 */
	public OrderModel getOrderModel()
	{
		return orderModel;
	}

	/**
	 * @return the shipmentNewStatus
	 */
	public ConsignmentStatus getShipmentNewStatus()
	{
		return shipmentNewStatus;
	}


}
