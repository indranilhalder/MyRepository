/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.tisl.mpl.facade.converter.MplProductConverter;
import com.tisl.mpl.facades.cms.data.CollectionComponentData;
import com.tisl.mpl.facades.cms.data.CollectionProductData;
import com.tisl.mpl.model.cms.components.MobileCollectionBannerComponentModel;


/**
 * @author 584443
 *
 */
public class DefaultMobileCollectionComponentPopulator
		implements Populator<MobileCollectionBannerComponentModel, CollectionComponentData>
{
	private Converter<ProductModel, CollectionProductData> mobileCollectionProductConverter;
	private MplProductConverter mplProductConverter;

	/**
	 * @return the mplProductConverter
	 */
	public MplProductConverter getMplProductConverter()
	{
		return mplProductConverter;
	}

	/**
	 * @param mplProductConverter
	 *           the mplProductConverter to set
	 */
	public void setMplProductConverter(final MplProductConverter mplProductConverter)
	{
		this.mplProductConverter = mplProductConverter;
	}

	/**
	 * @return the mobileCollectionProductConverter
	 */
	public Converter<ProductModel, CollectionProductData> getMobileCollectionProductConverter()
	{
		return mobileCollectionProductConverter;
	}

	/**
	 * @param mobileCollectionProductConverter
	 *           the mobileCollectionProductConverter to set
	 */
	public void setMobileCollectionProductConverter(
			final Converter<ProductModel, CollectionProductData> mobileCollectionProductConverter)
	{
		this.mobileCollectionProductConverter = mobileCollectionProductConverter;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MobileCollectionBannerComponentModel source, final CollectionComponentData target)
			throws ConversionException
	{
		// YTODO Auto-generated method stub

		target.setImage(source.getImage() != null ? source.getImage().getURL() : null);
		target.setDecription(source.getDescription());
		target.setTitle(source.getTitle());
		target.setProducts(mplProductConverter.convertAll(source.getProducts(), getMobileCollectionProductConverter()));
	}

}
