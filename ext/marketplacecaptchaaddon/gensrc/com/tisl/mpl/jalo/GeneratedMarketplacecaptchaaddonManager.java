/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Feb 26, 2016 8:23:46 PM                     ---
 * ----------------------------------------------------------------
 */
package com.tisl.mpl.jalo;

import com.tisl.mpl.constants.MarketplacecaptchaaddonConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.store.BaseStore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>MarketplacecaptchaaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMarketplacecaptchaaddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("captchaCheckEnabled", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.store.BaseStore", Collections.unmodifiableMap(tmp));
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.captchaCheckEnabled</code> attribute.
	 * @return the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public Boolean isCaptchaCheckEnabled(final SessionContext ctx, final BaseStore item)
	{
		return (Boolean)item.getProperty( ctx, MarketplacecaptchaaddonConstants.Attributes.BaseStore.CAPTCHACHECKENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.captchaCheckEnabled</code> attribute.
	 * @return the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public Boolean isCaptchaCheckEnabled(final BaseStore item)
	{
		return isCaptchaCheckEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @return the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public boolean isCaptchaCheckEnabledAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Boolean value = isCaptchaCheckEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @return the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public boolean isCaptchaCheckEnabledAsPrimitive(final BaseStore item)
	{
		return isCaptchaCheckEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @param value the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public void setCaptchaCheckEnabled(final SessionContext ctx, final BaseStore item, final Boolean value)
	{
		item.setProperty(ctx, MarketplacecaptchaaddonConstants.Attributes.BaseStore.CAPTCHACHECKENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @param value the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public void setCaptchaCheckEnabled(final BaseStore item, final Boolean value)
	{
		setCaptchaCheckEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @param value the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public void setCaptchaCheckEnabled(final SessionContext ctx, final BaseStore item, final boolean value)
	{
		setCaptchaCheckEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.captchaCheckEnabled</code> attribute. 
	 * @param value the captchaCheckEnabled - Determines whether the site should use captcha during registration.
	 */
	public void setCaptchaCheckEnabled(final BaseStore item, final boolean value)
	{
		setCaptchaCheckEnabled( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return MarketplacecaptchaaddonConstants.EXTENSIONNAME;
	}
	
}
