package com.hybris.oms.tata.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * this class is used to copy the data from MplTimeSlotsData to MplTimeSlotsModel
 *
 * @author prabhakar
 */

public class MplTimeSlotsDataToModelReversePopulator implements
		de.hybris.platform.converters.Populator<MplTimeSlotsData, MplTimeSlotsModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplTimeSlotsData source, final MplTimeSlotsModel target) throws ConversionException
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setTimeslotType(source.getTimeslotType());
		target.setFromTime(source.getFromTime());
		target.setToTime(source.getToTime());
	}

}