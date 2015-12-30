package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.strategies.impl.DefaultCustomerOrdersStrategy;

public class MplDefaultCustomerOrdersStrategy extends
		DefaultCustomerOrdersStrategy {

	@Override
	public Collection<OrderModel> getCustomerOrders(CustomerModel customer) {
		// TODO Auto-generated method stub
		List<OrderModel> lst =  new ArrayList<OrderModel>(super.getCustomerOrders(customer));
		
	      Collections.sort(lst, new Comparator<OrderModel>()

	      {
	        public int compare(OrderModel event1, OrderModel event2)
	        {
	          return event2.getCreationtime().compareTo(event1.getCreationtime());


	        }
	      });
	      return lst;
	}
}
