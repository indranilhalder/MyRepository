/**
 *
 */
package com.hybris.oms.tata.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

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
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setAirDeliveryBuffer(source.getAirDeliveryBuffer());
		target.setSurDeliveryBuffer(source.getSurDeliveryBuffer());
		target.setSdCharge(source.getSdCharge());
		target.setEdCharge(source.getEdCharge());
	}


}