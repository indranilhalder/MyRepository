/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.cms.data.CollectionProductData;
import com.tisl.mpl.facades.cms.data.VariantOptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.ProductTagDto;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


/**
 * @author 584443
 *
 */
public class DefaultMobileCollectionProductPopulator implements Populator<ProductModel, CollectionProductData>
{
	private static final Logger LOG = Logger.getLogger(DefaultMobileCollectionProductPopulator.class);
	private ProductFacade productFacade;

	private BuyBoxFacade buyBoxFacade;

	/**
	 * @return the buyBoxFacade
	 */
	public BuyBoxFacade getBuyBoxFacade()
	{
		return buyBoxFacade;
	}

	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final ProductModel source, final CollectionProductData target) throws ConversionException
	{
		final ProductData productData = productFacade.getProductForOptions(source,
				Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.GALLERY, ProductOption.PRICE,
						ProductOption.IMAGES, ProductOption.SUMMARY, ProductOption.VARIANT_FULL, ProductOption.CATEGORIES,
						ProductOption.DESCRIPTION));
		if (productData != null)
		{
			try
			{
				target.setProductCode(productData.getCode());
				target.setProductName(productData.getName());
				target.setBrand(productData.getBrand() != null ? productData.getBrand().getBrandname() : null);

				target.setRating(productData.getAverageRating() != null ? productData.getAverageRating().toString() : null);
				final ImageData imgData = getPrimaryImageForProductAndFormat(productData, "searchPage");
				target.setUrl(imgData != null ? imgData.getUrl() : null);
				final ProductTagDto productTag = new ProductTagDto();
				productTag.setNewProduct(productData.getIsProductNew());
				productTag.setOnlineExclusive(productData.getIsOnlineExclusive());//new Boolean(true)
				target.setProductTag(productTag);
				if (null == productData.getIsOfferExisting())
				{
					target.setIsOfferExisting(Boolean.FALSE);
				}
				else
				{
					target.setIsOfferExisting(productData.getIsOfferExisting());
				}


				VariantOptions variant = null;
				final List<VariantOptions> variantOptionsList = new ArrayList<VariantOptions>();
				if (null != productData.getVariantOptions())
				{
					for (final VariantOptionData variantOption : productData.getVariantOptions())
					{
						variant = new VariantOptions();
						if (null != variantOption.getCode())
						{
							variant.setCode(variantOption.getCode());
						}
						if (null != variantOption.getColourCode())
						{
							variant.setColorcode(variantOption.getColourCode());
						}
						if (null != variantOption.getColour())
						{
							variant.setColorname(variantOption.getColour());
						}
						variantOptionsList.add(variant);
					}
				}
				target.setVariantOptions(variantOptionsList);

				final BuyBoxData buyboxData = getBuyBoxFacade().buyboxPrice(source.getCode());
				if (buyboxData != null)
				{
					target.setOfferPrice(buyboxData.getSpecialPrice() != null ? buyboxData.getSpecialPrice() : buyboxData.getPrice());
					target.setSellingPrice(buyboxData.getMrp());
				}

			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + "Buybox price is not available for a product");
			}
			catch (final Exception e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + "Exception while calling BuyBox for a product");
			}
		}
	}


	private ImageData getPrimaryImageForProductAndFormat(final ProductData product, final String format)
	{
		if (product != null && format != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				for (final ImageData image : images)
				{
					if (ImageDataType.PRIMARY.equals(image.getImageType()) && format.equals(image.getFormat()))
					{
						return image;
					}
				}
			}
		}
		return null;
	}

}
