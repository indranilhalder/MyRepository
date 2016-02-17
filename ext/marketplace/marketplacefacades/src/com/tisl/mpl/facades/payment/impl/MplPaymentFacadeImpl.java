/**
 *
 */
package com.tisl.mpl.facades.payment.impl;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplNetbankingData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.request.InitOrderRequest;
import com.tisl.mpl.juspay.request.ListCardsRequest;
import com.tisl.mpl.juspay.request.NetbankingRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.InitOrderResponse;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.juspay.response.StoredCard;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

@Component
@Qualifier(MarketplacecommerceservicesConstants.MPLPAYMENTFACADE)
public class MplPaymentFacadeImpl implements MplPaymentFacade
{
	private MplPaymentService mplPaymentService;
	@Resource
	private OTPGenericService otpGenericService;
	@Resource
	private BlacklistService blacklistService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CartService cartService;
	@Resource
	private SessionService sessionService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private BusinessProcessService businessProcessService;
	@Autowired
	private BinService binService;
	@Autowired
	private SendSMSFacade sendSMSFacade;

	private static final Logger LOG = Logger.getLogger(MplPaymentFacadeImpl.class);

	/**
	 * This method returns the map of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) and their
	 * availability for the specific store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return Map<String, Boolean>
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public Map<String, Boolean> getPaymentModes(final String store) throws EtailNonBusinessExceptions
	{

		//Declare variable
		final Map<String, Boolean> data = new HashMap<String, Boolean>();

		try
		{
			//Get payment modes
			final List<PaymentTypeModel> paymentTypes = getMplPaymentService().getPaymentModes(store);

			if (CollectionUtils.isNotEmpty(paymentTypes))
			{
				//looping through the mode to get payment Types
				for (final PaymentTypeModel mode : paymentTypes)
				{
					//retrieving the data
					data.put(mode.getMode(), mode.getIsAvailable());
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B6001);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.PAYMENTTYPEERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.PAYMENTTYPEERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplaceFacadesConstants.PAYMENTTYPEERROR, e);
		}

		//returning data
		return data;
	}



	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @return Map<String, String>
	 *
	 */
	@Override
	public List<MplNetbankingData> getBanksByPriority() throws EtailNonBusinessExceptions
	{
		List<BankforNetbankingModel> bankList = new ArrayList<BankforNetbankingModel>();
		final List<MplNetbankingData> nbBankDataList = new ArrayList<MplNetbankingData>();

		try
		{
			//getting the priority banks
			bankList = getMplPaymentService().getBanksByPriority();

			if (CollectionUtils.isNotEmpty(bankList))
			{
				//looping through the bank to get banks
				for (final BankforNetbankingModel bank : bankList)
				{
					final MplNetbankingData netbankingData = new MplNetbankingData();

					//putting the bank data
					if (null != bank.getNbCode())
					{
						netbankingData.setBankCode(bank.getNbCode());
					}
					if (null != bank.getName().getBankLogo())
					{
						netbankingData.setBankLogoUrl(bank.getName().getBankLogo().getURL());
					}
					if (null != bank.getName().getBankName())
					{
						netbankingData.setBankName(bank.getName().getBankName());
					}
					nbBankDataList.add(netbankingData);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B6002);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.PRIORITYBANKSERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.PRIORITYBANKSERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplaceFacadesConstants.PRIORITYBANKSERROR, e);
		}

		//returning data
		return nbBankDataList;
	}

	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * map with key-value pair as bankName-bankCode
	 *
	 * @return Map<String, String>
	 *
	 */
	@Override
	public Map<String, String> getOtherBanks() throws EtailNonBusinessExceptions
	{
		List<BankforNetbankingModel> bankList = new ArrayList<BankforNetbankingModel>();
		final Map<String, String> data = new TreeMap<String, String>();

		try
		{
			//getting the non prioritized banks
			bankList = getMplPaymentService().getOtherBanks();

			if (CollectionUtils.isNotEmpty(bankList))
			{
				//looping through the bank to get banks
				for (final BankforNetbankingModel bank : bankList)
				{
					//putting the bank data
					data.put(bank.getName().getBankName(), bank.getNbCode());
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B6003);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.NONPRIORITYBANKSERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.NONPRIORITYBANKSERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplaceFacadesConstants.NONPRIORITYBANKSERROR, e);
		}

		//returning data
		return data;
	}


	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @return Map<String, String>
	 */
	@Override
	public Map<String, String> getEMIBankNames(final Double cartValue)
	{
		//getting the EMI related banks
		final List<EMIBankModel> emiBankList = getMplPaymentService().getEMIBanks(cartValue);

		final Map<String, String> emiBanks = new TreeMap<String, String>();

		//looping through the bank to get banks
		for (final EMIBankModel bank : emiBankList)
		{
			//putting the bank data
			emiBanks.put(bank.getName().getBankName(), bank.getCode());
		}

		//returning the bank map
		return emiBanks;
	}


	/**
	 * This method takes the totalAmount as parameter and then calculates the emi using the emi calculation logic. It the
	 * returns an ArrayList of EMITermrateData for the selected bank to be displayed on the payment page.
	 *
	 * @param bank
	 * @param totalAmount
	 * @return ArrayList<EMITermRateData>
	 */
	@Override
	public List<EMITermRateData> getBankTerms(final String bank, final Double totalAmount) throws EtailNonBusinessExceptions
	{
		List<EMITermRateData> bankTerms = new ArrayList<EMITermRateData>();
		try
		{
			//getting the bank terms
			bankTerms = getMplPaymentService().getBankTerms(bank, totalAmount);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.EMITERMSERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.EMITERMSERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplaceFacadesConstants.EMITERMSERROR, e);
		}

		return bankTerms;
	}


	/**
	 * This method takes the customerID as parameter and calls the service to generate OTP
	 *
	 * @param customerID
	 * @param mobileNumber
	 * @param mplCustomerName
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public String generateOTPforCODWeb(final String customerID, final String mobileNumber, final String mplCustomerName,
			final String cartId) throws InvalidKeyException, NoSuchAlgorithmException
	{
		//calling service to generate OTP
		boolean isMobile = false;
		if (null != cartId && !cartId.isEmpty())
		{
			isMobile = true;
		}
		String otp = getOtpGenericService().generateOTP(customerID, OTPTypeEnum.COD.getCode(), mobileNumber);
		if (null == otp)
		{
			otp = "".intern();
		}
		CartModel cart = new CartModel();
		if (isMobile)
		{
			final FlexibleSearchService flexibleSearchService = Registry.getApplicationContext()
					.getBean(FlexibleSearchService.class);

			cart.setCode(cartId);
			cart = flexibleSearchService.getModelByExample(cart);
		}
		else
		{
			cart = getCartService().getSessionCart();
		}
		if (null != cart.getDeliveryAddress() && null != cart.getDeliveryAddress().getPhone1())
		{
			cart.getDeliveryAddress().setPhone1(mobileNumber);
		}
		if (null != cart.getDeliveryAddress() && null != cart.getDeliveryAddress().getCellphone())
		{
			cart.getDeliveryAddress().setCellphone(mobileNumber);
		}
		getModelService().save(cart.getDeliveryAddress());
		saveCart(cart);

		//sending sms to verify COD Payment before order confirmation
		final String contactNumber = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		try
		{
			//TODO mplCustomerName null pointer code fix 16-AUG-15

			getSendSMSFacade().sendSms(
					MarketplacecommerceservicesConstants.SMS_SENDER_ID,
					MarketplacecommerceservicesConstants.SMS_MESSAGE_COD_OTP
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
									mplCustomerName != null ? mplCustomerName : "There")
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, otp)
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, contactNumber), mobileNumber);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.LOGERROR, e);
		}

		return otp;
	}


	/**
	 * This method takes the customer ID and enter OTP as input parameters and calls the service to validate the entered
	 * OTP
	 *
	 * @param customerID
	 * @param enteredOTPNumber
	 * @return String
	 */
	@Override
	public String validateOTPforCODWeb(final String customerID, final String enteredOTPNumber)
	{
		try
		{
			//OTP validation
			final OTPResponseData otpResponse = getOtpGenericService().validateOTP(
					customerID,
					null,
					enteredOTPNumber,
					OTPTypeEnum.COD,
					Long.parseLong(getConfigurationService().getConfiguration().getString(
							MarketplacecommerceservicesConstants.TIMEFOROTP)));

			if (null != otpResponse && null != otpResponse.getInvalidErrorMessage())
			{
				//returning true or false based on whether OTP is valid or not
				return otpResponse.getInvalidErrorMessage();
			}
			else
			{
				return null;
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
			return null;
		}
	}


	/**
	 * This method takes the customer ID as input parameters and calls the service to check whether the customer is
	 * blacklisted or not.
	 *
	 * @param ipAddress
	 * @param cart
	 * @return boolean
	 */
	@Override
	public boolean isBlackListed(final String ipAddress, final CartModel cart) throws EtailNonBusinessExceptions
	{
		//getting current customer details
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final String customerPk = customer.getPk().toString();
		final String customerEmail = customer.getOriginalUid();
		final String customerPhoneNo = fetchPhoneNumber(cart);

		boolean mplCustomerIsBlackListed = false;
		try
		{
			//checking user blacklisted or not
			mplCustomerIsBlackListed = getBlacklistService().getBlacklistedCustomerforCOD(customerPk, customerEmail,
					customerPhoneNo, ipAddress).booleanValue();

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.BLACKLISTINGERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplaceFacadesConstants.BLACKLISTINGERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplaceFacadesConstants.BLACKLISTINGERROR, e);
		}

		//returning true or false based on whether user is blacklisted or not
		return mplCustomerIsBlackListed;
	}

	/**
	 * This method takes the mobile number as input parameters and calls the service to check whether the customer is
	 * blacklisted or not.
	 *
	 * @param mobileNumber
	 * @return boolean
	 */
	@Override
	public boolean isMobileBlackListed(final String mobileNumber)
	{
		//checking user blacklisted or not
		final boolean mplMobileIsBlackListed = getBlacklistService().mobileBlacklistedOrNot(mobileNumber);

		//returning true or false based on whether user is blacklisted or not
		return mplMobileIsBlackListed;
	}

	/**
	 * This method is used to save the cart details
	 *
	 * @param cart
	 */
	@Override
	public void saveCart(final CartModel cart)
	{
		//saving cartmodel
		final Double deliveryCost = cart.getDeliveryCost();
		getModelService().save(cart);
		cart.setDeliveryCost(deliveryCost);
		getModelService().save(cart);
	}


	/**
	 * This method creates an order in Juspay against which Payment will be processed
	 *
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public String createJuspayOrder(final CartModel cart, final String firstName, final String lastName,
			final String addressLine1, final String addressLine2, final String addressLine3, final String country,
			final String state, final String city, final String pincode, final String checkValues, final String returnUrl,
			final String uid, final String channel) throws EtailNonBusinessExceptions
	{
		try
		{
			//creating PaymentService of Juspay
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService
					.withKey(
							getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).withMerchantId(
							getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			//getting the current customer to fetch customer Id and customer email
			CustomerModel customer = getModelService().create(CustomerModel.class);
			if (null != uid)
			{
				customer = getMplPaymentService().getCustomer(uid);
			}

			final String customerEmail = customer.getOriginalUid();
			//			String juspayOrderStatus = "";
			String juspayOrderId = "";
			boolean flag = false;
			String resJusOrderId = null;

			juspayOrderId = getMplPaymentService().createPaymentId();
			LOG.debug("Order Id created by key generator is " + juspayOrderId);

			//Create entry in Audit table
			flag = getMplPaymentService().createEntryInAudit(juspayOrderId, channel, cart.getGuid());

			if (flag)
			{
				//creating InitOrderRequest of Juspay
				// For netbanking firstname will be set as Bank Code
				final InitOrderRequest request = new InitOrderRequest().withOrderId(juspayOrderId).withAmount(cart.getTotalPrice())
						.withCustomerId(uid).withEmail(customerEmail).withUdf1(firstName).withUdf2(lastName).withUdf3(addressLine1)
						.withUdf4(addressLine2).withUdf5(addressLine3).withUdf6(country).withUdf7(state).withUdf8(city)
						.withUdf9(pincode).withUdf10(checkValues).withReturnUrl(returnUrl);

				LOG.info("Juspay Request Structure " + request);

				//creating InitOrderResponse
				final InitOrderResponse initOrderResponse = juspayService.initOrder(request);
				if (null != initOrderResponse && StringUtils.isNotEmpty(initOrderResponse.getOrderId()))
				{
					resJusOrderId = initOrderResponse.getOrderId();
					final String orderStatus = initOrderResponse.getStatus();
					if (StringUtils.isNotEmpty(orderStatus))
					{
						LOG.info(MarketplacecommerceservicesConstants.JUSPAY_ORDER_RESP + resJusOrderId + " " + orderStatus);
					}
				}

				//Adding orderID to session for later use to get Order status
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID, resJusOrderId);

				//Update the Audit table before proceeding with payment
				getMplPaymentService().createEntryInAudit(resJusOrderId, channel, cart.getGuid());
			}

			//returning order ID from initOrderResponse
			return resJusOrderId;
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, "E0001");
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, "E0007");
		}
		catch (final AdapterException e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Something went wrong ", e);
			throw new EtailNonBusinessExceptions(e, "E0007");
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, "E0007");
		}
	}



	/**
	 * This method sends request to JusPay to get the Order Status everytime the user enters the payment screen
	 *
	 * @param orderId
	 * @return String
	 *
	 */
	@Override
	public String getJuspayOrderStatus(final String orderId)
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		//creating OrderStatusRequest
		final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
		orderStatusRequest.withOrderId(orderId);
		//creating OrderStatusResponse
		GetOrderStatusResponse orderStatusResponse = new GetOrderStatusResponse();
		try
		{
			//getting the response by calling get Order Status service
			orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

		//returning the statues of the order
		return orderStatusResponse.getStatus();
	}


	/**
	 * This method sends request to JusPay to get the Order Status on completion of Payment
	 *
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public String getOrderStatusFromJuspay() throws EtailNonBusinessExceptions
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		final Map<String, Double> paymentMode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE);
		final CartModel cart = getCartService().getSessionCart();
		String orderStatus = null;
		boolean updAuditErrStatus = false;

		//creating OrderStatusRequest
		final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();

		LOG.info(getSessionService().getAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID).toString());
		orderStatusRequest.withOrderId(getSessionService().getAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID)
				.toString());

		//creating OrderStatusResponse
		GetOrderStatusResponse orderStatusResponse = new GetOrderStatusResponse();
		try
		{
			//getting the response by calling get Order Status service
			orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
			if (null != orderStatusResponse)
			{
				//Update Audit Table after getting payment response
				updAuditErrStatus = getMplPaymentService().updateAuditEntry(orderStatusResponse);
				//Update PaymentTransaction and PaymentTransactionEntry Models
				getMplPaymentService().setPaymentTransaction(orderStatusResponse, paymentMode, cart);
			}

			//Logic when transaction is successful i.e. CHARGED
			if (MarketplacecommerceservicesConstants.CHARGED.equalsIgnoreCase(orderStatusResponse.getStatus()))
			{
				//saving card details
				getMplPaymentService().saveCardDetailsFromJuspay(orderStatusResponse, paymentMode, cart);
			}
			getMplPaymentService().paymentModeApportion(cart);

			if (updAuditErrStatus)
			{
				orderStatus = orderStatusResponse.getStatus();
			}

			//returning the statues of the order
			getSessionService().removeAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID);
			return orderStatus;
		}
		catch (final Exception e)
		{
			LOG.error("Failed to save order status in payment transaction with error: " + e);
			throw new EtailNonBusinessExceptions(e);
		}

	}


	/**
	 * This method fetches the stored cards from Juspay and returns them back to the Controller
	 *
	 * @return List<StoredCard>
	 *
	 */
	@Override
	public Map<Date, SavedCardData> listStoredCreditCards(final CustomerModel customer)
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		//creating ListCardsRequest of Juspay
		final ListCardsRequest listCardsRequest = new ListCardsRequest().withCustomerId(customer.getUid());
		ListCardsResponse listCardsResponse = new ListCardsResponse();
		final Map<Date, SavedCardData> savedCardDataMap = new TreeMap<Date, SavedCardData>(Collections.reverseOrder());
		try
		{
			//getting ListCardsResponse by calling List Cards service of Juspay
			listCardsResponse = juspayService.listCards(listCardsRequest);
			final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard();
			final List<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();
			if (CollectionUtils.isNotEmpty(savedCardForCustomer))
			{
				savedCardList.addAll(savedCardForCustomer);
				if (null != listCardsResponse && null != listCardsResponse.getCards())
				{
					for (final StoredCard juspayCard : listCardsResponse.getCards())
					{
						final String bin = juspayCard.getCardIsin() != null ? juspayCard.getCardIsin()
								: MarketplacecommerceservicesConstants.EMPTY;
						BinModel binModel = getModelService().create(BinModel.class);
						try
						{
							binModel = getBinService().checkBin(bin);
						}
						catch (final NullPointerException e)
						{
							LOG.error(MarketplacecommerceservicesConstants.BINNOERROR, e);
							ExceptionUtil.getCustomizedExceptionTrace(e);
						}
						catch (final EtailBusinessExceptions e)
						{
							LOG.error(MarketplacecommerceservicesConstants.BINNOERROR, e);
							ExceptionUtil.getCustomizedExceptionTrace(e);
						}
						//iterating through the saved cards list
						for (final SavedCardModel savedCard : savedCardList)
						{
							//TISEE-396
							if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null != binModel
									&& (StringUtils.equalsIgnoreCase("CREDIT", binModel.getCardType()) || StringUtils.equalsIgnoreCase(
											"CC", binModel.getCardType())) && null != savedCard.getBillingAddress())
							{
								final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}

							else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null != binModel && StringUtils.isEmpty(binModel.getCardType())
									&& null != savedCard.getBillingAddress())
							{
								final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}

							else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null == binModel && null != savedCard.getBillingAddress())
							{
								final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}
						}
					}
				}

			}
			else
			{
				LOG.info("No Saved credit cards found !!");
			}
		}
		catch (final NullPointerException e)
		{
			//logging error message
			LOG.error(MarketplacecommerceservicesConstants.SAVEDCCERROR, e);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDCCERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDCCERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDCCERROR, e);
		}

		return savedCardDataMap;
	}


	/**
	 * This method sets the saved credit cards in a data
	 *
	 * @param juspayCard
	 * @param binModel
	 * @param savedCard
	 * @return SavedCardData
	 */
	private SavedCardData setSavedCreditCards(final StoredCard juspayCard, final BinModel binModel, final SavedCardModel savedCard)
	{
		final SavedCardData savedCardData = new SavedCardData();
		savedCardData.setCardToken(juspayCard.getCardToken());
		savedCardData.setCardReferenceNumber(juspayCard.getCardReference());
		savedCardData.setCardFingerprint(juspayCard.getCardFingerprint());
		savedCardData.setCardISIN(juspayCard.getCardIsin());
		final String cardNumberMasked = juspayCard.getCardNumber();
		final String lastFourDigits = cardNumberMasked.substring((cardNumberMasked.length() - 4), cardNumberMasked.length());
		savedCardData.setCardEndingDigits(lastFourDigits);
		savedCardData.setNameOnCard(juspayCard.getNameOnCard());
		savedCardData.setExpiryMonth(juspayCard.getCardExpMonth());
		savedCardData.setExpiryYear(juspayCard.getCardExpYear());
		savedCardData.setCardType("CREDIT");
		savedCardData.setJuspayCardType(juspayCard.getCardType());
		if (null != binModel)
		{
			if (null != binModel.getBank())
			{
				final String bankName = binModel.getBank().getBankName();
				if (StringUtils.isNotEmpty(bankName))
				{
					savedCardData.setCardIssuer(bankName);
				}
			}
			if (StringUtils.isNotEmpty(binModel.getIssuingCountry()) && binModel.getIssuingCountry().equalsIgnoreCase("India"))
			{
				savedCardData.setIsDomestic(Boolean.TRUE);
			}
			else
			{
				savedCardData.setIsDomestic(Boolean.FALSE);
			}
		}
		else
		{
			savedCardData.setIsDomestic(Boolean.FALSE);
		}
		savedCardData.setCardBrand(juspayCard.getCardBrand());
		savedCardData.setNickname(juspayCard.getNickname());

		if (null != juspayCard.getExpired())
		{
			savedCardData.setExpired(juspayCard.getExpired());
		}
		else
		{
			savedCardData.setExpired(" ");
		}
		savedCardData.setFirstName(savedCard.getBillingAddress().getFirstname());
		savedCardData.setLastName(savedCard.getBillingAddress().getLastname());
		savedCardData.setAddressLine1(savedCard.getBillingAddress().getLine1());
		savedCardData.setAddressLine2(savedCard.getBillingAddress().getLine2());
		savedCardData.setAddressLine3(savedCard.getBillingAddress().getAddressLine3());
		savedCardData.setCountry(savedCard.getBillingAddress().getCountry().getName());
		savedCardData.setState(savedCard.getBillingAddress().getDistrict());
		savedCardData.setCity(savedCard.getBillingAddress().getTown());
		savedCardData.setPincode(savedCard.getBillingAddress().getPostalcode());

		return savedCardData;
	}


	/**
	 * This method fetches the stored cards from Juspay and returns them back to the Controller
	 *
	 * @return List<StoredCard>
	 *
	 */
	@Override
	public Map<Date, SavedCardData> listStoredDebitCards(final CustomerModel customer)
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		//creating ListCardsRequest of Juspay
		final ListCardsRequest listCardsRequest = new ListCardsRequest().withCustomerId(customer.getUid());
		ListCardsResponse listCardsResponse = new ListCardsResponse();
		final Map<Date, SavedCardData> savedCardDataMap = new TreeMap<Date, SavedCardData>(Collections.reverseOrder());
		try
		{
			//getting ListCardsResponse by calling List Cards service of Juspay
			listCardsResponse = juspayService.listCards(listCardsRequest);
			final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard();
			final List<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();
			if (CollectionUtils.isNotEmpty(savedCardForCustomer))
			{
				savedCardList.addAll(savedCardForCustomer);
				if (null != listCardsResponse && null != listCardsResponse.getCards())
				{
					for (final StoredCard juspayCard : listCardsResponse.getCards())
					{
						final String bin = juspayCard.getCardIsin() != null ? juspayCard.getCardIsin()
								: MarketplacecommerceservicesConstants.EMPTY;
						BinModel binModel = getModelService().create(BinModel.class);
						try
						{
							binModel = getBinService().checkBin(bin);
						}
						catch (final NullPointerException e)
						{
							LOG.error(MarketplacecommerceservicesConstants.BINNOERROR, e);
							ExceptionUtil.getCustomizedExceptionTrace(e);
						}
						catch (final EtailBusinessExceptions e)
						{
							LOG.error(MarketplacecommerceservicesConstants.BINNOERROR, e);
							ExceptionUtil.getCustomizedExceptionTrace(e);
						}
						//iterating through the saved cards list
						for (final SavedCardModel savedCard : savedCardList)
						{
							//TISEE-396
							if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null != binModel
									&& (StringUtils.equalsIgnoreCase("DEBIT", binModel.getCardType()) || StringUtils.equalsIgnoreCase(
											"DC", binModel.getCardType())))
							{
								final SavedCardData savedCardData = setSavedDebitCards(juspayCard, binModel);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}

							else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null != binModel && StringUtils.isEmpty(binModel.getCardType())
									&& null == savedCard.getBillingAddress())
							{
								final SavedCardData savedCardData = setSavedDebitCards(juspayCard, binModel);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}

							else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
									&& null == binModel && null == savedCard.getBillingAddress())
							{
								final SavedCardData savedCardData = setSavedDebitCards(juspayCard, binModel);

								savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
							}
						}
					}
				}
			}
			else
			{
				LOG.info("No Saved debit cards found !!");
			}
		}
		catch (final NullPointerException e)
		{
			//logging error message
			LOG.error(MarketplacecommerceservicesConstants.SAVEDDCERROR, e);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDDCERROR, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDDCERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SAVEDDCERROR, e);
		}
		return savedCardDataMap;
	}



	/**
	 * This method sets the saved debit cards in a data
	 *
	 * @param juspayCard
	 * @param binModel
	 * @return SavedCardData
	 */
	private SavedCardData setSavedDebitCards(final StoredCard juspayCard, final BinModel binModel)
	{
		final SavedCardData savedCardData = new SavedCardData();
		savedCardData.setCardToken(juspayCard.getCardToken());
		savedCardData.setCardReferenceNumber(juspayCard.getCardReference());
		savedCardData.setCardFingerprint(juspayCard.getCardFingerprint());
		savedCardData.setCardISIN(juspayCard.getCardIsin());
		final String cardNumberMasked = juspayCard.getCardNumber();
		final String lastFourDigits = cardNumberMasked.substring((cardNumberMasked.length() - 4), cardNumberMasked.length());
		savedCardData.setCardEndingDigits(lastFourDigits);
		savedCardData.setNameOnCard(juspayCard.getNameOnCard());
		savedCardData.setExpiryMonth(juspayCard.getCardExpMonth());
		savedCardData.setExpiryYear(juspayCard.getCardExpYear());
		savedCardData.setCardType("DEBIT");
		savedCardData.setJuspayCardType(juspayCard.getCardType());
		if (null != binModel)
		{
			if (null != binModel.getBank())
			{
				final String bankName = binModel.getBank().getBankName();
				if (StringUtils.isNotEmpty(bankName))
				{
					savedCardData.setCardIssuer(bankName);
				}
			}
			if (StringUtils.isNotEmpty(binModel.getIssuingCountry()) && binModel.getIssuingCountry().equalsIgnoreCase("India"))
			{
				savedCardData.setIsDomestic(Boolean.TRUE);
			}
			else
			{
				savedCardData.setIsDomestic(Boolean.FALSE);
			}
		}
		else
		{
			savedCardData.setIsDomestic(Boolean.FALSE);
		}
		savedCardData.setCardBrand(juspayCard.getCardBrand());
		savedCardData.setNickname(juspayCard.getNickname());
		if (null != juspayCard.getExpired())
		{
			savedCardData.setExpired(juspayCard.getExpired());
		}
		else
		{
			savedCardData.setExpired(" ");
		}

		return savedCardData;

	}



	/**
	 * This method saves Payment info in case of COD mode of Payment
	 *
	 * @param cartValue
	 * @param totalCODCharge
	 *
	 */
	@Override
	public void saveCODPaymentInfo(final Double cartValue, final Double totalCODCharge)
	{
		//getting the current user
		final CustomerModel mplCustomer = (CustomerModel) getUserService().getCurrentUser();
		final Map<String, Double> paymentMode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE);
		final CartModel cart = getCartService().getSessionCart();
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		getMplPaymentService().setPaymentTransactionForCOD(paymentMode, cart);
		if (null != mplCustomer)
		{
			if (StringUtils.isNotEmpty(mplCustomer.getName()) && !mplCustomer.getName().equalsIgnoreCase(" "))
			{
				final String custName = mplCustomer.getName();

				//saving COD PaymentInfo
				getMplPaymentService().saveCODPaymentInfo(custName, cartValue, totalCODCharge, entries, cart);
			}
			else
			{
				final String custEmail = mplCustomer.getOriginalUid();

				//saving COD PaymentInfo
				getMplPaymentService().saveCODPaymentInfo(custEmail, cartValue, totalCODCharge, entries, cart);
			}
		}
		getMplPaymentService().paymentModeApportion(cart);

	}

	/**
	 * This method fetches the customer's phone number from Delivery address
	 *
	 * @return String
	 */
	@Override
	public String fetchPhoneNumber(final CartModel cart)
	{
		if (null != cart.getDeliveryAddress() && null != cart.getDeliveryAddress().getPhone1())
		{
			return cart.getDeliveryAddress().getPhone1();
		}
		else if (null != cart.getDeliveryAddress() && null != cart.getDeliveryAddress().getCellphone())
		{
			return cart.getDeliveryAddress().getCellphone();
		}
		else
		{
			return MarketplacecommerceservicesConstants.EMPTYSTRING;
		}

	}


	/**
	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	 * success
	 *
	 * @param juspayOrderId
	 * @param mplCustomerID
	 * @return PaymentTransactionModel
	 */
	@Override
	public PaymentTransactionModel getOrderStatusFromCart(final String juspayOrderId, final String mplCustomerID)
	{
		if (null != getMplPaymentService().getOrderStatusFromCart(juspayOrderId, mplCustomerID))
		{
			final PaymentTransactionModel paymentTransaction = getMplPaymentService().getOrderStatusFromCart(juspayOrderId,
					mplCustomerID);
			return paymentTransaction;
		}
		else
		{
			return null;
		}
	}



	/**
	 * This method handles netbanking request and response
	 *
	 * @param juspayOrderId
	 * @param paymentMethodType
	 * @param paymentMethod
	 * @param redirectAfterPayment
	 * @param format
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public String getNetbankingOrderStatus(final String juspayOrderId, final String paymentMethodType, final String paymentMethod,
			final String redirectAfterPayment, final String format) throws EtailNonBusinessExceptions
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		//creating OrderStatusRequest
		final NetbankingRequest netbankingRequest = new NetbankingRequest();
		netbankingRequest.setOrderId(juspayOrderId);
		netbankingRequest.setMerchantId(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.MARCHANTID));
		netbankingRequest.setPaymentMethodType(paymentMethodType);
		netbankingRequest.setPaymentMethod(paymentMethod);
		netbankingRequest.setRedirectAfterPayment(redirectAfterPayment);
		netbankingRequest.setFormat(format);
		//creating OrderStatusResponse
		try
		{
			final String netbankingResponse = juspayService.getNetbankingResponse(netbankingRequest);
			LOG.info("Netbanking response " + netbankingResponse);
			return netbankingResponse;
		}
		catch (final Exception e)
		{
			LOG.error("Failed to save order status in payment transaction with error: " + e);
			throw new EtailNonBusinessExceptions(e);
		}
	}


	/**
	 * This method is used to get the List of Countries
	 *
	 * @return List<String>
	 */
	@Override
	public List<String> getCountries()
	{
		return getMplPaymentService().getCountries();
	}



	/**
	 * This method fetches the stored cards from Juspay and returns them back to the Controller
	 *
	 * @return Map<Date, SavedCardData>
	 *
	 */
	@Override
	public Map<Date, SavedCardData> listStoredEMICards(final CustomerModel customer, final String bankName)
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

		//creating ListCardsRequest of Juspay
		final ListCardsRequest listCardsRequest = new ListCardsRequest().withCustomerId(customer.getUid());
		ListCardsResponse listCardsResponse = new ListCardsResponse();
		final Map<Date, SavedCardData> savedCardDataMap = new TreeMap<Date, SavedCardData>(Collections.reverseOrder());
		try
		{
			//getting ListCardsResponse by calling List Cards service of Juspay
			listCardsResponse = juspayService.listCards(listCardsRequest);
			final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard();
			final List<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();
			savedCardList.addAll(savedCardForCustomer);
			for (final StoredCard juspayCard : listCardsResponse.getCards())
			{
				final String bin = juspayCard.getCardIsin();
				final BinModel binModel = getBinService().checkBin(bin);
				if (null != binModel && null != binModel.getBank() && StringUtils.isNotEmpty(binModel.getBank().getBankName())
						&& binModel.getBank().getBankName().equalsIgnoreCase(bankName))
				{
					for (final SavedCardModel savedCard : savedCardList)
					{
						//TISEE-396
						if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber())
								&& null != binModel
								&& (StringUtils.equalsIgnoreCase("CREDIT", binModel.getCardType()) || StringUtils.equalsIgnoreCase("CC",
										binModel.getCardType())) && null != savedCard.getBillingAddress())
						{
							final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

							savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
						}

						else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber()) && null != binModel
								&& StringUtils.isEmpty(binModel.getCardType()) && null != savedCard.getBillingAddress())
						{
							final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

							savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
						}

						else if (juspayCard.getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber()) && null == binModel
								&& null != savedCard.getBillingAddress())
						{
							final SavedCardData savedCardData = setSavedCreditCards(juspayCard, binModel, savedCard);

							savedCardDataMap.put(savedCard.getCreationtime(), savedCardData);
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return savedCardDataMap;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.payment.MplPaymentFacade#applyPromotions()
	 */
	@Override
	public MplPromoPriceData applyPromotions(final CartData cartData, final CartModel cart) throws ModelSavingException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException
	{
		return getMplPaymentService().applyPromotions(cartData, cart);
	}


	//Getters and setters
	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}

	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}

	/**
	 * @return the otpGenericService
	 */
	public OTPGenericService getOtpGenericService()
	{
		return otpGenericService;
	}

	/**
	 * @param otpGenericService
	 *           the otpGenericService to set
	 */
	public void setOtpGenericService(final OTPGenericService otpGenericService)
	{
		this.otpGenericService = otpGenericService;
	}

	/**
	 * @return the blacklistService
	 */
	public BlacklistService getBlacklistService()
	{
		return blacklistService;
	}

	/**
	 * @param blacklistService
	 *           the blacklistService to set
	 */
	public void setBlacklistService(final BlacklistService blacklistService)
	{
		this.blacklistService = blacklistService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}



	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}



	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}



	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}



	/**
	 * @return the binService
	 */
	public BinService getBinService()
	{
		return binService;
	}



	/**
	 * @param binService
	 *           the binService to set
	 */
	public void setBinService(final BinService binService)
	{
		this.binService = binService;
	}



	/**
	 * @return the sendSMSFacade
	 */
	public SendSMSFacade getSendSMSFacade()
	{
		return sendSMSFacade;
	}



	/**
	 * @param sendSMSFacade
	 *           the sendSMSFacade to set
	 */
	public void setSendSMSFacade(final SendSMSFacade sendSMSFacade)
	{
		this.sendSMSFacade = sendSMSFacade;
	}

}
