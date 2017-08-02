/**
 *
 */
package com.tisl.mpl.pojo;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.io.Serializable;


/**
 * @author TCS
 *
 */
public class OmsOrderCancelTaskContext implements Serializable
{
	private final OrderData orderData;
	private final OrderEntryData orderEntryData;

	public OmsOrderCancelTaskContext(final OrderData orderData, final OrderEntryData orderEntryData)
	{
		this.orderData = orderData;
		this.orderEntryData = orderEntryData;
	}

	/**
	 * @return the orderData
	 */
	public OrderData getOrderData()
	{
		return orderData;
	}

	/**
	 * @return the orderEntryData
	 */
	public OrderEntryData getOrderEntryData()
	{
		return orderEntryData;
	}

}