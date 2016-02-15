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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
public class MplProductColourGroupValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
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
	 * @description: It returns the size of a specific base product color group as string
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel pcmColorModel = (PcmProductVariantModel) model;
			//Retrieving base product PK
			final String baseProductPK = pcmColorModel.getBaseProduct().getPk().getLongValueAsString();
			final String colors = pcmColorModel.getColour();

			if (colors != null && !colors.isEmpty())
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					//indexing base product and color
					fieldValues.addAll(createFieldValue(StringUtils.capitalize(colors), indexedProperty, baseProductPK));
				}
				return fieldValues;
			}
			else
			{
				return Collections.emptyList();
			}
		}
		else
		{

			final ProductModel pcmModel = (ProductModel) model;

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			if (pcmModel != null)
			{
				fieldValues.addAll(createSimpleFieldValue(indexedProperty, pcmModel.getCode()));
				return fieldValues;

			}
			else
			{
				return Collections.emptyList();
			}
			//indexing base product and color


		}
	}

	//Create field value
	protected List<FieldValue> createFieldValue(final String color, final IndexedProperty indexedProperty,
			final String baseProductPK)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, color.toLowerCase() + baseProductPK));
		}
		return fieldValues;
	}

	//Create field value
	protected List<FieldValue> createSimpleFieldValue(final IndexedProperty indexedProperty, final String productCode)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, productCode));
		}
		return fieldValues;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
