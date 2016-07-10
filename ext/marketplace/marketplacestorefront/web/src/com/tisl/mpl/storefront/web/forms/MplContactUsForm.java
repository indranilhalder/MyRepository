/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author TCS
 *
 */
public class MplContactUsForm
{
	private String orderDetails;
	private String emailId;
	private String issueDetails;

	/**
	 * @return the issueDetails
	 */
	public String getIssueDetails()
	{
		return issueDetails;
	}

	/**
	 * @param issueDetails
	 *           the issueDetails to set
	 */
	public void setIssueDetails(final String issueDetails)
	{
		this.issueDetails = issueDetails;
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
	 * @return the orderDetails
	 */
	public String getOrderDetails()
	{
		return orderDetails;
	}

	/**
	 * @param orderDetails
	 *           the orderDetails to set
	 */
	public void setOrderDetails(final String orderDetails)
	{
		this.orderDetails = orderDetails;
	}

	/**
	 * @return the file
	 */
	public MultipartFile getFile()
	{
		return file;
	}

	/**
	 * @param file
	 *           the file to set
	 */
	public void setFile(final MultipartFile file)
	{
		this.file = file;
	}

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

	private MultipartFile file;
	private String message;


}
