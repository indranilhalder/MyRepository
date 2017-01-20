/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public interface MplMWalletRefundService
{

	/**
	 * @param refundAmount
	 * @param paymentTransactionType
	 * @return PaymentTransactionModel
	 */
	PaymentTransactionModel doRefund(final OrderModel order, final double refundAmount,
			final PaymentTransactionType paymentTransactionType, final String uniqueRequestId);

	/**
	 * @param order
	 * @return order
	 */
	boolean isCODOrder(AbstractOrderModel order);

	/**
	 * @param order
	 * @return order
	 */
	PaymentTypeModel getValidPaymentModeType(AbstractOrderModel order);



	String getRefundUniqueRequestId();

	/*
	 * @Desc used in web and cscockpit for handling network exception while cancellation
	 *
	 * @param orderRequestRecord
	 *
	 * @param paymentTransactionType
	 *
	 * @param juspayRefundType
	 *
	 * @param uniqueRequestId
	 *
	 * @return void
	 */

	/**
	 * @param orderRequestRecord
	 * @param paymentTransactionType
	 * @param uniqueRequestId
	 */
	void createCancelRefundExceptionEntry(OrderCancelRecordEntryModel orderRequestRecord,
			PaymentTransactionType paymentTransactionType, String uniqueRequestId);


	/*
	 * @Desc used in web and cscockpit for in case no response received from mRupee while cancellation refund
	 *
	 * @param orderRequestRecord
	 *
	 * @param paymentTransactionType
	 *
	 * @param juspayRefundType
	 *
	 * @param uniqueRequestId
	 *
	 * @return void
	 */

	void createCancelRefundPgErrorEntry(OrderCancelRecordEntryModel orderRequestRecord,
			PaymentTransactionType paymentTransactionType, String uniqueRequestId);


}
