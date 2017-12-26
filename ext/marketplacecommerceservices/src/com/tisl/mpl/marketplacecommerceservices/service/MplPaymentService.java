package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.JuspayCardStatusModel;
import com.tisl.mpl.core.model.JuspayEBSResponseDataModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.request.AddCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.AddCardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.model.BankModel;
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
	void saveCardDetailsFromJuspay(GetOrderStatusResponse orderStatusResponse, Map<String, Double> paymentMode,
			AbstractOrderModel cart);


	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param entries
	 * @throws EtailNonBusinessExceptions
	 *            ,Exception
	 */
	void saveCODPaymentInfo(String custName, Double cartValue, Double totalCODCharge, List<AbstractOrderEntryModel> entries,
			AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;

	/**
	 * juspay payment specific changes from cscockpit
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCharge
	 * @param entries
	 * @param abstractOrderModel
	 * @throws EtailNonBusinessExceptions
	 */
	void saveJusPayPaymentInfo(String custName, Double cartValue, Double totalCharge, List<AbstractOrderEntryModel> entries,
			AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;

	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for non-COD
	 * payment modes including wallet
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 *
	 */
	void setPaymentTransaction(GetOrderStatusResponse orderStatusResponse, Map<String, Double> paymentMode, AbstractOrderModel cart);


	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for COD and
	 * wallet
	 *
	 * @param paymentMode
	 * @param abstractOrderModel
	 * @throws EtailNonBusinessExceptions
	 *            ,Exception
	 *
	 */
	//TISPRD-361 method signature changes
	void setPaymentTransactionForCOD(AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;


	/**
	 * This method helps to save apportion for the PaymentModes which were successful
	 *
	 */
	void paymentModeApportion(final AbstractOrderModel cart);


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
	 * This method returns the list of Countries
	 *
	 * @return List<String>
	 */
	List<String> getCountries();


	/**
	 *
	 * @param cartData
	 * @param orderData
	 * @param cartModel
	 * @param orderModel
	 * @return MplPromoPriceData
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 * @throws EtailNonBusinessExceptions
	 */
	MplPromoPriceData applyPromotions(final CartData cartData, final OrderData orderData, final CartModel cartModel,
			final OrderModel orderModel, final MplPromoPriceData promoPriceData) throws ModelSavingException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException,
			JaloPriceFactoryException, EtailNonBusinessExceptions;


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
	void setEBSRiskStatus(String riskStatus, JuspayEBSResponseDataModel juspayEBSResponseModel);

	/**
	 * This method sets the EbsRiskLevel in JuspayEBSResponseModel
	 *
	 * @param riskLevel
	 * @param juspayEBSResponseModel
	 *
	 */
	void setEBSRiskLevel(String riskLevel, JuspayEBSResponseDataModel juspayEBSResponseModel);

	/**
	 * @param orderStatusResponse
	 * @param cart
	 * @param sameAsShipping
	 */
	void saveCreditCard(GetOrderStatusResponse orderStatusResponse, AbstractOrderModel cart, String sameAsShipping);

	/**
	 * @param orderStatusResponse
	 * @param cart
	 */
	void saveDebitCard(GetOrderStatusResponse orderStatusResponse, AbstractOrderModel cart);

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
	JuspayEBSResponseDataModel getEntryInAuditByOrder(final String orderId);

	/*
	 * @description : fetching bank model for a bank name TISPRO-179\
	 *
	 * @param : bankName
	 *
	 * @return : BankModel
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	BankModel getBankDetailsForBank(final String bankName) throws EtailNonBusinessExceptions;

	/*
	 * @Description : Fetching bank name for net banking-- TISPT-169
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 * @throws Exception
	 */
	List<BankforNetbankingModel> getNetBankingBanks() throws EtailNonBusinessExceptions;

	/**
	 * TISPT-200
	 *
	 * @param cartGuid
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	String getAuditId(String cartGuid) throws EtailNonBusinessExceptions;

	/**
	 * TIS-3168
	 *
	 * @param orderStatusResponse
	 * @param orderStatusRequest
	 * @param orderModel
	 * @return boolean
	 */
	boolean updateAuditEntry(GetOrderStatusResponse orderStatusResponse, GetOrderStatusRequest orderStatusRequest,
			final OrderModel orderModel, Map<String, Double> paymentMode);

	/**
	 * @param guid
	 * @return OrderModel
	 */
	OrderModel fetchOrderOnGUID(String guid);

	/**
	 * @return
	 */
	String createWalletPaymentId();

	/**
	 * @param request
	 * @param channelWeb
	 * @param walletOrderId
	 * @param order
	 */
	//Commented for Mobile use

	//	void entryInTPWaltAudit(HttpServletRequest request, String channelWeb, String guid, String walletOrderId);

	void entryInTPWaltAudit(String status, String channelWeb, String guid, String walletOrderId);

	/**
	 * @param custName
	 * @param entries
	 * @param cart
	 * @param request
	 */

	//Commented for Mobile use
	//	void saveTPWalletPaymentInfo(String custName, List<AbstractOrderEntryModel> entries, AbstractOrderModel cart,
	//			HttpServletRequest request);
	AbstractOrderModel saveTPWalletPaymentInfo(String custName, List<AbstractOrderEntryModel> entries, AbstractOrderModel cart,
			String refernceCode);

	/**
	 * @param paymentMode
	 * @param cart
	 * @param transactionAmount
	 * @param request
	 */
	//Commented for Mobile use
	//	void setTPWalletPaymentTransaction(Map<String, Double> paymentMode, AbstractOrderModel cart, HttpServletRequest request);
	void setTPWalletPaymentTransaction(Map<String, Double> paymentMode, AbstractOrderModel cart, final String refernceCode,
			Double transactionAmount);

	/**
	 * @param refNo
	 * @return
	 */
	MplPaymentAuditModel getWalletAuditEntries(String refNo);

	/**
	 * SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel against
	 * the cart for non-COD from OMS Submit Order Job
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 *
	 */
	boolean createPaymentTransactionFromSubmitOrderJob(OrderModel order);


	/**
	 * SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel against
	 * the cart for non -COD from OMS Submit Order Job
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param order
	 *
	 */
	void setPaymentTransactionFromJob(GetOrderStatusResponse orderStatusResponse, Map<String, Double> paymentMode, OrderModel order);

	/**
	 * SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
	 *
	 * @param payInfo
	 * @return
	 */
	public String getPaymentModeFrompayInfo(final PaymentInfoModel payInfo);


	/**
	 * SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel against
	 * the cart for COD from OMS Submit Order Job
	 *
	 * @param paymentMode
	 * @param abstractOrderModel
	 * @throws EtailNonBusinessExceptions
	 *            ,Exception
	 *
	 */
	void setPaymentTransactionForCODFromSubmitProcess(Map<String, Double> paymentMode, OrderModel orderModel)
			throws EtailNonBusinessExceptions;

	/**
	 * added for TPR-1348 for AutofundInitiation SprintPaymentFixes:-
	 *
	 * @param orderEntryModel
	 * @param bigDecimal
	 *
	 */
	public String doRefundPayment(List<OrderEntryModel> orderEntryModel, BigDecimal amountToRefund);

	/**
	 * @param rs
	 * @param paymentMode
	 * @param order
	 * @throws EtailNonBusinessExceptions
	 */
	void setQCPaymentTransaction(ArrayList<String> rs, Map<String, Double> paymentMode, AbstractOrderModel order,
			String cliqCashPaymentMode, final String WalletTotal) throws EtailNonBusinessExceptions;

	/**
	 * @param qcOrderID
	 * @param channel
	 * @param cartGuId
	 * @param qcAmount
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	boolean createQCEntryInAudit(String qcOrderID, String channel, String cartGuId, String qcAmount, String qcResponseCode,
			String transactionId) throws EtailNonBusinessExceptions;

	/**
	 * @return
	 */
	public String createQCPaymentId();


	//CheckedInvalid PaymentInfo missing handled call
	boolean createPaymentInfo(OrderModel order);

	/**
	 * @param addCardRequest
	 * @return AddCardResponse
	 * @throws Exception
	 */
	public AddCardResponse saveAndGetCardReferenceNo(AddCardRequest addCardRequest) throws Exception;

	/**
	 * @param cardToken
	 * @param email
	 * @param customerId
	 * @return AddCardResponse
	 * @throws Exception
	 */
	public AddCardResponse getCurrentCardReferenceNo(String cardToken, String email, String customerId) throws Exception;

	/**
	 * @param customerId
	 */
	public void rmvJuspayCardStatusForCustomer(String customerId);



	/**
	 * @param customerId
	 * @param guid
	 * @return JuspayCardStatusModel
	 */
	public JuspayCardStatusModel getJuspayCardStatusForCustomer(String customerId, String guid);



}
