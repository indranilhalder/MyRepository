/**
 *
 */
package com.tisl.mpl.util;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author TCS
 *
 */
public class MplVariantComparator implements Comparator<PcmProductVariantModel>
{

	private String pattern;
	private Pattern regexPattern;
	private List<List<String>> sizeSystems;

	private String variantType = null;

	//this is used to compare the sizes of different variants

	@Override
	public int compare(final PcmProductVariantModel variantProductModel1, final PcmProductVariantModel variantProductModel2)
	{
		String value0 = null;
		String value1 = null;

		if (variantType != null && variantType.equalsIgnoreCase("size"))
		{
			value0 = variantProductModel1.getSize();
			value1 = variantProductModel2.getSize();
			return getSizeCompareValue(value0, value1);
		}
		if (variantType != null && variantType.equalsIgnoreCase("capacity"))
		{
			value0 = variantProductModel1.getCapacity();
			value1 = variantProductModel2.getCapacity();
			return getCapacityCompareValue(value0, value1);
		}
		return 0;
	}

	public int getCapacityCompareValue(final String value0, final String value1)
	{
		//try numeric compare - if doesn't work then try size system
		final boolean value1IsNumber = isNumber(value0);
		final boolean value2IsNumber = isNumber(value1);
		if (value1IsNumber && value2IsNumber)
		{
			return numericCompare(value0, value1);
		}
		else
		{
			final double modifiedValue1 = Double.parseDouble(value0.replaceAll(MplConstants.DOUBLE, ""));
			final double modifiedValue2 = Double.parseDouble(value1.replaceAll(MplConstants.DOUBLE, ""));
			//values out of size-systems are placed as last thus so big number.
			return Double.compare(modifiedValue1, modifiedValue2);
		}
		//no luck - assume values are equal
	}

	public int getSizeCompareValue(final String value0, final String value1)
	{
		if (value0 == null || value1 == null)
		{
			return 0;
		}
		//try numeric compare - if doesn't work then try size system
		final boolean value1IsNumber = isNumber(value0);
		final boolean value2IsNumber = isNumber(value1);
		if (value1IsNumber && value2IsNumber)
		{
			return numericCompare(value0, value1);
		}
		//try compare within each size system
		for (final List<String> sizeSystem : sizeSystems)
		{
			if (sizeSystem.contains(value0) && sizeSystem.contains(value1))
			{
				final int index1 = sizeSystem.indexOf(value0);
				final int index2 = sizeSystem.indexOf(value1);
				return index1 - index2;
			}
		}
		//values may come from distinct size systems - where one is numeric
		final int value1SizeSystemIndex = getSizeSystemIndex(value0);
		final int value2SizeSystemIndex = getSizeSystemIndex(value1);
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
				return -(value2SizeSystemIndex + 1) * 100 + sizeSystems.get(value2SizeSystemIndex).indexOf(value1);
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
				return (value1SizeSystemIndex + 1) * 100 + sizeSystems.get(value1SizeSystemIndex).indexOf(value0);
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
				final int val1Index = (value1SizeSystemIndex + 1) * 100 + sizeSystems.get(value1SizeSystemIndex).indexOf(value0);
				final int val2Index = (value2SizeSystemIndex + 1) * 100 + sizeSystems.get(value2SizeSystemIndex).indexOf(value1);
				return val1Index - val2Index;
			}
		}
		else if (value2SizeSystemIndex != -1)
		{
			final double modifiedValue1 = Double.parseDouble(value0.replaceAll(MplConstants.DOUBLE, ""));
			final double modifiedValue2 = Double.parseDouble(value1.replaceAll(MplConstants.DOUBLE, ""));
			//values out of size-systems are placed as last thus so big number.
			return Double.compare(modifiedValue1, modifiedValue2);
		}
		//no luck - assume values are equal
		return 0;
	}

	/**
	 * @param value
	 *           to search number in.
	 * @return true if number can be found within given string
	 */
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

	/**
	 * @return the variantType
	 */
	public String getVariantType()
	{
		return variantType;
	}

	/**
	 * @param variantType
	 *           the variantType to set
	 */
	public void setVariantType(final String variantType)
	{
		this.variantType = variantType;
	}

}
