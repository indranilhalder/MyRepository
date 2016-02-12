/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 *
 */
public class MplSearchQuery extends SearchQuery
{

	private boolean snsFlag;


	/**
	 * @return the snsFlag
	 */
	public boolean isSnsFlag()
	{
		return snsFlag;
	}


	/**
	 * @param snsFlag
	 *           the snsFlag to set
	 */
	public void setSnsFlag(final boolean snsFlag)
	{
		this.snsFlag = snsFlag;
	}


	/**
	 * @param facetSearchConfig
	 * @param indexedType
	 */
	public MplSearchQuery(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType)
	{
		super(facetSearchConfig, indexedType);
	}


	/**
	 * @param facetSearchConfig
	 * @param indexedType
	 */
	public MplSearchQuery(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType, final boolean isSnS)
	{
		super(facetSearchConfig, indexedType);

		this.setSnS(isSnS);
	}


	/**
	 * @return the isSnS
	 */
	public boolean isSnS()
	{
		return snsFlag;
	}


	/**
	 * @param isSnS
	 *           the isSnS to set
	 */
	public void setSnS(final boolean isSnS)
	{
		this.snsFlag = isSnS;
	}
}
