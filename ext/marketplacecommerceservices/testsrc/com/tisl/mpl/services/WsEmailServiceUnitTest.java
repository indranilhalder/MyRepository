package com.tisl.mpl.services;

import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.data.SendEmailRequestData;


public class WsEmailServiceUnitTest
{

	@Mock
	private ModelService modelService;

	private SendEmailRequestData sendEmailRequestData;

	private static final Logger LOG = Logger.getLogger(WsEmailServiceUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.sendEmailRequestData = Mockito.mock(SendEmailRequestData.class);

	}

	@Test
	public void sendEmail()
	{
		final String senderEmail = "priya2@tcs.com";
		final String senderName = "Priyanka";
		final String recipientEmail = "Welcome@tcs.com";
		final String replyTo = "priya2@tcs.com";
		final String subject = "Email Sending";
		final String content = "Mail has been sent";

		sendEmailRequestData.setSenderEmail(senderEmail);
		sendEmailRequestData.setSenderName(senderName);
		sendEmailRequestData.setRecipientEmail(recipientEmail);
		sendEmailRequestData.setReplyTo(replyTo);
		sendEmailRequestData.setSubject(subject);
		sendEmailRequestData.setContent(content);
		Mockito.doNothing().when(modelService).save(sendEmailRequestData);
		LOG.info("Method : sendEmail >>>>>>>");
	}
}
