/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

/**
 * @author Techouts
 * 
 * Form Object for TrackOrder
 */
public class TrackOrderForm
{

	private String orderCode;
	private String emailId;
	private String captcha;

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId()
	{
		return emailId;
	}

	/**
	 * @param emailId
	 *           the emailId to set
	 */
	public void setEmailId(final String emailId)
	{
		this.emailId = emailId;
	}

	/**
	 * @return the captcha
	 */
	public String getCaptcha()
	{
		return captcha;
	}

	/**
	 * @param captcha
	 *           the captcha to set
	 */
	public void setCaptcha(final String captcha)
	{
		this.captcha = captcha;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "TrackOrderForm [orderCode=" + orderCode + ", emailId=" + emailId + ", captcha=" + captcha + "]";
	}

	
	

}
