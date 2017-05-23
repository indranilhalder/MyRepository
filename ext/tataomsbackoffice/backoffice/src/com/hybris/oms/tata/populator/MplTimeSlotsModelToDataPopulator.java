/**
 *
 */
package com.hybris.oms.tata.populator;

import de.hybris.platform.converters.Populator;

import org.springframework.util.Assert;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * @author prabhakar
 *
 */
public class MplTimeSlotsModelToDataPopulator implements Populator<MplTimeSlotsModel, MplTimeSlotsData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.commons.conversion.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplTimeSlotsModel source, final MplTimeSlotsData target) throws ConversionException,
			IllegalArgumentException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setTimeslotType(source.getTimeslotType());
		target.setFromTime(source.getFromTime());
		target.setToTime(source.getToTime());
	}

}
