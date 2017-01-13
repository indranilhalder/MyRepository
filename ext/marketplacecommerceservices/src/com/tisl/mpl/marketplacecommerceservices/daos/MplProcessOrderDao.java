/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.core.model.JuspayWebhookModel;


/**
 * @author TCS
 *
 */
public interface MplProcessOrderDao
{
	List<OrderModel> getPaymentPedingOrders(String statusCode);

	List<JuspayWebhookModel> getEventsForPendingOrders(String redId);

	//MRUPYEE CHANGES
	List<OrderModel> getPendingOrRefundInitiatedOrders(final String statusCode1, final String statusCode2);
}
