package com.tisl.mpl.cockpits.cscockpit.services;

import java.util.List;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

public interface AddressHistoryService {
	public abstract List<AddressModel> getHistoryEntries(OrderModel paramOrderModel);
}
