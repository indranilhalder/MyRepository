/**
 *
 */
package com.tisl.mpl.solrfacet.search.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.impl.DefaultSolrProductSearchService;

import java.util.Collections;

import com.tisl.mpl.solrfacet.search.service.MplProductSearchService;


/**
 * The Class DefaultSolrDeviceSearchService.
 *
 * @param <ITEM>
 *           the generic type
 * @author TCS
 */
public class DefaultMplProductSearchService<ITEM> extends DefaultSolrProductSearchService implements
		MplProductSearchService<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>>

{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.solrfacet.search.service.impl.DefaultMplProductSearchService#mplProductSearch(java.lang.Object,
	 * de.hybris.platform.commerceservices.search.pagedata.PageableData)
	 */
	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> mplProductSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}

	//To select brand facet by default on the Brand landing page
	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		//	final List<SolrSearchQueryTermData> solrSearchQueryTermData = new ArrayList<SolrSearchQueryTermData>();
		final SolrSearchQueryTermData solrSearchQueryTermDataCategory = new SolrSearchQueryTermData();
		if (categoryCode.startsWith("MBH") || categoryCode.startsWith("mbh"))
		{
			solrSearchQueryTermDataCategory.setKey("brand");
			solrSearchQueryTermDataCategory.setValue(categoryCode);
			searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermDataCategory));

		}
		searchQueryData.setCategoryCode(categoryCode);

		return doSearch(searchQueryData, pageableData);
	}
}
