/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.io.Serializable;


/**
 * @author TCS
 *
 */
public class InventoryReservationFailedEvent extends AbstractEvent
{
	private static final long serialVersionUID = 1L;

	private OrderModel order;

	public InventoryReservationFailedEvent()
	{
		super();
	}

	/**
	 * Attention: for backward compatibility this constructor invokes
	 *
	 * <pre>
	 * setOrder(source)
	 * </pre>
	 *
	 * in case the source object is a OrderModel!
	 */
	public InventoryReservationFailedEvent(final Serializable source)
	{
		super(source);

		// compatibility!
		if (source instanceof OrderModel)
		{
			setOrder((OrderModel) source);
		}
	}


	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}


	public OrderModel getOrder()
	{
		return order;
	}

	public InventoryReservationFailedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
