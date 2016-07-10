/**
 *
 */
package com.tisl.mpl.core.celldecorator;

import de.hybris.platform.util.CSVCellDecorator;

import java.util.Map;


/**
 * @author TCS
 *
 */
public class SizeGuideDimensionDecorator implements CSVCellDecorator
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.util.CSVCellDecorator#decorate(int, java.util.Map)
	 */
	@Override
	public String decorate(final int position, final Map<Integer, String> srcLine)
	{



		return srcLine.get(position).toUpperCase();
	}

}
