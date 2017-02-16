/**
 * 
 */
package com.hybris.dtocache.cache.key.impl;

import org.apache.commons.lang.StringUtils;

import com.hybris.dtocache.cache.CacheDescriptor;
import com.hybris.dtocache.cache.key.CacheKey;
import com.hybris.dtocache.cache.key.KeyStrategy;
import com.hybris.dtocache.exception.DtoCacheRuntimeException;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;

/**
 * 
 * @author giovanni.ganassin@sap.com
 * Jan 16, 2016 7:19:43 PM
 *
 * This implementations avoid the last modification timestamp as part of the key.
 * It is used when a {@link CacheDescriptor} is marked as invalidate=true.
 * When using this cache key, hAC > monitoring > cache will show the invalidations properly.
 */
public class InvalidableKeyStrategy extends KeyStrategy
{

	public CacheKey buildKey(String populatorClassName, PK pk, ItemModel itemModel) {
		final PK _pk = itemModel == null ? pk : itemModel.getPk() == null? pk : itemModel.getPk();
		if (StringUtils.isEmpty(populatorClassName) || _pk == null) {
			throw new DtoCacheRuntimeException("populatorClassName and one between pk and itemModel cannot be null.");
		}
		final String key = populatorClassName + "/" + pk.getLongValueAsString();
		return new CacheKey(_pk.getTypeCodeAsString(), key);
	}

	@Override
	public String toString()
	{
		return "InvalidableKeyStrategy []";
	}

}
