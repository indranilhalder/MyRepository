package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.dao.MarketPlaceOrderSearchDao;
import com.tisl.mpl.cockpits.cscockpit.services.MarketPlaceOrderSearchServices;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

public class DefaultMarketPlaceOrderSearchServices implements
		MarketPlaceOrderSearchServices {

	@Autowired
	private MarketPlaceOrderSearchDao marketPlaceOrderSearchDao;

	@Override
	public Collection<OrderModel> getAssociatedOrders(String orderCode) {
		try {
			return marketPlaceOrderSearchDao.getAssociatedOrders(orderCode);
		} catch (ModelNotFoundException e) {
			return CollectionUtils.EMPTY_COLLECTION;
		}
	}

}
