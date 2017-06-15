/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;


/**
 * @author 1047493
 *
 */
public interface JuspayPaymentService
{
	//void getTransactionModel(CartModel cart);

	//void getPaymentTransactionEntryModel(PaymentTransactionModel paymentTransactionModel, CartModel cart);

	/**
	 * @param cart
	 * @param amount
	 */
	void getTransactionModel(CartModel cart, Double amount);

	/**
	 * @param paymentTransactionModel
	 * @param cart
	 * @param amount
	 */
	void getPaymentTransactionEntryModel(PaymentTransactionModel paymentTransactionModel, CartModel cart, Double amount);

	public void createJusPayPaymentInfo(final CartModel cart);
}
