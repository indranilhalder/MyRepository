/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedPushNotificationEvent;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;


/**
 *
 * @author Jagadeeswar Venkatasubbu
 *
 *         Listener for OrderPlacedPushNotificationEvent. Used to send push notification for the placed order.
 *
 */
public class OrderConfirmationPushNotificationEventListener extends AbstractSiteEventListener<OrderPlacedPushNotificationEvent>
{

	private static final Logger LOG = Logger.getLogger(OrderConfirmationPushNotificationEventListener.class);

	private NotificationService notificationService;

	/**
	 *
	 * @author Jagadeeswar Venkatasubbu
	 * @see de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 *
	 *      Sends mobile push notification for the order placed.
	 */
	@Override
	protected void onSiteEvent(final OrderPlacedPushNotificationEvent orderPlacedPushNotificationEvent)
	{
		final OrderModel orderModel = orderPlacedPushNotificationEvent.getProcess().getOrder();
		try
		{
			getNotificationService().sendMobileNotifications(orderModel);
		}
		catch (final Exception e)
		{
			LOG.error("Mobile push notification failed for order number " + orderModel.getCode(), e);
		}

	}

	/**
	 *
	 * @author Jagadeeswar Venkatasubbu
	 * @see de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final OrderPlacedPushNotificationEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}

	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}
}
