/**
 *
 */
package com.tisl.mpl.core.mplsearch.impl;

//import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.SolrCoreNameResolver;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServer;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServiceStandaloneImpl;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.tisl.mpl.core.mplsearch.MplSolrServiceStandalone;
import com.tisl.mpl.facades.search.data.EndpointSlaveURL;



/**
 * @author TCS
 *
 */
public class MplSolrServiceStandaloneImpl extends SolrServiceStandaloneImpl implements MplSolrServiceStandalone
{
	private static final Logger LOG = Logger.getLogger(MplSolrServiceStandaloneImpl.class);
	@Resource
	private SolrCoreNameResolver solrCoreNameResolver;
	private final String multicoreInstanceDir = Config.getString("solr.standalone.multicore.instancedir", ".");

	// @Resource(name = "configurationService")
	// private ConfigurationService configurationService;

	@Override
	public Set<SolrServer> getRegistredServersForFacetSearchConfig(final FacetSearchConfig facetSearchConfig)
			throws SolrServiceException
	{
		ServicesUtil.validateParameterNotNull(facetSearchConfig, "Parameter 'facetSearchConfig' can not be null");
		LOG.debug("inside getRegistredServersForFacetSearchConfig of MplSolrServiceStandaloneImpl");
		final Set result = new HashSet();

		for (final IndexedType indexedType : facetSearchConfig.getIndexConfig().getIndexedTypes().values())
		{

			LOG.debug("inside for loop of MplSolrServiceStandaloneImpl");
			final String contextValue = getCoreName(this.solrCoreNameResolver.getSolrCoreName(indexedType));
			for (final EndpointURL endpointURL : facetSearchConfig.getSolrConfig().getClusterConfig().getEndpointURLs())
			{
				LOG.debug("end point url ::::::::" + endpointURL.getUrl());
				final boolean remoteCoreExist = checkRemoteCore(endpointURL.getUrl(), contextValue);
				if (!(remoteCoreExist))
				{
					registerRemoteCore(endpointURL.getUrl(), contextValue, this.multicoreInstanceDir);
				}

				final String url = addContextValue(endpointURL.getUrl(), contextValue);
				final HttpSolrServer commonsHttpSolrServer = new HttpSolrServer(url);
				if (isStandaloneInstanceAlive(commonsHttpSolrServer))
				{
					final SolrServer solrServer = new SolrServer(commonsHttpSolrServer, endpointURL.isMaster());
					result.add(solrServer);
				}
				else
				{
					throw new SolrServiceException("Standalone solr core [" + contextValue + "] [" + endpointURL.getUrl()
							+ "] is not alive");
				}
			}




			if (CollectionUtils.isNotEmpty(facetSearchConfig.getSolrConfig().getClusterConfig().getEndpointSlaveURLs()))
			{
				for (final EndpointSlaveURL endpointSlaveURLs : facetSearchConfig.getSolrConfig().getClusterConfig()
						.getEndpointSlaveURLs())
				{
					if (StringUtils.isNotEmpty(endpointSlaveURLs.getUrl()) && endpointSlaveURLs.isSlave())
					{
						final String slaveurl = addContextValue(endpointSlaveURLs.getUrl(), contextValue);
						final HttpSolrServer commonsHttpSolrServer = new HttpSolrServer(slaveurl);
						if (isStandaloneInstanceAlive(commonsHttpSolrServer))
						{
							final SolrServer solrServer = new SolrServer(commonsHttpSolrServer, false);
							result.add(solrServer);
						}
						else
						{
							throw new SolrServiceException("In solr Slave :Standalone solr core [" + contextValue + "] [" + false
									+ "] is not alive");
						}
					}
				}
			}

			//			final String slaves = configurationService.getConfiguration().getString("mpl.solr.synonyms.slaves");
			//			List<String> myList = null;
			//			if (StringUtils.isNotEmpty(slaves))
			//			{
			//				myList = Arrays.asList(slaves.split(","));
			//
			//			}
			//
			//			//final List<String> list1 = new ArrayList<String>(myList);
			//			for (final String url : myList)
			//			{
			//
			//				final String slaveurl = addContextValue(url, contextValue);
			//				final HttpSolrServer commonsHttpSolrServer = new HttpSolrServer(slaveurl);
			//				if (isStandaloneInstanceAlive(commonsHttpSolrServer))
			//				{
			//					final SolrServer solrServer = new SolrServer(commonsHttpSolrServer, false);
			//					result.add(solrServer);
			//				}
			//				else
			//				{
			//					throw new SolrServiceException("Standalone solr core [" + contextValue + "] [" + false + "] is not alive");
			//				}
			//
			//			}




		}

		return result;
	}

	private String getCoreName(final String IndexedTypeCode)
	{
		return this.tenant.getTenantID() + "_" + IndexedTypeCode;
	}

	private boolean isStandaloneInstanceAlive(final org.apache.solr.client.solrj.SolrServer standaloneServer)
	{
		try
		{
			return "OK".equalsIgnoreCase((String) standaloneServer.ping().getResponse().get("status"));
		}
		catch (final Exception localException)
		{
			LOG.error("exception in isStandaloneInstanceAlive");
		}
		return false;
	}
}