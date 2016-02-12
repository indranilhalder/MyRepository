/**
 *
 */
package com.tisl.mpl.facade.account.test;

import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.sms.facades.impl.DefaultSendSMSFacade;


/**
 * @author 592217
 *
 */
public class DefaultSendSMSFacadeUnitTest
{

	private static final Logger LOG = Logger.getLogger(DefaultSendSMSFacade.class);
	@Mock
	private ModelService modelService;

	//private SendSMSRequestData smsRequestData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		//this.smsRequestData = Mockito.mock(SendSMSRequestData.class);

	}

	@Test
	public void testsendSms()
	{


		final SendSMSRequestData smsRequestData = new SendSMSRequestData();
		smsRequestData.setContent("This is msg");
		smsRequestData.setRecipientPhoneNumber("7896541236");


		Mockito.doNothing().when(modelService).save(smsRequestData);
		LOG.info("Method : sendEmail >>>>>>>");
		//	sendSMSService.sendSMS(smsRequestData);


	}


}
