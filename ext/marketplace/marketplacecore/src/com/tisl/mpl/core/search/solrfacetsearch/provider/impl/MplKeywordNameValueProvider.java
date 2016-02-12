/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
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


/**
 * @author 361234
 *
 */
public class MplKeywordNameValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{




	private FieldNameProvider fieldNameProvider;

	private ClassificationService classificationService;

	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
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
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		String finalTitle = null;

		if (model instanceof ProductModel)
		{
			//Model should be instance of PcmProductVariantModel


			final ProductModel pcmproductVariantModel = (ProductModel) model;
			final String title = pcmproductVariantModel.getTitle();
			if (pcmproductVariantModel.getProductCategoryType() != null
					&& pcmproductVariantModel.getProductCategoryType().equalsIgnoreCase("Electronics"))
			{


				String titleWithColor = null;
				String colorElectronics = null;
				String titlewithouColor = null;
				String modelNumber = null;
				final List<ProductFeatureModel> productFeatures = pcmproductVariantModel.getFeatures();



				if (CollectionUtils.isNotEmpty(productFeatures))
				{



					for (final ProductFeatureModel productFeature : productFeatures)
					{
						if (null != productFeature.getClassificationAttributeAssignment()
								&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
								&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
										.equalsIgnoreCase("colorelectronics"))

						{
							final ClassificationAttributeValueModel colorElectronicsModel = (ClassificationAttributeValueModel) productFeature
									.getValue();

							if (colorElectronicsModel != null)
							{


								colorElectronics = colorElectronicsModel.getName();
								break;

							}
						}



					}


					for (final ProductFeatureModel productFeature : productFeatures)
					{
						if (null != productFeature.getClassificationAttributeAssignment()
								&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
								&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
										.equalsIgnoreCase("modelnumber"))

						{

							if (productFeature.getValue() != null && colorElectronics != null)
							{

								modelNumber = productFeature.getValue().toString();
								break;

							}

						}

					}


					if (colorElectronics != null && modelNumber != null)
					{
						titlewithouColor = title.replaceAll("(?i)" + colorElectronics, "");
						titleWithColor = titlewithouColor.replaceAll("(?i)" + modelNumber, modelNumber + " " + colorElectronics);
						finalTitle = titleWithColor + " " + "360641361234";
					}
					else
					{

						finalTitle = title;
					}
				}
				else
				{

					finalTitle = title;
				}
			}

			if (pcmproductVariantModel.getProductCategoryType() != null
					&& pcmproductVariantModel.getProductCategoryType().equalsIgnoreCase("Clothing"))
			{


				finalTitle = title + " " + "360641361234";
			}


			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(finalTitle, indexedProperty));
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
	protected List<FieldValue> createFieldValue(final String nameKeyword, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, nameKeyword));
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
