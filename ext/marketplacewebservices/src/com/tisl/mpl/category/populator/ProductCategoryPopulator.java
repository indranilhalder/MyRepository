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
package com.tisl.mpl.category.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.catalog.CatalogOption;
import de.hybris.platform.commercefacades.catalog.data.CategoryHierarchyData;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.wsdto.ProductForCategoryData;


/**
 * Populates {@link CategoryHierarchyData} from {@link CategoryModel} using specific {@link CatalogOption}s
 */
public class ProductCategoryPopulator implements Populator<CategoryModel, ProductForCategoryData>
{

	private AbstractUrlResolver<CategoryModel> categoryUrlResolver;


	@Override
	public void populate(final CategoryModel source, final ProductForCategoryData target) throws ConversionException
	{

		target.setTitle(source.getName());
		target.setBanner_image(categoryUrlResolver.resolve(source));
	}

	@Required
	public void setCategoryUrlResolver(final AbstractUrlResolver<CategoryModel> categoryUrlResolver)
	{
		this.categoryUrlResolver = categoryUrlResolver;
	}


	public AbstractUrlResolver<CategoryModel> getCategoryUrlResolver()
	{
		return categoryUrlResolver;
	}
}
