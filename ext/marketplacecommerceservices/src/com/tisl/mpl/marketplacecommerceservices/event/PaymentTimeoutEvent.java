/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * @author 880282
 *
 */
public class PaymentTimeoutEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public PaymentTimeoutEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
