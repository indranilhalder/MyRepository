/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.component.data.CMSSubbrandData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CMSSubbrandModel;


/**
 * @author TCS
 *
 */
public class MplCmsSubBrandPopulator implements Populator<CMSSubbrandModel, CMSSubbrandData>
{

	private Converter<MediaModel, ImageData> imageConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CMSSubbrandModel source, final CMSSubbrandData target) throws ConversionException
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		if (null != source.getSubBrandImage())
		{
			final ImageData subBrandImageData = imageConverter.convert(source.getSubBrandImage());
			target.setSubBrandImage(subBrandImageData);
		}

		if (null != source.getSubBrandLogo())
		{
			final ImageData subBrandLogoData = imageConverter.convert(source.getSubBrandLogo());
			target.setSubBrandLogo(subBrandLogoData);
		}
		target.setSubBrandName(source.getSubBrandName());
		target.setSubBrandUrl(source.getSubBrandUrl());
	}

	/**
	 * @return the imageConverter
	 */
	public Converter<MediaModel, ImageData> getImageConverter()
	{
		return imageConverter;
	}

	/**
	 * @param imageConverter
	 *           the imageConverter to set
	 */
	public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
	{
		this.imageConverter = imageConverter;
	}
}
