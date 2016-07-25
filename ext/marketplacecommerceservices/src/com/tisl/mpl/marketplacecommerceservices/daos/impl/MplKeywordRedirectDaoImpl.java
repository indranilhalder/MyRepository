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
	private final String SELECT_RD = "SELECT {rd.";
	private final String PK = "pk";
	private final String END = "}";
	private final String END_D = "}}";
	private final String SFC = "SolrFacetSearchConfig";
	private final String SFKR = "SolrFacetSearchKeywordRedirect";
	private final String CFG = " as cfg ";
	private final String FROM = "  FROM {";
	private final String JOIN = "  JOIN ";
	private final String RD_AS = " as rd ";
	private final String CFG_ON = "    ON {cfg.";
	private final String RD_ON = "} = {rd.";
	private final String LANG_ON = "    ON {lang.";
	private final String LANG = "Language";
	private final String LANG_S = "language";
	private final String LANG_AS = " as lang ";
	private final String CFG_AS = " WHERE {cfg.";
	private final String NAME = "name";
	private final String NAME_START = "} = ?name ";
	private final String FSC = "facetSearchConfig";
	private final String ISO_END = "} = ?iso ";
	private final String ISO = "isocode";
	private final String LANG_AND = "   AND {lang.";
	private final String LOWER = "   AND LOWER({rd.";
	private final String KEYWORD = "keyword";
	private final String KEYWORD_END = "}) = ?keyword ";
	private final String RD_AND = "   AND {rd.";
	private final String MATCH = "matchType";
	private final String MATCH_END = "} = ?match ";

	@Override
	public List<SolrFacetSearchKeywordRedirectModel> findKeywords(final String keyword, final KeywordRedirectMatchType matchType,
			final String facetSearchConfigName, final String langIso)
	{
		final StringBuilder query = new StringBuilder(450);
		query.append(SELECT_RD).append(PK).append(END);
		query.append(FROM).append(SFC).append(CFG);
		query.append(JOIN).append(SFKR).append(RD_AS);
		query.append(CFG_ON).append(PK).append(RD_ON).append(FSC).append(END);
		query.append(JOIN).append(LANG).append(LANG_AS);
		query.append(LANG_ON).append(PK).append(RD_ON).append(LANG_S).append(END_D);
		query.append(CFG_AS).append(NAME).append(NAME_START);
		query.append(LANG_AND).append(ISO).append(ISO_END);
		query.append(LOWER).append(KEYWORD).append(KEYWORD_END);
		query.append(RD_AND).append(MATCH).append(MATCH_END);

		final Map queryParams = new HashMap();
		queryParams.put("iso", langIso);
		queryParams.put("name", facetSearchConfigName);
		queryParams.put("match", matchType);
		queryParams.put("keyword", keyword.toLowerCase());

		final SearchResult result = flexibleSearchService.search(new FlexibleSearchQuery(query.toString(), queryParams));
		return result.getResult();
	}
}
