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
public class DeliveryMethodForm
{
	private List<DeliveryMethodEntry> deliveryMethodEntry;

	/**
	 * @return the deliveryMethodEntry
	 */
	public List<DeliveryMethodEntry> getDeliveryMethodEntry()
	{
		return deliveryMethodEntry;
	}

	/**
	 * @param deliveryMethodEntry
	 *           the deliveryMethodEntry to set
	 */
	public void setDeliveryMethodEntry(final List<DeliveryMethodEntry> deliveryMethodEntry)
	{
		this.deliveryMethodEntry = deliveryMethodEntry;
	}
}
