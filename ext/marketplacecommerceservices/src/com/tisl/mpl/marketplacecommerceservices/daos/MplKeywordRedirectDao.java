/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplKeywordRedirectDao
{
	public List<SolrFacetSearchKeywordRedirectModel> findKeywords(final String keyword, final KeywordRedirectMatchType matchType,
			final String facetSearchConfigName, final String langIso);
}
