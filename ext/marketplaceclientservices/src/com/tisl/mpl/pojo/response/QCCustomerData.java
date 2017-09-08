/**
 *
 */
package com.tisl.mpl.pojo.response;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;


/**
 * @author TUL
 *
 */

@XmlRootElement(name = "QCCustomerData")
@XmlType(propOrder =
{ "pincode", "anniversary", "customerType", "dob", "area", "salutation", "phoneNumber", "country", "lastName", "corporateName",
		"city", "employeeId", "region", "email", "state", "gender", "firstName", "phoneAlternate", "maritalStatus", "addressLine1",
		"addressLine2", "addressLine3" })
public class QCCustomerData
{
	private String pincode;

	private DateTime anniversary;

	private String customerType;

	private DateTime dob;

	private String area;

	private String salutation;

	private String phoneNumber;

	private String country;

	private String lastName;

	private String corporateName;

	private String city;

	private String employeeId;

	private String region;

	private String email;

	private String state;

	private String gender;

	private String firstName;

	private String phoneAlternate;

	private String maritalStatus;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

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
	 * @return the anniversary
	 */
	public DateTime getAnniversary()
	{
		return anniversary;
	}

	/**
	 * @param anniversary
	 *           the anniversary to set
	 */
	public void setAnniversary(final DateTime anniversary)
	{
		this.anniversary = anniversary;
	}

	public String getCustomerType()
	{
		return customerType;
	}

	/**
	 * @param customerType
	 *           the customerType to set
	 */
	public void setCustomerType(final String customerType)
	{
		this.customerType = customerType;
	}


	/**
	 * @return the dob
	 */
	public DateTime getDob()
	{
		return dob;
	}

	/**
	 * @param dob
	 *           the dob to set
	 */
	public void setDob(final DateTime dob)
	{
		this.dob = dob;
	}

	/**
	 * @return the area
	 */
	public String getArea()
	{
		return area;
	}

	/**
	 * @param area
	 *           the area to set
	 */
	public void setArea(final String area)
	{
		this.area = area;
	}

	/**
	 * @return the salutation
	 */
	public String getSalutation()
	{
		return salutation;
	}

	/**
	 * @param salutation
	 *           the salutation to set
	 */
	public void setSalutation(final String salutation)
	{
		this.salutation = salutation;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *           the phoneNumber to set
	 */
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
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
	 * @return the corporateName
	 */
	public String getCorporateName()
	{
		return corporateName;
	}

	/**
	 * @param corporateName
	 *           the corporateName to set
	 */
	public void setCorporateName(final String corporateName)
	{
		this.corporateName = corporateName;
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
	 * @return the employeeId
	 */
	public String getEmployeeId()
	{
		return employeeId;
	}

	/**
	 * @param employeeId
	 *           the employeeId to set
	 */
	public void setEmployeeId(final String employeeId)
	{
		this.employeeId = employeeId;
	}

	/**
	 * @return the region
	 */
	public String getRegion()
	{
		return region;
	}

	/**
	 * @param region
	 *           the region to set
	 */
	public void setRegion(final String region)
	{
		this.region = region;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
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
	 * @return the gender
	 */
	public String getGender()
	{
		return gender;
	}

	/**
	 * @param gender
	 *           the gender to set
	 */
	public void setGender(final String gender)
	{
		this.gender = gender;
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
	 * @return the phoneAlternate
	 */
	public String getPhoneAlternate()
	{
		return phoneAlternate;
	}

	/**
	 * @param phoneAlternate
	 *           the phoneAlternate to set
	 */
	public void setPhoneAlternate(final String phoneAlternate)
	{
		this.phoneAlternate = phoneAlternate;
	}

	/**
	 * @return the maritalStatus
	 */
	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	/**
	 * @param maritalStatus
	 *           the maritalStatus to set
	 */
	public void setMaritalStatus(final String maritalStatus)
	{
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *           the addressLine1 to set
	 */
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *           the addressLine2 to set
	 */
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the addressLine3
	 */
	public String getAddressLine3()
	{
		return addressLine3;
	}

	/**
	 * @param addressLine3
	 *           the addressLine3 to set
	 */
	public void setAddressLine3(final String addressLine3)
	{
		this.addressLine3 = addressLine3;
	}


}
