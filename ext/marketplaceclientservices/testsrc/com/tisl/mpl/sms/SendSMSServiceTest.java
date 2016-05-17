/**
 *
 */
package com.tisl.mpl.sms;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.data.SendSMSRequestData;


/**
 * @author TCS
 *
 */
/**
 * JUnit test suite for {@link SendSMSService}
 */
@UnitTest
public class SendSMSServiceTest
{
	@Mock
	private MplSendSMSService sendSMSService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		this.sendSMSService = new MplSendSMSService();
	}

	@Test
	public void sendSMS()
	{

		final SendSMSRequestData sendSMSRequestData = new SendSMSRequestData();
		sendSMSRequestData.setContent("");//TODO : Please enter content
		sendSMSRequestData.setRecipientPhoneNumber("");//TODO : Please enter phn number
		sendSMSRequestData.setStatus("");//TODO : Please enter status
		//sendSMSService.sendSMS(sendSMSRequestData);

	}




}
