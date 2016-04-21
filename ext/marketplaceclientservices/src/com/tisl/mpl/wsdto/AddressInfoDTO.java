/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Dileep
 *
 */
@XmlRootElement(name = "AddressInfo")
@XmlType(propOrder =
{ "shippingFirstName", "shippingLastName", "phoneNo", "address1", "address2", "address3", "country", "city", "state", "pincode",
		"landmark" })
public class AddressInfoDTO
{
	private String shippingFirstName;
	private String shippingLastName;
	private String phoneNo;
	private String address1;
	private String address2;
	private String address3;
	private String country;
	private String city;
	private String state;
	private String pincode;
	private String landmark;

	/**
	 * @return the shippingFirstName
	 */
	@XmlElement(name = "shippingFirstName")
	public String getShippingFirstName()
	{
		return shippingFirstName;
	}

	/**
	 * @param shippingFirstName
	 *           the shippingFirstName to set
	 */
	public void setShippingFirstName(final String shippingFirstName)
	{
		this.shippingFirstName = shippingFirstName;
	}

	/**
	 * @return the shippingLastName
	 */
	@XmlElement(name = "shippingLastName")
	public String getShippingLastName()
	{
		return shippingLastName;
	}

	/**
	 * @param shippingLastName
	 *           the shippingLastName to set
	 */
	public void setShippingLastName(final String shippingLastName)
	{
		this.shippingLastName = shippingLastName;
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
	 * @param phoneNo
	 *           the phoneNo to set
	 */
	public void setPhoneNo(final String phoneNo)
	{
		this.phoneNo = phoneNo;
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
	 * @param address2
	 *           the address2 to set
	 */
	public void setAddress2(final String address2)
	{
		this.address2 = address2;
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
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
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
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
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
	 * @param landmark
	 *           the landmark to set
	 */
	public void setLandmark(final String landmark)
	{
		this.landmark = landmark;
	}
}
