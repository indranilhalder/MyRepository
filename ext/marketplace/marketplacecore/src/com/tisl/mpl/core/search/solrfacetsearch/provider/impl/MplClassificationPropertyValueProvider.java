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
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ClassificationPropertyValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.standardizationfactory.StandardizationService;


public class MplClassificationPropertyValueProvider extends ClassificationPropertyValueProvider
{

	//private static final Logger LOG = Logger.getLogger(MplClassificationPropertyValueProvider.class);

	@Autowired
	private StandardizationService sizeStandard;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			/********** TISPRO-326 changes **********/
			if (!"Electronics".equalsIgnoreCase(((ProductModel) model).getProductCategoryType())
					&& StringUtils.isEmpty(indexedProperty.getClassificationProductType())
					||

					("Electronics".equalsIgnoreCase(((ProductModel) model).getProductCategoryType()) && "Electronics"
							.equalsIgnoreCase(indexedProperty.getClassificationProductType())))
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
					final List<ClassAttributeAssignment> classAttributeAssignmentList = new ArrayList<ClassAttributeAssignment>();
					final Product product = (Product) this.modelService.getSource(model);
					final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
					//for (final ClassAttributeAssignmentModel classAttrAssignmentModel : classAttrAssignmentList)
					//{

					/*
					 * final ClassAttributeAssignment classAttributeAssignment = (ClassAttributeAssignment) this.modelService
					 * .getSource(classAttrAssignmentModel);
					 */
					/********** TISPRO-326 changes **********/
					for (final ClassAttributeAssignmentModel classAttrAssignmentModel : classAttrAssignmentList)
					{

						final ClassAttributeAssignment classAttributeAssignment = (ClassAttributeAssignment) this.modelService
								.getSource(classAttrAssignmentModel);


						classAttributeAssignmentList.add(classAttributeAssignment);


					}

					final FeatureContainer cont = FeatureContainer.loadTyped(product, classAttributeAssignmentList);

					for (final ClassAttributeAssignment classAttributeAssignment : classAttributeAssignmentList)
					{
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

					//}
					return fieldValues;
				}

				return Collections.emptyList();


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
		//Search POC
		if (indexedProperty.isLocalized() && indexedProperty.getIsRangeFaceted().equals(Boolean.TRUE))
		{
			for (final LanguageModel language : indexConfig.getLanguages())
			{
				final Locale locale = this.i18nService.getCurrentLocale();
				try
				{
					this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
					final List<FeatureValue> listFeatureValue = featureValues;

					for (final FeatureValue singleFeatureValue : listFeatureValue)
					{
						Object value = singleFeatureValue.getValue();
						if (value instanceof ClassificationAttributeValue)
						{
							value = ((ClassificationAttributeValue) value).getName();
							//standardizing value
							final Object temp;
							if (indexedProperty.getIsNumeric().equals(Boolean.TRUE))
							{
								temp = sizeStandard.getStandardValue(value.toString(), indexedProperty.getUnitType());
							}
							else
							{
								temp = sizeStandard.getStandardValueNonNumeric(value.toString(), indexedProperty.getName(),
										indexedProperty.getUnitType());
							}
							if (temp != null)
							{
								value = temp;
							}

							singleFeatureValue.setValue(value);
							//clearing the existing value
							listFeatureValue.clear();
							//adding the standard value
							listFeatureValue.add(singleFeatureValue);
						}
					}
					result.addAll(extractFieldValues(indexedProperty, language, listFeatureValue));

					//sizeStandard.getStandardValue(value)

				}
				finally
				{
					this.i18nService.setCurrentLocale(locale);
				}
			}
		}

		else if (indexedProperty.isLocalized())
		{
			for (final LanguageModel language : indexConfig.getLanguages())
			{
				final Locale locale = this.i18nService.getCurrentLocale();
				try
				{
					this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
					result.addAll(extractFieldValues(indexedProperty, language, (feature.isLocalized()) ? feature.getValues()
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
			result.addAll(extractFieldValues(indexedProperty, null, feature.getValues()));
		}
		return result;
	}
}
