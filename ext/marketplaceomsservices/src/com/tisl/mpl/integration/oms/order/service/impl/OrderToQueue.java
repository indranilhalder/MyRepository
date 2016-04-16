package com.tisl.mpl.integration.oms.order.service.impl;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


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

	public void sendMessage(final String order)
	{

		LOG.debug("**Sending Order***" + order);
		jmsTemplate.send(new MessageCreator()
		{

			public Message createMessage(final Session session) throws JMSException
			{

				final TextMessage message = session.createTextMessage(order);
				return message;
			}

		});

	}
}
