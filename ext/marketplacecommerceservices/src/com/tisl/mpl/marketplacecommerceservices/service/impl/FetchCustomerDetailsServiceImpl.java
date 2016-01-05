/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.daos.FetchCustomerDetailsDao;
import com.tisl.mpl.marketplacecommerceservices.service.FetchCustomerDetailsService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class FetchCustomerDetailsServiceImpl implements FetchCustomerDetailsService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FetchCustomerDetailsServiceImpl.class.getName());
	private ModelService modelService;
	private FetchCustomerDetailsDao fetchCustomerDetailsDao;

	/**
	 * @return the fetchCustomerDetailsDao
	 */
	public FetchCustomerDetailsDao getFetchCustomerDetailsDao()
	{
		return fetchCustomerDetailsDao;
	}

	/**
	 * @param fetchCustomerDetailsDao
	 *           the fetchCustomerDetailsDao to set
	 */
	public void setFetchCustomerDetailsDao(final FetchCustomerDetailsDao fetchCustomerDetailsDao)
	{
		this.fetchCustomerDetailsDao = fetchCustomerDetailsDao;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @Description : To Save Cron details in a Table
	 * @param: endTime
	 * @param: cronCode
	 */
	@Override
	public void saveCronDetails(final Date time, final String cronCode)
	{
		final MplConfigurationModel oModel = fetchCustomerDetailsDao.fetchConfigDetails(cronCode);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			oModel.setMplConfigDate(time);
			getModelService().save(oModel);
		}


	}

	/**
	 * @Description : To Save Cron details in a Table
	 * @param: cronCode
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String cronCode)
	{
		return fetchCustomerDetailsDao.fetchConfigDetails(cronCode);

	}

	/**
	 * @Description : Fetch all Customer Details
	 * @return: List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> fetchCustomerDetails()
	{
		return fetchCustomerDetailsDao.fetchCustomerDetails();
	}

	/**
	 * @Description : Fetch all Customer Details
	 * @return: List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> specificCustomerDetails(final Date earlierDate, final Date presentDate)
	{
		return fetchCustomerDetailsDao.specificCustomerDetails(earlierDate, presentDate);
	}
}
