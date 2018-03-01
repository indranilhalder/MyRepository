/**
 *
 */
package com.tisl.mpl.pojo.response;

/**
 * @author Nirav Bhanushali
 *
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "CustomerType", "Salutation", "Firstname", "LastName", "PhoneNumber", "Email", "DOB", "AddressLine1", "AddressLine2",
		"AddressLine3", "City", "State", "Country", "Gender", "Anniversary", "MaritalStatus", "EmployeeID", "PhoneAlternate",
		"PinCode", "Region", "Area", "CorporateName" })
public class Customer
{

	@JsonProperty("CustomerType")
	private String customerType;
	@JsonProperty("Salutation")
	private String salutation;
	@JsonProperty("Firstname")
	private String firstname;
	@JsonProperty("LastName")
	private String lastName;
	@JsonProperty("PhoneNumber")
	private String phoneNumber;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("DOB")
	private String dOB;
	@JsonProperty("AddressLine1")
	private String addressLine1;
	@JsonProperty("AddressLine2")
	private String addressLine2;
	@JsonProperty("AddressLine3")
	private Object addressLine3;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("Gender")
	private String gender;
	@JsonProperty("Anniversary")
	private String anniversary;
	@JsonProperty("MaritalStatus")
	private String maritalStatus;
	@JsonProperty("EmployeeID")
	private String employeeID;
	@JsonProperty("PhoneAlternate")
	private String phoneAlternate;
	@JsonProperty("PinCode")
	private String pinCode;
	@JsonProperty("Region")
	private Object region;
	@JsonProperty("Area")
	private String area;
	@JsonProperty("CorporateName")
	private String corporateName;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("CustomerType")
	public String getCustomerType()
	{
		return customerType;
	}

	@JsonProperty("CustomerType")
	public void setCustomerType(final String customerType)
	{
		this.customerType = customerType;
	}

	@JsonProperty("Salutation")
	public String getSalutation()
	{
		return salutation;
	}

	@JsonProperty("Salutation")
	public void setSalutation(final String salutation)
	{
		this.salutation = salutation;
	}

	@JsonProperty("Firstname")
	public String getFirstname()
	{
		return firstname;
	}

	@JsonProperty("Firstname")
	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	@JsonProperty("LastName")
	public String getLastName()
	{
		return lastName;
	}

	@JsonProperty("LastName")
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	@JsonProperty("PhoneNumber")
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	@JsonProperty("PhoneNumber")
	public void setPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	@JsonProperty("Email")
	public String getEmail()
	{
		return email;
	}

	@JsonProperty("Email")
	public void setEmail(final String email)
	{
		this.email = email;
	}

	@JsonProperty("DOB")
	public String getDOB()
	{
		return dOB;
	}

	@JsonProperty("DOB")
	public void setDOB(final String dOB)
	{
		this.dOB = dOB;
	}

	@JsonProperty("AddressLine1")
	public String getAddressLine1()
	{
		return addressLine1;
	}

	@JsonProperty("AddressLine1")
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	@JsonProperty("AddressLine2")
	public String getAddressLine2()
	{
		return addressLine2;
	}

	@JsonProperty("AddressLine2")
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	@JsonProperty("AddressLine3")
	public Object getAddressLine3()
	{
		return addressLine3;
	}

	@JsonProperty("AddressLine3")
	public void setAddressLine3(final Object addressLine3)
	{
		this.addressLine3 = addressLine3;
	}

	@JsonProperty("City")
	public String getCity()
	{
		return city;
	}

	@JsonProperty("City")
	public void setCity(final String city)
	{
		this.city = city;
	}

	@JsonProperty("State")
	public String getState()
	{
		return state;
	}

	@JsonProperty("State")
	public void setState(final String state)
	{
		this.state = state;
	}

	@JsonProperty("Country")
	public String getCountry()
	{
		return country;
	}

	@JsonProperty("Country")
	public void setCountry(final String country)
	{
		this.country = country;
	}

	@JsonProperty("Gender")
	public String getGender()
	{
		return gender;
	}

	@JsonProperty("Gender")
	public void setGender(final String gender)
	{
		this.gender = gender;
	}

	@JsonProperty("Anniversary")
	public String getAnniversary()
	{
		return anniversary;
	}

	@JsonProperty("Anniversary")
	public void setAnniversary(final String anniversary)
	{
		this.anniversary = anniversary;
	}

	@JsonProperty("MaritalStatus")
	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	@JsonProperty("MaritalStatus")
	public void setMaritalStatus(final String maritalStatus)
	{
		this.maritalStatus = maritalStatus;
	}

	@JsonProperty("EmployeeID")
	public String getEmployeeID()
	{
		return employeeID;
	}

	@JsonProperty("EmployeeID")
	public void setEmployeeID(final String employeeID)
	{
		this.employeeID = employeeID;
	}

	@JsonProperty("PhoneAlternate")
	public String getPhoneAlternate()
	{
		return phoneAlternate;
	}

	@JsonProperty("PhoneAlternate")
	public void setPhoneAlternate(final String phoneAlternate)
	{
		this.phoneAlternate = phoneAlternate;
	}

	@JsonProperty("PinCode")
	public String getPinCode()
	{
		return pinCode;
	}

	@JsonProperty("PinCode")
	public void setPinCode(final String pinCode)
	{
		this.pinCode = pinCode;
	}

	@JsonProperty("Region")
	public Object getRegion()
	{
		return region;
	}

	@JsonProperty("Region")
	public void setRegion(final Object region)
	{
		this.region = region;
	}

	@JsonProperty("Area")
	public String getArea()
	{
		return area;
	}

	@JsonProperty("Area")
	public void setArea(final String area)
	{
		this.area = area;
	}

	@JsonProperty("CorporateName")
	public String getCorporateName()
	{
		return corporateName;
	}

	@JsonProperty("CorporateName")
	public void setCorporateName(final String corporateName)
	{
		this.corporateName = corporateName;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value)
	{
		this.additionalProperties.put(name, value);
	}

}