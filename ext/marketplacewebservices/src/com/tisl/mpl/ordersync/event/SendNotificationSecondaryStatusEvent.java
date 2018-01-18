/**
 *
 */
package com.tisl.mpl.ordersync.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;


/**
 * @author TCS
 *
 */
public class SendNotificationSecondaryStatusEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

	/**
	 *
	 * @param orderModel
	 */
	public SendNotificationSecondaryStatusEvent(final String awbSecondaryStatus, final String orderLineID,
			final OrderModel orderModel)
	{

		this.awbSecondaryStatus = awbSecondaryStatus;
		this.orderLineID = orderLineID;
		this.orderModel = orderModel;
	}

	private final String awbSecondaryStatus;

	/**
	 * @return the awbSecondaryStatus
	 */
	public String getAwbSecondaryStatus()
	{
		return awbSecondaryStatus;
	}


	/**
	 * @return the orderLineID
	 */
	public String getOrderLineID()
	{
		return orderLineID;
	}

	private final String orderLineID;
	private final OrderModel orderModel;


	/**
	 * @return the orderModel
	 */
	public OrderModel getOrderModel()
	{
		return orderModel;
	}


}
