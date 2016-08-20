/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.localization.Localization;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
	@Resource
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
	@Resource
	private ModelService modelService;
	@Resource
	private ExtendedUserService extUserService;

	@Resource
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";

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
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
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
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				codCheckDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				codCheckDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			codCheckDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return codCheckDTO;
	}

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.BINVALIDATIONURL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public MplPromoPriceWsDTO binValidation(@PathVariable final String userId, @RequestParam final String paymentMode,
			@RequestParam final String cartGuid, @RequestParam(required = false) final String binNo,
			@RequestParam(required = false) final String bankName)
	{
		LOG.debug(String.format("binValidation : binNo :  %s | paymentMode : %s | cartGuid : %s | userId : %s | bankName : %s ",
				binNo, paymentMode, cartGuid, userId, bankName));
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		OrderModel orderModel = null;
		CartModel cart = null;
		try
		{
			if (StringUtils.isNotEmpty(cartGuid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}

			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				//TISPT-29
				if (null != cart
						&& StringUtils.isNotEmpty(paymentMode)
						&& (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING) || paymentMode
									.equalsIgnoreCase(MarketplacewebservicesConstants.EMI)))
				{
					//setting in cartmodel
					cart.setConvenienceCharges(Double.valueOf(0));
					//saving cartmodel
					modelService.save(cart);
				}

				if (getMplCheckoutFacade().isPromotionValid(cart))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);
					promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cart, userId, bankName);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
				}

			}
			else
			{
				if (null != bankName && !bankName.equalsIgnoreCase("null"))
				{
					getMplPaymentFacade().setBankForSavedCard(bankName);
				}

				//TISPT-29
				if (StringUtils.isNotEmpty(paymentMode)
						&& (paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT)
								|| paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING) || paymentMode
									.equalsIgnoreCase(MarketplacewebservicesConstants.EMI)))
				{
					//setting in cartmodel
					orderModel.setConvenienceCharges(Double.valueOf(0));
					//saving cartmodel
					modelService.save(orderModel);
				}

				if (getMplCheckoutFacade().isPromotionValid(orderModel))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);
					promoPriceData = getMplPaymentWebFacade().binValidation(binNo, paymentMode, cart, userId, bankName);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
				}
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
		catch (final Exception e)
		{
			LOG.error(MarketplacewebservicesConstants.BINVALIDATIONURL, e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				promoPriceData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				promoPriceData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
		}
		return promoPriceData;
	}

	/**
	 * This method Provided all the Saved Card Details
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
	public PaymentServiceWsData updateTransactionDetailsforCOD(@PathVariable final String userId,
			@RequestParam final String otpPin, @RequestParam final String cartGuid)
	{
		final PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();
		OrderModel orderModel = null;
		OrderData orderData = null;
		CartModel cart = null;
		LOG.debug(String.format("updateTransactionDetailsforCOD : CartId: %s | UserId : %s |", cartGuid, userId));
		try
		{
			//updateTransactionDtls = getMplPaymentWebFacade().updateCODTransactionDetails(cartGuid, userId);
			final UserModel user = getExtUserService().getUserForOriginalUid(userId);
			if (user != null)
			{
				final String validationMsg = getMplPaymentFacade().validateOTPforCODWeb(user.getUid(), otpPin);
				if (null != validationMsg)
				{
					//IF valid then proceed saving COD payment
					if (validationMsg == MarketplacecommerceservicesConstants.OTPVALIDITY)
					{
						if (StringUtils.isNotEmpty(cartGuid))
						{
							//final String orderGuid = decryptKey(guid);
							orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
						}
						if (null == orderModel)
						{
							//getting Cartdata
							cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);

							//Logic when Payment mode is COD
							if (null != cart)
							{
								//Adding cartdata into model
								final Double cartValue = cart.getSubtotal();
								final Double totalCODCharge = cart.getConvenienceCharges();

								//saving COD Payment related info
								getMplPaymentFacade().saveCODPaymentInfo(cartValue, totalCODCharge, null);

								//Mandatory checks agains cart
								final boolean isValidCart = getMplPaymentFacade().checkCart(cart);
								if (isValidCart)
								{
									orderData = mplCheckoutFacade.placeOrderByCartId(cartGuid);
									if (orderData == null)
									{
										throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9321);
									}
									else
									{
										updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
										updateTransactionDtls.setOrderId(orderData.getCode());
									}
								}
								else
								{
									updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
									updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALID_CART);
								}
							}
							else
							{
								LOG.error("Exception while completing COD Payment in /view");
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
								updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALID_CART);
							}
						}
						else
						{
							final Double orderValue = orderModel.getSubtotal();
							final Double totalCODCharge = orderModel.getConvenienceCharges();

							//saving COD Payment related info
							getMplPaymentFacade().saveCODPaymentInfo(orderValue, totalCODCharge, orderModel);

							//adding Payment id to model
							if (mplPaymentWebFacade.updateOrder(orderModel))
							{
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
								updateTransactionDtls.setOrderId(orderModel.getCode());
							}
							else
							{
								updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
								updateTransactionDtls.setError(MarketplacecommerceservicesConstants.INVALIDORDERID);
							}

						}

						updateTransactionDtls.setOtpStatus(MarketplacecommerceservicesConstants.OTP_SENT);
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
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0006);
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
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacewebservicesConstants.UPDATE_COD_TRAN_FAILED, e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				updateTransactionDtls.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				updateTransactionDtls.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return updateTransactionDtls;

	}

	// Update Transaction Details for Credit Card /Debit Card / EMI
	/**
	 * @Description Update Transaction and related Retails for Credit Card /Debit Card / EMI and Create Card
	 * @param paymentMode
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
			@RequestParam final String paymentMode, @PathVariable final String userId, @RequestParam final String cartGuid)
	{
		final PaymentServiceWsData updateTransactionDetail = new PaymentServiceWsData();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("PaymentMode: %s | cartGuid: %s | UserId : %s | juspayOrderID : %s", paymentMode, cartGuid,
					userId, juspayOrderID));
		}
		OrderModel orderToBeUpdated = null;
		String statusResponse = "";
		try
		{
			//final String orderGuid = decryptKey(guid);
			orderToBeUpdated = mplPaymentFacade.getOrderByGuid(cartGuid);
			if (null == orderToBeUpdated.getPaymentInfo())
			{
				//Wallet amount assigned. Will be changed after release1
				final double walletAmount = MarketplacewebservicesConstants.WALLETAMOUNT;
				//setting the payment modes and the amount against it in session to be used later
				final Map<String, Double> paymentInfo = new HashMap<String, Double>();
				paymentInfo.put(paymentMode, Double.valueOf(orderToBeUpdated.getTotalPriceWithConv().doubleValue() - walletAmount));

				statusResponse = mplPaymentFacade.getOrderStatusFromJuspay(cartGuid, paymentInfo, orderToBeUpdated, juspayOrderID);
				//Redirection when transaction is successful i.e. CHARGED
				if (null != statusResponse)
				{
					if (MarketplacewebservicesConstants.CHARGED.equalsIgnoreCase(statusResponse))
					{
						//return placeOrder(model, redirectAttributes);
						if (mplPaymentWebFacade.updateOrder(orderToBeUpdated))
						{
							updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
							updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
						}
						else
						{
							updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
						}
					}
					else if (MarketplacewebservicesConstants.JUSPAY_DECLINED.equalsIgnoreCase(statusResponse))
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9322);
					}
					else if (MarketplacewebservicesConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(statusResponse)
							|| MarketplacewebservicesConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(statusResponse)
							|| MarketplacewebservicesConstants.PENDING_VBV.equalsIgnoreCase(statusResponse))
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9323);
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9324);
					}
				}
				//Redirection when transaction is failed
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9324);

				}
			}
			else
			{
				if (mplPaymentWebFacade.updateOrder(orderToBeUpdated))
				{
					updateTransactionDetail.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
					updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
				}
				else
				{
					updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					updateTransactionDetail.setOrderId(orderToBeUpdated.getCode());
				}

			}
			//updateTransactionDtls = getMplPaymentWebFacade().updateCardTransactionDetails(juspayOrderID, paymentMode, cartId,
			//userId, cartGuid);

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDetail.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				updateTransactionDetail.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for All Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				updateTransactionDetail.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				updateTransactionDetail.setErrorCode(ex.getErrorCode());
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacewebservicesConstants.UPDATE_CARD_TRAN_FAILED, ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				updateTransactionDetail.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				updateTransactionDetail.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			updateTransactionDetail.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return updateTransactionDetail;
	}



	/**
	 * This method fetches delete the saved cards
	 *
	 * @return MplSavedCardDTO
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = MarketplacewebservicesConstants.GETPAYMENTMODE, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPLICATIONPRODUCES)
	@ResponseBody
	public PaymentServiceWsData getPaymentModes(@RequestParam final String cartGuid)
	{
		PaymentServiceWsData paymentModesData = new PaymentServiceWsData();
		CartModel cart = null;
		OrderModel orderModel = null;
		CartData cartData = null;
		OrderData orderData = null;
		try
		{
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				if (cart != null)
				{
					cartData = getMplExtendedCartConverter().convert(cart);
					final Map<String, Boolean> paymentMode = getMplPaymentFacade().getPaymentModes(
							MarketplacewebservicesConstants.MPLSTORE, true, cartData);
					paymentModesData = getMplPaymentWebFacade().potentialPromotionOnPaymentMode(cart);
					paymentModesData.setPaymentModes(paymentMode);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				orderData = mplCheckoutFacade.getOrderDetailsForCode(orderModel);
				//Getting Payment modes
				final Map<String, Boolean> paymentMode = getMplPaymentFacade().getPaymentModes(
						MarketplacewebservicesConstants.MPLSTORE, orderData);
				paymentModesData = getMplPaymentWebFacade().potentialPromotionOnPaymentMode(orderModel);
				paymentModesData.setPaymentModes(paymentMode);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(ex.getErrorMessage());
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				paymentModesData.setError(ex.getErrorMessage());
				paymentModesData.setErrorCode(ex.getErrorCode());
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacewebservicesConstants.GETPAYMENTMODE, ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				paymentModesData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				paymentModesData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			paymentModesData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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

	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}

	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}

}
