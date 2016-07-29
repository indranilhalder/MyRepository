/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.daos.impl.DefaultFetchMerchantMasterDaoImpl;
import com.tisl.mpl.model.MerchantMasterTableModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class DefaultFetchMerchantMasterServiceImpl
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultFetchSalesOrderServiceImpl.class.getName());

	private DefaultFetchMerchantMasterDaoImpl fetchMerchantMasterDao;
	private ModelService modelService;

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
	 * @return the fetchMerchantMasterDao
	 */
	public DefaultFetchMerchantMasterDaoImpl getFetchMerchantMasterDao()
	{
		return fetchMerchantMasterDao;
	}

	/**
	 * @param fetchMerchantMasterDao
	 *           the fetchMerchantMasterDao to set
	 */
	public void setFetchMerchantMasterDao(final DefaultFetchMerchantMasterDaoImpl fetchMerchantMasterDao)
	{
		this.fetchMerchantMasterDao = fetchMerchantMasterDao;
	}

	/**
	 * @Description : Fetch Cron Configuration Details
	 * @param: code
	 * @return : MplConfigurationModel
	 */

	public MplConfigurationModel getCronDetails(final String code)
	{
		return fetchMerchantMasterDao.getCronDetails(code);
	}

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */

	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = fetchMerchantMasterDao.getCronDetails(code);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			oModel.setMplConfigDate(startTime);
			getModelService().save(oModel);
		}
	}

	/**
	 * @Description : Fetch All Parent Order Details
	 * @return List<OrderModel>
	 */

	public List<MerchantMasterTableModel> fetchDetails()
	{
		return fetchMerchantMasterDao.fetchDetails();
	}

	/**
	 * @Description : Fetch All Parent Order Details within stipulated Date Range
	 * @return List<OrderModel>
	 */

	/*
	 * public List<MerchantMasterTableModel> fetchSpecifiedData(final Date startTime, final Date endTime) { return
	 * fetchMerchantMasterDao.fetchSpecifiedData(startTime, endTime); }
	 */
}
