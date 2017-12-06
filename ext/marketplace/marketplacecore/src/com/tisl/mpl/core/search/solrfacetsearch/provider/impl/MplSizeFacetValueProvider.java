/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

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

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.standardizationfactory.StandardizationService;


/**
 * @author TCS
 *
 */
//Index size for a PcmProductVariantModel
public class MplSizeFacetValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	//For TPR:4847: size facet clubbing for kidswear
	@Autowired
	private StandardizationService sizeStandard;

	//For TPR:4847: size facet clubbing for kidswear

	//	@Autowired
	//	private ConfigurationService configurationService;

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
	 * @description: It returns the size of a specific variant product
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmColorModel = (PcmProductVariantModel) model;
			//Get size for a product
			String size = pcmColorModel.getSize();
			String kidswearSize = "";

			/**
			 * This logic used to fix issue: TISREL-654 ('Size' facet shouldn't get displayed in the PLP of Belts category)
			 */
			if ("Accessories".equalsIgnoreCase(pcmColorModel.getProductCategoryType()))
			{
				return Collections.emptyList();
			}
			if (MarketplaceCoreConstants.HOME_FURNISHING.equalsIgnoreCase(pcmColorModel.getProductCategoryType())
					&& size.equalsIgnoreCase(MarketplaceCoreConstants.NOSIZE))
			{
				return Collections.emptyList();
			}
			//For TPR:4847: size facet clubbing
			if ("Kidswear".equalsIgnoreCase(pcmColorModel.getProductCategoryTypeL2()))
			{
				kidswearSize = sizeStandard.getStandardValueNonNumeric(size, "KidswearSize", "0.0");

				if (null != kidswearSize && StringUtils.isNotEmpty(kidswearSize))
				{
					size = kidswearSize;
				}
			}
			//For TPR:4847: size facet clubbing
			/**
			 * Fix issue : TISREL-654 End
			 */
			//If size is not empty
			if (size != null && !size.isEmpty())
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					//add field values
					fieldValues.addAll(createFieldValue(size, indexedProperty));
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
}
