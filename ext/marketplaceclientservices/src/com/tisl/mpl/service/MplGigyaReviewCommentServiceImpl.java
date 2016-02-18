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

	public static final String TRUE_STATUS = "true";
	public static final String PROXY_SET_STATEMNT = "******************** PROXY SET ";
	public static final String PROXY_HOST_STATEMNT = "******************** PROXY HOST ";
	public static final String PROXY_PORT_STATEMNT = "******************** PROXY PORT ";

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
	 */
	@Override
	public boolean getReviewsByCategoryProductId(final String category, final String productId, final String customerUID)
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

		if (null != proxySet && proxySet.equalsIgnoreCase(TRUE_STATUS) && null != proxyHost && null != proxyPort)
		{
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			gsRequest.setProxy(proxy);
		}

		LOG.debug(PROXY_SET_STATEMNT + proxySet);
		LOG.debug(PROXY_HOST_STATEMNT + proxyHost);
		LOG.debug(PROXY_PORT_STATEMNT + proxyPort);

		final GSResponse gsResponse = gsRequest.send();
		if (gsResponse.getErrorCode() == 0)
		{
			try
			{
				final GSObject gsObj = new GSObject(gsResponse.getResponseText());
				return (gsObj.getInt("commentCount") == 0) ? false : true;//sonar fix for avoiding unnecessary boolean returns

			}
			catch (final Exception e)
			{
				LOG.error(MarketplacecclientservicesConstants.REVIEWS_CATEGORYID_EXCEPTION + e.getMessage());

			}
		}
		return true;
	}

	/**
	 * @Description: Gigya get comments for user id supplied
	 * @param customerUID
	 * @return List<GigyaProductReviewWsDTO>
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<GigyaProductReviewWsDTO> getReviewsByUID(final String customerUID)
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

		if (null != proxySet && proxySet.equalsIgnoreCase(TRUE_STATUS) && null != proxyHost && null != proxyPort)
		{
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			gsRequest.setProxy(proxy);
		}
		LOG.debug(PROXY_SET_STATEMNT + proxySet);
		LOG.debug(PROXY_HOST_STATEMNT + proxyHost);
		LOG.debug(PROXY_PORT_STATEMNT + proxyPort);
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

					if (checkItemKey(ratings, "Quality")) //removing unneccessary comparison of boolean objects(Sonar Fix)
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

					//	if (category.equals("Clothing"))
					if ("Clothing".equalsIgnoreCase(category) || "Footwear".equalsIgnoreCase(category)) //removing unneccessary comparison of boolean objects(Sonar Fix)
					{
						if (checkItemKey(ratings, "Fit"))
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
						if (checkItemKey(ratings, "Ease of use")) //removing unneccessary comparison of boolean objects(Sonar Fix)
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

					if (checkItemKey(ratings, "Value for Money")) //removing unneccessary comparison of boolean objects(Sonar Fix)
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

		}
		return customerReviewList;
	}

	/**
	 * @Description: edit review comments
	 * @param categoryID
	 *           ,streamID,commentID,commentText,commentTitle.ratings,UID
	 * @return String
	 */
	@Override
	public String editComment(final String categoryID, final String streamID, final String commentID, final String commentText,
			final String commentTitle, final String ratings, final String UID)
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

		if (null != proxySet && proxySet.equalsIgnoreCase(TRUE_STATUS) && null != proxyHost && null != proxyPort)
		{
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			proxy = new Proxy(Proxy.Type.HTTP, addr);
			gsRequestAllowEdit.setProxy(proxy);
		}
		LOG.debug(PROXY_SET_STATEMNT + proxySet);
		LOG.debug(PROXY_HOST_STATEMNT + proxyHost);
		LOG.debug(PROXY_PORT_STATEMNT + proxyPort);
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
			LOG.error(MarketplacecclientservicesConstants.REVIEWS_EDIT_EXCEPTION + e.getMessage());

		}
		return null;
	}

	/**
	 * @Description: Deletes a comment from GIGYA review
	 * @param categoryID
	 *           ,streamID,commentID
	 * @return String
	 */
	@Override
	public String deleteComment(final String categoryID, final String streamID, final String commentID)
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


		if (null != proxySet && proxySet.equalsIgnoreCase(TRUE_STATUS) && null != proxyHost && null != proxyPort)
		{
			final int proxyPortInt = Integer.parseInt(proxyPort);
			final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			gsRequest.setProxy(proxy);
		}
		LOG.debug(PROXY_SET_STATEMNT + proxySet);
		LOG.debug(PROXY_HOST_STATEMNT + proxyHost);
		LOG.debug(PROXY_PORT_STATEMNT + proxyPort);
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
			LOG.error(MarketplacecclientservicesConstants.REVIEWS_DELETE_EXCEPTION + e.getMessage());
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
		int years = 0;
		int months = 0;
		int days = 0;
		int updatedDays = 0;
		//create calendar object for review day
		final Calendar reviewDay = Calendar.getInstance();
		reviewDay.setTimeInMillis(commentDateObj.getTime());
		//create calendar object for current day
		final long currentTime = System.currentTimeMillis();
		final Calendar now = Calendar.getInstance();
		now.setTimeInMillis(currentTime);
		//Get difference between years
		years = now.get(Calendar.YEAR) - reviewDay.get(Calendar.YEAR);
		final int currMonth = now.get(Calendar.MONTH) + 1;
		final int reviewMonth = reviewDay.get(Calendar.MONTH) + 1;
		final String appendDays = "days ago";
		final String appendDay = "a day ago";
		//Get difference between months
		months = currMonth - reviewMonth;
		//if month difference is in negative then reduce years by one and calculate the number of months.
		if (months < 0)
		{
			years--;
			months = 12 - reviewMonth + currMonth;
			if (now.get(Calendar.DATE) < reviewDay.get(Calendar.DATE))
			{
				months--;
			}
		}
		else if (months == 0 && now.get(Calendar.DATE) < reviewDay.get(Calendar.DATE))
		{
			years--;
			months = 11;
		}
		//Calculate the days
		if (now.get(Calendar.DATE) > reviewDay.get(Calendar.DATE))
		{
			days = now.get(Calendar.DATE) - reviewDay.get(Calendar.DATE);
		}
		else if (now.get(Calendar.DATE) < reviewDay.get(Calendar.DATE))
		{
			final int today = now.get(Calendar.DAY_OF_MONTH);
			now.add(Calendar.MONTH, -1);
			days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - reviewDay.get(Calendar.DAY_OF_MONTH) + today;
		}
		else
		{
			days = 0;
			if (months == 12)
			{
				years++;
				months = 0;
			}
		}

		if (months == 0)
		{
			if (days == 0)
			{
				formatedDate = "today";
			}
			else if (days == 1)
			{
				formatedDate = appendDay;
			}
			else
			{
				final String fullDate = days + " " + appendDays + updatedDays + " " + "months ago" + years + " " + "years ago";
				LOG.debug(">>fullDate>> " + fullDate);
				formatedDate = (days + " " + appendDays);
			}
		}
		else
		{
			updatedDays = (months * 30);
			final String fullDate = days + " " + appendDays + updatedDays + " " + "months ago" + years + " " + "years ago";
			LOG.debug(">>fullDate>> " + fullDate);
			formatedDate = (days + updatedDays) + " " + appendDays;
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
