package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.OrderWrapper;
import de.hybris.platform.integration.oms.adapter.OmsSyncAdapter;
import de.hybris.platform.omsorders.services.query.daos.SyncDao;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;



/**
 * @author TCS
 */


public class CustomOmsOrderSyncAdapter implements OmsSyncAdapter<Order, OrderModel>
{
	private OmsSyncAdapter<OrderWrapper, ConsignmentModel> omsShipmentSyncAdapter;
	private ModelService modelService;
	private SyncDao<OrderModel> orderSyncDao;

	@Override
	public OrderModel update(final Order order, final Date orderUpdateTime)
	{
		final List<Order> sellerOrder = cloneSellerOrder(order);
		OrderModel orderModel = null;
		for (final Order sOrder : sellerOrder)
		{
			orderModel = orderSyncDao.findById("code", sOrder.getOrderId());
			orderModel.setOrderUpdateRemoteTime(orderUpdateTime);
			modelService.save(orderModel);
			if (!(OrderStatus.CANCELLING.equals(orderModel.getStatus())))
			{
				final OrderWrapper orderWrapper = new OrderWrapper(sOrder);
				this.omsShipmentSyncAdapter.update(orderWrapper, orderModel);

			}
		}

		return orderModel;
	}

	private List<Order> cloneSellerOrder(final Order order)
	{
		final Map<String, List<OrderLine>> sellerEntryMap = new HashMap<String, List<OrderLine>>();
		final List<Order> sellerOrders = new ArrayList<Order>();
		for (final OrderLine orderLine : order.getOrderLines())
		{
			List<OrderLine> entryList = new ArrayList<OrderLine>();
			if (sellerEntryMap.get(orderLine.getSellerOrderId()) != null)
			{
				entryList = sellerEntryMap.get(orderLine.getSellerOrderId());
				entryList.add(orderLine);
			}
			else
			{
				entryList.add(orderLine);
			}
			sellerEntryMap.put(orderLine.getSellerOrderId(), entryList);
		}

		for (final Map.Entry<String, List<OrderLine>> entry : sellerEntryMap.entrySet())
		{

			sellerOrders.add(sellerOrder(order, entry.getValue(), entry.getKey()));
		}

		return sellerOrders;

	}

	/**
	 * @param value
	 */


	@SuppressWarnings("javadoc")
	private Order sellerOrder(final Order order, final List<OrderLine> orderLine, final String orderId)
	{

		final Order sellerOrder = new Order();
		sellerOrder.setOrderId(orderId);
		sellerOrder.setOrderLines(orderLine);
		sellerOrder.setCurrencyCode(order.getCurrencyCode());
		sellerOrder.setCustomerLocale(order.getCustomerLocale());
		sellerOrder.setEmailid(order.getEmailid());
		sellerOrder.setFirstName(order.getFirstName());
		sellerOrder.setIssueDate(order.getIssueDate());
		sellerOrder.setScheduledShippingDate(order.getScheduledShippingDate());
		sellerOrder.setLastName(order.getLastName());
		sellerOrder.setPaymentInfos(order.getPaymentInfos());
		sellerOrder.setShippingAddress(order.getShippingAddress());
		sellerOrder.setShippingFirstName(order.getShippingFirstName());
		sellerOrder.setShippingLastName(order.getShippingLastName());
		sellerOrder.setShippingMethod(order.getShippingMethod());
		return sellerOrder;
	}

	protected void processOrderStatus(@SuppressWarnings("unused") final OrderModel order, final OrderStatus newOrderStatus)
	{
		if (newOrderStatus == null)
		{
			return;
		}
		OrderStatus.CANCELLED.equals(newOrderStatus);
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setOrderSyncDao(final SyncDao<OrderModel> orderSyncDao)
	{
		this.orderSyncDao = orderSyncDao;
	}

	@Required
	public void setOmsShipmentSyncAdapter(final OmsSyncAdapter<OrderWrapper, ConsignmentModel> omsShipmentSyncAdapter)
	{
		this.omsShipmentSyncAdapter = omsShipmentSyncAdapter;
	}



	@Override
	public OrderModel update(final Order dto, final ItemModel parent)
	{
		return null;
	}

	/**
	 * @return the omsShipmentSyncAdapter
	 */
	public OmsSyncAdapter<OrderWrapper, ConsignmentModel> getOmsShipmentSyncAdapter()
	{
		return omsShipmentSyncAdapter;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the orderSyncDao
	 */
	public SyncDao<OrderModel> getOrderSyncDao()
	{
		return orderSyncDao;
	}


}
