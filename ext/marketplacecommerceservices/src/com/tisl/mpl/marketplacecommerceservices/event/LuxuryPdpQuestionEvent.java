/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;



/**
 * @author TCS
 *
 */
public class LuxuryPdpQuestionEvent extends AbstractCommerceUserEvent<BaseSiteModel>
{
	/**
	 *
	 */
	private String message;
	private String productCode;
	private String attachedFileName;
	private String issueDetails;
	private String customerEmailId;
	private String issueType;
	private String emailTo;

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
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
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
	 * @return the emailTo
	 */
	public String getEmailTo()
	{
		return emailTo;
	}

	/**
	 * @param emailTo
	 *           the emailTo to set
	 */
	public void setEmailTo(final String emailTo)
	{
		this.emailTo = emailTo;
	}



}
