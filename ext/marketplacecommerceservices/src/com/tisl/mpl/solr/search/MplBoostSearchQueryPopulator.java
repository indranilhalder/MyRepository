/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.commercesearch.search.solrfacetsearch.populators.BoostSearchQueryPopulator;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.SolrBoostRuleModel;
import de.hybris.platform.commercesearch.searchandizing.boost.BoostService;
import de.hybris.platform.commercesearch.searchandizing.boost.operators.SolrQueryOperatorTranslator;
import de.hybris.platform.commercesearch.searchandizing.heroproduct.HeroProductFacade;
import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.QueryParser;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Required;


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
			SearchQueryPageableData<SolrSearchQueryData> source,
			SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{
		IndexedTypeSort indexedSort = (IndexedTypeSort) target.getCurrentSort();
		if (isBoostDisabled(indexedSort))
		{
			return;
		}

		SearchQuery searchQuery = (SearchQuery) target.getSearchQuery();
		searchQuery.setQueryParser(SearchQuery.QueryParser.EDISMAX);
		IndexedType indexedType = (IndexedType) target.getIndexedType();
		FacetSearchConfig facetConfig = (FacetSearchConfig) target.getFacetSearchConfig();

		if (StringUtils.isBlank(searchQuery.getUserQuery()))
		{
			searchQuery.addRawQuery("*:*", SearchQuery.Operator.OR);
		}
		String categoryCode = getSelectedCategory(source);
		//		if (StringUtils.isNotBlank(categoryCode)) {
		//			addHeroProductBoosts(searchQuery, categoryCode);
		//		}
		addConfiguredProductBoosts(searchQuery, indexedType, categoryCode, facetConfig);

		List orderFields = searchQuery.getOrderFields();
		OrderField firstOrderField = (OrderField) orderFields.get(0);

		if (("score".equals(firstOrderField.getField())) && (!(firstOrderField.isAscending())))
		{
			return;
		}
		orderFields.add(0, new OrderField("score", false));
	}
}
