/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.model.OrderStatusCodeMasterModel;



/**
 * @author TCS
 *
 */
public interface OrderModelService
{


	/**
	 * @description: Get all Order List
	 * @return : List<OrderModel>
	 */
	List<OrderModel> getOrderList();


	/**
	 * @description: Get Order List from a given date
	 * @param: lastRuntime
	 * @return : List<OrderModel>
	 */
	List<OrderModel> getOrderList(Date lastRuntime);

	/**
	 * @description: Get Order List from a given start date and end date
	 * @param: startDate
	 * @param: endDate
	 * @return : List<OrderModel>
	 */
	List<OrderModel> getOrderList(final Date startDate, final Date endDate);

	/**
	 * @description: Get Order
	 * @param: code
	 * @return : List<OrderModel>
	 */

	/**
	 * Get order for push notification --- versionId is not null
	 *
	 * @param code
	 * @return OrderModel
	 */
	OrderModel getOrder(final String code);

	public OrderModel getOrderPushNotification(final String code);

	/**
	 * @description: Get OrderStatusCode
	 * @param: statuscode
	 * @return : OrderStatusCodeMasterModel
	 */
	OrderStatusCodeMasterModel getOrderStausCodeMaster(final String statuscode);

	/**
	 * @description: Get OrderStatusCode List
	 * @return : List<OrderStatusCodeMasterModel>
	 */
	Map<String, OrderStatusCodeMasterModel> getOrderStausCodeMasterList();

	/**
	 * @description: Get OrderStatusCode List
	 * @return : List<OrderStatusCodeMasterModel>
	 */
	Map<String, OrderStatusCodeMasterModel> getOrderStatusCodeMasterList();


	/**
	 * UpdatePickUpdetails
	 * 
	 * @param orderId
	 * @param name
	 * @param mobile
	 * @return
	 */
	OrderModel updatePickUpDetailService(final String orderId, final String name, final String mobile);


}