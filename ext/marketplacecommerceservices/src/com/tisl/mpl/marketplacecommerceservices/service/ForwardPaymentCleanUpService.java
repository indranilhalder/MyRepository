/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.FPCRefundEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface ForwardPaymentCleanUpService
{
	List<OrderModel> fetchOrdersWithMultiplePayments(final Date startTime, final Date endTime);
	List<OrderModel> fetchCliqCashOrdersWithMultiplePayments(final Date startTime, final Date endTime);


	List<OrderModel> fetchPaymentFailedOrders(final Date startTime, final Date endTime);

	List<OrderModel> fetchRmsFailedOrders(final Date startTime, final Date endTime);

	List<MplPaymentAuditModel> fetchAuditsWithoutOrder(final Date startTime, final Date endTime);

	List<FPCRefundEntryModel> fetchSpecificRefundEntries(String expiredFlag);

	MplConfigurationModel fetchConfigDetails(final String code);

	void createRefundEntryForMultiplePayments(final OrderModel orderModel);

	void createRefundEntryForFailedOrders(final OrderModel orderModel);

	void createRefundEntryForRmsFailedOrders(final OrderModel orderModel);

	void createRefundEntryForAuditsWithoutOrder(final MplPaymentAuditModel auditModel);

	void processRefund(final FPCRefundEntryModel refundEntry);

	void expireRefundEntry(final FPCRefundEntryModel refundEntry);
}
