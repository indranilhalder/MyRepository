/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.media.MediaModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplAdvancedCategoryCarouselComponentModel;
import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplCategoryCarouselComponentModel;
import com.tisl.mpl.core.model.MplImageCategoryComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ImageCarouselComponentModel;
import com.tisl.mpl.model.cms.components.MplSequentialBannerComponentModel;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author TCS
 *
 */
public class HomepageComponentServiceImpl implements HomepageComponentService
{

	@Autowired
	private CommerceCategoryService commerceCategoryService;

	//store url change
	//private static final String MISSING_IMAGE_URL = "/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	//private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);

	//private static final String SEQUENCE_NUMBER = "SequenceNumber";
	//private static final String SEQUENCE_NUMBER_STAYQUED = "SeqNumForStayQued";
	private static final String TITLE = "title";
	private static final String ICID = "icid";

	private static final Logger LOG = Logger.getLogger(HomepageComponentServiceImpl.class);


	@Override
	public JSONObject getBestPicksJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject bestPicks = new JSONObject();
		if (CollectionUtils.isNotEmpty(contentSlot.getCmsComponents()))
		{
			components = contentSlot.getCmsComponents();
		}

		for (final AbstractCMSComponentModel component : components)
		{
			if (component instanceof ImageCarouselComponentModel)
			{
				final ImageCarouselComponentModel bestPickCarouselComponent = (ImageCarouselComponentModel) component;
				String title = "";
				if (StringUtils.isNotEmpty(bestPickCarouselComponent.getTitle()))
				{
					title = bestPickCarouselComponent.getTitle();
				}

				//Added for making the button link cmsmanaged
				if (StringUtils.isNotEmpty(bestPickCarouselComponent.getButtonText()))
				{
					bestPicks.put("buttonText", bestPickCarouselComponent.getButtonText());
				}
				if (StringUtils.isNotEmpty(bestPickCarouselComponent.getButtonLink()))
				{
					bestPicks.put("buttonLink", bestPickCarouselComponent.getButtonLink());
				}

				bestPicks.put("title", title);

				final JSONArray subComponentJsonArray = new JSONArray();
				if (CollectionUtils.isNotEmpty(bestPickCarouselComponent.getCollectionItems()))
				{
					String imageURL = "";
					String text = "";
					String linkUrl = "#";

					for (final CMSMediaParagraphComponentModel bestPickItem : bestPickCarouselComponent.getCollectionItems())
					{
						final JSONObject bestPickItemJson = new JSONObject();

						if (null != bestPickItem)
						{
							if (null != bestPickItem.getMedia())
							{
								if (null != bestPickItem.getMedia().getURL() && StringUtils.isNotEmpty(bestPickItem.getMedia().getURL()))
								{
									imageURL = bestPickItem.getMedia().getURL();
								}

							}
							else
							{
								LOG.info("No Media for this item");
								//imageURL = MISSING_IMAGE_URL;
								imageURL = GenericUtilityMethods.getMissingImageUrl();
							}

							bestPickItemJson.put("imageUrl", imageURL);

							if (null != bestPickItem.getContent() && StringUtils.isNotEmpty(bestPickItem.getContent()))
							{
								text = bestPickItem.getContent();
							}
							else
							{
								LOG.info("No text for this item");
							}
							bestPickItemJson.put("text", text);

							if (null != bestPickItem.getUrl() && StringUtils.isNotEmpty(bestPickItem.getUrl()))
							{
								linkUrl = bestPickItem.getUrl();
							}
							else
							{
								LOG.info("No URL for this item");
							}
							bestPickItemJson.put("url", linkUrl);
							bestPickItemJson.put(ICID, bestPickItem.getPk().getLongValueAsString());
							subComponentJsonArray.add(bestPickItemJson);

						}
						else
						{
							LOG.info("No instance of bestPickCarouselComponent found!!!");
						}
					}
				}
				bestPicks.put("subItems", subComponentJsonArray);
				bestPicks.put("slideBy", bestPickCarouselComponent.getSlideBy());
				bestPicks.put("autoplayTimeout", bestPickCarouselComponent.getAutoplayTimeout());
			}
		}

		return bestPicks;
	}





	@Override
	public JSONObject getProductsYouCareJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject productYouCare = new JSONObject();

		if (CollectionUtils.isNotEmpty(contentSlot.getCmsComponents()))
		{
			components = contentSlot.getCmsComponents();
		}

		String title = MarketplacecommerceservicesConstants.EMPTY;
		String mediaUrl = MarketplacecommerceservicesConstants.EMPTY;
		Integer slideBy = null;
		Integer autoplayTimeout = null;

		final JSONArray subComponentJsonArray = new JSONArray();

		for (final AbstractCMSComponentModel component : components)
		{
			//Forming JSON is the component is of MplCategoryCarouselComponentModel type
			if (component instanceof MplCategoryCarouselComponentModel)
			{
				final MplCategoryCarouselComponentModel productYouCareCarouselComponent = (MplCategoryCarouselComponentModel) component;

				if (StringUtils.isNotEmpty(productYouCareCarouselComponent.getTitle()))
				{
					title = productYouCareCarouselComponent.getTitle();
				}

				if (productYouCareCarouselComponent.getSlideBy() != null)
				{

					slideBy = productYouCareCarouselComponent.getSlideBy();
				}

				if (productYouCareCarouselComponent.getAutoplayTimeout() != null)
				{

					autoplayTimeout = productYouCareCarouselComponent.getAutoplayTimeout();

				}


				if (CollectionUtils.isNotEmpty(productYouCareCarouselComponent.getCategories()))
				{
					for (final CategoryModel category : productYouCareCarouselComponent.getCategories())
					{
						final JSONObject categoryJSON = getCategoryJSON(category);
						categoryJSON.put("mediaURL", getCategoryMediaUrl(category));
						categoryJSON.put(ICID, productYouCareCarouselComponent.getPk().getLongValueAsString());
						subComponentJsonArray.add(categoryJSON);
					}
				}



			}
			//Forming JSON is the component is of MplAdvancedCategoryCarouselComponentModel type for overriding PCM provided Category Images
			if (component instanceof MplAdvancedCategoryCarouselComponentModel)
			{

				final MplAdvancedCategoryCarouselComponentModel productYouCareCarouselComponent = (MplAdvancedCategoryCarouselComponentModel) component;

				if (StringUtils.isNotEmpty(productYouCareCarouselComponent.getTitle()))
				{
					title = productYouCareCarouselComponent.getTitle();
				}

				if (productYouCareCarouselComponent.getSlideBy() != null)
				{

					slideBy = productYouCareCarouselComponent.getSlideBy();
				}

				if (productYouCareCarouselComponent.getAutoplayTimeout() != null)
				{

					autoplayTimeout = productYouCareCarouselComponent.getAutoplayTimeout();

				}
				if (CollectionUtils.isNotEmpty(productYouCareCarouselComponent.getCategories()))
				{
					for (final MplImageCategoryComponentModel imageCategoryComponent : productYouCareCarouselComponent.getCategories())
					{
						if (imageCategoryComponent.getCategory() != null)
						{
							final JSONObject categoryJSON = getCategoryJSON(imageCategoryComponent.getCategory());
							if (imageCategoryComponent.getIsImageFromPCM().booleanValue())
							{
								mediaUrl = getCategoryMediaUrl(imageCategoryComponent.getCategory());

							}
							else
							{
								if (null != imageCategoryComponent.getImage()
										&& StringUtils.isNotEmpty(imageCategoryComponent.getImage().getURL()))
								{
									mediaUrl = imageCategoryComponent.getImage().getURL();
								}

							}

							categoryJSON.put("mediaURL", mediaUrl);
							categoryJSON.put(ICID, imageCategoryComponent.getPk().getLongValueAsString());
							subComponentJsonArray.add(categoryJSON);
						}


					}
				}

			}

			productYouCare.put("slideBy", slideBy);
			productYouCare.put("autoplayTimeout", autoplayTimeout);
			productYouCare.put(TITLE, title);
			productYouCare.put("categories", subComponentJsonArray);


		}
		return productYouCare;
	}


	/**
	 * @param category
	 * @return JSONObject
	 */
	private JSONObject getCategoryJSON(final CategoryModel category)
	{
		//	TISPRD-2315
		final JSONObject categoryJSON = new JSONObject();
		String categoryPath = GenericUtilityMethods.buildPathString(getCategoryPath(category));
		if (StringUtils.isNotEmpty(categoryPath))
		{
			try
			{
				categoryPath = URLDecoder.decode(categoryPath, "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error(e.getMessage());
			}
			categoryPath = categoryPath.toLowerCase();
			categoryPath = GenericUtilityMethods.changeUrl(categoryPath);
			categoryJSON.put("categoryPath", categoryPath);
		}

		if (StringUtils.isNotEmpty(category.getName()))
		{
			categoryJSON.put("categoryName", category.getName());

		}

		if (StringUtils.isNotEmpty(category.getCode()))
		{
			categoryJSON.put("categoryCode", category.getCode());

		}

		return categoryJSON;

	}


	/**
	 * @param category
	 */
	private String getCategoryMediaUrl(final CategoryModel category)
	{

		//String mediaUrl = MISSING_IMAGE_URL;
		String mediaUrl = GenericUtilityMethods.getMissingImageUrl();
		if (null != category.getMedias())
		{
			for (final MediaModel categoryMedia : category.getMedias())
			{
				if (null != categoryMedia.getMediaFormat()
						&& categoryMedia.getMediaFormat().getQualifier().equalsIgnoreCase("324Wx324H")
						&& null != categoryMedia.getURL2())
				{
					mediaUrl = getMediaUrlStrategy(categoryMedia.getURL2());

				}
			}

		}
		return mediaUrl;



	}



	@Override
	public JSONObject getJsonBanner(final ContentSlotModel slot, final String compType)
	{
		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		final JSONObject allBannerJson = new JSONObject();
		if (CollectionUtils.isNotEmpty(slot.getCmsComponents()))
		{
			components = slot.getCmsComponents();
		}

		for (final AbstractCMSComponentModel component : components)
		{
			LOG.info("Component>>>>with id :::" + component.getUid());
			if (component instanceof MplSequentialBannerComponentModel)
			{
				final MplSequentialBannerComponentModel promoBanner = (MplSequentialBannerComponentModel) component;

				final JSONArray bannerJsonArray = new JSONArray();

				for (final BannerComponentModel banner : promoBanner.getBannersList())
				{
					final JSONObject bannerJson = new JSONObject();
					if (banner instanceof MplBigPromoBannerComponentModel)
					{
						final MplBigPromoBannerComponentModel bannerImage = (MplBigPromoBannerComponentModel) banner;
						if (bannerImage.getBannerImage() != null)
						{
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_IMAGE, bannerImage.getBannerImage().getURL());
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_ALTTEXT, bannerImage.getBannerImage()
									.getAltText());
						}
						else
						{
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_IMAGE,
									MarketplacecommerceservicesConstants.EMPTYSPACE);
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_ALTTEXT,
									MarketplacecommerceservicesConstants.EMPTYSPACE);
						}
						bannerJson.put("bannerUrlLink", bannerImage.getUrlLink());
						bannerJson.put("promoText1", bannerImage.getMajorPromoText());
						bannerJson.put("promoText2", bannerImage.getMinorPromo1Text());
						bannerJson.put("promoText3", bannerImage.getMinorPromo2Text());
						bannerJson.put("sequenceNumber", bannerImage.getSequenceNumber());
						bannerJsonArray.add(bannerJson);
					}

					if (banner instanceof MplBigFourPromoBannerComponentModel)
					{
						final MplBigFourPromoBannerComponentModel bannerImage = (MplBigFourPromoBannerComponentModel) banner;
						if (bannerImage.getBannerImage() != null)
						{
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_IMAGE, bannerImage.getBannerImage().getURL());
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_ALTTEXT, bannerImage.getBannerImage()
									.getAltText());
						}
						else
						{
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_IMAGE,
									MarketplacecommerceservicesConstants.EMPTYSPACE);
							bannerJson.put(MarketplacecommerceservicesConstants.BANNER_ALTTEXT,
									MarketplacecommerceservicesConstants.EMPTYSPACE);
						}
						bannerJson.put("bannerUrlLink", bannerImage.getUrlLink());
						bannerJson.put("promoText1", bannerImage.getPromoText1());
						bannerJson.put("promoText2", bannerImage.getPromoText2());
						bannerJson.put("promoText3", bannerImage.getPromoText3());
						bannerJson.put("promoText4", bannerImage.getPromoText4());
						bannerJson.put("sequenceNumber", bannerImage.getSequenceNumber());
						bannerJsonArray.add(bannerJson);
					}
				}
				allBannerJson.put("allBannerJsonObject", bannerJsonArray);
			}
		}
		return allBannerJson;
	}

	/**
	 * This method takes the sequence number and fetches the banner for that sequence number
	 *
	 * @param sequenceNumber
	 * @param component
	 * @return displayBanner
	 */
	@SuppressWarnings("unused")
	private BannerComponentModel getBannerforSequenceNumber(final int sequenceNumber,
			final MplSequentialBannerComponentModel component)
	{
		BannerComponentModel displayBanner = null;
		if (component.getBannersList() != null)
		{
			for (final BannerComponentModel banner : component.getBannersList())
			{

				if (banner instanceof MplBigPromoBannerComponentModel)
				{
					final MplBigPromoBannerComponentModel promoBanner = (MplBigPromoBannerComponentModel) banner;

					if (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber))
					{
						displayBanner = banner;
					}

					/*
					 * if ("stayQued".equalsIgnoreCase(sq)) { if (promoBanner.getSeqNumForStayQued() ==
					 * Integer.valueOf(sequenceNumber)) { displayBanner = banner; } } else { if
					 * (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber)) { displayBanner = banner; } }
					 */

				}
				if (banner instanceof MplBigFourPromoBannerComponentModel)
				{
					final MplBigFourPromoBannerComponentModel promoBanner = (MplBigFourPromoBannerComponentModel) banner;

					if (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber))
					{
						displayBanner = banner;
					}
					/*
					 * if ("stayQued".equalsIgnoreCase(sq)) { if (promoBanner.getSeqNumForStayQued() ==
					 * Integer.valueOf(sequenceNumber)) { displayBanner = banner; } } else { if
					 * (promoBanner.getSequenceNumber() == Integer.valueOf(sequenceNumber)) { displayBanner = banner; } }
					 */

				}
			}

		}
		return displayBanner;
	}

	private String getMediaUrlStrategy(final String mediaUrl)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Media Url is :::::::" + mediaUrl);
		}

		String newMediaUrl = mediaUrl;
		if (StringUtils.isNotEmpty(mediaUrl))
		{
			if (mediaUrl.contains("http") || mediaUrl.contains("https"))
			{
				newMediaUrl = mediaUrl.substring((mediaUrl.lastIndexOf(':') + 1));
			}


		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Media Url without protocol is :::::::" + newMediaUrl);
		}

		return newMediaUrl;

	}

	@Override
	public List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> path = commerceCategoryService.getPathsForCategory(category);
		if (null != path)
		{
			for (final List<CategoryModel> path1 : path)
			{
				if (path1.size() > 1)
				{
					path1.remove(0);
				}
			}
		}

		return (path.iterator().next());
	}

}