package com.tisl.mpl.sns.push.tools;

/*
 * TCS
 */
public class MplSNSStringUtils
{

	/**
	 * check the string is empty
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isEmpty(final String s)
	{
		if (s == null)
		{
			return true;
		}

		if (s.length() < 1)
		{
			return true;
		}

		return false;
	}

	/**
	 * check the string is blank
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isBlank(final String s)
	{
		if (isEmpty(s))
		{
			return true;
		}

		if (isEmpty(s.trim()))
		{
			return true;
		}

		return false;
	}
}
