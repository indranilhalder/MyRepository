/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseFacetsPopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.TopValuesProvider;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
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
			if (!"categoryNameCodeMapping".equalsIgnoreCase(facet.getName()))
			{
				buildFacetValuesNew(facetData, facet, indexedProperty, solrSearchResult, searchQueryData);

				if ((facetData.getValues() == null) || (facetData.getValues().isEmpty()))
				{
					continue;
				}
			}
			else if ("categoryNameCodeMapping".equalsIgnoreCase(facet.getName()))
			{
				List allFacetValues = null;

				if (null != facet.getFacetValues())
				{
					allFacetValues = new ArrayList(facet.getFacetValues().size());

					for (final FacetValue facetValue : facet.getFacetValues())
					{
						final FacetValueData facetValueData = createFacetValueData();
						facetValueData.setCode(facetValue.getName());
						facetValueData.setName(facetValue.getDisplayName());
						facetValueData.setCount(facetValue.getCount());

						allFacetValues.add(facetValueData);
					}
				}

				facetData.setValues(allFacetValues);
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

	protected void buildFacetValuesNew(final FacetData<SolrSearchQueryData> facetData, final Facet facet,
			final IndexedProperty indexedProperty, final SearchResult solrSearchResult, final SolrSearchQueryData searchQueryData)
	{
		final List<FacetValue> facetValues = facet.getFacetValues();
		if ((facetValues == null) || (facetValues.isEmpty()))
		{
			return;
		}
		final List<FacetValueData<SolrSearchQueryData>> allFacetValues = new ArrayList(facetValues.size());

		int count = 0;
		for (final FacetValue facetValue : facetValues)
		{
			final FacetValueData<SolrSearchQueryData> facetValueData = buildFacetValueNew(facetData, facet, facetValue,
					solrSearchResult, searchQueryData);
			if (facetValueData == null)
			{
				continue;
			}
			if (facetValueData.isSelected())
			{
				count++;
			}
			allFacetValues.add(facetValueData);

		}
		facetData.setSelectedFilterCount(count);
		facetData.setValues(allFacetValues);

		final TopValuesProvider topValuesProvider = getTopValuesProvider(indexedProperty);
		if ((isRanged(indexedProperty)) || (topValuesProvider == null))
		{
			return;
		}
		final List<FacetValue> topFacetValues = topValuesProvider.getTopValues(indexedProperty, facetValues);
		if (topFacetValues == null)
		{
			return;
		}
		final List<FacetValueData<SolrSearchQueryData>> topFacetValuesData = new ArrayList();

		for (final FacetValue facetValue : topFacetValues)
		{
			final FacetValueData<SolrSearchQueryData> topFacetValueData = buildFacetValueNew(facetData, facet, facetValue,
					solrSearchResult, searchQueryData);
			if (topFacetValueData == null)
			{
				continue;
			}
			topFacetValuesData.add(topFacetValueData);
		}

		facetData.setTopValues(topFacetValuesData);
	}

	protected FacetValueData<SolrSearchQueryData> buildFacetValueNew(final FacetData<SolrSearchQueryData> facetData,
			final Facet facet, final FacetValue facetValue, final SearchResult solrSearchResult,
			final SolrSearchQueryData searchQueryData)
	{
		if (facetData.isMultiSelect())
		{
			final FacetValueData facetValueData = createFacetValueData();
			facetValueData.setCode(facetValue.getName());
			facetValueData.setName(facetValue.getDisplayName());
			facetValueData.setCount(facetValue.getCount());


			facetValueData.setSelected(isFacetSelected(searchQueryData, facet.getName(), facetValue.getName()));

			if (facetValueData.isSelected())

			{
				facetValueData.setQuery(refineQueryRemoveFacet(searchQueryData, facet.getName(), facetValue.getName()));

			}
			else
			{
				facetValueData.setQuery(refineQueryAddFacet(searchQueryData, facet.getName(), facetValue.getName()));
			}

			return facetValueData;


		}

		if (facetValue.getCount() < solrSearchResult.getTotalNumberOfResults())
		{
			final FacetValueData facetValueData = createFacetValueData();
			facetValueData.setCode(facetValue.getName());
			facetValueData.setName(facetValue.getDisplayName());
			facetValueData.setCount(facetValue.getCount());

			if (facetValueData.isSelected())

			{
				facetValueData.setQuery(refineQueryRemoveFacet(searchQueryData, facet.getName(), facetValue.getName()));

			}
			else
			{
				facetValueData.setQuery(refineQueryAddFacet(searchQueryData, facet.getName(), facetValue.getName()));
			}

			return facetValueData;
		}
		return null;
	}
}