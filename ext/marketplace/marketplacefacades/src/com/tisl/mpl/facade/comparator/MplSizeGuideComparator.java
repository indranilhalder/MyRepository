/**
 *
 */
package com.tisl.mpl.facade.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;



/**
 * @author TCS
 *
 */
public class MplSizeGuideComparator implements Comparator<String>
{
	private String pattern;
	private Pattern regexPattern;
	private List<List<String>> sizeSystems;
	private static final Logger LOG = Logger.getLogger(SizeGuideComparator.class);
	private static final String S = "\\s+";

	@Override
	public int compare(final String sizeData1, final String sizeData2)
	{

		LOG.debug("Check SizeData1 dimension size: " + sizeData1);
		LOG.debug("Check SizeData1 dimension size: " + sizeData2);

		final int value1SizeSystemIndex = getSizeSystemIndex(sizeData1.replaceAll(S, "").toUpperCase());
		final int value2SizeSystemIndex = getSizeSystemIndex(sizeData2.replaceAll(S, "").toUpperCase());

		if (value1SizeSystemIndex != -1 && value2SizeSystemIndex != -1)
		{
			final int val1Index = (value1SizeSystemIndex + 1) * 100 + sizeSystems.get(value1SizeSystemIndex).indexOf(sizeData1);
			final int val2Index = (value2SizeSystemIndex + 1) * 100 + sizeSystems.get(value2SizeSystemIndex).indexOf(sizeData2);
			return val1Index - val2Index;
		}

		return 0;
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
	 * @return the pattern
	 */
	protected String getPattern()
	{
		return pattern;
	}

	/**
	 * @param pattern
	 *           the pattern to set
	 */
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
		this.regexPattern = Pattern.compile(pattern);
	}

	/**
	 * @return the regexPattern
	 */
	protected Pattern getRegexPattern()
	{
		return regexPattern;
	}


	/**
	 * @return the sizeSystems
	 */
	public List<List<String>> getSizeSystems()
	{
		return sizeSystems;
	}

	/**
	 * @param sizeSystems
	 *           the sizeSystems to set
	 */
	public void setSizeSystems(final List<List<String>> sizeSystems)
	{
		this.sizeSystems = sizeSystems;
	}


}
