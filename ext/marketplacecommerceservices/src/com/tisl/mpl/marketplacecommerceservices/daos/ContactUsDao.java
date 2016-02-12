package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;


public interface ContactUsDao
{
	public List<OrderModel> getOrderModel(String orderCode);
}