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
{ "orderId", "path", "name", "pancardNumber", "status", "ticketNo" })
@XmlRootElement(name = "pancardticket")
public class CRMpancardResponse
{
	@XmlElement(name = "orderId")
	private String orderId;

	@XmlElement(name = "path")
	private String path;

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "pancardNumber")
	private String pancardNumber;

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "ticketNo")
	private String ticketNo;



	/**
	 * @return the ticketNo
	 */

	public String getTicketNo()
	{
		return ticketNo;
	}

	/**
	 * @param ticketNo
	 *           the ticketNo to set
	 */
	public void setTicketNo(final String ticketNo)
	{
		this.ticketNo = ticketNo;
	}

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
	 * @return the path
	 */

	public String getPath()
	{
		return path;
	}

	/**
	 * @param path
	 *           the path to set
	 */
	public void setPath(final String path)
	{
		this.path = path;
	}

	/**
	 * @return the name
	 */

	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
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
