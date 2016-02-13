package com.tisl.mpl.storefront.businessvalidator;

/**
 * @author TCS
 *
 */
public class CommonAsciiValidator
{
	//	ASCII Code		Characters

	//	32					Space
	//	33-47				Special Characters
	//	48-57				0-9
	//	58-64				Special Characters
	//	65-90				A-Z
	//	91-96				Special Characters
	//	97-122			a-z

	/**
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
	 * @param Str
	 * @return boolean
	 */
	public static boolean validateNumericWithoutSpace(final String Str)
	{
		boolean flag = false;
		char s;
		if (Str.length() > 0)
		{
			for (int i = 0; i < Str.length(); i++)
			{
				s = Str.charAt(i);
				final char ch = s;
				if ((ch >= 48 && ch <= 57))
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
	 * @param Str
	 * @return boolean
	 */
	public static boolean validatePassword(final String Str)
	{
		//	Password format : atleast one UPPER, one LOWER, one numeric, one special char
		boolean flag = false;
		boolean upperAlpha = false;
		boolean lowerAlpha = false;
		boolean numeric = false;
		boolean spChar = false;
		char s;
		if (Str.length() > 0)
		{
			for (int i = 0; i < Str.length(); i++)
			{
				s = Str.charAt(i);
				final char ch = s;
				if (ch >= 65 && ch <= 90)
				{
					upperAlpha = true;
				}
				else if (ch >= 97 && ch <= 122)
				{
					lowerAlpha = true;
				}
				else if (ch >= 48 && ch <= 57)
				{
					numeric = true;
				}
				else if ((ch >= 33 && ch <= 47) || (ch >= 58 && ch <= 64) || (ch >= 91 && ch <= 96) || (ch == 126))
				{
					spChar = true;
				}
			}
			if (upperAlpha && lowerAlpha && numeric && spChar)
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		return flag;
	}
}
