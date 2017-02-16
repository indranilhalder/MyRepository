package com.hybris.dtocache.cache.key.impl;

import org.apache.log4j.Logger;

import com.hybris.dtocache.cache.key.CacheKey;
import com.hybris.dtocache.cache.key.KeyStrategy;
import com.hybris.dtocache.exception.DtoCacheRuntimeException;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;

/**
 * 
 * This default implementation uses the last modification timestamp as part of the cache key.
 * in this way - no matter what happens - the front end will never show something outdated.
 *
 * @author giovanni.ganassin@sap.com
 * Jan 16, 2016 7:19:49 PM
 * 
 */
public class TimestampKeyStrategy extends KeyStrategy
{
	private static Logger LOG = Logger.getLogger(TimestampKeyStrategy.class);

	/**
	 * {@link PK} parameter is optional in this case - {@link ItemModel} is not!
	 */
	@Override
	public CacheKey buildKey(String populatorClassName, PK pk, ItemModel itemModel)
	{
		String t = itemModel.getModifiedtime() == null ? "" : itemModel.getModifiedtime().getTime() + "";
		t = populatorClassName + "/" + pk.getLongValueAsString() + "-" + t;
		if (itemModel == null || itemModel.getModifiedtime() == null) {
			throw new DtoCacheRuntimeException("cannot use TimestampKeyStrategy with null ItemModel or with a null modified timestamp. Please check the configuration of the populator '" + populatorClassName + "'.");
		}
		final CacheKey key = new CacheKey(itemModel.getPk().getTypeCodeAsString(), t);
		if (LOG.isDebugEnabled()) {
			LOG.debug("cache key built: " + key.toString());
		}
		return key;
	}

}