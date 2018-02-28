package com.tisl.mpl.cache.strategy.impl;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cache.strategy.MplApiCachingStrategy;
import com.tisl.mpl.cache.strategy.MplMemcachedClientHelper;
import com.tisl.mpl.wsdto.MplNewProductDetailMobileWsData;

import de.hybris.platform.servicelayer.config.ConfigurationService;


public class MplDefaultApiCachingStrategy implements MplApiCachingStrategy
{
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplMemcachedClientHelper mplMemcachedClientHelper;

	int ttl = 43200000;

	private final static Logger LOG = Logger.getLogger(MplDefaultApiCachingStrategy.class.getName());


	@Override
	public void put(String code, MplNewProductDetailMobileWsData result)
	{
		// YTODO Auto-generated method stub
		try
		{
			ttl = Integer.parseInt(configurationService.getConfiguration().getString("pdp.memcache.ttl"));

			LOG.debug("ttl for Memcache" + ttl);

			LOG.debug("Putting the value for PRODUCT LISTING ID : " + code + "Value : " + result);

			final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
			cacheClient.set(code, ttl, result);

			LOG.debug("Able to Put the value for PRODUCT LISTING ID : " + code + "Value : " + result);
		}
		catch (final Exception exception)
		{
			LOG.error("Error during cacheClient creation for PDP Mem cache Impl : Method : PUT");
			exception.printStackTrace();
		}

	}

	@Override
	public MplNewProductDetailMobileWsData get(String code)
	{
		// YTODO Auto-generated method stub
		try
		{
			LOG.debug("Returning value for Code: " + code);
			final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
			return ((MplNewProductDetailMobileWsData) cacheClient.get(code));
		}
		catch (final Exception exception)
		{
			LOG.error("Error during cacheClient creation for PDP Mem cache Impl : Method : GET");
			exception.printStackTrace();
		}
		return null;
	}

	@Override
	public void remove(String code)
	{
		// YTODO Auto-generated method stub
		try
		{
			LOG.debug("Deleting value for the code : " + code);
			final MemcachedClient cacheClient = getMplMemcachedClientHelper().getCacheClient();
			cacheClient.delete(code);
		}
		catch (final Exception exception)
		{
			LOG.error("Error during cacheClient creation for PDP Mem cache Impl : Method : REMOVE");
			exception.printStackTrace();
		}
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
