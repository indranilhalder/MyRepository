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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hybris.dtocache.cache.key.CacheKey;
import com.hybris.dtocache.cache.key.KeyStrategy;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;

/**
 * Cache description.
 *
 * @author Sidi Mohammed El Aatifi
 *
 */
public class CacheDescriptor
{
	private static final Logger LOG = Logger.getLogger(CacheDescriptor.class);

	/**
	 * cache name (i.e. : populator fully qualified name)
	 */
	private String populatorClassName;

	/**
	 * if true, the invalidationListener won't invalidate the cache entry for this populator
	 */
	private boolean invalidate = false;

	/**
	 * reference to its CacheRegion
	 */
	private EHCacheRegion cacheRegion;
	
	/**
	 * when the populator is configured without the fields property.
	 * If set to true, eventual properties available in the {@link ItemModel} which are not available
	 * in the DTO will fail to map silently instead of throwing a {@link RuntimeException}.
	 */
	private boolean isAutoWired = false;
	
	/**
	 * List of dto properties that will be cached. All dto properties should be listed here, otherwise, cached values
	 * will miss values.
	 */
	private final List<String> properties = new LinkedList<String>();

	private KeyStrategy keyStrategy;
	
	public CacheDescriptor(String populatorClassName)
	{
		this.populatorClassName = populatorClassName;
	}

	/**
	 * Generates model's cache key.
	 *
	 * @param model
	 *            model object
	 * @return cache key
	 */
	public <T extends ItemModel> CacheKey cacheKey(final T model)
	{
		return this.keyStrategy.buildKey(this.populatorClassName, model.getPk(), model);
	}

	public <T extends ItemModel> CacheKey cacheKey(final PK pk)
	{
		return this.keyStrategy.buildKey(this.populatorClassName, pk, null);
	}

	/**
	 * Creates a cache entry from a data object using reflection mapping.
	 *
	 * @param dataObject
	 *            object to map to a cache entry
	 * @return cache entry populated with all the dataObject values for the declared properties.
	 */
	public Object[] createCacheEntry(final Object dataObject)
	{
		final Object[] e = new Object[properties.size()];

		map(dataObject, e);

		return e;
	}

	/**
	 * Checks if cache is disabled for the populator class name
	 *
	 * @return true if cache is disabled (cache is null). true otherwise.
	 */
	public boolean isDisabled()
	{
		return this.cacheRegion == null;
	}

	/**
	 * Copy all the values from the cache entries to the data object.
	 *
	 * @param e
	 *            cache entry
	 * @param data
	 *            data object (dto)
	 */
	public void copyFromCache(final Object[] e, final Object data)
	{
		map(e, data);
	}

	/**
	 * setter
	 *
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final List<String> properties)
	{
		this.properties.clear();
		for (final String prop : properties)
		{
			this.properties.add(prop.trim());
		}
	}

	/**
	 * getter
	 * 
	 * @return the properties
	 */
	public List<String> getProperties()
	{
		return properties;
	}

	private static String firstLowerCase(String replaceFirst)
	{
		char c[] = replaceFirst.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return String.valueOf(c);
	}

	private Map<Class<? extends Object>, Map<String, Method>> getterMappings = new HashMap<>();

	private Map<String, Method> getCachedGettersMap(Object dataObject)
	{
		Class<? extends Object> dataObjectClass = dataObject.getClass();

		Map<String, Method> cachedGettersMap = getterMappings.get(dataObjectClass);
		if (cachedGettersMap != null)
			return cachedGettersMap;

		Map<String, Method> gettersMap = new HashMap<>();
		// Parsing the methods of the dataObject
		Method[] methods = dataObjectClass.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			// This isn't exact, but is ok.
			// We could just end up with too much getters such as dto.myMethod() -> "myMethod" as a property.
			if (methodName.startsWith("set"))
				continue;

			String propertyName = firstLowerCase(methodName.replaceFirst("^get", "").replaceFirst("^is", ""));

			method.setAccessible(true); // Avoid the speed penalty of Reflection doing security checks on every
										// invocation
			gettersMap.put(propertyName, method);
		}

		getterMappings.put(dataObjectClass, gettersMap);
		return gettersMap;
	}

	/**
	 * 
	 * @param dataObject
	 * @param e
	 */
	private void map(Object dataObject, Object[] e)
	{		
		Map<String, Method> gettersMap = getCachedGettersMap(dataObject);

		for (int i = 0; i < properties.size(); i++)
		{
			String propertyName = properties.get(i);

			Method method = gettersMap.get(propertyName);

			try
			{
				Object value = method.invoke(dataObject);
				e[i] = value;
			} catch (Exception ex)
			{
				if (!isAutoWired) {
					LOG.error("Property '" + propertyName + "' was not found found for populator '" + populatorClassName + "' - please check the configuration");
				} else {
					LOG.debug("Failed to resolve property '" + propertyName + "' on populator " + this.populatorClassName + ". Is the DTO class ");
				}
			}
		}
	}

	private Map<Class<? extends Object>, Map<String, Method>> setterMappings = new HashMap<>();

	private Map<String, Method> getCachedSettersMap(Object dataObject)
	{
		Class<? extends Object> dataObjectClass = dataObject.getClass();

		Map<String, Method> cachedMap = setterMappings.get(dataObjectClass);
		if (cachedMap != null)
			return cachedMap;

		Map<String, Method> settersMap = new HashMap<>();
		// Parsing the methods of the dataObject
		Method[] methods = dataObjectClass.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			String methodName = method.getName();

			if (!methodName.startsWith("set"))
				continue;

			String propertyName = firstLowerCase(methodName.replaceFirst("^set", ""));

			method.setAccessible(true); // Avoid the speed penalty of Reflection doing security checks on every
										// invocation
			settersMap.put(propertyName, method);
		}

		setterMappings.put(dataObjectClass, settersMap);
		return settersMap;
	}

	private void map(Object[] e, Object dataObject)
	{
		Map<String, Method> settersMap = getCachedSettersMap(dataObject);

		for (int i = 0; i < properties.size(); i++)
		{
			String propertyName = properties.get(i);

			Object value = e[i];
			if (value == null) {
				continue;
			}
			Method method = settersMap.get(propertyName);

			try
			{
				method.invoke(dataObject, value);
			} catch (Exception ex)
			{
				throw new RuntimeException(ex);
			}

		}
	}

	public EHCacheRegion getCacheRegion()
	{
		return cacheRegion;
	}

	public void setCacheRegion(EHCacheRegion cacheRegion)
	{
		this.cacheRegion = cacheRegion;
	}

	public String getPopulatorClassName()
	{
		return populatorClassName;
	}

	public boolean isInvalidate()
	{
		return invalidate;
	}

	public void setInvalidate(boolean invalidate)
	{
		this.invalidate = invalidate;
	}

	public boolean isAutoWired()
	{
		return isAutoWired;
	}

	public void setAutoWired(boolean isAutoWired)
	{
		this.isAutoWired = isAutoWired;
	}

	@Override
	public String toString()
	{
		return "CacheDescriptor [populatorClassName=" + populatorClassName + ", invalidate=" + invalidate
				+ ", cacheRegion=" + cacheRegion + ", isAutoWired=" + isAutoWired + ", properties=" + properties + "]";
	}

	public KeyStrategy getKeyStrategy()
	{
		return keyStrategy;
	}

	public void setKeyStrategy(KeyStrategy keyStrategy)
	{
		this.keyStrategy = keyStrategy;
	}

}
