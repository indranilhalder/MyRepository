/**
 * 
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;

import com.tisl.mpl.core.model.ReturnQuickDropProcessModel;

/**
 * @author TO-OW101
 *
 */
public class OrderReturnToStoreEvent  extends OrderProcessingEvent
{

	/**
	 * @param process
	 */
	public OrderReturnToStoreEvent(ReturnQuickDropProcessModel process)
	{
		super(process);
	}

}
