/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.converters.populator.VariantOptionDataPopulator;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;


/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */


public class CustomImagePopulator<SOURCE extends VariantProductModel, TARGET extends VariantOptionData> extends
		VariantOptionDataPopulator
{

	protected static final Logger LOG = Logger.getLogger(CustomImagePopulator.class);

	private TypeService typeService;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private ImageFormatMapping mplImageFormatMapping;

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


	private Map<String, String> variantAttributeMapping;

	/**
	 * @description method is to populate image in variant data from media container in pdp
	 * @param variantProductModel
	 * @param variantOptionData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */


	@Override
	public void populate(final VariantProductModel variantProductModel, final VariantOptionData variantOptionData)
			throws ConversionException, EtailNonBusinessExceptions
	{


		//super.populate(variantProductModel, variantOptionData);

		Assert.notNull(variantProductModel, "Parameter source cannot be null.");
		Assert.notNull(variantOptionData, "Parameter target cannot be null.");

		if (variantProductModel.getBaseProduct() != null)
		{
			//			final List<VariantAttributeDescriptorModel> descriptorModels = getVariantsService().getVariantAttributesForVariantType(
			//					variantProductModel.getBaseProduct().getVariantType());

			//	final Collection<VariantOptionQualifierData> variantOptionQualifiers = new ArrayList<VariantOptionQualifierData>();
			//			for (final VariantAttributeDescriptorModel descriptorModel : descriptorModels)
			//			{
			//				// Create the variant qualifier
			//				final VariantOptionQualifierData variantOptionQualifier = new VariantOptionQualifierData();
			//				final String qualifier = descriptorModel.getQualifier();
			//				variantOptionQualifier.setQualifier(qualifier);
			//				variantOptionQualifier.setName(descriptorModel.getName());
			//				// Lookup the value
			//				final Object variantAttributeValue = lookupVariantAttributeName(variantProductModel, qualifier);
			//				variantOptionQualifier.setValue(variantAttributeValue == null ? "" : variantAttributeValue.toString());
			//
			//				// Add to list of variants
			//				variantOptionQualifiers.add(variantOptionQualifier);
			//			}
			//variantOptionData.setVariantOptionQualifiers(variantOptionQualifiers);
			variantOptionData.setCode(variantProductModel.getCode());
			variantOptionData.setUrl(getProductModelUrlResolver().resolve(variantProductModel));
			//Stock Level wont be set for Product, as stock will be set at USSID level.All calculations will be done from Buy Box.
			//variantOptionData.setStock(getStockConverter().convert(variantProductModel));

			//			final PriceDataType priceType;
			//			final PriceInformation info;
			//			if (CollectionUtils.isEmpty(variantProductModel.getVariants()))
			//			{
			//				priceType = PriceDataType.BUY;
			//				info = getCommercePriceService().getWebPriceForProduct(variantProductModel);
			//			}
			//			else
			//			{
			//				priceType = PriceDataType.FROM;
			//				info = getCommercePriceService().getFromPriceForProduct(variantProductModel);
			//			}
			//
			//			if (info != null)
			//			{
			//				final PriceData priceData = getPriceDataFactory().create(priceType,
			//						BigDecimal.valueOf(info.getPriceValue().getValue()), info.getPriceValue().getCurrencyIso());
			//				variantOptionData.setPriceData(priceData);
			//			}
		}

		final MediaContainerModel mediaContainer = getPrimaryImageMediaContainer(variantProductModel);
		if (mediaContainer != null)
		{

			final MediaModel media = getMediaWithImageFormat(mediaContainer, MarketplaceFacadesConstants.STYLE_SWATCH);

			if (media != null)
			{
				variantOptionData.setImage(getImageConverter().convert(media));
			}
		}
	}


	protected MediaModel getMediaWithImageFormat(final MediaContainerModel mediaContainer, final String imageFormat)
	{
		if (mediaContainer != null && imageFormat != null)
		{
			final String mediaFormatQualifier = getMplImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
			if (mediaFormatQualifier != null)
			{
				final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
				if (mediaFormat != null)
				{
					return getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
				}
			}
		}
		return null;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}



	protected Map<String, String> getVariantAttributeMapping()
	{
		return variantAttributeMapping;
	}

	@Required
	public void setVariantAttributeMapping(final Map<String, String> variantAttributeMapping)
	{
		this.variantAttributeMapping = variantAttributeMapping;
	}


	protected MediaContainerModel getPrimaryImageMediaContainer(final VariantProductModel variantProductModel)
	{
		final MediaModel picture = variantProductModel.getPicture();
		if (picture != null)
		{
			return picture.getMediaContainer();
		}
		return null;
	}

}
