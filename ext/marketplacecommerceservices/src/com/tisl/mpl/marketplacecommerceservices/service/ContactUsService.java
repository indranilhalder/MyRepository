package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;



public interface ContactUsService
{
	public List<OrderModel> getOrderModel(String orderCode);
}