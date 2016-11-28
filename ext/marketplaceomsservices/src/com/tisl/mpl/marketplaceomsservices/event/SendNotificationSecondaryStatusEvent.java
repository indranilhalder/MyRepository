/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import com.hybris.oms.domain.shipping.Shipment;

/**
 * @author pankajk
 *
 */
public class SendNotificationSecondaryStatusEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

	/**
	 *
	 * @param shipment
	 * @param consignmentModel
	 * @param orderModel
	 * @param secondaryShipmentNewStatus
	 */
	public SendNotificationSecondaryStatusEvent(final Shipment shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel,
			final ConsignmentStatus secondaryShipmentNewStatus)
	{

		this.shipment = shipment;
		this.consignmentModel = consignmentModel;
		this.orderModel = orderModel;
		this.secondaryShipmentNewStatus = secondaryShipmentNewStatus;
	}

	private final Shipment shipment;
	private final ConsignmentModel consignmentModel;
	private final OrderModel orderModel;
	private final ConsignmentStatus secondaryShipmentNewStatus;
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
	 * @return the secondaryShipmentNewStatus
	 */
	public ConsignmentStatus getSecondaryShipmentNewStatus()
	{
		return secondaryShipmentNewStatus;
	}


}
