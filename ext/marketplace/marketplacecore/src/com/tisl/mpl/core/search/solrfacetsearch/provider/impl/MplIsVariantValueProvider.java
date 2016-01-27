/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

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
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author 361234
 *
 */
public class MplIsVariantValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;


	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns if teh product is base or variant
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		boolean isVariant = false;
		//if (model != null && model instanceof PcmProductVariantModel)	SONAR Fix
		if (model instanceof PcmProductVariantModel)
		{

			isVariant = true;

		}

		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		//add field values
		fieldValues.addAll(createFieldValue(isVariant, indexedProperty));
		//return the field values
		return fieldValues;





	}

	/**
	 * @return List<FieldValue>
	 * @param offersExist
	 *           ,indexedProperty
	 * @description: It returns if teh product is base or variant
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final boolean isVariant, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, isVariant));
		}
		return fieldValues;
	}

	/**
	 * @return void
	 * @param fieldNameProvider
	 * @description: Set Field name provider
	 *
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
