/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Dileep
 *
 */
@XmlRootElement(name = "OrderLine")
@XmlType(propOrder =
{ "orderId", "transactionId", "interfaceType", "reasonCode", "refundMode", "returnStoreId", "storeCreditNo", "isReturnEligible",
		"isReturnInitiated", "subReasonCode", "comments" })
public class OrderLineDTO
{
	private String orderId;
	private String transactionId;
	private String interfaceType;
	private String reasonCode;
	private String refundMode;
	private String returnStoreId;
	private String storeCreditNo;
	private String isReturnEligible;
	private String isReturnInitiated;
	private String subReasonCode;
	private String comments;


	/**
	 * @return the subReasonCode
	 */
	@XmlElement(name = "SubReturnReason")
	public String getSubReasonCode()
	{
		return subReasonCode;
	}

	/**
	 * @param subReasonCode
	 *           the subReasonCode to set
	 */
	public void setSubReasonCode(final String subReasonCode)
	{
		this.subReasonCode = subReasonCode;
	}

	/**
	 * @return the comments
	 */
	@XmlElement(name = "Comments")
	public String getComments()
	{
		return comments;
	}

	/**
	 * @param comments
	 *           the comments to set
	 */
	public void setComments(final String comments)
	{
		this.comments = comments;
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
	 * @return the interfaceType
	 */
	@XmlElement(name = "interfaceType")
	public String getInterfaceType()
	{
		return interfaceType;
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
	 * @return the reasonCode
	 */
	@XmlElement(name = "ReasonCode")
	public String getReasonCode()
	{
		return reasonCode;
	}

	/**
	 * @param reasonCode
	 *           the reasonCode to set
	 */
	public void setReasonCode(final String reasonCode)
	{
		this.reasonCode = reasonCode;
	}

	/**
	 * @return the refundMode
	 */
	@XmlElement(name = "RefundMode")
	public String getRefundMode()
	{
		return refundMode;
	}

	/**
	 * @param refundMode
	 *           the refundMode to set
	 */
	public void setRefundMode(final String refundMode)
	{
		this.refundMode = refundMode;
	}

	/**
	 * @return the returnStoreId
	 */
	@XmlElement(name = "ReturnStoreId")
	public String getReturnStoreId()
	{
		return returnStoreId;
	}

	/**
	 * @param returnStoreId
	 *           the returnStoreId to set
	 */
	public void setReturnStoreId(final String returnStoreId)
	{
		this.returnStoreId = returnStoreId;
	}

	/**
	 * @return the storeCreditNo
	 */
	@XmlElement(name = "storeCreditNo")
	public String getStoreCreditNo()
	{
		return storeCreditNo;
	}

	/**
	 * @param storeCreditNo
	 *           the storeCreditNo to set
	 */
	public void setStoreCreditNo(final String storeCreditNo)
	{
		this.storeCreditNo = storeCreditNo;
	}

	/**
	 * @return the isReturnEligible
	 */
	@XmlElement(name = "isReturnEligible")
	public String getIsReturnEligible()
	{
		return isReturnEligible;
	}

	/**
	 * @param isReturnEligible
	 *           the isReturnEligible to set
	 */
	public void setIsReturnEligible(final String isReturnEligible)
	{
		this.isReturnEligible = isReturnEligible;
	}

	/**
	 * @return the isReturnInitiated
	 */
	@XmlElement(name = "isReturnInitiated")
	public String getIsReturnInitiated()
	{
		return isReturnInitiated;
	}

	/**
	 * @param isReturnInitiated
	 *           the isReturnInitiated to set
	 */
	public void setIsReturnInitiated(final String isReturnInitiated)
	{
		this.isReturnInitiated = isReturnInitiated;
	}
}
