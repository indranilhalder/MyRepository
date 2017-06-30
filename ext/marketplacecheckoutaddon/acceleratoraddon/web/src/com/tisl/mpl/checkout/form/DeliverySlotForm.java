/**
 *
 */
package com.tisl.mpl.checkout.form;

import java.util.List;



/**
 * @author TCS
 *
 */
/*
 * @description Used for fetching the delivery mode details against ussid while delivery mode selection in Checkout
 * journey
 */
public class DeliverySlotForm
{
	private List<DeliverySlotEntry> deliverySlotEntry;

	/**
	 * @return the deliverySlotEntry
	 */
	public List<DeliverySlotEntry> getDeliverySlotEntry()
	{
		return deliverySlotEntry;
	}

	/**
	 * @param deliverySlotEntry
	 *           the deliverySlotEntry to set
	 */
	public void setDeliverySlotEntry(final List<DeliverySlotEntry> deliverySlotEntry)
	{
		this.deliverySlotEntry = deliverySlotEntry;
	}


}
