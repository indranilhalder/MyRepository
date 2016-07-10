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
public class OrderRejectRiskEvent extends OrderProcessingEvent
{

	/**
	 * @param process
	 */
	public OrderRejectRiskEvent(final OrderProcessModel process)
	{
		super(process);

	}

}