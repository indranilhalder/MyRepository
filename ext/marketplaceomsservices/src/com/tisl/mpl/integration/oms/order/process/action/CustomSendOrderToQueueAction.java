/**
 *
 */
package com.tisl.mpl.integration.oms.order.process.action;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

import org.apache.log4j.Logger;

import com.hybris.oms.domain.order.Order;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.integration.oms.order.service.impl.OrderToQueue;


/**
 * @author TCS
 *
 */
public class CustomSendOrderToQueueAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CustomSendOrderToQueueAction.class);
	private OrderToQueue mplOrderToQueue;
	private CustomOmsOrderService omsOrderService;

	/**
	 * @desc It will send orders to queue as part of OMS fallback mechanism
	 * @param orderProcessModel
	 * @return Transition
	 * @throws Exception
	 */

	@Override
	public Transition executeAction(final OrderProcessModel orderProcessModel) throws Exception
	{
		LOG.debug("Inside CustomSendOrderToQueueAction class............... ");
		final OrderModel orderModel = orderProcessModel.getOrder();
		LOG.debug("********Sending to order Queue with id::**********" + orderModel.getCode());

		Transition transitionStatus = AbstractSimpleDecisionAction.Transition.OK;

		Order order = null;
		//converting from order model to order dto
		order = getOmsOrderService().getOrderConverter().convert(orderModel);

		if (order == null)
		{
			transitionStatus = AbstractSimpleDecisionAction.Transition.NOK;
		}

		LOG.debug("Before sending order to queue  :::: " + order.getOrderId());

		try
		{
			mplOrderToQueue.sendMessage(order);
		}
		catch (final Exception ex)
		{
			LOG.error(" Exception occured while posting order to Queue in CustomSendOrderToQueueAction ", ex);
			transitionStatus = AbstractSimpleDecisionAction.Transition.NOK;
		}

		return transitionStatus;
	}


	/**
	 * @return the mplOrderToQueue
	 */
	public OrderToQueue getMplOrderToQueue()
	{
		return mplOrderToQueue;
	}


	/**
	 * @param mplOrderToQueue
	 *           the mplOrderToQueue to set
	 */
	public void setMplOrderToQueue(final OrderToQueue mplOrderToQueue)
	{
		this.mplOrderToQueue = mplOrderToQueue;
	}

	/**
	 * @return the omsOrderService
	 */
	public CustomOmsOrderService getOmsOrderService()
	{
		return omsOrderService;
	}

	/**
	 * @param omsOrderService
	 *           the omsOrderService to set
	 */
	public void setOmsOrderService(final CustomOmsOrderService omsOrderService)
	{
		this.omsOrderService = omsOrderService;
	}


}
