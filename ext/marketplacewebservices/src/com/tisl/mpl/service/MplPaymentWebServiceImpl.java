package com.tisl.mpl.service;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.dao.MplPaymentWebDAO;
import com.tisl.mpl.data.BinData;
import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentModeSpecificPromotionRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.wsdto.BillingAddressWsData;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.PaymentServiceWsData;
import com.tisl.mpl.wsdto.PotentialRestrictionData;


/**
 * @author TCS
 *
 */
public class MplPaymentWebServiceImpl implements MplPaymentWebService
{
	private static final Logger LOG = Logger.getLogger(MplPaymentWebService.class);
	@Resource(name = "mplPaymentWebDAO")
	private MplPaymentWebDAO mplPaymentWebDAO;
	//	@Resource(name = "productService")
	//	private ProductService productService;		SONAR Fix
	@Resource(name = "binService")
	private BinService binService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "i18NService")
	private I18NService i18NService;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Autowired
	private CommerceCartService commerceCartService;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MplSellerInformationService mplSellerInformationService;
	@Autowired
	private CartService cartService;


	/**
	 * To Check COD Eligibility for Cart Items
	 *
	 * @param cartID
	 * @param customerID
	 * @return PaymentServiceWsData
	 */
	@Override
	public PaymentServiceWsData getCODDetails(final String cartID, final String customerID) throws EtailNonBusinessExceptions
	{
		PaymentServiceWsData paymentServiceData = new PaymentServiceWsData();
		try
		{
			//getting cartmodel using cart id
			final CartModel cartModel = getMplPaymentWebDAO().findCartValues(cartID);

			if (null != cartModel)
			{
				//item eligible for COD or not
				final List<String> paymentTypeList = new ArrayList<String>();

				//to check items are seller fulfilled or not
				final List<String> fulfillmentDataList = new ArrayList<String>();

				//iterating over all the cart entries
				LOG.debug(" getCODDetails ServiceImpl : cartModel.getEntries() : " + cartModel.getEntries());

				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					//getting the product code
					LOG.debug(" getCODDetails ServiceImpl : entry.getProduct().getCode() : " + entry.getProduct().getCode());

					if (entry.getSelectedUSSID() != null)
					{
						final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
								entry.getSelectedUSSID());
						List<RichAttributeModel> richAttributeModel = null;
						if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
						{
							richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
							if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
							{
								final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
								LOG.debug(" getCODDetails ServiceImpl : fulfillmentType : " + fulfillmentType);
								fulfillmentDataList.add(fulfillmentType.toUpperCase());
							}

							if (richAttributeModel != null && richAttributeModel.get(0).getPaymentModes() != null)
							{
								final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
								if (StringUtils.isNotEmpty(paymentMode))
								{
									//setting the payment mode in a list
									paymentTypeList.add(paymentMode);
								}
							}
						}
						else
						{
							// richAttributeModel or sellerInfoModel is null or empty display error message
							paymentServiceData.setError(MarketplacewebservicesConstants.NODATAAVAILABLE);
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9052);
						}
					}
					else
					{
						// Selected USSID is null or empty display error message
						paymentServiceData.setError(MarketplacewebservicesConstants.NODATAAVAILABLE);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9052);
					}
				}
				paymentServiceData = checkCODEligibility(paymentTypeList, fulfillmentDataList, cartModel);
			}
			else
			{
				paymentServiceData.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
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
		return paymentServiceData;
	}



	/**
	 * checkCODEligibility in separate method and call the function into getCODDetails
	 *
	 * @param paymentTypeList
	 * @param fulfillmentDataList
	 * @param cartModel
	 * @return PaymentServiceWsData
	 */
	private PaymentServiceWsData checkCODEligibility(final List<String> paymentTypeList, final List<String> fulfillmentDataList,
			final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		final PaymentServiceWsData PaymentServiceData = new PaymentServiceWsData();
		//declaring a counter
		//int countItemNo = 0;
		//int countTshipNo = 0;
		boolean tshipFlag = true;
		try
		{
			LOG.debug("checkCODEligibility : fulfillmentDataList  : " + fulfillmentDataList + " % paymentTypeList : "
					+ paymentTypeList);
			//Validate  fulfillmentDataList is not empty and paymentTypeList is not empty
			if (!fulfillmentDataList.isEmpty() && !paymentTypeList.isEmpty())
			{
				//iterating over the list of Sailor Fulfillment for all the cart entries
				for (final String fulfillment : fulfillmentDataList)
				{
					LOG.debug("checkCODEligibility : fulfillment Seller  : " + fulfillment);

					if (!com.tisl.mpl.core.enums.DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillment))
					{
						tshipFlag = false;
						break;
						//countTshipNo = countTshipNo + 1; //if item is eligible for TSHIP countTshipNo increases +1 when Seller fulfillment is not TSHIP
					}
				}

				//if (countTshipNo == fulfillmentDataList.size())
				if (tshipFlag)
				{
					//declaring a flag
					boolean codEligibilityFlag = true;

					//iterating over the list of Payment types for all the cart entries
					for (final String paymentType : paymentTypeList)
					{
						LOG.debug("checkCODEligibility : paymentType  : " + paymentType);

						if (PaymentModesEnum.COD.toString().equalsIgnoreCase(paymentType)
								|| PaymentModesEnum.BOTH.toString().equalsIgnoreCase(paymentType))
						{
							//flag set to true if the item's payment type is either COD or Both
							codEligibilityFlag = true;
						}
						else
						{ //flag set to false if the item's payment type is Prepaid
							codEligibilityFlag = false;
							break;
						}
					}

					// If Item is Eligible for COD
					//if (countItemNo == paymentTypeList.size()) // if Item is COD Eligible no nf items eligible for COD is equal to size of cart
					if (codEligibilityFlag)
					{
						//if item is TSHIP then adding to model true
						if (null != cartModel.getIsCODEligible() && cartModel.getIsCODEligible().booleanValue())
						{
							PaymentServiceData.setStatus(MarketplacewebservicesConstants.COD_ELIGIBLE);
							Long convenienceCharge = getBaseStoreService().getCurrentBaseStore().getConvenienceChargeForCOD();
							if (null == convenienceCharge)
							{
								convenienceCharge = Long.valueOf(0);
							}
							cartModel.setConvenienceCharges(Double.valueOf(convenienceCharge.doubleValue()));
							modelService.save(cartModel);
							PaymentServiceData.setConvenienceCharge(convenienceCharge.toString());
							PaymentServiceData.setTotalPrice(cartModel.getTotalPrice().toString());
						}
						else
						{
							//Message to display not eligible for COD
							PaymentServiceData.setError(MarketplacewebservicesConstants.PIN_CODE_ELIGIBLE);
						}
					}
					else
					{
						//if Item is COD Eligible no nf items eligible for COD dispaly respective message
						PaymentServiceData.setError(MarketplacewebservicesConstants.ITEM_ELIGIBLE);
					}
				}
				else
				{
					//if item is not eligible for TSHIP countTshipNo display respective message
					PaymentServiceData.setError(MarketplacewebservicesConstants.SSHIP_ELIGIBLE);
				}
			}
			else
			{
				//If fulfillmentDataList is empty or paymentTypeList is empty  display error message
				PaymentServiceData.setError(MarketplacewebservicesConstants.INVALIDDATA);
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
		return PaymentServiceData;
	}

	/**
	 * This private method get the payment mode and set it into a map from Json
	 *
	 * @param paymentMode
	 * @return paymentModeMap
	 */
	/*
	 * private Map<String, Double> getPaymentMode(final String paymentMode) { LOG.debug(
	 * "getPaymentMode : paymentMode  JSON Response : " + paymentMode); // Payment Mode Map final Map<String, Double>
	 * paymentModeMap = new HashMap<String, Double>(); try { final JSONObject rec_paymode = (JSONObject)
	 * JSONValue.parse(paymentMode);
	 *
	 * LOG.debug("getPaymentMode : rec_paymode  JSON Response : " + rec_paymode);
	 *
	 * // Fetch Details from Json final String debit = rec_paymode.get(MarketplacewebservicesConstants.DEBIT) != null ?
	 * rec_paymode.get( MarketplacewebservicesConstants.DEBIT).toString() :
	 * MarketplacewebservicesConstants.DECIMALULLCHK; final String credit =
	 * rec_paymode.get(MarketplacewebservicesConstants.CREDIT) != null ? rec_paymode.get(
	 * MarketplacewebservicesConstants.CREDIT).toString() : MarketplacewebservicesConstants.DECIMALULLCHK; final String
	 * emi = rec_paymode.get(MarketplacewebservicesConstants.EMI) != null ? rec_paymode.get(
	 * MarketplacewebservicesConstants.EMI).toString() : MarketplacewebservicesConstants.DECIMALULLCHK; final String
	 * netBanking = rec_paymode.get(MarketplacewebservicesConstants.NETBANKING) != null ? rec_paymode.get(
	 * MarketplacewebservicesConstants.NETBANKING).toString() : MarketplacewebservicesConstants.DECIMALULLCHK;
	 *
	 * // Get data in Double value final Double debit_amt = new Double(debit); final Double credit_amt = new
	 * Double(credit); final Double emi_amt = new Double(emi); final Double net_amt = new Double(netBanking);
	 *
	 * // Validate Payment Mode Value and set value into map if (debit != MarketplacewebservicesConstants.DECIMALULLCHK)
	 * { paymentModeMap.put(MarketplacewebservicesConstants.DEBIT, debit_amt); } if (credit !=
	 * MarketplacewebservicesConstants.DECIMALULLCHK) { paymentModeMap.put(MarketplacewebservicesConstants.CREDIT,
	 * credit_amt); } if (emi != MarketplacewebservicesConstants.DECIMALULLCHK) {
	 * paymentModeMap.put(MarketplacewebservicesConstants.EMI, emi_amt); } if (netBanking !=
	 * MarketplacewebservicesConstants.DECIMALULLCHK) { paymentModeMap.put(MarketplacewebservicesConstants.NETBANKING,
	 * net_amt); }
	 *
	 * LOG.debug("getPaymentMode : rec_paymode  JSON Response paymentModeMap : " + paymentModeMap); } catch (final
	 * EtailBusinessExceptions | EtailNonBusinessExceptions e) { throw e; } catch (final Exception e) { throw new
	 * EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); } // returns a Map return
	 * paymentModeMap; }
	 */


	//SONAR Fix. Method not used
	//	/**
	//	 * This private method set values into Get Order Responce from Json
	//	 *
	//	 * @param orderStatusResponse
	//	 * @return orderStatusResponseJuspay
	//	 */
	//	private GetOrderStatusResponse getRequestParameterForSard(final String orderStatusResponse)
	//	{
	//		final GetOrderStatusResponse orderStatusResponseJuspay = new GetOrderStatusResponse();
	//		final CardResponse cardResponse = new CardResponse();
	//		final PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();
	//		final RiskResponse riskResponse = new RiskResponse();
	//
	//		try
	//		{
	//			final JSONObject jobjectOrdrRes = (JSONObject) JSONValue.parse(orderStatusResponse);
	//			// Set values into GetOrderStatusResponse
	//			orderStatusResponseJuspay.setMerchantId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.MERCHENTID));
	//			orderStatusResponseJuspay.setOrderId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.ORDERID));
	//			orderStatusResponseJuspay.setAmount(getDoubleValue(jobjectOrdrRes.get(MarketplacewebservicesConstants.AMOUNT)));
	//			orderStatusResponseJuspay.setStatus((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.STATUS));
	//			orderStatusResponseJuspay.setStatusId((Long) jobjectOrdrRes.get(MarketplacewebservicesConstants.STATUSID));
	//			orderStatusResponseJuspay.setTxnId((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.TXNID));
	//			orderStatusResponseJuspay.setGatewayId((Long) jobjectOrdrRes.get(MarketplacewebservicesConstants.GATEWAYID));
	//			orderStatusResponseJuspay.setBankErrorCode((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.BANKERRORCODE));
	//			orderStatusResponseJuspay
	//					.setBankErrorMessage((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.BANKERRORMESSAGE));
	//			orderStatusResponseJuspay.setUdf1((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD1));
	//			orderStatusResponseJuspay.setUdf2((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD2));
	//			orderStatusResponseJuspay.setUdf3((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD3));
	//			orderStatusResponseJuspay.setUdf4((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD4));
	//			orderStatusResponseJuspay.setUdf5((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD5));
	//			orderStatusResponseJuspay.setUdf6((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD6));
	//			orderStatusResponseJuspay.setUdf7((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD7));
	//			orderStatusResponseJuspay.setUdf8((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD8));
	//			orderStatusResponseJuspay.setUdf9((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD9));
	//			orderStatusResponseJuspay.setUdf10((String) jobjectOrdrRes.get(MarketplacewebservicesConstants.UFD10));
	//
	//			// Fetch Details from Json for cardResponse
	//			final JSONObject jobjectRisk = (JSONObject) jobjectOrdrRes.get(MarketplacewebservicesConstants.RISK);
	//
	//			// Set values into Risk Response
	//			riskResponse.setEbsBinCountry((String) jobjectRisk.get(MarketplacewebservicesConstants.EBSBINCOUNTRY));
	//			riskResponse.setEbsRiskLevel((String) jobjectRisk.get(MarketplacewebservicesConstants.EBSRISKLEVEL));
	//			riskResponse.setEbsRiskPercentage((Long) jobjectRisk.get(MarketplacewebservicesConstants.EBSRISKPERCENTAGE));
	//			riskResponse.setEbsPaymentStatus((String) jobjectRisk.get(MarketplacewebservicesConstants.EBSPAYMENTSTATUS));
	//			riskResponse.setFlagged((Boolean) jobjectRisk.get(MarketplacewebservicesConstants.FLAGGED));
	//			riskResponse.setMessage((String) jobjectRisk.get(MarketplacewebservicesConstants.MESSAGE));
	//			riskResponse.setProvider((String) jobjectRisk.get(MarketplacewebservicesConstants.PROVIDER));
	//			riskResponse.setRecommendedAction((String) jobjectRisk.get(MarketplacewebservicesConstants.RECOMMENDEDACTION));
	//			riskResponse.setStatus((String) jobjectRisk.get(MarketplacewebservicesConstants.EBSSTATUS));
	//			// Set Risk Response into orderStatusResponseJuspay
	//			orderStatusResponseJuspay.setRiskResponse(riskResponse);
	//
	//			LOG.debug("getRequestParameterForSard: riskResponse : " + riskResponse);
	//
	//			final JSONObject jobjectCard = (JSONObject) jobjectOrdrRes.get(MarketplacewebservicesConstants.CARD);
	//			// Set values into cardResponse
	//			cardResponse.setCardNumber((String) jobjectCard.get(MarketplacewebservicesConstants.LASTFOURDIGITS));
	//			cardResponse.setCardISIN((String) jobjectCard.get(MarketplacewebservicesConstants.CARDISIN));
	//			cardResponse.setExpiryMonth((String) jobjectCard.get(MarketplacewebservicesConstants.EXPIRYMONTH));
	//			cardResponse.setExpiryYear((String) jobjectCard.get(MarketplacewebservicesConstants.EXPIRYYEAR));
	//			cardResponse.setNameOnCard((String) jobjectCard.get(MarketplacewebservicesConstants.NAMEOFCARD));
	//			cardResponse.setCardType((String) jobjectCard.get(MarketplacewebservicesConstants.CARDTYPE));
	//			cardResponse.setCardBrand((String) jobjectCard.get(MarketplacewebservicesConstants.CARDBRAND));
	//			cardResponse.setCardIssuer((String) jobjectCard.get(MarketplacewebservicesConstants.CARDISSUER));
	//			cardResponse.setCardReference((String) jobjectCard.get(MarketplacewebservicesConstants.CARDREFNO));
	//			cardResponse.setCardFingerprint((String) jobjectCard.get(MarketplacewebservicesConstants.CARDFINGERPRINT));
	//			cardResponse.setUsingSavedCard((Boolean) jobjectCard.get(MarketplacewebservicesConstants.USINGSAVEDCARD));
	//			cardResponse.setSavedToLocker((Boolean) jobjectCard.get(MarketplacewebservicesConstants.SAVEDTOLOCKER));
	//			// Set cardResponce into orderStatusResponseJuspay
	//			orderStatusResponseJuspay.setCardResponse(cardResponse);
	//
	//			LOG.debug("getRequestParameterForSard: cardResponse : " + cardResponse);
	//
	//			// Fetch Details from Json for paymentGatewayResponse
	//			final JSONObject jobjectPayment = (JSONObject) jobjectOrdrRes
	//					.get(MarketplacewebservicesConstants.PAYMENT_GATEWAY_RESPONSE);
	//
	//			// Set values into paymentGatewayResponse
	//			paymentGatewayResponse.setCreated((String) jobjectPayment.get(MarketplacewebservicesConstants.CREATED));
	//			paymentGatewayResponse.setExternalGatewayTxnId((String) jobjectPayment.get(MarketplacewebservicesConstants.EPGTXNID));
	//			paymentGatewayResponse.setRootReferenceNumber((String) jobjectPayment.get(MarketplacewebservicesConstants.RRN));
	//			paymentGatewayResponse.setAuthIdCode((String) jobjectPayment.get(MarketplacewebservicesConstants.AUTHIDCODE));
	//			paymentGatewayResponse.setTxnId((String) jobjectPayment.get(MarketplacewebservicesConstants.TXNID_P));
	//			paymentGatewayResponse.setResponseCode((String) jobjectPayment.get(MarketplacewebservicesConstants.RESPCODE));
	//			paymentGatewayResponse.setResponseMessage((String) jobjectPayment.get(MarketplacewebservicesConstants.RESPMSG));
	//
	//			// Set paymentGatewayResponse into orderStatusResponseJuspay
	//			orderStatusResponseJuspay.setPaymentGatewayResponse(paymentGatewayResponse);
	//			LOG.debug("getRequestParameterForSard: orderStatusResponseJuspay : " + orderStatusResponseJuspay);
	//
	//		}
	//		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
	//		{
	//			throw e;
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	//		}
	//		return orderStatusResponseJuspay;
	//
	//	}

	/**
	 * Update Card Transaction Details
	 *
	 * @param orderStatusResponseJuspay
	 * @param paymentMode
	 * @param cartId
	 * @param userID
	 * @return PaymentServiceWsData
	 * @see com.tisl.mpl.wsDTO.PaymentServiceWsData#updateCardTransactionDetails(com.tisl.mpl.juspay.response.GetOrderStatusResponse,
	 *      java.util.Map, java.lang.String)
	 */
	@Override
	public PaymentServiceWsData updateCardTransactionDetails(final GetOrderStatusResponse orderStatusResponseJuspay,
			final String paymentMode, final String cartId, final String userID) throws EtailNonBusinessExceptions
	{
		final PaymentServiceWsData updateCardDetails = new PaymentServiceWsData();
		try
		{

			// Converted Order Status Response from JSON
			if (null != orderStatusResponseJuspay)
			{

				LOG.debug("updateCardTransactionDetails : paymentMode : " + paymentMode);

				// Validate Payment Mode not null
				if (StringUtils.isNotEmpty(paymentMode) && !paymentMode.contains(MarketplacewebservicesConstants.COD))
				{
					final UserModel user = getExtendedUserService().getUserForOriginalUid(userID);

					LOG.debug("updateCardTransactionDetails : user : " + user);

					if (null != user)
					{
						//getting cartmodel using cart id
						final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);

						LOG.debug("updateCardTransactionDetails : cartModel : " + cartModel);
						if (null != cartModel)
						{
							// Get Payment Mode in Map format
							//							final Map<String, Double> paymentModeJuspay = getPaymentMode(paymentMode);

							final Map<String, Double> paymentModeMap = new HashMap<String, Double>();
							paymentModeMap.put(paymentMode, cartModel.getTotalPriceWithConv());


							//final Map<String, Double> paymentInfo = new HashMap<String, Double>();
							//paymentInfo.put(paymentMode, Double.valueOf(totalPriceAfterConvCharge.getValue().doubleValue()));
							getSessionService().setAttribute(MarketplacewebservicesConstants.PAYMENTMODE, paymentModeMap);


							if (!paymentModeMap.isEmpty())
							{
								// Update Audit Table in respect or order id in order response structure
								getMplPaymentService().updateAuditEntry(orderStatusResponseJuspay);

								// Save payment Transaction Entry
								getMplPaymentService().setPaymentTransaction(orderStatusResponseJuspay, paymentModeMap, cartModel);
								try
								{
									//Logic when transaction is successful i.e. CHARGED
									if (StringUtils.isNotEmpty(orderStatusResponseJuspay.getStatus()))
									{
										LOG.debug("Status of the order from Juspay response is ::::::"
												+ orderStatusResponseJuspay.getStatus());
										if (MarketplacecommerceservicesConstants.CHARGED.equalsIgnoreCase(orderStatusResponseJuspay
												.getStatus()))
										{
											//saving card details
											getMplPaymentService().saveCardDetailsFromJuspay(orderStatusResponseJuspay, paymentModeMap,
													cartModel);
											updateCardDetails.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
										}
										else
										{
											updateCardDetails.setStatus(orderStatusResponseJuspay.getStatus());
										}
									}
									else
									{
										updateCardDetails.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
									}
									getMplPaymentService().paymentModeApportion(cartModel);
								}
								catch (final Exception e)
								{
									// if Update COD Payment debug failure display error Message
									updateCardDetails.setError(MarketplacewebservicesConstants.PAYMENTINFOSAVINFFALIUR);
									throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
								}

							}
							else
							{
								// If the Payment Mode is not COD display error Message
								updateCardDetails.setError(MarketplacewebservicesConstants.MODEOFPAYMENT);
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9053);
							}
						}
						else
						{
							//If  Cart Model is null display error message
							updateCardDetails.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
						}
					}
					else
					{
						//If  User Model is null display error message
						updateCardDetails.setError(MarketplacewebservicesConstants.USEREMPTY);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
					}
				}
			}
		}
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			LOG.error("updateCardTransactionDetails mobile WS " + e.getMessage());
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error("updateCardTransactionDetails mobile WS " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return updateCardDetails;
	}

	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param paymentMode
	 * @param cartID
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param userID
	 * @return PaymentServiceWsData
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public PaymentServiceWsData updateCODTransactionDetails(final String cartID, final String userID)
			throws EtailNonBusinessExceptions
	{
		final PaymentServiceWsData updateCODDitles = new PaymentServiceWsData();
		try
		{
			final UserModel user = getExtendedUserService().getUserForOriginalUid(userID);
			LOG.debug("updateCODTransactionDetails : user : " + user);

			if (null != user)
			{
				//getting cartmodel using cart id and user
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartID, user);
				// Validate Cart Model is not null
				if (null != cartModel)
				{
					getCartService().setSessionCart(cartModel);
					// Get Cart entries
					final List<AbstractOrderEntryModel> entries = cartModel.getEntries();

					// Validate entries is not null
					LOG.debug("updateCODTransactionDetails : Cart Entries : " + entries);
					if (null != entries)
					{
						// Get Payment Mode in Map format
						final Map<String, Double> paymentModeMap = new HashMap<String, Double>();
						try
						{
							paymentModeMap.put(MarketplacewebservicesConstants.COD, cartModel.getTotalPriceWithConv());
							getSessionService().setAttribute(MarketplacewebservicesConstants.PAYMENTMODE, paymentModeMap);
							// If the Payment Mode is COD then only we can proceed else provide error Message
							if (!paymentModeMap.isEmpty())
							{
								//Get The Sting into Json Object
								//								final JSONObject recPaymode = (JSONObject) JSONValue.parse(paymentMode);

								//								LOG.debug("updateCODTransactionDetails : recPaymode JSON : " + recPaymode);

								//								final String cod = recPaymode.get(MarketplacewebservicesConstants.COD) != null ? recPaymode.get(
								//										MarketplacewebservicesConstants.COD).toString() : MarketplacewebservicesConstants.DECIMALULLCHK;

								//								LOG.debug("updateCODTransactionDetails : recPaymode JSON Return Value : " + cod);

								//								final Double cod_amt = new Double(cod);

								//								if (cod != MarketplacewebservicesConstants.DECIMALULLCHK)
								//								{

								//								}
								// Save payment Transaction Entry
								getMplPaymentService().setPaymentTransactionForCOD(paymentModeMap, cartModel);
								try
								{
									// Update COD Payment Info

									final Double cartValue = cartModel.getSubtotal();
									final Double totalCODCharge = cartModel.getConvenienceCharges();
									String userName = null;
									if (null != user.getName())
									{
										userName = user.getName();
									}
									else
									{
										userName = userID;
									}
									LOG.debug(String.format(
											"updateCODTransactionDetails: cartValue: %s | totalCODCharge: %s | Name : %s |", cartValue,
											totalCODCharge, userName));

									//CartModel parameters added as session cart is loading the CartModel
									mplPaymentService.saveCODPaymentInfo(userName, cartValue, totalCODCharge, entries, cartModel);

									updateCODDitles.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
								}
								catch (final EtailNonBusinessExceptions e)
								{
									// if Update COD Payment Info failure display error Message
									updateCODDitles.setError(MarketplacewebservicesConstants.PAYMENTINFOSAVINFFALIUR);
									throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
								}
							}
							else
							{
								// If the Payment Mode is not COD display error Message
								updateCODDitles.setError(MarketplacewebservicesConstants.MODEOFPAYMENT);
							}
						}
						catch (final Exception e)
						{
							// Set payment Transaction failure display error message
							updateCODDitles.setError(MarketplacewebservicesConstants.PAYMENTSAVEFALIUR);
						}
					}
					else
					{
						//entries is null display error message
						updateCODDitles.setError(MarketplacewebservicesConstants.ENTRYEMPTY);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9054);
					}
				}
				else
				{
					//If  Cart Model is null display error message
					updateCODDitles.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				//If  User Model is null display error message
				updateCODDitles.setError(MarketplacewebservicesConstants.USEREMPTY);
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

		return updateCODDitles;
	}


	/**
	 * @description Double Check
	 * @param inputObject
	 * @return Double
	 */
	protected Double getDoubleValue(final Object inputObject)
	{
		if (inputObject instanceof Long)
		{
			return new Double(((Long) inputObject).doubleValue());
		}
		else if (inputObject instanceof Double)
		{
			return ((Double) inputObject);
		}
		else
		{
			LOG.error(MarketplacewebservicesConstants.DECIMALERROR);
			return null;
		}
	}

	/**
	 * @description Get Billing Address for Card Reference Number
	 * @param originalUid
	 * @param cardRefNo
	 * @return BillingAddressWsData
	 */
	@Override
	public BillingAddressWsData getBillingAddress(final String originalUid, final String cardRefNo)
			throws EtailNonBusinessExceptions
	{
		final BillingAddressWsData billingAddress = new BillingAddressWsData();
		try
		{
			// Get Card Model from Db using originalUid and cardRefNo
			final SavedCardModel cardModel = getMplPaymentWebDAO().getBillingAddress(originalUid, cardRefNo);

			// validate cardModel is not null and cardModel.getBillingAddress() is not null
			if (null != cardModel && null != cardModel.getBillingAddress())
			{
				// Validate null check and set values into BillingAddressWsData
				billingAddress.setFirstName(cardModel.getBillingAddress().getFirstname() != null ? cardModel.getBillingAddress()
						.getFirstname() : MarketplacewebservicesConstants.NA);
				billingAddress.setLastName(cardModel.getBillingAddress().getLastname() != null ? cardModel.getBillingAddress()
						.getLastname() : MarketplacewebservicesConstants.NA);
				billingAddress.setAddressLine1(cardModel.getBillingAddress().getLine1() != null ? cardModel.getBillingAddress()
						.getLine1() : MarketplacewebservicesConstants.NA);
				billingAddress.setAddressLine2(cardModel.getBillingAddress().getLine2() != null ? cardModel.getBillingAddress()
						.getLine2() : MarketplacewebservicesConstants.NA);
				billingAddress.setAddressLine3(cardModel.getBillingAddress().getAddressLine3() != null ? cardModel
						.getBillingAddress().getAddressLine3() : MarketplacewebservicesConstants.NA);
				billingAddress.setPostalcode(cardModel.getBillingAddress().getPostalcode() != null ? cardModel.getBillingAddress()
						.getPostalcode() : MarketplacewebservicesConstants.NA);
				billingAddress.setState(cardModel.getBillingAddress().getDistrict() != null ? cardModel.getBillingAddress()
						.getDistrict() : MarketplacewebservicesConstants.NA);
				billingAddress.setTown(cardModel.getBillingAddress().getTown() != null ? cardModel.getBillingAddress().getTown()
						: MarketplacewebservicesConstants.NA);
				billingAddress.setCountry(cardModel.getBillingAddress().getCountry().getName() != null ? cardModel
						.getBillingAddress().getCountry().getName() : MarketplacewebservicesConstants.NA);
				billingAddress.setShippingFlag(cardModel.getBillingAddress().getShippingAddress());
			}
			else
			{
				// if cardModel is null or cardModel.getBillingAddress() is null display error Message
				billingAddress.setError(MarketplacewebservicesConstants.NOBILLINGDETAILS);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
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
		return billingAddress;
	}


	/**
	 * @Description Update Transaction and related Details for COD
	 * @param juspayOrderId
	 * @param channel
	 * @param cartId
	 * @param userId
	 * @return MplUserResultWsDto
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplUserResultWsDto createEntryInAudit(final String juspayOrderId, final String channel, final String cartId,
			final String userId) throws EtailNonBusinessExceptions
	{
		final MplUserResultWsDto auditEntry = new MplUserResultWsDto();
		try
		{
			//final CartModel cartModel = mplPaymentWebDAO.findCartValues(cartID);
			//getting cartmodel using cart id
			final UserModel user = getExtendedUserService().getUserForOriginalUid(userId);

			LOG.debug("createEntryInAudit : user : " + user);

			if (null != user)
			{
				//getting cartmodel using cart id
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);
				if (null != cartModel)
				{
					getMplPaymentService().createEntryInAudit(juspayOrderId, channel, cartModel.getGuid());
					auditEntry.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
				}
				else
				{
					auditEntry.setError(MarketplacewebservicesConstants.NOBILLINGDETAILS);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				//If  User Model is null display error message
				auditEntry.setError(MarketplacewebservicesConstants.USEREMPTY);
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
		return auditEntry;
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
	@Override
	public MplPromoPriceWsDTO validateBinNumber(final String binNo, final String paymentMode, final String bankName)
	{
		final MplPromoPriceWsDTO promoPriceData = new MplPromoPriceWsDTO(); //The New Returning DTO
		boolean toProceedFlag = false;
		boolean toProceedWithNoBinFlag = false;
		final BinData binData = new BinData();
		final String ebsDowntime = getConfigurationService().getConfiguration().getString("payment.ebs.downtime");
		getSessionService().setAttribute(MarketplacewebservicesConstants.PAYMENTMODEFORPROMOTION, paymentMode);
		BinModel bin = new BinModel();

		LOG.debug("validateBinNumber : ebsDowntime :  " + ebsDowntime);

		try
		{ // Get Bank Model for Saved , New Card and EMI
			if (StringUtils.isNotEmpty(binNo) && StringUtils.isEmpty(bankName)
					&& !paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
			{
				//calling facade method to perform BIN check
				bin = getBinService().checkBin(binNo);
				// EBS DownTime Y
				if (StringUtils.isNotEmpty(ebsDowntime) && ebsDowntime.equalsIgnoreCase("Y"))
				{
					// BinModel is not null
					if (null != bin)
					{
						binData.setBankName(bin.getBank().getBankName());
						if (StringUtils.isNotEmpty(bin.getIssuingCountry()) && bin.getIssuingCountry().equalsIgnoreCase("India"))
						{
							binData.setIsValid(true);
							toProceedFlag = true;
							promoPriceData.setIsDomestic(Boolean.TRUE);
						}
						else
						{
							binData.setIsValid(false);
							promoPriceData.setIsDomestic(Boolean.FALSE);
							promoPriceData.setError(MarketplacewebservicesConstants.EBSDOWNINTERNATIONAL);
						}
						if (null != bin.getBank() && null != bin.getBank().getBankName())
						{
							promoPriceData.setBankName(bin.getBank().getBankName());
						}
					}
					//	EBS DownTime N
					else
					{
						promoPriceData.setBankName("");
						binData.setIsValid(false);
						promoPriceData.setIsDomestic(Boolean.TRUE);
						promoPriceData.setError(MarketplacewebservicesConstants.EBSDOWNNOBINRY);
					}
					promoPriceData.setEbsDown(Boolean.TRUE);
				}
				else
				{
					if (null != bin)
					{
						promoPriceData.setBankName(bin.getBank().getBankName());
						binData.setIsValid(true);
						toProceedWithNoBinFlag = true;
						toProceedFlag = true;
						if (null != bin.getBank() && null != bin.getBank().getBankName())
						{
							promoPriceData.setBankName(bin.getBank().getBankName());
						}
					}
					else
					{
						promoPriceData.setBankName("");
						binData.setIsValid(true);
						toProceedWithNoBinFlag = true;
					}
					promoPriceData.setEbsDown(Boolean.FALSE);

				}

				if (null != bin && null != bin.getBank() && toProceedFlag)
				{
					//setting the bank in session to be used for Promotion
					getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, bin.getBank());
					promoPriceData.setBinCheck(Boolean.TRUE);
				}
				else
				{
					if (toProceedWithNoBinFlag)
					{
						//setting the bank in session to be used for Promotion
						getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, null);
						promoPriceData.setBinCheck(Boolean.TRUE);
					}

				}
				if (null != bin && null != bin.getCardType())
				{
					promoPriceData.setCardType(bin.getCardType());
				}
				else
				{
					promoPriceData.setCardType(" ");
				}
			}
			/*
			 * // Get Bank Model for Saved Card else if (null != bankName && null == binNo &&
			 * !paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD)) { bin = getBinService().checkBin(binNo);
			 * getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, bin.getBank()); validFlag =
			 * true; }
			 */
			// Set Bank in Session as null for COD
			else if (StringUtils.isEmpty(binNo) && StringUtils.isEmpty(bankName)
					&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.COD))
			{
				getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, null);
				promoPriceData.setBinCheck(Boolean.TRUE);
			}
			// Set Bank in Session as bankName for Net Banking
			else if (StringUtils.isEmpty(binNo) && StringUtils.isNotEmpty(bankName)
					&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.NETBANKING))
			{
				final BankModel bankModel = getMplPaymentWebDAO().savedCardBankFromBin(bankName);
				getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, bankModel);
				promoPriceData.setBinCheck(Boolean.TRUE);
			}
			else
			{
				promoPriceData.setError(MarketplacewebservicesConstants.NOPROPERDATA);
				promoPriceData.setBinCheck(Boolean.FALSE);
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
	 * This method returns potential promotion on paymeny mode
	 *
	 * @param cartId
	 * @return PaymentServiceWsData
	 *
	 */
	@Override
	public PaymentServiceWsData potentialPromotionOnPaymentMode(final String userId, final String cartId)
	{
		PaymentServiceWsData promoData = new PaymentServiceWsData();
		try
		{
			//fetch usermodel against customer
			final UserModel user = getExtendedUserService().getUserForOriginalUid(userId);
			// Check userModel null
			if (null != user)
			{
				// Type Cast User Model to Address Model

				LOG.debug(String.format("potentialPromotionOnPaymentMode: | userId: %s | cartId : %s  ", userId, cartId));

				//getting cartmodel using cart id and user
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);
				// Validate Cart Model is not null
				if (null != cartModel)
				{
					LOG.debug(String.format("potentialPromotionOnPaymentMode: | cartModel.getCode().: %s  ", cartModel.getCode()));

					promoData = promotionsInPaymentMode(cartModel);

				}
				else
				{
					//If  Cart Model is null display error message
					promoData.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				//If  User Model is null display error message
				promoData.setError(MarketplacewebservicesConstants.USEREMPTY);
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

		return promoData;
	}

	/**
	 *
	 * @param cartModel
	 * @return PaymentServiceWsData
	 */

	private PaymentServiceWsData promotionsInPaymentMode(final CartModel cartModel)
	{
		final PaymentServiceWsData promoData = new PaymentServiceWsData();
		final List<PotentialRestrictionData> potentialRestrictionDataList = new ArrayList<PotentialRestrictionData>();
		PotentialRestrictionData potentialProductPromo = new PotentialRestrictionData();
		PotentialRestrictionData potentialOrderPromo = new PotentialRestrictionData();

		// To get individual Entries level promotion in a cart
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			// Collection of Entry Level Promotion
			final Collection<ProductPromotionModel> productPromotionColl = entry.getProduct().getPromotions();
			// Convert Collection into List
			final List<ProductPromotionModel> productPromotionList = new ArrayList<ProductPromotionModel>();
			productPromotionList.addAll(productPromotionColl);
			if (CollectionUtils.isNotEmpty(productPromotionList))
			{
				// Iterate individual Promotion
				for (final ProductPromotionModel prodPromo : productPromotionList)
				{
					LOG.debug(String.format(
							"promotionsInPaymentMode: | prodPromo.getDescription(): %s | prodPromo.getEnabled(): %s   ",
							prodPromo.getDescription(), prodPromo.getEnabled()));

					// Get Potential Promotions
					potentialProductPromo = productAndCartpromotion(prodPromo, null);
					if (null != potentialProductPromo.getPaymentModePromotion() && null != potentialProductPromo.getPromoDesc())
					{
						// Add promotion details ito Data List
						potentialRestrictionDataList.add(potentialProductPromo);
					}
				}
			}
		}

		// Get Enabled OrderLevel Promotions from  OrderPromotionModel
		final List<OrderPromotionModel> orderPromotion = getMplPaymentWebDAO().orderPromotions();
		if (CollectionUtils.isNotEmpty(orderPromotion))
		{
			// Iterate individual Promotion
			for (final OrderPromotionModel orderPromo : orderPromotion)
			{
				LOG.debug(String.format(
						"promotionsInPaymentMode: | orderPromo.getDescription(): %s | orderPromo.getEnabled(): %s   ",
						orderPromo.getDescription(), orderPromo.getEnabled()));

				// Get Potential Promotions
				potentialOrderPromo = productAndCartpromotion(null, orderPromo);
				if (null != potentialOrderPromo.getPaymentModePromotion() && null != potentialOrderPromo.getPromoDesc())
				{
					// Add promotion details ito Data List
					potentialRestrictionDataList.add(potentialOrderPromo);
				}
			}
		}

		// ADD the List of Payment Mode restriction Product and Order Lavel Potential Promotion List into PaymentServiceWsData
		promoData.setListofPotentialRestriction(potentialRestrictionDataList);

		return promoData;
	}




	/**
	 *
	 * @param prodPromo
	 * @param orderPromo
	 * @return
	 */
	private PotentialRestrictionData productAndCartpromotion(final ProductPromotionModel prodPromo,
			final OrderPromotionModel orderPromo)
	{
		PotentialRestrictionData potentialPromotionForPayment = new PotentialRestrictionData();
		// For Product Level Promotion
		if (null != prodPromo && null == orderPromo)
		{
			final java.util.Date endDate = prodPromo.getEndDate();

			LOG.debug(String.format("productAndCartpromotion: | prodPromo.getEndDate(): %s   ", endDate));

			// Validate if Promotion is enable and not expired
			if (prodPromo.getEnabled().booleanValue() && !endDate.before(new Date()))
			{
				final List<SalesApplication> salesChannel = prodPromo.getChannel();
				LOG.debug(String.format("productAndCartpromotion: | prodPromo--salesChannel: %s   ", salesChannel));
				// If No channel or channel is Mobile then only provide promotion details
				if (salesChannel.isEmpty())
				{
					// Get Payment Mode Restriction Promotion Details
					potentialPromotionForPayment = productWithPaymentResPromotion(prodPromo);
				}
				else
				{
					LOG.debug("productAndCartpromotion: | prodPromo--salesChannel not null");
					for (final SalesApplication salesChnl : salesChannel)
					{
						if (salesChnl.equals(SalesApplication.MOBILE) || salesChannel.isEmpty())
						{
							// Get Payment Mode Restriction Promotion Details
							potentialPromotionForPayment = productWithPaymentResPromotion(prodPromo);
						}
					}
				}
			}
		}
		// For Order Level Promotion
		else if (null != orderPromo && null == prodPromo)
		{
			final java.util.Date endDate = orderPromo.getEndDate();
			LOG.debug(String.format("productAndCartpromotion: | orderPromo.getEndDate(): %s   ", endDate));

			// Validate if Promotion is enable and not expired
			if (orderPromo.getEnabled().booleanValue() && !endDate.before(new Date()))
			{
				final List<SalesApplication> salesChannel = orderPromo.getChannel();
				LOG.debug(String.format("productAndCartpromotion: | orderPromo--salesChannel: %s   ", salesChannel));

				// If No channel or channel is Mobile then only provide promotion details
				if (salesChannel.isEmpty())
				{
					// Get Payment Mode Restriction Promotion Details
					potentialPromotionForPayment = orderWithPaymentResPromotion(orderPromo);
				}
				else
				{
					LOG.debug("productAndCartpromotion: | orderPromo--salesChannel not null");

					for (final SalesApplication salesChnl : salesChannel)
					{
						if (salesChnl.equals(SalesApplication.MOBILE) || salesChannel.isEmpty())
						{
							// Get Payment Mode Restriction Promotion Details
							potentialPromotionForPayment = orderWithPaymentResPromotion(orderPromo);
						}
					}
				}
			}
		}
		return potentialPromotionForPayment;
	}

	/**
	 *
	 * @param prodPromo
	 * @return PotentialRestrictionData
	 */
	private PotentialRestrictionData productWithPaymentResPromotion(final ProductPromotionModel prodPromo)
	{
		final PotentialRestrictionData potentialPromotionForPayment = new PotentialRestrictionData();
		for (final AbstractPromotionRestrictionModel restriction : prodPromo.getRestrictions())
		{
			LOG.debug("productWithPaymentResPromotion: | prodPromo.getRestrictions(): " + restriction);
			if (restriction instanceof PaymentModeSpecificPromotionRestrictionModel)
			{
				LOG.debug("productWithPaymentResPromotion: | prodPromo-- Payment Mode Restriction ");

				final List<String> paymentmodes = new ArrayList<String>();
				for (final PaymentTypeModel paymentType : ((PaymentModeSpecificPromotionRestrictionModel) restriction)
						.getPaymentModes())
				{
					LOG.debug("productWithPaymentResPromotion: | prodPromo-- paymentType.getMode() " + paymentType.getMode());
					LOG.debug("productWithPaymentResPromotion: | prodPromo-- prodPromo.getDescription() " + prodPromo.getDescription());
					paymentmodes.add(paymentType.getMode());
				}

				potentialPromotionForPayment.setPaymentModePromotion(paymentmodes);
				potentialPromotionForPayment.setPromoDesc(prodPromo.getDescription());
			}
		}
		return potentialPromotionForPayment;
	}


	/**
	 *
	 * @param orderPromo
	 * @return PotentialRestrictionData
	 */
	private PotentialRestrictionData orderWithPaymentResPromotion(final OrderPromotionModel orderPromo)
	{
		final PotentialRestrictionData potentialPromotionForPayment = new PotentialRestrictionData();
		for (final AbstractPromotionRestrictionModel restriction : orderPromo.getRestrictions())
		{
			LOG.debug("orderWithPaymentResPromotion: | orderPromo.getRestrictions(): " + restriction);
			if (restriction instanceof PaymentModeSpecificPromotionRestrictionModel)
			{
				LOG.debug("orderWithPaymentResPromotion: | orderPromo-- Payment Mode Restriction ");

				final List<String> paymentmodes = new ArrayList<String>();
				for (final PaymentTypeModel paymentType : ((PaymentModeSpecificPromotionRestrictionModel) restriction)
						.getPaymentModes())
				{
					LOG.debug("orderWithPaymentResPromotion: | orderPromo-- paymentType.getMode() " + paymentType.getMode());
					LOG.debug("orderWithPaymentResPromotion: | orderPromo-- prodPromo.getDescription() " + orderPromo.getDescription());
					paymentmodes.add(paymentType.getMode());
				}

				potentialPromotionForPayment.setPaymentModePromotion(paymentmodes);
				potentialPromotionForPayment.setPromoDesc(orderPromo.getDescription());
			}
		}
		return potentialPromotionForPayment;
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
		final CustomerModel customer = getMplPaymentWebDAO().getCustomer(userId);
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
		final CartModel cart = getMplPaymentWebDAO().findCartValues(cartID);
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
		final CartModel cart = mplPaymentWebDAO.findCartValuesAnonymous(guid);
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
	 * @return the i18NService
	 */
	public I18NService getI18NService()
	{
		return i18NService;
	}

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
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
	 * @return the mplPaymentWebDAO
	 */
	public MplPaymentWebDAO getMplPaymentWebDAO()
	{
		return mplPaymentWebDAO;
	}

	/**
	 * @param mplPaymentWebDAO
	 *           the mplPaymentWebDAO to set
	 */
	public void setMplPaymentWebDAO(final MplPaymentWebDAO mplPaymentWebDAO)
	{
		this.mplPaymentWebDAO = mplPaymentWebDAO;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
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
	 *
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
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}



	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
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





}
