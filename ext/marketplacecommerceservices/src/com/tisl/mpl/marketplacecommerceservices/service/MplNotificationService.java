/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;


/**
 * @author TCS
 *
 */
public interface MplNotificationService
{
	public void saveToNotification(final OrderStatusNotificationModel osnm);
}