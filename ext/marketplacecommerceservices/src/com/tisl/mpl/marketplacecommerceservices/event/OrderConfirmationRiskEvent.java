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
public class OrderConfirmationRiskEvent extends OrderProcessingEvent
{
	/**
	 * @param process
	 */
	private static final long serialVersionUID = 1L;

	public OrderConfirmationRiskEvent(final OrderProcessModel process)
	{
		super(process);

	}
}
