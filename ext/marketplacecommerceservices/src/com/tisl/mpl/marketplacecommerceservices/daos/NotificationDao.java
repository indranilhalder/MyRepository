/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.util.List;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.VoucherStatusNotificationModel;


/**
 * @author TCS
 *
 */
public interface NotificationDao
{

	List<OrderStatusNotificationModel> getNotificationDetail(String email, boolean isDesktop);

	public List<OrderStatusNotificationModel> checkCustomerFacingEntry(final String customerId, final String orderId,
			final String transactionId, final String customerStatus);


	public List<OrderStatusNotificationModel> getModelforDetails(final String customerId, final String orderId,
			final String transactionId, final String shopperStatus);

	List<OrderStatusNotificationModel> getNotification(final String customerId, final String orderId, final String transactionId,
			final String orderStatus);

	List<VoucherStatusNotificationModel> findVoucher();

	List<AbstractPromotionModel> getPromotion();


	boolean checkIsUpdated(String voucherCode);


	List<VoucherStatusNotificationModel> getModelForVoucher(final String voucherIndentifier);

	List<VoucherStatusNotificationModel> getModelForVoucherIdentifier(final String voucherCode);

}
