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
{ "custCreationFlag", "customerID", "emailID", "firstName", "lastName", "dateOfBirth", "mobileNumber", "gender",
		"dateOfAnniversary" })
public class CustomerUpdateTrialWSDTO
{
	private String custCreationFlag;
	private String customerID;
	private String emailID;

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
	 * @return the customerID
	 */
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
	 * @return the emailID
	 */
	public String getEmailID()
	{
		return emailID;
	}

	/**
	 * @param emailID
	 *           the emailID to set
	 */
	public void setEmailID(final String emailID)
	{
		this.emailID = emailID;
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
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *           the dateOfBirth to set
	 */
	public void setDateOfBirth(final String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
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
	 * @return the dateOfAnniversary
	 */
	public String getDateOfAnniversary()
	{
		return dateOfAnniversary;
	}

	/**
	 * @param dateOfAnniversary
	 *           the dateOfAnniversary to set
	 */
	public void setDateOfAnniversary(final String dateOfAnniversary)
	{
		this.dateOfAnniversary = dateOfAnniversary;
	}

	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String mobileNumber;
	private String gender;
	private String dateOfAnniversary;

}
