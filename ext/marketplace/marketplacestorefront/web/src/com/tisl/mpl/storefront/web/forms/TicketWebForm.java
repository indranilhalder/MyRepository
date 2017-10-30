/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import java.util.List;


/**
 * @author TCS
 *
 *         Form Object for Web Form
 */
public class TicketWebForm
{

	private String ticketType;
	private String ticketSubType;
	private String orderCode;
	private String subOrderCode;
	private String transactionId;
	private String nodeL1;
	private String nodeL2;
	private String nodeL3;
	private String nodeL4;
	private String comment;
	private List<String> attachments;

	/**
	 * @return the ticketType
	 */
	public String getTicketType()
	{
		return ticketType;
	}

	/**
	 * @param ticketType
	 *           the ticketType to set
	 */
	public void setTicketType(final String ticketType)
	{
		this.ticketType = ticketType;
	}

	/**
	 * @return the ticketSubType
	 */
	public String getTicketSubType()
	{
		return ticketSubType;
	}

	/**
	 * @param ticketSubType
	 *           the ticketSubType to set
	 */
	public void setTicketSubType(final String ticketSubType)
	{
		this.ticketSubType = ticketSubType;
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
	 * @return the subOrderCode
	 */
	public String getSubOrderCode()
	{
		return subOrderCode;
	}

	/**
	 * @param subOrderCode
	 *           the subOrderCode to set
	 */
	public void setSubOrderCode(final String subOrderCode)
	{
		this.subOrderCode = subOrderCode;
	}

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
	 * @return the nodeL1
	 */
	public String getNodeL1()
	{
		return nodeL1;
	}

	/**
	 * @param nodeL1
	 *           the nodeL1 to set
	 */
	public void setNodeL1(final String nodeL1)
	{
		this.nodeL1 = nodeL1;
	}

	/**
	 * @return the nodeL2
	 */
	public String getNodeL2()
	{
		return nodeL2;
	}

	/**
	 * @param nodeL2
	 *           the nodeL2 to set
	 */
	public void setNodeL2(final String nodeL2)
	{
		this.nodeL2 = nodeL2;
	}

	/**
	 * @return the nodeL3
	 */
	public String getNodeL3()
	{
		return nodeL3;
	}

	/**
	 * @param nodeL3
	 *           the nodeL3 to set
	 */
	public void setNodeL3(final String nodeL3)
	{
		this.nodeL3 = nodeL3;
	}

	/**
	 * @return the nodeL4
	 */
	public String getNodeL4()
	{
		return nodeL4;
	}

	/**
	 * @param nodeL4
	 *           the nodeL4 to set
	 */
	public void setNodeL4(final String nodeL4)
	{
		this.nodeL4 = nodeL4;
	}

	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment
	 *           the comment to set
	 */
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the attachments
	 */
	public List<String> getAttachments()
	{
		return attachments;
	}

	/**
	 * @param attachments
	 *           the attachments to set
	 */
	public void setAttachments(final List<String> attachments)
	{
		this.attachments = attachments;
	}


}
