/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.CommerceIndexedPropertyPopulator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.enums.SolrIndexedPropertyFacetType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

import org.apache.commons.lang.StringUtils;


/**
 * @author 361234
 *
 */
public class MplCommerceIndexedPropertyPopulator extends CommerceIndexedPropertyPopulator
{


	@Override
	public void populate(final SolrIndexedPropertyModel property, final IndexedProperty indexedProperty) throws ConversionException
	{
		indexedProperty.setCategoryField(property.isCategoryField());
		indexedProperty.setPriority(property.getPriority());
		indexedProperty.setVisible(property.isVisible());
		indexedProperty.setDisplayName(property.getDisplayName());

		if (property.getQueryType() == null || StringUtils.isEmpty(property.getQueryType()))
		{

			indexedProperty.setClassAttributeAssignment(property.getClassAttributeAssignment());

		}

		indexedProperty.setTopValuesProvider(property.getTopValuesProvider());

		if (indexedProperty.isFacet())
		{
			if (SolrIndexedPropertyFacetType.MULTISELECTAND.equals(property.getFacetType()))
			{
				indexedProperty.setMultiSelect(true);
			}
			else if (SolrIndexedPropertyFacetType.MULTISELECTOR.equals(property.getFacetType()))
			{
				indexedProperty.setMultiSelect(true);
			}
			else
			{
				indexedProperty.setMultiSelect(false);
			}

			indexedProperty.setFacetSortProvider(property.getCustomFacetSortProvider());
		}

		indexedProperty.setValueProviderParameter(property.getValueProviderParameter());
		indexedProperty.setAutoSuggest(Boolean.TRUE.equals(property.getUseForAutocomplete()));
		indexedProperty.setSpellCheck(Boolean.TRUE.equals(property.getUseForSpellchecking()));
	}

}
