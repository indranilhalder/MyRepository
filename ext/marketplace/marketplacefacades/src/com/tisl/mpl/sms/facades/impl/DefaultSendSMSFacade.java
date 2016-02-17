/**
 *
 */
package com.tisl.mpl.sms.facades.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.sms.MplSendSMSService;
import com.tisl.mpl.sms.facades.SendSMSFacade;


/*
 *  @author TCS
 */


public class DefaultSendSMSFacade implements SendSMSFacade
{


	@Autowired
	private MplSendSMSService sendSMSService;

	/**
	 * @description Method is called to implement SMS Service
	 * @param senderId
	 * @param content
	 * @param mobileNumber
	 * @throws EtailNonBusinessExceptions
	 */


	@Override
	public void sendSms(final String senderId, final String content, final String mobileNumber)
	{
		try
		{
			final SendSMSRequestData smsRequestData = new SendSMSRequestData();
			smsRequestData.setSenderID(senderId);
			smsRequestData.setContent(content);
			smsRequestData.setRecipientPhoneNumber(mobileNumber);
			sendSMSService.sendSMS(smsRequestData);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


}
