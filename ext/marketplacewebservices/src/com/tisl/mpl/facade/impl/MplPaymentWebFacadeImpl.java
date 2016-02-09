/**
 *
 */
package com.tisl.mpl.facade.impl;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.DeleteCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.DeleteCardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.service.MplPaymentWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.wsdto.BillingAddressWsData;
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
	@Autowired
	private MplPaymentWebService mplPaymentWebService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private DiscountUtility discountUtility;
	@Autowired
	private MplPaymentFacade mplPaymentFacade;
	@Autowired
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Autowired
	private CartService cartService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CommerceCartService commerceCartService;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	//	@Resource(name = "sessionService")
	//	private SessionService sessionService;

	/**
	 * To Check COD Eligibility for Cart Items
	 *
	 * @param cartID
	 * @param customerID
	 * @return PaymentServiceWsData
	 */
	@Override
	public PaymentServiceWsData getCODDetails(final String cartID, final String customerID)
	{
		//final PaymentServiceWsData PaymentServiceData = getMplPaymentWebService().getCODDetails(cartID, customerID);		//SONAR Fix
		//return PaymentServiceData;		//SONAR Fix
		return getMplPaymentWebService().getCODDetails(cartID, customerID);
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
	 * @param cartID
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplPromoPriceWsDTO binValidation(final String binNo, final String paymentMode, final String cartID,
			final String userId, final String bankName) throws EtailNonBusinessExceptions
	{
		MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		MplPromoPriceData data;
		try
		{
			// Validate Correct Input
			promoPriceData = getMplPaymentWebService().validateBinNumber(binNo, paymentMode, bankName);
			if (promoPriceData.getBinCheck().booleanValue())
			{
				data = new MplPromoPriceData();
				try
				{
					//fetch usermodel against customer
					final UserModel user = getExtendedUserService().getUserForOriginalUid(userId);
					// Check userModel null
					if (null != user)
					{

						LOG.debug(String.format("binValidation Facade: | userId: %s | cartId : %s  ", userId, cartID));

						//getting cartmodel using cart id and user
						final CartModel cart = getCommerceCartService().getCartForCodeAndUser(cartID, user);
						// Validate Cart Model is not null
						if (null != cart)
						{
							if (!paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
							{
								cart.setConvenienceCharges(Double.valueOf(0.0));
								modelService.save(cart);
							}

							getCartService().setSessionCart(cart);
							//					SalesApplicationCockpit
							cart.setChannel(SalesApplication.MOBILE);
							getModelService().save(cart);

							LOG.debug("binValidation : cartModel : " + cart);

							final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
							if (null != cartData)
							{
								LOG.debug("binValidation : cartData : " + cartData);

								//Note : Response will Have DTO within which will be a list of DTO with Promo Details
								data = getMplPaymentFacade().applyPromotions(cartData, cart);

								LOG.debug("binValidation : promotionData : " + data);

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
									promoPriceData.setError(MarketplacewebservicesConstants.PROMOTIONDATAEMPTY);
									throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9803);
								}
							}
							else
							{
								promoPriceData.setError(MarketplacewebservicesConstants.CARTDATAEMPTY);
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9802);
							}
						}
						else
						{
							promoPriceData.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
						}
					}
					else
					{
						//If  User Model is null display error message
						promoPriceData.setError(MarketplacewebservicesConstants.USEREMPTY);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
					}
				}
				catch (final EtailNonBusinessExceptions e)
				{
					promoPriceData.setError(MarketplacewebservicesConstants.UPDATE_FAILURE);
				}
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
	public PaymentServiceWsData potentialPromotionOnPaymentMode(final String userId, final String cartId)
	{
		PaymentServiceWsData promoData = new PaymentServiceWsData();
		promoData = getMplPaymentWebService().potentialPromotionOnPaymentMode(userId, cartId);
		return promoData;
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

}
