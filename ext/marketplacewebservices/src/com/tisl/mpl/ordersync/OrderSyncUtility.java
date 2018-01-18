/**
 *
 */
package com.tisl.mpl.ordersync;

import de.hybris.platform.core.model.order.OrderModel;

import java.io.IOException;
import java.io.InputStream;

import com.tisl.mpl.core.model.OrderSyncLogModel;
import com.tisl.mpl.order.SellerOrderDTO;


/**
 * @author TCS
 *
 */
public interface OrderSyncUtility
{
	public OrderSyncLogModel syncOrder(final InputStream orderSyncXML) throws IOException;

	void updateOrCreateConsignment(final SellerOrderDTO sellerOrder, final OrderModel sOrder);
}
