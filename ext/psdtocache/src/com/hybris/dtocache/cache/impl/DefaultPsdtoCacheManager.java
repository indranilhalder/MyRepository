/**
 * 
 */
package com.hybris.dtocache.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.hybris.dtocache.cache.PsdtoCacheManager;

import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;

/**
 * 
 * @author giovanni.ganassin@sap.com
 * Jan 16, 2016 7:19:53 PM
 *
 */
public class DefaultPsdtoCacheManager implements PsdtoCacheManager
{

	private CacheController cacheController;

	private Map<String, CacheRegion> regions;

	public DefaultPsdtoCacheManager()
	{
		this.regions = new ConcurrentHashMap<String, CacheRegion>();
	}

	/**
	 * logger
	 */
	private static Logger LOG = Logger.getLogger(DefaultPsdtoCacheManager.class);

	@Override
	public EHCacheRegion get(String name)
	{
		if (this.regions.size() == 0)
		{
			LOG.debug("Indexing all the existing cacheRegions available");
			for (CacheRegion cacheRegion : this.cacheController.getRegions())
			{
				this.regions.put(cacheRegion.getName(), cacheRegion);
				LOG.debug(cacheRegion.getName() + " added");
			}
		}
		return (EHCacheRegion) this.regions.get(name);
	}

	public CacheController getCacheController()
	{
		return cacheController;
	}

	public void setCacheController(CacheController cacheController)
	{
		this.cacheController = cacheController;
	}

}