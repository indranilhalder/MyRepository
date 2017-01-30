/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * @author tcs
 *
 */
public class NpsEmailEvent extends OrderProcessingEvent
{

	/**
	 * @param process
	 */
	public NpsEmailEvent(final OrderProcessModel process)
	{
		super(process);

	}

}
