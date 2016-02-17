package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public interface MplPaymentService
{
	/**
	 * This method returns the list of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) for the specific
	 * store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return List<PaymentTypeModel>
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	List<PaymentTypeModel> getPaymentModes(String store) throws EtailNonBusinessExceptions;

	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 * @throws EtailNonBusinessExceptions
	 */
	List<BankforNetbankingModel> getBanksByPriority() throws EtailNonBusinessExceptions;

	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 * @throws EtailNonBusinessExceptions
	 */
	List<BankforNetbankingModel> getOtherBanks() throws EtailNonBusinessExceptions;

	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @return List<EMIBankModel>
	 */
	List<EMIBankModel> getEMIBanks(Double cartValue) throws EtailNonBusinessExceptions;



	/**
	 * This method takes the totalAmount as parameter and then calculates the emi using the emi calculation logic. It the
	 * returns an ArrayList of EMITermrateData for the selected bank to be displayed on the payment page.
	 *
	 * @param bank
	 * @param totalAmount
	 * @return ArrayList<EMITermRateData>
	 */
	List<EMITermRateData> getBankTerms(String bank, Double totalAmount);

	/**
	 * This method saves the card related info fetching from response from Juspay Order Status service
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 */
	void saveCardDetailsFromJuspay(GetOrderStatusResponse orderStatusResponse, Map<String, Double> paymentMode, CartModel cart);


	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param entries
	 */
	void saveCODPaymentInfo(String custName, Double cartValue, Double totalCODCharge, List<AbstractOrderEntryModel> entries,
			CartModel cartModel);


	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for non-COD
	 * payment modes including wallet
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 *
	 */
	void setPaymentTransaction(GetOrderStatusResponse orderStatusResponse, Map<String, Double> paymentMode, CartModel cart);


	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for COD and
	 * wallet
	 *
	 * @param paymentMode
	 * @param cart
	 *
	 */
	void setPaymentTransactionForCOD(Map<String, Double> paymentMode, CartModel cart);


	/**
	 * This method helps to save apportion for the PaymentModes which were successful
	 *
	 */
	void paymentModeApportion(final CartModel cart);


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
	 * This method returns the list of Countries
	 *
	 * @return List<String>
	 */
	List<String> getCountries();


	/**
	 *
	 * @param cartData
	 * @param cart
	 * @return MplPromoPriceData
	 * @throws JaloPriceFactoryException
	 * @throws JaloSecurityException
	 * @throws CalculationException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 */
	MplPromoPriceData applyPromotions(final CartData cartData, final CartModel cart)
			throws ModelSavingException, NumberFormatException, JaloInvalidParameterException, VoucherOperationException,
			CalculationException, JaloSecurityException, JaloPriceFactoryException;


	/**
	 * This method creates an entry in the audit table after an Id has been generated for Juspay
	 *
	 *
	 * @param juspayOrderId
	 * @param channel
	 * @param cartGuId
	 * @return boolean
	 */
	boolean createEntryInAudit(final String juspayOrderId, final String channel, final String cartGuId);


	/**
	 * This method generates an ID for Juspay
	 *
	 * @return String
	 */
	String createPaymentId();

	/**
	 * This method updates already created entry in the Audit Table with response from Juspay
	 *
	 * @param orderStatusResponse
	 * @return boolean
	 */
	boolean updateAuditEntry(GetOrderStatusResponse orderStatusResponse);

	/**
	 * This method sets the EbsRiskStatus in JuspayEBSResponseModel
	 *
	 * @param riskStatus
	 * @param juspayEBSResponseModel
	 */
	void setEBSRiskStatus(String riskStatus, JuspayEBSResponseModel juspayEBSResponseModel);

	/**
	 * This method sets the EbsRiskLevel in JuspayEBSResponseModel
	 *
	 * @param riskLevel
	 * @param juspayEBSResponseModel
	 *
	 */
	void setEBSRiskLevel(String riskLevel, JuspayEBSResponseModel juspayEBSResponseModel);

	/**
	 * @param orderStatusResponse
	 * @param cart
	 * @param sameAsShipping
	 */
	void saveCreditCard(GetOrderStatusResponse orderStatusResponse, CartModel cart, String sameAsShipping);

	/**
	 * @param orderStatusResponse
	 * @param cart
	 */
	void saveDebitCard(GetOrderStatusResponse orderStatusResponse, CartModel cart);

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param uid
	 * @return CustomerModel
	 *
	 */
	CustomerModel getCustomer(String uid);

	/**
	 * This method returns the last payment done to fetch risk
	 *
	 * @param orderId
	 * @return MplPaymentAuditModel
	 *
	 */
	JuspayEBSResponseModel getEntryInAuditByOrder(final String orderId);

}
