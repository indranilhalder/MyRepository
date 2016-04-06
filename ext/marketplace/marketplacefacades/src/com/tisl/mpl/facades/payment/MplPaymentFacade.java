package com.tisl.mpl.facades.payment;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplNetbankingData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplPaymentFacade
{

	/**
	 * This method returns the map of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) and their
	 * availability for the specific store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return Map<String, Boolean>
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	Map<String, Boolean> getPaymentModes(String store) throws EtailNonBusinessExceptions;


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @return Map<String, String>
	 *
	 */
	List<MplNetbankingData> getBanksByPriority() throws EtailNonBusinessExceptions;


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @return Map<String, String>
	 *
	 */
	Map<String, String> getOtherBanks() throws EtailNonBusinessExceptions;


	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @return Map<String, String>
	 */
	Map<String, String> getEMIBankNames(Double cartValue);


	/**
	 * This method takes the totalAmount as parameter and then calculates the emi using the emi calculation logic. It the
	 * returns an ArrayList of EMITermrateData for the selected bank to be displayed on the payment page.
	 *
	 * @param bank
	 * @param totalAmount
	 * @return ArrayList<EMITermRateData>
	 */
	List<EMITermRateData> getBankTerms(String bank, Double totalAmount) throws EtailNonBusinessExceptions;


	/**
	 * This method takes the customerID as parameter and calls the service to generate OTP
	 *
	 * @param customerID
	 * @return otp
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTPforCODWeb(String customerID, String mobileNumber, String mplCustomerName, String cartId)
			throws InvalidKeyException, NoSuchAlgorithmException;


	/**
	 * This method takes the customer ID and enter OTP as input parameters and calls the service to validate the entered
	 * OTP
	 *
	 * @param customerID
	 * @param enteredOTPNumber
	 * @return OTPResponseData
	 */
	String validateOTPforCODWeb(String customerID, String enteredOTPNumber);


	/**
	 * This method takes the customer ID as input parameters and calls the service to check whether the customer is
	 * blacklisted or not.
	 *
	 * @param mplCustomerID
	 * @return boolean
	 */
	boolean isBlackListed(String mplCustomerID, final CartModel cart) throws EtailNonBusinessExceptions;


	/**
	 * This method is used to save the cart details
	 *
	 * @param cart
	 */
	void saveCart(CartModel cart);


	/**
	 * This method sends request to JusPay to get the Order Status
	 *
	 * @return String
	 *
	 */
	String getOrderStatusFromJuspay();


	/**
	 * This method fetches the stored cards from Juspay and returns them back to the Controller
	 *
	 * @param customer
	 * @return Map<Date, SavedCardData>
	 *
	 */
	Map<Date, SavedCardData> listStoredCreditCards(final CustomerModel customer);


	/**
	 * This method saves Payment info in case of COD mode of Payment
	 *
	 * @param cartValue
	 * @param totalCODCharge
	 */
	//TISPRD-361
	void saveCODPaymentInfo(Double cartValue, Double totalCODCharge) throws EtailNonBusinessExceptions, Exception;


	/**
	 * This method fetches the customer's phone number from Delivery address
	 *
	 * @param cart
	 * @return String
	 */
	String fetchPhoneNumber(final CartModel cart);


	/**
	 * This method sends request to JusPay to get the Order Status everytime the user enters the payment screen
	 *
	 * @param orderId
	 * @return String
	 *
	 */
	String getJuspayOrderStatus(String orderId);


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
	 * This method takes the Mobile Number as input parameters and calls the service to check whether the customer is
	 * blacklisted or not.
	 *
	 * @param mplCustMobileNumber
	 * @return boolean
	 */
	boolean isMobileBlackListed(String mplCustMobileNumber);


	/**
	 * @param juspayOrderId
	 * @param paymentMethodType
	 * @param paymentMethod
	 * @param redirectAfterPayment
	 * @param format
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	String getNetbankingOrderStatus(String juspayOrderId, String paymentMethodType, String paymentMethod,
			String redirectAfterPayment, String format) throws EtailNonBusinessExceptions;


	/**
	 * This method creates an order in Juspay against which Payment will be processed
	 *
	 * @param cart
	 * @param firstName
	 * @param lastName
	 * @param addressLine1
	 * @param addressLine2
	 * @param addressLine3
	 * @param country
	 * @param state
	 * @param city
	 * @param pincode
	 * @param returnUrl
	 * @param uid
	 * @param channel
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	String createJuspayOrder(CartModel cart, String firstName, String lastName, String addressLine1, String addressLine2,
			String addressLine3, String country, String state, String city, String pincode, String cardSaved, String returnUrl,
			String uid, String channel) throws EtailNonBusinessExceptions;


	/**
	 * @param customer
	 * @return Map<Date, SavedCardData>
	 */
	Map<Date, SavedCardData> listStoredDebitCards(final CustomerModel customer);


	/**
	 * This method is used to get the List of Countries
	 *
	 * @return List<String>
	 */
	List<String> getCountries();

	/**
	 * @return MplPromoPriceData
	 * @throws CalculationException
	 * @throws JaloPriceFactoryException
	 * @throws JaloSecurityException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 */
	MplPromoPriceData applyPromotions(final CartData cartData, final CartModel cart) throws ModelSavingException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException, EtailNonBusinessExceptions;


	/**
	 * @param customer
	 * @param bankName
	 * @return Map<Date, SavedCardData>
	 */
	Map<Date, SavedCardData> listStoredEMICards(CustomerModel customer, String bankName);

	/*
	 * @Description : saving bank name in session -- TISPRO-179
	 *
	 * @param bankName
	 *
	 * @return Boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	Boolean setBankForSavedCard(final String bankName) throws EtailNonBusinessExceptions;

}
