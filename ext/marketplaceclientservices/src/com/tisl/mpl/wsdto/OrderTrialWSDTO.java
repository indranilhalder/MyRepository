/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author 884206
 *
 */

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Order")
@XmlType(propOrder =
{ "orderRefNo", "orderType", "channel", "submissionDateTime", "orderDate", "suborder", "ca", "pd" })
public class OrderTrialWSDTO
{
	@XmlElement(name = "orderRefNo")
	private String orderRefNo;
	@XmlElement(name = "orderType")
	private String orderType;
	@XmlElement(name = "channel")
	private String channel;
	@XmlElement(name = "submissionDateTime")
	private Date submissionDateTime;
	@XmlElement(name = "orderDate")
	private Date orderDate;
	//@XmlElementWrapper(name = "suborder") changed now
	//@XmlElement(name = "suborderelement")
	private List<SubOrderWsDTO> suborder;
	@XmlElement(name = "CustomerAddress")
	private CustomerAddressWsDTO ca;
	@XmlElement(name = "PaymentDetails")
	private PaymentDetWsDTO pd;



	/**
	 * @return the orderRefNo
	 */

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
	 * @return the orderType
	 */

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
	 * @return the channel
	 */

	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel
	 *           the channel to set
	 */
	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	/**
	 * @return the submissionDateTime
	 */

	public Date getSubmissionDateTime()
	{
		return submissionDateTime;
	}

	/**
	 * @param submissionDateTime
	 *           the submissionDateTime to set
	 */
	public void setSubmissionDateTime(final Date submissionDateTime)
	{
		this.submissionDateTime = submissionDateTime;
	}

	/**
	 * @return the orderDate
	 */

	public Date getOrderDate()
	{
		return orderDate;
	}

	/**
	 * @param orderDate
	 *           the orderDate to set
	 */
	public void setOrderDate(final Date orderDate)
	{
		this.orderDate = orderDate;
	}

	/**
	 * @return the suborder
	 */
	public List<SubOrderWsDTO> getSuborder()
	{
		return suborder;
	}

	/**
	 * @param suborder
	 *           the suborder to set
	 */
	public void setSuborder(final List<SubOrderWsDTO> suborder)
	{
		this.suborder = suborder;
	}

	/**
	 * @return the ca
	 */
	public CustomerAddressWsDTO getCa()
	{
		return ca;
	}

	/**
	 * @param ca
	 *           the ca to set
	 */
	public void setCa(final CustomerAddressWsDTO ca)
	{
		this.ca = ca;
	}

	/**
	 * @return the pd
	 */
	public PaymentDetWsDTO getPd()
	{
		return pd;
	}

	/**
	 * @param pd
	 *           the pd to set
	 */
	public void setPd(final PaymentDetWsDTO pd)
	{
		this.pd = pd;
	}


}
