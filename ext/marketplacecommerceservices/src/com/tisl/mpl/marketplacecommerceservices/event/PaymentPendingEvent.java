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
public class PaymentPendingEvent extends OrderProcessingEvent
{
	private static final long serialVersionUID = 1L;

	public PaymentPendingEvent(final OrderProcessModel process)
	{
		super(process);
	}
}