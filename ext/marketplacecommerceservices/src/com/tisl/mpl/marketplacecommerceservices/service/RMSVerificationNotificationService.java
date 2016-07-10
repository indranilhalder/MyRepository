/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;



/**
 * @author TCS
 *
 */
public interface RMSVerificationNotificationService
{

	/**
	 *
	 * @param orderModel
	 * 
	 */


	public void sendRMSNotification(final OrderModel orderModel);
}
