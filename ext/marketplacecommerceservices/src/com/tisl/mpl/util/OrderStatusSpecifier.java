/*
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class OrderStatusSpecifier
{
	private static final Logger LOG = Logger.getLogger(OrderStatusSpecifier.class);

	@Autowired
	private ModelService modelService;
	@Autowired
	private OrderHistoryService orderHistoryService;

	public void setOrderStatus(final OrderModel order, final OrderStatus orderStatus)
	{
		boolean flag = false;
		LOG.debug("Order id is " + order.getCode());

		boolean statusChangeEligible = checkStatusChangeEligibility(order, orderStatus);
		
		final List<OrderModel> subOrderList = order.getChildOrders();
		if (CollectionUtils.isNotEmpty(subOrderList))
		{
			for (final OrderModel subOrder : subOrderList)
			{
				int quantity = 0;
				final List<AbstractOrderEntryModel> subOrderEntryList = subOrder.getEntries();
				if (CollectionUtils.isNotEmpty(subOrderEntryList))
				{
					quantity = countQuantity(subOrderEntryList, quantity);
				}

				flag = addOrderHistory(quantity, order, orderStatus, subOrderEntryList, subOrder, flag);

			}
			LOG.debug("Sub ORder status changed " + flag);
		}
		//TPR-1081
		//SDI-2922
		else if (statusChangeEligible)
		{
			flag = addOrderHistory(1, order, orderStatus, order.getEntries(), order, flag);
		}
		//if (flag || orderStatus.equals(OrderStatus.PAYMENT_PENDING)) //flag == true
		//{
		//SDI-2922
		if (flag && statusChangeEligible)
		{
			order.setStatus(orderStatus);
			getModelService().save(order);
		}
		if (null != order.getStatus())
		{
			LOG.debug("Status of the order is :::::::::::::::" + order.getStatus().toString());
		}
		else
		{
			LOG.error(order.getCode() + " is erroneous order as it does not have any Order Status");
		}
	}



	/**
	 * This method adds Order History entry based on condition
	 *
	 * @param quantity
	 * @param order
	 * @param orderStatus
	 * @param subOrderEntryList
	 * @param subOrder
	 * @param flag
	 * @return boolean
	 */
	private boolean addOrderHistory(final int quantity, final OrderModel order, final OrderStatus orderStatus,
			final List<AbstractOrderEntryModel> subOrderEntryList, final OrderModel subOrder, boolean flag)
	{
		if (quantity > 0)
		{
			//final List<OrderHistoryEntryModel> historyEntryList = new ArrayList<OrderHistoryEntryModel>();
			if (null != order.getStatus() && !orderStatus.equals(order.getStatus()))
			{
				try
				{
					createOrderHistoryEntry(subOrderEntryList, subOrder, orderStatus);
				}
				catch (final ModelSavingException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.ORDERHISTORY_CREATE_ERROR + e);
				}
				catch (final Exception e)
				{
					LOG.error(MarketplacecommerceservicesConstants.ORDERHISTORY_CREATE_ERROR + e);
				}

			}

			else if (null == order.getStatus())
			{
				try
				{
					createOrderHistoryEntry(subOrderEntryList, subOrder, orderStatus);
				}
				catch (final ModelSavingException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.ORDERHISTORY_CREATE_ERROR + e);
				}
				catch (final Exception e)
				{
					LOG.error(MarketplacecommerceservicesConstants.ORDERHISTORY_CREATE_ERROR + e);
				}

			}
		}

		subOrder.setStatus(orderStatus);
		getModelService().save(subOrder);
		flag = true;

		return flag;
	}



	/**
	 * This method creates order history and attaches it to suborder
	 *
	 * @param subOrderEntryList
	 * @param subOrder
	 * @param orderStatus
	 */
	//TISEE-405
	private void createOrderHistoryEntry(final List<AbstractOrderEntryModel> subOrderEntryList, final OrderModel subOrder,
			final OrderStatus orderStatus)
	{
		try
		{
			for (final AbstractOrderEntryModel subOrderEntry : subOrderEntryList)
			{
				if (subOrderEntry.getQuantity().doubleValue() > 0)
				{
					final OrderHistoryEntryModel historyEntryModel = getModelService().create(OrderHistoryEntryModel.class);
					historyEntryModel.setDescription(orderStatus.toString());
					historyEntryModel.setLineId(subOrderEntry.getTransactionID());
					historyEntryModel.setOrder(subOrder);
					historyEntryModel.setTimestamp(new Date());
					getModelService().save(historyEntryModel);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			throw e;
		}
	}



	/**
	 * This method counts the quantity of orderentry in sub-order
	 *
	 * @param subOrderEntryList
	 * @param quantity
	 * @return int
	 */
	private int countQuantity(final List<AbstractOrderEntryModel> subOrderEntryList, int quantity)
	{
		for (final AbstractOrderEntryModel subOrderEntry : subOrderEntryList)
		{
			if (subOrderEntry.getQuantity().doubleValue() > 0)
			{
				quantity++;
			}
		}

		return quantity;
	}
	
	private boolean checkStatusChangeEligibility(final OrderModel order, final OrderStatus orderStatus)
	{
		if (order.getType().equals(MarketplacecommerceservicesConstants.SUBORDER))
		{
			return true;
		}
		else if (orderStatus.equals(OrderStatus.PAYMENT_SUCCESSFUL) || orderStatus.equals(OrderStatus.PAYMENT_PENDING)
				|| orderStatus.equals(OrderStatus.PAYMENT_FAILED) || orderStatus.equals(OrderStatus.PAYMENT_TIMEOUT)
				|| orderStatus.equals(OrderStatus.CHECKED_INVALID) || orderStatus.equals(OrderStatus.RMS_VERIFICATION_PENDING)
				|| orderStatus.equals(OrderStatus.RMS_VERIFICATION_FAILED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the orderHistoryService
	 */
	public OrderHistoryService getOrderHistoryService()
	{
		return orderHistoryService;
	}

	/**
	 * @param orderHistoryService
	 *           the orderHistoryService to set
	 */
	public void setOrderHistoryService(final OrderHistoryService orderHistoryService)
	{
		this.orderHistoryService = orderHistoryService;
	}


}
