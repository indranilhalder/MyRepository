/**
 *
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author prasad1
 *
 */
@XmlRootElement(name = "ChangeDeliveryAddress")
@XmlType(propOrder =
{ "orderID", "FName", "LName", "emailID", "phoneNo", "address1", "address2", "address3", "landmark", "country", "city", "state",
		"pincode" })
public class MplChangeDeliveryAddressRequest
{
	private String orderID;
	private String fName;
	private String lName;
	private String emailID;
	private String phoneNo;
	private String address1;
	private String address2;
	private String address3;
	private String landmark;
	private String country;
	private String city;
	private String state;
	private String pincode;

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "OrderID")
	public String getOrderID()
	{
		return orderID;
	}

	public void setOrderID(final String orderId)
	{
		orderID = orderId;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "FName")
	public String getFName()
	{
		return fName;
	}

	public void setFName(final String fName)
	{
		this.fName = fName;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "LName")
	public String getLName()
	{
		return lName;
	}

	public void setLName(final String lName)
	{
		this.lName = lName;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "EmailID")
	public String getEmailID()
	{
		return emailID;
	}

	public void setEmailID(final String emailID)
	{
		this.emailID = emailID;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "PhoneNo")
	public String getPhoneNo()
	{
		return phoneNo;
	}

	public void setPhoneNo(final String phoneNo)
	{
		this.phoneNo = phoneNo;
	}

	/**
	 *
	 * @return Address1
	 */
	@XmlElement(name = "Address1")
	public String getAddress1()
	{
		return address1;
	}


	public void setAddress1(final String address1)
	{
		this.address1 = address1;
	}

	/**
	 *
	 * @return Address2
	 */
	@XmlElement(name = "Address2")
	public String getaddress2()
	{
		return address2;
	}


	public void setAddress2(final String address2)
	{
		this.address2 = address2;
	}

	/**
	 *
	 * @return Address3
	 */
	@XmlElement(name = "Address3")
	public String getAddress3()
	{
		return address3;
	}


	public void setAddress3(final String address3)
	{
		this.address3 = address3;
	}

	/**
	 *
	 * @return Landmark
	 */
	@XmlElement(name = "Landmark")
	public String getLandmark()
	{
		return landmark;
	}

	public void setLandmark(final String landmark)
	{
		this.landmark = landmark;
	}

	/**
	 *
	 * @return Country
	 */
	@XmlElement(name = "Country")
	public String getCountry()
	{
		return country;
	}

	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 *
	 * @return City
	 */
	@XmlElement(name = "City")
	public String getCity()
	{
		return city;
	}

	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 *
	 * @return State
	 */
	@XmlElement(name = "State")
	public String getState()
	{
		return state;
	}

	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 *
	 * @return Pincode
	 */
	@XmlElement(name = "Pincode")
	public String getPincode()
	{
		return pincode;
	}

	public void setPincode(final String pincode)
	{
		this.phoneNo = pincode;
	}




}
