/**
 * 
 */
package com.tisl.mpl.facade.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;

/**
 * @author Techouts
 *
 */
public class MplConfigFacadeImpl implements MplConfigFacade
{
	@Autowired
   private MplConfigService mplConfigService;
   
	/**
	 * Get configuration value from MplConfig Model by key
	 * 
	 * @param configKey
	 * @return configurationValue
	 */
	@Override
	public String getCongigValue(String configKey)
	{
		return mplConfigService.getConfigValueById(configKey);
	}

	
	/**
	 * Get configuration value from MplConfig Model by key
	 * 
	 * @param configKey
	 * @return configurationValue
	 */
	@Override
	public List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey)
	{
		return mplConfigService.getDeliveryTimeSlotByKey(configKey);
	}


	/* (non-Javadoc)
	 * @see com.tisl.mpl.facade.config.MplConfigFacade#getDeliveryCharges()
	 */
	@Override
	public MplBUCConfigurationsModel getDeliveryCharges()
	{
		// YTODO Auto-generated method stub
		return mplConfigService.getDeliveryCharges();
	}

   /**
	 * Get configuration value from MplConfig Model by key
	 * 
	 * @param configKey
	 * @return configurationValue
	 */
	@Override
	public List<String> getDeliveryTimeSlots(String configKey)
	{
		return mplConfigService.getDeliveryTimeSlots(configKey);
	}


	/* (non-Javadoc)
	 * @see com.tisl.mpl.facade.config.MplConfigFacade#getMplLPHolidays(java.lang.String)
	 */
	@Override
	public MplLPHolidaysModel getMplLPHolidays(String configKey)
	{
		// YTODO Auto-generated method stub
		return mplConfigService.getMplLPHolidays(configKey);
	}
   

}
