/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplWalletServices;


/**
 * @author TUL
 *
 */
public class MplWalletServicesImpl implements MplWalletServices
{

	private static final Logger LOG = Logger.getLogger(MplWalletServicesImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;




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
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			webResource = client.resource(UriBuilder.fromUri(MarketplaceclientservicesConstants.QC_INITIALIZATION_URL).build());
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
			final String transactionID = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.TRANSACTION_ID);
			final String isForwardingEntryExists = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS);

			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, forwardEntityID)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, forwardEntityPassword)
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, terminalID)
					.header(MarketplaceclientservicesConstants.USERNAME, userName)
					.header(MarketplaceclientservicesConstants.PASSWORD, password)
					.header(MarketplaceclientservicesConstants.TRANSACTION_ID, transactionID) //(random number logic)
					.header(MarketplaceclientservicesConstants.DATE_AT_CLIENT, dateFormat.format(new Date()))
					.header(MarketplaceclientservicesConstants.IS_FORWARDING_ENTIRY_EXISTS, isForwardingEntryExists)
					.header(MarketplaceclientservicesConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				final ObjectMapper objectMapper = new ObjectMapper();

				qcInitializationResponse = objectMapper.readValue(output, QCInitializationResponse.class);
				return qcInitializationResponse;
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}

		return qcInitializationResponse;
	}

	@Override
	public void createWallet()
	{

		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


		try
		{
			webResource = client
					.resource(UriBuilder.fromUri("http://qc3.qwikcilver.com/Qwikcilver/eGMS.RestApi/api/wallet/").build());

			//need to create marshalling for request body
			final String requestBody = "{\"Externalwalletid\":\"nb@tataunistore.com\",\"Customer\":{\"CustomerType\":null,\"Salutation\":\"\",\"Firstname\":\"Nirav\",\"LastName\":\"B\",\"PhoneNumber\":\"9876543288\",\"Email\":\"nb@tataunistore.com\",\"DOB\":\"\",\"AddressLine1\":\"add1\",\"AddressLine2\":\"add2\",\"AddressLine3\":\"add3\",\"City\":\"Bangalore\",\"State\":\"Karnataka\",\"Country\":\"India\",\"Gender\":\"\",\"Anniversary\":\"\",\"MaritalStatus\":\"\",\"EmployeeID\":\"\",\"PhoneAlternate\":\"2709201601\",\"PinCode\":\"\",\"Region\":null,\"Area\":\"\",\"CorporateName\":\"Tata Unistore Ltd\"},\"Notes\":\"Wallet creation\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "1") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Type", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);



			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Initilization for  WALLET" + output); //need to create marshalling for response object
				System.out.println(" ************** Initilization for  WALLET" + output);

			}
		}
		catch (

		final Exception ex)
		{

			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}


	}



	@Override
	public void purchaseEgv()
	{

		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			webResource = client
					.resource(UriBuilder.fromUri("http://qc3.qwikcilver.com/Qwikcilver/eGMS.RestApi/api/gc/createandissue").build());

			//need to create marshalling for request body
			//IdempotencyKey random unique logic use UUID for generat key
			final String requestBody = "{\"CardProgramGroupName\": \"TUL B2C eGift Card\",\"Amount\": 2000,\"BillAmount\": 0,\"InvoiceNumber\": \"27092016001\",\"ExternalCardNumber\": null,\"Customer\": {\"CustomerType\": null,\"Salutation\": \"\",\"Firstname\": \"Gift Card\",\"LastName\": \"user\",\"PhoneNumber\": \"2709201600\",\"Email\": \"nbhanushali@tataunistore.com\",\"DOB\": \"\",\"AddressLine1\": \"\",\"AddressLine2\": \"\",\"AddressLine3\": null,\"City\": \"\",\"State\": \"\",\"Country\": \"\",\"Gender\": \"\",\"Anniversary\": \"\",\"MaritalStatus\": \"\",\"EmployeeID\": \"\",\"PhoneAlternate\": \"2709201601\",\"PinCode\": \"\",\"Region\": null,\"Area\": \"\",\"CorporateName\": \"Tata Unistore Ltd\"},\"Expiry\":\"0001-01-01T00:00:00\",\"Notes\": \"create egiftcard\",\"IdempotencyKey\":\"tq2408201701\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "22") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Purchase EGV----" + output); //need to create marshalling for response object
				System.out.println(" ************** Purchase EGV----" + output);

			}
		}
		catch (

		final Exception ex)
		{

			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
	}

	@Override
	public void addEgvToWallet()
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


		try
		{
			//get Wallet number from facade
			webResource = client.resource(
					UriBuilder.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020032/card").build());

			//need to create marshalling for request body
			final String requestBody = "{\"CardNumber\":\"4000161013166520\", \"CardPin\":\"368719\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "21") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);


			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Added EGV to Wallet----" + output); //need to create marshalling for response object
				System.out.println(" **************Added EGV to Wallet----" + output);

			}
		}
		catch (

		final Exception ex)
		{

			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
	}


	@Override
	public BalanceBucketWise getQCBucketBalance(final String customerWalletId, final String transactionId)
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		BalanceBucketWise walletBalance = new BalanceBucketWise();

		try
		{
			webResource = client.resource(UriBuilder
					.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId + "/balance").build());


			response = webResource.type(MediaType.APPLICATION_JSON)
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_ID, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.FORWARDING_ENTITY_PASSWORD, "tatacliq.com")
					.header(MarketplaceclientservicesConstants.TERMINAL_ID, "webpos-tul-dev10").header("Username", "tulwebuser")
					.header(MarketplaceclientservicesConstants.PASSWORD, "webusertul").header("TransactionId", transactionId) //(random number logic)
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
					.header(MarketplaceclientservicesConstants.CURRENT_BATCH_NUMBER, "10208532").type(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				final ObjectMapper objectMapper = new ObjectMapper();
				walletBalance = objectMapper.readValue(output, BalanceBucketWise.class);

				return walletBalance;
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		return walletBalance;
	}


	@Override
	public QCRedeeptionResponse getWalletRedeem(final String customerWalletId, final String transactionId,
			final QCRedeemRequest qcRedeemRequest)
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		QCRedeeptionResponse qcRedeeptionResponse = new QCRedeeptionResponse();

		try
		{
			//get Wallet number from facade
			webResource = client.resource(UriBuilder
					.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/" + customerWalletId + "/Redeem").build());
			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(qcRedeemRequest);
			response = webResource.header("ForwardingEntityId", "tatacliq.com").header("ForwardingEntityPassword", "tatacliq.com")
					.header("TerminalId", "webpos-tul-dev10").header("Username", "tulwebuser").header("Password", "webusertul")
					.header("TransactionId", transactionId) //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Type", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				qcRedeeptionResponse = objectMapper.readValue(output, QCRedeeptionResponse.class);
				System.out.println("Reedem--" + qcRedeeptionResponse.getResponseMessage());
				return qcRedeeptionResponse;
			}
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
		}
		return qcRedeeptionResponse;
	}


	@Override
	public void getWalletRefundRedeem()
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


		try
		{
			//get Wallet number from facade
			webResource = client.resource(UriBuilder
					.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020032/Cancelredeem").build());

			//need to create marshalling for request body
			//Transaction ID will be same as Wallet redeem
			final String requestBody = "{\"OriginalTransactionId\":\"20\",\"OriginalBatchNumber\":\"10207477\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "33") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Type", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);



			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** redeem Refund Wallet Balance----" + output); //need to create marshalling for response object
				System.out.println(" **************redeem Refund Wallet Balance----" + output);

			}
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}

	}


	@Override
	public void addTULWalletCashBack()
	{

		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			//get Wallet number from facade
			//TransactionId unique
			// InvoiceNo Unique
			webResource = client.resource(UriBuilder
					.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020028/load/CASHBACK").build());

			//need to create marshalling for request body
			final String requestBody = "{\"Amount\":\"500\",\"InvoiceNumber\":\"1003\",\"Notes\":\"Sample load for 500 for CASHBACK\"}";

			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "24") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Adding CashBack Balance----" + output); //need to create marshalling for response object
				System.out.println(" **************Adding CashBack Balance----" + output);

			}
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
	}


	@Override
	public void refundTULPromotionalCash()
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			//get Wallet number from facade
			//TransactionId unique
			// InvoiceNo Unique
			webResource = client.resource(UriBuilder
					.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020032/CancelLoad").build());

			//need to create marshalling for request body
			final String requestBody = "{\"OriginalTransactionId\":\"24\",\"OriginalBatchNumber\":\"10207477\"}";
			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "45") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, requestBody);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** Adding CashBack Balance----" + output); //need to create marshalling for response object
				System.out.println(" **************Adding CashBack Balance----" + output);

			}
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}
	}


	@Override
	public void getCustomerWallet()
	{
		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		//final ReturnLogisticsResponse responsefromOMS = new ReturnLogisticsResponse();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try
		{
			//get Wallet number from facade
			//TransactionId unique
			// InvoiceNo Unique
			webResource = client.resource(
					UriBuilder.fromUri("http://qc3.qwikcilver.com/QwikCilver/eGMS.RestAPI/api/wallet/4000162010020032").build());

			//need to create marshalling for request body
			response = webResource.type(MediaType.APPLICATION_JSON).header("ForwardingEntityId", "tatacliq.com")
					.header("ForwardingEntityPassword", "tatacliq.com").header("TerminalId", "webpos-tul-dev10")
					.header("Username", "tulwebuser").header("Password", "webusertul").header("TransactionId", "46") //(random number logic)
					.header("DateAtClient", dateFormat.format(new Date())).header("IsForwardingEntityExists", "true")
					.header("Content-Typ", "application/json").header("MerchantOutletName", "TUL-Online")
					.header("AcquirerId", "Tata Unistore Ltd").header("OrganizationName", "Tata Unistore Ltd")
					.header("POSEntryMode", "2").header("POSTypeId", "1").header("POSName", "webpos-tul-qc-01")
					.header("TermAppVersion", "null").header("CurrentBatchNumber", "10208532").type(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug(" ************** GET CUSTOMER WALLET----" + output); //need to create marshalling for response object
				System.out.println(" ************** GET CUSTOMER WALLET----" + output);

			}
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Error response Status:------" + response.getStatus());
		}

	}


}
