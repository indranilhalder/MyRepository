/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Date;
import java.util.List;


/**
 * @author prasad1
 *
 */
public interface MplDeliveryAddressService
{


	public boolean isDeliveryAddressChangable(String orderId);

	/**
	 * Based on orderCode We get TemproryAddressModel And OrderModel
	 *
	 * @param orderCode
	 * @return flag
	 */
	public boolean saveDeliveryAddress(AddressModel newAddressModel,OrderModel orderModel);

	/***
	 * set status for change delivery address Request 
	 * @param orderModel
	 */
   public void deliveryAddressFailureRequest(OrderModel orderModel);
  
   
	/**
	 *  This method is used to check whether order has Schedule delivery items or not
	 * @param  orderModel
	 */
	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel);

	
	public List<OrderModel> getOrderModelList(Date fromDate, final Date toDate);

}
