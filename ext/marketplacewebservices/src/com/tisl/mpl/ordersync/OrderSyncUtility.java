/**
 *
 */
package com.tisl.mpl.ordersync;

import de.hybris.platform.core.model.order.OrderModel;

import com.tisl.mpl.order.SellerOrderDTO;


/**
 * @author TCS
 *
 */
public interface OrderSyncUtility
{
	public void syncOrder(final SellerOrderDTO sellerOrder, final String orderUpdateTime);

	void updateOrCreateConsignment(final SellerOrderDTO sellerOrder, final OrderModel sOrder);
}
