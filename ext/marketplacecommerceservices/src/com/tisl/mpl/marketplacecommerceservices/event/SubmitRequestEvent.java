/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;



/**
 * @author TCS
 *
 */
public class SubmitRequestEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{

	/**
	 *
	 */
	private String message;
	private String orderCode;
	private EmailAttachmentModel emailAttachment;
	private String attachedFileName;
	private String issueDetails;
	private String customerEmailId;
	private String issueType;





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
	 * @return the customerEmailId
	 */
	public String getCustomerEmailId()
	{
		return customerEmailId;
	}

	/**
	 * @param customerEmailId
	 *           the customerEmailId to set
	 */
	public void setCustomerEmailId(final String customerEmailId)
	{
		this.customerEmailId = customerEmailId;
	}

	/**
	 * @return the issueType
	 */
	public String getIssueType()
	{
		return issueType;
	}

	/**
	 * @param issueType
	 *           the issueType to set
	 */
	public void setIssueType(final String issueType)
	{
		this.issueType = issueType;
	}

	/**
	 * @return the attachedFileName
	 */
	public String getAttachedFileName()
	{
		return attachedFileName;
	}

	/**
	 * @param attachedFileName
	 *           the attachedFileName to set
	 */
	public void setAttachedFileName(final String attachedFileName)
	{
		this.attachedFileName = attachedFileName;
	}

	/**
	 * @return the emailAttachment
	 */
	public EmailAttachmentModel getEmailAttachment()
	{
		return emailAttachment;
	}

	/**
	 * @param emailAttachment
	 *           the emailAttachment to set
	 */
	public void setEmailAttachment(final EmailAttachmentModel emailAttachment)
	{
		this.emailAttachment = emailAttachment;
	}

	//	public SubmitRequestEvent()
	//	{
	//		super();
	//	}

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


}
