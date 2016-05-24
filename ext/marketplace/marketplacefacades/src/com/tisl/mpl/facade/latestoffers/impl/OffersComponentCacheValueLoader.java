/**
 *
 */
package com.tisl.mpl.facade.latestoffers.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.cms.components.ImageCarouselComponentModel;


/**
 * @author TCS
 *
 *         Loads the latest offers from cache loader using cache key @see OffersComponentCacheKey
 *
 */
public class OffersComponentCacheValueLoader implements CacheValueLoader
{

	@Autowired
	private DefaultCMSContentSlotService contentSlotService;

	@Override
	public Object load(final CacheKey key) throws CacheValueLoadException
	{
		String title = null;
		if (key instanceof OffersComponentCacheKey)
		{
			//HeaderTopSection
			final ContentSlotModel homepageHeaderConcierge = contentSlotService.getContentSlotForId("HeaderLinksSlot");
			List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
			title = "Latest Offers";
			if (CollectionUtils.isNotEmpty(homepageHeaderConcierge.getCmsComponents()))
			{
				components = homepageHeaderConcierge.getCmsComponents();
			}

			for (final AbstractCMSComponentModel component : components)
			{
				if (component instanceof ImageCarouselComponentModel)
				{
					final ImageCarouselComponentModel latestOffersCarouselComponent = (ImageCarouselComponentModel) component;

					if (StringUtils.isNotEmpty(latestOffersCarouselComponent.getTitle()))
					{
						title = latestOffersCarouselComponent.getTitle();
						break;
					}
					break;
				}
			}
			//return title;
		}

		return title;
	}

}
