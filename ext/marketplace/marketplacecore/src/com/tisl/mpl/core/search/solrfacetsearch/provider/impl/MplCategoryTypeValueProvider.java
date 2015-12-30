/**
 *
 */
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


/**
 * @author 361234
 *
 */
public class MplCategoryTypeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;

	/**
	 * @Description : Return Category Type
	 * @param :
	 *           final IndexConfig indexConfig, final IndexedProperty indexedProperty,final Object model
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

		String productCategoryType = productModel.getProductCategoryType();

		if (productCategoryType != null && !productCategoryType.isEmpty())
		{

			if (productCategoryType.equalsIgnoreCase("Clothing"))
			{

				productCategoryType = "Apparel";
			}
			else
			{

				productCategoryType = "Electronics";

			}

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			fieldValues.addAll(createFieldValue(productCategoryType, indexedProperty));

			return fieldValues;

		}

		return Collections.emptyList();


	}


	/**
	 * @Description : Return filed values
	 * @param :
	 *           final String brand, final IndexedProperty indexedProperty
	 * @return : List<FieldValue>
	 */
	//Create field value for brand name
	protected List<FieldValue> createFieldValue(final String productCategoryType, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = productCategoryType;
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
