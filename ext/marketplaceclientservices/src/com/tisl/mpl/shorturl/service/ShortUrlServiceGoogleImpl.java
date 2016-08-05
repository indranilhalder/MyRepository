/**
 *
 */
package com.tisl.mpl.shorturl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.gigya.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import com.tisl.mpl.core.model.TULShortUrlReportModel;
import com.tisl.mpl.shorturl.report.dao.ShortUrlReportDao;


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
	private ShortUrlReportDao shortUrlReportDaoImpl;

	/**
	 * @Description Generates short URL for a given order code
	 * @param orderCode
	 * @return shortUrl
	 */
	@Override
	public String genearateShorterURL(final String orderCode)
	{
		try
		{
			final String longUrl = this.genearateLongURL(orderCode);

			LOG.info("--OrderCode-- " + orderCode + "---Long URL-- " + longUrl);

			final String googleShortUrlApiKey = (String) getConfigurationService().getConfiguration().getProperty(
					MarketplaceclientservicesConstants.GOOGLE_SHORT_URL_API_KEY);
			final String googleShortUrlConnectUrl = (String) getConfigurationService().getConfiguration().getProperty(
					MarketplaceclientservicesConstants.GOOGLE_API_SHORT_URL);
			LOG.debug("API Key " + googleShortUrlApiKey + "Connect URL " + googleShortUrlConnectUrl);

			final StringBuilder sb = new StringBuilder(googleShortUrlConnectUrl);
			sb.append("?key");
			sb.append("=" + googleShortUrlApiKey);

			final String url = new String(sb);
			LOG.debug("Before Connecting to Google URL -- " + sb);

			final Client client = Client.create();
			//prepare input and send in body
			final String input = "{\"longUrl\": \"" + longUrl + "\"}";
			LOG.debug("long url--" + longUrl);
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
			LOG.debug("Output from google Server .... \n" + output);
			final JSONObject obj = new JSONObject(output);
			final String shortUrl = (String) obj.get("id");
			//create TULShortUrlReport model to generate report ,later use and update this model when user clicks on short url
			try
			{
				final TULShortUrlReportModel shortUrlModel = getModelService().create(TULShortUrlReportModel.class);
				shortUrlModel.setOrderId(orderCode);
				shortUrlModel.setShortURL(shortUrl);
				shortUrlModel.setLongURL(longUrl);
				getModelService().save(shortUrlModel);
			}
			catch (final Exception e)
			{
				LOG.error("Error while saving TULShortUrlReportModel " + orderCode);
				e.printStackTrace();
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
	 * @Description This method will get the short url report model from db
	 * @param orderCode
	 * @return TULShortUrlReportModel
	 */
	@Override
	public TULShortUrlReportModel getShortUrlReportModelByOrderId(final String orderCode)
	{
		return getShortUrlReportDaoImpl().getShortUrlReportModelByOrderId(orderCode);
	}

	/**
	 * @Description This method will form the long URL from local.properties and ordercode
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
	 * @return the shortUrlReportDaoImpl
	 */
	public ShortUrlReportDao getShortUrlReportDaoImpl()
	{
		return shortUrlReportDaoImpl;
	}

	/**
	 * @param shortUrlReportDaoImpl
	 *           the shortUrlReportDaoImpl to set
	 */
	public void setShortUrlReportDaoImpl(final ShortUrlReportDao shortUrlReportDaoImpl)
	{
		this.shortUrlReportDaoImpl = shortUrlReportDaoImpl;
	}
}
