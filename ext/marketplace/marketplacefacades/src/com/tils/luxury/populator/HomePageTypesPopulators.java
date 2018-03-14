/**
 *
 */
package com.tils.luxury.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.tisl.lux.facades.homepagetypes.data.HomePageTypesData;
import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public class HomePageTypesPopulators implements Populator<HomePageTypesModel, HomePageTypesData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final HomePageTypesModel source, final HomePageTypesData target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		target.setCode(source.getCode());
		target.setDiscription(source.getDiscription());

	}

}
