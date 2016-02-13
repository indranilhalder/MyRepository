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
@XmlRootElement(name = "OrderLine")
@XmlType(propOrder =
{ "orderId", "transactionId", "isReturnLogisticsAvailable" })
public class OrderLineDataResponse
{
	private String orderId;
	private String transactionId;
	private String isReturnLogisticsAvailable;

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
	@XmlElement(name = "transactionId")
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
	 * @return the isReturnLogisticsAvailable
	 */
	@XmlElement(name = "isReturnLogisticsAvailable")
	public String getIsReturnLogisticsAvailable()
	{
		return isReturnLogisticsAvailable;
	}

	/**
	 * @param isReturnLogisticsAvailable
	 *           the isReturnLogisticsAvailable to set
	 */
	public void setIsReturnLogisticsAvailable(final String isReturnLogisticsAvailable)
	{
		this.isReturnLogisticsAvailable = isReturnLogisticsAvailable;
	}



}
