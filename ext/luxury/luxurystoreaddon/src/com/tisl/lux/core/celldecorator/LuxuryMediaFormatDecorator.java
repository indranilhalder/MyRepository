/**
 *
 */
package com.tisl.lux.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;
import de.hybris.platform.util.Config;

import java.util.Map;


/**
 * @author BORN
 *
 */
public class LuxuryMediaFormatDecorator implements CSVCellDecorator
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.util.CSVCellDecorator#decorate(int, java.util.Map)
	 */

	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{
		final String imagePath = srcLine.get(position + 2);
		if (Config.getBoolean("luxury.mediaformat.translator.enabled", false))
		{
			if (imagePath.contains("secondary"))
			{
				return Config.getString("luxury.secondory.format", "luxurySecondary");
			}
			if (imagePath.contains("model"))
			{
				return Config.getString("luxury.model.format", "luxuryModel");
			}
		}

		return srcLine.get(position);
	}


}
