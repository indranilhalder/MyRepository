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
public class OrderOnHoldEvent extends OrderProcessingEvent
{
	/**
	 * @param process
	 */
	public OrderOnHoldEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
