/**
 *
 */
package com.tisl.mpl.webservice.businessvalidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.DomainValidator;


/**
 * @author TCS
 *
 */
public class DefaultCommonAsciiValidator
{

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	/**
	 * @description method is called to update string values with space removing numeric values
	 * @param Str
	 * @return boolean
	 */
	public static boolean validateAlphaWithSpaceNoSpCh(final String Str)
	{


		boolean flag = false;
		char s;
		if (Str.length() > 0)
		{
			for (int i = 0; i < Str.length(); i++)
			{
				s = Str.charAt(i);
				final char ch = s;
				if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122) || (ch == 32))
				{
					flag = true;
				}
				else
				{
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * @description method is called to validate EmailAddress
	 * @param emailid
	 * @return boolean
	 */
	public static boolean validateEmailAddress(final String emailid)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(emailid);
		return matcher.matches();
	}

	/**
	 * @description method is called to validate EmailAddress
	 * @param emailid
	 * @return boolean
	 */
	public static boolean validDomain(final String emailid)
	{
		final DomainValidator domainvalidator = DomainValidator.getInstance();
		final int index = emailid.indexOf('@');
		final String substringEmail = emailid.substring(index + 1);
		final boolean valid = domainvalidator.isValid(substringEmail);
		return valid;
	}



}
