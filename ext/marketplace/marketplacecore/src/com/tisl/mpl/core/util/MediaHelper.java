/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.util;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.net.URL;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tils.mpl.media.MplMediaService;


/**
 * Helper bean for generating the MCC site links for the supported websites
 */
public class MediaHelper
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MediaHelper.class);
	@Resource
	private MplMediaService mplMediService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@SuppressWarnings("deprecation")
	public MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		MediaModel media = null;
		try
		{
			if ((product != null) && (mediaFormat != null) && CollectionUtils.isNotEmpty(product.getGalleryImages()))
			{ //TPR-796 EQA check
				media = mplMediService.getMediaForIndexing(product, mediaFormat, product.getGalleryImages());
				if (media.getUrl() != null && media.getUrl().length() > 0)
				{
					final String prodCode = product.getCode().toString();
					final char lastChar = prodCode.charAt(prodCode.length() - 1);
					final int lastProductNum = Integer.parseInt(String.valueOf(lastChar));
					int numberOfHosts;
					if (configurationService.getConfiguration().getString("search.media.numberofhosts") != null)
					{
						numberOfHosts = Integer.parseInt(String.valueOf(configurationService.getConfiguration().getString(
								"search.media.numberofhosts")));
					}
					else
					{
						numberOfHosts = 1;
					}
					if (((lastProductNum % numberOfHosts) != 0) && (numberOfHosts != 1))
					{
						URL netUrl = null;
						if (!media.getUrl().contains("https://") && !media.getUrl().contains("http://"))
						{
							netUrl = new URL("http:" + media.getUrl());
						}
						else
						{
							netUrl = new URL(media.getUrl());
						}
						String host = null;
						host = netUrl.getHost();
						final String path = netUrl.getPath();
						if (!host.contains("localhost"))
						{
							final String splitHostName[] = host.split("\\.");
							if (splitHostName != null)
							{
								String newHost = "";
								newHost = splitHostName[0] + (lastProductNum % numberOfHosts) + "." + splitHostName[1] + "."
										+ splitHostName[2];
								final String newMediaUrl = "//" + newHost + path;
								media.setUrl(newMediaUrl);
							}
						}
					}
				}
			}
		}
		catch (final ModelNotFoundException localModelNotFoundException)
		{
			LOG.debug("Error finding Media for the Product" + localModelNotFoundException);
			return null;
		}
		catch (final Exception e)
		{
			LOG.debug("Exception in finding Media" + e);
			return null;
		}

		return media;
	}
}
