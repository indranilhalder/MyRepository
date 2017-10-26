/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;


/**
 * @author TCS
 *
 *         Form Object for Web Form
 */
public class TicketWebForm
{

	private String commerceTicketId;
	private String typeOfQuery;
	private String transactionId;
	private String nodeL1;
	private String nodeL2;
	private String nodeL3;
	private String nodeL4;
	private String comment;
	private String attachment;
	private String crmTicketId;
	private List<OrderEntryData> entries;

	/**
	 * @return the commerceTicketId
	 */
	public String getCommerceTicketId()
	{
		return commerceTicketId;
	}

	/**
	 * @param commerceTicketId
	 *           the commerceTicketId to set
	 */
	public void setCommerceTicketId(final String commerceTicketId)
	{
		this.commerceTicketId = commerceTicketId;
	}

	/**
	 * @return the typeOfQuery
	 */
	public String getTypeOfQuery()
	{
		return typeOfQuery;
	}

	/**
	 * @param typeOfQuery
	 *           the typeOfQuery to set
	 */
	public void setTypeOfQuery(final String typeOfQuery)
	{
		this.typeOfQuery = typeOfQuery;
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
	 * @return the attachment
	 */
	public String getAttachment()
	{
		return attachment;
	}

	/**
	 * @param attachment
	 *           the attachment to set
	 */
	public void setAttachment(final String attachment)
	{
		this.attachment = attachment;
	}

	/**
	 * @return the crmTicketId
	 */
	public String getCrmTicketId()
	{
		return crmTicketId;
	}

	/**
	 * @param crmTicketId
	 *           the crmTicketId to set
	 */
	public void setCrmTicketId(final String crmTicketId)
	{
		this.crmTicketId = crmTicketId;
	}

	/**
	 * @return the entries
	 */
	public List<OrderEntryData> getEntries()
	{
		return entries;
	}

	/**
	 * @param entries
	 *           the entries to set
	 */
	public void setEntries(final List<OrderEntryData> entries)
	{
		this.entries = entries;
	}



}
