/**
 *
 */
package com.tisl.mpl.cache.strategy;

import de.hybris.platform.promotions.jalo.CachingStrategy;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class MplDefaultCachingStrategy implements CachingStrategy
{

	private final Map<String, List<PromotionResult>> cache = new ConcurrentHashMap();
	String memCacheEnabled;
	boolean isMemCachedEnabled;

	MemcachedClient cacheClient;
	int ttl = 3600;

	@Autowired
	private ConfigurationService configurationService;

	private final static Logger LOG = Logger.getLogger(MplDefaultCachingStrategy.class.getName());


	@Override
	public void put(final String code, final List<PromotionResult> results)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			this.cache.put(code, results);
		}
		else
		{
			try
			{
				final String ip = configurationService.getConfiguration().getString("promotion.memcache.env.ip");
				ttl = Integer.parseInt(configurationService.getConfiguration().getString("promotion.memcache.ttl"));

				cacheClient = new MemcachedClient(AddrUtil.getAddresses(ip));
				cacheClient.set(code, ttl, results);
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl");
			}

		}

	}

	@Override
	public List<PromotionResult> get(final String code)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			return (this.cache.get(code));
		}
		else
		{
			try
			{
				return ((List) cacheClient.get(code));
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl");
			}

		}
		return null;

	}


	@Override
	public void remove(final String code)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			this.cache.remove(code);
		}
		else
		{
			try
			{
				cacheClient.delete(code);
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl");
			}

		}

	}


	/**
	 * @return the memCacheEnabled
	 */
	public String getMemCacheEnabled()
	{
		return memCacheEnabled;
	}


	/**
	 * @param memCacheEnabled
	 *           the memCacheEnabled to set
	 */
	public void setMemCacheEnabled(final String memCacheEnabled)
	{
		this.memCacheEnabled = memCacheEnabled;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
