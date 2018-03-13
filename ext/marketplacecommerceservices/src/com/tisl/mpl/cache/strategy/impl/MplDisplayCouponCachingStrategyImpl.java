package com.tisl.mpl.cache.strategy.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cache.strategy.MplDisplayCouponCachingStrategy;
import com.tisl.mpl.cache.strategy.MplMemcachedClientHelper;
import com.tisl.mpl.wsdto.MplVisibleCouponsDTO;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import net.spy.memcached.MemcachedClient;

/**
 * 
 * @author TCS
 *
 */
public class MplDisplayCouponCachingStrategyImpl implements MplDisplayCouponCachingStrategy
{

	private final Map<String, List<MplVisibleCouponsDTO>> cache = new ConcurrentHashMap();
	String memCacheEnabled;
	boolean isMemCachedEnabled;
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplMemcachedClientHelper mplMemcachedClientHelper;
	
	int ttl = 3600;
	
	private final static Logger LOG = Logger.getLogger(MplDisplayCouponCachingStrategyImpl.class.getName());
	
	/**
	 * The Method Stores Coupon Data in Cache
	 * 
	 * @param key
	 * @param couponList
	 */
	@Override
	public void put(String key, List<MplVisibleCouponsDTO> couponList)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			this.cache.put(key, couponList);
		}
		else
		{
			try
			{
				LOG.debug("ttl for Memcache" + ttl);

				LOG.debug("Putting the value for Key: " + key + "Value : " + couponList);
				
				ttl = Integer.parseInt(configurationService.getConfiguration().getString("display.coupon.memcache.ttl"));
				
				final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
				cacheClient.set(key, ttl, couponList);
				
				LOG.debug("Able to Put the value for KEY : " + key + "Value : " + couponList);
			}
			catch(final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Coupon cache Impl : Method : PUT");
				LOG.error(">>>>>>>>>>>>>>>>Reverting to Hybris Cache >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				
				this.cache.put(key, couponList);
				
				LOG.error(">>>>>>>>>>>>>>>>Added to Hybris Cache >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				LOG.error("Exception Trace",exception);
			}
		}
		
	}

	/**
	 * The Method returns Coupon List from Cache
	 * 
	 * @param key
	 * @return List<MplVisibleCouponsDTO>
	 */
	@Override
	public List<MplVisibleCouponsDTO> get(String key)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			return (this.cache.get(key));
		}
		else
		{
			try
			{
			LOG.debug("Returning Coupon value for Code: " + key);
			final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
			return ((List) cacheClient.get(key));
			}
			catch(final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Coupon Mem cache Impl : Method : get");
				LOG.error(">>>>>>>>>>>>>>>>Fetching from Hybris Cache >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				
				//Added for Fall Back Mechanism
				if (this.cache.containsKey(key))
				{
					LOG.error(">>>>>>>>>>>>>>>>Sending Data from Hybris Cache  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					return (this.cache.get(key));
				}
				
				LOG.error("Exception Trace",exception);
				
			}
		}
		return null;
	}

	/**
	 * The Methods Deletes Coupon Data from Cache
	 * 
	 * @param key
	 */
	@Override
	public void remove(String key)
	{
		isMemCachedEnabled = Boolean.parseBoolean(this.memCacheEnabled);
		if (!isMemCachedEnabled)
		{
			this.cache.remove(key);
		}
		else
		{
			try
			{
				LOG.debug("Deleting value for the code : " + key);
				final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
				cacheClient.delete(key);
			}
			catch (final Exception exception)
			{
				LOG.error("Error during cacheClient creation for Coupon Mem cache Impl : Method : remove");

				//Added for Fall Back Mechanism
				if (this.cache.containsKey(key))
				{
					LOG.error(">>>>>>>>>>>>>>>>Removing Data from Hybris Cache  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					this.cache.remove(key);
				}

				LOG.error("Exception Trace",exception);
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
