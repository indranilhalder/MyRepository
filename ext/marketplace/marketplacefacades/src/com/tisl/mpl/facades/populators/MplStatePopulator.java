/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.model.StateModel;


/**
 * @author Dileep
 *
 */
public class MplStatePopulator implements Populator<StateModel, StateData>
{

	/**
	 * @Description Populating state Model to state Data
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final StateModel source, final StateData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getRegion());
		target.setCountryKey(source.getCountrykey());
		target.setName(source.getDescription());

	}

}
