/**
 *
 */
package com.tisl.mpl.storefront.security.cookie;



/**
 * @author TCS
 *
 */
public class LuxuryEmailCookieGenerator extends EnhancedCookieGenerator
{

	@Override
	public String getCookieName()
	{
		return "luxmpl-email";
	}
}
