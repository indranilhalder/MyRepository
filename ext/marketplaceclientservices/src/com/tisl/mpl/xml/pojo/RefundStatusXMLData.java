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
@XmlRootElement(name = "RefundStatus")
@XmlType(propOrder =
{ "orderRefNo", "transactionId", "oMSStatusCode", "refundInfoList" })
public class RefundStatusXMLData
{
	private String orderRefNo;
	private String transactionId;
	private String oMSStatusCode;
	private List<RefundInfoListXMLData> refundInfoList;

	/**
	 * @return the orderRefNo
	 */
	@XmlElement(name = "OrderRefNo")
	public String getOrderRefNo()
	{
		return orderRefNo;
	}

	/**
	 * @param orderRefNo
	 *           the orderRefNo to set
	 */

	public void setOrderRefNo(final String orderRefNo)
	{
		this.orderRefNo = orderRefNo;
	}

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
	 * @return the oMSStatusCode
	 */
	@XmlElement(name = "OMSStatusCode")
	public String getoMSStatusCode()
	{
		return oMSStatusCode;
	}

	/**
	 * @param oMSStatusCode
	 *           the oMSStatusCode to set
	 */
	public void setoMSStatusCode(final String oMSStatusCode)
	{
		this.oMSStatusCode = oMSStatusCode;
	}

	/**
	 * @return the refundInfoList
	 */
	@XmlElement(name = "RefundedInfo")
	public List<RefundInfoListXMLData> getRefundInfoList()
	{
		return refundInfoList;
	}

	/**
	 * @param refundInfoList
	 *           the refundInfoList to set
	 */
	public void setRefundInfoList(final List<RefundInfoListXMLData> refundInfoList)
	{
		this.refundInfoList = refundInfoList;
	}

}
