/**
 *
 */
package com.hybris.oms.tata.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.oms.tata.data.MplBUCConfigurationsData;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;


/**
 * this class is used to copy the MplBUCConfigurationData to MplBUCConfigurationModel
 *
 * @author prabhakar
 *
 */
public class MplBUCConfigurationsDataToModelReversePopulator implements
		Populator<MplBUCConfigurationsData, MplBUCConfigurationsModel>


{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplBUCConfigurationsData source, final MplBUCConfigurationsModel target) throws ConversionException
	{
		target.setAirDeliveryBuffer(source.getAirDeliveryBuffer());
		target.setSurDeliveryBuffer(source.getSurDeliveryBuffer());
		target.setSdCharge(source.getSdCharge());
		target.setEdCharge(source.getEdCharge());
	}


}
