/**
 *
 */
package com.tisl.mpl.converter.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseBreadcrumbsPopulator;


/**
 * @author 361234
 *
 */
public class MplSearchResponseBreadcrumbsPopulator extends SearchResponseBreadcrumbsPopulator
{

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
