/**
 *
 */
package com.cacheclear.eventlistener;

import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.cacheclear.event.CacheClearEvent;
import com.cacheclear.model.CacheClearCronJobModel;
import com.cacheclear.model.CacheClearingStatusModel;


/**
 * <h1>Cache Clear Event Listener</h1>
 * <p>
 * This extends AbstractEventListener<CacheClearEvent>.
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */
public class CacheClearEventListener extends AbstractEventListener<CacheClearEvent>
{

	private static final Logger LOG = Logger.getLogger(CacheClearEventListener.class);

	private CacheConfiguration cacheConfiguration;
	private ModelService modelService;
	private static String DELIMITER = ",";

	/**
	 * This method captures the CacheClearEvent. Cache regions to be cleared in the node are obtained after splitting
	 * regionsToClear String attribute in cacheClearCronJobModel. Existing cache regions from the tenant are obtained by
	 * using getRegions() of CacheConfiguration Cache regions names which match the regions specified in the
	 * cacheClearCronJobModel are cleared. The status attribute is modified according to the result and saved.
	 *
	 * @param event
	 *           CacheClearEvent
	 * @author Krishnakumar Raju
	 *
	 */
	@Override
	protected void onEvent(final CacheClearEvent event)
	{

		final CacheClearCronJobModel cacheClearCronJobModel = event.getCronjob();
		final int nodeID = event.getNodeID();
		final CacheClearingStatusModel status = event.getStatus();
		String status_string = nodeID + " - ";

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Inside event listener of node - " + nodeID + ". About to clear regions - "
					+ cacheClearCronJobModel.getRegionsToClear());
		}

		try
		{
			final String[] regionNames = cacheClearCronJobModel.getRegionsToClear().split(DELIMITER);
			for (final CacheRegion region : getCacheConfiguration().getRegions())
			{
				for (final String name : regionNames)
				{
					if (name.equalsIgnoreCase(region.getName()))
					{
						region.clearCache();
					}
				}
			}
			status_string = status_string + "Cleared caches from the regions - " + cacheClearCronJobModel.getRegionsToClear();

		}
		catch (final Exception e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(status_string + "Exception occured, failed to clear", e);
			}
			status_string = status_string + "Exception occured, failed to clear";

		}
		finally
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Saving the cache clear status");
			}
			final Collection<String> statuses = status.getStatus();
			final List<String> newStatuses = new ArrayList<String>();
			if (statuses != null)
			{
				for (final String statusLine : statuses)
				{
					newStatuses.add(statusLine);
				}
				newStatuses.add(status_string);
			}
			else
			{
				newStatuses.add(status_string);
			}
			status.setStatus(newStatuses);
			getModelService().save(status);
		}


	}

	/**
	 * @return the cacheConfiguration
	 */
	public CacheConfiguration getCacheConfiguration()
	{
		return cacheConfiguration;
	}

	/**
	 * @param cacheConfiguration
	 *           the cacheConfiguration to set
	 */
	public void setCacheConfiguration(final CacheConfiguration cacheConfiguration)
	{
		this.cacheConfiguration = cacheConfiguration;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
