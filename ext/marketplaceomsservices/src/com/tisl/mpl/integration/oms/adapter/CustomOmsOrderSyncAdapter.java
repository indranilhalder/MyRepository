package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.OrderWrapper;
import de.hybris.platform.integration.oms.adapter.DefaultOmsOrderSyncAdapter;
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


public class CustomOmsOrderSyncAdapter extends DefaultOmsOrderSyncAdapter
{
	private CustomOmsSyncAdapter<OrderWrapper, ConsignmentModel> omsShipmentSyncAdapterCustom;
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
				getOmsShipmentSyncAdapterCustom().update(orderWrapper, orderModel);

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

	@Override
	protected void processOrderStatus(@SuppressWarnings("unused") final OrderModel order, final OrderStatus newOrderStatus)
	{
		if (newOrderStatus == null)
		{
			return;
		}
		OrderStatus.CANCELLED.equals(newOrderStatus);
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	@Required
	public void setOrderSyncDao(final SyncDao<OrderModel> orderSyncDao)
	{
		this.orderSyncDao = orderSyncDao;
	}



	@Override
	public OrderModel update(final Order dto, final ItemModel parent)
	{
		return null;
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

	/**
	 * @return the omsShipmentSyncAdapterCustom
	 */
	public CustomOmsSyncAdapter<OrderWrapper, ConsignmentModel> getOmsShipmentSyncAdapterCustom()
	{
		return omsShipmentSyncAdapterCustom;
	}

	/**
	 * @param omsShipmentSyncAdapterCustom
	 *           the omsShipmentSyncAdapterCustom to set
	 */
	@Required
	public void setOmsShipmentSyncAdapterCustom(
			final CustomOmsSyncAdapter<OrderWrapper, ConsignmentModel> omsShipmentSyncAdapterCustom)
	{
		this.omsShipmentSyncAdapterCustom = omsShipmentSyncAdapterCustom;
	}


}
