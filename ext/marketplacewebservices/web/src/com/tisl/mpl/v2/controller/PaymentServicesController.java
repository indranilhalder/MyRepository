/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.MplSavedCardDTO;
import com.tisl.mpl.wsdto.PaymentServiceWsDTO;
import com.tisl.mpl.wsdto.PaymentServiceWsData;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/payments", headers = "Accept=application/xml,application/json")
public class PaymentServicesController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(PaymentServicesController.class);
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private MplPaymentWebFacade mplPaymentWebFacade;
	//	@Resource(name = "mplPaymentWebDAO")
	//	private MplPaymentWebDAO mplPaymentWebDAO;
	//	@Resource(name = "cartService")
	//	private CartService cartService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	//	@Resource(name = "customerFacade")
	//	private CustomerFacade customerFacade;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;
	@Resource(name = "binService")
	private BinService binService;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ExtendedUserService extUserService;
	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

	//	/**
	//	 * @Description : For Juspay Order Creation. It returns juspayMerchantKey, juspayMerchantId, juspayReturnUrl
	//	 * @return OrderCreateInJusPayWsDto
	//	 */
	//	@RequestMapping(value = "/orderCreateInJuspay", method = RequestMethod.GET)
	//	@ResponseBody
	//	public OrderCreateInJusPayWsDto createOrderInJuspay()
	//	{
	//		final String juspayMerchantKey = !getConfigurationService().getConfiguration()
	//				.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY).isEmpty() ? getConfigurationService()
	//
	//		.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)
	//				: "No juspayMerchantKey is defined in local properties";
	//
	//		final String juspayMerchantId = !getConfigurationService().getConfiguration()
	//				.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty() ? getConfigurationService().getConfiguration()
	//				.getString(MarketplacecommerceservicesConstants.MARCHANTID) : "No juspayMerchantId is defined in local properties";
	//
	//
	//		final String juspayReturnUrl = !getConfigurationService().getConfiguration()
	//				.getString(MarketplacecommerceservicesConstants.RETURNURL).isEmpty() ? getConfigurationService().getConfiguration()
	//				.getString(MarketplacecommerceservicesConstants.RETURNURL) : "No juspayReturnUrl is defined in local properties";
	//
	//
	//		final OrderCreateInJusPayWsDto orderCreateInJusPayWsDto = new OrderCreateInJusPayWsDto();
	//		orderCreateInJusPayWsDto.setJuspayMerchantKey(juspayMerchantKey);
	//		orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
	//		orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
	//
	//		return orderCreateInJusPayWsDto;
	//	}

	//  COD Eligible Check
	/**
	 * @Description COD Eligibility check for all the items in the cart (check Bleack list Consumer Sailor Fulfillment
	 *              Item Eligibility and Pin Code Eligibility)
	 * @param pinCode
	 * @param cartID
	 *
	 * @return PaymentServiceWsDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.CODELIGIBILITYURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsDTO getCODEligibility(@RequestParam final String cartID, @PathVariable final String userId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		PaymentServiceWsData codCheck = new PaymentServiceWsData();
		PaymentServiceWsDTO codCheckDTO = new PaymentServiceWsDTO();
		LOG.debug(String.format("getCODEligibility : cartID:  %s | userId : %s ", cartID, userId));
		try
		{
			if (StringUtils.isEmpty(cartID))
			{
				codCheck.setError(MarketplacewebservicesConstants.CARTIDNULL);
			}
			else
			{
				final CartModel cart = getMplPaymentWebFacade().findCartValues(cartID);
				if (null != cart)
				{
					CustomerModel customer = modelService.create(CustomerModel.class);

					customer = getMplPaymentWebFacade().getCustomer(userId);

					final boolean mplCustomerIsBlackListed = getMplPaymentFacade().isBlackListed(customer.getUid(), cart);
					//To check if the customer is a black listed customer
					if (!mplCustomerIsBlackListed)
					{
						//Getting COD details
						codCheck = getMplPaymentWebFacade().getCODDetails(cartID, customer.getUid());
					}
					else
					{
						//Message to display Customer is Black list Consumer
						codCheck.setError(MarketplacewebservicesConstants.BLACKLIST);
					}

					if (codCheck != null)
					{
						//Putting data into DTOs
						codCheckDTO = dataMapper.map(codCheck, PaymentServiceWsDTO.class, fields);
					}
				}
				else
				{
					codCheckDTO.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
				}
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				codCheckDTO.setError(ex.getErrorMessage());
				codCheckDTO.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				codCheckDTO.setError(ex.getErrorMessage());
				codCheckDTO.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				codCheckDTO.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				codCheckDTO.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return codCheckDTO;
	}

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @param cartID
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.BINVALIDATIONURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplPromoPriceWsDTO binValidation(@PathVariable final String userId, @RequestParam(required = false) final String binNo,
			@RequestParam final String paymentMode, @RequestParam final String cartID,
			@RequestParam(required = false) final String bankName)
	{
		LOG.debug(String.format("binValidation : binNo :  %s | paymentMode : %s | cartID : %s | userId : %s | bankName : %s ",
				binNo, paymentMode, cartID, userId, bankName));
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		try
		{
			if (StringUtils.isNotEmpty(cartID) && StringUtils.isNotEmpty(paymentMode))
			{
				if (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
				{
					if (StringUtils.isEmpty(binNo) && StringUtils.isEmpty(bankName))
					{
						promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cartID, userId, bankName);
					}
					else
					{
						promoPriceData.setError(MarketplacewebservicesConstants.PROPERMODEOFPAYMENT);
					}
				}
				else if (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING))
				{
					if (StringUtils.isEmpty(binNo) && StringUtils.isNotEmpty(bankName))
					{
						promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cartID, userId, bankName);
					}
					else
					{
						promoPriceData.setError(MarketplacewebservicesConstants.BANKNAMEEMPTY);
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(binNo) && StringUtils.isEmpty(bankName))
					{
						promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cartID, userId, bankName);
					}
					else
					{
						promoPriceData.setError(MarketplacewebservicesConstants.BINNOEMPTY);
					}
				}
			}
			else
			{
				promoPriceData.setError(MarketplacewebservicesConstants.BINCHECKINPUTDATA);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions

			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				promoPriceData.setError(ex.getErrorMessage());
				promoPriceData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				promoPriceData.setError(ex.getErrorMessage());
				promoPriceData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				promoPriceData.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				promoPriceData.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return promoPriceData;
	}

	/*
	 * // Get Billing Address
	 *//**
	 * @Description Get Billing Address
	 * @param userId
	 * @param cardRefNo
	 * @return BillingAddressWsData
	 * @throws EtailNonBusinessExceptions
	 */
	/*
	 *
	 * @Secured( { "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 *
	 * @RequestMapping(value = MarketplacewebservicesConstants.BILLINGADDRESSURL, method = RequestMethod.POST, produces =
	 * MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	 *
	 * @ResponseBody public BillingAddressWsData getBillingAddress(@PathVariable final String userId, @RequestParam final
	 * String cardRefNo) { LOG.debug(String.format("getBillingAddress : cardRefNo : %s", cardRefNo));
	 * BillingAddressWsData billingAddress = new BillingAddressWsData(); try { if (StringUtils.isNotEmpty(cardRefNo)) {
	 * billingAddress = mplPaymentWebFacade.getBillingAddress(userId, cardRefNo); } else {
	 * billingAddress.setError(MarketplacewebservicesConstants.CRDREFNONULL); } } catch (final EtailNonBusinessExceptions
	 * ex) { // Error message for All Exceptions ExceptionUtil.etailNonBusinessExceptionHandler(ex); if (null !=
	 * ex.getErrorMessage()) { billingAddress.setError(ex.getErrorMessage());
	 * billingAddress.setErrorCode(ex.getErrorCode()); } } catch (final EtailBusinessExceptions ex) { // Error message
	 * for All Exceptions ExceptionUtil.etailBusinessExceptionHandler(ex, null); if (null != ex.getErrorMessage()) {
	 * billingAddress.setError(ex.getErrorMessage()); billingAddress.setErrorCode(ex.getErrorCode()); } } catch (final
	 * Exception ex) { // Error message for All Exceptions if (null != ((EtailBusinessExceptions) ex).getErrorMessage())
	 * { billingAddress.setError(((EtailBusinessExceptions) ex).getErrorMessage());
	 * billingAddress.setErrorCode(((EtailBusinessExceptions) ex).getErrorCode()); } } return billingAddress; }
	 *//**
	 * @Description Update Transaction and related Retails for COD
	 * @param juspayOrderId
	 * @param channel
	 * @param cartID
	 * @return MplUserResultWsDto
	 * @throws EtailNonBusinessExceptions
	 */
	/*
	 *
	 * @Secured( { "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 *
	 * @RequestMapping(value = MarketplacewebservicesConstants.CREATEENTRYINAUDITURL, method = RequestMethod.POST,
	 * produces = "application/json")
	 *
	 * @ResponseBody public MplUserResultWsDto createEntryInAudit(@RequestParam final String juspayOrderId, @RequestParam
	 * String channel,
	 *
	 * @RequestParam final String cartID, @PathVariable final String userId) { LOG.debug(String.format(
	 * "createEntryInAudit : juspayOrderId: %s | channel: %s | cartID : %s", juspayOrderId, channel));
	 *
	 * MplUserResultWsDto auditEntry = new MplUserResultWsDto(); try { channel =
	 * MarketplacecommerceservicesConstants.CHANNEL_WEBMOBILE; if (StringUtils.isNotEmpty(juspayOrderId) &&
	 * StringUtils.isNotEmpty(channel) && StringUtils.isNotEmpty(cartID)) { auditEntry =
	 * mplPaymentWebFacade.createEntryInAudit(juspayOrderId, channel, cartID, userId); } else {
	 * auditEntry.setError(MarketplacewebservicesConstants.AUDITENTRYINPUTDATA); } } catch (final
	 * EtailNonBusinessExceptions ex) { // Error message for All Exceptions
	 * ExceptionUtil.etailNonBusinessExceptionHandler(ex); if (null != ex.getErrorMessage()) {
	 * auditEntry.setError(ex.getErrorMessage()); auditEntry.setErrorCode(ex.getErrorCode()); } } catch (final
	 * EtailBusinessExceptions ex) { // Error message for All Exceptions ExceptionUtil.etailBusinessExceptionHandler(ex,
	 * null); if (null != ex.getErrorMessage()) { auditEntry.setError(ex.getErrorMessage());
	 * auditEntry.setErrorCode(ex.getErrorCode()); } } catch (final Exception ex) { // Error message for All Exceptions
	 * if (null != ((EtailBusinessExceptions) ex).getErrorMessage()) { auditEntry.setError(((EtailBusinessExceptions)
	 * ex).getErrorMessage()); auditEntry.setErrorCode(((EtailBusinessExceptions) ex).getErrorCode()); } } return
	 * auditEntry; }
	 */

	/**
	 * This method Provied all the Saved Card Details
	 *
	 * @param cardType
	 * @param userId
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.SAVEDCARDS, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplSavedCardDTO savedCards(@RequestParam final String cardType, @PathVariable final String userId,
			@RequestParam(required = false) final String bankName)
	{
		LOG.debug(String.format("savedCards :  cardType : %s bankName : %s userId : %s| ", cardType, bankName, userId));


		final MplSavedCardDTO savedCards = new MplSavedCardDTO();
		// Create Map to get saved card details
		Map<Date, SavedCardData> savedCardsMap = new TreeMap<Date, SavedCardData>();
		Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();
		try
		{
			CustomerModel customer = modelService.create(CustomerModel.class);
			customer = getMplPaymentWebFacade().getCustomer(userId);
			//validate if all the inputs are available
			//			if (StringUtils.isNotEmpty(cardType) || StringUtils.isNotEmpty(bankName))
			//			{
			// If card type is Credit Card
			if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CC) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredCreditCards(customer);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// If card type is Debit Card
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.DC) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredDebitCards(customer);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// if the card type is EMI
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CC) && StringUtils.isNotEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredEMICards(customer, bankName);
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			// If card type is Both
			else if (cardType.equalsIgnoreCase(MarketplacewebservicesConstants.CCDC_BOTH) && StringUtils.isEmpty(bankName))
			{
				savedCardsMap = getMplPaymentFacade().listStoredCreditCards(customer);
				savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer);
				// Add everything in savedCardsMap from savedDebitCards
				savedCardsMap.putAll(savedDebitCards);
				//Adding details into DTO
				savedCards.setSavedCardDetailsMap(savedCardsMap);
			}
			else if (StringUtils.isEmpty(cardType) && StringUtils.isEmpty(bankName))
			{
				// else provide error message
				savedCards.setError(MarketplacewebservicesConstants.CARDTYPE_CONID);
			}


			if (null == savedCards.getSavedCardDetailsMap() || savedCards.getSavedCardDetailsMap().isEmpty())
			{
				savedCards.setStatus(MarketplacewebservicesConstants.CARDDATA);
			}
			//			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions

			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				savedCards.setError(ex.getErrorMessage());
				savedCards.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				savedCards.setError(ex.getErrorMessage());
				savedCards.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				savedCards.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				savedCards.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return savedCards;
	}

	/**
	 * This method fetches delete the saved cards
	 *
	 * @param cardToken
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REMOVESAVEDCARDS, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplSavedCardDTO removeSavedCards(@RequestParam final String cardToken)
	{
		LOG.debug(String.format("removeSavedCards : cardToken: %s| ", cardToken));
		MplSavedCardDTO deleteCardDetails = new MplSavedCardDTO();
		try
		{
			// Return if deleted or not
			if (StringUtils.isNotEmpty(cardToken))
			{
				deleteCardDetails = getMplPaymentWebFacade().deleteSavedCards(cardToken);
			}
			else
			{
				deleteCardDetails.setError(MarketplacewebservicesConstants.NOCARDTOKEN);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				deleteCardDetails.setError(ex.getErrorMessage());
				deleteCardDetails.setErrorCode(ex.getErrorCode());
			}
		}

		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				deleteCardDetails.setError(ex.getErrorMessage());
				deleteCardDetails.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				deleteCardDetails.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				deleteCardDetails.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return deleteCardDetails;
	}


	/**
	 * @description Update Transaction and related Retails for COD alos create Order
	 * @param cartId
	 * @param otpPin
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 */

	//GetOrderStatusResponse
	@Secured(
	{ "ROLE_CLIENT", CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.UPDATETRANSACTIONFORCODURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData updateTransactionDetailsforCOD(@RequestParam final String cartId,
			@PathVariable final String userId, @RequestParam final String otpPin)
	{
		PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();

		LOG.debug(String.format("updateTransactionDetailsforCOD : CartId: %s | UserId : %s |", cartId, userId));

		try
		{
			updateTransactionDtls = getMplPaymentWebFacade().updateCODTransactionDetails(cartId, userId);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			updateTransactionDtls.setError(MarketplacecommerceservicesConstants.ORDER_ERROR);
			LOG.error(MarketplacewebservicesConstants.UPDATE_COD_TRAN_FAILED, ex);
		}

		try
		{
			if (StringUtils.isNotEmpty(updateTransactionDtls.getStatus())
					&& updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.UPDATE_SUCCESS))
			{
				final UserModel user = getExtUserService().getUserForOriginalUid(userId);
				final String validationMsg = getMplPaymentFacade().validateOTPforCODWeb(user.getUid(), otpPin);
				if (null != validationMsg)
				{
					if (validationMsg == MarketplacecommerceservicesConstants.OTPVALIDITY)
					{
						//after validation create order
						final OrderData orderdata = getMplCheckoutFacade().placeOrderByCartId(cartId, userId);
						if (orderdata != null)
						{
							updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							updateTransactionDtls.setOtpStatus(MarketplacecommerceservicesConstants.OTP_SENT);
							updateTransactionDtls.setOrderId(orderdata.getCode());
						}
						else
						{
							updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							updateTransactionDtls.setError(MarketplacecommerceservicesConstants.ORDER_ERROR);
						}
					}
					else if (validationMsg == MarketplacecommerceservicesConstants.OTPEXPIRY)
					{
						updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						updateTransactionDtls.setError(MarketplacecommerceservicesConstants.OTP_EXPIRY_MESSAGE);
					}
					else
					{
						updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALID_OTP);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
				updateTransactionDtls.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
				updateTransactionDtls.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				updateTransactionDtls.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				updateTransactionDtls.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return updateTransactionDtls;

	}

	// Update Transaction Details for Credit Card /Debit Card / EMI
	/**
	 * @Description Update Transaction and related Retails for Credit Card /Debit Card / EMI and Create Card
	 * @param paymentMode
	 *           (Json)
	 * @param orderStatusResponse
	 *           (Json)
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 */
	//GetOrderStatusResponse

	@Secured(
	{ "ROLE_CLIENT", CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.UPDATETRANSACTIONFORCARDURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData updateTransactionDetailsforCard(@RequestParam final String juspayOrderID,

	@RequestParam final String paymentMode, @PathVariable final String userId, @RequestParam final String cartId)
	{
		PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();

		LOG.debug(String.format("Order Status Response : %s ", juspayOrderID));
		LOG.debug(String.format("PaymentMode: %s | CartId: %s | UserId : %s", paymentMode, cartId, userId));

		try
		{
			updateTransactionDtls = getMplPaymentWebFacade()
					.updateCardTransactionDetails(juspayOrderID, paymentMode, cartId, userId);

			LOG.debug(String.format("Update transaction details status %s ",
					((null != updateTransactionDtls.getStatus()) ? updateTransactionDtls.getStatus() : "")));
		}
		catch (final Exception e)
		{
			updateTransactionDtls.setError(MarketplacewebservicesConstants.UPDATE_CARD_TRAN_FAILED);
			LOG.error(MarketplacewebservicesConstants.UPDATE_CARD_TRAN_FAILED, e);
		}
		try
		{
			if (StringUtils.isNotEmpty(updateTransactionDtls.getStatus())
					&& updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.UPDATE_SUCCESS))
			{
				final OrderData orderData = getMplCheckoutFacade().placeOrderByCartId(cartId, userId);
				if (orderData != null)
				{
					updateTransactionDtls.setOrderId(orderData.getCode());
				}
				else
				{
					updateTransactionDtls.setError(MarketplacewebservicesConstants.ORDER_ERROR);
				}
			}
			else if (StringUtils.isNotEmpty(updateTransactionDtls.getStatus())
					&& updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.JUSPAY_DECLINED))
			{
				updateTransactionDtls.setError(MarketplacewebservicesConstants.JUSPAY_DECLINED_ERROR);
			}
			else if (StringUtils.isNotEmpty(updateTransactionDtls.getStatus())
					&& (updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.AUTHORIZATION_FAILED))
					|| updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.AUTHENTICATION_FAILED)
					|| updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.PENDING_VBV))
			{
				updateTransactionDtls.setError(MarketplacewebservicesConstants.JUSPAY_FAILED_ERROR);
			}
			else
			{
				updateTransactionDtls.setError(MarketplacewebservicesConstants.PAYMENTUPDATE_ERROR);
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDtls.setError(ex.getErrorMessage());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				updateTransactionDtls.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
			}
		}
		return updateTransactionDtls;
	}



	/**
	 * This method fetches delete the saved cards
	 *
	 * @param cardToken
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = MarketplacewebservicesConstants.GETPAYMENTMODE, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData getPaymentModes(@PathVariable final String userId, @RequestParam final String cartId)
	{
		//		LOG.debug(String.format("removeSavedCards : cardToken: %s| ", cardToken));
		PaymentServiceWsData paymentModesData = new PaymentServiceWsData();
		try
		{
			final Map<String, Boolean> paymentMode = getMplPaymentFacade().getPaymentModes("mpl");
			paymentModesData = getMplPaymentWebFacade().potentialPromotionOnPaymentMode(userId, cartId);
			paymentModesData.setPaymentModes(paymentMode);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(MarketplacewebservicesConstants.NODATAAVAILABLE);
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
		}

		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(MarketplacewebservicesConstants.NODATAAVAILABLE);
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				paymentModesData.setError(MarketplacewebservicesConstants.NODATAAVAILABLE);
				paymentModesData.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return paymentModesData;
	}

	//Commenting due to SONAR Fix
	//	/**
	//	 * @Description : Populating Promotion Details
	//	 * @param promoData
	//	 * @param data
	//	 * @return dtoData
	//	 */
	//	private MplPromotionDTO getPromoDetails(final MplPromotionData promoData, final MplPromoPriceData data)
	//	{
	//		final MplPromotionDTO dtoData = new MplPromotionDTO();
	//		final Map<String, String> promoIdMsg = new HashMap<String, String>();
	//
	//		if (null != promoData)
	//		{
	//			if (promoData.getPromoTypeIdentifier().equalsIgnoreCase(MarketplacewebservicesConstants.POTENTIALPROMOTION)
	//					&& null != promoData.getPotentialPromotion() && null != promoData.getPotentialPromotion().getPromoMessage())
	//			{
	//				promoIdMsg.put(promoData.getPromoTypeIdentifier(), promoData.getPotentialPromotion().getPromoMessage());
	//				dtoData.setPromoIdMessage(promoIdMsg);
	//			}
	//			else
	//			{
	//				if (data.getTotalDiscount().getValue().compareTo(BigDecimal.ZERO) > 0)
	//				{
	//					if (null != promoData.getFiredPromotion() && null != promoData.getFiredPromotion().getPromoMessage())
	//					{
	//						promoIdMsg.put(promoData.getPromoTypeIdentifier(), promoData.getFiredPromotion().getPromoMessage());
	//						dtoData.setPromoIdMessage(promoIdMsg);
	//					}
	//
	//					if (null != data.getCurrency() && null != data.getCurrency())
	//					{
	//						dtoData.setDiscountPriceCurrency(data.getCurrency());
	//					}
	//
	//					if (null != promoData.getIsPercentage())
	//					{
	//						dtoData.setIsPercentage(promoData.getIsPercentage());
	//					}
	//					if (null != promoData.getPercentagePromotion())
	//					{
	//						dtoData.setPercentagePromotion(promoData.getPercentagePromotion());
	//					}
	//				}
	//			}
	//		}
	//
	//		return dtoData;
	//	}

	/**
	 * @return the mplCustomAddressFacade
	 */
	public MplCustomAddressFacade getMplCustomAddressFacade()
	{
		return mplCustomAddressFacade;
	}

	/**
	 * @param mplCustomAddressFacade
	 *           the mplCustomAddressFacade to set
	 */
	public void setMplCustomAddressFacade(final MplCustomAddressFacade mplCustomAddressFacade)
	{
		this.mplCustomAddressFacade = mplCustomAddressFacade;
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
	 * @return the mplPaymentFacade
	 */
	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}

	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
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
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}

	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
	}

	/**
	 * @return the mplPaymentWebFacade
	 */
	public MplPaymentWebFacade getMplPaymentWebFacade()
	{
		return mplPaymentWebFacade;
	}

	/**
	 * @param mplPaymentWebFacade
	 *           the mplPaymentWebFacade to set
	 */
	public void setMplPaymentWebFacade(final MplPaymentWebFacade mplPaymentWebFacade)
	{
		this.mplPaymentWebFacade = mplPaymentWebFacade;
	}

	/**
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}

}
