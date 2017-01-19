/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.util.List;
import java.util.Map.Entry;

import com.tisl.mpl.juspay.response.GetOrderStatusResponse;


/**
 * @author TCS
 *
 */
public interface MplPaymentTransactionService
{

	/**
	 * @param getOrderStatusResponse
	 * @param cart
	 * @param entry
	 * @param paymentTransactionEntryList
	 * @return List<PaymentTransactionEntryModel>
	 */
	List<PaymentTransactionEntryModel> createPaymentTranEntry(GetOrderStatusResponse getOrderStatusResponse,
			AbstractOrderModel cart, Entry<String, Double> entry, List<PaymentTransactionEntryModel> paymentTransactionEntryList);

	/**
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @param paymentTransactionList
	 * @return List<PaymentTransactionModel>
	 */
	//List<PaymentTransactionModel> createPaymentTransaction(AbstractOrderModel cart, GetOrderStatusResponse orderStatusResponse,
	//		List<PaymentTransactionEntryModel> paymentTransactionEntryList, List<PaymentTransactionModel> paymentTransactionList);

	/**
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @return PaymentTransactionModel
	 */
	PaymentTransactionModel createPaymentTransaction(AbstractOrderModel cart, GetOrderStatusResponse orderStatusResponse,
			List<PaymentTransactionEntryModel> paymentTransactionEntryList);

	/**
	 * @param order
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @return PaymentTransactionModel
	 * @desc SprintPaymentFixes:-:- To handle missing paymentTransaction for specific order
	 */
	PaymentTransactionModel createPaymentTranFromSubmitOrderJob(OrderModel order, GetOrderStatusResponse orderStatusResponse,
			List<PaymentTransactionEntryModel> paymentTransactionEntryList);


	/**
	 * @param getOrderStatusResponse
	 * @param order
	 * @param entry
	 * @param paymentTransactionEntryList
	 * @return List<PaymentTransactionEntryModel>
	 * @desc SprintPaymentFixes:-:- To handle missing paymentTransaction for specific order
	 */
	List<PaymentTransactionEntryModel> createPaymentTranEntryFromSubmitOrderJob(GetOrderStatusResponse getOrderStatusResponse,
			OrderModel cart, Entry<String, Double> entry, List<PaymentTransactionEntryModel> paymentTransactionEntryList);

}
