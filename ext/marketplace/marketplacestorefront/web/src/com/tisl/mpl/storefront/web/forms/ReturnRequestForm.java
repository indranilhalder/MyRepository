/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.commercefacades.order.data.OrderData;


/**
 * @author TCS
 *
 */
public class ReturnRequestForm
{
	/**
	 * @return the reasonCode
	 */

	private String reasonCode;
	private String reasonDescription;
	private OrderData orderLineItemDetail;
	private String ussid;
	private String orderCode;
	private String refundType;
	private String ticketTypeCode;
	private String transactionId;

	/**
	 * @return the transactionId
	 */
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
	 * @return the orderLineItemDetail
	 */
	public OrderData getOrderLineItemDetail()
	{
		return orderLineItemDetail;
	}

	/**
	 * @param orderLineItemDetail
	 *           the orderLineItemDetail to set
	 */
	public void setOrderLineItemDetail(final OrderData orderLineItemDetail)
	{
		this.orderLineItemDetail = orderLineItemDetail;
	}

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
	 * @return the reasonDescription
	 */
	public String getReasonDescription()
	{
		return reasonDescription;
	}

	/**
	 * @param reasonDescription
	 *           the reasonDescription to set
	 */
	public void setReasonDescription(final String reasonDescription)
	{
		this.reasonDescription = reasonDescription;
	}

	/**
	 * @return the ussid
	 */
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the ticketTypeCode
	 */
	public String getTicketTypeCode()
	{
		return ticketTypeCode;
	}

	/**
	 * @param ticketTypeCode
	 *           the ticketTypeCode to set
	 */
	public void setTicketTypeCode(final String ticketTypeCode)
	{
		this.ticketTypeCode = ticketTypeCode;
	}

	/**
	 * @return the refundType
	 */
	public String getRefundType()
	{
		return refundType;
	}

	/**
	 * @param refundType
	 *           the refundType to set
	 */
	public void setRefundType(final String refundType)
	{
		this.refundType = refundType;
	}




}
