/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.JuspayWebhookModel;


/**
 * @author TCS
 *
 */
public interface MplProcessOrderDao
{
	//PaymentFix2017:- queryTAT added
	List<OrderModel> getPaymentPedingOrders(String statusCode, Date queryTAT);

	List<JuspayWebhookModel> getEventsForPendingOrders(String redId);

}
