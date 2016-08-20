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
public class PaymentFailedEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public PaymentFailedEvent(final OrderProcessModel process)
	{
		super(process);
	}
}
