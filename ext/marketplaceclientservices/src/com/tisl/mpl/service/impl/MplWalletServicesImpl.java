/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.pojo.request.AddToCardWallet;
import com.tisl.mpl.pojo.request.PurchaseEGVRequest;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.request.QCRefundRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.PurchaseEGVResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.pojo.response.WalletBalanceResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;
import com.tisl.mpl.service.MplWalletServices;
import com.tisl.mpl.service.QCInitDataBean;



/**
 * @author TUL
 *
 */
public class MplWalletServicesImpl implements MplWalletServices
{

	private static final Logger LOG = Logger.getLogger(MplWalletServicesImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "qcInitDataBean")
	public QCInitDataBean qcInitDataBean;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private final String SELECT_CLASS = "select {";
	private final String FROM_CLASS = "} from {";
	private final String WHERE_CLASS = "} where {";

	/**
	 * @return the qcInitDataBean
	 */
	public QCInitDataBean getQcInitDataBean()
	{
		return qcInitDataBean;
	}


	/**
	 * @param qcInitDataBean
	 *           the qcInitDataBean to set
	 */
	public void setQcInitDataBean(final QCInitDataBean qcInitDataBean)
	{
		this.qcInitDataBean = qcInitDataBean;
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

	@Override
	public QCInitializationResponse walletInitilization()
	{
		QCInitializationResponse qcInitializationResponse = new QCInitializationResponse();
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;

		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));

			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/Qwikcilver/eGMS.RestApi/api/initialize").build());
			final String forwardEntityID = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID);
			final String forwardEntityPassword = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD);

			final String terminalID = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.TERMINAL_ID);
			final String userName = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.USERNAME);
			final String password = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.PASSWORD);
			final String isForwardingEntryExists = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS);

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, forwardEntityID)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, forwardEntityPassword)
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, terminalID)
					.header(MarketplaceclientservicesConstants.USERNAME, userName)
					.header(MarketplaceclientservicesConstants.PASSWORD, password)
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, "11")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, isForwardingEntryExists)
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON).get(ClientResponse.class);
			LOG.debug("*********response**************" + response);
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				final ObjectMapper objectMapper = new ObjectMapper();

				qcInitializationResponse = objectMapper.readValue(output, QCInitializationResponse.class);
				LOG.debug("*********qcInitializationResponse**************" + qcInitializationResponse.toString());
				return qcInitializationResponse;

			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * qcInitializationResponse.setResponseMessage("Timeout Exception");
			 * qcInitializationResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return qcInitializationResponse;
		}

		return qcInitializationResponse;
	}

	@Override
	public QCCustomerRegisterResponse registerCustomerWallet(final QCCustomerRegisterRequest registerCustomerRequest,
			final String transactionId)
	{

		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		QCCustomerRegisterResponse custResponse = new QCCustomerRegisterResponse();
		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/Qwikcilver/eGMS.RestApi/api/wallet/").build());

			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(registerCustomerRequest);

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", transactionId)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Type", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null")
					.header("CurrentBatchNumber", getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				custResponse = objectMapper.readValue(output, QCCustomerRegisterResponse.class);
				LOG.debug(" *********QC NEW CUSTOMER REGISTRATION***** response----" + custResponse.getWallet().getWalletNumber());
				return custResponse;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * custResponse.setResponseMessage("Timeout Exception"); custResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return custResponse;
		}
		return custResponse;
	}


	@Override
	public PurchaseEGVResponse purchaseEgv(final PurchaseEGVRequest purchaseEGVRequest, final String transactionId)
	{

		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		PurchaseEGVResponse purchaseEgvResponse = new PurchaseEGVResponse();

		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/Qwikcilver/eGMS.RestApi/api/gc/createandissue").build());

			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(purchaseEGVRequest);

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", transactionId)
					//(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null")
					.header("CurrentBatchNumber", getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ******PURCHASE EGV******** output----" + output);
				purchaseEgvResponse = objectMapper.readValue(output, PurchaseEGVResponse.class);
				return purchaseEgvResponse;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * purchaseEgvResponse.setResponseMessage("Timeout Exception");
			 * purchaseEgvResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return purchaseEgvResponse;
		}
		return purchaseEgvResponse;
	}

	@Override
	public void addEgvToWallet()
	{
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020032/card").build());

			//need to create marshalling for request body
			final String requestBody = "{\"CardNumber\":\"4000161013166520\", \"CardPin\":\"368719\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "21")
					//(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null")
					.header("CurrentBatchNumber", getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);


			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Added EGV to Wallet----" + output); //need to create marshalling for response object
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
		}
	}



	@Override
	public BalanceBucketWise getQCBucketBalance(final String customerWalletId, final String transactionId)
	{
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		BalanceBucketWise walletBalance = new BalanceBucketWise();

		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));

			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId + "/balance").build());
			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10").header("Username", "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul").header("TransactionId", transactionId)
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				final ObjectMapper objectMapper = new ObjectMapper();
				walletBalance = objectMapper.readValue(output, BalanceBucketWise.class);

				return walletBalance;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * walletBalance.setResponseMessage("Timeout Exception"); walletBalance.setResponseCode(Integer.valueOf(3001));
			 * }
			 */
			return walletBalance;
		}
		return walletBalance;
	}


	@Override
	public QCRedeeptionResponse getWalletRedeem(final String customerWalletId, final String transactionId,
			final QCRedeemRequest qcRedeemRequest)
	{
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		ClientResponse response = null;
		WebResource webResource = null;
		QCRedeeptionResponse qcRedeeptionResponse = new QCRedeeptionResponse();

		try
		{

			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId + "/Redeem").build());
			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(qcRedeemRequest);
			response = webResource.header("ForwardingEntityId", "tatacliq.com").header("ForwardingEntityPassword", "tatacliq.com")
					.header("TerminalId", "webpos-tul-dev10").header("Username", "tulwebuser").header("Password", "webusertul")
					.header("TransactionId", transactionId)
					//(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Type", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null")
					.header("CurrentBatchNumber", getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				return qcRedeeptionResponse;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * qcRedeeptionResponse.setResponseMessage("Timeout Exception");
			 * qcRedeeptionResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return qcRedeeptionResponse;
		}
		return qcRedeeptionResponse;
	}


	@Override
	public QCRedeeptionResponse getWalletRefundRedeem(final String walletId, final QCRefundRequest qcRefundRequest)
	{
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		QCRedeeptionResponse qcRedeeptionResponse = null;
		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + walletId + "/Cancelredeem").build());

			//need to create marshalling for request body
			//Transaction ID will be same as Wallet redeem
			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(qcRefundRequest);

			//final String requestBody = "{\"OriginalTransactionId\":\"20\",\"OriginalBatchNumber\":\"10207477\"}";

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER, getQcInitDataBean().getCurrentBatchNumber())
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, qcRefundRequest.getOriginalTransactionId())
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);



			if (null != response)
			{
				final String output = response.getEntity(String.class);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				LOG.debug(" ************** output----" + output);
				LOG.debug(" ************** redeem Refund Wallet Balance----" + output); //need to create marshalling for response object

			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			if (ex instanceof SocketTimeoutException)
			{
				//
			}
		}
		return qcRedeeptionResponse;
	}


	@Override
	public QCRedeeptionResponse addTULWalletCashBack(final String walletId, final QCCustomerPromotionRequest request)
	{
		LOG.debug("***********************************in Mpl Wallet request..............." + request.toString());

		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		String requestBody = null;
		QCRedeeptionResponse qcRedeeptionResponse = null;
		String output = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			requestBody = objectMapper.writeValueAsString(request);
			webResource = client.resource(UriBuilder
					.fromUri(MarketplaceclientservicesConstants.GET_BALANCE_FOR_WALLET + walletId + "/load/CASHBACK").build());

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, request.getInvoiceNumber())
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			if (null != response)
			{
				output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				if (null != qcRedeeptionResponse)
				{
					LOG.debug(" ************** GET CUSTOMER WALLET BALENCE----" + qcRedeeptionResponse); //need to create marshalling for response object
				}
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
		return qcRedeeptionResponse;
	}


	@Override
	public QCRedeeptionResponse refundTULPromotionalCash(final String walletId, final String transactionId)
	{
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		String requestBody = null;
		String output = null;
		QCRedeeptionResponse qcRedeeptionResponse = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final QCRefundRequest request = new QCRefundRequest();
		request.setOriginalTransactionId(transactionId);
		request.setOriginalBatchNumber(getConfigurationService().getConfiguration().getString("qc.batch.number"));
		try
		{
			//get Wallet number from facade
			//TransactionId unique
			// InvoiceNo Unique
			webResource = client.resource(
					UriBuilder.fromUri(MarketplaceclientservicesConstants.ADD_TO_CARD_TO_WALLET + walletId + "/CancelLoad").build());
			final ObjectMapper objectMapper = new ObjectMapper();
			requestBody = objectMapper.writeValueAsString(request);

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, transactionId).type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				if (null != qcRedeeptionResponse)
				{
					LOG.debug(" ************** GET CUSTOMER WALLET BALENCE----" + qcRedeeptionResponse); //need to create marshalling for response object
				}

			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
		return qcRedeeptionResponse;
	}


	@Override
	public CustomerWalletDetailResponse getCustomerWallet(final String customerWalletId, final String transactionId)
	{

		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		CustomerWalletDetailResponse custWalletDetail = new CustomerWalletDetailResponse();
		final ObjectMapper objectMapper = new ObjectMapper();

		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId).build());

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "46")
					//(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null")
					.header("CurrentBatchNumber", getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (null != response)
			{

				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				custWalletDetail = objectMapper.readValue(output, CustomerWalletDetailResponse.class);
				return custWalletDetail;

			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * custWalletDetail.setResponseMessage("Timeout Exception");
			 * custWalletDetail.setResponseCode(Integer.valueOf(3001)); }
			 */
			return custWalletDetail;
		}
		return custWalletDetail;

	}



	@Override
	public RedimGiftCardResponse getAddEGVToWallet(final String cardNumber, final String cardPin, final String transactionId,
			final String customerWalletId)
	{

		RedimGiftCardResponse redimGiftCardResponse = new RedimGiftCardResponse();
		try
		{
			//	final Client client = Client.create();
			final Client client = getProxyConnection();
			LOG.debug("Successfully client .................." + client);
			ClientResponse response = null;
			WebResource webResource = null;
			String requestBody = null;
			String output = null;

			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));

			final AddToCardWallet addToCardWallet = buildAddtoCardWallet(cardNumber, cardPin);
			final ObjectMapper objectMapper = new ObjectMapper();

			requestBody = objectMapper.writeValueAsString(addToCardWallet);
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId + "/card").build());

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, transactionId).type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				redimGiftCardResponse = objectMapper.readValue(output, RedimGiftCardResponse.class);
				return redimGiftCardResponse;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * redimGiftCardResponse.setResponseMessage("Timeout Exception");
			 * redimGiftCardResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return redimGiftCardResponse;
		}
		return redimGiftCardResponse;

	}


	public WalletBalanceResponse getBalenceForWallet(final String cardNumber, final String transactionId)
	{
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		WalletBalanceResponse walletBalanceResponse = new WalletBalanceResponse();
		String output = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + cardNumber + "/balance").build());

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, transactionId).type(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			if (null != response)
			{
				output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				walletBalanceResponse = objectMapper.readValue(output, WalletBalanceResponse.class);
				if (null != walletBalanceResponse && null != walletBalanceResponse.getWallet())
				{
					LOG.debug(" ************** GET CUSTOMER WALLET BALENCE----" + walletBalanceResponse.getWallet().getBalance()); //need to create marshalling for response object
				}
				System.out.println(" ************** GET CUSTOMER WALLET BALENCE----" + walletBalanceResponse.getResponseCode());


			}
		}
		catch (final Exception ex)

		{
			LOG.error(ex.getMessage());
			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * walletBalanceResponse.setResponseMessage("Timeout Exception");
			 * walletBalanceResponse.setResponseCode(Integer.valueOf(3001)); }
			 */
			return walletBalanceResponse;
		}

		return walletBalanceResponse;
	}

	@Override
	public WalletTransacationsList getWalletTransactionList(final String walletCardNumber, final String transactionId)
	{

		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		WebResource webResource = null;
		WalletTransacationsList walletTransacationsList = new WalletTransacationsList();

		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			client.setConnectTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			client.setReadTimeout(Integer.valueOf(getConfigurationService().getConfiguration().getString("qcTimeout")));
			webResource = client
					.resource(UriBuilder.fromUri("http://" + getConfigurationService().getConfiguration().getString("qcUrl")
							+ "/QwikCilver/eGMS.RestAPI/api/wallet/" + walletCardNumber + "/transactions").build());

			final ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, transactionId).type(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);


			if (null != response)
			{

				final String output = response.getEntity(String.class);
				walletTransacationsList = objectMapper.readValue(output, WalletTransacationsList.class);
				LOG.debug("response output .................." + output);
				if (null != walletTransacationsList && null != walletTransacationsList.getWalletTransactions())
				{
					LOG.debug("walletTransacationsList .................." + walletTransacationsList);
					return walletTransacationsList;
				}
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());

			/*
			 * if (ex.getMessage().contains("SocketTimeoutException")) {
			 * walletTransacationsList.setResponseMessage("Timeout Exception");
			 * walletTransacationsList.setResponseCode(Integer.valueOf(3001)); }
			 */
			return walletTransacationsList;
		}
		return walletTransacationsList;
	}

	public AddToCardWallet buildAddtoCardWallet(final String cardNumber, final String cardPin)
	{

		final AddToCardWallet addToCardWallet = new AddToCardWallet();
		addToCardWallet.setCardNumber(cardNumber);
		addToCardWallet.setCardPin(cardPin);
		return addToCardWallet;

	}

	@Override
	public QCCustomerRegisterResponse createWalletContainer(final QCCustomerRegisterRequest registerCustomerRequest,
			final String transactionId)
	{
		// YTODO Auto-generated method stub
		return registerCustomerWallet(registerCustomerRequest, transactionId);
	}


	@Override
	public QCRedeeptionResponse createPromotion(final String walletId, final QCCustomerPromotionRequest request)
	{
		System.out.println("***********************************in Mpl Wallet request..............." + request.toString());
		LOG.debug("Successfully request.toString()  .................." + request.toString());
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		QCRedeeptionResponse qcRedeeptionResponse = new QCRedeeptionResponse();
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			final String requestBody = objectMapper.writeValueAsString(request);
			webResource = client.resource(UriBuilder
					.fromUri(MarketplaceclientservicesConstants.GET_BALANCE_FOR_WALLET + walletId + "/load/PROMOTION").build());

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, request.getInvoiceNumber())
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			LOG.debug("requestBody client .................." + requestBody);
			LOG.debug("response client .................." + response);
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				if (null != qcRedeeptionResponse)
				{
					LOG.debug(" ************** GET CUSTOMER WALLET BALENCE----" + qcRedeeptionResponse.toString()); //need to create marshalling for response object
				}
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			LOG.error("exception............ client .................." + ex);
		}
		return qcRedeeptionResponse;
	}


	@Override
	public QCRedeeptionResponse qcCredit(final String walletId, final QCCreditRequest request)
	{
		System.out.println("***********************************in QCCreditRequest request..............." + request.toString());
		LOG.debug("Successfully request.toString()  .................." + request.toString());
		//	final Client client = Client.create();
		final Client client = getProxyConnection();
		LOG.debug("Successfully client .................." + client);
		ClientResponse response = null;
		WebResource webResource = null;
		QCRedeeptionResponse qcRedeeptionResponse = new QCRedeeptionResponse();
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			final String requestBody = objectMapper.writeValueAsString(request);
			webResource = client.resource(
					UriBuilder.fromUri(MarketplaceclientservicesConstants.GET_BALANCE_FOR_WALLET + walletId + "/load/CREDIT").build());

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10")
					.header(MarketplaceclientservicesConstants.USERNAME, "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul")
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, "true")
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, "application/json")
					.header(MarketplaceclientservicesConstants.MERCHANT_OUTLET_NAME, "TUL-Online")
					.header(MarketplaceclientservicesConstants.ACQUIRERID, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.ORGANIZATION_NAME, "Tata Unistore Ltd")
					.header(MarketplaceclientservicesConstants.POS_ENTRY_MODE, "2")
					.header(MarketplaceclientservicesConstants.POS_TYPE_ID, "1")
					.header(MarketplaceclientservicesConstants.POS_NAME, "webpos-tul-qc-01")
					.header(MarketplaceclientservicesConstants.TERM_APP_VERSION, "null")
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER,
							getConfigurationService().getConfiguration().getString("qc.batch.number"))
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, request.getInvoiceNumber())
					.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, requestBody);

			LOG.debug("requestBody client .................." + requestBody);
			LOG.debug("response client .................." + response);
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** output----" + output);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				if (null != qcRedeeptionResponse)
				{
					LOG.debug(" ************** GET CUSTOMER WALLET BALENCE----" + qcRedeeptionResponse.toString()); //need to create marshalling for response object
				}
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			LOG.error("exception............ client .................." + ex);
		}
		return qcRedeeptionResponse;
	}

	/**
	 * @return
	 */
	private Client getProxyConnection()
	{
		final ClientConfig config = new DefaultClientConfig();
		LOG.debug("***********Try to configure the Proxy");
		final String proxyEnableStatus = getConfigurationService().getConfiguration().getString("proxy.enabled");
		LOG.debug("********proxyEnableStatus*********:" + proxyEnableStatus);
		final String proxyAddress = getConfigurationService().getConfiguration().getString("proxy.address");
		LOG.debug("********proxyAddress*********:" + proxyAddress);
		final String proxyPort = getConfigurationService().getConfiguration().getString("proxy.port");
		System.out.println("********proxyPort*********:" + proxyPort);
		final Client client = new Client(new URLConnectionClientHandler(new HttpURLConnectionFactory()
		{
			Proxy p = null;

			@Override
			public HttpURLConnection getHttpURLConnection(final URL url) throws IOException
			{
				if (p == null)
				{
					if (proxyEnableStatus.equalsIgnoreCase("true"))
					{
						p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
					}
					else
					{
						p = Proxy.NO_PROXY;
					}
				}
				return (HttpURLConnection) url.openConnection(p);
			}

		}), config);
		LOG.debug("Successfully Configured Proxy ..................");
		return client;
	}

	@Override
	public WalletCardApportionDetailModel getOrderFromWalletCardNumber(final String cardNumber)
	{ 
		final String query = SELECT_CLASS + WalletCardApportionDetailModel.PK + FROM_CLASS + WalletCardApportionDetailModel._TYPECODE + WHERE_CLASS
				+ WalletCardApportionDetailModel.CARDNUMBER + "} =?cardNumber";
		final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
		flexQuery.addQueryParameter("cardNumber", cardNumber);
		return flexibleSearchService.<WalletCardApportionDetailModel> searchUnique(flexQuery);
	}

}
