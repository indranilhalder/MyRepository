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
@XmlRootElement(name = "OrderLine")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "transactionId", "interfaceType", "pancardPath", "pancardStatus" })
public class OrderLine
{
	@XmlElement(name = "TransactionID")
	private String transactionId;
	@XmlElement(name = "InterfaceType")
	private String interfaceType;
	@XmlElement(name = "PANCARDPATH")
	private String pancardPath;
	@XmlElement(name = "PANCARDSTATUS")
	private String pancardStatus;

	/**
	 * @return the transactionId
	 */
	//@XmlElement(name = "TransactionID")@XmlAttribute
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @return the interfaceType
	 */
	//@XmlElement(name = "InterfaceType")
	public String getInterfaceType()
	{
		return interfaceType;
	}

	/**
	 * @return the pancardPath
	 */
	//@XmlElement(name = "PANCARDPATH")
	public String getPancardPath()
	{
		return pancardPath;
	}

	/**
	 * @return the pancardStatus
	 */
	//@XmlElement(name = "PANCARDSTATUS")
	public String getPancardStatus()
	{
		return pancardStatus;
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
	 * @param interfaceType
	 *           the interfaceType to set
	 */
	public void setInterfaceType(final String interfaceType)
	{
		this.interfaceType = interfaceType;
	}

	/**
	 * @param pancardPath
	 *           the pancardPath to set
	 */
	public void setPancardPath(final String pancardPath)
	{
		this.pancardPath = pancardPath;
	}

	/**
	 * @param pancardStatus
	 *           the pancardStatus to set
	 */
	public void setPancardStatus(final String pancardStatus)
	{
		this.pancardStatus = pancardStatus;
	}


}
