/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductGalleryImagesPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.enums.MediaTypeEnum;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;


/**
 * @author TCS
 *
 */
public class CustomProductGalleryImagePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductGalleryImagesPopulator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(CustomProductGalleryImagePopulator.class);

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		// Collect the media containers on the product
		final List<MediaContainerModel> mediaContainers = new ArrayList<MediaContainerModel>();
		collectMediaContainers(productModel, mediaContainers);

		if (!mediaContainers.isEmpty())
		{
			final List<ImageData> imageList = new ArrayList<ImageData>();

			// fill our image list with the product's existing images
			if (productData.getImages() != null)
			{
				imageList.addAll(productData.getImages());
			}

			// Use all the images as gallery images
			int galleryIndex = 0;
			for (final MediaContainerModel mediaContainer : mediaContainers)
			{
				addImagesInFormats(mediaContainer, ImageDataType.GALLERY, galleryIndex++, imageList);
			}

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}
			}

			// Overwrite the existing list of images
			productData.setImages(imageList);
		}
	}

	@Override
	protected void collectMediaContainers(final ProductModel productModel, final List<MediaContainerModel> list)
	{
		super.collectMediaContainers(productModel, list);

	}

	@Override
	protected void addImagesInFormats(final MediaContainerModel mediaContainer, final ImageDataType imageType,
			final int galleryIndex, final List<ImageData> list)
	{
		for (final String imageFormat : getImageFormats())
		{
			try
			{
				final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
				if (mediaFormatQualifier != null)
				{
					final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
					if (mediaFormat != null)
					{
						final MediaModel media = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
						if (media != null)
						{
							final ImageData imageData = getImageConverter().convert(media);
							imageData.setFormat(imageFormat);
							imageData.setImageType(imageType);
							/*
							 * if (media != null) {
							 */
							if (null != media.getMediaPriority())
							{
								if (null != media.getMediaType() && MediaTypeEnum.VIDEO.equals(media.getMediaType()))
								{
									final int newPriority = (media.getMediaPriority() == null ? 0 : media.getMediaPriority().intValue())
											+ MarketplaceFacadesConstants.PRIORITY_INCREMENT;
									imageData.setMediaPriority(Integer.valueOf(newPriority));
								}
								else
								{
									imageData.setMediaPriority(media.getMediaPriority());
								}
							}
							else
							{
								imageData.setMediaPriority(Integer.valueOf(0));
							}
							if (ImageDataType.GALLERY.equals(imageType))
							{
								imageData.setGalleryIndex(Integer.valueOf(galleryIndex));
							}
							list.add(imageData);
							//}
						}
					}
				}
			}
			catch (final ModelNotFoundException ignore)
			{
				// Ignore
				//LOGGER added for SONAR Fix
				LOG.error(ignore);
			}
		}
	}
}
