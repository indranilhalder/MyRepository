/**
 *
 */
package com.tisl.mpl.ordersync.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import com.tisl.mpl.order.ShipmentDTO;


/**
 * @author TCS
 *
 */
public class SendNotificationEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

	/**
	 *
	 * @param shipment
	 * @param consignmentModel
	 * @param orderModel
	 * @param shipmentNewStatus
	 */
	public SendNotificationEvent(final ShipmentDTO shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel,
			final ConsignmentStatus shipmentNewStatus)
	{

		this.shipment = shipment;
		this.consignmentModel = consignmentModel;
		this.orderModel = orderModel;
		this.shipmentNewStatus = shipmentNewStatus;
	}

	private final ShipmentDTO shipment;
	private final ConsignmentModel consignmentModel;
	private final OrderModel orderModel;
	private final ConsignmentStatus shipmentNewStatus;

	/**
	 * @return the shipment
	 */
	public ShipmentDTO getShipment()
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
