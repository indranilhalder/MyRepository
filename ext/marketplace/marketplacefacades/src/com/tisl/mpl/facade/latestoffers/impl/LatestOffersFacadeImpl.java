/**
 *
 */
package com.tisl.mpl.facade.latestoffers.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.facade.latestoffers.LatestOffersFacade;
import com.tisl.mpl.facades.data.LatestOffersData;
import com.tisl.mpl.facades.data.LatestOffersEntriesData;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.model.cms.components.ImageCarouselComponentModel;


/**
 * @author TCS
 *
 */
public class LatestOffersFacadeImpl implements LatestOffersFacade
{
	private static final Logger LOG = Logger.getLogger(LatestOffersFacadeImpl.class);
	private static final String MISSING_IMAGE_URL = "/store/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";



	/**
	 * This method fetches the CMSSlot Model and returns the required data for latest offers
	 * 
	 * @param contentSlot
	 * @return LatestOffersData
	 */
	@Override
	public LatestOffersData getLatestOffers(final ContentSlotModel contentSlot)
	{
		final LatestOffersData offerData = new LatestOffersData();
		final List<LatestOffersEntriesData> entriesdataList = new ArrayList<LatestOffersEntriesData>();

		List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		if (CollectionUtils.isNotEmpty(contentSlot.getCmsComponents()))
		{
			components = contentSlot.getCmsComponents();
		}

		for (final AbstractCMSComponentModel component : components)
		{
			if (component instanceof ImageCarouselComponentModel)
			{
				final ImageCarouselComponentModel latestOffersCarouselComponent = (ImageCarouselComponentModel) component;
				String title = "";

				if (StringUtils.isNotEmpty(latestOffersCarouselComponent.getTitle()))
				{
					title = latestOffersCarouselComponent.getTitle();
				}

				offerData.setTitle(title);
				final List<CMSMediaParagraphComponentModel> latestOffersItemList = latestOffersCarouselComponent.getCollectionItems();

				for (final CMSMediaParagraphComponentModel latestOffersItem : latestOffersItemList)
				{
					final LatestOffersEntriesData entriesdata = new LatestOffersEntriesData();
					String imageURL = MISSING_IMAGE_URL;
					String text = "";
					String linkUrl = "#";
					if (null != latestOffersItem)
					{
						if (null != latestOffersItem.getMedia() && StringUtils.isNotEmpty(latestOffersItem.getMedia().getURL()))
						{
							imageURL = latestOffersItem.getMedia().getURL();
						}

						if (StringUtils.isNotEmpty(latestOffersItem.getContent()))
						{
							text = latestOffersItem.getContent();
						}

						if (StringUtils.isNotEmpty(latestOffersItem.getUrl()))
						{
							linkUrl = latestOffersItem.getUrl();
						}
					}
					else
					{
						LOG.info("No instance of bestPickCarouselComponent found!!!");
					}

					//Setting Latest Offer Entries Data
					entriesdata.setImageUrl(imageURL);
					entriesdata.setText(text);
					entriesdata.setUrl(linkUrl);
					entriesdata.setIcid(latestOffersItem.getPk().getLongValueAsString());

					entriesdataList.add(entriesdata);
				}
				offerData.setLatestOfferEntry(entriesdataList);

			}
		}
		return offerData;
	}


}
