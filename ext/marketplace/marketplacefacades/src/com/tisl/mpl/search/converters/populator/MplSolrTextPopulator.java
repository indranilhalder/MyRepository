/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;


/**
 * @author 361234
 *
 */
public class MplSolrTextPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{




	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final String cleanedFreeTextSearch = cleanupSolrSearchText(target.getSearchQueryData().getFreeTextSearch());
		target.setSearchText(cleanedFreeTextSearch);
		if (cleanedFreeTextSearch.isEmpty())
		{
			return;
		}
		addFreeTextQuery(target.getSearchQuery(), cleanedFreeTextSearch);
	}

	protected String cleanupSolrSearchText(final String text)
	{
		String cleanedText = text;
		if (text == null)
		{
			cleanedText = "";
		}
		else
		{
			cleanedText = cleanedText.replace(':', ' ').trim();

			cleanedText = cleanedText.replaceAll("AND", "and");
			cleanedText = cleanedText.replaceAll("OR", "or");
		}

		return cleanedText;
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final String cleanedFreeTextSearch)
	{
		final String[] words = cleanedFreeTextSearch.split("\\s+");

		final Map<String, IndexedProperty> indexedProperty = searchQuery.getIndexedType().getIndexedProperties();
		//	final List<IndexedProperty> listIndexedProperty = new ArrayList<IndexedProperty>();

		for (final Map.Entry<String, IndexedProperty> entry : indexedProperty.entrySet())
		{

			if (entry.getValue().getBoost() != null)
			{
				addFreeTextQuery(searchQuery, cleanedFreeTextSearch, words, entry.getValue().getBoost().intValue(), entry.getValue());
			}
		}







	}

	public void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords,
			final int boostValue, final IndexedProperty indexedProperty)
	{


		addFreeTextQuery(searchQuery, indexedProperty, fullText, textWords, indexedProperty.getBoost().intValue());

	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String fullText,
			final String[] textWords, final int boost)
	{
		addFreeTextQuery(searchQuery, indexedProperty, fullText, boost * 2.0D);

		if ((textWords == null) || (textWords.length <= 1))
		{
			return;
		}
		for (final String word : textWords)
		{
			addFreeTextQuery(searchQuery, indexedProperty, word, boost);
		}
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String value,
			final double boost)
	{
		final String field = indexedProperty.getName();
		if (!(indexedProperty.isFacet()))
		{


			addFreeTextQuery(searchQuery, field, value.toLowerCase(), "", boost);
			addFreeTextQuery(searchQuery, field, value.toLowerCase(), "*", boost / 4.0D);
			//Removed fuzzy logic from search query
			//addFreeTextQuery(searchQuery, field, value.toLowerCase(), "~", boost / 4.0D);


		}
		//SONAR Fix
		//else
		//{
		//LOG.debug("Not searching " + indexedProperty
		//	+ ". Free text search not available in facet property. Configure an additional text property for searching.");
		//}
	}

	protected boolean isProduct(final String productCode)
	{
		boolean isProduct = false;
		if (productCode != null && productCode.startsWith("mp"))
		{

			final String remainingChars = productCode.substring(2);
			if (StringUtils.isNumeric(remainingChars))
			{
				isProduct = true;

			}
		}

		return isProduct;
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final String field, final String value, final String suffixOp,
			final double boost)
	{
		if (!isProduct(value))
		{
			searchQuery.searchInField(field, ClientUtils.escapeQueryChars(value) + suffixOp + "^" + boost, SearchQuery.Operator.OR);

		}
		else
		{
			searchQuery.searchInField("code_string", value.toUpperCase());

		}
	}
}
