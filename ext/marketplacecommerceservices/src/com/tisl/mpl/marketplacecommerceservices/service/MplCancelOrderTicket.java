/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @author TCS
 *
 */
public interface MplCancelOrderTicket
{

	/**
	 * This method creates cancellation ticket in CRM for order cancelled from EBS console
	 *
	 * @param orderModel
	 * @return boolean
	 */
	public boolean createCancelTicket(OrderModel orderModel);

	/**
	 * This method creates refund transaction mapping
	 *
	 * @param refundID
	 * @param orderModel
	 */
	public void refundMapping(String refundID, OrderModel orderModel);
}
