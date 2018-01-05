/**
 *
 */
package com.tisl.mpl.ordersync.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import com.tisl.mpl.order.ShipmentDTO;


/**
 * @author TCS
 *
 */
public class UnCollectedOrderToInitiateRefundEvent extends AbstractEvent
{
	private final ShipmentDTO shipment;
	private final ConsignmentModel consignmentModel;
	private final OrderModel orderModel;
	private final ConsignmentStatus shipmentNewStatus;
	private final EventService eventService;
	private final ConfigurationService configurationService;

	public UnCollectedOrderToInitiateRefundEvent(final ShipmentDTO shipment, final ConsignmentModel consignmentModel,
			final OrderModel orderModel, final ConsignmentStatus shipmentNewStatus, final EventService eventService,
			final ConfigurationService configurationService)
	{

		this.shipment = shipment;
		this.consignmentModel = consignmentModel;
		this.orderModel = orderModel;
		this.shipmentNewStatus = shipmentNewStatus;
		this.eventService = eventService;
		this.configurationService = configurationService;
	}

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

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
