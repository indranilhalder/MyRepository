/**
 *
 */
package com.tisl.mpl.pojo;

/**
 * @author 985717
 *
 */
public class BulkSmsDto
{
	private String message;
	private long mobileNumber;


	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}

	/**
	 * @return the mobileNumber
	 */
	public long getMobileNumber()
	{
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *           the mobileNumber to set
	 */
	public void setMobileNumber(final long mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

}
