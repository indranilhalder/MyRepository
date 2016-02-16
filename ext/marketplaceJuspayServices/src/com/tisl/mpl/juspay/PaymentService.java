package com.tisl.mpl.juspay;

import de.hybris.platform.core.Registry;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.tisl.mpl.juspay.constants.MarketplaceJuspayServicesConstants;
import com.tisl.mpl.juspay.model.Promotion;
import com.tisl.mpl.juspay.request.AddCardRequest;
import com.tisl.mpl.juspay.request.CreatePromotionRequest;
import com.tisl.mpl.juspay.request.DeactivatePromotionRequest;
import com.tisl.mpl.juspay.request.DeleteCardRequest;
import com.tisl.mpl.juspay.request.GetCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.request.InitOrderRequest;
import com.tisl.mpl.juspay.request.ListCardsRequest;
import com.tisl.mpl.juspay.request.NetbankingRequest;
import com.tisl.mpl.juspay.request.PaymentRequest;
import com.tisl.mpl.juspay.request.RefundRequest;
import com.tisl.mpl.juspay.response.AddCardResponse;
import com.tisl.mpl.juspay.response.CardResponse;
import com.tisl.mpl.juspay.response.CreatePromotionResponse;
import com.tisl.mpl.juspay.response.DeactivatePromotionResponse;
import com.tisl.mpl.juspay.response.DeleteCardResponse;
import com.tisl.mpl.juspay.response.GetCardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.InitOrderResponse;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.juspay.response.PaymentGatewayResponse;
import com.tisl.mpl.juspay.response.PaymentResponse;
import com.tisl.mpl.juspay.response.RefundResponse;
import com.tisl.mpl.juspay.response.RiskResponse;
import com.tisl.mpl.juspay.response.StoredCard;
import com.tisl.mpl.juspay.util.ISO8601DateParser;


public class PaymentService
{

	private static final Logger LOG = Logger.getLogger(PaymentService.class);

	private int connectionTimeout = 5 * 10000;
	private int readTimeout = 5 * 1000;
	//private static final Logger log = Logger.getLogger(PaymentService.class);
	private String baseUrl;
	private String key;
	private String merchantId;
	private Environment environment;
	private String environmentSet;


	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the environmentSet
	 */
	public String getEnvironmentSet()
	{
		return environmentSet;
	}

	/**
	 * @param environmentSet
	 *           the environmentSet to set
	 */
	public void setEnvironmentSet(final String environmentSet)
	{
		this.environmentSet = environmentSet;
	}


	/**
	 * @return the merchantId
	 */
	public String getMerchantId()
	{
		return merchantId;
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment()
	{
		return environment;
	}

	public void setKey(final String key)
	{
		this.key = key;
	}

	public PaymentService withKey(final String key)
	{
		this.key = key;
		return this;
	}

	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}

	public PaymentService withMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
		return this;
	}

	public void setEnvironment(final Environment environment)
	{
		this.environment = environment;
		this.baseUrl = environment.getBaseUrl();
	}

	public PaymentService withEnvironment(final Environment environment)
	{
		this.environment = environment;
		this.baseUrl = environment.getBaseUrl();
		return this;
	}

	private String serializeParams(final Map<String, String> parameters)
	{

		final StringBuilder bufferUrl = new StringBuilder();
		try
		{
			for (final Map.Entry<String, String> entry : parameters.entrySet())
			{
				bufferUrl.append(entry.getKey());
				bufferUrl.append('=');
				bufferUrl.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				bufferUrl.append('&');
			}
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.info("Encoding exception while trying to construct payload", e);
		}
		return bufferUrl.toString();
	}

	/**
	 * It opens the connection to the given endPoint and returns the http response as String.
	 *
	 * @param endPoint
	 *           - The HTTP URL of the request
	 * @return HTTP response as string
	 */
	private String makeServiceCall(final String endPoint, final String encodedParams)
	{

		final String proxyEnableStatus = getConfigurationService().getConfiguration()
				.getString(MarketplaceJuspayServicesConstants.PROXYENABLED);
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();

		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				final String proxyName = getConfigurationService().getConfiguration()
						.getString(MarketplaceJuspayServicesConstants.GENPROXY);
				final int proxyPort = Integer.parseInt(
						getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.GENPROXYPORT));
				final SocketAddress addr = new InetSocketAddress(proxyName, proxyPort);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			else
			{
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection();
			}

			String encodedKey = new String(Base64.encodeBase64(this.key.getBytes()));
			encodedKey = encodedKey.replaceAll("\n", "");
			connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("version",
					getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(encodedParams);
			wr.flush();
			wr.close();

			// Read the response
			final InputStream inputStream = connection.getInputStream();
			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
			return buffer.toString();
		}
		catch (final Exception e)
		{
			throw new AdapterException("Error with connection", e);
		}
	}

	/**
	 * Creates a new order and returns the InitOrderResponse associated with that.
	 *
	 * @param initOrderRequest
	 *           - InitOrderRequest with all required params
	 * @return InitOrderResponse for the given request
	 * @throws AdapterException
	 */
	public InitOrderResponse initOrder(final InitOrderRequest initOrderRequest) throws AdapterException
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.AMOUNT, String.valueOf(initOrderRequest.getAmount()));
		params.put(MarketplaceJuspayServicesConstants.CUSTOMERID, initOrderRequest.getCustomerId());
		params.put("customer_email", initOrderRequest.getCustomerEmail());
		params.put(MarketplaceJuspayServicesConstants.ORDERID, initOrderRequest.getOrderId());

		// Optional parameters
		params.put("udf1", initOrderRequest.getUdf1() == null ? "" : initOrderRequest.getUdf1());
		params.put("udf2", initOrderRequest.getUdf2() == null ? "" : initOrderRequest.getUdf2());
		params.put("udf3", initOrderRequest.getUdf3() == null ? "" : initOrderRequest.getUdf3());
		params.put("udf4", initOrderRequest.getUdf4() == null ? "" : initOrderRequest.getUdf4());
		params.put("udf5", initOrderRequest.getUdf5() == null ? "" : initOrderRequest.getUdf5());
		params.put("udf6", initOrderRequest.getUdf6() == null ? "" : initOrderRequest.getUdf6());
		params.put("udf7", initOrderRequest.getUdf7() == null ? "" : initOrderRequest.getUdf7());
		params.put("udf8", initOrderRequest.getUdf8() == null ? "" : initOrderRequest.getUdf8());
		params.put("udf9", initOrderRequest.getUdf9() == null ? "" : initOrderRequest.getUdf9());
		params.put("udf10", initOrderRequest.getUdf10() == null ? "" : initOrderRequest.getUdf10());
		params.put("return_url", initOrderRequest.getReturnUrl() == null ? "" : initOrderRequest.getReturnUrl());
		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/init_order";

		//log.info("Sending init_order to " + url);
		//log.debug("Payload (init_order): " + serializedParams);

		//To store the request Payload in Audit
		initOrderRequest.setRequestPayload(serializedParams);

		final String response = makeServiceCall(url, serializedParams);

		//log.debug("Init order response received: " + response);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final String merchantId = (String) jsonResponse.get("merchant_id");
		final Long statusId = (Long) jsonResponse.get("status_id");
		final String status = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.STATUS);
		final String orderId = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.ORDERID);

		final InitOrderResponse initOrderResponse = new InitOrderResponse();
		initOrderResponse.setStatus(status);
		initOrderResponse.setOrderId(orderId);
		initOrderResponse.setStatusId(statusId.longValue());
		initOrderResponse.setMerchantId(merchantId);

		return initOrderResponse;
	}

	/**
	 * Returns the card details associated with a token. The token information is encapsulated in the GetCardRequest
	 * instance. This method will throw an Exception if the call is made with an API Key that does not have the required
	 * privileges. If the token is invalid and the API Key is privileged, then the card related fields in the response
	 * would be null.
	 *
	 * @throws Exception
	 */
	public GetCardResponse getCard(final GetCardRequest getCardRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.CARDTOKEN, getCardRequest.getCardToken());

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/card/get";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final GetCardResponse cardResponse = new GetCardResponse();
		final StoredCard card = new StoredCard();
		if (null != jsonResponse.get(MarketplaceJuspayServicesConstants.CARDNUMBER))
		{
			card.withCardToken((String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDTOKEN))
					.withCardReference((String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDREF))
					.withCardNumber((String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDNUMBER))
					.withCardExpYear((String) jsonResponse.get("card_exp_year"))
					.withCardExpMonth((String) jsonResponse.get("card_exp_month")).withCardIsin((String) jsonResponse.get("card_isin"))
					.withCardBrand((String) jsonResponse.get("card_brand")).withCardIssuer((String) jsonResponse.get("card_issuer"))
					.withCardType((String) jsonResponse.get("card_type")).withNickname((String) jsonResponse.get("nickname"))
					.withNameOnCard((String) jsonResponse.get(MarketplaceJuspayServicesConstants.NAMEONCARD));
			cardResponse.setCard(card);
		}
		cardResponse.setCardToken((String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDTOKEN));
		cardResponse.setMerchantId((String) jsonResponse.get(MarketplaceJuspayServicesConstants.MERCHANTID));
		return cardResponse;
	}


	/**
	 * This method fetches the orderStatusResponse for one Juspay Order Id
	 *
	 * @param orderStatusRequest
	 * @return GetOrderStatusResponse
	 * @throws AdapterException
	 */
	public GetOrderStatusResponse getOrderStatus(final GetOrderStatusRequest orderStatusRequest) throws AdapterException
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.ORDERID, orderStatusRequest.getOrderId());

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/order_status";

		final String response = makeServiceCall(url, serializedParams);

		LOG.debug("Response from juspay::::::::::::::::::::::::::::" + response);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		return assembleOrderStatusResponse(jsonResponse, new GetOrderStatusResponse());
	}

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
			LOG.info("Can't seem to understand the input");
			return null;
		}
	}

	protected GetOrderStatusResponse assembleOrderStatusResponse(final JSONObject jsonResponse,
			final GetOrderStatusResponse target)
	{
		final GetOrderStatusResponse orderStatusResponse = target;
		orderStatusResponse.setMerchantId((String) jsonResponse.get("merchant_id"));
		orderStatusResponse.setOrderId((String) jsonResponse.get(MarketplaceJuspayServicesConstants.ORDERID));
		orderStatusResponse.setStatus((String) jsonResponse.get(MarketplaceJuspayServicesConstants.STATUS));
		orderStatusResponse.setStatusId((Long) jsonResponse.get("status_id"));
		orderStatusResponse.setTxnId((String) jsonResponse.get("txn_id"));
		orderStatusResponse.setGatewayId((Long) jsonResponse.get("gateway_id"));

		orderStatusResponse.setAmount(getDoubleValue(jsonResponse.get(MarketplaceJuspayServicesConstants.AMOUNT)));
		orderStatusResponse.setBankErrorCode((String) jsonResponse.get("bank_error_code"));
		orderStatusResponse.setBankErrorMessage((String) jsonResponse.get("bank_error_message"));

		orderStatusResponse.setUdf1((String) jsonResponse.get("udf1"));
		orderStatusResponse.setUdf2((String) jsonResponse.get("udf2"));
		orderStatusResponse.setUdf3((String) jsonResponse.get("udf3"));
		orderStatusResponse.setUdf4((String) jsonResponse.get("udf4"));
		orderStatusResponse.setUdf5((String) jsonResponse.get("udf5"));
		orderStatusResponse.setUdf6((String) jsonResponse.get("udf6"));
		orderStatusResponse.setUdf7((String) jsonResponse.get("udf7"));
		orderStatusResponse.setUdf8((String) jsonResponse.get("udf8"));
		orderStatusResponse.setUdf9((String) jsonResponse.get("udf9"));
		orderStatusResponse.setUdf10((String) jsonResponse.get("udf10"));

		orderStatusResponse.setAmountRefunded(getDoubleValue(jsonResponse.get("amount_refunded")));
		orderStatusResponse.setRefunded((Boolean) jsonResponse.get("refunded"));

		final JSONObject gatewayResponse = (JSONObject) jsonResponse.get("payment_gateway_response");
		final JSONObject card = (JSONObject) jsonResponse.get("card");

		if (card != null)
		{
			final CardResponse cardResponse = new CardResponse();
			cardResponse.setCardNumber((String) card.get("last_four_digits"));
			cardResponse.setCardISIN((String) card.get("card_isin"));
			cardResponse.setExpiryMonth((String) card.get("expiry_month"));
			cardResponse.setExpiryYear((String) card.get("expiry_year"));
			cardResponse.setNameOnCard((String) card.get(MarketplaceJuspayServicesConstants.NAMEONCARD));
			cardResponse.setCardType((String) card.get("card_type"));
			cardResponse.setCardIssuer((String) card.get("card_issuer"));
			cardResponse.setCardBrand((String) card.get("card_brand"));
			cardResponse.setCardReference((String) card.get(MarketplaceJuspayServicesConstants.CARDREF));
			cardResponse.setCardFingerprint((String) card.get("card_fingerprint"));
			cardResponse.setUsingSavedCard((Boolean) card.get("using_saved_card"));
			cardResponse.setSavedToLocker((Boolean) card.get("saved_to_locker"));

			orderStatusResponse.setCardResponse(cardResponse);
		}

		if (gatewayResponse != null)
		{
			final PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();
			paymentGatewayResponse.setRootReferenceNumber((String) gatewayResponse.get("rrn"));
			paymentGatewayResponse.setResponseCode((String) gatewayResponse.get("resp_code"));
			paymentGatewayResponse.setResponseMessage((String) gatewayResponse.get("resp_message"));
			paymentGatewayResponse.setTxnId((String) gatewayResponse.get("txn_id"));
			paymentGatewayResponse.setExternalGatewayTxnId((String) gatewayResponse.get("epg_txn_id"));
			paymentGatewayResponse.setAuthIdCode((String) gatewayResponse.get("auth_id_code"));
			orderStatusResponse.setPaymentGatewayResponse(paymentGatewayResponse);
		}


		final JSONObject promotionResponse = (JSONObject) jsonResponse.get("promotion");
		if (promotionResponse != null)
		{
			final Promotion promotion = assemblePromotionObjectFromJSON(promotionResponse);
			orderStatusResponse.setPromotion(promotion);
		}

		final JSONArray refunds = (JSONArray) jsonResponse.get("refunds");
		if (refunds != null && refunds.size() > 0)
		{
			final List<Refund> refundList = new ArrayList<Refund>(refunds.size());
			for (final Iterator refundIter = refunds.iterator(); refundIter.hasNext();)
			{
				final JSONObject refundEntry = (JSONObject) refundIter.next();
				final Refund refund = new Refund();
				refund.setId((String) refundEntry.get("id"));
				refund.setReference((String) refundEntry.get("ref"));
				refund.setAmount(getDoubleValue(refundEntry.get(MarketplaceJuspayServicesConstants.AMOUNT)));
				refund.setUniqueRequestId((String) refundEntry.get("unique_request_id"));
				refund.setStatus((String) refundEntry.get(MarketplaceJuspayServicesConstants.STATUS));
				try
				{
					refund.setCreated(ISO8601DateParser.parse((String) refundEntry.get("created")));
				}
				catch (final ParseException e)
				{
					LOG.error("Exception while trying to parse date created. Skipping the field", e);
				}
				refundList.add(refund);
			}
			orderStatusResponse.setRefunds(refundList);
		}

		final JSONObject risk = (JSONObject) jsonResponse.get("risk");
		//TODO: change after risk response from Juspay is finalized
		if (null != risk)
		{
			final RiskResponse riskResponse = new RiskResponse();
			riskResponse.setEbsBinCountry((String) risk.get("ebs_bin_country"));
			riskResponse.setEbsRiskLevel((String) risk.get("ebs_risk_level"));
			riskResponse.setEbsRiskPercentage((Long) risk.get("ebs_risk_percentage"));
			riskResponse.setEbsPaymentStatus((String) risk.get("ebs_payment_status"));
			riskResponse.setFlagged((Boolean) risk.get("flagged"));
			riskResponse.setMessage((String) risk.get("message"));
			riskResponse.setProvider((String) risk.get("provider"));
			riskResponse.setRecommendedAction((String) risk.get("recommended_action"));
			riskResponse.setStatus((String) risk.get(MarketplaceJuspayServicesConstants.STATUS));
			orderStatusResponse.setRiskResponse(riskResponse);
		}
		return orderStatusResponse;
	}

	/**
	 * Creates a new card
	 *
	 * @param addCardRequest
	 *           - AddCardRequest with all the required params
	 * @return AddCardResponse for the given request.
	 * @throws Exception
	 */
	public AddCardResponse addCard(final AddCardRequest addCardRequest) throws Exception
	{
		//Get the required params
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.CUSTOMERID, addCardRequest.getCustomerId());
		params.put("customer_email", addCardRequest.getCustomerEmail());
		params.put(MarketplaceJuspayServicesConstants.CARDNUMBER, String.valueOf(addCardRequest.getCardNumber()));
		params.put("card_exp_year", String.valueOf(addCardRequest.getCardExpYear()));
		params.put("card_exp_month", String.valueOf(addCardRequest.getCardExpMonth()));
		params.put(MarketplaceJuspayServicesConstants.NAMEONCARD,
				addCardRequest.getNameOnCard() != null ? addCardRequest.getNameOnCard() : "");
		params.put("nickname", addCardRequest.getNickname() != null ? addCardRequest.getNickname() : "");

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/card/add";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject json = (JSONObject) JSONValue.parse(response);

		final String cardToken = (String) json.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		final String cardReference = (String) json.get(MarketplaceJuspayServicesConstants.CARDREF);

		final AddCardResponse addCardResponse = new AddCardResponse();
		addCardResponse.setCardToken(cardToken);
		addCardResponse.setCardReference(cardReference);

		return addCardResponse;

	}

	/**
	 * Deletes the card (if present) and returns the DeleteCardResponse associated with the request
	 *
	 * @param deleteCardRequest
	 *           - DeleteCardRequest request with all required params.
	 * @return DeleteCardResponse for the given request.
	 * @throws Exception
	 */
	public DeleteCardResponse deleteCard(final DeleteCardRequest deleteCardRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.CARDTOKEN, deleteCardRequest.getCardToken());

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/card/delete";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final String card_token = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		final Boolean deleted = (Boolean) jsonResponse.get("deleted");
		String card_reference = null;
		if (null != jsonResponse.get(MarketplaceJuspayServicesConstants.CARDREF))
		{
			card_reference = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.CARDREF);
		}

		return new DeleteCardResponse().withCardToken(card_token).withDeleted(deleted.booleanValue())
				.withCardReference(card_reference);
	}

	/**
	 * Initiates the payment call and returns the response of Payment
	 *
	 * @param paymentRequest
	 *           - PaymentRequest with all required params
	 * @return PaymentResponse for the given request
	 * @throws Exception
	 */
	public PaymentResponse makePayment(final PaymentRequest paymentRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		//String merchantId, String orderId,String cardNumber,String cardExpYear,String cardExpMonth,String cardSecurityCode
		params.put(MarketplaceJuspayServicesConstants.MERCHANTID, paymentRequest.getMerchantId());
		params.put("orderId", paymentRequest.getOrderId());
		params.put("cardNumber", paymentRequest.getCardNumber());
		params.put("cardExpYear", paymentRequest.getCardExpYear());
		params.put("cardExpMonth", paymentRequest.getCardExpMonth());
		params.put("cardSecurityCode", paymentRequest.getCardSecurityCode());

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/payment/handlePay";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final String status = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.STATUS);
		final String txnId = (String) jsonResponse.get("txnId");
		final String merchantId = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.MERCHANTID);
		final String purchaseId = (String) jsonResponse.get("purchaseId");
		final String vbvUrl = (String) jsonResponse.get("vbv_url");

		final PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setStatus(status);
		paymentResponse.setTxnId(txnId);
		paymentResponse.setMerchantId(merchantId);
		paymentResponse.setPurchaseId(purchaseId);
		paymentResponse.setVbvUrl(vbvUrl);

		return paymentResponse;
	}

	/**
	 * List the cards for the user (if already present) for the given request
	 *
	 * @param listCardsRequest
	 *           - ListCardsRequest with all required params.
	 * @return ListCardsResponse for the given request.
	 * @throws Exception
	 */
	public ListCardsResponse listCards(final ListCardsRequest listCardsRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.CUSTOMERID, listCardsRequest.getCustomerId());

		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/card/list";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final String customerId = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.CUSTOMERID);
		final String merchantId = (String) jsonResponse.get(MarketplaceJuspayServicesConstants.MERCHANTID);
		final ArrayList<JSONObject> tempCards = (ArrayList<JSONObject>) jsonResponse.get("cards");

		final ListCardsResponse listCardsResponse = new ListCardsResponse();
		listCardsResponse.setCustomerId(customerId);
		listCardsResponse.setMerchantId(merchantId);

		final ArrayList<StoredCard> cards = new ArrayList<StoredCard>();

		if (tempCards != null)
		{
			for (final JSONObject cardObject : tempCards)
			{

				final StoredCard card = new StoredCard();

				card.withCardToken((String) cardObject.get(MarketplaceJuspayServicesConstants.CARDTOKEN))
						.withCardReference((String) cardObject.get(MarketplaceJuspayServicesConstants.CARDREF))
						.withCardNumber((String) cardObject.get(MarketplaceJuspayServicesConstants.CARDNUMBER))
						.withCardExpYear((String) cardObject.get("card_exp_year"))
						.withCardExpMonth((String) cardObject.get("card_exp_month")).withCardIsin((String) cardObject.get("card_isin"))
						.withNameOnCard(cardObject.get(MarketplaceJuspayServicesConstants.NAMEONCARD) != null
								? cardObject.get(MarketplaceJuspayServicesConstants.NAMEONCARD).toString() : "")
						.withCardToken((String) cardObject.get(MarketplaceJuspayServicesConstants.CARDTOKEN))
						.withCardBrand((String) cardObject.get("card_brand")).withCardIssuer((String) cardObject.get("card_issuer"))
						.withCardType((String) cardObject.get("card_type")).withNickname((String) cardObject.get("nickname"));

				cards.add(card);
			}
		}

		listCardsResponse.setCards(cards);
		return listCardsResponse;
	}

	public RefundResponse refund(final RefundRequest refundRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put(MarketplaceJuspayServicesConstants.ORDERID, refundRequest.getOrderId());
		params.put(MarketplaceJuspayServicesConstants.AMOUNT, refundRequest.getAmount().toString());
		params.put("unique_request_id", refundRequest.getUniqueRequestId());
		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/order/refund";
		LOG.debug("JUSPAY REFUND REQUEST--------------url-----" + url + "-------request----" + serializedParams);
		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);
		if (jsonResponse != null)
		{
			LOG.debug("JUSPAY REFUND RESPONSE----------" + jsonResponse.toJSONString());
		}
		final RefundResponse refundResponse = (RefundResponse) assembleOrderStatusResponse(jsonResponse, new RefundResponse());
		refundResponse.setSuccess(true);
		return refundResponse;

	}

	/**
	 * Assembles a complete Promotion object from the given JSONObject.
	 *
	 * @param jsonObject
	 *           - An instance of JSONObject that contains promotion information.
	 * @return - Promotion object that corresponds to the json input.
	 */
	private Promotion assemblePromotionObjectFromJSON(final JSONObject jsonObject)
	{
		final Promotion promotion = new Promotion();
		promotion.setId((String) jsonObject.get("id"));
		promotion.setOrderId((String) jsonObject.get(MarketplaceJuspayServicesConstants.ORDERID));
		promotion.setDiscountAmount(Double.valueOf(jsonObject.get("discount_amount").toString()));
		final String status = (String) jsonObject.get(MarketplaceJuspayServicesConstants.STATUS);
		promotion.setStatus(PromotionStatus.valueOf(status));

		final ArrayList<JSONObject> conditions = (ArrayList<JSONObject>) jsonObject.get("conditions");
		final ArrayList<JSONObject> rules = (ArrayList<JSONObject>) jsonObject.get("rules");

		final List<PromotionCondition> promotionConditions = new ArrayList<PromotionCondition>();
		for (final JSONObject jsonCondition : (conditions != null ? conditions : rules))
		{
			final PromotionCondition promotionCondition = new PromotionCondition();
			promotionCondition.setDimension((String) jsonCondition.get("dimension"));
			promotionCondition.setValue((String) jsonCondition.get("value"));
			promotionConditions.add(promotionCondition);
		}
		promotion.setPromotionConditions(promotionConditions);
		return promotion;
	}


	/**
	 * Create a promotion for an order given all the inputs.
	 *
	 * @param createPromotionRequest
	 *           - An instance of CreatePromotionRequest that encapsulates all the information pertaining to the
	 *           promotion.
	 * @return - An instance of CreatePromotionResponse that contains the response status of the API call and the
	 *         Promotion object itself.
	 * @throws Exception
	 */
	public CreatePromotionResponse createPromotion(final CreatePromotionRequest createPromotionRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		final double discountAmount = createPromotionRequest.getDiscountAmount();
		final Double discAmt = Double.valueOf(discountAmount);
		params.put(MarketplaceJuspayServicesConstants.ORDERID, createPromotionRequest.getOrderId());
		params.put("discount_amount", discAmt.toString());
		for (int i = 0; i < createPromotionRequest.getPromotionConditions().size(); i++)
		{
			final PromotionCondition condition = createPromotionRequest.getPromotionConditions().get(i);
			params.put("dimensions[" + i + "]", condition.getDimension());
			params.put("values[" + i + "]", condition.getValue());
		}
		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/promotions";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final CreatePromotionResponse promotionResponse = new CreatePromotionResponse();
		promotionResponse.setSuccess(true);
		final Promotion promotion = assemblePromotionObjectFromJSON(jsonResponse);
		promotionResponse.setPromotion(promotion);
		return promotionResponse;
	}

	/**
	 * Deactivates a promotion.
	 *
	 * @param deactivatePromotionRequest
	 *           - request object representing the promotion using the id.
	 * @return - returns an object containing the status and the promotion object itself.
	 * @throws Exception
	 */
	public DeactivatePromotionResponse deactivatePromotion(final DeactivatePromotionRequest deactivatePromotionRequest)
			throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("promotion_id", deactivatePromotionRequest.getPromotionId());
		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/promotions/" + deactivatePromotionRequest.getPromotionId() + "/deactivate";

		final String response = makeServiceCall(url, serializedParams);
		final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);

		final DeactivatePromotionResponse deactivatePromotionResponse = new DeactivatePromotionResponse();
		deactivatePromotionResponse.setSuccess(true);
		final Promotion promotion = assemblePromotionObjectFromJSON(jsonResponse);
		deactivatePromotionResponse.setPromotion(promotion);
		return deactivatePromotionResponse;
	}



	/**
	 * Gets the response for Netbanking from PayU
	 *
	 * @return String
	 * @throws Exception
	 */
	public String getNetbankingResponse(final NetbankingRequest netbankingRequest) throws Exception
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put(MarketplaceJuspayServicesConstants.ORDERID, netbankingRequest.getOrderId());
		params.put("merchant_id", netbankingRequest.getMerchantId());
		params.put("payment_method_type", netbankingRequest.getPaymentMethodType());
		params.put("payment_method", netbankingRequest.getPaymentMethod());
		params.put("redirect_after_payment", netbankingRequest.getRedirectAfterPayment());
		params.put("format", netbankingRequest.getFormat());
		final String serializedParams = serializeParams(params);
		final String url = baseUrl + "/txns";

		final String response = makeServiceCall(url, serializedParams);

		return response;
	}


	//	private String makeServiceCallForNB(final String endPoint, final String encodedParams) throws Exception
	//	{
	//
	//		final String proxyEnableStatus = getConfigurationService().getConfiguration()
	//				.getString(MarketplaceJuspayServicesConstants.PROXYENABLED);
	//		HttpsURLConnection connection = null;
	//		final StringBuilder buffer = new StringBuilder();
	//
	//		try
	//		{
	//			if (proxyEnableStatus.equalsIgnoreCase("true"))
	//			{
	//				final String proxyName = getConfigurationService().getConfiguration()
	//						.getString(MarketplaceJuspayServicesConstants.GENPROXY);
	//				final int proxyPort = Integer.parseInt(
	//						getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.GENPROXYPORT));
	//				final SocketAddress addr = new InetSocketAddress(proxyName, proxyPort);
	//				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
	//				final URL url = new URL(endPoint);
	//				connection = (HttpsURLConnection) url.openConnection(proxy);
	//			}
	//			else
	//			{
	//				final URL url = new URL(endPoint);
	//				connection = (HttpsURLConnection) url.openConnection();
	//			}
	//
	//			String encodedKey = new String(Base64.encodeBase64(this.key.getBytes()));
	//			encodedKey = encodedKey.replaceAll("\n", "");
	//			connection.setRequestProperty("Authorization", "Basic " + encodedKey);
	//
	//			connection.setConnectTimeout(connectionTimeout);
	//			connection.setReadTimeout(readTimeout);
	//			connection.setRequestMethod("POST");
	//			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	//			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
	//			connection.setRequestProperty("Content-Language", "en-US");
	//			connection.setRequestProperty("charset", "utf-8");
	//			connection.setRequestProperty("version", "2015-01-09");
	//			connection.setUseCaches(false);
	//			connection.setDoInput(true);
	//			connection.setDoOutput(true);
	//
	//			// Setup the POST payload
	//
	//			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	//			wr.writeBytes(encodedParams);
	//			wr.flush();
	//
	//			// Read the response
	//			final InputStream inputStream = connection.getInputStream();
	//			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	//			String line;
	//			while ((line = in.readLine()) != null)
	//			{
	//				buffer.append(line);
	//			}
	//			wr.close();
	//			in.close();
	//			return buffer.toString();
	//		}
	//		catch (final Exception e)
	//		{
	//			final InputStream errorStream = connection.getErrorStream();
	//			LOG.error("Error occured with::::" + errorStream);
	//			throw new Exception("Exception while trying to make service call to Juspay", e);
	//		}
	//	}






	public void setConnectionTimeout(final int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	public void setReadTimeout(final int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}

}
