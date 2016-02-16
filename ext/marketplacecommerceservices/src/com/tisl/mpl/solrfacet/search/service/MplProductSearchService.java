/**
 *
 */
package com.tisl.mpl.solrfacet.search.service;

import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;


/**
 * The Interface DeviceSearchService.
 *
 * @param <STATE>
 *           the generic type
 * @param <ITEM>
 *           the generic type
 * @param <RESULT>
 *           the generic type
 * @author rohit
 */
/**
 * @author TCS
 *
 */
public interface MplProductSearchService<STATE, ITEM, RESULT extends ProductSearchPageData<STATE, ITEM>> extends
		ProductSearchService
{
	/**
	 * Mpl Product search.
	 *
	 * @param searchQueryData
	 *           the search query data
	 * @param pageableData
	 *           the pageable data
	 * @return the result
	 */
	RESULT mplProductSearch(STATE searchQueryData, PageableData pageableData);
}
