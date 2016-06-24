package com.tisl.mpl.solr.search;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;


public class KeywordRedirectValue
{
	private final String keyword;
	private final KeywordRedirectMatchType matchType;
	private final SolrAbstractKeywordRedirectModel redirect;
	private final SolrAbstractKeywordRedirectModel redirectMobile;

	public KeywordRedirectValue(final String keyword, final KeywordRedirectMatchType matchType,
			final SolrAbstractKeywordRedirectModel redirect, final SolrAbstractKeywordRedirectModel redirectMobile)
	{
		ServicesUtil.validateParameterNotNull(keyword, "Keyword required");
		ServicesUtil.validateParameterNotNull(matchType, "Match type required");
		ServicesUtil.validateParameterNotNull(redirect, "Redirect required");
		ServicesUtil.validateParameterNotNull(redirectMobile, "Redirect Mobile required");
		this.keyword = keyword;
		this.matchType = matchType;
		this.redirect = redirect;
		this.redirectMobile = redirectMobile;
	}

	public String getKeyword()
	{
		return this.keyword;
	}

	public KeywordRedirectMatchType getMatchType()
	{
		return this.matchType;
	}

	public SolrAbstractKeywordRedirectModel getRedirect()
	{
		return this.redirect;
	}

	public SolrAbstractKeywordRedirectModel getRedirectMobile()
	{
		return this.redirectMobile;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + this.keyword.hashCode();
		result = 31 * result + this.matchType.hashCode();
		result = 31 * result + this.redirect.hashCode();
		result = 31 * result + this.redirectMobile.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (super.getClass() != obj.getClass())
		{
			return false;
		}
		final KeywordRedirectValue other = (KeywordRedirectValue) obj;
		if (!(this.keyword.equals(other.keyword)))
		{
			return false;
		}
		if (this.matchType != other.matchType)
		{
			return false;
		}
		if (this.redirectMobile != other.redirectMobile)
		{
			return false;
		}

		return (this.redirect.equals(other.redirect));
	}
}