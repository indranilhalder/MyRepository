/**
 *
 */
package com.tisl.lux.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;

import java.util.Map;

import com.tisl.lux.constants.LuxurystoreaddonConstants;


/**
 * @author madhavan
 *
 */
public class LuxuryIndicatorDecorator implements CSVCellDecorator
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.util.CSVCellDecorator#decorate(int, java.util.Map)
	 */
	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{
		final String luxIndicator = srcLine.get(position);
		if (luxIndicator.equalsIgnoreCase(LuxurystoreaddonConstants.LUXURY)
				|| luxIndicator.equalsIgnoreCase(LuxurystoreaddonConstants.MARKETPLACE))
		{
			return luxIndicator;
		}
		else
		{
			return LuxurystoreaddonConstants.LUXURY;
		}
	}

}
