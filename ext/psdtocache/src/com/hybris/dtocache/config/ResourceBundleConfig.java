/**
 *
 */
package com.hybris.dtocache.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author i309277
 *
 */
public class ResourceBundleConfig extends Config
{
	private static ResourceBundle resourceBundle;

	public ResourceBundleConfig(final String bundle)
	{
		resourceBundle = ResourceBundle.getBundle(bundle);
		Config.me = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.dtocache.config.Config#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(final String key, final int def)
	{
		try
		{
			final String value = resourceBundle.getString(key);
			return Integer.parseInt(value);
		} catch (final MissingResourceException e)
		{
			return def;
		}

	}

	@Override
	public String getString(final String key, final String def)
	{
		try
		{
			return resourceBundle.getString(key);
		} catch (final MissingResourceException e)
		{
			return def;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.dtocache.config.Config#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(final String key, final boolean def)
	{
		try
		{
			return Boolean.valueOf(resourceBundle.getString(key)).booleanValue();
		} catch (final MissingResourceException e)
		{
			return def;
		}
	}

}
