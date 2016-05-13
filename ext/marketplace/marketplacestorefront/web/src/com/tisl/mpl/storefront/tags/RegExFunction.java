/**
 *
 */
package com.tisl.mpl.storefront.tags;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author TCS
 *
 */
public class RegExFunction
{
	/**
	 * @desc This function matches agaist a regular expression validationg to true or false
	 * @param toMatch
	 * @param expression
	 * @return boolean
	 */
	public static boolean regExMatch(final String toMatch, final String expression)
	{
		boolean flag = false;
		final Pattern p = Pattern.compile(expression);
		final Matcher m = p.matcher(toMatch);
		if (m.find())
		{
			flag = true;
		}
		//SONAR fix
		//else
		//{
		//	return false;
		//}
		return flag;
	}

	/**
	 * @desc This function matches agaist a regular expression validationg to true or false
	 * @param toMatch
	 * @param expression
	 * @return boolean
	 */
	public static String regExMatchAndRemove(final String toMatch, final String expression)
	{
		final String removedString = toMatch;
		final Pattern p = Pattern.compile(expression);
		final Matcher m = p.matcher(toMatch);
		try
		{
			if (m.find())
			{
				return toMatch.substring(0, (m.end() - 1));
			}
		}
		catch (final IllegalStateException e)
		{
			return removedString;
		}

		return removedString;
	}
}
