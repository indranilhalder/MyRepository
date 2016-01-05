/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;


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

	List<VoucherModel> findVoucher();

	List<AbstractPromotionModel> getPromotion();

}
