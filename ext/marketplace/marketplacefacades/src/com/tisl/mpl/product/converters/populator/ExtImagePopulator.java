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
/**
 * @author TCS
 */
package com.tisl.mpl.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;

import org.springframework.util.Assert;

import com.tisl.mpl.facades.data.MediaStatusData;
import com.tisl.mpl.facades.data.MediaTypeData;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.media.MediaModel} as source and
 * {@link de.hybris.platform.commercefacades.product.data.ImageData} as target type.
 */
public class ExtImagePopulator implements Populator<MediaModel, ImageData>
{
	/*
	 * @Javadoc Method to Populate Image
	 * 
	 * @param source,target
	 * 
	 * @return null
	 */
	@Override
	public void populate(final MediaModel source, final ImageData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setUrl(source.getURL());
		target.setAltText(source.getAltText());
		if (source.getMediaFormat() != null)
		{
			target.setFormat(source.getMediaFormat().getQualifier());
		}
		if (source.getMediaPriority() != null)
		{
			//Setting Media Priority
			target.setMediaPriority(source.getMediaPriority());
		}
		if (source.getQualifier() != null && !source.getQualifier().isEmpty())
		{
			//Setting Media Qualifier
			target.setQualifier(source.getQualifier());
		}
		//Setting Media Status
		if (source.getMediaStatus() != null)
		{
			final MediaStatusData mediaStatus = new MediaStatusData();
			mediaStatus.setCode(source.getMediaStatus().getCode());
			mediaStatus.setType(source.getMediaStatus().getType());
			target.setMediaStatus(mediaStatus);
		}

		//Setting Media Type
		if (source.getMediaType() != null)
		{
			final MediaTypeData mediaType = new MediaTypeData();
			mediaType.setCode(source.getMediaType().getCode());
			mediaType.setType(source.getMediaType().getType());
			target.setMediaType(mediaType);
		}
	}
}
