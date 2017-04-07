/**
 *
 */
package com.hybris.yps.nodewarmer.service;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.hybris.yps.nodewarmer.NodeStartupStateHolder;
import com.hybris.yps.nodewarmer.NodeStartupStateHolder.StartupState;
import com.hybris.yps.nodewarmer.warmer.WarmerStrategy;


/**
 * Service that creates a thread at startup that polls the tenant checking if the tenant has finished starting up. Once
 * the tenant has started we execute the list of {@link WarmerStrategy}. Each {@link WarmerStrategy} warms up part of
 * the system e.g. loads the Composed Types, HTTP requests to common pages etc
 *
 * @author brendan.dobbs
 *
 */
public class NodeWarmupService
{
	private List<WarmerStrategy> warmerStrategies;
	private static final Logger LOG = Logger.getLogger(NodeWarmupService.class);
	private static final String WARM_SLAVES = "nodewarmer.warmslavestenants";
	private static final String NODE_WARMING_ENABLED = "nodewarmer.enabled";

	@Resource
	private UserService userService;

	@Resource
	private ConfigurationService configurationService;

	@Resource
	private CacheConfiguration cacheConfiguration;

	/**
	 *
	 * @param tenant
	 */
	public void execute(final AbstractTenant tenant)
	{
		if (tenant.getTenantID().equals("junit"))
		{
			// we don't care about the junit tenant
			return;
		}

		Registry.setCurrentTenant(tenant);

		if (CollectionUtils.isEmpty(warmerStrategies))
		{
			return;
		}

		LOG.info("Starting warmup process for [" + tenant.getTenantID() + "]");
		for (final WarmerStrategy warmerStrategy : warmerStrategies)
		{
			warmerStrategy.execute();
		}
	}

	/**
	 * Called at startup, starts a new processor thread
	 */
	@PostConstruct
	public void run()
	{
		// run the processor as a thread so we don't hold up the starting of the tenant/app context
		final Thread t = new ProcessorThread();
		t.start();
	}

	/**
	 * @param warmerStrategies
	 *           the warmerStrategies to set
	 */
	public void setWarmerStrategies(final List<WarmerStrategy> warmerStrategies)
	{
		this.warmerStrategies = warmerStrategies;
	}

	public class ProcessorThread extends Thread
	{
		@Override
		public void run()
		{
			while (!Registry.getMasterTenant().isStarting() && !Registry.getMasterTenant().isStopping())
			{
				try
				{
					Thread.sleep(2000);
				}
				catch (final InterruptedException e)
				{
					throw new IllegalStateException("Could not execute warmup process ", e);
				}
			}

			Registry.activateMasterTenant();

			if (isEnabled())
			{
				final StopWatch stopWatch = new StopWatch("Node warmup process");
				stopWatch.start();
				// ensure there are no permissions/search restriction issues by running as admin
				userService.setCurrentUser(userService.getAdminUser());

				//  execute the warm up process for the master tenant
				execute(Registry.getMasterTenant());

				// slave tenant warming is currently disabled because there appears to be a bug
				if (configurationService.getConfiguration().getBoolean(WARM_SLAVES, Boolean.FALSE).booleanValue())
				{
					final Collection<SlaveTenant> slaveTenants = Registry.getSlaveTenants().values();
					for (final SlaveTenant slave : slaveTenants)
					{
						execute(slave);
					}
				}
				// finished warmup, mark as ready
				NodeStartupStateHolder.getInstance().setStartupState(StartupState.STARTED);

				outputCacheStatisticsData();

				stopWatch.stop();
				LOG.info(stopWatch.shortSummary());
			}
		}

	}

	protected boolean isEnabled()
	{
		return configurationService.getConfiguration().getBoolean(NODE_WARMING_ENABLED, Boolean.TRUE).booleanValue();
	}

	protected void outputCacheStatisticsData()
	{
		final Collection<CacheRegion> regions = cacheConfiguration.getRegions();
		for (final CacheRegion region : regions)
		{
			LOG.info("*****************************************");
			LOG.info(" Region : " + region.getName());
			LOG.info("     current size    :" + region.getCacheRegionStatistics().getInstanceCount());
			LOG.info("     max reached size: " + region.getMaxReachedSize());
			LOG.info("     max entries     :" + region.getCacheMaxEntries());


			if (LOG.isDebugEnabled())
			{
				if (!region.getName().equals("queryCacheRegion"))
				{
					for (final Object type : region.getCacheRegionStatistics().getTypes())
					{
						LOG.debug("      Type : " + getTypeNamesForDeployment(type) + " count: "
								+ region.getCacheRegionStatistics().getInstanceCount(type));
					}
				}
				//				else
				//				{
				//					for (final CacheKey key : region.getAllKeys())
				//					{
				//						LOG.debug("     Key : " + key);
				//					}
				//				}
			}

			LOG.info("*****************************************");
			LOG.info("");
		}
	}

	private String formatValidNameForTypeCode(final int typeCode)
	{
		final StringBuilder builder = new StringBuilder(getTypeNameForTypeCode(typeCode));
		final PersistenceManager persistenceManager = Registry.getPersistenceManager();
		final ItemDeployment itemDeployment = getItemDeployment(typeCode, persistenceManager);
		Collection<ItemDeployment> subDeployments = null;

		if (itemDeployment != null)
		{
			builder.append(" (").append(itemDeployment.getName().replaceAll("\\.", ". ")).append(")");
			subDeployments = persistenceManager.getAllSubDeployments(itemDeployment);
		}

		if (CollectionUtils.isNotEmpty(subDeployments))
		{
			builder.append(" - ");
			for (final ItemDeployment id : subDeployments)
			{
				builder.append(getTypeNameForTypeCode(id.getTypeCode())).append(", ");
			}
		}
		return builder.toString();
	}

	private ItemDeployment getItemDeployment(final int typeCode, final PersistenceManager persistenceManager)
	{
		try
		{
			return persistenceManager.getItemDeployment(typeCode);
		}
		catch (final Exception e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No deployment for type code: " + typeCode, e);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private String getTypeNameForTypeCode(final int typeCode)
	{
		final TypeManager typeManager = TypeManager.getInstance();
		String result;
		try
		{
			result = typeManager.getRootComposedType(typeCode).getCode();
		}
		catch (final Exception localException)
		{
			result = getFallbackTypeNameForTypeCode(typeCode);
		}
		return result;
	}

	private String getFallbackTypeNameForTypeCode(final int typeCode)
	{
		try
		{
			final ItemDeployment itemDeployment = Registry.getPersistenceManager().getItemDeployment(typeCode);
			if (itemDeployment == null)
			{
				return "INVALID";
			}
			return itemDeployment.getDatabaseTableName();
		}
		catch (final Exception e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No type name for type code: " + typeCode, e);
			}
			return "INVALID";
		}
	}

	private String getTypeNamesForDeployment(final Object typeObj)
	{
		final String type = (typeObj == null) ? null : typeObj.toString();
		String result;
		try
		{
			result = formatValidNameForTypeCode(Integer.parseInt(type));
		}
		catch (final NumberFormatException localNumberFormatException)
		{
			result = "INVALID";
		}
		return result;
	}
}
