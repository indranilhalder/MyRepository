/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Arunkumar Selvam
 *
 *         CacheKey for BrandComponent
 *
 */
public class BrandComponentCacheKey implements CacheKey
{

	private String categoryCode;

	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode()
	{
		return categoryCode;
	}

	public BrandComponentCacheKey(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.regioncache.key.CacheKey#getCacheValueType()
	 */
	@Override
	public CacheUnitValueType getCacheValueType()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.regioncache.key.CacheKey#getTenantId()
	 */
	@Override
	public String getTenantId()
	{
		return Registry.getCurrentTenant().getTenantID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.regioncache.key.CacheKey#getTypeCode()
	 */
	@Override
	public Object getTypeCode()
	{
		return null;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
		{
			return true;
		}
		if (obj.getClass() != getClass())
		{
			return false;
		}
		final BrandComponentCacheKey key = (BrandComponentCacheKey) obj;
		return new EqualsBuilder().append(categoryCode, key.categoryCode).isEquals();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 31).append(categoryCode).toHashCode();
	}

}
