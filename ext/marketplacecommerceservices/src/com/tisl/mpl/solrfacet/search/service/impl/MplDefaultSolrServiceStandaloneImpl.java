
package com.tisl.mpl.solrfacet.search.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.ClusterConfig;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServer;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServiceStandaloneImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;



public class MplDefaultSolrServiceStandaloneImpl extends SolrServiceStandaloneImpl
{
	private static final Logger LOG = Logger.getLogger(SolrServiceStandaloneImpl.class);
	private final Map<String, LBHttpSolrServer> standaloneSolrCores = new ConcurrentHashMap();
	private final Map<String, SolrConfig> existingStandaloneSolrConfigs = new HashMap();

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public SolrServer getSolrServer(final SolrConfig solrConfig, final String indexedTypeCode) throws SolrServiceException
	{
		ServicesUtil.validateParameterNotNull(solrConfig, "Parameter 'solrConfig' can not be null");
		ServicesUtil.validateParameterNotNull(indexedTypeCode, "Parameter 'indexedTypeCode' can not be null");
		final String contextValue = getCoreName(indexedTypeCode);
		if ((!(this.standaloneSolrCores.containsKey(contextValue))) || (hasConfigurationChanged(contextValue, solrConfig)))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("New Standalone LBHttpSolrServer for [" + contextValue + "]");
			}

			final LBHttpSolrServer newCluster = getCluster(solrConfig, contextValue);
			final DefaultHttpClient client = (DefaultHttpClient) newCluster.getHttpClient();
			final String userName = configurationService.getConfiguration().getString("solrUI.username");
			final String password = configurationService.getConfiguration().getString("solrUI.password");

			client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

			this.standaloneSolrCores.put(contextValue, newCluster);
		}
		return new SolrServer(this.standaloneSolrCores.get(contextValue), false);
	}

	private String getCoreName(final String IndexedTypeCode)
	{
		return this.tenant.getTenantID() + "_" + IndexedTypeCode;
	}

	private final boolean hasConfigurationChanged(final String contextValue, final SolrConfig solrConfig)
	{
		if (this.existingStandaloneSolrConfigs.containsKey(contextValue))
		{
			final SolrConfig existingConfig = this.existingStandaloneSolrConfigs.get(contextValue);
			return hasConfigChange(existingConfig, solrConfig, contextValue);
		}
		this.existingStandaloneSolrConfigs.put(contextValue, solrConfig);
		return true;
	}

	private boolean hasConfigChange(final SolrConfig existingConfig, final SolrConfig solrConfig, final String key)
	{
		final boolean changed = !(areNotNullAndEqual(existingConfig, solrConfig));
		if (changed)
		{
			this.existingStandaloneSolrConfigs.put(key, solrConfig);
			LOG.debug("Standalone SOLR server configuration changed");
		}
		return changed;
	}

	private boolean areNotNullAndEqual(final SolrConfig existingConfig, final SolrConfig solrConfig)
	{
		if ((existingConfig != null) && (solrConfig != null))
		{
			boolean result = existingConfig.getMode().equals(solrConfig.getMode());

			final ClusterConfig existingClusterConfig = existingConfig.getClusterConfig();
			final ClusterConfig solrClusterConfig = solrConfig.getClusterConfig();
			if ((existingClusterConfig != null) && (solrClusterConfig != null))
			{
				result = (result) && (existingClusterConfig.isEmbeddedMaster() == solrClusterConfig.isEmbeddedMaster());
				result = (result) && (sameURLs(existingClusterConfig.getEndpointURLs(), solrClusterConfig.getEndpointURLs()));
				result = (result) && (sameHTTPParams(existingClusterConfig, solrClusterConfig));
				result = (result) && (existingClusterConfig.isUseMasterNodeExclusivelyForIndexing() == solrClusterConfig
						.isUseMasterNodeExclusivelyForIndexing());
			}
			return result;
		}
		return false;
	}

	private boolean sameHTTPParams(final ClusterConfig existingClusterConfig, final ClusterConfig solrClusterConfig)
	{
		boolean result = sameIntegers(existingClusterConfig.getAliveCheckInterval(), solrClusterConfig.getAliveCheckInterval());
		result = (result) && (sameIntegers(existingClusterConfig.getConnectionTimeout(), solrClusterConfig.getConnectionTimeout()));
		result = (result) && (sameIntegers(existingClusterConfig.getMaxConnections(), solrClusterConfig.getMaxConnections()));
		result = (result)
				&& (sameIntegers(existingClusterConfig.getMaxConnectionsPerHost(), solrClusterConfig.getMaxConnectionsPerHost()));
		result = (result) && (sameIntegers(existingClusterConfig.getReadTimeout(), solrClusterConfig.getReadTimeout()));
		result = (result) && (sameIntegers(existingClusterConfig.getSocketTimeout(), solrClusterConfig.getSocketTimeout()));
		result = (result) && (existingClusterConfig.isTcpNoDelay() == solrClusterConfig.isTcpNoDelay());
		return result;
	}

	private boolean sameIntegers(final Integer int1, final Integer int2)
	{
		if ((int1 == null) && (int2 == null))
		{
			return true;
		}
		if ((((int1 == null) ? 1 : 0) ^ ((int2 == null) ? 1 : 0)) != 0)
		{
			return false;
		}
		return int1.equals(int2);
	}

	private boolean sameURLs(final List<EndpointURL> endpointURLs, final List<EndpointURL> endpointURLs2)
	{
		final boolean empty2 = CollectionUtils.isEmpty(endpointURLs2);
		final boolean empty1 = CollectionUtils.isEmpty(endpointURLs);
		if ((empty2) && (empty1))
		{
			return true;
		}
		if ((empty2 ^ empty1))
		{
			return false;
		}
		if (endpointURLs.size() != endpointURLs2.size())
		{
			return false;
		}
		for (int i = 0; i < endpointURLs.size(); ++i)
		{
			if (!(sameUrl(endpointURLs2.get(i), endpointURLs.get(i))))
			{
				return false;
			}
		}
		return true;
	}

	private boolean sameUrl(final EndpointURL endpointURL, final EndpointURL endpointURL2)
	{
		return ((endpointURL.isMaster() == endpointURL2.isMaster()) && (endpointURL.getUrl().equals(endpointURL2.getUrl())));
	}
}
