package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.cockpits.cscockpit.data.RefundDeliveryData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;

public interface MarketPlaceOrderController extends OrderController {

	public List<TypedObject> getAssociatedOrders();

	public CallContextController getContextController();

	public List<TypedObject> getPreviousOrders();

	public String doRefundPayment(List<OrderEntryModel> orderEntryModel);

	public TypedObject createRefundDeliveryChargesRequest(OrderModel object,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap);

	public TypedObject createOrderHistoryRequest(OrderModel object,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap,
			Double totalRefundDeliveryCharges);

	public boolean isOrderCODforDeliveryCharges(OrderModel order,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap);

	public Map<String, List<TypedObject>> getOrderEntriesGroupByAWB(
			OrderModel orderModel);

	public boolean sendInvoice(List<TypedObject> orderLineId, String orderId);

	public boolean isOrderCODforManualRefund(OrderModel object,
			List<OrderEntryModel> manualRefundList);

}
