/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 884206
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "customerId", "emailId", "firstName", "lastName", "phoneNo", "address1", "address2", "address3", "country", "city", "state",
		"pincode", "defaultFlag", "addresstag" })
public class CustomerAddressWsDTO
{
	@XmlElement(name = "customerId")
	private String customerId;
	@XmlElement(name = "emailId")
	private String emailId;
	@XmlElement(name = "firstName")
	private String firstName;
	@XmlElement(name = "lastName")
	private String lastName;
	@XmlElement(name = "phoneNo")
	private String phoneNo;
	@XmlElement(name = "address1")
	private String address1;
	@XmlElement(name = "address2")
	private String address2;
	@XmlElement(name = "address3")
	private String address3;
	@XmlElement(name = "country")
	private String country;
	@XmlElement(name = "city")
	private String city;
	@XmlElement(name = "state")
	private String state;
	@XmlElement(name = "pincode")
	private String pincode;
	@XmlElement(name = "defaultFlag")
	private String defaultFlag;
	@XmlElement(name = "addresstag")
	private String addresstag;

	/*
	 * public CustomerAddressWsDTO() {
	 * 
	 * }
	 */

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId
	 *           the customerId to set
	 */
	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId()
	{
		return emailId;
	}

	/**
	 * @param emailId
	 *           the emailId to set
	 */
	public void setEmailId(final String emailId)
	{
		this.emailId = emailId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo()
	{
		return phoneNo;
	}

	/**
	 * @param phoneNo
	 *           the phoneNo to set
	 */
	public void setPhoneNo(final String phoneNo)
	{
		this.phoneNo = phoneNo;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1()
	{
		return address1;
	}

	/**
	 * @param address1
	 *           the address1 to set
	 */
	public void setAddress1(final String address1)
	{
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2()
	{
		return address2;
	}

	/**
	 * @param address2
	 *           the address2 to set
	 */
	public void setAddress2(final String address2)
	{
		this.address2 = address2;
	}

	/**
	 * @return the address3
	 */
	public String getAddress3()
	{
		return address3;
	}

	/**
	 * @param address3
	 *           the address3 to set
	 */
	public void setAddress3(final String address3)
	{
		this.address3 = address3;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the pincode
	 */
	public String getPincode()
	{
		return pincode;
	}

	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		this.pincode = pincode;
	}

	/**
	 * @return the defaultFlag
	 */
	public String getDefaultFlag()
	{
		return defaultFlag;
	}

	/**
	 * @param defaultFlag
	 *           the defaultFlag to set
	 */
	public void setDefaultFlag(final String defaultFlag)
	{
		this.defaultFlag = defaultFlag;
	}

	/**
	 * @return the addresstag
	 */
	public String getAddresstag()
	{
		return addresstag;
	}

	/**
	 * @param addresstag
	 *           the addresstag to set
	 */
	public void setAddresstag(final String addresstag)
	{
		this.addresstag = addresstag;
	}




}
