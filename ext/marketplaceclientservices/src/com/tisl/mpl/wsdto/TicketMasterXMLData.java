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
{ "ecomRequestId", "customerID", "orderId", "subOrderId", "ticketType", "refundType", "ticketSubType", "returnCategory",
		"lineItemDataList", "l0CatCode", "l1CatCode", "l2CatCode", "l3CatCode", "l4CatCode", "ticketCat", "comments",
		"alternateContactName", "alternatePhoneNo", "source", "lineItemId", "addressInfo" })
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
	private AddressInfoDTO addressInfo;
	private String ecomRequestId;
	//New addition for TPR-5989
	private String L0CatCode;
	private String L1CatCode;
	private String L2CatCode;
	private String L3CatCode;
	private String L4CatCode;
	private String TicketCat;
	private String Comments;

	//private List<UploadImage> uploadImage;



	/**
	 * @return the l0CatCode
	 */
	@XmlElement(name = "L0CatCode")
	public String getL0CatCode()
	{
		return L0CatCode;
	}

	/**
	 * @param l0CatCode
	 *           the l0CatCode to set
	 */
	public void setL0CatCode(final String l0CatCode)
	{
		L0CatCode = l0CatCode;
	}

	/**
	 * @return the l1CatCode
	 */
	@XmlElement(name = "L1CatCode")
	public String getL1CatCode()
	{
		return L1CatCode;
	}

	/**
	 * @param l1CatCode
	 *           the l1CatCode to set
	 */
	public void setL1CatCode(final String l1CatCode)
	{
		L1CatCode = l1CatCode;
	}

	/**
	 * @return the l2CatCode
	 */
	@XmlElement(name = "L2CatCode")
	public String getL2CatCode()
	{
		return L2CatCode;
	}

	/**
	 * @param l2CatCode
	 *           the l2CatCode to set
	 */
	public void setL2CatCode(final String l2CatCode)
	{
		L2CatCode = l2CatCode;
	}

	/**
	 * @return the l3CatCode
	 */
	@XmlElement(name = "L3CatCode")
	public String getL3CatCode()
	{
		return L3CatCode;
	}

	/**
	 * @param l3CatCode
	 *           the l3CatCode to set
	 */
	public void setL3CatCode(final String l3CatCode)
	{
		L3CatCode = l3CatCode;
	}

	/**
	 * @return the l4CatCode
	 */
	@XmlElement(name = "L4CatCode")
	public String getL4CatCode()
	{
		return L4CatCode;
	}

	/**
	 * @param l4CatCode
	 *           the l4CatCode to set
	 */
	public void setL4CatCode(final String l4CatCode)
	{
		L4CatCode = l4CatCode;
	}

	/**
	 * @return the ticketCat
	 */
	@XmlElement(name = "TicketCat")
	public String getTicketCat()
	{
		return TicketCat;
	}

	/**
	 * @param ticketCat
	 *           the ticketCat to set
	 */
	public void setTicketCat(final String ticketCat)
	{
		TicketCat = ticketCat;
	}

	/**
	 * @return the comments
	 */
	@XmlElement(name = "Comments")
	public String getComments()
	{
		return Comments;
	}

	/**
	 * @param comments
	 *           the comments to set
	 */
	public void setComments(final String comments)
	{
		Comments = comments;
	}

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

	/**
	 * @return the addressInfo
	 */
	@XmlElement(name = "AddressInfo")
	public AddressInfoDTO getAddressInfo()
	{
		return addressInfo;
	}

	/**
	 * @param addressInfo
	 *           the addressInfo to set
	 */
	public void setAddressInfo(final AddressInfoDTO addressInfo)
	{
		this.addressInfo = addressInfo;
	}

	/**
	 * @return the ecomRequestId
	 */

	@XmlElement(name = "EcomRequestId")
	public String getEcomRequestId()
	{
		return ecomRequestId;
	}

	/**
	 * @param ecomRequestId
	 *           the ecomRequestId to set
	 */
	public void setEcomRequestId(final String ecomRequestId)
	{
		this.ecomRequestId = ecomRequestId;
	}

	/**
	 * @return the uploadImage
	 */
	/*
	 * @XmlElement(name = "UploadImage") public List<UploadImage> getUploadImage() { return uploadImage; }
	 *//**
	 * @param uploadImage
	 *           the uploadImage to set
	 */
	/*
	 * public void setUploadImage(final List<UploadImage> uploadImage) { this.uploadImage = uploadImage; }
	 */

}