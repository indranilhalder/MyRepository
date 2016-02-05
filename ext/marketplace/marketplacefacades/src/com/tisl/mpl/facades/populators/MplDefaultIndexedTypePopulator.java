/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultIndexedTypePopulator;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author 360641
 *
 */
public class MplDefaultIndexedTypePopulator extends DefaultIndexedTypePopulator
{
	private Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter;

	@Override
	protected Collection<IndexedProperty> getIndexedPropertiesFromItems(final SolrIndexedTypeModel itemTypeModel)
			throws FacetConfigServiceException
	{
		final Collection result = new ArrayList();
		for (final SolrIndexedPropertyModel property : itemTypeModel.getSolrIndexedProperties())
		{
			property.setQueryType(itemTypeModel.getQueryType());
			final IndexedProperty indexedProperty = getIndexedPropertyFromItem(property);
			result.add(indexedProperty);
		}
		return result;
	}

	@Override
	protected IndexedProperty getIndexedPropertyFromItem(final SolrIndexedPropertyModel property)
			throws FacetConfigServiceException
	{
		return (this.indexedPropertyConverter.convert(property));
	}

	@Override
	public void setIndexedPropertyConverter(final Converter<SolrIndexedPropertyModel, IndexedProperty> indexedPropertyConverter)
	{
		this.indexedPropertyConverter = indexedPropertyConverter;
	}

}
