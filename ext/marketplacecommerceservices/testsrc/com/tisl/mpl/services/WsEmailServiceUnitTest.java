package com.tisl.mpl.services;

import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
		//TISSEC-50
		final String senderEmail = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter sender email
		final String senderName = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter sender name
		final String recipientEmail = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter recipient email
		final String replyTo = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter reply to email
		final String subject = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter subject
		final String content = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter content

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
