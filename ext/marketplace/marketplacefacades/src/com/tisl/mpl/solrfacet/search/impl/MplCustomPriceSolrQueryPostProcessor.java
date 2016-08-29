/**
 *
 */
/**
 *
 */
package com.tisl.mpl.solrfacet.search.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author 1210148
 *
 */
public class MplCustomPriceSolrQueryPostProcessor implements SolrQueryPostProcessor
{
	/**
	 *
	 */
	private static final String PRICE_VALUE = "priceValue";
	/**
	 *
	 */
	private static final String PRICE = "price";
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
	 * @see
	 * de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor#process(org.apache.solr.client.solrj.SolrQuery,
	 * de.hybris.platform.solrfacetsearch.search.SearchQuery)
	 */
	@Override
	public SolrQuery process(final SolrQuery paramSolrQuery, final SearchQuery paramSearchQuery)
	{
		if (CollectionUtils.isEmpty(paramSearchQuery.getBreadcrumbs()))
		{
			// No breadcrumbs.
			return paramSolrQuery;
		}
		//checking breadcrumb value
		Breadcrumb priceBreadCrumb = null;
		for (final Breadcrumb bc : paramSearchQuery.getBreadcrumbs())
		{
			//if (bc.getFieldName().equalsIgnoreCase("priceValue"))
			if (bc.getFieldName().equalsIgnoreCase(PRICE))
			{
				priceBreadCrumb = bc;
				break;
			}
		}

		if (priceBreadCrumb == null)
		{
			// No price breadcrumb.
			return paramSolrQuery;
		}

		final String priceFacetFormat = "%s:[%.2f TO %.2f]";

		// Remove existing price filter query
		final List<String> fqList = Arrays.asList(paramSolrQuery.getFilterQueries());
		for (final String fq : fqList)
		{
			if (fq.contains(PRICE))
			{
				paramSolrQuery.removeFilterQuery(fq);
			}
		}

		// Add the custom price filter
		//final IndexedProperty priceProperty = paramSearchQuery.getIndexedType().getIndexedProperties().get("priceValue");
		final IndexedProperty priceProperty = paramSearchQuery.getIndexedType().getIndexedProperties().get(PRICE_VALUE);
		if (priceProperty == null)
		{
			// something wrong. LOG ERROR HERE.
			return paramSolrQuery;
		}

		final String[] priceRanges = priceBreadCrumb.getValue().split("-");

		final Double start = Double.valueOf(priceRanges[0].replace("₹", "").replace("\\", "").replace(",", ""));
		final Double end = Double.valueOf(priceRanges[1].replace("₹", "").replace(",", ""));
		paramSolrQuery.addFilterQuery(String.format(
				priceFacetFormat,
				getSolrFieldNameProvider().getFieldName(
						priceProperty,
						priceProperty.isLocalized() ? paramSearchQuery.getLanguage() : priceProperty.isCurrency() ? paramSearchQuery
								.getCurrency() : null, FieldType.INDEX), start, end));

		return paramSolrQuery;
	}
}
