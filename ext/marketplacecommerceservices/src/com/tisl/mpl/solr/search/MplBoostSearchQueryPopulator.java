/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.commercesearch.search.solrfacetsearch.populators.BoostSearchQueryPopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * @author 885010
 *
 */
public class MplBoostSearchQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		extends
		BoostSearchQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
{
	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{
		final IndexedTypeSort indexedSort = (IndexedTypeSort) target.getCurrentSort();
		if (isBoostDisabled(indexedSort))
		{
			return;
		}

		final SearchQuery searchQuery = target.getSearchQuery();
		searchQuery.setQueryParser(SearchQuery.QueryParser.EDISMAX);
		final IndexedType indexedType = (IndexedType) target.getIndexedType();
		final FacetSearchConfig facetConfig = (FacetSearchConfig) target.getFacetSearchConfig();

		if (StringUtils.isBlank(searchQuery.getUserQuery()))
		{
			searchQuery.addRawQuery("*:*", SearchQuery.Operator.OR);
		}
		final String categoryCode = getSelectedCategory(source);
		//		if (StringUtils.isNotBlank(categoryCode)) {
		//			addHeroProductBoosts(searchQuery, categoryCode);
		//		}
		addConfiguredProductBoosts(searchQuery, indexedType, categoryCode, facetConfig);

		final List orderFields = searchQuery.getOrderFields();
		final OrderField firstOrderField = (OrderField) orderFields.get(0);

		if (("score".equals(firstOrderField.getField())) && (!(firstOrderField.isAscending())))
		{
			return;
		}
		//orderFields.add(0, new OrderField("score", false));
	}
}
