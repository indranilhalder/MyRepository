/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.QueryParser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class MplSolrTextPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private FieldNameProvider solrFieldNameProvider;

	/**
	 * @return the solrFieldNameProvider
	 */
	public FieldNameProvider getSolrFieldNameProvider()
	{
		return solrFieldNameProvider;
	}

	/**
	 * @param solrFieldNameProvider
	 *           the solrFieldNameProvider to set
	 */
	@Required
	public void setSolrFieldNameProvider(final FieldNameProvider solrFieldNameProvider)
	{
		this.solrFieldNameProvider = solrFieldNameProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{

		final String searchText = target.getSearchQueryData().getFreeTextSearch();
		final String cleanSearchText = cleanup(searchText);
		if (StringUtils.isEmpty(cleanSearchText))
		{
			return;
		}
		//PR-23 start
		boolean TwoTokenNextSearch = false;
		if (null != source.getSearchQueryData() && source.getSearchQueryData().isNextSearch())
		{
			TwoTokenNextSearch = source.getSearchQueryData().isNextSearch();
		}
		//PR-23 end
		setUpQuery(target.getSearchQuery(), cleanSearchText, TwoTokenNextSearch);

		target.setSearchText(searchText);

	}


	/**
	 * @param searchText
	 * @return String
	 */
	private String cleanup(final String searchText)
	{

		String cleanedText = "";

		if (searchText == null)
		{
			cleanedText = "";
		}
		else
		{
			final String[] words = searchText.split("\\s+");
			final StringBuilder sb = new StringBuilder();
			for (final String w : words)
			{

				if ("and".equalsIgnoreCase(w) || "or".equalsIgnoreCase(w) || "not".equalsIgnoreCase(w) || "for".equalsIgnoreCase(w))
				{
					continue;
				}
				sb.append(ClientUtils.escapeQueryChars(w.replaceAll(":", "")));
				sb.append(' ');
				cleanedText = sb.toString().trim();
			}

		}
		return cleanedText;

	}

	/**
	 * @param searchQuery
	 * @param cleanSearchText
	 */


	private void setUpQuery(final SearchQuery searchQuery, final String cleanSearchText, final boolean TwoTokenNextSearch)
	{
		final Map<String, IndexedProperty> originalProps = searchQuery.getIndexedType().getIndexedProperties();
		final Map<String, IndexedProperty> props = new HashMap<>();
		int maxBoostValue = 0;
		for (final Map.Entry<String, IndexedProperty> entry : originalProps.entrySet())
		{
			if (entry.getValue().getBoostDouble() != null)
			{
				props.put(entry.getKey(), entry.getValue());
				maxBoostValue = maxBoostValue > entry.getValue().getBoostDouble().intValue() ? maxBoostValue : entry.getValue()
						.getBoostDouble().intValue();
			}
		}

		// Lets get the query filter
		final String qf = formFilter(props, 0, searchQuery.getLanguage(), searchQuery.getCurrency());

		// Lets get the phrase filter
		final String pf = formFilter(props, maxBoostValue, searchQuery.getLanguage(), searchQuery.getCurrency());

		// Setup query parser information.

		// e-dismax parser
		searchQuery.setQueryParser(QueryParser.EDISMAX);
		// actual query text
		searchQuery.getAllFields().clear();



		searchQuery.addSolrParams("q", cleanSearchText);
		// list of filtered fields for the query
		searchQuery.addSolrParams("qf", qf);
		// list of filtered fields for phrase query
		searchQuery.addSolrParams("pf", pf);
		// phrase slop for getting better relevance
		searchQuery.addSolrParams("ps", "5");
		// minimum match changes for TPR-6335
		//PR-23 start
		if (TwoTokenNextSearch)
		{
			searchQuery.addSolrParams("mm", "50%");
		}
		else
		{
			searchQuery.addSolrParams("mm", "2<75% 3<50%");
		}
		//PR-23 end
		// Tie param added for better search score
		searchQuery.addSolrParams("tie", "0.1");

	}



	/**
	 * @param props
	 * @return String
	 */

	@Required
	private String formFilter(final Map<String, IndexedProperty> props, final int maxBoostValue, final String lang,
			final String currency)
	{

		final StringBuilder sp = new StringBuilder();
		for (final Map.Entry<String, IndexedProperty> entry : props.entrySet())
		{
			final IndexedProperty value = props.get(entry.getKey());
			sp.append(String.format(
					"%s^%.2f ",
					getSolrFieldNameProvider().getFieldName(value, value.isLocalized() ? lang : value.isCurrency() ? currency : null,
							FieldType.INDEX), Double.valueOf(value.getBoostDouble().intValue() + maxBoostValue)));


		}
		return sp.toString().trim();

	}

}