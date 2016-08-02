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
import de.hybris.platform.servicelayer.config.ConfigurationService;
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


public class MplClassificationPropertyValueProvider extends ClassificationPropertyValueProvider
{
	@Autowired
	private ConfigurationService configurationService;

	//private static final Logger LOG = Logger.getLogger(MplClassificationPropertyValueProvider.class);

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			//Added for Tata-24 Start :::
			final ProductModel productModel = (ProductModel) model;
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
								//Added for Tata-24 Start :::
								final String dynCategory = configurationService.getConfiguration().getString(
										"classification.attirbutes.dynamic." + productModel.getProductCategoryType());

								if (StringUtils.isNotEmpty(dynCategory))
								{
									final String[] dynProperties = dynCategory.split(",");
									for (final String dynproperty : dynProperties)
									{
										final String property = dynproperty.replaceAll(" ", "").replaceAll("-", "").toLowerCase();
										if (property.equals(indexedProperty.getExportId().toLowerCase()))
										{
											dynGroupFeaturesValues(property, feature);
											break;
										}
									}
								}
								//Added for Tata-24 END :::


								//								List featureValues = feature.getValues();
								//								for(Object value: featureValues){
								//									if("multipack".equals(indexedProperty.getName())){
								//										String featureValue=(String)value;
								//										if(!"yes".equals(featureValue)){
								//											break;
								//										}
								//									}
								//								}

								final List<FieldValue> temp = getFeaturesValues(indexConfig, feature, indexedProperty);
								if ("multipack".equals(indexedProperty.getName()))
								{
									for (final FieldValue fieldValue : temp)
									{

										final String value = (String) fieldValue.getValue();
										if (StringUtils.isNotEmpty(value) && !"yes".equals(value.toLowerCase()))
										{
											temp.remove(fieldValue);
										}
									}
								}
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
		if (indexedProperty.isLocalized())
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

	/**
	 * Method used to get local.properties configuration display for the indexed property
	 */
	public void dynGroupFeaturesValues(final String property, final Feature feature)
	{
		final List<FeatureValue> featureValues = feature.getValues();
		final List<FeatureValue> newFeatures = new ArrayList<FeatureValue>();

		//classification.attirbutes.dynamic.materialtype=Canvas,Cotton,Leather,Others,PU,Suede,Fabric,Metal,Plastic
		final String dynGroup = configurationService.getConfiguration().getString("classification.attirbutes.dynamic." + property);
		if (StringUtils.isNotEmpty(dynGroup))
		{
			boolean flag = false;
			final String[] dynGroups = dynGroup.split(",");
			if (dynGroups != null && dynGroups.length > 0)
			{
				//groupName=Canvas
				for (final String groupName : dynGroups)
				{
					final String name = groupName.replaceAll(" ", "").replaceAll("-", "").toLowerCase();
					//classification.attirbutes.dynamic.materialtype.metal=Metal,Alloys,Titanium,Aluminium,Stainless Steel
					final String dynAttribute = configurationService.getConfiguration().getString(
							"classification.attirbutes.dynamic." + property + "." + name);
					if (StringUtils.isNotEmpty(dynAttribute))
					{
						//dynAttributes=[Metal,Alloys,Titanium,Aluminium,Stainless Steel]
						final String[] dynAttributes = dynAttribute.split(",");
						if (dynAttributes != null && dynAttributes.length > 0)
						{
							for (final String attribute : dynAttributes)
							{
								//att= metal || alloys || titanium || aluminium || stainlesssteel
								final String att = attribute.replaceAll(" ", "").replaceAll("-", "").toLowerCase();
								for (final FeatureValue featureValue : featureValues)
								{
									final Object value = featureValue.getValue();
									if (value instanceof ClassificationAttributeValue)
									{
										final String valueName = ((ClassificationAttributeValue) value).getName();
										if (valueName != null && att.equals(valueName.toLowerCase()))
										{

											System.out.println("loggggggg========groupName=" + groupName);
											((ClassificationAttributeValue) value).setName(groupName);
											featureValue.setValue(value);
											flag = true;
											newFeatures.add(featureValue);
											feature.getValues().remove(featureValue);
											break;
										}

									}

								}
								if (flag)
								{
									break;
								}
							}
						}
					}
					if (flag)
					{
						break;
					}
				}
				feature.getValues().addAll(newFeatures);
			}
		}

	}
}
