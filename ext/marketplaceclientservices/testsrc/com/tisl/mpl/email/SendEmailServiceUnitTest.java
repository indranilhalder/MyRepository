/**
 *
 */
package com.tisl.mpl.email;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.data.SendEmailRequestData;



/**
 * @author TCS
 *
 */
public class SendEmailServiceUnitTest
{

	private MplSendEmailService sendEmailService;

	private SendEmailRequestData request;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.sendEmailService = new MplSendEmailService();
		this.request = Mockito.mock(SendEmailRequestData.class);

	}

	@Test
	public void testSendEmail()
	{
		Mockito.when(request.getSenderEmail()).thenReturn("sender@abc.com");
		Mockito.when(request.getSenderName()).thenReturn("Sender");
		Mockito.when(request.getRecipientEmail()).thenReturn("recipient@abc.com");
		Mockito.when(request.getCcEmail()).thenReturn("ccemail@abc.com");
		Mockito.when(request.getBccEmail()).thenReturn("bccemail@abc.com");
		Mockito.when(request.getSubject()).thenReturn("Test Subject");
		Mockito.when(request.getContent()).thenReturn("Test Content");

		sendEmailService.sendEmail(request);





	}
}
