/**
 *
 */
package com.tisl.mpl.xml.pojo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "RefundedInfo")
@XmlType(propOrder =
{ "refundedBy", "refundedAmt", "refundedBankTrxId", "refundedType", "refundedBankTrxStatus", "refundTriggeredDate" })
public class RefundInfoListXMLData
{
	private String refundedBy;
	private float refundedAmt;
	private String refundedBankTrxId;
	private String refundedType;
	private String refundedBankTrxStatus;
	private Date refundTriggeredDate;

	/**
	 * @return the refundedBy
	 */
	@XmlElement(name = "RefundedBy")
	public String getRefundedBy()
	{
		return refundedBy;
	}

	/**
	 * @param refundedBy
	 *           the refundedBy to set
	 */
	public void setRefundedBy(final String refundedBy)
	{
		this.refundedBy = refundedBy;
	}

	/**
	 * @return the refundedAmt
	 */
	@XmlElement(name = "RefundedAmt")
	public float getRefundedAmt()
	{
		return refundedAmt;
	}

	/**
	 * @param refundedAmt
	 *           the refundedAmt to set
	 */
	public void setRefundedAmt(final float refundedAmt)
	{
		this.refundedAmt = refundedAmt;
	}

	/**
	 * @return the refundedBankTrxId
	 */
	@XmlElement(name = "RefundedBankTrxID")
	public String getRefundedBankTrxId()
	{
		return refundedBankTrxId;
	}

	/**
	 * @param refundedBankTrxId
	 *           the refundedBankTrxId to set
	 */
	public void setRefundedBankTrxId(final String refundedBankTrxId)
	{
		this.refundedBankTrxId = refundedBankTrxId;
	}

	/**
	 * @return the refundedType
	 */
	@XmlElement(name = "RefundType")
	public String getRefundedType()
	{
		return refundedType;
	}

	/**
	 * @param refundedType
	 *           the refundedType to set
	 */
	public void setRefundedType(final String refundedType)
	{
		this.refundedType = refundedType;
	}

	/**
	 * @return the refundedBankTrxStatus
	 */
	@XmlElement(name = "RefundedBankTrxStatus")
	public String getRefundedBankTrxStatus()
	{
		return refundedBankTrxStatus;
	}

	/**
	 * @param refundedBankTrxStatus
	 *           the refundedBankTrxStatus to set
	 */
	public void setRefundedBankTrxStatus(final String refundedBankTrxStatus)
	{
		this.refundedBankTrxStatus = refundedBankTrxStatus;
	}

	/**
	 * @return the refundTriggeredDate
	 */
	@XmlElement(name = "RefundTriggeredDate")
	public Date getRefundTriggeredDate()
	{
		return refundTriggeredDate;
	}

	/**
	 * @param refundTriggeredDate
	 *           the refundTriggeredDate to set
	 */
	public void setRefundTriggeredDate(final Date refundTriggeredDate)
	{
		this.refundTriggeredDate = refundTriggeredDate;
	}



}
