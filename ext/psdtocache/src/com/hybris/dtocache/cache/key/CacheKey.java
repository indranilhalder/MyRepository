package com.hybris.dtocache.cache.key;

import de.hybris.platform.core.Registry;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

/**
 * 
 * Used as key on Hybris cache regions.
 * 
 * @author giovanni.ganassin@sap.com
 * Jan 16, 2016 7:19:34 PM
 *
 */
public final class CacheKey implements de.hybris.platform.regioncache.key.CacheKey
{

	private String typeCode;
	private String key;

	public CacheKey (String typeCode, String key) {
		this.typeCode = typeCode;
		this.key = key;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.regioncache.key.CacheKey#getCacheValueType()
	 */
	@Override
	public CacheUnitValueType getCacheValueType()
	{
		return CacheUnitValueType.SERIALIZABLE;
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
		return this.typeCode;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((typeCode == null) ? 0 : typeCode.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheKey other = (CacheKey) obj;
		if (key == null)
		{
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (typeCode == null)
		{
			if (other.typeCode != null)
				return false;
		} else if (!typeCode.equals(other.typeCode))
			return false;
		return true;
	}


	@Override
	public String toString()
	{
		return "CacheKey [typeCode=" + typeCode + ", key=" + key + "]";
	}

}
