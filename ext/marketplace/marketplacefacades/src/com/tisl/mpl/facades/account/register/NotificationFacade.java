
/**
 *
 */
package com.tisl.mpl.facades.account.register;

import java.util.List;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;


/**
 * @author TCS
 *
 */
public interface NotificationFacade
{
	List<NotificationData> getNotificationDetail(String customerUID, boolean isDesktop);

	List<NotificationData> getNotificationDetailForEmailID(String emailID, boolean isDesktop);

	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm);


	public void markNotificationRead(String customerId, String orderNo, String consignmentNo, final String shopperStatus);

	/**
	 * @param customerId
	 * @param orderNo
	 * @param consignmentNo
	 * @param shopperStatus
	 */
	void markNotificationReadForOriginalUid(String customerId, String orderNo, String consignmentNo, String shopperStatus);

	Integer getUnReadNotificationCount(List<NotificationData> notificationDatas);


}

