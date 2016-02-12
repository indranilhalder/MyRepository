/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "Customer")
@XmlType(propOrder =
{ "custCreationFlag", "address1", "address2", "city", "district", "pincode", "country", "defaultFlag", "firstName", "lastName",
		"mobileNumber" })
public class CustomerUpdateAddressWSDTO
{

	private String custCreationFlag;
	private String country;

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

	private String city;
	private String district;
	private String pincode;
	private String defaultFlag;
	private String address1;
	private String address2;

	private String firstName;
	private String lastName;
	private String mobileNumber;

	/**
	 * @return the custCreationFlag
	 */
	public String getCustCreationFlag()
	{
		return custCreationFlag;
	}

	/**
	 * @param custCreationFlag
	 *           the custCreationFlag to set
	 */
	public void setCustCreationFlag(final String custCreationFlag)
	{
		this.custCreationFlag = custCreationFlag;
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
	 * @return the district
	 */
	public String getDistrict()
	{
		return district;
	}

	/**
	 * @param district
	 *           the district to set
	 */
	public void setDistrict(final String district)
	{
		this.district = district;
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
	 * @return the mobileNumber
	 */
	public String getMobileNumber()
	{
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *           the mobileNumber to set
	 */
	public void setMobileNumber(final String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}
}
