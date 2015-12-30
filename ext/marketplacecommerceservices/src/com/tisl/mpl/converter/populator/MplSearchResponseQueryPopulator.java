/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.converter.populator;

import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.IndexedPropertyValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.ArrayList;
import java.util.List;


public class MplSearchResponseQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, SEARCH_QUERY_TYPE, SEARCH_RESULT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE>, FacetSearchPageData<SolrSearchQueryData, ITEM>>
{
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE> source,
			final FacetSearchPageData<SolrSearchQueryData, ITEM> target)
	{
		target.setCurrentQuery(buildSearchQueryData(source));
	}


	protected SolrSearchQueryData buildSearchQueryData(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE> source)
	{
		return buildSearchQueryData(source.getRequest().getSearchText(), source.getRequest().getSearchQueryData().getCategoryCode(),
				source.getRequest().getSearchQueryData().getSellerID(), source.getRequest().getSearchQueryData().getOfferID(),
				source.getRequest().getSearchQueryData().getOfferCategoryID(), source.getRequest().getCurrentSort(),
				source.getRequest().getIndexedPropertyValues());
	}

	protected SolrSearchQueryData buildSearchQueryData(final String searchText, final String categoryCode, final String sellerID,
			final String offerID, final String offerCategoryID, final IndexedTypeSort currentSort,
			final List<IndexedPropertyValueData<IndexedProperty>> indexedPropertyValues)
	{
		final SolrSearchQueryData result = createSearchQueryData();
		result.setFreeTextSearch(searchText);
		result.setCategoryCode(categoryCode);

		if (sellerID != null)
		{

			result.setSellerID(sellerID);
		}
		if (offerID != null)
		{

			result.setOfferID(offerID);
		}
		if (offerCategoryID != null)
		{

			result.setOfferCategoryID(offerCategoryID);
		}

		if (currentSort != null)
		{
			result.setSort(currentSort.getCode());
		}

		final List terms = new ArrayList();

		if ((indexedPropertyValues != null) && (!(indexedPropertyValues.isEmpty())))
		{
			for (final IndexedPropertyValueData indexedPropertyValue : indexedPropertyValues)
			{
				final SolrSearchQueryTermData term = createSearchQueryTermData();
				term.setKey(((IndexedProperty) indexedPropertyValue.getIndexedProperty()).getName());
				term.setValue(indexedPropertyValue.getValue());
				terms.add(term);
			}
		}

		result.setFilterTerms(terms);

		return result;
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}

	protected SolrSearchQueryTermData createSearchQueryTermData()
	{
		return new SolrSearchQueryTermData();
	}
}