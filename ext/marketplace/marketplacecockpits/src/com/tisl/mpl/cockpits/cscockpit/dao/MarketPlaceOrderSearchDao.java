package com.tisl.mpl.cockpits.cscockpit.dao;

import java.util.Collection;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;


public interface MarketPlaceOrderSearchDao {
	
	public Collection<OrderModel> getAssociatedOrders(String orderCode) throws ModelNotFoundException;

}
