/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.converter.populator;

import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;


public class MplSearchResponseSellerIDPopulator<STATE, ITEM, CATEGORY>
		implements Populator<SolrSearchResponse, ProductCategorySearchPageData<STATE, ITEM, CATEGORY>>
{
	public void populate(final SolrSearchResponse source, final ProductCategorySearchPageData<STATE, ITEM, CATEGORY> target)
	{
		target.setSellerID(source.getRequest().getSearchQueryData().getSellerID());
	}
}