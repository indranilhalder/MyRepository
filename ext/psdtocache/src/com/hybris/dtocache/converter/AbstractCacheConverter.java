/**
 *
 */
package com.hybris.dtocache.converter;

import org.apache.log4j.Logger;

import com.hybris.dtocache.cache.key.KeyStrategy;
import com.hybris.dtocache.cache.key.impl.InvalidableKeyStrategy;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.impl.EHCacheRegion;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * @author jose.carreno
 *
 */
public abstract class AbstractCacheConverter<SOURCE, TARGET> extends AbstractPopulatingConverter<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(AbstractCacheConverter.class);
	
	private EHCacheRegion cacheRegion;

	private boolean cacheEnabled = false;
	
	private KeyStrategy keyStrategy = new InvalidableKeyStrategy();

	@Override
	public TARGET convert(final SOURCE source) throws ConversionException
	{
		if (isCacheEnabled())
		{
			return cacheConvert(source);
		}
		else
		{
			return doConvert(source);
		}
	}

	private TARGET doConvert(final SOURCE source) throws ConversionException
	{
		final TARGET target = createTarget();
		populate(source, target);
		return target;
	}

	@SuppressWarnings("unchecked")
	private TARGET cacheConvert(final SOURCE source) throws ConversionException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cache converting : " + source);
		}
		final ItemModel itemModel = (ItemModel) source;
		final TARGET target = (TARGET) cacheRegion.getWithLoader(keyStrategy.buildKey(this.getClass().getName(), itemModel.getPk(), itemModel), 
				new CacheValueLoader<Object[]>()
				{

					@Override
					public Object[] load(CacheKey paramCacheKey) throws CacheValueLoadException
					{
						final TARGET target = doConvert(source);
						return new Object[]{target};
					}
				});
		return target;
	}

	public EHCacheRegion getCacheRegion()
	{
		return cacheRegion;
	}

	public void setCacheRegion(EHCacheRegion cacheRegion)
	{
		this.cacheRegion = cacheRegion;
	}

	public boolean isCacheEnabled()
	{
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled)
	{
		this.cacheEnabled = cacheEnabled;
	}


}