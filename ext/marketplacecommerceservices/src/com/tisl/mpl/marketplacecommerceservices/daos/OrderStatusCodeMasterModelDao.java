/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.model.OrderStatusCodeMasterModel;


/**
 * @author TCS
 *
 */
public interface OrderStatusCodeMasterModelDao
{
	/**
	 * It OrderStatusCodeMasterModel Order
	 * 
	 * @param statusCode
	 * @return OrderModel
	 *
	 */
	OrderStatusCodeMasterModel getOrderStatusCodeMaster(final String statusCode);

	/**
	 * It gets OrderStatusCodeMasterModel List
	 *
	 * @return List<OrderStatusCodeMasterModel>
	 *
	 */
	List<OrderStatusCodeMasterModel> getOrderStatusCodeMasterList();

}