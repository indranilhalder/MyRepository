/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commercesearch.search.solrfacetsearch.populators.BoostSearchQueryPopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;


/**
 * @author TCS
 *
 */
public class MplSorlBoostQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		extends
		BoostSearchQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
{
	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{
		super.populate(source, target);
		final SearchQuery searchQuery = target.getSearchQuery();
		final List<OrderField> orderFields = searchQuery.getOrderFields();
		final OrderField firstOrderField = orderFields.get(0);
		if (("score".equals(firstOrderField.getField())))
		{
			orderFields.remove(0);
		}
	}
}
