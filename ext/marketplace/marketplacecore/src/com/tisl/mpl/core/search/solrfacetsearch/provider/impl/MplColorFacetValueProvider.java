/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.search.provider.helper.MplCache;


/**
 * @author TCS
 *
 */
public class MplColorFacetValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	/** The product variant colour cache. */

	@Autowired
	MplCache<String> mplColourCache;

	@Autowired
	private ConfigurationService configurationService;

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
	 * @description: It returns the colour of a specific variant product
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		//Only variant models need to be considered
		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel pcmColorModel = (PcmProductVariantModel) model;
			final String variantColorHexCode = pcmColorModel.getColourHexCode();
			String variantColor = "";
			if (variantColorHexCode == null || variantColorHexCode.isEmpty())
			{
				variantColor = getColorWithHexCode(pcmColorModel.getColour().toLowerCase());
			}
			else
			{
				variantColor = StringUtils.capitalize(pcmColorModel.getColour()) + "_"
						+ (variantColorHexCode.contains("#") ? variantColorHexCode.replace("#", "") : variantColorHexCode);
			}
			// If the product is not approved then excluding it's colors from the list.
			if (!ArticleApprovalStatus.APPROVED.equals(pcmColorModel.getApprovalStatus()))
			{
				//Return empty list
				return Collections.emptyList();
			}
			//Initializing a list of colors
			List<String> colorsList = new ArrayList();
			/*
			 * If mplColourCache contains product variant pk then retrieve colors and check if current product color is
			 * already present in the cache If the color is present dont include in field value provider, return an empty
			 * list, else return field value provider.
			 */
			if (mplColourCache.get(pcmColorModel.getBaseProduct().getPk().toString()) != null)
			{
				//Retrieve list of colors from cache
				colorsList = mplColourCache.get(pcmColorModel.getBaseProduct().getPk().toString());
				if (!colorsList.isEmpty())
				{
					//Check if the color is present in the list
					if (!colorsList.contains(variantColor))
					{
						colorsList.add(variantColor);
						fieldValues.addAll(createFieldValue(variantColor, indexedProperty));
						mplColourCache.put(pcmColorModel.getBaseProduct().getPk().toString(), colorsList);
						return fieldValues;
					}
					else
					{
						//Don't add the color in field value provider.
						return Collections.emptyList();
					}
				}
				else
				{
					throw new FieldValueProviderException(
							"Either none of the variants for this product are APPROVED or the variants do not have any colours linked. Product Listing Id = "
									+ pcmColorModel.getCode());

				}

			}
			else
			{
				//there is no data in cache for the listing id, add the listing id and the product.
				colorsList.add(variantColor);
				fieldValues.addAll(createFieldValue(variantColor, indexedProperty));
				mplColourCache.put(pcmColorModel.getBaseProduct().getPk().toString(), colorsList);
				return fieldValues;
			}
		}
		//By default return empty list
		return Collections.emptyList();
	}

	/**
	 * @return List<FieldValue>
	 * @param color
	 *           ,indexedProperty
	 * @description: It returns the colour of a specific variant product
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final String color, final IndexedProperty indexedProperty)
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
