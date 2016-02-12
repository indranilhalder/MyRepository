package com.tisl.mpl.cockpits.cscockpit.dao.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.dao.MarketPlaceOrderSearchDao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultMarketPlaceOrderSearchDao implements
		MarketPlaceOrderSearchDao {

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Collection<OrderModel> getAssociatedOrders(String orderCode)
			throws ModelNotFoundException {

		FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
				MarketplaceCockpitsConstants.QUERY_FIND_ASSOCIATED_ORDERS);

		searchQuery.addQueryParameter("orderId", "%" + orderCode.trim() + "%");
		searchQuery.addQueryParameter("currentOrderId", orderCode);
		SearchResult<OrderModel> orderModels = flexibleSearchService
				.search(searchQuery);
		return orderModels.getResult();
	}

}
