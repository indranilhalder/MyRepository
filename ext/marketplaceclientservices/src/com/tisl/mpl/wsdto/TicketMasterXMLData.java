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
{ "customerID", "orderId", "subOrderId", "ticketType", "refundType", "ticketSubType", "returnCategory", "lineItemDataList",
		"alternateContactName", "alternatePhoneNo", "source", "lineItemId" })
public class TicketMasterXMLData
{
	private String customerID;
	private String orderId;
	private String subOrderId;
	private String ticketType;
	private String refundType;
	private String returnCategory;
	private List<TicketlineItemsXMLData> lineItemDataList;
	private String alternateContactName;
	private String alternatePhoneNo;
	private String source;
	private String lineItemId;
	private String ticketSubType;


	/**
	 * @return the lineItemId
	 */
	@XmlElement(name = "lineItemId")
	public String getLineItemId()
	{
		return lineItemId;
	}

	/**
	 * @param lineItemId
	 *           the lineItemId to set
	 */
	public void setLineItemId(final String lineItemId)
	{
		this.lineItemId = lineItemId;
	}

	/**
	 * @param source
	 *           the source to set
	 */

	public void setSource(final String source)
	{
		this.source = source;
	}

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

	/**
	 * @return the alternateContactName
	 */
	@XmlElement(name = "AlternateContactName")
	public String getAlternateContactName()
	{
		return alternateContactName;
	}

	/**
	 * @return the alternatePhoneNo
	 */
	@XmlElement(name = "AlternatePhoneNo")
	public String getAlternatePhoneNo()
	{
		return alternatePhoneNo;
	}

	/**
	 * @return the source
	 */

	@XmlElement(name = "source")
	public String getSource()
	{
		return source;
	}

	/**
	 * @param alternateContactName
	 *           the alternateContactName to set
	 */
	public void setAlternateContactName(final String alternateContactName)
	{
		this.alternateContactName = alternateContactName;
	}

	/**
	 * @param alternatePhoneNo
	 *           the alternatePhoneNo to set
	 */
	public void setAlternatePhoneNo(final String alternatePhoneNo)
	{
		this.alternatePhoneNo = alternatePhoneNo;
	}

	/**
	 * @return the ticketSubType
	 */
	@XmlElement(name = "TicketSubType")
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
}