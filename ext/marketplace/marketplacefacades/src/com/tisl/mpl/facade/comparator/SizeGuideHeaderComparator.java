/**
 *
 */
package com.tisl.mpl.facade.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class SizeGuideHeaderComparator implements Comparator<String>
{
	private String pattern;
	private Pattern regexPattern;
	private List<List<String>> sizeSystems;

	private static final Logger LOG = Logger.getLogger(SizeGuideHeaderComparator.class);

	/**
	 * This method is responsible for sizes to be displayed in size chart
	 *
	 *
	 * @param sizeData1
	 * @param sizeData2
	 *
	 */

	@Override
	public int compare(final String arg0, final String arg1)
	{
		final String value1 = arg0.replaceAll("\\s+", "");
		final String value2 = arg1.replaceAll("\\s+", "");
		if (value1 == null || value2 == null)
		{
			return 0;
		}
		//try numeric compare - if doesn't work then try size system
		final boolean value1IsNumber = isNumber(value1);
		final boolean value2IsNumber = isNumber(value2);
		if (value1IsNumber && value2IsNumber)
		{
			return numericCompare(value1, value2);
		}
		//try compare within each size system
		for (final List<String> sizeSystem : sizeSystems)
		{
			if (sizeSystem.contains(value1) && sizeSystem.contains(value2))
			{
				final int index1 = sizeSystem.indexOf(value1);
				final int index2 = sizeSystem.indexOf(value2);
				return index1 - index2;
			}
		}
		//values may come from distinct size systems - where one is numeric
		final int value1SizeSystemIndex = getSizeSystemIndex(value1);
		final int value2SizeSystemIndex = getSizeSystemIndex(value2);
		if (value1IsNumber)
		{
			if (value2SizeSystemIndex == -1)
			{
				//indicates numeric values comes first
				//and values out of size-systems are placed as last thus so big number.
				return Integer.MIN_VALUE;
			}
			else
			{
				//this will distribute other systems
				return -(value2SizeSystemIndex + 1) * 100 + sizeSystems.get(value2SizeSystemIndex).indexOf(value2);
			}
		}
		else if (value2IsNumber)
		{
			if (value1SizeSystemIndex == -1)
			{
				//indicates numeric values comes first
				//and values out of size-systems are placed as last thus so big number.
				return Integer.MAX_VALUE;
			}
			else
			{
				//this will distribute other systems
				return (value1SizeSystemIndex + 1) * 100 + sizeSystems.get(value1SizeSystemIndex).indexOf(value1);
			}
		}
		//values may come from distinct size systems - where both are not numeric
		else if (value1SizeSystemIndex != -1)
		{
			if (value2SizeSystemIndex == -1)
			{
				//values out of size-systems are placed as last thus so big number.
				return Integer.MIN_VALUE;
			}
			else
			{
				final int val1Index = (value1SizeSystemIndex + 1) * 100 + sizeSystems.get(value1SizeSystemIndex).indexOf(value1);
				final int val2Index = (value2SizeSystemIndex + 1) * 100 + sizeSystems.get(value2SizeSystemIndex).indexOf(value2);
				return val1Index - val2Index;
			}
		}
		else if (value2SizeSystemIndex != -1)
		{
			final double modifiedValue1 = Double.parseDouble(value1.replaceAll("\\D+", ""));
			final double modifiedValue2 = Double.parseDouble(value2.replaceAll("\\D+", ""));
			//values out of size-systems are placed as last thus so big number.
			return Double.compare(modifiedValue1, modifiedValue2);
		}
		else if (value1SizeSystemIndex == -1 && value2SizeSystemIndex == -1)
		{
			return alphaNumericCompare(value1, value2);
		}
		//no luck - assume values are equal
		return 0;
	}

	protected boolean isNumber(final String value)
	{
		return getRegexPattern().matcher(value).find();
	}

	/**
	 * @param value
	 *           to search
	 * @return index of first size-system containing this value, if not found -1 is returned
	 */
	protected int getSizeSystemIndex(final String value)
	{
		for (final List<String> sizeSystem : sizeSystems)
		{
			if (sizeSystem.contains(value))
			{
				return sizeSystems.indexOf(sizeSystem);
			}
		}
		return -1;//indicates that value doesn't exist in any size-system
	}

	/**
	 * Compares two numbers
	 *
	 * @param value1
	 * @param value2
	 */
	protected int numericCompare(final String value1, final String value2)
	{
		/* Changes for TISPRO-250 */
		int retValue = 0;
		final double number1 = getNumber(value1);
		final double number2 = getNumber(value2);
		retValue = Double.compare(number1, number2);
		return retValue;
	}


	/**
	 * Compares two alphaNumeric string
	 *
	 * @param value1
	 * @param value2
	 */
	/* Changes for TISPRO-250 */
	protected int alphaNumericCompare(final String firstString, final String secondString)
	{
		if (secondString == null || firstString == null)
		{
			return 0;
		}

		final int lengthFirstStr = firstString.length();
		final int lengthSecondStr = secondString.length();

		int index1 = 0;
		int index2 = 0;

		while (index1 < lengthFirstStr && index2 < lengthSecondStr)
		{
			char ch1 = firstString.charAt(index1);
			char ch2 = secondString.charAt(index2);

			final char[] space1 = new char[lengthFirstStr];
			final char[] space2 = new char[lengthSecondStr];

			int loc1 = 0;
			int loc2 = 0;

			do
			{
				space1[loc1++] = ch1;
				index1++;

				if (index1 < lengthFirstStr)
				{
					ch1 = firstString.charAt(index1);
				}
				else
				{
					break;
				}
			}
			while (Character.isDigit(ch1) == Character.isDigit(space1[0]));

			do
			{
				space2[loc2++] = ch2;
				index2++;

				if (index2 < lengthSecondStr)
				{
					ch2 = secondString.charAt(index2);
				}
				else
				{
					break;
				}
			}
			while (Character.isDigit(ch2) == Character.isDigit(space2[0]));

			final String str1 = new String(space1);
			final String str2 = new String(space2);

			int result;

			if (Character.isDigit(space1[0]) && Character.isDigit(space2[0]))
			{
				final Integer firstNumberToCompare = Integer.valueOf(Integer.parseInt(str1.trim()));
				//new Integer(Integer.parseInt(str1.trim()));
				final Integer secondNumberToCompare = Integer.valueOf(Integer.parseInt(str2.trim()));//;new Integer(Integer.parseInt(str2.trim()));
				result = firstNumberToCompare.compareTo(secondNumberToCompare);
			}
			else
			{
				result = str1.compareTo(str2);
			}

			if (result != 0)
			{
				return result;
			}
		}
		return lengthFirstStr - lengthSecondStr;
	}

	/**
	 * @param value
	 * @return first double value (depends on pattern) found in given string
	 */
	protected double getNumber(final String value)
	{
		final Matcher matcher = getRegexPattern().matcher(value);
		if (matcher.find())
		{
			return Double.parseDouble(matcher.group());
		}
		return Integer.MAX_VALUE;//let unknown 'numbers' to end up at the end
	}

	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
		this.regexPattern = Pattern.compile(pattern);
	}

	protected Pattern getRegexPattern()
	{
		return regexPattern;
	}

	protected List<List<String>> getSizeSystems()
	{
		return sizeSystems;
	}

	@Required
	public void setSizeSystems(final List<List<String>> sizeSystems)
	{
		this.sizeSystems = sizeSystems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */

}
