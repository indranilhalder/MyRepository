package com.tisl.mpl.integration.oms.order.service.impl;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.hybris.oms.domain.order.Order;


/**
 *
 * @author TCS
 *
 */
public class OrderToQueue
{
	private static final Logger LOG = Logger.getLogger(OrderToQueue.class);
	private JmsTemplate jmsTemplate;
	private CustomOmsOrderService omsOrderService;

	public void setJmsTemplate(final JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(final Order order)
	{
		LOG.debug("**Sending Order***" + order.getOrderId());

		//		jmsTemplate.send(new MessageCreator()
		//		{
		//			@Override
		//			public Message createMessage(final Session session) throws JMSException
		//			{
		//				final ObjectMessage message = session.createObjectMessage();
		//				message.setObject(order);
		//				return message;
		//			}
		//		});

		jmsTemplate.send(new MessageCreator()
		{
			@Override
			public Message createMessage(final Session session) throws JMSException
			{
				final TextMessage message = session.createTextMessage();
				final String orderXml = getOmsOrderService().getOrderAuditXml(order);
				LOG.debug("OrderToQueue : orderXml" + orderXml);
				message.setText(orderXml);
				return message;
			}
		});
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
