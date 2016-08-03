/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplProcessOrderService
{
	List<OrderModel> getPaymentPedingOrders();

}
