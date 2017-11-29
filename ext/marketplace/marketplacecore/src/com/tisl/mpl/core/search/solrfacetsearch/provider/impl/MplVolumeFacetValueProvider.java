/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
//Index size for a PcmProductVariantModel
public class MplVolumeFacetValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;


	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmSizeModel = (PcmProductVariantModel) model;
			//Get size for a product
			String volume = null;

			final List<String> volumeCategoryList = Arrays.asList(getConfigurationService().getConfiguration()
					.getString("mpl.homefurnishing.category.volume", "").split(","));


			if (MarketplaceCoreConstants.HOME_FURNISHING.equalsIgnoreCase(pcmSizeModel.getProductCategoryType()))
			{
				final Collection<CategoryModel> superCategories = pcmSizeModel.getSupercategories();
				if (CollectionUtils.isNotEmpty(superCategories) && CollectionUtils.isNotEmpty(volumeCategoryList))
				{
					for (final CategoryModel primaryCategory : superCategories)
					{
						if (primaryCategory != null && StringUtils.isNotEmpty(primaryCategory.getCode()))
						{
							if (volumeCategoryList.contains(primaryCategory.getCode()))
							{
								volume = pcmSizeModel.getSize();
							}
						}

					}
				}
				if (volume.equalsIgnoreCase(MarketplaceCoreConstants.NOSIZE))
				{
					return Collections.emptyList();
				}
			}

			if (StringUtils.isNotEmpty(volume))
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					//add field values
					fieldValues.addAll(createFieldValue(volume, indexedProperty));
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

	/**
	 * @return List<FieldValue>
	 * @param size
	 *           ,indexedProperty
	 * @description: It creates field values
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final String size, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			//Add field values
			fieldValues.add(new FieldValue(fieldName, size.toUpperCase()));
		}
		return fieldValues;
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

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}
}
