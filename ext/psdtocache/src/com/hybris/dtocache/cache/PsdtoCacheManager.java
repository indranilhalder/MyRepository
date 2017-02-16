/**
 * 
 */
package com.hybris.dtocache.cache;

import de.hybris.platform.regioncache.region.impl.EHCacheRegion;

/**
 * 
 * @author giovanni.ganassin@sap.com
 * Jan 19, 2016 11:52:36 AM
 *
 */
public interface PsdtoCacheManager
{

	public EHCacheRegion get(String name);

}
