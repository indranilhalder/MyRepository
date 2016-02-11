/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ClassificationPropertyValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;


public class MplClassificationPropertyCodeValueProvider extends ClassificationPropertyValueProvider
{
	private FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final List<ClassAttributeAssignmentModel> classAttrAssignmentList = new ArrayList<ClassAttributeAssignmentModel>();

			final ClassAttributeAssignmentModel classAttributeAssignmentModel = indexedProperty.getClassAttributeAssignment();

			if (classAttributeAssignmentModel != null)
			{
				classAttrAssignmentList.add(classAttributeAssignmentModel);
			}
			if (indexedProperty.getClassificationAttributeAssignments() != null)
			{
				classAttrAssignmentList.addAll(indexedProperty.getClassificationAttributeAssignments());
			}

			//if (classAttrAssignmentList.size() > 0)
			if (CollectionUtils.isNotEmpty(classAttrAssignmentList))
			{

				final Product product = (Product) this.modelService.getSource(model);
				final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
				for (final ClassAttributeAssignmentModel classAttrAssignmentModel : classAttrAssignmentList)
				{

					final ClassAttributeAssignment classAttributeAssignment = (ClassAttributeAssignment) this.modelService
							.getSource(classAttrAssignmentModel);


					final FeatureContainer cont = FeatureContainer.loadTyped(product, new ClassAttributeAssignment[]
					{ classAttributeAssignment });
					if (cont.hasFeature(classAttributeAssignment))
					{
						final Feature feature = cont.getFeature(classAttributeAssignment);
						if ((feature != null) && (!feature.isEmpty()))
						{
							final List<FieldValue> temp = getFeaturesValues(indexConfig, feature, indexedProperty);
							fieldValues.addAll(temp);
						}
					}
				}
				return fieldValues;
			}

			return Collections.emptyList();
		}

		throw new FieldValueProviderException("Cannot provide classification property of non-product item");
	}

	@Override
	protected List<FieldValue> getFeaturesValues(final IndexConfig indexConfig, final Feature feature,
			final IndexedProperty indexedProperty) throws FieldValueProviderException
	{
		final List result = new ArrayList();
		List featureValues = null;
		if (!(feature.isLocalized()))
		{
			featureValues = feature.getValues();
		}
		if (indexedProperty.isLocalized())
		{
			for (final LanguageModel language : indexConfig.getLanguages())
			{
				final Locale locale = this.i18nService.getCurrentLocale();
				try
				{
					this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
					result.addAll(getFieldValues(indexedProperty, language, (feature.isLocalized()) ? feature.getValues()
							: featureValues));
				}
				finally
				{
					this.i18nService.setCurrentLocale(locale);
				}
			}

		}
		else
		{
			result.addAll(getFieldValues(indexedProperty, null, feature.getValues()));
		}
		return result;
	}

	protected List<FieldValue> getFieldValues(final IndexedProperty indexedProperty, final LanguageModel language,
			final List<FeatureValue> list) throws FieldValueProviderException
	{
		final List result = new ArrayList();

		for (final FeatureValue featureValue : list)
		{
			Object value = featureValue.getValue();
			if (value instanceof ClassificationAttributeValue)
			{
				value = ((ClassificationAttributeValue) value).getCode();
			}
			final List<String> rangeNameList = getRangeNameList(indexedProperty, value);
			final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, (language == null) ? null
					: language.getIsocode());
			for (final String fieldName : fieldNames)
			{
				if (rangeNameList.isEmpty())
				{
					result.add(new FieldValue(fieldName, value));
				}
				else
				{
					for (final String rangeName : rangeNameList)
					{
						result.add(new FieldValue(fieldName, (rangeName == null) ? value : rangeName));
					}
				}
			}
		}
		return result;
	}

	@Override
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}