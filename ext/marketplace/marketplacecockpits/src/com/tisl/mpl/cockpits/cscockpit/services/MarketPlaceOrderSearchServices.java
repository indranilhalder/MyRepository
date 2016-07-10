/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.services;

import java.util.Collection;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author 1006687
 *
 */
public interface MarketPlaceOrderSearchServices {
	
	public Collection<OrderModel> getAssociatedOrders(String orderCode);

}
