/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class SendSmsWsDTO
{
	String Sender_ID;
	String Mobile_No;
	String Message;

	/**
	 * @return the sender_ID
	 */
	public String getSender_ID()
	{
		return Sender_ID;
	}

	/**
	 * @param sender_ID
	 *           the sender_ID to set
	 */
	public void setSender_ID(final String sender_ID)
	{
		Sender_ID = sender_ID;
	}

	/**
	 * @return the mobile_No
	 */
	public String getMobile_No()
	{
		return Mobile_No;
	}

	/**
	 * @param mobile_No
	 *           the mobile_No to set
	 */
	public void setMobile_No(final String mobile_No)
	{
		Mobile_No = mobile_No;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return Message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		Message = message;
	}
}
