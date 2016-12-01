/**
 * [y] hybris Platform Copyright
 * (c) 2000-2015 hybris AG All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.hybris.dtocache.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.hybris.dtocache.cache.key.KeyStrategy;
import com.hybris.dtocache.config.Config;
import com.hybris.dtocache.exception.DtoCacheException;
import com.hybris.dtocache.populator.analyzer.PopulatorAnalyzer;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;

/**
 * Factory that builds {@link CacheDescriptor} instances easily based on properties naming conventions.
 *
 * @author Jose Carreno
 *
 */
public class CacheDescriptorFactory
{
	/**
	 * logger
	 */
	private static Logger LOG = Logger.getLogger(CacheDescriptorFactory.class);

	/**
	 * references all cache descriptors by their populator names (built by this class).
	 */
	private final Map<String, CacheDescriptor> registry;

	/**
	 * references all cache descriptors by their ItemModel class names (built by this class).
	 */
	private final Map<String, List<CacheDescriptor>> invalidationRegistry;

	/**
	 * Cache manager.
	 */
	private PsdtoCacheManager cacheManager;

	/**
	 * Builds the factory. Initializes an empty registry.
	 */
	public CacheDescriptorFactory()
	{
		this.registry = new ConcurrentHashMap<String, CacheDescriptor>();
		this.invalidationRegistry = new ConcurrentHashMap<String, List<CacheDescriptor>>();
	}

	/**
	 * Creates and indexes the {@link CacheDescriptor} the first time the aspect is triggered on a given {@link Populator}.
	 * It will mostly read configuration from properties files and create the corresponding cache.
	 * Properties are :
	 * <ul>
	 * <li>psdtocache.my.package.MyPopulator=cachename : cache name associated to this populator.</li>
	 * <li>psdtocache.my.package.MyPopulator.keyStrategy=aKeyStrategy : key strategy associated to this cache. You can define or choose key strategies from spring configurations</li>
	 * <li>psdtocache.my.package.MyPopulator.fields=some,fields,are,here : list of all the fields of dto handled by the
	 * given populator. If omitted, Psdtocache will automatically resolve them.</li>
	 * <li>psdtocache.my.package.MyPopulator.invalidate=true||false : if true, any event that changes the related ItemModel, will try to invalidate the cached DTO instance.</li>
	 * </ul>
	 * 
	 * @param populatorClassName
	 * @param itemModel
	 * @return
	 */
	public CacheDescriptor createCacheDescriptor(final String populatorClassName, final ItemModel itemModel)
	{
		CacheDescriptor cacheDescriptor = registry.get(populatorClassName);
		if (cacheDescriptor != null)
		{
			return cacheDescriptor;
		}

		cacheDescriptor = new CacheDescriptor(populatorClassName);
		registry.put(populatorClassName, cacheDescriptor);

		final String cacheName = Config.get().getString(Config.SUFFIX + populatorClassName, Config.VALUES_NO_CACHE);
		cacheDescriptor.setCacheRegion(getCacheManager().get(cacheName));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Using cache " + cacheName + " for populator " + populatorClassName);
		}

		boolean isInvalidate = Config.get().getBoolean(Config.SUFFIX + populatorClassName + "." + Config.PARAMS_INVALIDATE, Config.VALUES_INVALIDATE_DEFAULT);
		cacheDescriptor.setInvalidate(isInvalidate);

		setupFields(itemModel, cacheDescriptor, cacheName);

		setupKeyStrategy(cacheDescriptor);

		if (LOG.isInfoEnabled())
		{
			LOG.info("Cache is " + (cacheDescriptor.isDisabled() ? "disabled" : "enabled") + " for "
					+ populatorClassName);
		}

		return cacheDescriptor;
	}

	private void setupFields(final ItemModel itemModel, CacheDescriptor cacheDescriptor, final String cacheName)
	{
		try
		{
			final String fieldsProperty = Config.SUFFIX + cacheDescriptor.getPopulatorClassName() + "." + Config.PARAMS_FIELDS;
			final String fields = Config.get().getString(fieldsProperty, null);
			if (fields != null)
			{
				final List<String> props = Arrays.asList(fields.split("\\s*,\\s*"));
				cacheDescriptor.setProperties(props);
				LOG.info("configuring populator '" + cacheDescriptor.getPopulatorClassName() + "' with fields: " + Arrays.toString(props.toArray()));
			} else
			{
				String discoveredProps = "?";

				try
				{
					final List<String> props = PopulatorAnalyzer.analyse(cacheDescriptor.getPopulatorClassName(), true);
					LOG.info("Auto configuring populator '" + cacheDescriptor.getPopulatorClassName() + "' with fields: " + props);
					if (props != null && !props.isEmpty())
					{	
						if (!cacheName.equals(Config.VALUES_NO_CACHE))
						{
							// if we specify the populator config with an empty list of fields, we'll automatically
							// retrieve them
							cacheDescriptor.setAutoWired(true);
							cacheDescriptor.setProperties(props);
						} else
						{
							// We do *NOT* cache things automatically if no populator config is set in the config file.
							cacheDescriptor.setCacheRegion(null);
						}
						discoveredProps = Arrays.toString(props.toArray());

					}
				} catch (final DtoCacheException e)
				{
					discoveredProps = e.getClass().getSimpleName() + ":" + e.getMessage();
					cacheDescriptor.setCacheRegion(null);
				}

				LOG.debug("Missing property '" + fieldsProperty + "'. Advice from PopulatorAnalyzer: "
						+ discoveredProps);

			}
			indexCacheDescriptor(cacheDescriptor, itemModel);
		} catch (final Exception e)
		{
			LOG.error("Can not create a mapper for populator '" + cacheDescriptor.getPopulatorClassName() + "'. Reason : " + e.getMessage());
			cacheDescriptor.setCacheRegion(null);
		}
	}

	private void setupKeyStrategy(CacheDescriptor cacheDescriptor)
	{
		KeyStrategy strategy;
		String strategyBean;
		final String keyStrategy = Config.get().getString(Config.SUFFIX + cacheDescriptor.getPopulatorClassName() + "." + Config.PARAMS_KEY_STRATEGY, "");
		if (StringUtils.isEmpty(keyStrategy)) {
			if (cacheDescriptor.isInvalidate()) {
				strategyBean = Config.VALUES_DEFAULT_INVALIDABLE_KEY_STRATEGY;
			} else {
				strategyBean = Config.VALUES_DEFAULT_KEY_STRATEGY;
			}
		} else {
			strategyBean = keyStrategy; 
		}
		try
		{
			strategy = Registry.getApplicationContext().getBean(strategyBean, KeyStrategy.class);
			cacheDescriptor.setKeyStrategy(strategy);
		} catch (Exception e)
		{
			LOG.error("the populator '" + cacheDescriptor.getPopulatorClassName() + "' seems badly configured - cannot find the keyStrategy Spring definition: " + e.getMessage());
			cacheDescriptor.setCacheRegion(null);
		}
	}

	/**
	 *
	 * @param populatorClassName
	 *            populator fully qualified name
	 * @return cache descriptor for this populator
	 */
	public CacheDescriptor getCacheDescriptor(final String populatorClassName)
	{
		return this.registry.get(populatorClassName);
	}

	/**
	 * 
	 * used to extract from the {@link Populator} the used ItemModel exact type.
	 * In this way - when an InvalidationEvent is triggered cluster-wise - we are able to invalid all
	 * the possible affected DTOs. 
	 * 
	 * @param cacheDescriptor
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws DtoCacheException 
	 */
	private void indexCacheDescriptor(CacheDescriptor cacheDescriptor, final ItemModel itemModel) throws DtoCacheException
	{
		LOG.info("indexing the invalidation registry for populator: " + cacheDescriptor.getPopulatorClassName());
		
		List<CacheDescriptor> descriptors = this.invalidationRegistry.get(itemModel.getPk().getTypeCodeAsString());
		String typeCode = itemModel.getPk().getTypeCodeAsString();
		if (descriptors == null)
		{
			descriptors = new ArrayList<CacheDescriptor>();
			this.invalidationRegistry.put(typeCode, descriptors);
		}
		descriptors.add(cacheDescriptor);
		LOG.info("typeCode '" + typeCode + "' ('" + itemModel.getClass().getName() + "') indexed into the invalidation registry (" + descriptors.size() + " descriptors)");
	}

	public List<CacheDescriptor> getCacheDescriptors(String typeCode)
	{
		return this.invalidationRegistry.get(typeCode);
	}

	public PsdtoCacheManager getCacheManager()
	{
		return cacheManager;
	}

	public void setCacheManager(PsdtoCacheManager cacheManager)
	{
		this.cacheManager = cacheManager;
	}

}