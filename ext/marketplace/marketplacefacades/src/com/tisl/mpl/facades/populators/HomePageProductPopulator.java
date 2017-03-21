/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class HomePageProductPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{

	protected static final Logger LOG = Logger.getLogger(HomePageProductPopulator.class);


	public UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	/**
	 * @param productModelUrlResolver
	 *           the productModelUrlResolver to set
	 */
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}


	@Autowired
	private UrlResolver<ProductModel> productModelUrlResolver;


	private TypeService typeService;
	@Autowired
	private MediaService mediaService;
	@Autowired
	private MediaContainerService mediaContainerService;
	@Autowired
	private ImageFormatMapping mplImageFormatMapping;

	/**
	 * @return the typeService
	 */
	public TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @return the mediaService
	 */

	public MediaService getMediaService()
	{
		return mediaService;
	}

	/**
	 * @param mediaService
	 *           the mediaService to set
	 */

	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	/**
	 * @return the mediaContainerService
	 */
	public MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	/**
	 * @param mediaContainerService
	 *           the mediaContainerService to set
	 */

	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

	/**
	 * @return the mplImageFormatMapping
	 */
	public ImageFormatMapping getMplImageFormatMapping()
	{
		return mplImageFormatMapping;
	}

	/**
	 * @param mplImageFormatMapping
	 *           the mplImageFormatMapping to set
	 */
	public void setMplImageFormatMapping(final ImageFormatMapping mplImageFormatMapping)
	{
		this.mplImageFormatMapping = mplImageFormatMapping;
	}




	/**
	 * @description method is to populate basic product details in homepage
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		//set Product Title
		productData.setProductTitle(productModel.getTitle() == null ? productData.getName() : productModel.getTitle());
		//set Product Code
		productData.setListingId((String) getProductAttribute(productModel, ProductModel.CODE));
		//set Product Name
		productData.setName((String) getProductAttribute(productModel, ProductModel.NAME));
		//set Product Url
		productData.setUrl(productModelUrlResolver.resolve(productModel));
		//set Media Url
		final MediaContainerModel mediaConatainer = getPrimaryImageMediaContainer(productModel);
		final MediaModel media = getMediaWithImageFormat(mediaConatainer, "searchPage");

		if (media != null)
		{
			productData.setHomepageImageurl(media.getURL());
		}

	}

	protected MediaModel getMediaWithImageFormat(final MediaContainerModel mediaContainer, final String imageFormat)
	{
		MediaModel mediaModel = null;

		if (mediaContainer != null && imageFormat != null)
		{
			final String mediaFormatQualifier = getMplImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
			if (mediaFormatQualifier != null)
			{
				final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
				if (mediaFormat != null)
				{
					//return getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
					try
					{
						mediaModel = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
					}
					catch (final Exception ex)
					{
						LOG.error("Could not find Media for the given resolution::::" + mediaFormat.getQualifier() + ":::"
								+ ex.getMessage());
					}
				}
			}
		}
		//return null;
		return mediaModel;
	}

	protected MediaContainerModel getPrimaryImageMediaContainer(final ProductModel variantProductModel)
	{

		return (variantProductModel != null && variantProductModel.getPicture() != null && variantProductModel.getPicture()
				.getMediaContainer() != null) ? variantProductModel.getPicture().getMediaContainer() : null;
	}


}
