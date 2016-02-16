/**
 *
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "RefundInfoResponse")
@XmlType(propOrder =
{ "received" })
public class RefundInfoResponse
{

	private String received;

	/**
	 * @return the received
	 */
	@XmlElement(name = "received")
	public String getReceived()
	{
		return received;
	}

	/**
	 * @param received
	 *           the received to set
	 */
	public void setReceived(final String received)
	{
		this.received = received;
	}

}
