/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author 584443
 *
 */
public class MplCompareProductValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the size of a specific variant product
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			//Model should be instance of PcmProductVariantModel
			final ProductModel productModel = (ProductModel) model;
			String categoryCode = null;
			//Get size for a product
			final Collection<CategoryModel> categories = productModel.getSupercategories();
			//If size is not empty
			if (categories != null && !categories.isEmpty())
			{

				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
				for (final CategoryModel category : categories)
				{
					if (category.getCode().startsWith("MSH"))
					{
						categoryCode = category.getCode();
						break;
					}

				}
				if (categoryCode != null)
				{
					fieldValues.addAll(createFieldValue(categoryCode, indexedProperty));
					return fieldValues;
				}
				else
				{
					return Collections.emptyList();
				}
			}
			else
			{
				return Collections.emptyList();
			}
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @return List<FieldValue>
	 * @param size
	 *           ,indexedProperty
	 * @description: It creates field values
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final String size, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			//Add field values
			fieldValues.add(new FieldValue(fieldName, size));
		}
		return fieldValues;
	}

	/**
	 * @return void
	 * @param fieldNameProvider
	 * @description: It set FieldNameProvider
	 *
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
