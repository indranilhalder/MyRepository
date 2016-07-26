/**
 *
 */
package com.hybris.oms.tata.facade.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.facade.ConfigarableParameterFacade;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.hybris.oms.tata.services.ConfigrableParameterService;


/**
 * @author prabhakar
 *
 */

public class DefaultConfigarableParameterFacade implements ConfigarableParameterFacade
{
	@Resource(name = "configrableParameterService")
	private ConfigrableParameterService configrableParameterService;

	@Resource(name = "mplTimeSlotsConverter")
	private Converter<MplTimeSlotsModel, MplTimeSlotsData> mplTimeSlotsConverter;


	// converter dependency

	/**
	 * @param configrableParameterService
	 *           the configrableParameterService to set
	 */
	public void setConfigrableParameterService(final ConfigrableParameterService configrableParameterService)
	{
		this.configrableParameterService = configrableParameterService;
	}

	/**
	 * @param mplTimeSlotsConverter
	 *           the mplTimeSlotsConverter to set
	 */
	public void setMplTimeSlotsConverter(final Converter<MplTimeSlotsModel, MplTimeSlotsData> mplTimeSlotsConverter)
	{
		this.mplTimeSlotsConverter = mplTimeSlotsConverter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.facade.ConfigarableParameterFacade#onLoadMplTimeSlots()
	 */
	@Override
	public List<MplTimeSlotsData> onLoadMplTimeSlots()
	{
		final List<MplTimeSlotsModel> timeSlotsModel = configrableParameterService.onLoadMplTimeSlots();
		return Converters.convertAll(timeSlotsModel, mplTimeSlotsConverter);


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.oms.tata.facade.ConfigarableParameterFacade#saveMplTimeSlots(java.util.List)
	 */
	@Override
	public void saveMplTimeSlots(final List<MplTimeSlotsModel> mplTimeSlots)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.oms.tata.facade.ConfigarableParameterFacade#saveMplBUCConfigurations(com.hybris.oms.tata.model.
	 * MplBUCConfigurationsModel)
	 */
	@Override
	public void saveMplBUCConfigurations(final MplBUCConfigurationsModel mplBucConfigurations)
	{
		// YTODO Auto-generated method stub

	}

}
