/**
 *
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "TransactionInfo")
@XmlType(propOrder =
{ "transactionId", "paymentRefID", "orderTag", "fulfillmentType", "itemNumber", "USSID", "amount", "shipmentCharge",
		"sellerCode", "expressdeliveryCharge", "primaryCategory", "secondaryCategory", "cancelDate", "returnDate",
		"reversePaymentRefId" })
public class ChildOrderXMlData
{

	private String transactionId;
	private String paymentRefID;
	private String orderTag;
	private String fulfillmentType;
	private String itemNumber;
	private String USSID;
	private double amount;
	private double shipmentCharge;
	private String sellerCode;
	private double expressdeliveryCharge;
	private String primaryCategory;
	private String secondaryCategory;
	private String cancelDate;
	private String returnDate;
	private String reversePaymentRefId;

	/**
	 * @return the transactionId
	 */
	@XmlElement(name = "TransactionID")
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
	 * @return the paymentRefID
	 */
	@XmlElement(name = "PaymentRefID")
	public String getPaymentRefID()
	{
		return paymentRefID;
	}

	/**
	 * @param paymentRefID
	 *           the paymentRefID to set
	 */
	public void setPaymentRefID(final String paymentRefID)
	{
		this.paymentRefID = paymentRefID;
	}

	/**
	 * @return the orderTag
	 */

	@XmlElement(name = "OrderTag")
	public String getOrderTag()
	{
		return orderTag;
	}

	/**
	 * @param orderTag
	 *           the orderTag to set
	 */
	public void setOrderTag(final String orderTag)
	{
		this.orderTag = orderTag;
	}

	/**
	 * @return the fulfillmentType
	 */
	@XmlElement(name = "FulfilmentType")
	public String getFulfillmentType()
	{
		return fulfillmentType;
	}

	/**
	 * @param fulfillmentType
	 *           the fulfillmentType to set
	 */
	public void setFulfillmentType(final String fulfillmentType)
	{
		this.fulfillmentType = fulfillmentType;
	}

	/**
	 * @return the itemNumber
	 */
	@XmlElement(name = "ItemNumber")
	public String getItemNumber()
	{
		return itemNumber;
	}

	/**
	 * @param itemNumber
	 *           the itemNumber to set
	 */
	public void setItemNumber(final String itemNumber)
	{
		this.itemNumber = itemNumber;
	}

	/**
	 * @return the uSSID
	 */
	@XmlElement(name = "USSID")
	public String getUSSID()
	{
		return USSID;
	}

	/**
	 * @param uSSID
	 *           the uSSID to set
	 */
	public void setUSSID(final String uSSID)
	{
		USSID = uSSID;
	}

	/**
	 * @return the amount
	 */
	@XmlElement(name = "Amount")
	public double getAmount()
	{
		return amount;
	}

	/**
	 * @param amount
	 *           the amount to set
	 */
	public void setAmount(final double amount)
	{
		this.amount = amount;
	}



	/**
	 * @return the shipmentCharge
	 */
	@XmlElement(name = "ShipmentCharge")
	public double getShipmentCharge()
	{
		return shipmentCharge;
	}

	/**
	 * @param shipmentCharge
	 *           the shipmentCharge to set
	 */
	public void setShipmentCharge(final double shipmentCharge)
	{
		this.shipmentCharge = shipmentCharge;
	}

	/**
	 * @return the sellerCode
	 */
	@XmlElement(name = "SellerCode")
	public String getSellerCode()
	{
		return sellerCode;
	}

	/**
	 * @param sellerCode
	 *           the sellerCode to set
	 */
	public void setSellerCode(final String sellerCode)
	{
		this.sellerCode = sellerCode;
	}


	/**
	 * @return the expressdeliveryCharge
	 */
	@XmlElement(name = "ExpressDelCharge")
	public double getExpressdeliveryCharge()
	{
		return expressdeliveryCharge;
	}

	/**
	 * @param expressdeliveryCharge
	 *           the expressdeliveryCharge to set
	 */
	public void setExpressdeliveryCharge(final double expressdeliveryCharge)
	{
		this.expressdeliveryCharge = expressdeliveryCharge;
	}

	/**
	 * @return the primaryCategory
	 */
	@XmlElement(name = "L1ProdCategory")
	public String getPrimaryCategory()
	{
		return primaryCategory;
	}

	/**
	 * @param primaryCategory
	 *           the primaryCategory to set
	 */
	public void setPrimaryCategory(final String primaryCategory)
	{
		this.primaryCategory = primaryCategory;
	}

	/**
	 * @return the secondaryCategory
	 */
	@XmlElement(name = "L2ProdCategory")
	public String getSecondaryCategory()
	{
		return secondaryCategory;
	}

	/**
	 * @param secondaryCategory
	 *           the secondaryCategory to set
	 */
	public void setSecondaryCategory(final String secondaryCategory)
	{
		this.secondaryCategory = secondaryCategory;
	}

	/**
	 * @return the reversePaymentRefId
	 */
	@XmlElement(name = "ReversePaymentRefId")
	public String getReversePaymentRefId()
	{
		return reversePaymentRefId;
	}

	/**
	 * @param reversePaymentRefId
	 *           the reversePaymentRefId to set
	 */
	public void setReversePaymentRefId(final String reversePaymentRefId)
	{
		this.reversePaymentRefId = reversePaymentRefId;
	}

	/**
	 * @return the cancelDate
	 */
	@XmlElement(name = "CancelDate")
	public String getCancelDate()
	{
		return cancelDate;
	}

	/**
	 * @param cancelDate
	 *           the cancelDate to set
	 */
	public void setCancelDate(final String cancelDate)
	{
		this.cancelDate = cancelDate;
	}

	/**
	 * @return the returnDate
	 */
	@XmlElement(name = "ReturnDate")
	public String getReturnDate()
	{
		return returnDate;
	}

	/**
	 * @param returnDate
	 *           the returnDate to set
	 */
	public void setReturnDate(final String returnDate)
	{
		this.returnDate = returnDate;
	}

}
