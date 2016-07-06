package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.ChangeDeliveryAddressFacade;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.facades.account.register.MplOrderFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;

public class MarketPlaceChangeDeliveryAddressControllerImpl extends DefaultOrderManagementActionsWidgetController implements MarketPlaceChangeDeliveryAddressController{
	private static final Logger LOG = Logger.getLogger(MarketPlaceChangeDeliveryAddressControllerImpl.class);
	@Autowired
	private ChangeDeliveryAddressFacade changeDeliveryAddressFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Override
	public boolean isDeliveryAddressChangable(TypedObject orderObject) {
		OrderModel orderModel = (OrderModel) orderObject.getObject();
		boolean changable = false;
		try {
			changable = changeDeliveryAddressFacade
					.isDeliveryAddressChangable(orderModel);
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getCause());
		}

		return changable;
	}

	
	@Override
	public void ticketCreateToCrm(OrderModel Order, String customerId,
			String source) {
		changeDeliveryAddressFacade.createcrmTicketForChangeDeliveryAddress(Order, customerId,source);
		
	}
	
	public boolean changeDeliveryAddressCallToOMS(String orderId,AddressModel newDeliveryAddress) 
	{
		return changeDeliveryAddressFacade.changeDeliveryRequestToOMS(orderId, newDeliveryAddress);
		
	}

}
