/**
 *
 */
package com.hybris.dtocache.config;

/**
 * @author i309277
 *
 */
public class HybrisConfig extends Config
{
	public HybrisConfig()
	{
		Config.me = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.dtocache.config.Config#getString(java.lang.String)
	 */
	@Override
	public String getString(final String key, final String def)
	{
		return de.hybris.platform.util.Config.getString(key, def);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.dtocache.config.Config#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String key, final int def)
	{
		return de.hybris.platform.util.Config.getInt(key, def);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.dtocache.config.Config#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(final String key, final boolean def)
	{
		// YTODO Auto-generated method stub
		return de.hybris.platform.util.Config.getBoolean(key, def);
	}

}
