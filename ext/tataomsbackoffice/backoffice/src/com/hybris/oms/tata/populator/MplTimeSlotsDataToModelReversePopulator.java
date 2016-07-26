/**
 *
 */


package com.hybris.oms.tata.populator;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplTimeSlotsModel;


/**
 * this class is used to copy the data from MplTimeSlotsData to MplTimeSlotsModel
 *
 * @author prabhakar
 */

public class MplTimeSlotsDataToModelReversePopulator implements Populator<MplTimeSlotsData, MplTimeSlotsModel>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.commons.conversion.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplTimeSlotsData source, final MplTimeSlotsModel target) throws ConversionException,
			IllegalArgumentException
	{

		target.setTimeslotType(source.getTimeslotType());
		target.setFromTime(source.getFromTime());
		target.setToTime(source.getToTime());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.commons.conversion.Populator#populateFinals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populateFinals(final MplTimeSlotsData paramS, final MplTimeSlotsModel paramT) throws ConversionException,
			IllegalArgumentException
	{
		// YTODO Auto-generated method stub

	}



}
