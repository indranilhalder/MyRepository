/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlSeeAlso(ClickToCallWsDTO.class)
@XmlRootElement(name = "ClickToCall")
@XmlType(propOrder =
{ "customerName", "customerEmail", "customerMobile", "reasonToCall" })
public class ClickToCallWsDTO
{

	private String customerName;
	private String customerEmail;
	private String customerMobile;
	private String reasonToCall;

	/**
	 * @return the customerName
	 */
	@XmlElement(name = "customerName")
	public String getCustomerName()
	{
		return customerName;
	}

	/**
	 * @param customerName
	 *           the customerName to set
	 */
	public void setCustomerName(final String customerName)
	{
		this.customerName = customerName;
	}

	/**
	 * @return the customerEmail
	 */
	@XmlElement(name = "customerEmail")
	public String getCustomerEmail()
	{
		return customerEmail;
	}

	/**
	 * @param customerEmail
	 *           the customerEmail to set
	 */
	public void setCustomerEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	/**
	 * @return the customerMobile
	 */
	@XmlElement(name = "customerMobile")
	public String getCustomerMobile()
	{
		return customerMobile;
	}

	/**
	 * @param customerMobile
	 *           the customerMobile to set
	 */
	public void setCustomerMobile(final String customerMobile)
	{
		this.customerMobile = customerMobile;
	}

	/**
	 * @return the reasonToCall
	 */
	@XmlElement(name = "reasonToCall")
	public String getReasonToCall()
	{
		return reasonToCall;
	}

	/**
	 * @param reasonToCall
	 *           the reasonToCall to set
	 */
	public void setReasonToCall(final String reasonToCall)
	{
		this.reasonToCall = reasonToCall;
	}



}
