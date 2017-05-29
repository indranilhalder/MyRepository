/**
 *
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "orderId", "pancardNumber", "status" })
@XmlRootElement(name = "pancardticket")
public class CRMpancardResponse
{
	@XmlElement(name = "orderId")
	private String orderId;

	@XmlElement(name = "pancardNumber")
	private String pancardNumber;

	@XmlElement(name = "status")
	private String status;

	/**
	 * @return the status
	 */

	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}


	/**
	 * @return the orderId
	 */

	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}


	/**
	 * @return the pancardNumber
	 */

	public String getPancardNumber()
	{
		return pancardNumber;
	}

	/**
	 * @param pancardNumber
	 *           the pancardNumber to set
	 */
	public void setPancardNumber(final String pancardNumber)
	{
		this.pancardNumber = pancardNumber;
	}


}
