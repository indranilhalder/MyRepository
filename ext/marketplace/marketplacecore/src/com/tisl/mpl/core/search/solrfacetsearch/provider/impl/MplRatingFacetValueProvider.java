/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
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

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author 361234
 *
 */
public class MplRatingFacetValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{




	private FieldNameProvider fieldNameProvider;

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}



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
	 * @description: It returns the average rating of the specific variant product
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel product = (PcmProductVariantModel) model;

			if (null != product.getRatingReview())
			{
				final Double averageRating = product.getRatingReview().getMplAverageRating();
				//If averageRating is not empty
				if (averageRating != null)
				{
					final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

					{
						final Collection<LanguageModel> languages = indexConfig.getLanguages();

						for (final LanguageModel language : languages)
						{
							fieldValues.addAll(createFieldValue(averageRating, language, indexedProperty));
						}

					}
					//return the field values
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
	protected List<FieldValue> createFieldValue(final Double avgRating, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		addFieldValues(fieldValues, language, indexedProperty, avgRating);
		/*
		 * for (final String fieldName : fieldNames) { //Add field values fieldValues.add(new FieldValue(fieldName,
		 * avgRating)); }
		 */
		return fieldValues;
	}


	protected void addFieldValues(final List<FieldValue> fieldValues, final LanguageModel language,
			final IndexedProperty indexedProperty, final Object value)
	{
		List<String> rangeNameList = null;
		try
		{
			rangeNameList = getRangeNameList(indexedProperty, value);
		}
		catch (final FieldValueProviderException e)
		{
			LOG.error("Could not get Range value", e);
		}
		String rangeName = null;
		if (CollectionUtils.isNotEmpty(rangeNameList))
		{
			rangeName = rangeNameList.get(0);
		}


		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		final Object valueToPass = (rangeName == null ? value : rangeName);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, valueToPass));
		}
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
