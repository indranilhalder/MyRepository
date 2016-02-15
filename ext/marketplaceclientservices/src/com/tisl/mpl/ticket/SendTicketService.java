/**
 *
 */
package com.tisl.mpl.ticket;

import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteAccessException;

import com.tisl.mpl.data.SendTicketRequestData;


/**
 * @author TCS
 *
 */
public class SendTicketService
{
	private static final Logger LOG = Logger.getLogger(SendTicketService.class);

	public boolean sendTicketAction(final SendTicketRequestData request)
	{
		boolean flag = false;
		try
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("*************************************************************");
				LOG.info("Sending Ticket using below details:");
				LOG.info("Email ID = " + request.getEmailId());
				LOG.info("Order ID = " + request.getOrderId());
				LOG.info("Type of Action = " + request.getActionType());
				//				LOG.info("Current Order Status = " + request.getCurrentOrderStatus());
			}

			if (!request.getOrderId().isEmpty())
			{
				//Call Webservice from here
				//Print request
				request.setTicketStatus("SUCCESS");
				LOG.info(request.getActionType() + " request is successfully logged for order number " + request.getOrderId()
						+ " of user " + request.getEmailId());
				LOG.info("*************************************************************");
			}
			else
			{
				request.setTicketStatus("FAILURE");
			}
		}
		catch (final RemoteAccessException rae)
		{
			request.setTicketStatus("FAILURE");
			LOG.error(rae.getMessage(), rae);
		}
		if (request.getTicketStatus().equalsIgnoreCase("SUCCESS"))
		{
			flag = true;
		}
		/*
		 * else { return false; }
		 */
		return flag;
	}

}
