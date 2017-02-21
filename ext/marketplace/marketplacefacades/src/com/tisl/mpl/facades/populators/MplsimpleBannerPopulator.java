/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.tisl.mpl.facades.data.SimpleBannerComponentData;


/**
 * @author TCS
 *
 */
public class MplsimpleBannerPopulator implements Populator<SimpleBannerComponentModel, SimpleBannerComponentData>
{


	private Converter<MediaModel, de.hybris.platform.commercefacades.product.data.ImageData> imageConverter;

	/**
	 * @return the imageConverter
	 */
	public Converter<MediaModel, de.hybris.platform.commercefacades.product.data.ImageData> getImageConverter()
	{
		return imageConverter;
	}

	/**
	 * @param imageConverter
	 *           the imageConverter to set
	 */
	public void setImageConverter(
			final Converter<MediaModel, de.hybris.platform.commercefacades.product.data.ImageData> imageConverter)
	{
		this.imageConverter = imageConverter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SimpleBannerComponentModel source, final SimpleBannerComponentData target)
			throws ConversionException
	{
		target.setTitle(source.getTitle());
		target.setDescription(source.getDescription());
		target.setUrlLink(source.getUrlLink());
		if (null != source.getMedia())
		{
			final ImageData imageData = imageConverter.convert(source.getMedia());
			target.setMedia(imageData);
		}

	}
}
