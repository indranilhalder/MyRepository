/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.commercefacades.product.ProductOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.HomepageComponentService;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ImageCarouselComponentModel;


/**
 * @author TCS
 *
 */
public class HomepageComponentServiceImpl implements HomepageComponentService
{

	private static final String MISSING_IMAGE_URL = "/store/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY);

	private static final String SEQUENCE_NUMBER = "SequenceNumber";
	private static final String SEQUENCE_NUMBER_STAYQUED = "SeqNumForStayQued";

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
								imageURL = MISSING_IMAGE_URL;
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
							subComponentJsonArray.add(bestPickItemJson);

						}
						else
						{
							LOG.info("No instance of bestPickCarouselComponent found!!!");
						}
					}
				}
				bestPicks.put("subItems", subComponentJsonArray);

			}
		}

		return bestPicks;
	}


	@Override
	public JSONObject getBrandsYouLoveJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject getProductsYouCareJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject getStayQuedComponentJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject getNewandExclusiveJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject getShowCaseJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public JSONObject getPromoBannerJSON(final ContentSlotModel contentSlot) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return null;
	}

}
