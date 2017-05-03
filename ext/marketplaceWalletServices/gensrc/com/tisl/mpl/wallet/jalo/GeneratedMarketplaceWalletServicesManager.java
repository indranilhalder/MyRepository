/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Apr 26, 2017 8:25:21 PM                     ---
 * ----------------------------------------------------------------
 */
package com.tisl.mpl.wallet.jalo;

import com.tisl.mpl.wallet.constants.MarketplaceWalletServicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.extension.Extension;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>MarketplaceWalletServicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMarketplaceWalletServicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	@Override
	public String getName()
	{
		return MarketplaceWalletServicesConstants.EXTENSIONNAME;
	}
	
}
