/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

/**
 * @author PankajS
 *
 */
public class OrderEGVRecipientEmailEvent extends OrderProcessingEvent
{
	
	public OrderEGVRecipientEmailEvent(OrderProcessModel recipientEmailProcesses)
	{
		super(recipientEmailProcesses);
	}
}
