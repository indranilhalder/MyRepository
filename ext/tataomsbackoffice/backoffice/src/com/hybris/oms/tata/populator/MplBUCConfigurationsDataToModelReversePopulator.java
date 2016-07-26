/**
 *
 */
package com.hybris.oms.tata.populator;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Populator;
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
	 * @see com.hybris.commons.conversion.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplBUCConfigurationsData source, final MplBUCConfigurationsModel tartget)
			throws ConversionException, IllegalArgumentException
	{
		tartget.setAirDeliveryBuffer(source.getAirDeliveryBuffer());
		tartget.setSurDeliveryBuffer(source.getSurDeliveryBuffer());
		tartget.setSdCharge(source.getSdCharge());
		tartget.setEdCharge(source.getEdCharge());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.commons.conversion.Populator#populateFinals(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populateFinals(final MplBUCConfigurationsData paramS, final MplBUCConfigurationsModel paramT)
			throws ConversionException, IllegalArgumentException
	{
		// YTODO Auto-generated method stub

	}
}
