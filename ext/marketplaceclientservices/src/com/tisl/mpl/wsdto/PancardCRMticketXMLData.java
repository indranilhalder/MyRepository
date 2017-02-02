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

@XmlRootElement(name = "pancardticket")
@XmlType(propOrder =
{ "orderId", "path", "name", "pancardNumber", "status", "ticketNo" })
public class PancardCRMticketXMLData
{
	private String orderId;
	private String path;
	private String name;
	private String pancardNumber;
	private String status;
	private String ticketNo;


	/**
	 * @return the status
	 */
	@XmlElement(name = "status")
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
	 * @return the ticketNo
	 */
	@XmlElement(name = "ticketNo")
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
	 * @return the orderId
	 */
	@XmlElement(name = "orderId")
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
	@XmlElement(name = "path")
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
	@XmlElement(name = "name")
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
	@XmlElement(name = "pancardNumber")
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
