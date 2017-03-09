/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductKeywordsValueProvider;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * @author TCS this value provider has been written to support multiple keywords values against a particular
 *         product(TISUATNOV-110)
 */
public class MplKeyWordValueProvider extends ProductKeywordsValueProvider
{


	/**
	 * get keyword values against product model
	 */
	@Override
	protected Object getPropertyValue(final Object model, final IndexedProperty indexedProperty)
	{

		final StringBuilder buf = new StringBuilder();
		if (model instanceof ProductModel)
		{
			Set keywords = new HashSet();
			keywords = collectProductKeywordValues(keywords, (ProductModel) model);
			if (!(keywords.isEmpty()))
			{

				final Iterator localIterator = keywords.iterator();
				while (localIterator.hasNext())
				{
					final String keyword = (String) localIterator.next();
					buf.append(keyword).append(' ');
				}
			}
		}
		return buf.toString();
	}

	/**
	 * collecting the keyword values against a products
	 *
	 * @param words
	 * @param product
	 * @return words
	 */
	protected Set<String> collectProductKeywordValues(Set<String> words, final ProductModel product)

	{
		final List<KeywordModel> keywords = product.getKeywords();
		if ((keywords != null) && (!(keywords.isEmpty())))

		{
			words = getListedKeyWords(keywords);

		}
		return words;

	}

	/**
	 * get all the keywords against particular products
	 *
	 * @param keywords
	 * @return words
	 */
	private Set<String> getListedKeyWords(final List<KeywordModel> keywords)
	{
		final Set<String> words = new HashSet<String>();
		for (final KeywordModel keyword : keywords)
		{
			final String[] values = keyword.getKeyword().split(",");
			for (final String key : values)
			{
				words.add(key);
			}
		}
		return words;
	}
}