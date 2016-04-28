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

import com.tisl.mpl.core.model.MplConfigModel;
import com.tisl.mpl.core.mplconfig.dao.MplConfigDao;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;



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

}