/**
 *
 */
package com.tisl.mpl.core.mplconfig.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//import com.hybris.oms.tata.data.MplTimeSlotsData;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.model.MplConfigModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.mplconfig.dao.MplConfigDao;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;
import com.tisl.mpl.util.MplTimeconverUtility;


/**
 * The MplConfigServiceImpl class invokes the MplConfigDao by passes the requested id to method of DAO.
 *
 * @author SAP
 * @version 1.0
 *
 */
public class MplConfigServiceImpl implements MplConfigService
{
	private static final Logger LOGGER = Logger.getLogger(MplConfigServiceImpl.class);
	@Autowired
	private MplConfigDao mplConfigDao;
	@Autowired
	private ModelService modelService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfigValueById(final String id)
	{
		String configValue = null;
		
		try
		{
		if (StringUtils.isNotEmpty(id))
		{
			final MplConfigModel configModel = mplConfigDao.getConfigValueById(id);
			if (null != configModel)
			{
				configValue = configModel.getValue();
			}
		}
		}
		catch(Exception e)
		{
			LOGGER.error("Congiguration Not foud for the Key:"+id);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - config value for key:" + id + " is :" + configValue);
		}
		return configValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getConfigValuesById(final String id, final String seperator)
	{
		List<String> values = null;
		final String configValue = getConfigValueById(id);
		if (StringUtils.isNotEmpty(seperator) && StringUtils.isNotEmpty(configValue))
		{
			values = new ArrayList<String>();
			values = Arrays.asList(configValue.split(seperator));
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getConfigValueById() - config values for key:" + id + " are :" + values);
			}

		}

		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveConfigValues(final String key, final String value)
	{
		if (StringUtils.isNotEmpty(key))
		{
			final MplConfigModel configModel = mplConfigDao.getConfigValueById(key);
			if (null != configModel)
			{
				configModel.setValue(value);
				modelService.save(configModel);
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("saveConfigValues() -Saved value is :" + value);
			}
		}


	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MplTimeSlotsModel> getDeliveryTimeSlotByKey(String configKey)
	{
		List<MplTimeSlotsModel> configValueList = null;
		
		try
		{
		if (StringUtils.isNotEmpty(configKey))
		{
			configValueList = mplConfigDao.getDeliveryTimeSlotByKey(configKey);
		}
		}
		catch(Exception e)
		{
			LOGGER.error("Congiguration Not foud for the Key:"+configKey);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - config value for key:" + configKey + " is :" + configValueList);
		}
		return configValueList;
	}

	/* (non-Javadoc)
	 * @see com.tisl.mpl.core.mplconfig.service.MplConfigService#getDeliveryCharges()
	 */
	@Override
	public MplBUCConfigurationsModel getDeliveryCharges()
	{
		MplBUCConfigurationsModel configValue = null;
		
		try
		{
		
			configValue = mplConfigDao.getDeliveryCharges();

		}
		catch(Exception e)
		{
			LOGGER.error("Congiguration Not foud for the class MplBUCConfigurationsModel :");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - config value for  is :" + configValue);
		}
		return configValue;
	}

        /**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getDeliveryTimeSlots(String configKey)
	{
		List<MplTimeSlotsModel> configValueList = null;
		ArrayList<String> deliverySlots=new ArrayList<String>();
		
		try
		{
		if (StringUtils.isNotEmpty(configKey))
		{
			configValueList = mplConfigDao.getDeliveryTimeSlotByKey(configKey);
			
			for (MplTimeSlotsModel timeslot : configValueList)
			{
				
				StringBuffer sb=new StringBuffer(MplTimeconverUtility.convert24hoursTo12hours(timeslot.getFromTime()));
				sb.append("-");
				sb.append(MplTimeconverUtility.convert24hoursTo12hours(timeslot.getToTime()));
				deliverySlots.add(new String(sb));
			}
			
		}
		}
		catch(Exception e)
		{
			LOGGER.error("Congiguration Not foud for the Key:"+configKey);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById() - config value for key:" + configKey + " is :" + configValueList);
		}
		return deliverySlots;
	}

		/* (non-Javadoc)
		 * @see com.tisl.mpl.core.mplconfig.service.MplConfigService#getMplLPHolidays(java.lang.String)
		 */
		@Override
		public MplLPHolidaysModel getMplLPHolidays(String configKey)
		{
			MplLPHolidaysModel configValue = null;
			
			try
			{
			
				configValue = mplConfigDao.getMplLPHolidays(configKey);
 
			}
			catch(Exception e)
			{
				LOGGER.error("Congiguration Not foud for the class MplBUCConfigurationsModel :");
			}

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getLpname() - config value for  is :" + configValue.getLpname());
			}
			return configValue;
		}
}