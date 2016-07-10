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
public class OrderDeliveryEvent extends OrderProcessingEvent
{
	/**
	 * @param process
	 */
	public OrderDeliveryEvent(final OrderProcessModel process)
	{
		super(process);
	}
}