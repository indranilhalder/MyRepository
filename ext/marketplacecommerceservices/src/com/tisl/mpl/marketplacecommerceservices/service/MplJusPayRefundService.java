/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public interface MplJusPayRefundService
{


	/**
	 * @param OrderEntryId
	 * @param refundAmount
	 * @param paymentTransactionType
	 * @return
	 */
	PaymentTransactionModel doRefund(final OrderModel order, final double refundAmount,
			final PaymentTransactionType paymentTransactionType, final String uniqueRequestId) throws Exception;

	/**
	 *
	 * @param orderId
	 * @throws Exception
	 */
	void doRefund(final String orderId, final String paymentType);

	/**
	 *
	 * @param orderModel
	 * @param paymentTransactionModel
	 * @return
	 */
	boolean attachPaymentTransactionModel(OrderModel orderModel, PaymentTransactionModel paymentTransactionModel);

	/**
	 *
	 * @param order
	 * @param paymentTransactionEntryModel
	 * @param paymentTransactionModel
	 * @return
	 */
	boolean checkAndAttachPaymentTransactionModel(final OrderModel order,
			final PaymentTransactionEntryModel paymentTransactionEntryModel, final PaymentTransactionModel paymentTransactionModel);


	/**
	 *
	 * @param orderModel
	 * @param status
	 * @param amount
	 * @param paymentTransactionType
	 * @param statusDetails
	 * @param code
	 * @return
	 */
	PaymentTransactionModel createPaymentTransactionModel(OrderModel orderModel, String status, Double amount,
			PaymentTransactionType paymentTransactionType, String statusDetails, String code);

	/**
	 *
	 * @param orderEntry
	 * @param paymentTransactionModel
	 * @param amount
	 * @return
	 */
	boolean makeRefundOMSCall(final AbstractOrderEntryModel orderEntry, final PaymentTransactionModel paymentTransactionModel,
			final Double amount, ConsignmentStatus newOrderLineStatus);

	/**
	 *
	 * @param orderEntry
	 * @return
	 */
	boolean makeOMSStatusUpdate(AbstractOrderEntryModel orderEntry, ConsignmentStatus newOrderLineStatus);

	/**
	 * @param order
	 * @return
	 */
	PaymentTypeModel getValidPaymentModeType(AbstractOrderModel order);

	/**
	 * @param order
	 * @return
	 */
	boolean isCODOrder(AbstractOrderModel order);

	/**
	 * @param refundAmount
	 * @param subOrderModel
	 * @return
	 */
	double validateRefundAmount(double refundAmount, OrderModel subOrderModel);


	/*
	 * @Desc used in web and cscockpit for handling network exception while cancellation TISSIT-1801 TISPRO-94
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

	void createCancelRefundExceptionEntry(OrderCancelRecordEntryModel orderRequestRecord,
			PaymentTransactionType paymentTransactionType, JuspayRefundType juspayRefundType, String uniqueRequestId);


	/*
	 * @Desc used in web and cscockpit for in case no response received from juspay while cancellation refund TISSIT-1801
	 * TISPRO-94
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
			PaymentTransactionType paymentTransactionType, JuspayRefundType juspayRefundType, String uniqueRequestId);


	String getRefundUniqueRequestId();


}
