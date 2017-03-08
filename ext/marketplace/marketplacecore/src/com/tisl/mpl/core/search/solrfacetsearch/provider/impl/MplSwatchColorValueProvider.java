/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 361234
 *
 */
public class MplSwatchColorValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	@Autowired
	private ConfigurationService configurationService;

	private MplBuyBoxUtility mplBuyBoxUtility;

	/**
	 * @return the mplBuyBoxUtility
	 */
	public MplBuyBoxUtility getMplBuyBoxUtility()
	{
		return mplBuyBoxUtility;
	}

	/**
	 * @param mplBuyBoxUtility
	 *           the mplBuyBoxUtility to set
	 */
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
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
	 * @description: It returns the color of all the variant product except the current variant
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

			final HashSet<String> colours = new HashSet<String>();

			String variantColor = null;
			String variantColorHexCode = null;
			String pcmSwatchVariantColor = null;
			String pcmVariantColour = null;

			//Fetch colors in all the Variants
			for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
			{

				final PcmProductVariantModel pcmSwatchVariantModel = (PcmProductVariantModel) pcmProductVariantModel;

				pcmSwatchVariantColor = mplBuyBoxUtility.getVariantColour(pcmSwatchVariantModel);
				pcmVariantColour = mplBuyBoxUtility.getVariantColour(pcmVariantModel);

				if (pcmSwatchVariantColor != null && pcmVariantColour != null)
				{

					if (!pcmSwatchVariantColor.equalsIgnoreCase(pcmVariantColour))
					{
						variantColorHexCode = pcmSwatchVariantModel.getColourHexCode();

						//if (pcmSwatchVariantModel.getColourHexCode() == null || pcmSwatchVariantModel.getColourHexCode().isEmpty())
						if (StringUtils.isBlank(variantColorHexCode))
						{
							variantColor = getColorWithHexCode(pcmSwatchVariantColor.toLowerCase());
						}
						else
						{
							variantColor = StringUtils.capitalize(pcmSwatchVariantColor) + "_"
									+ (variantColorHexCode.contains("#") ? variantColorHexCode.replace("#", "") : variantColorHexCode);
						}
						variantColor = variantColor + "||" + pcmSwatchVariantModel.getCode();
						colours.add(variantColor);

					}

				}


			}

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				//fieldValues.addAll(createFieldValue(hashMaptoSet(colours), indexedProperty));
				fieldValues.addAll(createFieldValue(colours, indexedProperty));
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
	 * @description: It returns the colours of all the variant product
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

	private Set<String> hashMaptoSet(final Map<String, String> colours)
	{
		final Set<String> colourSet = new HashSet<>();
		for (final Map.Entry<String, String> entry : colours.entrySet())
		{
			colourSet.add(entry.getKey() + "||" + entry.getValue());
		}

		return colourSet;
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
}
