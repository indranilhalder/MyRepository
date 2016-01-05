/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.AllVoucherListData;
import com.tisl.mpl.data.NotificationData;


/**
 * @author TCS
 *
 */
public interface NotificationService
{
	List<NotificationData> getNotification();

	List<OrderStatusNotificationModel> getNotificationDetails(String email, boolean isDesktop);

	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm);


	public void markNotificationRead(final String customerId, final String orderNo, final String consignmentNo,
			final String shopperStatus);



	List<OrderStatusNotificationModel> isAlreadyNotified(final String customerId, final String orderNo,
			final String transactionId, final String orderStatus);

	void triggerEmailAndSmsOnOrderConfirmation(OrderModel order, String trackorderurl) throws JAXBException;

	void sendMobileNotifications(final OrderModel orderDetails);


	List<VoucherModel> getVoucher();

	List<AbstractPromotionModel> getPromotion();

	List<NotificationData> getSortedNotificationData(List<NotificationData> notificationDataList);

	AllVoucherListData getAllVoucherList(CustomerModel currentCustomer, List<VoucherModel> voucherList);


}
