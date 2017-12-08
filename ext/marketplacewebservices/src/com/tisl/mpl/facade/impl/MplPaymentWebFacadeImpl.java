/**
 *
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.DeleteCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.DeleteCardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.service.MplPaymentWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.wsdto.BillingAddressWsData;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.MplSavedCardDTO;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.PaymentServiceWsData;


/**
 * @author TCS
 *
 *
 */
public class MplPaymentWebFacadeImpl implements MplPaymentWebFacade
{
	private static final Logger LOG = Logger.getLogger(MplPaymentWebFacadeImpl.class);
	@Resource
	private MplPaymentWebService mplPaymentWebService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private DiscountUtility discountUtility;
	@Resource
	private MplPaymentFacade mplPaymentFacade;
	@Resource
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Resource
	private CartService cartService;
	@Resource
	private ModelService modelService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private ExtendedUserService extendedUserService;
	@Resource
	private MplCartWebService mplCartWebService;
	@Resource(name = "checkoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	private Converter<AddressModel, AddressData> customAddressConverter;
	private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;


	@Resource
	private MplPaymentService mplPaymentService;

	@Resource(name = "notificationFacade")
	private NotificationFacade notificationFacade;

	/**
	 * To Check COD Eligibility for Cart Items
	 *
	 * @param abstractOrder
	 * @param customerID
	 * @return PaymentServiceWsData
	 */
	@Override
	public PaymentServiceWsData getCODDetails(final AbstractOrderModel abstractOrder, final String customerID)
	{
		//final PaymentServiceWsData PaymentServiceData = getMplPaymentWebService().getCODDetails(cartID, customerID);		//SONAR Fix
		//return PaymentServiceData;		//SONAR Fix
		return getMplPaymentWebService().getCODDetails(abstractOrder, customerID);
	}

	/**
	 * Update Card Transaction Details
	 *
	 * @param juspayOrderID
	 * @param paymentMode
	 * @param cartID
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public PaymentServiceWsData updateCardTransactionDetails(final String juspayOrderID, final String paymentMode,
			final String cartID, final String userId) throws EtailNonBusinessExceptions
	{
		PaymentServiceWsData updateCardDetails = new PaymentServiceWsData();
		try
		{
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService
					.withKey(
							getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).withMerchantId(
							getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
			//Set the card Token into DeleteCardRequest
			orderStatusRequest.setOrderId(juspayOrderID);

			GetOrderStatusResponse getOrderResponse = new GetOrderStatusResponse();

			// After deleting set the return values into DeleteCardResponse
			getOrderResponse = juspayService.getOrderStatus(orderStatusRequest);

			LOG.debug("Response from juspay Web::::::::::::::::::::::::::::" + getOrderResponse);
			updateCardDetails = getMplPaymentWebService()
					.updateCardTransactionDetails(getOrderResponse, paymentMode, cartID, userId);

		}
		/*
		 * catch (final IllegalArgumentException ex) { // Error message for IllegalArgumentException throw ex; }
		 */
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return updateCardDetails;
	}

	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param userId
	 * @param cartID
	 * @return PaymentServiceWsData
	 */
	@Override
	public PaymentServiceWsData updateCODTransactionDetails(final String cartID, final String userId)
	{
		final PaymentServiceWsData updateCODDitles = getMplPaymentWebService().updateCODTransactionDetails(cartID, userId);
		return updateCODDitles;
	}

	/**
	 * @description Get Billing Address for Card Reference Number
	 * @param originalUid
	 * @param cardRefNo
	 * @return BillingAddressWsData
	 */
	@Override
	public BillingAddressWsData getBillingAddress(final String originalUid, final String cardRefNo)
	{
		//final BillingAddressWsData billingAddress = getMplPaymentWebService().getBillingAddress(originalUid, cardRefNo);	SONAR Fix
		//return billingAddress;	SONAR Fix
		return getMplPaymentWebService().getBillingAddress(originalUid, cardRefNo);
	}


	/**
	 * @Description Update Transaction and related Retails for COD
	 * @param juspayOrderId
	 * @param channel
	 * @param cartID
	 * @return MplUserResultWsDto
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public MplUserResultWsDto createEntryInAudit(final String juspayOrderId, final String channel, final String cartID,
			final String userId) throws EtailNonBusinessExceptions

	{
		MplUserResultWsDto auditEntry = new MplUserResultWsDto();
		try
		{
			auditEntry = getMplPaymentWebService().createEntryInAudit(juspayOrderId, channel, cartID, userId);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			auditEntry.setError(MarketplacewebservicesConstants.UPDATE_FAILURE);
		}
		return auditEntry;
	}

	/**
	 * This method fetches delete the saved cards
	 *
	 * @param cardToken
	 * @return MplSavedCardDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplSavedCardDTO deleteSavedCards(final String cardToken) throws EtailNonBusinessExceptions
	{
		final MplSavedCardDTO deleteCardDetails = new MplSavedCardDTO();
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

			final DeleteCardRequest deleteCardRequest = new DeleteCardRequest();
			//Set the card Token into DeleteCardRequest
			deleteCardRequest.setCardToken(cardToken);

			DeleteCardResponse deleteCardResponse = new DeleteCardResponse();

			LOG.debug("deleteCardResponse::::::::::::::::::::::::::::" + deleteCardResponse);
			// After deleting set the return values into DeleteCardResponse
			deleteCardResponse = juspayService.deleteCard(deleteCardRequest);

			//validate if isDeleted is true return success
			if (deleteCardResponse.isDeleted())
			{
				deleteCardDetails.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
			}
			else
			{
				//validate if isDeleted is false return failure
				deleteCardDetails.setError(MarketplacewebservicesConstants.UPDATE_FAILURE);
			}
			// return Success or Failure
			return deleteCardDetails;
		}
		catch (final IllegalArgumentException ex)
		{
			// Error message for IllegalArgumentException
			throw ex;
		}
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @param cart
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplPromoPriceWsDTO binValidation(final String binNo, final String paymentMode, final CartModel cart,
			final String userId, final String bankName) throws EtailNonBusinessExceptions
	{
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		MplPromoPriceData data;
		CartData cartData = null;
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("binValidation Facade: | paymentMode: %s | cartId : %s  ", userId, cart.getCode()));
		}
		try
		{
			// Validate Correct Input
			//promoPriceData = getMplPaymentWebService().validateBinNumber(binNo, paymentMode, bankName);

			//Added for TPR-1035
			promoPriceData = getMplPaymentWebService().validateBinNumber(binNo, paymentMode, bankName, userId);
			if (promoPriceData.getBinCheck().booleanValue())
			{
				data = new MplPromoPriceData();
				// Validate Cart Model is not null
				if (null != cart)
				{
					final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
					final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
					if (cart.getEntries() != null)
					{
						for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
						{
							if (cartEntryModel != null && cartEntryModel.getGiveAway() != null
									& !cartEntryModel.getGiveAway().booleanValue() && cartEntryModel.getSelectedUSSID() != null)
							{
								freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
								freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
							}
						}
					}
					//TISPRO-540 - Setting Payment mode in Cart
					cart.setChannel(SalesApplication.MOBILE);
					if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.CREDIT);
						cart.setConvenienceCharges(Double.valueOf(0.0));
						//getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.DEBIT);
						cart.setConvenienceCharges(Double.valueOf(0.0));
						//getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.NETBANKING);
						cart.setConvenienceCharges(Double.valueOf(0.0));
						//getModelService().save(cart);
					}
					//Paytm changes for mobile || Start
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.PAYTM))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.PAYTM);
						cart.setConvenienceCharges(Double.valueOf(0.0));
					}
					//Paytm changes for mobile || End
					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.EMI))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.EMI);
						cart.setConvenienceCharges(Double.valueOf(0.0));
						//getModelService().save(cart);
					}

					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.MRUPEE);
						cart.setConvenienceCharges(Double.valueOf(0.0));
						//getModelService().save(cart);
					}

					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
					{
						cart.setModeOfPayment(MarketplacewebservicesConstants.COD);
						Long convenienceCharge = baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD();
						if (null == convenienceCharge)
						{
							convenienceCharge = Long.valueOf(0);
						}
						//setting conv charge in cartmodel
						cart.setConvenienceCharges(Double.valueOf(convenienceCharge.longValue()));
						//TISEE-5555
						//getting customer mobile number
						final String mplCustomerIDCellNumber = getMplPaymentFacade().fetchPhoneNumber(cart);
						promoPriceData.setMobileNo(mplCustomerIDCellNumber);
					}
					//saving the cartmodel TISQAUAT-609
					getModelService().save(cart);

					LOG.debug("binValidation : cartModel : " + cart);
					//final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
					//TISPRD-9350--  New Method created to pass the cartModel in place of fetching data from session
					cartData = mplExtendedCartConverter.convert(cart);
					//Note : Response will Have DTO within which will be a list of DTO with Promo Details
					data = getMplPaymentFacade().applyPromotions(cartData, null, cart, null, data);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("binValidation : promotionData : " + data);
					}

					getMplPaymentFacade().populateDelvPOSForFreebie(cart, freebieModelMap, freebieParentQtyMap);
					//TISEE-515
					if (null != data)
					{
						if (StringUtils.isEmpty(data.getErrorMsgForEMI()))
						{
							//TISEE-515
							if (null != data.getConvCharge())
							{
								promoPriceData.setConvCharges(data.getConvCharge());
							}
							//Populating Delivery Charges
							if (null != data.getDeliveryCost())
							{
								promoPriceData.setDeliveryCost(data.getDeliveryCost());
							}

							//Populating Total Price Excluding Convenience
							if (null != data.getTotalExcConv())
							{
								promoPriceData.setTotalPrice(data.getTotalExcConv());
							}

							//Populating Total Price
							if (null != data.getTotalPrice())
							{
								promoPriceData.setCurrency(MarketplacewebservicesConstants.EMPTY);
								if (null != data.getCurrency())
								{
									promoPriceData.setCurrency(data.getCurrency());
								}
								//TISEE-515
								promoPriceData.setTotalPriceInclConv(data.getTotalPrice());
							}
							//Populating Sub Total Details
							if (null != cart.getSubtotal())
							{
								promoPriceData.setSubTotal(getDiscountUtility().createPrice(cart, cart.getSubtotal()));
							}
							//Populating Discount Details
							if (null != data.getTotalDiscount())
							{
								promoPriceData.setTotalDiscount(data.getTotalDiscount());
							}
						}
						else
						{
							promoPriceData.setError(data.getErrorMsgForEMI());
						}

					}
					else
					{

						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9803);
					}


				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
			}
		}
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return promoPriceData;
	}

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card --TPR-629
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @param order
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplPromoPriceWsDTO binValidation(final String binNo, final String paymentMode, final OrderModel order,
			final String userId, final String bankName) throws EtailNonBusinessExceptions
	{
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		MplPromoPriceData data;
		OrderData orderData = null;
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("binValidation Facade: | paymentMode: %s | cartId : %s  ", userId, order.getCode()));
		}
		try
		{
			// Validate Correct Input
			promoPriceData = getMplPaymentWebService().validateBinNumber(binNo, paymentMode, bankName, userId);
			if (promoPriceData.getBinCheck().booleanValue())
			{
				data = new MplPromoPriceData();
				// Validate Cart Model is not null
				if (null != order)
				{
					final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
					final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
					if (order.getEntries() != null)
					{
						for (final AbstractOrderEntryModel cartEntryModel : order.getEntries())
						{
							if (cartEntryModel != null && cartEntryModel.getGiveAway() != null
									& !cartEntryModel.getGiveAway().booleanValue() && cartEntryModel.getSelectedUSSID() != null)
							{
								freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
								freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
							}
						}
					}

					//TISPRO-540 - Setting Payment mode (In order channel can't be set)
					//order.setChannel(SalesApplication.MOBILE);
					if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.CREDIT))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.CREDIT);
						order.setConvenienceCharges(Double.valueOf(0.0));
						getModelService().save(order);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.DEBIT))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.DEBIT);
						order.setConvenienceCharges(Double.valueOf(0.0));
						getModelService().save(order);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.NETBANKING);
						order.setConvenienceCharges(Double.valueOf(0.0));
						getModelService().save(order);
					}
					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.EMI))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.EMI);
						order.setConvenienceCharges(Double.valueOf(0.0));
						getModelService().save(order);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.MRUPEE);
						order.setConvenienceCharges(Double.valueOf(0.0));
						getModelService().save(order);
					}
					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
					{
						order.setModeOfOrderPayment(MarketplacewebservicesConstants.COD);
						Long convenienceCharge = baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD();
						if (null == convenienceCharge)
						{
							convenienceCharge = Long.valueOf(0);
						}
						//setting conv charge in cartmodel
						order.setConvenienceCharges(Double.valueOf(convenienceCharge.longValue()));
						//saving the cartmodel
						getModelService().save(order);
						//TISEE-5555
						//getting customer mobile number
						final String mplCustomerIDCellNumber = getMplPaymentFacade().fetchPhoneNumber(order);
						promoPriceData.setMobileNo(mplCustomerIDCellNumber);
					}


					orderData = mplCheckoutFacade.getOrderDetailsForCode(order);
					//Note : Response will Have DTO within which will be a list of DTO with Promo Details
					data = getMplPaymentFacade().applyPromotions(null, orderData, null, order, data);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("binValidation : promotionData : " + data);
					}

					getMplPaymentFacade().populateDelvPOSForFreebie(order, freebieModelMap, freebieParentQtyMap);
					//TISEE-515
					if (null != data)
					{
						if (StringUtils.isEmpty(data.getErrorMsgForEMI()))
						{
							//TISEE-515
							if (null != data.getConvCharge())
							{
								promoPriceData.setConvCharges(data.getConvCharge());
							}

							//Populating Delivery Charges
							if (null != data.getDeliveryCost())
							{
								promoPriceData.setDeliveryCost(data.getDeliveryCost());
							}

							//Populating Total Price Excluding Convenience
							if (null != data.getTotalExcConv())
							{
								promoPriceData.setTotalPrice(data.getTotalExcConv());
							}

							//Populating Total Price
							if (null != data.getTotalPrice())
							{
								promoPriceData.setCurrency(MarketplacewebservicesConstants.EMPTY);
								if (null != data.getCurrency())
								{
									promoPriceData.setCurrency(data.getCurrency());
								}
								//TISEE-515
								promoPriceData.setTotalPriceInclConv(data.getTotalPrice());
							}
							//Populating Sub Total Details
							if (null != order.getSubtotal())
							{
								promoPriceData.setSubTotal(getDiscountUtility().createPrice(order, order.getSubtotal()));
							}
							//Populating Discount Details
							if (null != data.getTotalDiscount())
							{
								promoPriceData.setTotalDiscount(data.getTotalDiscount());
							}
						}
						else
						{
							promoPriceData.setError(data.getErrorMsgForEMI());
						}

					}
					else
					{

						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9803);
					}


				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
			}
		}
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return promoPriceData;
	}

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param userId
	 * @return CustomerModel
	 *
	 */
	@Override
	public CustomerModel getCustomer(final String userId)
	{
		final CustomerModel customer = getMplPaymentWebService().getCustomer(userId);
		if (null != customer)
		{
			return customer;
		}
		else
		{
			return null;
		}
	}

	/**
	 * This method returns the cart model based on the Cart id
	 *
	 * @param cartID
	 * @return CartModel
	 *
	 */
	@Override
	public CartModel findCartValues(final String cartID)
	{
		final CartModel cart = getMplPaymentWebService().findCartValues(cartID);
		if (null != cart)
		{
			return cart;
		}
		else
		{
			return null;
		}
	}


	/**
	 * This method returns the cart model based on the guid
	 *
	 * @param guid
	 * @return CartModel
	 *
	 */
	@Override
	public CartModel findCartAnonymousValues(final String guid)
	{
		final CartModel cart = getMplPaymentWebService().findCartAnonymousValues(guid);
		if (null != cart)
		{
			return cart;
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.MplPaymentWebFacade#potentialPromotionOnPaymentMode(java.lang.String, java.lang.String)
	 */
	@Override
	public PaymentServiceWsData potentialPromotionOnPaymentMode(final AbstractOrderModel cartModel)
	{
		PaymentServiceWsData promoData = new PaymentServiceWsData();
		promoData = getMplPaymentWebService().potentialPromotionOnPaymentMode(cartModel);
		return promoData;
	}

	/**
	 * This method returns the cart model based on the guid
	 *
	 * @param cartGuId
	 * @param pincode
	 * @return CartModel
	 *
	 */
	@Override
	public CartDataDetailsWsDTO displayOrderSummary(final String userId, final String cartId, final String cartGuId,
			final String pincode)
	{
		CartDataDetailsWsDTO cartDetailsData = new CartDataDetailsWsDTO();
		OrderModel orderModel = null;
		CartModel cartModel = null;
		if (StringUtils.isNotEmpty(cartGuId))
		{
			orderModel = mplPaymentFacade.getOrderByGuid(cartGuId);
		}
		if (null == orderModel)
		{
			cartModel = findCartValues(cartId);
			// Validate Cart Model is not null
			if (null != cartModel)
			{
				cartDetailsData = mplCartWebService.displayOrderSummary(pincode, cartModel, cartDetailsData);
				cartDetailsData.setCartGuid(cartModel.getGuid());
				cartDetailsData.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
			}
		}
		else
		{
			cartDetailsData = mplCartWebService.displayOrderSummary(pincode, orderModel, cartDetailsData);
			cartDetailsData.setCartGuid(cartGuId);
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
		}
		return cartDetailsData;
	}

	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param order
	 * @return updated
	 * @throws CalculationException
	 * @throws InvalidCartException
	 */
	@Override
	public boolean updateOrder(final OrderModel order) throws EtailBusinessExceptions, InvalidCartException, CalculationException
	{
		boolean updated = false;
		//OrderData orderData = null;
		if (null != order.getPaymentInfo() && CollectionUtils.isEmpty(order.getChildOrders()))
		{
			// INC144314180  PRDI-25
			mplCheckoutFacade.beforeSubmitOrderMobile(order);
			mplCheckoutFacade.submitOrder(order);
			//order confirmation email and sms
			notificationFacade.sendOrderConfirmationNotification(order);
			//CAR-110
			//orderData = mplCheckoutFacade.getOrderDetailsForCode(order);
			//order confirmation email and sms
			getNotificationFacade().sendOrderConfirmationNotification(order);
			updated = true;
		}
		else
		{
			//orderData = mplCheckoutFacade.getOrderDetailsForCode(order);
			updated = false;
			LOG.error("CardPayment Fail----as paymentinfo already attached");
		}
		/*
		 * if (orderData != null) { updated = true; }
		 */
		return updated;
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
	 * @return the mplPaymentWebService
	 */
	public MplPaymentWebService getMplPaymentWebService()
	{
		return mplPaymentWebService;
	}

	/**
	 * @param mplPaymentWebService
	 *           the mplPaymentWebService to set
	 */
	public void setMplPaymentWebService(final MplPaymentWebService mplPaymentWebService)
	{
		this.mplPaymentWebService = mplPaymentWebService;
	}

	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
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

	/**
	 * @return the customAddressConverter
	 */
	public Converter<AddressModel, AddressData> getCustomAddressConverter()
	{
		return customAddressConverter;
	}

	/**
	 * @param customAddressConverter
	 *           the customAddressConverter to set
	 */
	public void setCustomAddressConverter(final Converter<AddressModel, AddressData> customAddressConverter)
	{
		this.customAddressConverter = customAddressConverter;
	}

	/**
	 * @return the creditCardPaymentInfoConverter
	 */
	public Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> getCreditCardPaymentInfoConverter()
	{
		return creditCardPaymentInfoConverter;
	}

	/**
	 * @param creditCardPaymentInfoConverter
	 *           the creditCardPaymentInfoConverter to set
	 */
	public void setCreditCardPaymentInfoConverter(
			final Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
	}

	/**
	 * This method makes entry for mobile mRupee orders in Audit table
	 *
	 * @param status
	 * @param channelmobile
	 * @param guid
	 * @param walletOrderId
	 */
	@Override
	public void entryInTPWaltAuditMobile(final String status, final String channelmobile, final String guid,
			final String walletOrderId)
	{
		mplPaymentService.entryInTPWaltAudit(status, channelmobile, guid, walletOrderId);
	}

	/**
	 * This method saves payment info model for mobile mRupee orders and the returning the order
	 *
	 * @param cart
	 * @param refernceCode
	 * @param paymentMode
	 *
	 */
	@Override
	public void saveTPWalletPaymentInfoMobile(final AbstractOrderModel cart, final String refernceCode,
			final Map<String, Double> paymentMode, final String amount)
	{
		//getting the current user
		//final CustomerModel mplCustomer = (CustomerModel) getUserService().getCurrentUser();
		CustomerModel mplCustomer = null;
		String custName = null;
		if (null != cart)
		{
			mplCustomer = (CustomerModel) cart.getUser();
			final List<AbstractOrderEntryModel> entries = cart.getEntries();
			//setting payment transaction for
			mplPaymentService.setTPWalletPaymentTransaction(paymentMode, cart, refernceCode, Double.valueOf(amount));
			if (null != mplCustomer)
			{

				if (StringUtils.isNotEmpty(mplCustomer.getFirstName()) && !mplCustomer.getFirstName().equalsIgnoreCase(" "))
				{
					custName = mplCustomer.getFirstName();
					modelService.save(mplPaymentService.saveTPWalletPaymentInfo(custName, entries, cart, refernceCode));
				}

				else
				{
					custName = cart.getDeliveryAddress().getFirstname();
					modelService.save(mplPaymentService.saveTPWalletPaymentInfo(custName, entries, cart, refernceCode));
				}

			}
			mplPaymentService.paymentModeApportion(cart);
		}
		else
		{
			LOG.debug("Unable to save Third Party Wallet PAyment Info");
		}
	}


	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade()
	{
		return notificationFacade;
	}

	/**
	 * @param notificationFacade
	 *           the notificationFacade to set
	 */
	public void setNotificationFacade(final NotificationFacade notificationFacade)
	{
		this.notificationFacade = notificationFacade;
	}
}
