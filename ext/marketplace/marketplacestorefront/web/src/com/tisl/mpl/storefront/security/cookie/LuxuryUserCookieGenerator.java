/**
 *
 */
package com.tisl.mpl.storefront.security.cookie;

/**
 * @author TCS
 *
 */
public class LuxuryUserCookieGenerator extends EnhancedCookieGenerator
{

	@Override
	public String getCookieName()
	{
		return "luxmpl-user";
	}
}
