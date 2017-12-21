package com.tisl.mpl.facades.payment;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplNetbankingData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;


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
	Map<String, Boolean> getPaymentModes(String store, final boolean isMobile, final CartData cartDataMobile)
			throws EtailNonBusinessExceptions;

	//CAR-111: Added new
	/**
	 * This method returns the map of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) and their
	 * availability for the specific store and displays them on the payment page of the store
	 *
	 * @param store
	 * @param abstractOrderModel
	 * @return Map<String, Boolean>
	 * @throws EtailNonBusinessExceptions
	 */
	Map<String, Boolean> getPaymentModes(String store, final AbstractOrderModel abstractOrderModel)
			throws EtailNonBusinessExceptions;


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @param netBankingList
	 *           // TISPT-169
	 * @return Map<String, String>
	 *
	 */
	List<MplNetbankingData> getBanksByPriority(List<BankforNetbankingModel> netBankingList) throws EtailNonBusinessExceptions;


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @param netBankingList
	 *           // TISPT-169
	 * @return Map<String, String>
	 *
	 */
	Map<String, String> getOtherBanks(List<BankforNetbankingModel> netBankingList) throws EtailNonBusinessExceptions;


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
	 * @param mobileNumber
	 * @param mplCustomerName
	 * @param orderModel
	 * @return otp
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTPforCOD(final String customerID, final String mobileNumber, final String mplCustomerName,
			final AbstractOrderModel orderModel) throws InvalidKeyException, NoSuchAlgorithmException;


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
	 * This method takes the customer ID and enter OTP as input parameters and calls the service to validate the entered
	 * OTP
	 *
	 * @param customerID
	 * @param enteredOTPNumber
	 * @return OTPResponseData
	 */
	String validateOTPforCODWV(String customerID, String enteredOTPNumber);


	/**
	 * This method takes the customer ID as input parameters and calls the service to check whether the customer is
	 * blacklisted or not.
	 *
	 * @param mplCustomerID
	 * @return boolean
	 */
	boolean isBlackListed(String mplCustomerID, final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;


	/**
	 * This method is used to save the cart details
	 *
	 * @param cart
	 */
	void saveCart(CartModel cart);


	//	/**
	//	 * This method sends request to JusPay to get the Order Status
	//	 *
	//	 * @return String
	//	 *
	//	 */
	//String getOrderStatusFromJuspay();


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
	 * @param abstractOrderModel
	 */
	//TISPRD-361 TPR-629 Refactor
	void saveCODPaymentInfo(final Double cartValue, final Double totalCODCharge, final AbstractOrderModel abstractOrderModel) //Parameter OrderModel added extra for TPR-629
			throws EtailNonBusinessExceptions, Exception;


	/**
	 * This method fetches the customer's phone number from Delivery address
	 *
	 * @param abstractOrderModel
	 * @return String
	 */
	String fetchPhoneNumber(final AbstractOrderModel abstractOrderModel);


	//	/**
	//	 * This method sends request to JusPay to get the Order Status everytime the user enters the payment screen
	//	 *
	//	 * @param orderId
	//	 * @return String
	//	 *
	//	 */
	//	String getJuspayOrderStatus(String orderId);


	//	/**
	//	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	//	 * success
	//	 *
	//	 * @param juspayOrderId
	//	 * @param mplCustomerID
	//	 * @return PaymentTransactionModel
	//	 */
	//	PaymentTransactionModel getOrderStatusFromCart(String juspayOrderId, String mplCustomerID);

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


	//	/**
	//	 * This method creates an order in Juspay against which Payment will be processed
	//	 *
	//	 * @param cart
	//	 * @param firstName
	//	 * @param lastName
	//	 * @param addressLine1
	//	 * @param addressLine2
	//	 * @param addressLine3
	//	 * @param country
	//	 * @param state
	//	 * @param city
	//	 * @param pincode
	//	 * @param returnUrl
	//	 * @param uid
	//	 * @param channel
	//	 * @return String
	//	 * @throws EtailNonBusinessExceptions
	//	 */
	//	String createJuspayOrder(CartModel cart, String firstName, String lastName, String addressLine1, String addressLine2,
	//			String addressLine3, String country, String state, String city, String pincode, String cardSaved, String returnUrl,
	//			String uid, String channel) throws EtailNonBusinessExceptions;


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
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 */

	MplPromoPriceData applyPromotions(final CartData cartData, final OrderData orderData, final CartModel cartModel,
			final OrderModel orderModel, final MplPromoPriceData mplPromoPriceData) throws ModelSavingException,
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

	/*
	 * @Description : Fetching bank name for net banking-- TISPT-169
	 *
	 * @return Map<String, List<MplNetbankingData>>
	 *
	 * @throws Exception
	 */
	List<BankforNetbankingModel> getNetBankingBanks() throws EtailNonBusinessExceptions, Exception;

	/**
	 * This method is used to get the IP address of the client and check whether it is blacklisted TISPT-204 Point 2
	 *
	 * @param httpServletRequest
	 * @return String
	 *
	 */
	String getBlacklistByIPStatus(HttpServletRequest httpServletRequest);

	/**
	 * This method is used to get saved card details from juspay TISPT-204 Point no 4
	 *
	 * @param customer
	 * @return ListCardsResponse
	 *
	 */
	ListCardsResponse getJuspayCardResponse(CustomerModel customer);

	/**
	 * This method fetches the stored credit card from juspay response and returns them back to the Controller TISPT-204
	 * Point no 4
	 *
	 * @param customer
	 * @param listCardsResponse
	 * @return Tuple2<?, ?>
	 *
	 */
	Tuple2<?, ?> listStoredCards(final CustomerModel customer, ListCardsResponse listCardsResponse);

	//TISPRO-578
	boolean isValidCart(CartModel cartModel);

	//TISPRO-540
	/**
	 * This method is used to check whether payment info, delivery mode and address are present against cart or not
	 *
	 * @param cart
	 * @return boolean
	 */
	boolean checkCart(final CartModel cart);


	/**
	 * @param guid
	 * @return OrderModel
	 */
	OrderModel getOrderByGuid(String guid);


	/**
	 * @param store
	 * @param orderData
	 * @return Map<String, Boolean>
	 * @throws EtailNonBusinessExceptions
	 */
	Map<String, Boolean> getPaymentModes(String store, OrderData orderData) throws EtailNonBusinessExceptions;


	/**
	 * @param orderGuid
	 * @param orderModel
	 * @return String
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	public String getOrderStatusFromJuspay(final String orderGuid, Map<String, Double> paymentMode, final OrderModel orderModel,
			String juspayOrderId) throws EtailBusinessExceptions, EtailNonBusinessExceptions;


	/**
	 * @param cart
	 * @param order
	 * @param firstName
	 * @param lastName
	 * @param addressLine1
	 * @param addressLine2
	 * @param addressLine3
	 * @param country
	 * @param state
	 * @param city
	 * @param pincode
	 * @param checkValues
	 * @param returnUrl
	 * @param uid
	 * @param channel
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 * @throws AdapterException
	 *
	 */
	String createJuspayOrder(CartModel cart, OrderModel order, String firstName, String lastName, String addressLine1,
			String addressLine2, String addressLine3, String country, String state, String city, String pincode, String checkValues,
			String returnUrl, String uid, String channel, double amount) throws EtailNonBusinessExceptions, AdapterException;


	/**
	 * @param abstractOrderModel
	 */
	void populateDeliveryPointOfServ(AbstractOrderModel abstractOrderModel);


	/**
	 * @param promoResultList
	 * @return double
	 * @throws Exception
	 */
	double calculateTotalDiscount(List<PromotionResultData> promoResultList) throws Exception;


	/**
	 * @param abstractOrderModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 */
	void populateDelvPOSForFreebie(AbstractOrderModel abstractOrderModel,
			Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, Map<String, Long> freebieParentQtyMap);


	/**
	 * @param cart
	 * @param walletName
	 * @param channelWeb
	 * @return
	 */
	List<String> createWalletorder(AbstractOrderModel cart, String walletName, String channelWeb);


	/**
	 * @param request
	 * @param channelWeb
	 * @param walletOrderId
	 * @param orderModel
	 */
	void entryInTPWaltAudit(String status, String channelWeb, String guid, String walletOrderId);


	/**
	 * @param cart
	 * @param request
	 */
	void saveTPWalletPaymentInfo(AbstractOrderModel order, HttpServletRequest request);


	/**
	 * @param refNo
	 */
	String getWalletAuditEntries(String refNo);

	/***
	 * cscockpit specific order payment status call
	 *
	 * @param url
	 * @return
	 */
	public String makeGetPaymentStatusCall(final String url);



	/**
	 * Added for TPR-4461
	 *
	 * @param banklist
	 * @param bank
	 * @param boolean
	 */
	public boolean validateBank(final List<BankModel> bankList, final String bank);

	//TPR-7486
	public String fetchBankFromCustomerSavedCard(final String cardRefNum, final CustomerModel Customer);

	public String fetchBanknameFromBin(final String cardBinNo);//TPR-7486
	/**
	 * Added for paytm integration
	 * 
	 * @param juspayOrderId
	 * @param paymentMethodType
	 * @param paymentMethod
	 * @param redirectAfterPayment
	 * @param format
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	public String getPaytmOrderStatus(String juspayOrderId, String paymentMethodType, String paymentMethod,
			String redirectAfterPayment, String format) throws EtailNonBusinessExceptions;
	
	
	/**
	 * @param guid
	 * @param orderToBeUpdated
	 * @return
	 */
	public QCRedeeptionResponse createQCOrderRequest(String guid, AbstractOrderModel orderToBeUpdated, final String WalletId,
			String cliqCashPaymentMode, final String qcTransactionId, String channel, double walletTotal, double juspayAmount);

	/**
	 * @return
	 */
	public String generateQCCode();

}
