/**
 *
 */
package com.hybris.oms.tata.facade.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.oms.tata.data.MplBUCConfigurationsData;
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
	private static final Logger LOG = Logger.getLogger(DefaultConfigarableParameterFacade.class);
	@Resource(name = "configrableParameterService")
	private ConfigrableParameterService configrableParameterService;

	@Resource(name = "mplTimeSlotsConverter")
	private Converter<MplTimeSlotsModel, MplTimeSlotsData> mplTimeSlotsConverter;

	@Resource(name = "mplBUCReverseConverter")
	private Converter<MplBUCConfigurationsData, MplBUCConfigurationsModel> mplBUCReverseConverter;

	@Resource(name = "mplTimeSlotsReverseConverter")
	private Converter<MplTimeSlotsData, MplTimeSlotsModel> mplTimeSlotsReverseConverter;


	// converter dependency

	/**
	 * @param mplTimeSlotsReverseConverter
	 *           the mplTimeSlotsReverseConverter to set
	 */
	public void setMplTimeSlotsReverseConverter(final Converter<MplTimeSlotsData, MplTimeSlotsModel> mplTimeSlotsReverseConverter)
	{
		this.mplTimeSlotsReverseConverter = mplTimeSlotsReverseConverter;
	}

	/**
	 * @param mplBUCReverseConverter
	 *           the mplBUCReverseConverter to set
	 */
	public void setMplBUCReverseConverter(
			final Converter<MplBUCConfigurationsData, MplBUCConfigurationsModel> mplBUCReverseConverter)
	{
		this.mplBUCReverseConverter = mplBUCReverseConverter;
	}

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
	public void saveMplTimeSlots(final Set<MplTimeSlotsData> mplTimeSlots, final String timeSlottype)
	{
		LOG.info("SaveMplTimeSlots");
		final List<MplTimeSlotsModel> mplTimeSlotsModelList = Converters.convertAll(mplTimeSlots, mplTimeSlotsReverseConverter);

		configrableParameterService.saveMplTimeSlots(mplTimeSlotsModelList, timeSlottype);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.facade.ConfigarableParameterFacade#saveMplBUCConfigurations(com.hybris.oms.tata.data.
	 * MplBUCConfigurationsData)
	 */
	@Override
	public void saveMplBUCConfigurations(final MplBUCConfigurationsData mplBucConfigurations)
	{
		final MplBUCConfigurationsModel mplBucConfigModel = mplBUCReverseConverter.convert(mplBucConfigurations);
		configrableParameterService.saveMplBUCConfigurations(mplBucConfigModel);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.facade.ConfigarableParameterFacade#getScheduledCharge()
	 */
	@Override
	public double getScheduledCharge()
	{

		return configrableParameterService.getScheduledCharge();
	}

}