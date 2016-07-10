/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.CartModel;
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
	List<PaymentTransactionEntryModel> createPaymentTranEntry(GetOrderStatusResponse getOrderStatusResponse, CartModel cart,
			Entry<String, Double> entry, List<PaymentTransactionEntryModel> paymentTransactionEntryList);

	/**
	 * @param cart
	 * @param orderStatusResponse
	 * @param paymentTransactionEntryList
	 * @param paymentTransactionList
	 * @return List<PaymentTransactionModel>
	 */
	List<PaymentTransactionModel> createPaymentTransaction(CartModel cart, GetOrderStatusResponse orderStatusResponse,
			List<PaymentTransactionEntryModel> paymentTransactionEntryList, List<PaymentTransactionModel> paymentTransactionList);

}
