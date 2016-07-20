package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.facades.data.PincodeData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

public interface MarketPlaceChangeDeliveryAddressController {
	public abstract boolean isDeliveryAddressChangable(TypedObject orderObject);

	public abstract void ticketCreateToCrm(OrderModel Order, String customerId,
			String source);

	public abstract boolean changeDeliveryAddressCallToOMS(String code,
			AddressModel newDeliveryAddress);

	public TemproryAddressModel getTempororyAddress(String orderId);

	public void saveDeliveryAddress(OrderModel order, AddressModel address);
	public PincodeData getPincodeData(String pincode);

}
