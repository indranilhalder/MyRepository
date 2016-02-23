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
 * @author TCS
 *
 */
//Index promotedProductPriority for a ProductModel
public class MplPromotedProductPriorityValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	private FieldNameProvider fieldNameProvider;


	@SuppressWarnings("deprecation")
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		int promotedProductPriority = 0;
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (null != productModel.getPromotedProductPriority())
		{
			promotedProductPriority = productModel.getPromotedProductPriority().intValue();

		}

		fieldValues.addAll(createFieldValue(promotedProductPriority, indexedProperty));

		//return the field values
		return fieldValues;

	}


	/**
	 * @return fieldValues
	 * @param isPromotedProduct
	 *           ,indexedProperty
	 * @description: It creates isPromotedProduct field with adding index property
	 *
	 */
	@SuppressWarnings("boxing")
	protected List<FieldValue> createFieldValue(final int promotedProductPriority, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Object value = promotedProductPriority;
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
