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
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author 361234
 *
 */
public class MplSizeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
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
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmVariantModel = (PcmProductVariantModel) model;


			final Set<String> sizes = new TreeSet<String>();


			//	Fetch sizes in all the Variants
			for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
			{

				final PcmProductVariantModel pcmSizeVariantModel = (PcmProductVariantModel) pcmProductVariantModel;

				//SONAR Fix
				//				if (pcmSizeVariantModel.getColour() != null && pcmVariantModel.getColour() != null)
				//				{
				//
				//					if (pcmSizeVariantModel.getColour().equals(pcmVariantModel.getColour()))
				//					{
				//						//Included for Electronics Product
				//						if (pcmSizeVariantModel.getSize() != null)
				//						{
				//							sizes.add(pcmSizeVariantModel.getSize());
				//						}
				//					}
				//
				//
				//				}

				//Included for Electronics Product
				if (pcmSizeVariantModel.getColour() != null && pcmVariantModel.getColour() != null
						&& pcmSizeVariantModel.getColour().equalsIgnoreCase(pcmVariantModel.getColour())
						&& pcmSizeVariantModel.getSize() != null)
				{

					sizes.add(pcmSizeVariantModel.getSize().toUpperCase());

				}

			}


			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(sizes, indexedProperty));
			}
			//return the field values
			return fieldValues;

		}
		else
		{
			return Collections.emptyList();
		}



	}

	/**
	 * @return List<FieldValue>
	 * @param color
	 *           ,indexedProperty
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final Set<String> color, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, color));
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
