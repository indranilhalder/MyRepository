/**
 *
 */
package com.tisl.mpl.xml.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "OrderLine")
@XmlType(propOrder =
{ "orderId", "transactionId","pinCode","returnFulfilmentType" })
public class OrderLineData
{
	private String orderId;
	private String transactionId;
	private String pinCode;
	private List<String> returnFulfilmentType;

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
	 * @return the pinCode
	 */
	@XmlElement(name = "pinCode")
	public String getPinCode()
	{
		return pinCode;
	}

	/**
	 * @param pinCode the pinCode to set
	 */
	public void setPinCode(String pinCode)
	{
		this.pinCode = pinCode;
	}

	/**
	 * @return the returnFulfilmentType
	 */
	@XmlElement(name = "ReturnFulfilmentType")
	public List<String> getReturnFulfilmentType()
	{
		return returnFulfilmentType;
	}

	/**
	 * @param returnFulfilmentType the returnFulfilmentType to set
	 */
	public void setReturnFulfilmentType(List<String> returnFulfilmentType)
	{
		this.returnFulfilmentType = returnFulfilmentType;
	}


}
