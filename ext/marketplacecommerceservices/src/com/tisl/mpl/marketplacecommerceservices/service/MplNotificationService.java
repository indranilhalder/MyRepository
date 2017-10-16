/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;


/**
 * @author TCS
 *
 */
public interface MplNotificationService
{
	public void saveToNotification(final OrderStatusNotificationModel osnm);

	// added for TPR-1348 AutoRefund initiation
	public void sendRefundInitiatedNotification(int noOfItems, ReturnEntryModel returnEntryTmp, OrderModel orderModel);
}