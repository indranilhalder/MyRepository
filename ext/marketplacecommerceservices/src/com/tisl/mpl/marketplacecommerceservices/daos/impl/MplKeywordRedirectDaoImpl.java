/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.daos.MplKeywordRedirectDao;


/**
 * @author 1047001
 *
 */
public class MplKeywordRedirectDaoImpl implements MplKeywordRedirectDao
{

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<SolrFacetSearchKeywordRedirectModel> findKeywords(final String keyword, final KeywordRedirectMatchType matchType,
			final String facetSearchConfigName, final String langIso)
	{
		final StringBuilder query = new StringBuilder();
		query.append("SELECT {rd.").append("pk").append("}");
		query.append("  FROM {").append("SolrFacetSearchConfig").append(" as cfg ");
		query.append("  JOIN ").append("SolrFacetSearchKeywordRedirect").append(" as rd ");
		query.append("    ON {cfg.").append("pk").append("} = {rd.").append("facetSearchConfig").append("} ");
		query.append("  JOIN ").append("Language").append(" as lang ");
		query.append("    ON {lang.").append("pk").append("} = {rd.").append("language").append("}}");
		query.append(" WHERE {cfg.").append("name").append("} = ?name ");
		query.append("   AND {lang.").append("isocode").append("} = ?iso ");
		query.append("   AND LOWER({rd.").append("keyword").append("}) = ?keyword ");
		query.append("   AND {rd.").append("matchType").append("} = ?match ");

		final Map queryParams = new HashMap();
		queryParams.put("iso", langIso);
		queryParams.put("name", facetSearchConfigName);
		queryParams.put("match", matchType);
		queryParams.put("keyword", keyword.toLowerCase());

		final SearchResult result = flexibleSearchService.search(new FlexibleSearchQuery(query.toString(), queryParams));
		return result.getResult();
	}
}
