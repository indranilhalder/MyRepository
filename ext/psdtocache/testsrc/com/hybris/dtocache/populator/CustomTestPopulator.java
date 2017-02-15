/**
 * 
 */
package com.hybris.dtocache.populator;

import com.hybris.dtocache.testmodel.CustomTestData;
import com.hybris.dtocache.testmodel.CustomTestModel;
import com.hybris.dtocache.testmodel.TestData;
import com.hybris.dtocache.testmodel.TestModel;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author i309425
 *
 */
public class CustomTestPopulator extends TestPopulator {

	@Override
	public void populate(TestModel source, TestData target)
			throws ConversionException {
		CustomTestModel exactSource = (CustomTestModel) source;
		CustomTestData exactData = (CustomTestData) target;
		
		super.populate(source, target);
		
	}

}
