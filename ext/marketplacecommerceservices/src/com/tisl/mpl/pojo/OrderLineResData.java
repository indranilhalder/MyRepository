/**
 *
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "OrderLine")
public class OrderLineResData
{
	//@XmlElement(name = "TransactionID")
	private String transactionId;
	//@XmlElement(name = "PANCARDSTATUS")
	private String pancardStatus;

	/**
	 * @return the transactionId
	 */
	@XmlElement(name = "TransactionID")
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @return the pancardStatus
	 */
	@XmlElement(name = "PANCARDSTATUS")
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
	 * @param pancardStatus
	 *           the pancardStatus to set
	 */
	public void setPancardStatus(final String pancardStatus)
	{
		this.pancardStatus = pancardStatus;
	}

}
