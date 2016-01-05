/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 884206
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "paymentCost", "paymentMode", "paymentStatus", "paymentInfo", "paymentDate" })
public class PaymentDetWsDTO
{
	@XmlElement(name = "paymentCost")
	private double paymentCost;
	@XmlElement(name = "paymentMode")
	private String paymentMode;
	@XmlElement(name = "paymentStatus")
	private String paymentStatus;
	@XmlElement(name = "paymentInfo")
	private String paymentInfo;
	@XmlElement(name = "paymentDate")
	private Date paymentDate;

	//	public PaymentDetWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the paymentCost
	 */
	public double getPaymentCost()
	{
		return paymentCost;
	}

	/**
	 * @param paymentCost
	 *           the paymentCost to set
	 */
	public void setPaymentCost(final double paymentCost)
	{
		this.paymentCost = paymentCost;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode()
	{
		return paymentMode;
	}

	/**
	 * @param paymentMode
	 *           the paymentMode to set
	 */
	public void setPaymentMode(final String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the paymentStatus
	 */
	public String getPaymentStatus()
	{
		return paymentStatus;
	}

	/**
	 * @param paymentStatus
	 *           the paymentStatus to set
	 */
	public void setPaymentStatus(final String paymentStatus)
	{
		this.paymentStatus = paymentStatus;
	}

	/**
	 * @return the paymentInfo
	 */
	public String getPaymentInfo()
	{
		return paymentInfo;
	}

	/**
	 * @param paymentInfo
	 *           the paymentInfo to set
	 */
	public void setPaymentInfo(final String paymentInfo)
	{
		this.paymentInfo = paymentInfo;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate()
	{
		return paymentDate;
	}

	/**
	 * @param paymentDate
	 *           the paymentDate to set
	 */
	public void setPaymentDate(final Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}


}
