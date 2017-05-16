/**
 *
 */
package com.tisl.mpl.cache.strategy;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplMemcachedClientHelper
{
	private final static Logger LOG = Logger.getLogger(MplMemcachedClientHelper.class.getName());


	MemcachedClient cacheClient;

	public MplMemcachedClientHelper(final String ip)
	{
		try
		{
			LOG.error("IP for Memcache" + ip);
			this.cacheClient = new MemcachedClient(AddrUtil.getAddresses(ip));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the cacheClient
	 */
	public MemcachedClient getCacheClient()
	{
		return this.cacheClient;
	}

}
