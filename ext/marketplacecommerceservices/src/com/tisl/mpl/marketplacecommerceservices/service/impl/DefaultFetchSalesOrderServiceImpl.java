/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.daos.FetchSalesOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class DefaultFetchSalesOrderServiceImpl implements FetchSalesOrderService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultFetchSalesOrderServiceImpl.class.getName());

	private FetchSalesOrderDao fetchSalesOrderDao;
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
	 * @return the fetchSalesOrderDao
	 */
	public FetchSalesOrderDao getFetchSalesOrderDao()
	{
		return fetchSalesOrderDao;
	}

	/**
	 * @param fetchSalesOrderDao
	 *           the fetchSalesOrderDao to set
	 */
	public void setFetchSalesOrderDao(final FetchSalesOrderDao fetchSalesOrderDao)
	{
		this.fetchSalesOrderDao = fetchSalesOrderDao;
	}

	/**
	 * @Description : Fetch Cron Configuration Details
	 * @param: code
	 * @return : MplConfigurationModel
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		return fetchSalesOrderDao.getCronDetails(code);
	}

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */
	@Override
	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = fetchSalesOrderDao.getCronDetails(code);
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
	@Override
	public List<OrderModel> fetchOrderDetails()
	{
		return fetchSalesOrderDao.fetchOrderDetails();
	}

	@Override
	public List<OrderModel> fetchCancelOrderDetails()
	{
		return fetchSalesOrderDao.fetchCancelOrderDetails();
	}

	/**
	 * @Description : Fetch All Parent Order Details within stipulated Date Range
	 * @return List<OrderModel>
	 */
	@Override
	public List<OrderModel> fetchSpecifiedData(final Date startTime, final Date endTime)
	{
		return fetchSalesOrderDao.fetchSpecifiedData(startTime, endTime);
	}

	@Override
	public List<OrderModel> fetchSpecifiedCancelData(final Date startTime, final Date endTime)
	{
		return fetchSalesOrderDao.fetchSpecifiedCancelData(startTime, endTime);
	}


}
