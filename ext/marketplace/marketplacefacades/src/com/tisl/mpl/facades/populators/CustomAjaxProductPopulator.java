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
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.product.ProductModel} as source and
 * {@link de.hybris.platform.commercefacades.product.data.ProductData} as target type.
 */
public class CustomAjaxProductPopulator extends ProductUrlPopulator
{
	private Populator<ProductModel, ProductData> productBasicPopulator;
	private Populator<ProductModel, ProductData> variantSelectedPopulator;
	private Populator<ProductModel, ProductData> sellerPopulator;

	protected Populator<ProductModel, ProductData> getProductBasicPopulator()
	{
		return productBasicPopulator;
	}

	@Required
	public void setProductBasicPopulator(final Populator<ProductModel, ProductData> productBasicPopulator)
	{
		this.productBasicPopulator = productBasicPopulator;
	}

	protected Populator<ProductModel, ProductData> getVariantSelectedPopulator()
	{
		return variantSelectedPopulator;
	}

	@Required
	public void setVariantSelectedPopulator(final Populator<ProductModel, ProductData> variantSelectedPopulator)
	{
		this.variantSelectedPopulator = variantSelectedPopulator;
	}


	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		getProductBasicPopulator().populate(source, target);
		getVariantSelectedPopulator().populate(source, target);
		getSellerPopulator().populate(source, target);
		super.populate(source, target);
	}

	/**
	 * @return the sellerPopulator
	 */
	public Populator<ProductModel, ProductData> getSellerPopulator()
	{
		return sellerPopulator;
	}

	/**
	 * @param sellerPopulator
	 *           the sellerPopulator to set
	 */
	@Required
	public void setSellerPopulator(final Populator<ProductModel, ProductData> sellerPopulator)
	{
		this.sellerPopulator = sellerPopulator;
	}
}
