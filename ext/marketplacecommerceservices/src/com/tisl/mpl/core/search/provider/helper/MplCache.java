package com.tisl.mpl.core.search.provider.helper;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.apache.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * The Class MplCache.
 *
 * @param <K>
 *           the key type
 *
 */
/**
 * @author TCS
 *
 */
public class MplCache<K> extends StandardMBean implements MplCacheMBean
{

	private static final Logger LOG = Logger.getLogger(MplCache.class);

	private final AtomicLong cacheHits = new AtomicLong(0L);

	private final AtomicLong cacheMisses = new AtomicLong(0L);

	private Cache<K, Object> cacheNotSerializable;

	private Cache<K, byte[]> cacheSerializable;


	/** The serializable. */
	final boolean serializable;

	/**
	 * Instantiates a new Mpl cache.
	 *
	 * @param configurationService
	 *           the configuration service
	 * @param cacheName
	 *           the cache name
	 * @throws NotCompliantMBeanException
	 *            the not compliant m bean exception
	 */
	public MplCache(final ConfigurationService configurationService, final String cacheName) throws NotCompliantMBeanException
	{
		super(MplCacheMBean.class);

		//Get everything from local.properties
		final long cacheExpirationInMinutes = configurationService.getConfiguration()
				.getLong(cacheName.toLowerCase() + ".cacheExpirationInMinutes", 120);
		final int cacheSizeInitial = configurationService.getConfiguration().getInt(cacheName.toLowerCase() + ".cacheSizeInitial",
				16384);
		final int cacheSizeMaximum = configurationService.getConfiguration().getInt(cacheName.toLowerCase() + ".cacheSizeMaximum",
				100000);
		final int cacheConcurrencyLevel = configurationService.getConfiguration()
				.getInt(cacheName.toLowerCase() + ".cacheConcurrencyLevel", 8);
		//Get the Mbean name
		final String cacheMBeanName = configurationService.getConfiguration().getString(cacheName.toLowerCase() + ".cacheMBeanName",
				cacheName);

		serializable = configurationService.getConfiguration().getBoolean("cache.serializable", false);
		//Use serializable
		if (serializable)
		{
			cacheSerializable = CacheBuilder.newBuilder().concurrencyLevel(cacheConcurrencyLevel).initialCapacity(cacheSizeInitial)
					.expireAfterWrite(cacheExpirationInMinutes, TimeUnit.MINUTES).maximumSize(cacheSizeMaximum).build();
		}
		else
		{
			cacheNotSerializable = CacheBuilder.newBuilder().concurrencyLevel(cacheConcurrencyLevel)
					.initialCapacity(cacheSizeInitial).expireAfterWrite(cacheExpirationInMinutes, TimeUnit.MINUTES)
					.maximumSize(cacheSizeMaximum).build();
		}

		try
		{
			ManagementFactory.getPlatformMBeanServer().registerMBean(this,
					new ObjectName(cacheMBeanName + ":name=" + UUID.randomUUID().toString()));
		}
		catch (final Exception e)
		{
			LOG.error("Error registering cache for JMX: ", e);
		}
	}

	/*
	 * The JMX agent MplCache begins by obtaining an MBean server that has been created and initialized by the platform,
	 * by calling the getPlatformMBeanServer() method of the java.lang.management.ManagementFactory class. If no MBean
	 * server has been created by the platform already, then getPlatformMBeanServer() creates an MBean server
	 * automatically by calling the JMX method MBeanServerFactory.createMBeanServer(). The MBeanServer instance obtained
	 * by MplCache is named MplColourCache. Next, MplCache defines an object name for the MBean instance that it will
	 * create. Every JMX MBean must have an object name. The object name is an instance of the JMX class ObjectName and
	 * must conform to the syntax defined by the JMX specification. Namely, the object name must contain a domain and a
	 * list of key-properties. In the object name defined by MplCache, the domain is
	 * com.tisl.mpl.core.search.provider.helper (the package in which the example MBean is contained). In addition, the
	 * key-property declares that this object is of the type MplCache. An instance of a MplCache object, named mbean, is
	 * created. The MplCache object named mbean is then registered as an MBean in the MBean server MplCache with the
	 * object name name, by passing the object and the object name into a call to the JMX method
	 * MBeanServer.registerMBean().
	 */

	/**
	 * Gets the.
	 *
	 * @param <T>
	 *           the generic type
	 * @param key
	 *           the key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final K key)
	{
		if (serializable)
		{
			final byte[] value = cacheSerializable.getIfPresent(key);
			if (value == null)
			{
				cacheMisses.incrementAndGet();
				return null;
			}
			else
			{
				cacheHits.incrementAndGet();
				ObjectInputStream objectInputStream = null;
				try
				{
					return (T) (objectInputStream = new ObjectInputStream(new ByteArrayInputStream(value))).readObject();

				}
				catch (final IOException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.EXCEPTIONONCONV, e);
				}
				catch (final ClassNotFoundException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.EXCEPTIONONCONV, e);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error(MarketplacecommerceservicesConstants.EXCEPTIONONCONV, e);
				}
				finally
				{
					try
					{
						if (objectInputStream != null)
						{
							objectInputStream.close();
						}
					}
					catch (final IOException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.EXCEPTIONONCONV, e);
					}
					catch (final EtailNonBusinessExceptions e)
					{
						LOG.error(MarketplacecommerceservicesConstants.EXCEPTIONONCONV, e);
					}
				}
				return null;
			}
		}
		else
		{
			final Object value = cacheNotSerializable.getIfPresent(key);
			if (value == null)
			{
				cacheMisses.incrementAndGet();
				return null;
			}
			else
			{
				cacheHits.incrementAndGet();
				return (T) value;
			}
		}
	}

	/**
	 * Put.
	 *
	 * @param <T>
	 *           the generic type
	 * @param key
	 *           the key
	 * @param dto
	 *           the dto
	 */
	public <T> void put(final K key, final T dto)
	{
		if (serializable)
		{
			try
			{
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(dto);
				cacheSerializable.put(key, byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.close();
				objectOutputStream.close();
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
		else
		{
			try
			{
				cacheNotSerializable.put(key, dto);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.core.search.provider.helper.MplCacheMBean#getSize()
	 */
	@Override
	public long getSize()
	{
		return serializable ? cacheSerializable.size() : cacheNotSerializable.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.core.search.provider.helper.MplCacheMBean#getHits()
	 */
	@Override
	public long getHits()
	{
		return cacheHits.get();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.core.search.provider.helper.MplCacheMBean#getMisses()
	 */
	@Override
	public long getMisses()
	{
		return cacheMisses.get();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.core.search.provider.helper.MplCacheMBean#clear()
	 */
	@Override
	public void clear()
	{
		if (serializable)
		{
			cacheSerializable.invalidateAll();
		}
		else
		{
			cacheNotSerializable.invalidateAll();
		}
		cacheHits.set(0L);
		cacheMisses.set(0L);
	}
}
