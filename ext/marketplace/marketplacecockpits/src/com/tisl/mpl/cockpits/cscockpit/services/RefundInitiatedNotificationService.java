package com.tisl.mpl.cockpits.cscockpit.services;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;

/**
 * @author TCS
 *
 */
public interface RefundInitiatedNotificationService {

	/**
	 * @param noOfItems 
	 * @param returnEntry
	 * @param orderModel
	 *   
	 */

	public void sendRefundInitiatedNotification(int noOfItems,final ReturnEntryModel returnEntry,final OrderModel orderModel);

}
   