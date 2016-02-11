/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface OrderModelDao
{
	/**
	 * It gets the list of Orders
	 *
	 * @return List<OrderModel>
	 *
	 */
	List<OrderModel> getAllOrders();

	/**
	 * It gets the list of Orders from date
	 *
	 * @return List<OrderModel>
	 *
	 */
	List<OrderModel> getAllOrders(Date fromDate);

	/**
	 * It gets the list of Orders from start date to endDate
	 *
	 * @return List<OrderModel>
	 *
	 */
	List<OrderModel> getAllOrders(final Date startDate, final Date endDate);

	/**
	 * It gets Order
	 *
	 * @return OrderModel
	 *
	 */
	OrderModel getOrder(final String code);

	/**
	 * It gets Order when version id not null -- cancelled items
	 *
	 * @return OrderModel
	 *
	 */

	OrderModel getOrderPushNotification(final String code);



	/**
	 *
	 * It get OrderModel based on orderId and save update information in data base
	 *
	 *
	 * @return OrderModel
	 */
	OrderModel updatePickUpDetailsDao(final String orderId, final String name, final String mobile);

}