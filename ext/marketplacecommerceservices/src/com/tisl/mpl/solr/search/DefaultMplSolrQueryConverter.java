/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.CoupledQueryField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultSolrQueryConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;


/**
 *
 */
public class DefaultMplSolrQueryConverter extends DefaultSolrQueryConverter
{
	private static final Logger LOG = Logger.getLogger(DefaultMplSolrQueryConverter.class);
	private SearchQuery searchQuery;


	@Override
	public SolrQuery convertSolrQuery(final SearchQuery searchQuery) throws FacetSearchException
	{
		this.searchQuery = searchQuery;
		if (searchQuery instanceof MplSearchQuery && ((MplSearchQuery) searchQuery).isSnS())
		{
			LOG.debug("Entering MplSearchQuery..");
			checkQuery(searchQuery);
			final List queries = new ArrayList();
			final List boosts = new ArrayList();
			final List filterQueries = new ArrayList();

			final Map facetInfoMap = getFacetInfo(searchQuery, getSolrFieldNameProvider(), true);

			final List catalogVersionFilters = includeCatalogVersionFields(searchQuery);

			splitQueryFields(prepareQueryFields(searchQuery, getSolrFieldNameProvider()), queries, filterQueries, facetInfoMap);

			final List<QueryField> boostFields = prepareBoostFields(searchQuery, getSolrFieldNameProvider());
			for (final QueryField boostField : boostFields)
			{
				if (SearchQuery.QueryParser.EDISMAX == searchQuery.getQueryParser())
				{
					boosts.add(boostField);
				}
				else
				{
					queries.add(boostField);
				}

			}

			final String[] convertedQueryFields = convertQueryFields(queries, null);
			final String[] convertedCoupledQueryFields = convertCoupledQueryFields(searchQuery.getCoupledFields());

			final String[] combinedQueryFields = (String[]) ArrayUtils.addAll(convertedQueryFields, convertedCoupledQueryFields);

			final String query = buildQuery(combinedQueryFields, searchQuery);

			final SolrQuery solrQuery = new SolrQuery(query);

			if (searchQuery.isEnableSpellcheck())
			{
				solrQuery.add("spellcheck", new String[]
				{ "true" });
				solrQuery.add("spellcheck.dictionary", new String[]
				{ searchQuery.getLanguage() });
				solrQuery.add("spellcheck.collate", new String[]
				{ Boolean.TRUE.toString() });
				solrQuery.add("spellcheck.q", new String[]
				{ searchQuery.getUserQuery() });
			}

			if (SearchQuery.QueryParser.EDISMAX == searchQuery.getQueryParser())
			{
				final String[] convertedBoostFields = convertQueryFields(boosts, null);
				solrQuery.add("defType", new String[]
				{ SearchQuery.QueryParser.EDISMAX.getName() });
				solrQuery.add("bq", new String[]
				{ StringUtils.join(convertedBoostFields, SearchQuery.Operator.OR.getName()) });
			}

			final String[] convertedQueryFilters = convertQueryFields(filterQueries, facetInfoMap);
			final String[] convertedCatalogVersionFilters = convertCoupledQueryFields(catalogVersionFilters);
			final String[] combinedFilterFields = (String[]) ArrayUtils
					.addAll(convertedQueryFilters, convertedCatalogVersionFilters);

			solrQuery.setFilterQueries(combinedFilterFields);

			//final int start = searchQuery.getOffset() * searchQuery.getPageSize();
			solrQuery.setStart(Integer.valueOf(0));
			solrQuery.setRows(Integer.valueOf(2));
			solrQuery.setFacet(true);
			addFacetFields(solrQuery, facetInfoMap);
			for (final OrderField of : searchQuery.getOrderFields())
			{
				if ("score".equals(of.getField()))
				{
					solrQuery.addSortField(of.getField(), (of.isAscending()) ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
				}
				else
				{
					solrQuery.addSortField(translateFieldName(of.getField(), FieldNameProvider.FieldType.SORT, searchQuery),
							(of.isAscending()) ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
				}
			}
			solrQuery.setFacetMinCount(1);
			solrQuery.setFacetLimit(3);

			//Architectural Review Finding Point 16
			solrQuery.addField("name_text_en");
			solrQuery.addField("url_en_string");
			solrQuery.addField("img-252Wx374H_string");
			solrQuery.addField("priceValue_inr_double");
			//solrQuery.addField("code_string");
			//solrQuery.addField("code_string");
			solrQuery.addField("isLuxuryProduct_boolean");
			solrQuery.addField("newTitle_string_mv");
			//added for luxury Image
			solrQuery.addField("img-320Wx357H_string");
			/*
			 * if (getFacetSort() == null) { this.facetSort = DEFAULT_FACET_SORT; }
			 */

			//solrQuery.setFacetSort(FacetSort.COUNT.toString());
			//super.setFacetSort(FacetSort.COUNT);

			if (searchQuery.getSolrParams().size() > 0)
			{
				addSolrParams(solrQuery, searchQuery);
			}

			return applyPostProcessorsInOrder(solrQuery, searchQuery);
		}
		else
		{
			LOG.debug("Entering SearchQuery..");
			return super.convertSolrQuery(searchQuery);
		}
	}

	@Override
	public SolrQuery applyPostProcessorsInOrder(final SolrQuery solrQuery, final SearchQuery solrSearchQuery)

	{
		if (solrQuery == null)

		{
			throw new IllegalArgumentException("SolrQuery may not be null!");
		}
		if (searchQuery.isEnableSpellcheck())
		{
			final String defaultDict = "wordbreak";
			solrQuery.add("spellcheck.dictionary", new String[]
			{ defaultDict });
			solrQuery.add("spellcheck.accuracy", "0.65");

		}
		return super.applyPostProcessorsInOrder(solrQuery, solrSearchQuery);
	}

	@Override
	protected void addFacetFields(final SolrQuery solrQuery, Map<String, IndexedFacetInfo> facetInfoMap)
			throws FacetSearchException
	{
		//get page related facets and update facetInfoMap
		if (searchQuery.getIndexedType().getPageTypeFacets() != null)
		{
			facetInfoMap = getPageFacetInfo(this.searchQuery, getSolrFieldNameProvider());
		}

		for (final IndexedFacetInfo facetInfo : facetInfoMap.values())
		{
			if (facetInfo.isMultiSelect())
			{
				solrQuery.addFacetField(new String[]
				{ "{!ex=" + facetInfo.getKey() + "}" + facetInfo.getTranslatedFieldName() });
			}
			else
			{
				solrQuery.addFacetField(new String[]
				{ facetInfo.getTranslatedFieldName() });
			}
		}
	}

	protected Map<String, IndexedFacetInfo> getPageFacetInfo(final SearchQuery searchQuery,
			final FieldNameProvider solrFieldNameProvider) throws FacetSearchException
	{
		final Map results = new HashMap();

		int index = 0;

		final IndexedType indexedType = searchQuery.getIndexedType();
		final Set<String> typeFacets = indexedType.getTypeFacets();
		final Set<String> pageTypeFacets = indexedType.getPageTypeFacets();
		for (final String facetName : typeFacets)
		{
			final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get(facetName);
			if (indexedProperty != null && pageTypeFacets.contains(facetName))
			{
				final IndexedFacetInfo facetInfo = new IndexedFacetInfo();
				final FacetType facetType = indexedProperty.getFacetType();
				if (FacetType.MULTISELECTAND.equals(facetType))
				{
					facetInfo.setMultiSelect(true);
					facetInfo.setMultiSelectAnd(true);
				}
				else if (FacetType.MULTISELECTOR.equals(facetType))
				{
					facetInfo.setMultiSelect(true);
					facetInfo.setMultiSelectOr(true);
				}

				facetInfo.setTranslatedFieldName(translateFieldName(facetName, FieldNameProvider.FieldType.INDEX, searchQuery));
				facetInfo.setKey("fk" + index);
				results.put(facetInfo.getTranslatedFieldName(), facetInfo);
			}

			++index;
		}

		return results;
	}


	private String[] convertCoupledQueryFields(final List<CoupledQueryField> coupledQueryFields)
	{
		final List joinedQueries = new ArrayList();

		final Map<String, List> couples = new HashMap<String, List>();
		final Map operatorMapping = new HashMap(coupledQueryFields.size());

		for (final CoupledQueryField qf : coupledQueryFields)
		{
			final StringBuilder couple = new StringBuilder();
			couple.append('(').append(prepareQueryField(qf.getField1())).append(qf.getInnerCouplingOperator().getName())
					.append(prepareQueryField(qf.getField2())).append(')');

			List joinedCouples = couples.get(qf.getCoupleId());
			if (joinedCouples == null)
			{
				joinedCouples = new ArrayList();
			}
			joinedCouples.add(couple.toString());
			couples.put(qf.getCoupleId(), joinedCouples);

			operatorMapping.put(qf.getCoupleId(), qf.getOuterCouplingOperator());
		}
		for (final Map.Entry<String, List> entry : couples.entrySet())
		{
			final List list = couples.get(entry.getKey());
			joinedQueries.add("("
					+ combine((String[]) list.toArray(new String[list.size()]),
							((SearchQuery.Operator) operatorMapping.get(entry.getKey())).getName()) + ")");
		}

		return ((String[]) joinedQueries.toArray(new String[joinedQueries.size()]));
	}

	private String prepareQueryField(final QueryField field)
	{
		final StringBuilder result = new StringBuilder("(" + escape(field.getField()) + ":");
		if (field.getValues().size() == 1)
		{
			result.append(field.getValues().iterator().next()).append(')');
		}
		else
		{
			result.append('(')
					.append(combine(field.getValues().toArray(new String[field.getValues().size()]), field.getOperator().getName()))
					.append(')').append(')');
		}
		return result.toString();
	}

	protected Map<String, IndexedFacetInfo> getFacetInfo(final SearchQuery searchQuery,
			final FieldNameProvider solrFieldNameProvider, final boolean isSns) throws FacetSearchException
	{
		if (isSns)
		{
			final Map results = new HashMap();

			final IndexedType indexedType = searchQuery.getIndexedType();
			//Architectural Review Finding Point 17
			populatedFacetDetails(searchQuery, results, 0, indexedType, "snsCategory");
			populatedFacetDetails(searchQuery, results, 1, indexedType, "brand");
			populatedFacetDetails(searchQuery, results, 2, indexedType, "micrositeSnsCategory");
			populatedFacetDetails(searchQuery, results, 3, indexedType, "allCategories");
			populatedFacetDetails(searchQuery, results, 4, indexedType, "sellerId");
			populatedFacetDetails(searchQuery, results, 5, indexedType, "isLuxuryProduct");
			return results;
		}
		else
		{
			return getFacetInfo(searchQuery, solrFieldNameProvider);
		}
	}


	/**
	 * @param searchQuery
	 * @param results
	 * @param index
	 * @param indexedType
	 * @param facetName
	 * @throws FacetSearchException
	 */
	private void populatedFacetDetails(final SearchQuery searchQuery, final Map results, final int index,
			final IndexedType indexedType, final String facetName) throws FacetSearchException
	{
		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get(facetName);
		if (indexedProperty != null)
		{
			final IndexedFacetInfo facetInfo = new IndexedFacetInfo();
			final FacetType facetType = indexedProperty.getFacetType();
			if (FacetType.MULTISELECTAND.equals(facetType))
			{
				facetInfo.setMultiSelect(true);
				facetInfo.setMultiSelectAnd(true);
			}
			else if (FacetType.MULTISELECTOR.equals(facetType))
			{
				facetInfo.setMultiSelect(true);
				facetInfo.setMultiSelectOr(true);
			}

			facetInfo.setTranslatedFieldName(translateFieldName(facetName, FieldNameProvider.FieldType.INDEX, searchQuery));
			facetInfo.setKey("fk" + index);
			results.put(facetInfo.getTranslatedFieldName(), facetInfo);
		}
	}

}