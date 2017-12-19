/**
 *
 */
package com.tisl.mpl.core.cronjobs.gigya;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
import com.tisl.mpl.core.model.GigyaResponseCaptureModel;


/**
 * @author TCS
 *
 */
public class GigyaDataPullUtility extends StdSerializer<Object>
{
	private final static Logger LOG = Logger.getLogger(GigyaDataPullUtility.class.getName());
	private static final String RATING_PROXY_ENABLED = "proxy.enabled".intern();
	private static final String RATING_SECRETKEY = "gigya.secretkey".intern();
	private static final String RATING_APIKEY = "gigya.apikey".intern();
	private static final String REVIEWS_RATING_EXCEPTION = "Gigya 'rating and review count' error";
	private static final String CATEGORY_ID = "categoryId".intern();
	private static final String STREAM_ID = "streamId".intern();
	private static final String STREAM_INFO = "streamInfo".intern();
	private static final String INCLUDE_RATING_DETAILS = "includeRatingDetails".intern();
	private static final String TRUE_STATUS = "true";
	private static final String RATING_PROXY_PORT = "proxy.port".intern();
	private static final String RATING_PROXY = "proxy.address".intern();
	private static final String AVG_RATINGS = "avgRatings";
	private static final String OVERALL = "_overall";
	private static final String INCLUDE_STREAM_INFO = "includeStreamInfo".intern();
	private static final String INCLUDE_USER_OPTIONS = "includeUserOptions".intern();
	private static final String INCLUDE_USER_HIGHLIGHTING = "includeUserHighlighting".intern();
	private static final String INCLUDE_UID = "includeUID".intern();

	@Autowired
	private ConfigurationService configService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Resource(name = "modelService")
	private ModelService modelService;

	public void pullDataFromGigya(final List<String> product)
	{
		if (CollectionUtils.isNotEmpty(product))
		{
			for (final String prod : product)
			{
				final List<String> codeCategoryList = Arrays.asList(prod.split(":"));
				final String listingId = codeCategoryList.get(0);
				final String categoryType = codeCategoryList.get(1);
				callGigyaStreamInfo(listingId, categoryType);
			}
		}
	}

	/**
	 * @param listingId
	 * @param categoryType
	 */
	private void callGigyaComments(final String listingId, final String categoryType, final GigyaResponseCaptureModel gModel)
	{
		LOG.debug("calling comments.getComments with product : " + listingId + " and categoryType : " + categoryType);

		final String proxySet = configService.getConfiguration().getString(RATING_PROXY_ENABLED);
		//final String secretKey = configService.getConfiguration().getString(RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(RATING_APIKEY);

		//		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		//		final String site = currentBaseSite.getUid();
		//
		//		if (MarketplacecclientservicesConstants.LUXURYPREFIX.equals(site))
		//		{
		//			secretKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.LUXURY_RATING_SECRETKEY);
		//			apiKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.LUXURY_RATING_APIKEY);
		//
		//		}

		final String method = "comments.getComments";
		final ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializerProvider().setNullKeySerializer(new GigyaDataPullUtility());
		try
		{
			//final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);
			final GSRequest gsRequest = new GSRequest(apiKey, method);
			gsRequest.setParam(CATEGORY_ID, categoryType);
			gsRequest.setParam(STREAM_ID, listingId);
			gsRequest.setParam(INCLUDE_STREAM_INFO, TRUE_STATUS);
			gsRequest.setParam(INCLUDE_USER_OPTIONS, TRUE_STATUS);
			gsRequest.setParam(INCLUDE_USER_HIGHLIGHTING, TRUE_STATUS);
			gsRequest.setParam(INCLUDE_UID, TRUE_STATUS);

			if (TRUE_STATUS.equalsIgnoreCase(proxySet))
			{
				final Proxy proxy = getConfiguredProxyForGigya();
				if (null != proxy)
				{
					gsRequest.setProxy(proxy);
				}
			}
			final GSResponse gsResponse = gsRequest.send();
			if (gsResponse.getErrorCode() == 0)
			{
				//		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
				final String jsonInString = mapper.writeValueAsString(gsResponse);
				gModel.setReviewDetailResponse(jsonInString);
				modelService.save(gModel);

			}
			else
			{
				LOG.debug("**********ERROR MESSAGE****" + gsResponse.getErrorMessage());
				LOG.debug("**********ERROR CODE********" + gsResponse.getErrorCode());
			}
		}
		catch (final Exception e)
		{
			LOG.error(REVIEWS_RATING_EXCEPTION + e.getMessage());

		}
	}

	/**
	 * @param listingId
	 * @param categoryType
	 */
	private void callGigyaStreamInfo(final String listingId, final String categoryType)
	{
		LOG.debug("calling comments.getStreamInfo with product : " + listingId + " and categoryType : " + categoryType);

		final String proxySet = configService.getConfiguration().getString(RATING_PROXY_ENABLED);
		//final String secretKey = configService.getConfiguration().getString(RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(RATING_APIKEY);

		final String method = "comments.getStreamInfo";
		final ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializerProvider().setNullKeySerializer(new GigyaDataPullUtility());
		String overAllRating = "";

		try
		{
			//final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);
			final GSRequest gsRequest = new GSRequest(apiKey, method);
			gsRequest.setParam(CATEGORY_ID, categoryType);
			gsRequest.setParam(STREAM_ID, listingId);
			gsRequest.setParam(STREAM_INFO, TRUE_STATUS);
			gsRequest.setParam(INCLUDE_RATING_DETAILS, TRUE_STATUS);

			if (TRUE_STATUS.equalsIgnoreCase(proxySet))
			{
				final Proxy proxy = getConfiguredProxyForGigya();
				if (null != proxy)
				{
					gsRequest.setProxy(proxy);
				}
			}
			final GSResponse gsResponse = gsRequest.send();
			if (gsResponse.getErrorCode() == 0)
			{
				final GSObject gsObj = new GSObject(gsResponse.getResponseText());

				final GSObject gsCommentObject = gsObj.getObject(STREAM_INFO);
				final GSObject gsRatingObject = gsCommentObject.getObject(AVG_RATINGS);
				overAllRating = gsRatingObject.getString(OVERALL);

				if (StringUtils.isNotEmpty(overAllRating) && !StringUtils.equals(overAllRating, "0.0"))
				{
					final GigyaResponseCaptureModel gModel = new GigyaResponseCaptureModel();
					gModel.setListingId(listingId);
					//	mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
					final String jsonInString = mapper.writeValueAsString(gsResponse);
					gModel.setReviewSummaryResponse(jsonInString);
					modelService.save(gModel);

					callGigyaComments(listingId, categoryType, gModel);
				}
				else
				{
					LOG.debug("No review comments and rating present");
				}
			}
			else
			{
				LOG.debug("**********ERROR MESSAGE****" + gsResponse.getErrorMessage());
				LOG.debug("**********ERROR CODE********" + gsResponse.getErrorCode());
			}
		}
		catch (final Exception e)
		{
			LOG.error(REVIEWS_RATING_EXCEPTION + e.getMessage());

		}
	}

	/**
	 * @desc This method uses the configured proxy and returns a proxy object
	 * @return Proxy
	 */
	private Proxy getConfiguredProxyForGigya()
	{
		final String proxyPort = configService.getConfiguration().getString(RATING_PROXY_PORT);
		final String proxyHost = configService.getConfiguration().getString(RATING_PROXY);
		Proxy proxy = null;
		if (null != proxyHost && null != proxyPort)
		{
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			proxy = new Proxy(Proxy.Type.HTTP, addr);
		}
		LOG.debug("Proxy host----" + proxyHost);
		LOG.debug("Proxy port----" + proxyPort);
		return proxy;
	}

	public GigyaDataPullUtility()
	{
		this(null);
	}

	public GigyaDataPullUtility(final Class<Object> t)
	{
		super(t);
	}

	@Override
	public void serialize(final Object nullKey, final JsonGenerator jsonGenerator, final SerializerProvider unused)
			throws IOException, JsonProcessingException
	{
		jsonGenerator.writeFieldName("");
	}

}
