package com.tisl.mpl.storefront.security.cookie;

import javax.servlet.http.Cookie;


/**
 * @author TCS
 *
 */
public class PDPPincodeCookieGenerator extends EnhancedCookieGenerator
{

	@Override
	protected void setEnhancedCookie(final Cookie cookie)
	{
		//Override the Existing method in EnhancedCookieGenerator with Empty body, to set the MaxAge which is passed from spring-security-config.xml
	}
}
