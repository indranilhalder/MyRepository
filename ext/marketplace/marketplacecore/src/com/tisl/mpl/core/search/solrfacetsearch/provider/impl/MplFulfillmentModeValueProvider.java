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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;


/**
 * @author TCS
 *
 */
//Index size for a PcmProductVariantModel
public class MplFulfillmentModeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
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
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}


		final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();

		//if (richAttributeModel != null && richAttributeModel.size() > 0)
		if (CollectionUtils.isNotEmpty(richAttributeModel))
		{
			final DeliveryFulfillModesEnum fulfillModeEnum = richAttributeModel.get(0).getDeliveryFulfillModes();
			final String fulfillMode = fulfillModeEnum.getCode();
			if (!fulfillMode.isEmpty())
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				fieldValues.addAll(createFieldValue(fulfillMode, indexedProperty));

				return fieldValues;
			}
		}

		return Collections.emptyList();


	}

	/**
	 * @return fieldValues
	 * @param fulfillMode
	 *           ,indexedProperty
	 * @description: It create fulfillMode field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final String fulfillMode, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = fulfillMode;
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
