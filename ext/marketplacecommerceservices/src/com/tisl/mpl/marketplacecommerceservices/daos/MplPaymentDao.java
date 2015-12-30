/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.util.List;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public interface MplPaymentDao
{
	/**
	 * This method returns the list of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) for the specific
	 * store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return List<PaymentTypeModel>
	 *
	 */
	List<PaymentTypeModel> getPaymentTypes(String store);

	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	List<BankforNetbankingModel> getBanksByPriority();


	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @return List<EMIBankModel>
	 */
	List<EMIBankModel> getEMIBanks(Double cartValue);


	/**
	 * This method returns the EMI terms available for the selected bank.
	 *
	 * @param bank
	 * @return List<EMIBankModel>
	 */
	List<EMIBankModel> getEMIBankTerms(String bank);


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	List<BankforNetbankingModel> getOtherBanks();


	/**
	 * This method returns the list of the PaymentTypeModel whose mode is same as that present in PaymentTransactionEntry
	 *
	 * @param paymentType
	 * @return PaymentTypeModel
	 */
	PaymentTypeModel getPaymentTypeForApportion(String paymentType);


	/**
	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	 * success
	 *
	 * @param juspayOrderId
	 * @param mplCustomerID
	 * @return PaymentTransactionModel
	 */
	PaymentTransactionModel getOrderStatusFromCart(String juspayOrderId, String mplCustomerID);

	/**
	 * This method is used to get the PaymentTypeModel against the mode name
	 *
	 * @param paymentMode
	 * @return PaymentTypeModel
	 */
	PaymentTypeModel getPaymentMode(String paymentMode);

	/**
	 * This method is used to get the Country ISO
	 *
	 * @param countryName
	 * @return String
	 */
	String getCountryISO(String countryName);

	/**
	 * This method is used to get the List of Countries
	 *
	 * @return List<CountryModel>
	 */
	List<CountryModel> getCountries();

	/**
	 * This method takes the juspayOrderId as parameter and fetches any entry present in Audit Model
	 *
	 * @param auditId
	 * @return MplPaymentAuditModel
	 */
	MplPaymentAuditModel getAuditEntries(String auditId);


	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param uid
	 * @return CustomerModel
	 *
	 */
	CustomerModel getCustomer(String uid);

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param cartGuid
	 * @return CartModel
	 *
	 */
	CartModel getCart(String cartGuid);

}
