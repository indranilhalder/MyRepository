/**
 * 
 */
package com.hybris.oms.tata.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Pradeep
 * 
 */

public class PhoneNumberValidation
{
	private final Pattern patternPhone;
	private final Pattern patternMobile;


	private static final String MOBILE_PATTERN = "\\d{11}";
	private static final String PHONE_PATTERN = "\\d{10}";

	public PhoneNumberValidation()
	{
		patternPhone = Pattern.compile(PHONE_PATTERN);
		patternMobile = Pattern.compile(MOBILE_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *           hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex)
	{
		final Matcher matcherPhone = patternPhone.matcher(hex);
		final Matcher matcherMobile = patternMobile.matcher(hex);
		return (matcherPhone.matches() || matcherMobile.matches());
	}
}
