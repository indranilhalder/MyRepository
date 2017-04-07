/**
 *
 */
package com.hybris.dtocache.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.dtocache.testmodel.TestData;
import com.hybris.dtocache.testmodel.TestModel;


/**
 * @author i309277
 *
 */
public class TestPopulator implements Populator<TestModel, TestData>
{
	public static int CALL_COUNT = 0;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final TestModel source, final TestData target) throws ConversionException
	{
		if (source.getPk() != null)
		{
			target.setPk(source.getPk().getLong());
		}
		target.setP1(source.getP1());
		target.setP2(source.getP2());
		target.setP3(source.getP3());
		target.setP4(source.getP4());
		target.setP5(source.getP5());
		CALL_COUNT++;
	}

	public static int getCallCount()
	{
		return CALL_COUNT;
	}

	public static void resetCallCount()
	{
		CALL_COUNT = 0;
	}


}
