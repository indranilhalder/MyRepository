/**
 * [y] hybris Platform Copyright
 * (c) 2000-2015 hybris AG All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.hybris.dtocache.config;

/**
 * Configuration abstraction (not directly hybris related). The goal of this class is to be able to unit test the
 * extension without any tight coupling with hybris implementation. Basically, thanks to that abstraction, unit tests do
 * not need any "tenant activated".
 *
 * @author Jose Carreno
 *
 */
public abstract class Config
{

	public final static String SUFFIX = "psdtocache.";

	public final static String PARAMS_FIELDS = "fields";
	public final static String PARAMS_KEY_STRATEGY = "keyStrategy";
	public final static String PARAMS_INVALIDATE = "invalidate";
	public final static String PARAMS_SCAN_PACKAGE = "analyzer.scanPackage";
	public final static String PARAMS_SCAN_MODE = "analyzer.mode";
	public final static String PARAMS_POPULATE_ITEMMODEL = "itemModel";
	public final static String PARAMS_POPULATE_DTO = "dto";

	public final static String VALUES_NO_CACHE = "no-cache";
	public final static String VALUES_DEFAULT = "default";
	public final static boolean VALUES_INVALIDATE_DEFAULT = false;
	public final static String VALUES_SCAN_MODE_STRICT = "strict";
	public final static String VALUES_SCAN_MODE_SILENT = "silent";
	
	public final static String VALUES_DEFAULT_KEY_STRATEGY = "defaultDtocacheKeyStrategy";
	public final static String VALUES_DEFAULT_INVALIDABLE_KEY_STRATEGY = "defaultDtocacheInvalidableKeyStrategy";

	/**
	 * Actual config implementation.
	 */
	protected static Config me = null;

	/**
	 * get string value.
	 *
	 * @param key
	 *            property key
	 * @param def
	 *            default value
	 * @return string value
	 */
	public String getString(final String key, final String def)
	{
		return Config.get().getString(key, def);
	}

	/**
	 * get int value.
	 *
	 * @param key
	 *            property key
	 * @param def
	 *            default value
	 * @return int value
	 */
	public int getInt(final String key, final int def)
	{
		return Config.get().getInt(key, def);
	}

	/**
	 * get boolean value.
	 *
	 * @param key
	 *            property key
	 * @param def
	 *            default value
	 * @return boolean value
	 */
	public boolean getBoolean(final String key, final boolean def)
	{
		return Config.get().getBoolean(key, def);
	}

	/**
	 * getter for config instance
	 * 
	 * @return config actual instance
	 */
	public static Config get()
	{
		if (me == null)
		{
			new HybrisConfig();
		}
		return me;
	}

}
