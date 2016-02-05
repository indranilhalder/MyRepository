package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderStatusCodeMasterModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;


/**
 * @author TCS
 *
 */
public class OrderModelServiceImpl implements OrderModelService
{
	private OrderModelDao orderModelDao;

	private OrderStatusCodeMasterModelDao orderModelStatusDao;

	/**
	 * @return the orderModelDao
	 */
	public OrderModelDao getOrderModelDao()
	{
		return orderModelDao;
	}

	/**
	 * @param orderModelDao
	 *           the orderModelDao to set
	 */
	public void setOrderModelDao(final OrderModelDao orderModelDao)
	{
		this.orderModelDao = orderModelDao;
	}

	/**
	 * @description: Get all Order List
	 * @return : List<OrderModel>
	 */
	@Override
	public List<OrderModel> getOrderList()
	{
		return getOrderModelDao().getAllOrders();
	}

	/**
	 * @description: Get Order List from a given date
	 * @param: lastRuntime
	 * @return : List<OrderModel>
	 */
	@Override
	public List<OrderModel> getOrderList(final Date lastRuntime)
	{
		return getOrderModelDao().getAllOrders(lastRuntime);
	}

	/**
	 * @description: Get Order List from a given start date and end date
	 * @param: startDate
	 * @param: endDate
	 * @return : List<OrderModel>
	 */
	@Override
	public List<OrderModel> getOrderList(final Date startDate, final Date endDate)
	{
		return getOrderModelDao().getAllOrders(startDate, endDate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.OrderModelService#getOrder(java.lang.String)
	 */
	@Override
	public OrderModel getOrder(final String code)
	{
		return getOrderModelDao().getOrder(code);
	}

	/**
	 * Get order for push notification --- versionId is not null
	 *
	 * @param code
	 * @return OrderModel
	 */
	@Override
	public OrderModel getOrderPushNotification(final String code)
	{
		return getOrderModelDao().getOrderPushNotification(code);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.OrderModelService#getOrderStausCodeMaster(java.lang.String)
	 */
	@Override
	public OrderStatusCodeMasterModel getOrderStausCodeMaster(final String statuscode)
	{
		return orderModelStatusDao.getOrderStatusCodeMaster(statuscode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.OrderModelService#getOrderStausCodeMasterList()
	 */
	@Override
	public Map<String, OrderStatusCodeMasterModel> getOrderStausCodeMasterList()
	{
		final Map<String, OrderStatusCodeMasterModel> trackingModelMap = new HashMap<String, OrderStatusCodeMasterModel>();
		final List<OrderStatusCodeMasterModel> configueableTrackingList = orderModelStatusDao.getOrderStatusCodeMasterList();

		for (final OrderStatusCodeMasterModel trackingModel : configueableTrackingList)
		{
			trackingModelMap.put(
					trackingModel.getStage() + MarketplacecommerceservicesConstants.STRINGSEPARATOR + trackingModel.getStatusCode(),
					trackingModel);
		}
		return trackingModelMap;
	}

	@Override
	public Map<String, OrderStatusCodeMasterModel> getOrderStatusCodeMasterList()
	{
		final Map<String, OrderStatusCodeMasterModel> trackingModelMap = new HashMap<String, OrderStatusCodeMasterModel>();
		final List<OrderStatusCodeMasterModel> configueableTrackingList = orderModelStatusDao.getOrderStatusCodeMasterList();

		for (final OrderStatusCodeMasterModel trackingModel : configueableTrackingList)
		{
			trackingModelMap.put(trackingModel.getStatusCode(), trackingModel);
		}
		return trackingModelMap;
	}

	/**
	 * @return the orderModelStatusDao
	 */
	public OrderStatusCodeMasterModelDao getOrderModelStatusDao()
	{
		return orderModelStatusDao;
	}

	/**
	 * @param orderModelStatusDao
	 *           the orderModelStatusDao to set
	 */
	@Required
	public void setOrderModelStatusDao(final OrderStatusCodeMasterModelDao orderModelStatusDao)
	{
		this.orderModelStatusDao = orderModelStatusDao;
	}

}