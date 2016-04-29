/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeServiceDao;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebHookService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class DefaultMplPincodeService implements MplPincodeService
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultMplPincodeService.class);

	private MplPincodeServiceDao mplPincodeServiceDao;
	private JuspayWebHookService juspayWebHookService;




	/**
	 * Keeps record of Cron Last run time
	 *
	 * @param jobCode
	 * @return MplConfigurationModel
	 */
	@Override
	public Map<String, Date> getConfigurationData(final String jobCode)
	{
		final Map<String, Date> dataMap = new HashedMap();
		try
		{
			if (StringUtils.isNotEmpty(jobCode))
			{
				final MplConfigurationModel configModel = juspayWebHookService.getCronDetails(jobCode);
				if (null != configModel && null != configModel.getMplConfigDate())
				{
					dataMap.put(jobCode, configModel.getMplConfigDate());
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
			throw exception;
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
			throw exception;
		}

		return dataMap;
	}


	/**
	 * Fetch data to invalidate
	 *
	 * @param date
	 * @param jobLastRunDate
	 */
	@Override
	public List<PincodeServiceabilityDataModel> getPincodeData(final Date jobLastRunDate, final Date date)
	{
		return mplPincodeServiceDao.getPincodeData(jobLastRunDate, date);
	}

	/**
	 * Fetch data to invalidate
	 *
	 */
	@Override
	public List<PincodeServiceabilityDataModel> fetchData()
	{
		return mplPincodeServiceDao.getPincodeData(null, new Date());
	}



	/**
	 * Save the Cron Details
	 *
	 * @param startTime
	 * @param code
	 */
	@Override
	public void saveCronData(final Date startTime, final String code)
	{
		juspayWebHookService.saveCronDetails(startTime, code);
	}




	/**
	 * @return the mplPincodeServiceDao
	 */
	public MplPincodeServiceDao getMplPincodeServiceDao()
	{
		return mplPincodeServiceDao;
	}

	/**
	 * @param mplPincodeServiceDao
	 *           the mplPincodeServiceDao to set
	 */
	public void setMplPincodeServiceDao(final MplPincodeServiceDao mplPincodeServiceDao)
	{
		this.mplPincodeServiceDao = mplPincodeServiceDao;
	}




	/**
	 * @return the juspayWebHookService
	 */
	public JuspayWebHookService getJuspayWebHookService()
	{
		return juspayWebHookService;
	}




	/**
	 * @param juspayWebHookService
	 *           the juspayWebHookService to set
	 */
	public void setJuspayWebHookService(final JuspayWebHookService juspayWebHookService)
	{
		this.juspayWebHookService = juspayWebHookService;
	}




}
