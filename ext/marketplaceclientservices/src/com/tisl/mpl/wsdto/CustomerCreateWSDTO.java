/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "Customer")
@XmlType(propOrder =
{ "custCreationFlag", "customerID", "emailID" })
public class CustomerCreateWSDTO
{
	private String custCreationFlag;
	private String customerID;
	private String emailID;

	/**
	 * @return the custCreationFlag
	 */
	@XmlElement(name = "custCreationFlag")
	public String getCustCreationFlag()
	{
		return custCreationFlag;
	}

	/**
	 * @param custCreationFlag
	 *           the custCreationFlag to set
	 */
	public void setCustCreationFlag(final String custCreationFlag)
	{
		this.custCreationFlag = custCreationFlag;
	}

	/**
	 * @return the customerID
	 */
	@XmlElement(name = "customerID")
	public String getCustomerID()
	{
		return customerID;
	}

	/**
	 * @param customerID
	 *           the customerID to set
	 */
	public void setCustomerID(final String customerID)
	{
		this.customerID = customerID;
	}

	/**
	 * @return the emailID
	 */
	@XmlElement(name = "emailID")
	public String getEmailID()
	{
		return emailID;
	}

	/**
	 * @param emailID
	 *           the emailID to set
	 */
	public void setEmailID(final String emailID)
	{
		this.emailID = emailID;
	}


}
