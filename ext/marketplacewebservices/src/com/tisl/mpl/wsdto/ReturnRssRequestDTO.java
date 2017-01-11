/**
 * 
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tisl.mpl.jaxb.bind.annotation.UseDeclaredXmlRootElement;

/**
 * @author Pankaj
 *
 */

@XmlRootElement(name = "Ticket_Create")
@UseDeclaredXmlRootElement(enabled=true)
public class ReturnRssRequestDTO implements java.io.Serializable 
{
	private String customerID;
	private String ecomRequestId;
	private String requestId;
	private String ticketId;
	private String orderID;
	private String lineItemId;
	private String ticketType;
	private String ticketSubType;
	private String canc_Ret_Reas;
	private String refund_Type;
	private String remarks;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private String address1;
	private String address2;
	private String address3;
	private String country;
	private String city;
	private String state;
	private String pincode;
	private String landmark;
	private String returnStoreId;
	private String returnPickupDateFrom;
	private String returnPickupDateTo;
	private String flag;
	private String storeCreditNo;
	private String errorCode;
	
	/**
	 * @return the customerID
	 */
	@XmlElement(name = "CustomerID")
	public String getCustomerID()
	{
		return customerID;
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
	 * @return the requestId
	 */
	@XmlElement(name = "RequestId")
	public String getRequestId()
	{
		return requestId;
	}
	/**
	 * @return the ticketId
	 */
	@XmlElement(name = "TicketId")
	public String getTicketId()
	{
		return ticketId;
	}
	/**
	 * @return the orderID
	 */
	@XmlElement(name = "OrderID")
	public String getOrderID()
	{
		return orderID;
	}
	/**
	 * @return the lineItemId
	 */
	@XmlElement(name = "LineItemId")
	public String getLineItemId()
	{
		return lineItemId;
	}
	/**
	 * @return the ticketType
	 */
	@XmlElement(name = "TicketType")
	public String getTicketType()
	{
		return ticketType;
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
	 * @return the canc_Ret_Reas
	 */
	@XmlElement(name = "Canc_Ret_Reas")
	public String getCanc_Ret_Reas()
	{
		return canc_Ret_Reas;
	}
	/**
	 * @return the refund_Type
	 */
	@XmlElement(name = "Refund_Type")
	public String getRefund_Type()
	{
		return refund_Type;
	}
	/**
	 * @return the remarks
	 */
	@XmlElement(name = "Remarks")
	public String getRemarks()
	{
		return remarks;
	}
	/**
	 * @return the firstName
	 */
	@XmlElement(name = "FirstName")
	public String getFirstName()
	{
		return firstName;
	}
	/**
	 * @return the lastName
	 */
	@XmlElement(name = "LastName")
	public String getLastName()
	{
		return lastName;
	}
	/**
	 * @return the phoneNo
	 */
	@XmlElement(name = "PhoneNo")
	public String getPhoneNo()
	{
		return phoneNo;
	}
	/**
	 * @return the address1
	 */
	@XmlElement(name = "Address1")
	public String getAddress1()
	{
		return address1;
	}
	/**
	 * @return the address2
	 */
	@XmlElement(name = "Address2")
	public String getAddress2()
	{
		return address2;
	}
	/**
	 * @return the address3
	 */
	@XmlElement(name = "Address3")
	public String getAddress3()
	{
		return address3;
	}
	/**
	 * @return the country
	 */
	@XmlElement(name = "Country")
	public String getCountry()
	{
		return country;
	}
	/**
	 * @return the city
	 */
	@XmlElement(name = "City")
	public String getCity()
	{
		return city;
	}
	/**
	 * @return the state
	 */
	@XmlElement(name = "State")
	public String getState()
	{
		return state;
	}
	/**
	 * @return the pincode
	 */
	@XmlElement(name = "Pincode")
	public String getPincode()
	{
		return pincode;
	}
	/**
	 * @return the landmark
	 */
	@XmlElement(name = "Landmark")
	public String getLandmark()
	{
		return landmark;
	}
	/**
	 * @return the returnStoreId
	 */
	@XmlElement(name = "returnStoreId")
	public String getReturnStoreId()
	{
		return returnStoreId;
	}
	/**
	 * @return the returnPickupDateFrom
	 */
	@XmlElement(name = "returnPickupDateFrom")
	public String getReturnPickupDateFrom()
	{
		return returnPickupDateFrom;
	}
	/**
	 * @return the returnPickupDateTo
	 */
	@XmlElement(name = "returnPickupDateTo")
	public String getReturnPickupDateTo()
	{
		return returnPickupDateTo;
	}
	/**
	 * @return the flag
	 */
	@XmlElement(name = "Flag")
	public String getFlag()
	{
		return flag;
	}
	/**
	 * @return the storeCreditNo
	 */
	@XmlElement(name = "storeCreditNo")
	public String getStoreCreditNo()
	{
		return storeCreditNo;
	}
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID)
	{
		this.customerID = customerID;
	}
	
	/**
	 * @return the errorCode
	 */
	@XmlElement(name = "errorCode")
	public String getErrorCode()
	{
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	/**
	 * @param ecomRequestId the ecomRequestId to set
	 */
	public void setEcomRequestId(String ecomRequestId)
	{
		this.ecomRequestId = ecomRequestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId)
	{
		this.ticketId = ticketId;
	}
	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(String orderID)
	{
		this.orderID = orderID;
	}
	/**
	 * @param lineItemId the lineItemId to set
	 */
	public void setLineItemId(String lineItemId)
	{
		this.lineItemId = lineItemId;
	}
	/**
	 * @param ticketType the ticketType to set
	 */
	public void setTicketType(String ticketType)
	{
		this.ticketType = ticketType;
	}
	/**
	 * @param ticketSubType the ticketSubType to set
	 */
	public void setTicketSubType(String ticketSubType)
	{
		this.ticketSubType = ticketSubType;
	}
	/**
	 * @param canc_Ret_Reas the canc_Ret_Reas to set
	 */
	public void setCanc_Ret_Reas(String canc_Ret_Reas)
	{
		this.canc_Ret_Reas = canc_Ret_Reas;
	}
	/**
	 * @param refund_Type the refund_Type to set
	 */
	public void setRefund_Type(String refund_Type)
	{
		this.refund_Type = refund_Type;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo)
	{
		this.phoneNo = phoneNo;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}
	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3)
	{
		this.address3 = address3;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode)
	{
		this.pincode = pincode;
	}
	/**
	 * @param landmark the landmark to set
	 */
	public void setLandmark(String landmark)
	{
		this.landmark = landmark;
	}
	/**
	 * @param returnStoreId the returnStoreId to set
	 */
	public void setReturnStoreId(String returnStoreId)
	{
		this.returnStoreId = returnStoreId;
	}
	/**
	 * @param returnPickupDateFrom the returnPickupDateFrom to set
	 */
	public void setReturnPickupDateFrom(String returnPickupDateFrom)
	{
		this.returnPickupDateFrom = returnPickupDateFrom;
	}
	/**
	 * @param returnPickupDateTo the returnPickupDateTo to set
	 */
	public void setReturnPickupDateTo(String returnPickupDateTo)
	{
		this.returnPickupDateTo = returnPickupDateTo;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	/**
	 * @param storeCreditNo the storeCreditNo to set
	 */
	public void setStoreCreditNo(String storeCreditNo)
	{
		this.storeCreditNo = storeCreditNo;
	}
}
