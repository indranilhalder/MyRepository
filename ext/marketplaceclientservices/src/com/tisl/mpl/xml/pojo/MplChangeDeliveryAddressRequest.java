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
@XmlRootElement(name = "changeDeliveryAddressRequest")
@XmlType(propOrder =
{ "OrderID", "FName", "LName", "EmailID", "PhoneNo", "Address1", "Address2", "Address3", "Landmark", "Country", "City", "State",
		"Pincode" })
public class MplChangeDeliveryAddressRequest
{
	private String OrderID;
	private String FName;
	private String LName;
	private String EmailID;
	private String PhoneNo;
	private String Address1;
	private String Address2;
	private String Address3;
	private String Landmark;
	private String Country;
	private String City;
	private String State;
	private String Pincode;

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "OrderID")
	public String getOrderID()
	{
		return OrderID;
	}

	public void setOrderID(final String orderID)
	{
		OrderID = orderID;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "FName")
	public String getFName()
	{
		return FName;
	}

	public void setFName(final String fName)
	{
		FName = fName;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "LName")
	public String getLName()
	{
		return LName;
	}

	public void setLName(final String lName)
	{
		LName = lName;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "EmailID")
	public String getEmailID()
	{
		return EmailID;
	}

	public void setEmailID(final String emailID)
	{
		EmailID = emailID;
	}

	/**
	 *
	 * @return FName
	 */
	@XmlElement(name = "PhoneNo")
	public String getPhoneNo()
	{
		return PhoneNo;
	}

	public void setPhoneNo(final String phoneNo)
	{
		PhoneNo = phoneNo;
	}

	/**
	 *
	 * @return Address1
	 */
	@XmlElement(name = "Address1")
	public String getAddress1()
	{
		return Address1;
	}


	public void setAddress1(final String address1)
	{
		Address1 = address1;
	}

	/**
	 *
	 * @return Address2
	 */
	@XmlElement(name = "Address2")
	public String getAddress2()
	{
		return Address2;
	}


	public void setAddress2(final String address2)
	{
		Address2 = address2;
	}

	/**
	 *
	 * @return Address3
	 */
	@XmlElement(name = "Address3")
	public String getAddress3()
	{
		return Address3;
	}


	public void setAddress3(final String address3)
	{
		Address3 = address3;
	}

	/**
	 *
	 * @return Landmark
	 */
	@XmlElement(name = "Landmark")
	public String getLandmark()
	{
		return Landmark;
	}

	public void setLandmark(final String landmark)
	{
		Landmark = landmark;
	}

	/**
	 *
	 * @return Country
	 */
	@XmlElement(name = "Country")
	public String getCountry()
	{
		return Country;
	}

	public void setCountry(final String country)
	{
		Country = country;
	}

	/**
	 *
	 * @return City
	 */
	@XmlElement(name = "City")
	public String getCity()
	{
		return City;
	}

	public void setCity(final String city)
	{
		City = city;
	}

	/**
	 *
	 * @return State
	 */
	@XmlElement(name = "State")
	public String getState()
	{
		return State;
	}

	public void setState(final String state)
	{
		State = state;
	}

	/**
	 *
	 * @return Pincode
	 */
	@XmlElement(name = "Pincode")
	public String getPincode()
	{
		return Pincode;
	}

	public void setPincode(final String pincode)
	{
		Pincode = pincode;
	}




}
