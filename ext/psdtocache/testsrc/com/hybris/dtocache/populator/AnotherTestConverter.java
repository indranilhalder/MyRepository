/**
 *
 */
package com.hybris.dtocache.populator;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.dtocache.testmodel.TestData;
import com.hybris.dtocache.testmodel.TestModel;


/**
 * @author i309277
 *
 */
public class AnotherTestConverter extends AbstractPopulatingConverter<TestModel, TestData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.impl.AbstractConverter#convert(java.lang.Object)
	 */
	@Override
	public TestData convert(final TestModel source) throws ConversionException
	{
		return super.convert(source);
	}

	@Override
	protected TestData createTarget() {
		return new TestData();
	}
}
