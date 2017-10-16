/**
 *
 */
package com.tisl.mpl.core.mplsearch;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServer;

import java.util.Set;


/**
 * @author TCS
 *
 */
public interface MplSolrServiceStandalone
{
	public Set<SolrServer> getRegistredServersForFacetSearchConfig(FacetSearchConfig facetSearchConfig)
			throws SolrServiceException;
}