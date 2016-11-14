/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface NotificationFacade
{
	List<NotificationData> getNotificationDetail(String customerUID, boolean isDesktop) throws EtailNonBusinessExceptions;

	List<NotificationData> getNotificationDetailForEmailID(String emailID, boolean isDesktop);

	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm);


	public void markNotificationRead(String customerId, String orderNo, String consignmentNo, final String shopperStatus)
			throws EtailNonBusinessExceptions;

	/**
	 * @param customerId
	 * @param orderNo
	 * @param consignmentNo
	 * @param shopperStatus
	 */
	void markNotificationReadForOriginalUid(String customerId, String orderNo, String consignmentNo, String shopperStatus)
			throws EtailNonBusinessExceptions;

	Integer getUnReadNotificationCount(List<NotificationData> notificationDatas);

	/**
	 * TPR-629
	 * 
	 * @param orderModel
	 */
	void sendOrderConfirmationNotification(OrderModel orderModel);


}
