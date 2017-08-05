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
public class MediaFormatDecorator implements CSVCellDecorator
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.util.CSVCellDecorator#decorate(int, java.util.Map)
	 */
	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{
		final String mediaFormat = srcLine.get(position);
		if (null != mediaFormat && Config.getBoolean("luxury.mediaformat.translator.enabled", false))
		{
			if (mediaFormat.equals(Config.getParameter("luxury.secondory.format.equivalent")))
			{
				return Config.getString("luxury.secondory.format", "luxurySecondary");
			}
			if (mediaFormat.equals(Config.getParameter("luxury.model.format.equivalent")))
			{
				return Config.getString("luxury.model.format", "luxuryModel");
			}
		}

		return mediaFormat;
	}

}
