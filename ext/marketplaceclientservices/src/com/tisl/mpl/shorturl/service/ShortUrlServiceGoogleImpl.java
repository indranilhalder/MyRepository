/**
 *
 */
package com.tisl.mpl.shorturl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

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

	private static final Logger LOG = Logger.getLogger(ShortUrlServiceGoogleImpl.class);
	private static int connectionTimeout = 5 * 10000;
	private static int readTimeout = 5 * 1000;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "shortUrlReportDaoImpl")
	private OrderShortUrlDao orderShortUrlDaoImpl;
	

	/**
	 * @param url
	 * @param serializedParams
	 * @return
	 */
	@Override
	public String genearateShortURL(final String orderCode)
	{
		String shortUrl = null;
		try {
			LOG.info("Generating short url for order id :" + orderCode);
			final String googleAPIUrl = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);
			final String googleShortUrlApiKey = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
			final String longUrl = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
			
			StringBuilder sb = new StringBuilder(googleAPIUrl);
			sb.append("?key=");
			sb.append(googleShortUrlApiKey);
			if(LOG.isDebugEnabled()) {
				LOG.debug("Google Api key :"+googleShortUrlApiKey+" and connecting url"+googleAPIUrl);
			}
			String url = String.valueOf(sb); 
			
			final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
			params.put("longUrl", String.valueOf(longUrl));
			final String response = getShortUrl(url, jsonString(params));

			final JSONObject jsonResponse = (JSONObject) JSONValue.parse(response);
			LOG.debug("JSON responce "+jsonResponse);
			if(null != jsonResponse) {
				shortUrl = (String) jsonResponse.get("id");
			}
		}catch(Exception e) {
			LOG.error("Exception while getting the short Url for order id :"+orderCode);
		}
		return shortUrl;
		
	}
	
	private  String getShortUrl(final String endPoint, String encodedParams)
	{
		final String proxyEnableStatus = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.PROXYENABLED);
		final String proxyAddress = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GENPROXY);
		final String proxyPort = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GENPROXYPORT);
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();

		try
		{
			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
		      LOG.debug("Proxy is enabled and connecting to proxy address "+proxyAddress);
				final SocketAddress addr = new InetSocketAddress(proxyAddress, Integer.valueOf(proxyPort).intValue());
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			else
			{
				LOG.warn("Proxy is not enabled ");
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection();
			}

			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
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
			LOG.debug("output from server:"+buffer.toString());
			return buffer.toString();
			
		}
		catch (final Exception e)
		{
			System.out.println("ERROR"+e.getMessage());
		}
		return buffer.toString();
	}
	
	public static String jsonString(LinkedHashMap<String, String> params) {
        JSONObject longUrl = new JSONObject();
        longUrl.put("longUrl", params.get("longUrl"));
        return longUrl.toJSONString();
    }
	
	public String prepareLongUrlJSONString(String longURL) {
	      JSONObject longUrlJSONObj = new JSONObject();
	      longUrlJSONObj.put("longUrl", longURL);
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
		return getOrderShortUrlDaoImpl().getShortUrlReportModelByOrderId(orderCode);
	}

	/**
	 * @Description This method will form the long URL for an order
	 * @param orderCode
	 * @return longUrl
	 */
	public String genearateLongURL(final String orderCode)
	{
		final String longURLFormat = (String) getConfigurationService().getConfiguration().getProperty(
				MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
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
	 * @param orderShortUrlDaoImpl the orderShortUrlDaoImpl to set
	 */
	public void setOrderShortUrlDaoImpl(OrderShortUrlDao orderShortUrlDaoImpl)
	{
		this.orderShortUrlDaoImpl = orderShortUrlDaoImpl;
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.shorturl.service.ShortUrlService#getShortUrlReportModels(java.util.Date, java.util.Date)
	 */
	@Override
	public List<OrderShortUrlInfoModel> getShortUrlReportModels(Date fromDate, Date toDate)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	
	
	
	
}
