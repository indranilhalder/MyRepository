/**
 *
 */
package com.tisl.mpl.core.comparator;

import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.fulfilmentprocess.utility.GenericUtility;


/**
 * @author TCS
 *
 */
public class GenderFacetComparator implements Comparator<FacetValue>
{
	private String pattern;
	private Pattern regexPattern;
	private List<List<String>> genderSystems;
	private static final Logger LOG = Logger.getLogger(GenderFacetComparator.class);

	/**
	 * This method is responsible for genders to be displayed in gender chartok
	 */
	@Override
	public int compare(final FacetValue arg0, final FacetValue arg1)
	{
		String value1 = "";
		String value2 = "";
		final String valueGender1 = "";
		final String valueGender2 = "";
		if (null != arg0.getName())
		{
			value1 = arg0.getName().replaceAll("\\s+", "").toUpperCase();
		}
		if (null != arg0.getName())
		{
			value2 = arg1.getName().replaceAll("\\s+", "").toUpperCase();
		}

		else if (value1.equals(value2) || value1 == value2)
		{
			return 0;
		}
		//try numeric compare - if doesn't work then try gender system
		final boolean value1IsNumber = isNumber(value1);
		final boolean value2IsNumber = isNumber(value2);
		if (value1IsNumber && value2IsNumber)
		{
			return numericCompare(value1, value2);
		}
		//try compare within each gender system
		for (final List<String> genderSystem : genderSystems)
		{
			if (genderSystem.contains(value1) && genderSystem.contains(value2))
			{
				final int index1 = genderSystem.indexOf(value1);
				final int index2 = genderSystem.indexOf(value2);
				if (value1 == value2)
				{
					if (StringUtils.isNotEmpty(valueGender1) && StringUtils.isNotEmpty(valueGender2))
					{
						return numericCompare(valueGender1, valueGender2);
					}
					else
					{
						return index1 - index2;
					}
				}
				else
				{
					return index1 - index2;
				}
			}
		}
		//values may come from distinct gender systems - where one is numeric
		final int value1GenderSystemIndex = getGenderSystemIndex(value1);
		final int value2GenderSystemIndex = getGenderSystemIndex(value2);
		if (value1IsNumber)
		{
			if (value2GenderSystemIndex == -1)
			{
				//indicates numeric values comes first
				//and values out of gender-systems are placed as last thus so big number.
				return Integer.MIN_VALUE;
			}
			else
			{
				//this will distribute other systems
				return -(value2GenderSystemIndex + 1) * 100 + genderSystems.get(value2GenderSystemIndex).indexOf(value2);
			}
		}
		else if (value2IsNumber)
		{
			if (value1GenderSystemIndex == -1)
			{
				//indicates numeric values comes first
				//and values out of gender-systems are placed as last thus so big number.
				return Integer.MAX_VALUE;
			}
			else
			{
				//this will distribute other systems
				return (value1GenderSystemIndex + 1) * 100 + genderSystems.get(value1GenderSystemIndex).indexOf(value1);
			}
		}
		//values may come from distinct gender systems - where both are not numeric
		else if (value1GenderSystemIndex != -1)
		{
			if (value2GenderSystemIndex == -1)
			{
				//values out of gender-systems are placed as last thus so big number.
				return Integer.MIN_VALUE;
			}
			else
			{
				final int val1Index = (value1GenderSystemIndex + 1) * 100
						+ genderSystems.get(value1GenderSystemIndex).indexOf(value1);
				final int val2Index = (value2GenderSystemIndex + 1) * 100
						+ genderSystems.get(value2GenderSystemIndex).indexOf(value2);
				return val1Index - val2Index;
			}
		}
		else if (value2GenderSystemIndex != -1)
		{

			if (value1GenderSystemIndex == -1)
			{
				return Integer.MIN_VALUE;
			}
			else
			{
				final int val2Index = (value2GenderSystemIndex + 1) * 100
						+ genderSystems.get(value2GenderSystemIndex).indexOf(value2);
				final int val1Index = (value1GenderSystemIndex + 1) * 100
						+ genderSystems.get(value1GenderSystemIndex).indexOf(value1);
				return val1Index - val2Index;
			}
		}
		else if (value1GenderSystemIndex == -1 && value2GenderSystemIndex == -1)
		{
			LOG.debug("calling alpha Numeric Compare");
			return GenericUtility.alphaNumericCompare(value1, value2);
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
	 * @return index of first gender-system containing this value, if not found -1 is returned
	 */
	protected int getGenderSystemIndex(final String value)
	{
		for (final List<String> genderSystem : genderSystems)
		{
			if (genderSystem.contains(value))
			{
				return genderSystems.indexOf(genderSystem);
			}
		}
		return -1;//indicates that value doesn't exist in any gender-system
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
	 * @return first
	 */
	protected double getNumber(final String value)
	{
		final Matcher matcher = getRegexPattern().matcher(value);
		if (matcher.find())
		{
			return Double.parseDouble(matcher.group());
		}
		return Integer.MAX_VALUE;
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

	protected List<List<String>> getGenderSystems()
	{
		return genderSystems;
	}

	@Required
	public void setGenderSystems(final List<List<String>> genderSystems)
	{
		this.genderSystems = genderSystems;
	}


}
