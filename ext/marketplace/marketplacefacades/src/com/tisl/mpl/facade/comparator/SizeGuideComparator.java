/**
 *
 */
package com.tisl.mpl.facade.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.facades.product.data.SizeGuideData;


/**
 * @author TCS
 *
 */
public class SizeGuideComparator implements Comparator<SizeGuideData>
{

	private String pattern;
	private Pattern regexPattern;
	private List<List<String>> sizeSystems;

	/**
	 * This method is responsible for sizes to be displayed in size chartok
	 *
	 *
	 * @param sizeData1
	 * @param sizeData2
	 *
	 */
	@Override
	public int compare(final SizeGuideData sizeData1, final SizeGuideData sizeData2)
	{
		final String value1 = sizeData1.getDimensionSize().replaceAll("\\s+", "").toUpperCase();
		final String value2 = sizeData2.getDimensionSize().replaceAll("\\s+", "").toUpperCase();
		System.out.println("*********************sizeguide" + value1 + value2);

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
		final double number1 = getNumber(value1);
		final double number2 = getNumber(value2);
		return Double.compare(number1, number2);
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
}
