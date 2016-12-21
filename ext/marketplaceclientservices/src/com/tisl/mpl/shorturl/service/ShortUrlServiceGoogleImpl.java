/**
 *
 */
package com.tisl.mpl.shorturl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.gigya.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import com.tisl.mpl.core.model.OrderShortUrlInfoModel;
import com.tisl.mpl.shorturl.report.dao.OrderShortUrlDao;


/**
 * @author Techouts
 *
 *         Implementation class for short URL service, which will hit the Third party Google services to get short URL
 *         for a long URL
 */
public class ShortUrlServiceGoogleImpl implements ShortUrlService
{

	private static final Logger LOG = Logger.getLogger(ShortUrlServiceGoogleImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "shortUrlReportDaoImpl")
	private OrderShortUrlDao orderShortUrlDaoImpl;

	private final int connectionTimeout = 5 * 10000;
	private final int readTimeout = 5 * 1000;
	//private static final Logger log = Logger.getLogger(PaymentService.class);
	private String baseUrl;
	private String key;
	private String merchantId;
	//private Environment environment;
	private String environmentSet;

	/**
	 * @Description Generates short URL for a given order code
	 * @param orderCode
	 * @return shortUrl
	 */
	@Override
	public String genearateShortURL(final String orderCode)
	{
		try
		{
			final String longUrl = this.genearatelongUrl(orderCode);

			LOG.info("--OrderCode-- " + orderCode + "---Long URL-- " + longUrl);

			final String googleShortUrlApiKey = (String) getConfigurationService().getConfiguration().getProperty(
					MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
			final String googleShortUrlConnectUrl = (String) getConfigurationService().getConfiguration().getProperty(
					MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);

			final StringBuilder sb = new StringBuilder(googleShortUrlConnectUrl);
			sb.append("?key");
			sb.append("=" + googleShortUrlApiKey);

			final String url = new String(sb);

			final Client client = Client.create();
			//prepare input and send in body
			final String input = "{\"longUrl\": \"" + longUrl + "\"}";

			if (LOG.isDebugEnabled())
			{
				LOG.debug("API Key== " + googleShortUrlApiKey + "Connect URL=== " + googleShortUrlConnectUrl);
				LOG.debug("Before Connecting to Google URL === " + sb);
				LOG.debug("long url===" + longUrl);
			}

			try
			{
				LOG.debug(" method 1");
				final URLConnectionClientHandler ch = new URLConnectionClientHandler(new ConnectionFactory());
				final Client client1 = new Client(ch);
				final WebResource webResource = client1.resource(url);

				final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
						.post(ClientResponse.class, input);
				//if the response status is not 200,then return null(statuses other than 200 indicates wrong response)
				if (response.getStatus() != 200)
				{
					LOG.error("Status code =" + response.getStatus());
					final String output = response.getEntity(String.class);
					LOG.info(output);
					return null;
				}

				final String output = response.getEntity(String.class);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Output from google Server .... \n" + output);
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				LOG.debug(" method 4");
				final URLConnectionClientHandler ch = new URLConnectionClientHandler(new ConnectionFactoryTest());
				final Client client1 = new Client(ch);
				final WebResource webResource = client1.resource(url);

				final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
						.post(ClientResponse.class, input);
				//if the response status is not 200,then return null(statuses other than 200 indicates wrong response)
				if (response.getStatus() != 200)
				{
					LOG.error("Status code =" + response.getStatus());
					final String output = response.getEntity(String.class);
					LOG.info(output);
					return null;
				}

				final String output = response.getEntity(String.class);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Output from google Server .... \n" + output);
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			//client = ClientBuilder.newClient();
			//	client.property(ClientProperties.PROXY_URI, "<proxy_host>:<proxy_port");

			try
			{
				LOG.debug("Method 2");
				final WebResource webResource = client.resource(url);

				final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
						.post(ClientResponse.class, input);
				//if the response status is not 200,then return null(statuses other than 200 indicates wrong response)
				if (response.getStatus() != 200)
				{
					LOG.error("Status code =" + response.getStatus());
					final String output = response.getEntity(String.class);
					LOG.info(output);
					return null;
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				shortUrlMethod5();
				shortUrlMethod6();
				shortUrlMethod4();
				shortUrlMethod3();
				LOG.debug("Method 3");
				final WebResource webResource = client.resource(url);

				final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
						.post(ClientResponse.class, input);
				//if the response status is not 200,then return null(statuses other than 200 indicates wrong response)
				if (response.getStatus() != 200)
				{
					LOG.error("Status code =" + response.getStatus());
					final String output = response.getEntity(String.class);
					LOG.info(output);
					return null;
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			final WebResource webResource = client.resource(url);

			final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, input);
			//if the response status is not 200,then return null(statuses other than 200 indicates wrong response)
			if (response.getStatus() != 200)
			{
				LOG.error("Status code =" + response.getStatus());
				final String output = response.getEntity(String.class);
				LOG.info(output);
				return null;
			}

			final String output = response.getEntity(String.class);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Output from google Server .... \n" + output);
			}
			final JSONObject obj = new JSONObject(output);
			final String shortUrl = (String) obj.get("id");
			//create TULShortUrlReport model to generate report ,later use and update this model when user clicks on short url
			try
			{
				final OrderShortUrlInfoModel shortUrlModel = getModelService().create(OrderShortUrlInfoModel.class);
				shortUrlModel.setOrderId(orderCode);
				shortUrlModel.setShortURL(shortUrl);
				shortUrlModel.setLongURL(longUrl);
				getModelService().save(shortUrlModel);
			}
			catch (final ModelSavingException mse)
			{
				LOG.error("ModelSavingException while saving TULShortUrlReportModel " + orderCode);
			}
			catch (final Exception e)
			{
				LOG.error("Error while saving TULShortUrlReportModel " + orderCode);
			}
			return shortUrl;
		}
		catch (final Exception e)
		{
			LOG.error("Error while connecting google short url" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 */
	private void shortUrlMethod3()
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		//params.put(MarketplaceJuspayServicesConstants.CUSTOMERID, addCardRequest.getCustomerId());
		//params.put("customer_email", addCardRequest.getCustomerEmail());
		//params.put(MarketplaceJuspayServicesConstants.CARDNUMBER, String.valueOf(addCardRequest.getCardNumber()));
		//params.put("card_exp_year", String.valueOf(addCardRequest.getCardExpYear()));
		//params.put("card_exp_month", String.valueOf(addCardRequest.getCardExpMonth()));
		//params.put(MarketplaceJuspayServicesConstants.NAMEONCARD,
		//addCardRequest.getNameOnCard() != null ? addCardRequest.getNameOnCard() : "");
		params.put("longUrl", "http://10.10.79.33:9001/trackOrder/beforeTrack");

		final String serializedParams = serializeParams(params);
		final String url = "https://www.googleapis.com/urlshortener/v1/url";

		final String response = makeServiceCall(url, serializedParams);
		LOG.debug(response);
		//final JSONObject json = (JSONObject) JSONValue.parse(response);

		//final String cardToken = (String) json.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		//final String cardReference = (String) json.get(MarketplaceJuspayServicesConstants.CARDREF);
	}

	/**
	 *
	 */
	private void shortUrlMethod4()
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		//params.put(MarketplaceJuspayServicesConstants.CUSTOMERID, addCardRequest.getCustomerId());
		//params.put("customer_email", addCardRequest.getCustomerEmail());
		//params.put(MarketplaceJuspayServicesConstants.CARDNUMBER, String.valueOf(addCardRequest.getCardNumber()));
		//params.put("card_exp_year", String.valueOf(addCardRequest.getCardExpYear()));
		//params.put("card_exp_month", String.valueOf(addCardRequest.getCardExpMonth()));
		//params.put(MarketplaceJuspayServicesConstants.NAMEONCARD,
		//addCardRequest.getNameOnCard() != null ? addCardRequest.getNameOnCard() : "");
		params.put("longUrl", "http://localhost:9001/trackOrder/beforeTrack");

		final String serializedParams = serializeParams(params);
		final String url = "https://www.googleapis.com/urlshortener/v1/url?";

		final String response = makeServiceCall(url, serializedParams);
		LOG.debug(response);
		//final JSONObject json = (JSONObject) JSONValue.parse(response);

		//final String cardToken = (String) json.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		//final String cardReference = (String) json.get(MarketplaceJuspayServicesConstants.CARDREF);
	}

	private void shortUrlMethod5()
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		params.put("longUrl", "http://localhost:9001/trackOrder/beforeTrack");

		final String serializedParams = serializeParams(params);

		final String googleShortUrlApiKey = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
		final String googleShortUrlConnectUrl = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);

		final StringBuilder sb = new StringBuilder(googleShortUrlConnectUrl);
		sb.append("?key");
		sb.append("=" + googleShortUrlApiKey);

		final String url = new String(sb);
		//final String serializedParams = serializeParams(params);
		//final String url = "https://www.googleapis.com/urlshortener/v1/url?";
		final String response = makeServiceCall(url, serializedParams);
		LOG.debug(response);
		//final JSONObject json = (JSONObject) JSONValue.parse(response);

		//final String cardToken = (String) json.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		//final String cardReference = (String) json.get(MarketplaceJuspayServicesConstants.CARDREF);
	}

	private void shortUrlMethod6()
	{
		final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		final String longUrl = "http://localhost:9001/trackOrder/beforeTrack";
		final String input = "{\"longUrl\": \"" + longUrl + "\"}";
		params.put("input", input);
		final String serializedParams = serializeParams(params);

		final String googleShortUrlApiKey = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
		final String googleShortUrlConnectUrl = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);

		final StringBuilder sb = new StringBuilder(googleShortUrlConnectUrl);
		sb.append("?key");
		sb.append("=" + googleShortUrlApiKey);

		final String url = new String(sb);
		//final String serializedParams = serializeParams(params);
		//final String url = "https://www.googleapis.com/urlshortener/v1/url?";
		final String response = makeServiceCall(url, serializedParams);
		LOG.debug(response);
		//final JSONObject json = (JSONObject) JSONValue.parse(response);

		//final String cardToken = (String) json.get(MarketplaceJuspayServicesConstants.CARDTOKEN);
		//final String cardReference = (String) json.get(MarketplaceJuspayServicesConstants.CARDREF);
	}

	/**
	 * @param url
	 * @param serializedParams
	 * @return
	 */
	private String makeServiceCall(final String endPoint, final String encodedParams)
	{

		final String proxyEnableStatus = /*
													 * getConfigurationService().getConfiguration().getString(
													 * MarketplaceJuspayServicesConstants.PROXYENABLED);
													 */"true";
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();

		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("false"))
			{
				final String proxyName = /*
												  * getConfigurationService().getConfiguration().getString(
												  * MarketplaceJuspayServicesConstants.GENPROXY);
												  */"10.10.78.1";
				final int proxyPort = /*
											  * Integer.parseInt(getConfigurationService().getConfiguration().getString(
											  * MarketplaceJuspayServicesConstants.GENPROXYPORT));
											  */8080;
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
			//final String key = "AIzaSyBe-h03dMbCd4relUO57SSswRXdVpK9Jac";
			//String encodedKey = new String(Base64.encodeBase64(key.getBytes()));
			//encodedKey = encodedKey.replaceAll("\n", "");
			//connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			/*
			 * connection.setRequestProperty("version",
			 * getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			 */
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
			LOG.debug(buffer.toString());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}


		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("false"))
			{
				final String proxyName = /*
												  * getConfigurationService().getConfiguration().getString(
												  * MarketplaceJuspayServicesConstants.GENPROXY);
												  */"10.10.78.1";
				final int proxyPort = /*
											  * Integer.parseInt(getConfigurationService().getConfiguration().getString(
											  * MarketplaceJuspayServicesConstants.GENPROXYPORT));
											  */8080;
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
			final String key = "AIzaSyBe-h03dMbCd4relUO57SSswRXdVpK9Jac";
			String encodedKey = new String(Base64.encodeBase64(key.getBytes()));
			encodedKey = encodedKey.replaceAll("\n", "");
			connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			/*
			 * connection.setRequestProperty("version",
			 * getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			 */
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
			LOG.debug(buffer.toString());
		}
		catch (final Exception e)
		{
			//throw new AdapterException("Error with connection", e);
			e.printStackTrace();
		}


		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("false"))
			{
				final String proxyName = /*
												  * getConfigurationService().getConfiguration().getString(
												  * MarketplaceJuspayServicesConstants.GENPROXY);
												  */"10.10.78.1";
				final int proxyPort = /*
											  * Integer.parseInt(getConfigurationService().getConfiguration().getString(
											  * MarketplaceJuspayServicesConstants.GENPROXYPORT));
											  */8080;
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
			final String key = "AIzaSyCo2armGPIjkhMm-3Rlg69SqBSE_gVeIoc";
			String encodedKey = new String(Base64.encodeBase64(key.getBytes()));
			encodedKey = encodedKey.replaceAll("\n", "");
			connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			/*
			 * connection.setRequestProperty("version",
			 * getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			 */
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
			LOG.debug(buffer.toString());
		}
		catch (final Exception e)
		{
			//	throw new AdapterException("Error with connection", e);e.pr
			e.printStackTrace();
		}

		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("false"))
			{
				final String proxyName = /*
												  * getConfigurationService().getConfiguration().getString(
												  * MarketplaceJuspayServicesConstants.GENPROXY);
												  */"10.10.78.1";
				final int proxyPort = /*
											  * Integer.parseInt(getConfigurationService().getConfiguration().getString(
											  * MarketplaceJuspayServicesConstants.GENPROXYPORT));
											  */8080;
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
			final String key = "AIzaSyCo2armGPIjkhMm-3Rlg69SqBSE_gVeIoc";
			String encodedKey = new String(Base64.encodeBase64(key.getBytes()));
			encodedKey = encodedKey.replaceAll("\n", "");
			connection.setRequestProperty("Authorization", "Basic " + encodedKey);

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			/*
			 * connection.setRequestProperty("version",
			 * getConfigurationService().getConfiguration().getString(MarketplaceJuspayServicesConstants.VERSION));
			 */
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
			//throw new AdapterException("Error with connection", e);
			e.printStackTrace();
		}
		return proxyEnableStatus;
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
	 * @Description This method will get the short url report model from db
	 * @param orderCode
	 * @return TULShortUrlReportModel
	 */
	@Override
	public OrderShortUrlInfoModel getShortUrlReportModelByOrderId(final String orderCode)
	{
		return getOrderShortUrlDaoImpl().getShortUrlReportModelByOrderId(orderCode);
	}

	/**
	 * @Description This method will form the long URL for an order
	 * @param orderCode
	 * @return longUrl
	 */
	public String genearatelongUrl(final String orderCode)
	{
		final String longUrlFormat = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
		return longUrlFormat + "/" + orderCode;
	}

	/**
	 * @Description This method will give the Short Url reports generated between two dates
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel>
	 */
	@Override
	public List<OrderShortUrlInfoModel> getShortUrlReportModels(final Date fromDate, final Date toDate)
	{
		return getOrderShortUrlDaoImpl().getShortUrlReportModels(fromDate, toDate);
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
	 * @return the orderShortUrlDaoImpl
	 */
	public OrderShortUrlDao getOrderShortUrlDaoImpl()
	{
		return orderShortUrlDaoImpl;
	}

	/**
	 * @param orderShortUrlDaoImpl
	 *           the orderShortUrlDaoImpl to set
	 */
	public void setOrderShortUrlDaoImpl(final OrderShortUrlDao orderShortUrlDaoImpl)
	{
		this.orderShortUrlDaoImpl = orderShortUrlDaoImpl;
	}


}
