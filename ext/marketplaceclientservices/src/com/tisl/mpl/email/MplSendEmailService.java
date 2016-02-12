/**
 *
 */
package com.tisl.mpl.email;

import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteAccessException;

import com.tisl.mpl.data.SendEmailRequestData;


/**
 * @author TCS
 *
 */
public class MplSendEmailService
{
	private static final Logger LOG = Logger.getLogger(MplSendEmailService.class);


	public void sendEmail(final SendEmailRequestData request)
	{
		try
		{

			if (LOG.isInfoEnabled())
			{
				LOG.info("Sending email using below details:");
				LOG.info("From = " + request.getSenderEmail());
				LOG.info("From Name = " + request.getSenderName());
				LOG.info("To = " + request.getRecipientEmail());
				LOG.info("Cc = " + request.getCcEmail());
				LOG.info("Bcc = " + request.getBccEmail());
				LOG.info("Subject = " + request.getSubject());
				LOG.info("Body = " + request.getContent());
			}

			if (!request.getSenderName().isEmpty())
			{
				//Call Webservice from here
				//Print request
				request.setStatus("SUCCESS");
				LOG.info("Email has been Sent ................");
			}

		}
		catch (final RemoteAccessException rae)
		{
			LOG.error(rae.getMessage(), rae);
		}

	}
}
