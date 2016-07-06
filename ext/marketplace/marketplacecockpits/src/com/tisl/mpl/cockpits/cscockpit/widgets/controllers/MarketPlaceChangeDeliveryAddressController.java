package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;

public interface MarketPlaceChangeDeliveryAddressController extends OrderManagementActionsWidgetController{
	public abstract boolean isDeliveryAddressChangable(TypedObject orderObject);
	public abstract void ticketCreateToCrm(OrderModel Order,String customerId,String source);
	public abstract boolean changeDeliveryAddressCallToOMS(String code,AddressModel newDeliveryAddress);

}
