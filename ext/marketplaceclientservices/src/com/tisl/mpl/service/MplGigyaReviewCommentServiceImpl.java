/**
 *
 */
package com.tisl.mpl.service;


import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gigya.socialize.GSArray;
import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public class MplGigyaReviewCommentServiceImpl implements MplGigyaReviewCommentService
{

	private final static Logger LOG = Logger.getLogger(MplGigyaReviewCommentServiceImpl.class.getName());
	@Autowired
	private ConfigurationService configService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductFacade productFacade;

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the configService
	 */
	public ConfigurationService getConfigService()
	{
		return configService;
	}

	/**
	 * @param configService
	 *           the configService to set
	 */
	public void setConfigService(final ConfigurationService configService)
	{
		this.configService = configService;
	}

	/**
	 * @Description: structure formation using category productId username and getting JSON from GIGYA
	 * @param category
	 *           ,productId,customerUID
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean getReviewsByCategoryProductId(final String category, final String productId, final String customerUID)
			throws Exception
	{
		final String proxyPort = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY_PORT);
		final String proxySet = configService.getConfiguration()
				.getString(MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);
		final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);

		final String secretKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_APIKEY);

		final String method = "comments.getUserComments";
		final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);

		gsRequest.setParam(MarketplacecclientservicesConstants.CATEGORY_ID, category);
		gsRequest.setParam(MarketplacecclientservicesConstants.STREAM_ID, productId);
		gsRequest.setParam(MarketplacecclientservicesConstants.SENDER_UID, customerUID);

		if (null != proxySet && proxySet.equalsIgnoreCase("true"))
		{
			if (null != proxyHost && null != proxyPort)
			{
				final int proxyPortInt = Integer.parseInt(proxyPort);
				final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				gsRequest.setProxy(proxy);
			}
		}

		LOG.debug("******************** PROXY SET " + proxySet);
		LOG.debug("******************** PROXY HOST " + proxyHost);
		LOG.debug("******************** PROXY PORT " + proxyPort);

		final GSResponse gsResponse = gsRequest.send();
		if (gsResponse.getErrorCode() == 0)
		{
			try
			{
				final GSObject gsObj = new GSObject(gsResponse.getResponseText());
				if ((gsObj.getInt("commentCount") == 0))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (final Exception e)
			{
				LOG.error(MarketplacecclientservicesConstants.REVIEWS_CATEGORYID_EXCEPTION + e.getMessage());
				throw e;
			}
		}
		return true;
	}

	/**
	 * @Description: Gigya get comments for user id supplied
	 * @param customerUID
	 * @return List<GigyaProductReviewWsDTO>
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<GigyaProductReviewWsDTO> getReviewsByUID(final String customerUID) throws Exception
	{
		final String proxyPort = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY_PORT);
		final String proxySet = configService.getConfiguration()
				.getString(MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);
		final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);

		final String secretKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_APIKEY);

		final String method = "comments.getUserComments";
		final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);
		gsRequest.setParam(MarketplacecclientservicesConstants.SENDER_UID, customerUID);

		if (null != proxySet && proxySet.equalsIgnoreCase("true"))
		{
			if (null != proxyHost && null != proxyPort)
			{
				final int proxyPortInt = Integer.parseInt(proxyPort);
				final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				gsRequest.setProxy(proxy);
			}
		}
		LOG.debug("******************** PROXY SET " + proxySet);
		LOG.debug("******************** PROXY HOST " + proxyHost);
		LOG.debug("******************** PROXY PORT " + proxyPort);
		final List<GigyaProductReviewWsDTO> customerReviewList = new ArrayList<GigyaProductReviewWsDTO>();
		try
		{
			final GSResponse gsResponse = gsRequest.send();
			if (gsResponse.getErrorCode() == 0)
			{
				final GSObject gsObj = new GSObject(gsResponse.getResponseText());
				final GSArray commentsJson = gsObj.getArray("comments");

				for (int i = 0; i < commentsJson.length(); i++)
				{
					ProductData productData = null;
					final GigyaProductReviewWsDTO reviewDTO = new GigyaProductReviewWsDTO();
					final GSObject gsCommentObject = commentsJson.getObject(i);
					final String category = gsCommentObject.getString("categoryId");
					final GSObject ratings = gsCommentObject.getObject("ratings");

					if (ratings.getInt("_overall") != 0)
					{
						double overAllRatingInt = ratings.getDouble("_overall");
						overAllRatingInt = (overAllRatingInt / 5) * 100;
						overAllRatingInt = Math.ceil(overAllRatingInt);
						reviewDTO.setOverAllRating(String.valueOf(overAllRatingInt));
					}

					if (checkItemKey(ratings, "Quality") == true)
					{
						double qualityInt = ratings.getDouble("Quality");
						qualityInt = (qualityInt / 5) * 100;
						qualityInt = Math.ceil(qualityInt);
						reviewDTO.setQualityRating(String.valueOf(qualityInt));
					}
					else
					{
						reviewDTO.setQualityRating(String.valueOf(0));
					}

					if (category.equals("Clothing"))
					{
						if (checkItemKey(ratings, "Fit") == true)
						{
							double fitInt = ratings.getDouble("Fit");
							fitInt = (fitInt / 5) * 100;
							fitInt = Math.ceil(fitInt);
							reviewDTO.setFitRating(String.valueOf(fitInt));
						}
						else
						{
							reviewDTO.setFitRating(String.valueOf(0));
						}
					}
					else
					{
						if (checkItemKey(ratings, "Ease of use") == true)
						{
							double easeOfUseInt = ratings.getDouble("Ease of use");
							easeOfUseInt = (easeOfUseInt / 5) * 100;
							easeOfUseInt = Math.ceil(easeOfUseInt);
							reviewDTO.setEaseOfUse(String.valueOf(easeOfUseInt));
						}
						else
						{
							reviewDTO.setEaseOfUse(String.valueOf(0));
						}
					}

					if (checkItemKey(ratings, "Value for Money") == true)
					{

						double valueForMoneyInt = ratings.getDouble("Value for Money");
						valueForMoneyInt = (valueForMoneyInt / 5) * 100;
						valueForMoneyInt = Math.ceil(valueForMoneyInt);
						reviewDTO.setValueForMoneyRating(String.valueOf(valueForMoneyInt));
					}
					else
					{
						reviewDTO.setValueForMoneyRating(String.valueOf(0));
					}

					reviewDTO.setCommentId(gsCommentObject.getString("ID"));
					reviewDTO.setCommentTitle(gsCommentObject.getString("commentTitle"));
					reviewDTO.setCommentText(gsCommentObject.getString("commentText"));

					final long commentTimeStamp = gsCommentObject.getLong("timestamp");
					final Date commentDateObj = new Date(commentTimeStamp);
					final String reviewDate = getDate(commentDateObj);
					reviewDTO.setReviewDate(reviewDate);
					reviewDTO.setCommentDate(commentDateObj);

					if (null != gsCommentObject.getString("streamId"))
					{
						final ProductModel productModel = productService.getProductForCode(gsCommentObject.getString("streamId"));

						productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
								ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES,
								ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL));
					}

					reviewDTO.setProductData(productData);
					reviewDTO.setRootCategory(category);
					customerReviewList.add(reviewDTO);
				}

				LOG.debug(commentsJson);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.REVIEWS_UID_EXCEPTION + e.getMessage());
			throw e;

		}
		return customerReviewList;
	}

	/**
	 * @Description: edit review comments
	 * @param categoryID
	 *           ,streamID,commentID,commentText,commentTitle.ratings,UID
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String editComment(final String categoryID, final String streamID, final String commentID, final String commentText,
			final String commentTitle, final String ratings, final String UID) throws Exception
	{
		final String proxyPort = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY_PORT);
		final String proxySet = configService.getConfiguration()
				.getString(MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);
		final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);

		final String secretKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_APIKEY);

		final String setCategoryInfoMethod = "comments.setCategoryInfo";
		final GSRequest gsRequestAllowEdit = new GSRequest(apiKey, secretKey, setCategoryInfoMethod);
		gsRequestAllowEdit.setParam(MarketplacecclientservicesConstants.CATEGORY_ID, categoryID);
		gsRequestAllowEdit.setParam("categorySettings", "{userEditComment : true}");
		Proxy proxy = null;

		if (null != proxySet && proxySet.equalsIgnoreCase("true"))
		{
			if (null != proxyHost && null != proxyPort)
			{
				final int proxyPortInt = Integer.parseInt(proxyPort);
				final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
				proxy = new Proxy(Proxy.Type.HTTP, addr);
				gsRequestAllowEdit.setProxy(proxy);
			}
		}
		LOG.debug("******************** PROXY SET " + proxySet);
		LOG.debug("******************** PROXY HOST " + proxyHost);
		LOG.debug("******************** PROXY PORT " + proxyPort);
		try
		{
			final GSResponse allowEdit = gsRequestAllowEdit.send();

			if (allowEdit.getErrorCode() == 0)
			{
				final GSObject gsObj = new GSObject(allowEdit.getResponseText());
				if (gsObj.getString("statusReason").equals("OK"))
				{

					final String method = "comments.updateComment";
					final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);
					if (null != proxy)
					{
						gsRequest.setProxy(proxy);
					}
					gsRequest.setParam(MarketplacecclientservicesConstants.CATEGORY_ID, categoryID);
					gsRequest.setParam(MarketplacecclientservicesConstants.STREAM_ID, streamID);
					gsRequest.setParam(MarketplacecclientservicesConstants.COMMENT_ID, commentID);
					gsRequest.setParam(MarketplacecclientservicesConstants.COMMENT_TEXT, commentText);
					gsRequest.setParam(MarketplacecclientservicesConstants.COMMENT_TITLE, commentTitle);
					gsRequest.setParam(MarketplacecclientservicesConstants.RATINGS, ratings);
					gsRequest.setParam(MarketplacecclientservicesConstants.UID, UID);

					final GSResponse gsResponse = gsRequest.send();
					if (gsResponse.getErrorCode() == 0)
					{
						final GSObject gsObjToEdit = new GSObject(gsResponse.getResponseText());
						return gsObjToEdit.getString("statusReason");
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw e;

		}
		return null;
	}

	/**
	 * @Description: Deletes a comment from GIGYA review
	 * @param categoryID
	 *           ,streamID,commentID
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String deleteComment(final String categoryID, final String streamID, final String commentID) throws Exception
	{
		final String proxyPort = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY_PORT);
		final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);
		final String proxySet = configService.getConfiguration()
				.getString(MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);

		final String secretKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_SECRETKEY);
		final String apiKey = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_APIKEY);

		final String method = "comments.deleteComment";
		final GSRequest gsRequest = new GSRequest(apiKey, secretKey, method);
		gsRequest.setParam(MarketplacecclientservicesConstants.CATEGORY_ID, categoryID);
		gsRequest.setParam(MarketplacecclientservicesConstants.STREAM_ID, streamID);
		gsRequest.setParam(MarketplacecclientservicesConstants.COMMENT_ID, commentID);


		if (null != proxySet && proxySet.equalsIgnoreCase("true"))
		{
			if (null != proxyHost && null != proxyPort)
			{
				final int proxyPortInt = Integer.parseInt(proxyPort);
				final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				gsRequest.setProxy(proxy);
			}
		}
		LOG.debug("******************** PROXY SET " + proxySet);
		LOG.debug("******************** PROXY HOST " + proxyHost);
		LOG.debug("******************** PROXY PORT " + proxyPort);
		try
		{
			final GSResponse gsResponse = gsRequest.send();
			if (gsResponse.getErrorCode() == 0)
			{
				final GSObject gsObj = new GSObject(gsResponse.getResponseText());
				return gsObj.getString("statusReason");
			}
		}
		catch (final Exception e)
		{
			throw e;
		}
		return null;
	}

	/**
	 * @Description: filtering date from GIGYA review
	 * @param commentDateObj
	 * @return String
	 */
	@Override
	public String getDate(final Date commentDateObj)
	{
		String formatedDate = "";
		try
		{
			final Date date = new Date();
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			final int thisMonth = cal.get(Calendar.MONTH);
			final int thisDay = cal.get(Calendar.DAY_OF_MONTH);
			cal.setTime(commentDateObj);
			final int cYear = cal.get(Calendar.YEAR);
			final int cMonth = cal.get(Calendar.MONTH);
			final int cDay = cal.get(Calendar.DAY_OF_MONTH);
			final int cHour = cal.get(Calendar.HOUR_OF_DAY);
			final int cMinute = cal.get(Calendar.MINUTE);
			LOG.debug(">>>>>>>>>date>>>>>>" + commentDateObj);
			LOG.debug(">>>>>>>>>minute>>>>>>" + cMinute);
			LOG.debug(">>>>>>>>>year>>>>>>" + cHour);
			LOG.debug(">>>>>>>>>day>>>>>>" + cDay);
			LOG.debug(">>>>>>>>>month>>>>>>" + cMonth);
			LOG.debug(">>>>>>>>>year>>>>>>" + cYear);
			final int dayDiff = thisDay - cDay;
			if (dayDiff == 0)
			{
				formatedDate = "today";
			}
			else if (dayDiff == 1)
			{
				formatedDate = String.valueOf(dayDiff) + " " + "day ago";
			}
			else if (dayDiff > 1 && dayDiff < 30)
			{
				LOG.debug(">>>>>>>>>date diffrence>>>>>>" + dayDiff + " " + "day ago");
				formatedDate = String.valueOf(dayDiff) + " " + "days ago";
			}

			else if (dayDiff == 30 || dayDiff == 31)
			{
				final int monthDiff = thisMonth - cMonth;
				formatedDate = String.valueOf(monthDiff) + " " + " month ago";
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
		}
		return formatedDate;
	}

	/**
	 * @Description: checks if any key in GSobject is missing or not
	 * @param ratings
	 *           ,key
	 * @return boolean
	 * @throws GSKeyNotFoundException
	 *            ,NullPointerException
	 */

	@Override
	public boolean checkItemKey(final GSObject ratings, final String key)
	{
		try
		{
			ratings.get(key);
			return true;
		}
		catch (final GSKeyNotFoundException e)
		{
			return false;
		}
		catch (final NullPointerException e)
		{
			return false;
		}

	}

}
