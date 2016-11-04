package com.tisl.mpl.cockpits.cscockpit.widgets.adapter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.cockpits.cscockpit.services.AddressHistoryService;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.AddressHistoryListWidgetModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.adapters.AbstractInitialisingWidgetAdapter;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.orderhistory.OrderHistoryService;

public class MplOrderDeliveryAddressHistoryWidgetAdapter
		extends
		AbstractInitialisingWidgetAdapter<AddressHistoryListWidgetModel, OrderManagementActionsWidgetController> {
	
	@Autowired
	private AddressHistoryService addressHistoryService;
	private OrderHistoryService orderHistoryService;

	protected OrderHistoryService getOrderHistoryService() {
		return orderHistoryService;
	}

	@Required
	public void setOrderHistoryService(OrderHistoryService orderHistoryService) {
		this.orderHistoryService = orderHistoryService;
	}

	protected boolean updateModel() {
		boolean changed = false;
		TypedObject order = ((OrderController) getWidgetController())
				.getCurrentOrder();
		if (order != null) {
			OrderModel orderModel = (OrderModel) order.getObject();

			List<TypedObject> addressHistory = UISessionUtils
					.getCurrentSession()
					.getTypeService()
					.wrapItems(
							new ArrayList(addressHistoryService.getHistoryEntries(orderModel)));

			changed |= ((AddressHistoryListWidgetModel) getWidgetModel())
					.setItems(addressHistory);
			changed |= ((AddressHistoryListWidgetModel) getWidgetModel())
					.setOrder(order);
		}

		return changed;
	}
}
