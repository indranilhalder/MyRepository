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
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
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

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


public class MplClassificationColourCodeValueProvider extends ClassificationPropertyValueProvider
{
	private FieldNameProvider fieldNameProvider;


	public static final String COLORFAMILYFOOTWEAR = "colorfamilyfootwear";
	//For TPR-3955
	public static final String COLORFAMILYTRLG = "colorfamilytrlg";
	public static final String COLORFAMILY_BLANK = "";

	// For TPR-1886 | Fine jewellery
	public static final String COLOR_FINE_JEWELLERY = "colorfinejwlry";
	public static final String COMMA = ",";

	//public static final String COLORFAMILYFOOTWEAR = "colorfamilyfootwear";
	//public static final String COLORFAMILYFOOTWEARBLANK = "";


	@Autowired
	private ConfigurationService configurationService;

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
		//final PcmProductVariantModel pcmVariantProductModel = (PcmProductVariantModel) variantProductModel;
		for (final FeatureValue featureValue : list)
		{
			Object value = featureValue.getValue();

			if (value instanceof ClassificationAttributeValue)
			{
				value = ((ClassificationAttributeValue) value).getCode();
				value = value.toString().toLowerCase();
				if (value.toString().startsWith(MarketplaceCoreConstants.COLORFAMILYFOOTWEAR))
				               
				{   value = value.toString().replaceAll(MarketplaceCoreConstants.COLORFAMILYFOOTWEAR,
						MarketplaceCoreConstants.COLORFAMILYFOOTWEARBLANK);
					//value = value.toString().replaceAll(COLORFAMILYFOOTWEAR, COLORFAMILY_BLANK);
				}
				//For TPR-3955
				else if (value.toString().startsWith(COLORFAMILYTRLG))
				{
					value = value.toString().replaceAll(COLORFAMILYTRLG, COLORFAMILY_BLANK);
				}
				// For TPR-1886 | Fine jewellery
				else if (value.toString().contains(COLOR_FINE_JEWELLERY))
				{
					final String jewelleryColourFeatures = configurationService.getConfiguration()
							.getString("jewelleryColourFeatures");
					final String[] jewelleryColourFeatureList = jewelleryColourFeatures.split(COMMA);
					for (final String colorFeature : jewelleryColourFeatureList)
					{
						if (value.toString().startsWith(colorFeature))
						{
							value = value.toString().replaceAll(colorFeature.toString(), COLORFAMILY_BLANK);
							break;
						}
					}

					

				}
				// INC_12606 starts
				else if (value.toString().startsWith(MarketplaceCoreConstants.DIALCOLORELECTRONICS))
				{
					value = value.toString().replaceAll(MarketplaceCoreConstants.DIALCOLORELECTRONICS,
							MarketplaceCoreConstants.DIALCOLORELECTRONICSBLANK);
				}
				// INC_12606 ends
			}
			final List<String> rangeNameList = getRangeNameList(indexedProperty, value);
			final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, (language == null) ? null
					: language.getIsocode());

			// Construct colour hexcode here
			final String colour = (String) value;
			if (colour != null && !StringUtils.isEmpty(colour))
			{
				final Object colourCode = getColorWithHexCode(colour.toLowerCase());

				for (final String fieldName : fieldNames)
				{
					if (rangeNameList.isEmpty())
					{
						result.add(new FieldValue(fieldName, colourCode));
					}
					else
					{
						for (final String rangeName : rangeNameList)
						{
							result.add(new FieldValue(fieldName, (rangeName == null) ? colourCode : rangeName));
						}
					}
				}
			}
			else
			{
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
		}
		return result;
	}

	private String getColorWithHexCode(final String color)
	{
		if (!color.isEmpty())
		{
			final String colorWithCode = StringUtils.capitalize(color) + "_" + getColorHexCode(color);
			return colorWithCode;
		}
		return "";
	}

	private String getColorHexCode(final String color)
	{

		if (color.equalsIgnoreCase("multi"))
		{
			return color;
		}
		final String colorHexCode = configurationService.getConfiguration().getString("colorhexcode." + color);
		if (colorHexCode != null && colorHexCode.contains("#"))
		{
			return colorHexCode.replace("#", "");
		}
		return color;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
