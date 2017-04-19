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

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplMemcachedClientHelper mplMemcachedClientHelper;



	//final String ip = configurationService.getConfiguration().getString("promotion.memcache.env.ip");
	int ttl = 3600;



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

				ttl = Integer.parseInt(configurationService.getConfiguration().getString("promotion.memcache.ttl"));


				LOG.debug("ttl for Memcache" + ttl);

				LOG.debug("Putting the value for CART GUID : " + code + "Value : " + results);

				final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
				cacheClient.set(code, ttl, results);

				LOG.debug("Able to Put the value for CART GUID : " + code + "Value : " + results);
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl : Method : PUT");
				exception.printStackTrace();
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
				LOG.debug("Returning value for Code: " + code);
				final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
				return ((List) cacheClient.get(code));
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl : Method : get");
				exception.printStackTrace();
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
				LOG.debug("Deleting value for the code : " + code);
				final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
				cacheClient.delete(code);
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Promtion Mem cache Impl : Method : remove");
				exception.printStackTrace();
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


	/**
	 * @return the mplMemcachedClientHelper
	 */
	public MplMemcachedClientHelper getMplMemcachedClientHelper()
	{
		return mplMemcachedClientHelper;
	}


	/**
	 * @param mplMemcachedClientHelper
	 *           the mplMemcachedClientHelper to set
	 */
	public void setMplMemcachedClientHelper(final MplMemcachedClientHelper mplMemcachedClientHelper)
	{
		this.mplMemcachedClientHelper = mplMemcachedClientHelper;
	}
}
