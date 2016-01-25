/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseFacetsPopulator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 361234
 *
 */
public class MplSearchResponseFacetsPopulator extends SearchResponseFacetsPopulator

{


	@Override
	protected List<FacetData<SolrSearchQueryData>> buildFacets(final SearchResult solrSearchResult,
			final SolrSearchQueryData searchQueryData, final IndexedType indexedType)
	{
		final List<Facet> solrSearchResultFacets = solrSearchResult.getFacets();
		final List result = new ArrayList(solrSearchResultFacets.size());

		for (final Facet facet : solrSearchResultFacets)
		{
			final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get(facet.getName());

			final FacetData facetData = createFacetData();
			facetData.setCode(facet.getName());
			facetData.setCategory(indexedProperty.isCategoryField());
			final String displayName = indexedProperty.getDisplayName();
			facetData.setName((displayName == null) ? facet.getName() : displayName);
			facetData.setMultiSelect(indexedProperty.isMultiSelect());
			facetData.setPriority(indexedProperty.getPriority());
			facetData.setVisible(indexedProperty.isVisible());

			facetData.setGenericFilter(indexedProperty.isGenericFacet());
			buildFacetValues(facetData, facet, indexedProperty, solrSearchResult, searchQueryData);

			if ((facetData.getValues() == null) || (facetData.getValues().isEmpty()))
			{
				continue;
			}
			result.add(facetData);
		}

		return result;
	}

	@Override
	protected SolrSearchQueryData cloneSearchQueryData(final SolrSearchQueryData source)
	{
		final SolrSearchQueryData target = createSearchQueryData();
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		target.setSort(source.getSort());
		target.setFilterTerms(source.getFilterTerms());
		if (source.getSellerID() != null)
		{

			target.setSellerID(source.getSellerID());
		}
		if (source.getOfferID() != null)
		{

			target.setOfferID(source.getOfferID());
		}

		if (source.getOfferCategoryID() != null)
		{

			target.setOfferCategoryID(source.getOfferCategoryID());
		}


		return target;
	}
}
