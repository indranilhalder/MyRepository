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
public class RefundInitiatedEvent extends OrderProcessingEvent
{
	/**
	 * @param process
	 */
	public RefundInitiatedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
