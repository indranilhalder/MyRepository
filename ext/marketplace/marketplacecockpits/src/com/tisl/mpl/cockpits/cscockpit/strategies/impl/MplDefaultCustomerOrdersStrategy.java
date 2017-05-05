package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.strategies.impl.DefaultCustomerOrdersStrategy;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MplDefaultCustomerOrdersStrategy extends
		DefaultCustomerOrdersStrategy {

	@Resource
	private ConfigurationService configurationService;
	
	@Resource
	private OrderModelService orderModelService;
	
	@Override
	public Collection<OrderModel> getCustomerOrders(CustomerModel customer) {
		// TODO Auto-generated method stub
		List<OrderModel> lst =  new ArrayList<OrderModel>(getCustomerOrdersReloaded(customer));
		
	      Collections.sort(lst, new Comparator<OrderModel>()

	      {
	        public int compare(OrderModel event1, OrderModel event2)
	        {
	          return event2.getCreationtime().compareTo(event1.getCreationtime());


	        }
	      });
	      return lst;
	}
	
	protected Collection<OrderModel> getCustomerOrdersReloaded(CustomerModel customer)
	{
	  Collection<OrderModel> orders = new ArrayList<OrderModel>();
	  if (customer != null)
	  {
		List<OrderModel> orderList = null;
		if (isUserInRole(configurationService
				.getConfiguration()
				.getString(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERGROUP))) {
			final String agentId = (String) JaloSession.getCurrentSession().getAttribute("sellerId");
			orderList = orderModelService.getOrderByAgent(customer, agentId);
		}
		else{
			orderList = (List<OrderModel>) customer.getOrders();
		}
	    for (OrderModel order : orderList)
	    {
	      if (order.getOriginalVersion() != null)
	        continue;
	      orders.add(order);
	    }
	  }

	  return orders;
	}

	private boolean isUserInRole(String groupName) {
		Set<PrincipalGroupModel> userGroups = UISessionUtils
				.getCurrentSession().getUser().getAllGroups();

		for (PrincipalGroupModel ug : userGroups) {
			if (ug.getUid().equalsIgnoreCase(groupName)) {
				return true;
			}
		}
		return false;
	}
}
