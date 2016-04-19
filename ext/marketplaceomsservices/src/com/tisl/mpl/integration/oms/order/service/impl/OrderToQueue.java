package com.tisl.mpl.integration.oms.order.service.impl;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

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

	public void setJmsTemplate(final JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(final Order order)
	{
		LOG.debug("**Sending Order***" + order.getOrderId());
		jmsTemplate.send(new MessageCreator()
		{
			@Override
			public Message createMessage(final Session session) throws JMSException
			{
				final ObjectMessage message = session.createObjectMessage();
				message.setObject(order);
				return message;
			}
		});

	}
}
