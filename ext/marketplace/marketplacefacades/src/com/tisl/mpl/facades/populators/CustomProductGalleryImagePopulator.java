/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductGalleryImagesPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tils.mpl.media.MplMediaService;
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

	@Autowired
	private MplMediaService mediaService;

	//	DefaultCustomMediaService defaultCustomMediaService;

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

		final Map<String, MediaModel> mediaMapwithFormat = getMediaList(mediaContainer);

		try
		{
			for (final Map.Entry<String, MediaModel> entry : mediaMapwithFormat.entrySet())
			{
				final ImageData imageData = getImageConverter().convert(entry.getValue());
				imageData.setFormat(entry.getKey());
				imageData.setImageType(imageType);

				if (null != entry.getValue().getMediaPriority())
				{
					if (null != entry.getValue().getMediaType() && MediaTypeEnum.VIDEO.equals(entry.getValue().getMediaType()))
					{
						final int newPriority = (entry.getValue().getMediaPriority() == null ? 0 : entry.getValue().getMediaPriority()
								.intValue())
								+ MarketplaceFacadesConstants.PRIORITY_INCREMENT;
						imageData.setMediaPriority(Integer.valueOf(newPriority));
					}
					else
					{
						imageData.setMediaPriority(entry.getValue().getMediaPriority());
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

			}
		}
		catch (final ModelNotFoundException ignore)
		{
			LOG.error(ignore);
		}
		//Commented as a Part of Optimization Fix
		//		final List<String> mediaFormatQualifierList = new ArrayList<String>();
		//		final StringBuilder mediaFormatQualifierData = new StringBuilder(50);

		//		for (final String imageFormat : getImageFormats())
		//		{
		//			try
		//			{
		//				final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
		//				if (mediaFormatQualifier != null)
		//				{
		//					final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
		//					if (mediaFormat != null)
		//					{
		//
		//						final MediaModel media = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
		//						if (media != null)
		//						{
		//							final ImageData imageData = getImageConverter().convert(media);
		//							imageData.setFormat(imageFormat);
		//							imageData.setImageType(imageType);
		//							/*
		//							 * if (media != null) {
		//							 */
		//							if (null != media.getMediaPriority())
		//							{
		//								if (null != media.getMediaType() && MediaTypeEnum.VIDEO.equals(media.getMediaType()))
		//								{
		//									final int newPriority = (media.getMediaPriority() == null ? 0 : media.getMediaPriority().intValue())
		//											+ MarketplaceFacadesConstants.PRIORITY_INCREMENT;
		//									imageData.setMediaPriority(Integer.valueOf(newPriority));
		//								}
		//								else
		//								{
		//									imageData.setMediaPriority(media.getMediaPriority());
		//								}
		//							}
		//							else
		//							{
		//								imageData.setMediaPriority(Integer.valueOf(0));
		//							}
		//							if (ImageDataType.GALLERY.equals(imageType))
		//							{
		//								imageData.setGalleryIndex(Integer.valueOf(galleryIndex));
		//							}
		//							list.add(imageData);
		//							//}
		//						}
		//					}
		//				}


	}

	/*
	 * @JavaDoc Method to List down the MediaFormat and Fire Collective Query to Dao
	 * 
	 * @param MediaContainerModel container
	 * 
	 * @return MediaModel
	 */
	private Map<String, MediaModel> getMediaList(final MediaContainerModel container)
	{

		final List<String> mediaFormatQualifierList = new ArrayList<String>();
		final StringBuilder mediaFormatQualifierData = new StringBuilder(50);
		final Map<String, String> imageFormatMap = new HashMap<String, String>();

		for (final String imageFormat : getImageFormats())
		{
			final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
			imageFormatMap.put(mediaFormatQualifier, imageFormat);
			if (null != mediaFormatQualifier)
			{
				mediaFormatQualifierList.add(mediaFormatQualifier);
				mediaFormatQualifierData.append('\'').append(mediaFormatQualifier).append('\'').append(',');
			}
		}

		String mediaFormatQualifierApended = mediaFormatQualifierData.toString();
		if (!StringUtils.isEmpty(mediaFormatQualifierApended))
		{
			mediaFormatQualifierApended = mediaFormatQualifierApended.substring(0, mediaFormatQualifierApended.length() - 1);
		}

		LOG.debug("--------------" + mediaFormatQualifierApended);

		//Calling Optimized Method for Media
		final List<MediaModel> allmedia = mediaService.findMediaForQualifier(container, mediaFormatQualifierApended);


		final Map<String, MediaModel> mediaMapwithFormat = new HashMap<String, MediaModel>();
		//Mapping Media with Image Format
		for (final Map.Entry<String, String> entry : imageFormatMap.entrySet())
		{
			for (final MediaModel media : allmedia)
			{
				if (media.getMediaFormat().getName().equals(entry.getKey()))
				{
					mediaMapwithFormat.put(entry.getValue(), media);
				}
			}

		}
		LOG.debug("----------Media Format Mapping Completed----------");

		return mediaMapwithFormat;


	}
}
