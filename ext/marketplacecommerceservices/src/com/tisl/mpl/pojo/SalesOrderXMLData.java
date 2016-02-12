/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "SalesOrderPaymentInformationData")
@XmlType(propOrder =
{ "orderId", "orderDate", "merchantCode", "orderType", "subOrderList" })
public class SalesOrderXMLData
{

	private String orderId;
	//private String paymentRefID;
	private String orderDate;
	private String merchantCode;
	private String orderType;
	private List<SubOrderXMLData> subOrderList;

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "OrderRefNo")
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
	 * @return the paymentRefID
	 */
	/*
	 * @XmlElement(name = "PaymentRefID") public String getPaymentRefID() { return paymentRefID; }
	 *//**
	 * @param paymentRefID
	 *           the paymentRefID to set
	 */
	/*
	 * public void setPaymentRefID(final String paymentRefID) { this.paymentRefID = paymentRefID; }
	 */
	/**
	 * @return the orderDate
	 */
	@XmlElement(name = "OrderDate")
	public String getOrderDate()
	{
		return orderDate;
	}

	/**
	 * @param orderDate
	 *           the orderDate to set
	 */
	public void setOrderDate(final String orderDate)
	{
		this.orderDate = orderDate;
	}

	/**
	 * @return the merchantCode
	 */
	@XmlElement(name = "MerchantCode")
	public String getMerchantCode()
	{
		return merchantCode;
	}

	/**
	 * @param merchantCode
	 *           the merchantCode to set
	 */
	public void setMerchantCode(final String merchantCode)
	{
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the orderType
	 */
	@XmlElement(name = "OrderType")
	public String getOrderType()
	{
		return orderType;
	}

	/**
	 * @param orderType
	 *           the orderType to set
	 */
	public void setOrderType(final String orderType)
	{
		this.orderType = orderType;
	}

	/**
	 * @return the subOrderList
	 */
	@XmlElement(name = "SubOrderInfo")
	public List<SubOrderXMLData> getSubOrderList()
	{
		return subOrderList;
	}

	/**
	 * @param subOrderList
	 *           the subOrderList to set
	 */
	public void setSubOrderList(final List<SubOrderXMLData> subOrderList)
	{
		this.subOrderList = subOrderList;
	}

}
