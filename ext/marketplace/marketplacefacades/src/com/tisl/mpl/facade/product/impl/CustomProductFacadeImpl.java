/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;

import com.tisl.mpl.facade.product.CustomProductFacade;


/**
 * @author TCS
 *
 */
public class CustomProductFacadeImpl extends DefaultProductFacade implements CustomProductFacade
{

	private Converter<ProductModel, ProductData> customProductConverter;

	@Override
	public ProductData getProductForAjaxOptions(final ProductModel productModel, final Collection<ProductOption> options)
	{

		final ProductData productData = getCustomProductConverter().convert(productModel);
		if (options != null)
		{
			getProductConfiguredPopulator().populate(productModel, productData, options);
		}

		return productData;
	}

	/**
	 * @return the customProductConverter
	 */
	public Converter<ProductModel, ProductData> getCustomProductConverter()
	{
		return customProductConverter;
	}

	/**
	 * @param customProductConverter
	 *           the customProductConverter to set
	 */
	public void setCustomProductConverter(final Converter<ProductModel, ProductData> customProductConverter)
	{
		this.customProductConverter = customProductConverter;
	}


}
