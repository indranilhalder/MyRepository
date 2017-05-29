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
{ "orderId", "transactionId", "path", "pancardNumber" })
public class PancardCRMticketXMLData
{
	private String orderId;
	private String transactionId;
	private String path;
	private String pancardNumber;

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
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final String transactionId)
	{
		this.transactionId = transactionId;
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
