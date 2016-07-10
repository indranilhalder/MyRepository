/**
 *
 */
package com.tisl.mpl.sms.facades;


/**
 * @author TCS
 * @description Interface for sendSMS
 * @param senderId
 * @param content
 * @param mobileNumber
 */



public interface SendSMSFacade



{
	void sendSms(String senderId, String content, String mobileNumber);

}
