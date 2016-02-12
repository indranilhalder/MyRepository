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

	public CartModel findCartValues(String cartID);

	/**
	 * to get cart data from DB
	 *
	 * @param guid
	 * @return CartModel
	 */

	public CartModel findCartValuesAnonymous(String guid);

	/**
	 * to get payment mode from DB
	 *
	 * @param paymentMode
	 * @return PaymentTypeModel
	 */

	public PaymentTypeModel getPaymentMode(String paymentMode);


	/**
	 * to get Billing Address
	 *
	 * @param originalUid
	 * @param cardRefNo
	 * @return SavedCardModel
	 */

	public SavedCardModel getBillingAddress(final String originalUid, final String cardRefNo);

	/**
	 * This method fetches the details of bankModel with bin
	 *
	 * @param bin
	 * @return BankModel
	 * @throws EtailNonBusinessExceptions
	 */
	public BankModel fetchBankFromBin(final String bin) throws EtailNonBusinessExceptions;


	/**
	 * This method fetches the details of bankModel with Bank Name
	 *
	 * @param bankName
	 * @return BankModel
	 * @throws EtailNonBusinessExceptions
	 */
	public BankModel savedCardBankFromBin(final String bankName) throws EtailNonBusinessExceptions;

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param userId
	 * @return CustomerModel
	 *
	 */
	public CustomerModel getCustomer(String userId);

	/**
	 * This method returns the OrderPromotion Model where order is enable
	 *
	 * @param userId
	 * @return OrderPromotionModel
	 *
	 */
	public List<OrderPromotionModel> orderPromotions();
}