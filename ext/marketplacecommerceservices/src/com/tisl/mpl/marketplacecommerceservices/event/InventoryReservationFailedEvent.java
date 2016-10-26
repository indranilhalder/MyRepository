/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * @author TCS
 *
 */
public class InventoryReservationFailedEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public InventoryReservationFailedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
