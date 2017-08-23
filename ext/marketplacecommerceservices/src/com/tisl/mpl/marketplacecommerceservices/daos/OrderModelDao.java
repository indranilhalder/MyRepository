package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.core.model.BulkReturnProcessModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


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
	 * It gets Order
	 *
	 * @return OrderModel
	 *
	 */
	List<OrderModel> getOrders(final String code);

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


	/**
	 *
	 * @param orderId
	 * @return order Model
	 */

	OrderModel getOrderModel(final String orderId);

	/**
	 * @return BulkReturnProcessModel
	 * @throws EtailNonBusinessExceptions
	 */
	List<BulkReturnProcessModel> getAllBulkReturnData() throws EtailNonBusinessExceptions;

	/**
	 * @return List
	 */


	/**
	 * @param customer
	 * @param agentId
	 * @return
	 */
	List<OrderModel> getOrderByAgent(CustomerModel customer, String agentId);



	public PointOfServiceModel getPointOfService(String storeId);



	public List<OrderEntryModel> getOrderCancelData();

	/**
	 * @param transactionID
	 * @return
	 */
	@SuppressWarnings("javadoc")
	BulkCancellationProcessModel getAllBulkCancelData(String transactionID);


}
