/**
 *
 */
package com.tisl.mpl.storefront.security.cookie;

import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author 765463
 *
 */
public class UserCookieGenerator extends EnhancedCookieGenerator
{
	private BaseSiteService baseSiteService;

	@Override
	public String getCookieName()
	{
		return StringUtils.deleteWhitespace(getBaseSiteService().getCurrentBaseSite().getUid()) + "-user";
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

}
