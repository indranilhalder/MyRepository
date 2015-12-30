/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;


/**
 * @author TCS
 *
 */
public interface NotificationDao
{
	/**
	 * @param email
	 * @return
	 */
	List<OrderStatusNotificationModel> getNotificationDetail(String email, boolean isDesktop);

	public List<OrderStatusNotificationModel> checkCustomerFacingEntry(final String customerId, final String orderId,
			final String transactionId, final String customerStatus);


	public List<OrderStatusNotificationModel> getModelforDetails(final String customerId, final String orderId,
			final String transactionId, final String shopperStatus);

	List<OrderStatusNotificationModel> getNotification(final String customerId, final String orderId, final String transactionId,
			final String orderStatus);

}
