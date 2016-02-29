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
public class OrderCollectedByPersonEvent extends OrderProcessingEvent
{

	/**
	 * @param process
	 */
	public OrderCollectedByPersonEvent(final OrderProcessModel process)
	{
		super(process);
	}

}
