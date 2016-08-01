/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author 1210148
 *
 */
public class MplCustomPriceSolrQueryPostProcessor implements SolrQueryPostProcessor
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor#process(org.apache.solr.client.solrj.SolrQuery,
	 * de.hybris.platform.solrfacetsearch.search.SearchQuery)
	 */
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

	@Override
	public SolrQuery process(final SolrQuery paramSolrQuery, final SearchQuery paramSearchQuery)
	{
		String priceRange = "";
		String priceStartIndex = "";
		String priceEndIndex = "";
		//final IndexedProperty staticPriceProperty = null;

		final String priceFacetFormat = "%s:[%.2f TO %.2f]";
		final Map<String, IndexedProperty> props = paramSearchQuery.getIndexedType().getIndexedProperties();
		IndexedProperty priceProperty = null;

		if (CollectionUtils.isNotEmpty(paramSearchQuery.getBreadcrumbs()))
		{
			for (final Breadcrumb brc : paramSearchQuery.getBreadcrumbs())
			{

				if (brc.getFieldName().equalsIgnoreCase("price"))
				{
					priceRange = brc.getValue();
					final int length = priceRange.length();
					final int s = priceRange.indexOf("-");
					priceStartIndex = priceRange.substring(1, s - 1);
					priceEndIndex = priceRange.substring(s + 2, length);

					for (final String fq : paramSolrQuery.getFilterQueries())
					{
						if (fq.contains("price_inr_string:"))

						{

							paramSolrQuery.removeFilterQuery(fq);

							for (final Map.Entry<String, IndexedProperty> entry : props.entrySet())
							{
								if (entry.getKey().equals("priceValue"))
								{
									priceProperty = entry.getValue();
									// Get the start value
									final double start = Double.parseDouble(priceStartIndex.contains(",") ? priceStartIndex.replace(",",
											"") : priceStartIndex);
									// Get the end value
									final double end = Double.parseDouble(priceEndIndex.contains(",") ? priceEndIndex.replace(",", "")
											: priceEndIndex);

									paramSolrQuery.addFilterQuery(String.format(
											priceFacetFormat,
											getSolrFieldNameProvider().getFieldName(
													priceProperty,
													priceProperty.isLocalized() ? paramSearchQuery.getLanguage()
															: priceProperty.isCurrency() ? paramSearchQuery.getCurrency() : null,
													FieldType.INDEX), Double.valueOf(start), Double.valueOf(end)));
									break;

								}

							}

							break;

						}

					}

				}

			}
		}


		return paramSolrQuery;
	}
}
