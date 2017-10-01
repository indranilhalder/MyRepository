package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.SolrCoreNameResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.tisl.mpl.facades.search.data.EndpointSlaveURL;


public class SolrSlaveReplicatorJob extends AbstractJobPerformable
{
	private static final Logger LOG = Logger.getLogger(SolrSlaveReplicatorJob.class);


	@Autowired
	private FacetSearchConfigService facetSearchConfigService;

	@Autowired
	private ConfigurationService configurationService;

	@Resource
	private SolrCoreNameResolver solrCoreNameResolver;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		HttpURLConnection slaveHTTPConnection = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		int SlaveFailureCount = 0;

		LOG.error("DEBUG: Going to START SolrSlaveReplicatorJob ........");

		final String waitTimeForSlaveforMasterIndexUpdate = configurationService.getConfiguration().getString(
				"SolrSlaveReplicatorJob.mandatory.waittime.insec.forMasterIndexUpdateToComplete");

		if (waitTimeForSlaveforMasterIndexUpdate != null && waitTimeForSlaveforMasterIndexUpdate.trim().length() > 0)
		{
			try
			{
				LOG.error("DEBUG: Going to wait for Master to complete indexing for :: " + waitTimeForSlaveforMasterIndexUpdate
						+ " Sec");
				Thread.sleep(1000 * Integer.parseInt(waitTimeForSlaveforMasterIndexUpdate));
				LOG.error("DEBUG: UP  after waiting for Master to complete indexing for :: " + waitTimeForSlaveforMasterIndexUpdate
						+ " Sec..... Proceeding for Slave to replicate");
			}
			catch (final Exception e)
			{
				LOG.error("ERROR: in waiting  for :: " + waitTimeForSlaveforMasterIndexUpdate + " Sec");
				e.printStackTrace();
			}
		}

		final List<String> slaveURLsList = getAllSlavesURLs(); //Fetch the Slave URLs.

		if (CollectionUtils.isNotEmpty(slaveURLsList))
		{
			for (final String slaveURL : slaveURLsList)
			{
				slaveHTTPConnection = getURLConnection(slaveURL + "/replication?command=fetchindex", "", "", "GET");
				try
				{
					reader = new BufferedReader(new InputStreamReader(slaveHTTPConnection.getInputStream()));
					LOG.error("DEBUG: Requested Replication for Slave:: " + slaveURL + "/replication?command=fetchindex");

					sb = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null)
					{
						sb.append(line);
					}

					if (sb != null && sb.toString().trim().length() > 0)
					{
						LOG.error("DEBUG: Returned Response:: " + sb.toString());
					}
					else
					{
						LOG.error("ERROR: in gtting response from Purge URL.");
					}
				}
				catch (final Exception e)
				{
					SlaveFailureCount++;
					LOG.error("ERROR: in gtting response from Slave::" + slaveURL + "/replication?command=fetchindex" + e);
					sb = new StringBuffer();
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if (slaveHTTPConnection != null)
						{
							slaveHTTPConnection.disconnect();
						}
						if (reader != null)
						{
							reader.close();
						}
					}
					catch (final Exception e)
					{
						LOG.error("ERROR: in finally...." + e);
						e.printStackTrace();
					}
				}


				/******************** Read The Status of response **************************************/


				if (sb != null && sb.toString().trim().length() > 0)
				{
					final long estimatedSeconds = 0, queueLength = 0, httpStatus = 0, pingAfterSeconds = 0;
					final String responseHeader = "";
					String responseHeaderStatus = "";
					//final String QTime = "", status = "", indexversion = "", generation = "", submissionTime = "";

					try
					{
						final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						final Document doc = dBuilder.parse(new StringBufferInputStream(sb.toString()));
						doc.getDocumentElement().normalize();

						final NodeList lstNodeList = doc.getElementsByTagName("lst");

						final Element lstElement = (Element) lstNodeList.item(0); // As only one <lst> element is persent

						final NodeList intElemetList = lstElement.getElementsByTagName("int"); // get the <int> elements. total two <int> elements present.

						final Element statusElement = (Element) intElemetList.item(0);

						if ("status".equalsIgnoreCase(statusElement.getAttribute("name")))
						{
							LOG.error("DEBUG: Status (0 means OK):: " + (responseHeaderStatus = statusElement.getTextContent()));
						}
						else
						{
							LOG.error("ERROR: 'name' element not found in response");
						}
					}
					catch (final Exception e)
					{
						LOG.error("ERROR: in xml response persing:: " + e);
						e.printStackTrace();
					}
				}
				else
				{
					LOG.error("ERROR: in response::" + sb);
				}
			}
			if (SlaveFailureCount >= slaveURLsList.size())
			{
				LOG.error("ERROR ALL Slave update failed - Count:: " + slaveURLsList.size());
				LOG.error("ERROR: Going to ABORT SolrSlaveReplicatorJob .................");

				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}
		}
		else
		{
			LOG.error("ERROR: in getting Slave URLs.. ");
			LOG.error("ERROR: Going to ABORT SolrSlaveReplicatorJob .................");

			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		try
		{
			final String waittimeForAllSlaveReplicationToComplete = configurationService.getConfiguration().getString(
					"SolrSlaveReplicatorJob.mandatory.waittime.insec.forAllSlaveReplicationToComplete");
			if (waittimeForAllSlaveReplicationToComplete != null && waittimeForAllSlaveReplicationToComplete.trim().length() > 0)
			{
				final int threadWaitTimeinSec = Integer.parseInt(waittimeForAllSlaveReplicationToComplete);
				LOG.error("DEBUG: Going to Sleep SolrSlaveReplicatorJob for ::" + threadWaitTimeinSec
						+ " Seconds to Let all Slaves Complete Replication");
				Thread.sleep(threadWaitTimeinSec * 1000);
				LOG.error("DEBUG: UP after Sleep SolrSlaveReplicatorJob for ::" + threadWaitTimeinSec
						+ " Seconds to Let all Slaves Complete Replication");
			}
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: in waiting for thread.." + e);
			e.printStackTrace();
		}

		LOG.error("DEBUG: Going to END SolrSlaveReplicatorJob........");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private List getAllSlavesURLs()
	{
		final List<String> slaveList = new ArrayList();
		String slaveurl = "";
		FacetSearchConfig facetSearchConfig = null;

		try
		{
			final SolrFacetSearchConfigModel facetSearchConfigModel = getSolrFacetSearchConfigModel();//mplPartialIndexerService.getSolrFacetSearchConfigModel();

			if (null != facetSearchConfigModel)
			{
				facetSearchConfig = facetSearchConfigService.getConfiguration(facetSearchConfigModel.getName());
			}
			else
			{
				LOG.error("ERROR: in  .....getAllSlavesURLs() facetSearchConfigModel is null");
				return null;
			}

			if (facetSearchConfig != null)
			{
				for (final IndexedType indexedType : facetSearchConfig.getIndexConfig().getIndexedTypes().values())
				{
					final String contextValue = getCoreName(this.solrCoreNameResolver.getSolrCoreName(indexedType));
					if (CollectionUtils.isNotEmpty(facetSearchConfig.getSolrConfig().getClusterConfig().getEndpointSlaveURLs()))
					{
						for (final EndpointSlaveURL endpointSlaveURLs : facetSearchConfig.getSolrConfig().getClusterConfig()
								.getEndpointSlaveURLs())
						{
							if (StringUtils.isNotEmpty(endpointSlaveURLs.getUrl()) && endpointSlaveURLs.isSlave())
							{
								slaveurl = addContextValue(endpointSlaveURLs.getUrl(), contextValue);
								if (slaveurl != null && slaveurl.trim().length() > 0)
								{
									slaveList.add(slaveurl);
								}
							}
						}
					}
				}
			}
			else
			{
				LOG.error("ERROR: in  .....getAllSlavesURLs() facetSearchConfig is null");
				return null;
			}
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: in  .....getAllSlavesURLs()" + e);
			e.printStackTrace();
		}

		return slaveList;
	}

	private String getCoreName(final String IndexedTypeCode)
	{
		try
		{
			return Registry.getCurrentTenantNoFallback().getTenantID() + "_" + IndexedTypeCode;
		}
		catch (final Exception e)
		{
			LOG.error("Error!!! in  .....getCoreName()" + e);
			e.printStackTrace();
		}
		return null;
	}

	private String addContextValue(final String url, final String context)
	{
		String encodedContext = "";
		if ((url == null) || (StringUtils.isEmpty(url)))
		{
			return null;
		}
		if ((context == null) || (StringUtils.isEmpty(context)))
		{
			return url;
		}
		try
		{
			encodedContext = URLEncoder.encode(context, "UTF-8");
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: in addContextValue()" + e);
			e.printStackTrace();
		}

		return ((url.endsWith("/")) ? url.concat(encodedContext) : url.concat("/" + encodedContext));
	}

	private HttpURLConnection getURLConnection(final String url, final String username, final String password,
			final String methodSent)
	{
		//final Proxy proxy = null;
		HttpURLConnection urlHTTPConn = null;

		try
		{
			final URL urlObj = new URL(url);
			//			final String proxyEnabled = configurationService.getConfiguration().getString("proxy.enabled");
			//			final String proxyAddress = configurationService.getConfiguration().getString("proxy.address");
			//			final String proxyPort = configurationService.getConfiguration().getString("proxy.port");
			//			if (proxyEnabled != null && "true".equals(proxyEnabled.trim()) && !StringUtils.isEmpty(proxyAddress)
			//					&& !StringUtils.isEmpty(proxyPort))
			//			{
			//				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress.trim(), Integer.parseInt(proxyPort.trim())));
			//				urlHTTPConn = (HttpURLConnection) urlObj.openConnection(proxy);
			//			}
			//			else
			//			{
			urlHTTPConn = (HttpURLConnection) urlObj.openConnection();
			//			}

			//final HttpURLConnection urlHTTPConn = (HttpURLConnection) urlObj.openConnection();
			//final String authString = username + ":" + password;
			//final String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			//urlHTTPConn.setRequestProperty("Authorization", "Basic " + authStringEnc);

			if (null != methodSent && methodSent.trim().length() > 0)
			{
				urlHTTPConn.setRequestMethod(methodSent);
			}
			else
			{
				urlHTTPConn.setRequestMethod("GET");
			}
			urlHTTPConn.setRequestProperty("Accept", "application/json");
			urlHTTPConn.setRequestProperty("Content-type", "application/json");
			urlHTTPConn.setRequestProperty("Content-Language", "en-US");
			urlHTTPConn.setRequestProperty("charset", "utf-8");
			urlHTTPConn.setUseCaches(false);
			urlHTTPConn.setDoInput(true);
			urlHTTPConn.setDoOutput(true);

			return urlHTTPConn;
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: in getURLConnection()" + e);
			e.printStackTrace();
		}

		return null;
	}

	private SolrFacetSearchConfigModel getSolrFacetSearchConfigModel()
	{
		try
		{
			final String queryString = "select {pk} from {SolrFacetSearchConfig} where {name} = ?name";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("name", "mplIndex");

			final List<SolrFacetSearchConfigModel> dataList = flexibleSearchService.<SolrFacetSearchConfigModel> search(query)
					.getResult();

			if (CollectionUtils.isNotEmpty(dataList))
			{
				return dataList.get(0);
			}

			return null;
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in getSolrFacetSearchConfigModel()" + ex);
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * @return the solrCoreNameResolver
	 */
	public SolrCoreNameResolver getSolrCoreNameResolver()
	{
		return solrCoreNameResolver;
	}

	/**
	 * @param solrCoreNameResolver
	 *           the solrCoreNameResolver to set
	 */
	public void setSolrCoreNameResolver(final SolrCoreNameResolver solrCoreNameResolver)
	{
		this.solrCoreNameResolver = solrCoreNameResolver;
	}
}
