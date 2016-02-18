/**
 *
 */
package com.tisl.mpl.facade.comparator;

import de.hybris.platform.commercefacades.product.data.VariantOptionData;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author 592991
 *
 */
public class VariantCapacityComparator implements Comparator<VariantOptionData>
{
	private String pattern;
	private Pattern regexPattern;

	//this is used to compare the sizes of different variants

	@Override
	public int compare(final VariantOptionData arg0, final VariantOptionData arg1)
	{

		//TISPRO-50 - null check added
		final String value0 = arg0.getCapacity() == null ? null : arg0.getCapacity().replaceAll("\\s+", "");
		final String value1 = arg1.getCapacity() == null ? null : arg1.getCapacity().replaceAll("\\s+", "");
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
		else
		{
			final double modifiedValue1 = Double.parseDouble(value0.replaceAll("\\D+", ""));
			final double modifiedValue2 = Double.parseDouble(value1.replaceAll("\\D+", ""));
			//values out of size-systems are placed as last thus so big number.
			return Double.compare(modifiedValue1, modifiedValue2);
		}
		//no luck - assume values are equal

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



}
