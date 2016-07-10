/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;

import javax.annotation.Resource;

import com.tisl.mpl.facade.brand.BrandFacade;


/**
 * @author Arunkumar Selvam
 *
 *         Loads the brands from cache loader using cache key @see BrandComponentCacheKey
 *
 */
public class BrandComponentCacheValueLoader implements CacheValueLoader
{

	@Resource(name = "brandFacade")
	private BrandFacade brandFacade;

	@Override
	public Object load(final CacheKey key) throws CacheValueLoadException
	{
		if (key instanceof BrandComponentCacheKey)
		{
			final String categoryCode = ((BrandComponentCacheKey) key).getCategoryCode();
			return brandFacade.getAllBrandsInAplhabeticalOrder(categoryCode);
		}

		return null;
	}

}
