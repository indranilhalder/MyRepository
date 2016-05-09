/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * @author Dileep
 *
 */
public class OrderRefundCreditedEvent extends OrderProcessingEvent
{

	/**
	 * @param process
	 */
	public OrderRefundCreditedEvent(final OrderProcessModel process)
	{
		super(process);
	}

}
