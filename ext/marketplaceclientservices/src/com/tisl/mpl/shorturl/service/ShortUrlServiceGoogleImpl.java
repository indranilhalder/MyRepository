/**
 *
 */
package com.tisl.mpl.shorturl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;




import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
	private int connectionTimeout = 5 * 10000;
	private int readTimeout = 5 * 1000;
	private String baseUrl;
	private String key;
	private String merchantId;
	private String environmentSet;

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
		final String proxyEnableStatus = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.PROXYENABLED);
		final String googleAPIUrl = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);
		final String googleShortUrlApiKey = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
		final String longUrl = getConfigurationService().getConfiguration().getString(MarketplaceclientservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(googleAPIUrl+"?key="+googleShortUrlApiKey);

		StringEntity input = new StringEntity(prepareLongUrlJSONString(longUrl+"/"+orderCode),"UTF-8");
		input.setContentType("application/json; charset=UTF-8");
		postRequest.setEntity(input);
		
		
		if(proxyEnableStatus.equalsIgnoreCase("true")){
			LOG.debug("Proxy is enaled while calling google API for short url service");
			HttpHost proxy =new HttpHost(getConfigurationService().getConfiguration().getString(
					MarketplaceclientservicesConstants.GENPROXY), Integer.parseInt(getConfigurationService().getConfiguration().getString(
							MarketplaceclientservicesConstants.GENPROXYPORT)));
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
			
		}
		
		HttpResponse response=null;
		String output = null;
		try
		{
			response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				LOG.debug(output);
			}

		}
		catch (IOException e)
		{
			LOG.error("Exception while getting the short url for order id "+orderCode);
			e.printStackTrace();
		}

		return output;
	}
	
	public static String prepareLongUrlJSONString(String longURL) {
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
