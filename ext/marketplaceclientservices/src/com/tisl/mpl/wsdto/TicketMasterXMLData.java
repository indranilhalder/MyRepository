/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "ticketMaster")
@XmlType(propOrder =
{ "customerID", "orderId", "subOrderId", "ticketType", "refundType", "returnCategory", "lineItemDataList" })
public class TicketMasterXMLData
{
	private String customerID;
	private String orderId;
	private String subOrderId;
	private String ticketType;
	private String refundType;
	private String returnCategory;
	private List<TicketlineItemsXMLData> lineItemDataList;

	/**
	 * @return the customerID
	 */
	@XmlElement(name = "customerID")
	public String getCustomerID()
	{
		return customerID;
	}

	/**
	 * @param customerID
	 *           the customerID to set
	 */
	public void setCustomerID(final String customerID)
	{
		this.customerID = customerID;
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
	 * @return the subOrderId
	 */
	@XmlElement(name = "subOrderId")
	public String getSubOrderId()
	{
		return subOrderId;
	}

	/**
	 * @param subOrderId
	 *           the subOrderId to set
	 */
	public void setSubOrderId(final String subOrderId)
	{
		this.subOrderId = subOrderId;
	}

	/**
	 * @return the ticketType
	 */
	@XmlElement(name = "ticketType")
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
	 * @return the refundType
	 */
	@XmlElement(name = "refundType")
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

	/**
	 * @return the returnCategory
	 */
	@XmlElement(name = "returnCategory")
	public String getReturnCategory()
	{
		return returnCategory;
	}

	/**
	 * @param returnCategory
	 *           the returnCategory to set
	 */
	public void setReturnCategory(final String returnCategory)
	{
		this.returnCategory = returnCategory;
	}

	/**
	 * @return the lineItemDataList
	 */
	@XmlElement(name = "lineItems")
	public List<TicketlineItemsXMLData> getLineItemDataList()
	{
		return lineItemDataList;
	}

	/**
	 * @param lineItemDataList
	 *           the lineItemDataList to set
	 */
	public void setLineItemDataList(final List<TicketlineItemsXMLData> lineItemDataList)
	{
		this.lineItemDataList = lineItemDataList;
	}



}
