/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.standardizationfactory.StandardizationService;




public class MplClassificationPropertyValueProvider extends ClassificationPropertyValueProvider
{
	@Autowired
	private ConfigurationService configurationService;

	//private static final Logger LOG = Logger.getLogger(MplClassificationPropertyValueProvider.class);

	@Autowired
	private StandardizationService sizeStandard;

	//private static final String DYNAMICATTRIBUTE = "classification.attirbutes.dynamic.";

	private static final String ELECTRONICS = "Electronics".intern();

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		try
		{
			if (model instanceof ProductModel)
			{
				//Added for Tata-24 Start :::
				final ProductModel productModel = (ProductModel) model;
				final String classificationType = indexedProperty.getClassificationProductType();

				/********** TISPRO-326 changes **********/
				if ((!ELECTRONICS.equalsIgnoreCase(((ProductModel) model).getProductCategoryType()) && (StringUtils
						.isEmpty(classificationType) || isAllowedClassificationType(classificationType)))
						||

						(ELECTRONICS.equalsIgnoreCase(((ProductModel) model).getProductCategoryType()) && ELECTRONICS
								.equalsIgnoreCase(classificationType)))
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
						 * final ClassAttributeAssignment classAttributeAssignment = (ClassAttributeAssignment)
						 * this.modelService .getSource(classAttrAssignmentModel);
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
									//Added for Tata-24 Start :::
									final String dynCategory = configurationService.getConfiguration().getString(
									/* DYNAMICATTRIBUTE + productModel.getProductCategoryType()); */
									MarketplaceCoreConstants.DYNAMICATTRIBUTE + productModel.getProductCategoryType());

									if (StringUtils.isNotEmpty(dynCategory))
									{
										final String[] dynProperties = dynCategory.split(",");
										for (final String dynproperty : dynProperties)
										{
											final String property = dynproperty.replaceAll(" ", "").replaceAll("-", "").toLowerCase();

											if (StringUtils.isNotEmpty(indexedProperty.getName()))
											{
												final String name = indexedProperty.getName().replaceAll(" ", "").replaceAll("-", "")
														.toLowerCase();
												if (StringUtils.isNotEmpty(name) && property.equals(name))
												{
													/*
													 * System.out.println("----------------Current Product Code: " +
													 * product.getCode() + " ========exportid: " +
													 * indexedProperty.getExportId().toLowerCase() + "& name : " + name);
													 */
													dynGroupFeaturesValues(property, temp);
													break;
												}
											}

										}
									}
									//Added for Tata-24 END :::
									if ("multipack".equalsIgnoreCase(indexedProperty.getName()))
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
									/*
									 * MDD Requirement Here: Features ==> This facet will mainly use the attribute
									 * specialfeatureswatches (PIM_WATCH_028) from the MDD and use all LOVs from this attribute
									 * on the UI. In addition, if the attribute waterresistancewatches (PIM_WATCH_011) is present
									 * and set to a value more than 50m then an additional LOV called â€œWater Resistantâ€� must
									 * be added to the LOV for this facet automatically.
									 */
									if ("features".equalsIgnoreCase(indexedProperty.getName()))
									{
										final ClassificationAttribute attribute = classAttributeAssignment.getClassificationAttribute();
										final String classificationAttrCode = attribute != null
												&& StringUtils.isNotEmpty(attribute.getCode()) ? attribute.getCode() : "";

										if ("waterresistancewatches".equalsIgnoreCase(classificationAttrCode))
										{
											FieldValue newValue = null;
											for (final FieldValue fieldValue : temp)
											{
												String valueStr = (String) fieldValue.getValue();
												if (StringUtils.isNotEmpty(valueStr) && (valueStr.contains("m")) && valueStr.length() >= 2)
												{
													valueStr = valueStr.substring(0, valueStr.length() - 2);
												}
												final long value = Long.parseLong(valueStr);
												if (Long.compare(value, 50) > 0)
												{
													newValue = new FieldValue(fieldValue.getFieldName(), "Water Resistant");
													temp.remove(fieldValue);
													temp.add(newValue);
													break;
												}
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
			else
			/* added part of value provider go through */
			{
				throw new FieldValueProviderException("Cannot provide classification property of non-product item");
			}
		}
		catch (final Exception e) /* Try added part of value provider go through */
		{
			throw new FieldValueProviderException("Cannot evaluate " + indexedProperty.getName() + " using "
					+ super.getClass().getName() + "exception" + e, e);
		}
		//throw new FieldValueProviderException("Cannot provide classification property of non-product item");
	}


	private boolean isAllowedClassificationType(final String classificationType)
	{
		final String categoryNameList = configurationService.getConfiguration().getString("classification.index.category", " ");
		if (StringUtils.isNotEmpty(classificationType) && StringUtils.isNotEmpty(categoryNameList))
		{
			LOG.debug("Validating ClassificationType>> " + classificationType);
			final List<String> categoryList = new ArrayList<String>(Arrays.asList(categoryNameList.split(" , ")));
			if (categoryList.contains(classificationType))
			{
				return true;
			}
		}
		return false;
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
							Object temp = null;
							if (indexedProperty.getClassificationProductType()
									.equalsIgnoreCase(MarketplaceCoreConstants.HOME_FURNISHING)
									&& StringUtils.isNotEmpty(value.toString())
									&& value.toString().equalsIgnoreCase(MarketplaceCoreConstants.NOSIZE))
							{
								//Assigning null for No Size for classification Attribute
								value = null;

							}

							else if (indexedProperty.getIsNumeric().equals(Boolean.TRUE))
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

			//TPR-3548 Start
			/*
			 * if (indexedProperty.getIsNumericRange().equals(Boolean.TRUE)) { for (final LanguageModel language :
			 * indexConfig.getLanguages()) {
			 *
			 * final Locale locale = this.i18nService.getCurrentLocale(); try { merge issue fixed.
			 * this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
			 *
			 * final List<FeatureValue> listFeatureValue = featureValues;
			 *
			 * for (final FeatureValue singleFeatureValue : listFeatureValue) { Object value =
			 * singleFeatureValue.getValue(); //if (null != value && value instanceof String) SONAR FIX JEWELLERY if (value
			 * instanceof String) { final String vString = (String) value; value = Double.valueOf(vString); }
			 * singleFeatureValue.setValue(value); //clearing the existing value listFeatureValue.clear(); //adding the
			 * standard value listFeatureValue.add(singleFeatureValue); }
			 *
			 * //result.addAll(extractFieldValues(indexedProperty, language, listFeatureValue));
			 * result.addAll(extractFieldValues(indexedProperty, language, (feature.isLocalized()) ? feature.getValues() :
			 * featureValues)); } finally { this.i18nService.setCurrentLocale(locale); }
			 *
			 *
			 *
			 * } } //TPR-3548 End else {
			 */
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
			//}

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

	public void dynGroupFeaturesValues(final String property, final List<FieldValue> list)
	{

		final String dynGroup = configurationService.getConfiguration().getString(
				MarketplaceCoreConstants.DYNAMICATTRIBUTE + property);
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
					//DYNAMICATTRIBUTE + property + "." + name);
							MarketplaceCoreConstants.DYNAMICATTRIBUTE + property + "." + name);

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
								FieldValue newValue = null;
								for (final FieldValue fieldValue : list)
								{
									final String valueStr = (String) fieldValue.getValue();
									if (StringUtils.isNotEmpty(valueStr))
									{
										final String formattedValueStr = valueStr.replaceAll(" ", "").replaceAll("-", "").toLowerCase();
										if (att.equals(formattedValueStr))
										{
											newValue = new FieldValue(fieldValue.getFieldName(), groupName);
											list.remove(fieldValue);
											list.add(newValue);
											flag = true;
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
			}
		}
	}

	public void dynGroupFeaturesValues(final String property, final Feature feature)
	{
		final List<FeatureValue> featureValues = feature.getValues();
		final List<FeatureValue> newFeatures = new ArrayList<FeatureValue>();

		//classification.attirbutes.dynamic.materialtype=Canvas,Cotton,Leather,Others,PU,Suede,Fabric,Metal,Plastic
		final String dynGroup = configurationService.getConfiguration().getString(
				MarketplaceCoreConstants.DYNAMICATTRIBUTE + property);
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

					//DYNAMICATTRIBUTE + property + "." + name);

							MarketplaceCoreConstants.DYNAMICATTRIBUTE + property + "." + name);

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
										if (valueName != null && StringUtils.isNotEmpty(valueName))
										{
											final String formattedValueName = valueName.replaceAll(" ", "").replaceAll("-", "")
													.toLowerCase();
											if (att.equals(formattedValueName))
											{
												((ClassificationAttributeValue) value).setName(groupName);
												featureValue.setValue(value);
												flag = true;
												newFeatures.add(featureValue);
												feature.getValues().remove(featureValue);
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
