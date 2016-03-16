/**
 *
 */
package com.tisl.mpl.dao;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;

import java.util.List;

import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 *
 * @author TCS
 */
public interface MplPaymentWebDAO
{
	/**
	 * to get cart data from DB
	 *
	 * @param cartID
	 * @return CartModel
	 */

	CartModel findCartValues(String cartID);

	/**
	 * to get cart data from DB
	 *
	 * @param guid
	 * @return CartModel
	 */

	CartModel findCartValuesAnonymous(String guid);

	/**
	 * to get payment mode from DB
	 *
	 * @param paymentMode
	 * @return PaymentTypeModel
	 */

	PaymentTypeModel getPaymentMode(String paymentMode);


	/**
	 * to get Billing Address
	 *
	 * @param originalUid
	 * @param cardRefNo
	 * @return SavedCardModel
	 */

	SavedCardModel getBillingAddress(final String originalUid, final String cardRefNo);


	/**
	 * This method fetches the details of bankModel with Bank Name
	 *
	 * @param bankName
	 * @return BankModel
	 * @throws EtailNonBusinessExceptions
	 */
	BankModel savedCardBankFromBin(final String bankName) throws EtailNonBusinessExceptions;

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param userId
	 * @return CustomerModel
	 *
	 */
	CustomerModel getCustomer(String userId);

	/**
	 * This method returns the OrderPromotion Model where order is enable
	 * 
	 * @return OrderPromotionModel
	 *
	 */
	List<OrderPromotionModel> orderPromotions();
}