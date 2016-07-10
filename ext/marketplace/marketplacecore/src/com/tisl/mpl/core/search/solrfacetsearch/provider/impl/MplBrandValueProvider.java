package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

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

import com.tisl.mpl.core.model.BrandModel;


/**
 * @author TCS
 *
 */
public class MplBrandValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	/**
	 * @Description : Return brand name
	 * @param : final IndexConfig indexConfig, final IndexedProperty indexedProperty,final Object model
	 * @return : Collection<FieldValue>
	 */
	//Return brand name against each Product.Return brand facets.
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final List<BrandModel> brands = (List<BrandModel>) productModel.getBrands();


		if (brands != null && !brands.isEmpty())
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			for (final BrandModel brandModel : brands)
			{


				fieldValues.addAll(createFieldValue(brandModel.getName(), indexedProperty));

				return fieldValues;

			}
		}

		return Collections.emptyList();


	}


	/**
	 * @Description : Return filed values
	 * @param : final String brand, final IndexedProperty indexedProperty
	 * @return : List<FieldValue>
	 */
	//Create field value for brand name
	protected List<FieldValue> createFieldValue(final String brand, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = brand;
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}