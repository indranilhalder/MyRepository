/**
 * 
 */
package com.hybris.dtocache.cache.key;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;

/**
 * @author giovanni.ganassin@sap.com
 * Jan 23, 2016 10:59:34 AM
 *
 */
public abstract class KeyStrategy
{
	public abstract CacheKey buildKey(String populatorClassName, PK pk, ItemModel itemModel);
}
