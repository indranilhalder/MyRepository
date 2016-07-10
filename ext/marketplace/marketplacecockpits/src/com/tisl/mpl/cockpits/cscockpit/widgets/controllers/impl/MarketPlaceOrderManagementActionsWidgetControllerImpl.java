package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.ChangeDeliveryAddressFacade;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderManagementActionsWidgetController;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplChangeDeliveryAddressService;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class MarketPlaceOrderManagementActionsWidgetControllerImpl extends DefaultOrderManagementActionsWidgetController implements MarketPlaceOrderManagementActionsWidgetController{
	
	@Autowired
	private ChangeDeliveryAddressFacade changeDeliveryAddressFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Override
	public boolean isChangeDeliveryAddressPossible(TypedObject orderObject) {
		// TODO Auto-generated method stub
		AbstractOrderModel orderModel = (AbstractOrderModel) orderObject
				.getObject();
		List<String> nonChangableOrdeStatus = Arrays.asList(
				OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(),
		OrderStatus.PICK_LIST_GENERATED.getCode(),
		OrderStatus.PICK_CONFIRMED.getCode());
		boolean changable = Boolean.FALSE;
		if(null !=orderModel.getPaymentMode() && !orderModel.getPaymentMode().toString().equalsIgnoreCase("COD")) 
		{
			changable = true;
		}
		try {
		for(AbstractOrderEntryModel entry : orderModel.getEntries() )
		{
			
		  ConsignmentModel consignment= entry.getConsignmentEntries().iterator().next().getConsignment();
		  if(!nonChangableOrdeStatus.contains(consignment.getCode()))
		  {
			  changable= false;
		  }
		}
		}catch(Exception e) 
		{
			
		}
		return changable;
	}

	
	@Override
	public void ticketCreateToCrm(OrderModel Order, String customerId,
			String source) {
		changeDeliveryAddressFacade.createcrmTicketForChangeDeliveryAddress(Order, customerId,source);
		// TODO Auto-generated method stub
		
	}
	
	public boolean changeDeliveryAddressCallToOMS(String orderId,AddressModel newDeliveryAddress) 
	{
		return changeDeliveryAddressFacade.changeDeliveryRequestToOMS(orderId, newDeliveryAddress);
		
	}

}
