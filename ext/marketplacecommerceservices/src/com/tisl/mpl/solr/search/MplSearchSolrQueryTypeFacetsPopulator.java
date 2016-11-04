/**
 *
 */
package com.tisl.mpl.solr.search;


import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Prabhu Selvaraj
 *
 */

public class MplSearchSolrQueryTypeFacetsPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{

		final PageableData pageableData = source.getPageableData();

		if (null != pageableData)
		{

			final String pageFacets = pageableData.getPageFacets();

			if (null != pageFacets && !pageFacets.isEmpty())
			{
				final Set<String> pageTypeFacets = new HashSet<String>(Arrays.asList(pageFacets.split("&")));

				target.setIndexedType(getIndexedType(target.getFacetSearchConfig(), pageTypeFacets));
			}

		}

	}

	protected IndexedType getIndexedType(final FacetSearchConfig config, final Set<String> pageTypeFacets)
	{

		final IndexConfig indexConfig = config.getIndexConfig();
		final Collection indexedTypes = indexConfig.getIndexedTypes().values();
		if ((indexedTypes != null) && (!(indexedTypes.isEmpty())))
		{
			final IndexedType indexedType = ((IndexedType) indexedTypes.iterator().next());
			indexedType.setPageTypeFacets(pageTypeFacets);
			return indexedType;
		}

		return null;
	}

}
