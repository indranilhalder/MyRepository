/**
 *
 */
package com.hybris.oms.tata.services.impl;



import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.tata.daos.ConfigarableParameterDAO;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.hybris.oms.tata.services.ConfigrableParameterService;


/**
 * @author prabhakar
 *
 */

public class DefaultConfigarableParameterService implements ConfigrableParameterService
{

	@Resource(name = "configarableParameterDAO")
	private ConfigarableParameterDAO configarableParameterDAO;

	/**
	 * @param configarableParameterDAO
	 *           the configarableParameterDAO to set
	 */
	public void setConfigarableParameterDAO(final ConfigarableParameterDAO configarableParameterDAO)
	{
		this.configarableParameterDAO = configarableParameterDAO;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.services.ConfigrableParameterService#onLoadMplTimeSlots()
	 */
	@Override
	public List<MplTimeSlotsModel> onLoadMplTimeSlots()
	{
		return configarableParameterDAO.onLoadMplTimeSlots();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.services.ConfigrableParameterService#saveMplTimeSlots(java.util.List)
	 */
	@Override
	public void saveMplTimeSlots(final List<MplTimeSlotsModel> mplTimeSlots)
	{

		configarableParameterDAO.saveMplTimeSlots(mplTimeSlots);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.oms.tata.services.ConfigrableParameterService#saveMplBUCConfigurations(com.hybris.oms.tata.model.
	 * MplBUCConfigurationsModel)
	 */
	@Override
	public void saveMplBUCConfigurations(final MplBUCConfigurationsModel mplBucConfigurations)
	{
		if (mplBucConfigurations != null)
		{
			configarableParameterDAO.saveMplBUCConfigurations(mplBucConfigurations);
		}

	}





}
