package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.util.List;

import com.tisl.mpl.cockpits.cscockpit.services.AddressHistoryService;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;

public class AddressHistoryServiceimpl implements AddressHistoryService{
	public List<AddressModel> getHistoryEntries(OrderModel OrderModel) {
		if (OrderModel == null) {
			throw new JaloInvalidParameterException(
					"Missing ORDER for getting history entries ", 0);
		}
		return OrderModel.getDeliveryAddresses();
	}
}
