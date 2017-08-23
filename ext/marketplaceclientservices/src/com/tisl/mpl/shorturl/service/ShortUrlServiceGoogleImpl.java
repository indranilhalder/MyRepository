/**
 *
 */
package com.tisl.mpl.shorturl.service;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

	/**
	 *
	 */
	private static final String LONG_URL = "longUrl";
	private static final Logger LOG = Logger.getLogger(ShortUrlServiceGoogleImpl.class);
	private static boolean FORCE_DEBUG_LOG = true; //Added temporarily for debugging. Can be removed later.
	//No harm in leaving it here. Just change the value to false for PROD, and other higher envs.

	private static int connectionTimeout = 5 * 10000;
	private static int readTimeout = 5 * 1000;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "shortUrlReportDaoImpl")
	private OrderShortUrlDao orderShortUrlDaoImpl;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	/**
	 * @param url
	 * @param serializedParams
	 * @return
	 */
	@Override
	public String genearateShortURL(final String orderCode)
	{
		String shortUrl = null;
		try
		{
			//PROD fix - 2017-06-22. If Short URL has already been generated for an order and is available in DB.
			//then fetch that URL and return the same. Do not generate again, if short URL already exist.
			OrderShortUrlInfoModel shortUrlInfoModel = getShortUrlReportModelByOrderId(orderCode);
            if(null != shortUrlInfoModel && null != shortUrlInfoModel.getShortURL() && !shortUrlInfoModel.getShortURL().isEmpty()) {
				shortUrl = shortUrlInfoModel.getShortURL();
				if(LOG.isDebugEnabled()) {
					LOG.debug("Short URL  for Order id "+orderCode+ " is "+shortUrl);
				}
				return shortUrl;
			}
			LOG.info("Generating short url for order id :" + orderCode);

			final String googleAPIUrl = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);
			final String googleShortUrlApiKey = getConfigurationService().getConfiguration()
					.getString(MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
			//final String longUrl = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
			final String longUrl = genearateLongURL(orderCode);
			final StringBuilder sb = new StringBuilder(googleAPIUrl);
			sb.append("?key=");
			sb.append(googleShortUrlApiKey);
			if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
			{
				LOG.info("Google Api key :" + googleShortUrlApiKey + " and connecting url" + googleAPIUrl);
				LOG.info("Google Api key FINAL URL " + sb.toString());
				LOG.info("longUrl " + longUrl);
			}
			final String url = String.valueOf(sb);

			final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
			params.put(LONG_URL.intern(), String.valueOf(longUrl));
			final String response = getShortUrl(url, jsonString(params));

			final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);
			LOG.debug("JSON responce " + jsonResponse);
			if (null != jsonResponse)
			{
				shortUrl = (String) jsonResponse.get("id");
			}
			if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
			{
				LOG.info("Short URL " + shortUrl);
			}

			if (null != shortUrl)
			{
				//create TULShortUrlReport model to generate report ,later use and update this model when user clicks on short url
				try
				{
					final OrderShortUrlInfoModel shortUrlModel = getModelService().create(OrderShortUrlInfoModel.class);
					shortUrlModel.setOrderId(orderCode);
					shortUrlModel.setShortURL(shortUrl);
					shortUrlModel.setLongURL(longUrl);
					if (getShortUrlReportModelByOrderId(orderCode) == null)
					{
						LOG.debug("Creating TULShortUrlReportModel for Order " + orderCode);
						getModelService().save(shortUrlModel);
					}
				}
				catch (final ModelSavingException e)
				{
					LOG.error("ModelSavingException while saving TULShortUrlReportModel " + orderCode, e);
				}
				catch (final Exception e)
				{
					LOG.error("Error while saving TULShortUrlReportModel " + orderCode);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while getting the short Url for order id :" + orderCode);
			if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
			{
				LOG.info("Exception during Fetching Short URL: ", e);
			}
		}
		return shortUrl;

	}


	private String getShortUrl(final String endPoint, final String encodedParams)
	{
		final String proxyEnableStatus = getConfigurationService().getConfiguration()
				.getString(MarketplaceclientservicesConstants.PROXYENABLED);
		final String proxyAddress = getConfigurationService().getConfiguration()
				.getString(MarketplaceclientservicesConstants.GENPROXY);
		final String proxyPort = getConfigurationService().getConfiguration()
				.getString(MarketplaceclientservicesConstants.GENPROXYPORT);
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();

		if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
		{
			LOG.info("====>>> Proxy Details: " + proxyEnableStatus + " : " + proxyAddress + " : " + proxyPort);
			LOG.info("====>>> endPoint = " + endPoint);
			LOG.info("====>>> encodedParams = " + encodedParams);
		}

		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
				{
					LOG.info("Proxy is enabled and connecting to proxy address " + proxyAddress);
				}

				int proxyPortValue = 0;
				try
				{
					proxyPortValue = Integer.parseInt(proxyPort);
				}
				catch (final Exception e)
				{
					proxyPortValue = 80;
					LOG.error("Setting the default proxy port number :" + e.getMessage());
				}
				final SocketAddress addr = new InetSocketAddress(proxyAddress, proxyPortValue);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			else
			{
				if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
				{
					LOG.info("Proxy is not enabled. connecting without proxy ");
				}
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection();
			}


			final byte[] encodedParamsByteArray = encodedParams.getBytes();
			final int contentLength = encodedParamsByteArray.length;

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(encodedParamsByteArray);
			wr.flush();
			wr.close();

			// Check the HTTP response code from Google. Is response code is >= 400, then read from
			// Error stream, 400 represents HTTP error. Reading from Input Stream will throw IOException.

			// Read the response
			InputStream inputStream = null;
			final int respCode = connection.getResponseCode();
			if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
			{
				LOG.info("====>>> response Code: " + respCode);
			}
			if (connection.getResponseCode() >= 400)
			{
				if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
				{
					LOG.info("====>>> response Code: ERROR ------");
				}
				inputStream = connection.getErrorStream();
			}
			else
			{
				if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
				{
					LOG.info("====>>> response Code: SUCCESS ********");
				}
				inputStream = connection.getInputStream();
			}

			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
				{
					LOG.info("====>>> Google Response Line: " + line);
				}
				buffer.append(line);
			}
			if (LOG.isDebugEnabled() || FORCE_DEBUG_LOG)
			{
				LOG.info("====>>> Final complete Google Response : " + buffer.toString());
			}
			return buffer.toString();
		}
		catch (final Exception e)
		{
			LOG.error("ERROR while getting Short URL: " + e);
			LOG.info("Exception Stack During Short URL: ", e);
		}
		return buffer.toString();
	}

	public static String jsonString(final LinkedHashMap<String, String> params)
	{
		final JSONObject longUrl = new JSONObject();
		longUrl.put(LONG_URL.intern(), params.get(LONG_URL.intern()));
		return longUrl.toJSONString();
	}

	public String prepareLongUrlJSONString(final String longURL)
	{
		final JSONObject longUrlJSONObj = new JSONObject();
		longUrlJSONObj.put(LONG_URL.intern(), longURL);
		return longUrlJSONObj.toJSONString();
	}



	/**
	 * @Description This method will get the short url report model from db
	 * @param orderCode
	 * @return TULShortUrlReportModel
	 */
	@Override
	public OrderShortUrlInfoModel getShortUrlReportModelByOrderId(final String orderCode)
	{
		final OrderShortUrlInfoModel orderShortUrlInfoModel = getOrderShortUrlDaoImpl().getShortUrlReportModelByOrderId(orderCode);
		LOG.debug("Inside getShortUrlReportModelByOrderId**OrderCode**" + orderCode + "--**orderShortUrlInfoModel--"
				+ orderShortUrlInfoModel);
		//return getOrderShortUrlDaoImpl().getShortUrlReportModelByOrderId(orderCode);
		return orderShortUrlInfoModel;
	}

	/**
	 * @Description This method will form the long URL for an order
	 * @param orderCode
	 * @return longUrl
	 */
	public String genearateLongURL(final String orderCode)
	{
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final String site = currentBaseSite.getUid();
		final String longURLFormat;
		if (StringUtils.isNotBlank(site) && MarketplaceclientservicesConstants.LuxuryPrefix.equals(site))
		{
			longURLFormat = (String) getConfigurationService().getConfiguration()
					.getProperty(MarketplaceclientservicesConstants.LUX_TRACK_ORDER_LONG_URL_FORMAT);
		}
		else
		{
			longURLFormat = (String) getConfigurationService().getConfiguration()
					.getProperty(MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
		}
		return longURLFormat + "/" + orderCode;
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

	/*
	 * (non-Javadoc)
	 * @see com.tisl.mpl.shorturl.service.ShortUrlService#getShortUrlReportModels(java.util.Date, java.util.Date)
	 */
	@Override
	public List<OrderShortUrlInfoModel> getShortUrlReportModels(final Date fromDate, final Date toDate)
	{
		// YTODO Auto-generated method stub
		return getOrderShortUrlDaoImpl().getShortUrlReportModels(fromDate, toDate);
	}





}
