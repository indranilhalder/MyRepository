/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author 666220
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
	public PaymentTransactionModel doRefund(final OrderModel order, final double refundAmount,
			final PaymentTransactionType paymentTransactionType) throws Exception;

	/**
	 *
	 * @param orderId
	 * @throws Exception
	 */
	public void doRefund(final String orderId, final String paymentType);

	/**
	 *
	 * @param orderModel
	 * @param paymentTransactionModel
	 * @return
	 */
	public boolean attachPaymentTransactionModel(OrderModel orderModel, PaymentTransactionModel paymentTransactionModel);

	/**
	 *
	 * @param order
	 * @param paymentTransactionEntryModel
	 * @param paymentTransactionModel
	 * @return
	 */
	public boolean checkAndAttachPaymentTransactionModel(final OrderModel order,
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
	public PaymentTransactionModel createPaymentTransactionModel(OrderModel orderModel, String status, Double amount,
			PaymentTransactionType paymentTransactionType, String statusDetails, String code);

	/**
	 *
	 * @param orderEntry
	 * @param paymentTransactionModel
	 * @param amount
	 * @return
	 */
	public boolean makeRefundOMSCall(final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel, final Double amount, ConsignmentStatus newOrderLineStatus);

	/**
	 *
	 * @param orderEntry
	 * @return
	 */
	public boolean makeOMSStatusUpdate(AbstractOrderEntryModel orderEntry, ConsignmentStatus newOrderLineStatus);

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
	public double validateRefundAmount(double refundAmount, OrderModel subOrderModel);


}
