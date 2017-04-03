/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;
import de.hybris.platform.servicelayer.media.impl.MediaDao;
import de.hybris.platform.servicelayer.media.impl.ModelMediaSource;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.tisl.mpl.marketplacecommerceservices.service.CustomMediaService;


/**
 *
 */
public class DefaultCustomMediaService extends DefaultMediaService implements CustomMediaService
{
	private static final Logger LOG = Logger.getLogger(DefaultCustomMediaService.class);


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "mediaDao")
	private MediaDao mediaDao;


	public static final String CONTEXT_PARAM_MARKERS = "//";

	@Override
	public String getUrlForMedia(final MediaModel media)
	{
		if (hasData(media))
		{
			return MediaManager.getInstance().getURLForMedia(media.getFolder().getQualifier(), new ModelMediaSource(media));
		}

		final String damHostName = getConfigurationService().getConfiguration().getString("product.dammedia.host");
		String internalURL = media.getInternalURL();
		if (StringUtils.isEmpty(damHostName))
		{
			LOG.debug("Actual URL :" + internalURL);
			final int indexVal = internalURL.indexOf(CONTEXT_PARAM_MARKERS);

			if (indexVal == -1)
			{
				internalURL = CONTEXT_PARAM_MARKERS + internalURL;
			}
			return internalURL.substring(internalURL.indexOf(CONTEXT_PARAM_MARKERS));
		}

		final String modifiedInternalUrl = CONTEXT_PARAM_MARKERS + damHostName
				+ internalURL.substring(internalURL.indexOf(".com") + 4);

		LOG.debug("Modified URL :" + modifiedInternalUrl);
		return modifiedInternalUrl;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * This method returns media based on code for TPR-1285 Dynamic sitemap
	 *
	 * @param code
	 * @return MediaModel
	 */
	@Override
	public MediaModel getMediaByCode(final String code)
	{
		final List result = mediaDao.findMediaByCode(code);

		return (CollectionUtils.isNotEmpty(result) ? (MediaModel) result.get(0) : null);
	}


}
