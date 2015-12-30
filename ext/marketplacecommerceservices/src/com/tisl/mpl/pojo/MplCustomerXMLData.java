/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomerCreateUpdate" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="UpSertFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="isBlackListed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="DateOfMarriage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="EmailID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *                   &lt;element name="CommPreferences">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="CommPref" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Frequency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Subscription">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Categories">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="SubCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Addresses" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Address1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Address2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Address3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                             &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                             &lt;element name="Pincode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="defaultFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "customerCreateUpdate" })
@XmlRootElement(name = "Customers")
public class MplCustomerXMLData
{

	@XmlElement(name = "CustomerCreateUpdate")
	private List<MplCustomerXMLData.CustomerCreateUpdate> customerCreateUpdate;

	/**
	 * Gets the value of the customerCreateUpdate property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the customerCreateUpdate property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getCustomerCreateUpdate().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MplCustomerXMLData }
	 *
	 *
	 */
	public List<MplCustomerXMLData.CustomerCreateUpdate> getCustomerCreateUpdate()
	{
		if (customerCreateUpdate == null)
		{
			customerCreateUpdate = new ArrayList<MplCustomerXMLData.CustomerCreateUpdate>();
		}
		return this.customerCreateUpdate;
	}


	/**
	 * <p>
	 * Java class for anonymous complex type.
	 *
	 * <p>
	 * The following schema fragment specifies the expected content contained within this class.
	 *
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="UpSertFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
	 *         &lt;element name="isBlackListed" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="DateOfMarriage" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="EmailID" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}long"/>
	 *         &lt;element name="CommPreferences">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="CommPref" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                   &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                 &lt;/sequence>
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *         &lt;element name="Frequency" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="Subscription">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="Categories">
	 *                     &lt;complexType>
	 *                       &lt;complexContent>
	 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                           &lt;sequence>
	 *                             &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                             &lt;element name="SubCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                             &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                           &lt;/sequence>
	 *                         &lt;/restriction>
	 *                       &lt;/complexContent>
	 *                     &lt;/complexType>
	 *                   &lt;/element>
	 *                   &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                 &lt;/sequence>
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *         &lt;element name="Addresses" maxOccurs="unbounded" minOccurs="0">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="Address1" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                   &lt;element name="Address2" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                   &lt;element name="Address3" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                   &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *                   &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                   &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}byte"/>
	 *                   &lt;element name="Pincode" type="{http://www.w3.org/2001/XMLSchema}int"/>
	 *                   &lt;element name="defaultFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                 &lt;/sequence>
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 *
	 *
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder =
	{ "upSertFlag", "customerID", "isBlackListed", "firstName", "lastName", "dateOfBirth", "dateOfMarriage", "gender", "emailID",
			"phoneNumber", "commPreferences", "frequency", "subscription", "addresses" })
	public static class CustomerCreateUpdate
	{

		@XmlElement(name = "UpSertFlag", required = true)
		private String upSertFlag;
		@XmlElement(name = "CustomerID")
		private String customerID;
		@XmlElement(required = true)
		private String isBlackListed;
		@XmlElement(name = "FirstName", required = true)
		private String firstName;
		@XmlElement(name = "LastName", required = true)
		private String lastName;
		@XmlElement(name = "DateOfBirth", required = true)
		private String dateOfBirth;
		@XmlElement(name = "DateOfMarriage", required = true)
		private String dateOfMarriage;
		@XmlElement(name = "Gender", required = true)
		private String gender;
		@XmlElement(name = "EmailID", required = true)
		private String emailID;
		@XmlElement(name = "PhoneNumber")
		private String phoneNumber;
		@XmlElement(name = "CommPreferences", required = true)
		private MplCustomerXMLData.CustomerCreateUpdate.CommPreferences commPreferences;
		@XmlElement(name = "Frequency", required = true)
		private String frequency;
		@XmlElement(name = "Subscription", required = true)
		private MplCustomerXMLData.CustomerCreateUpdate.Subscription subscription;
		@XmlElement(name = "Addresses")
		private List<MplCustomerXMLData.CustomerCreateUpdate.Addresses> addresses;

		/**
		 * Gets the value of the upSertFlag property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getUpSertFlag()
		{
			return upSertFlag;
		}

		/**
		 * Sets the value of the upSertFlag property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setUpSertFlag(final String value)
		{
			this.upSertFlag = value;
		}

		/**
		 * Gets the value of the customerID property.
		 *
		 */
		public String getCustomerID()
		{
			return customerID;
		}

		/**
		 * Sets the value of the customerID property.
		 *
		 */
		public void setCustomerID(final String value)
		{
			this.customerID = value;
		}

		/**
		 * Gets the value of the isBlackListed property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getIsBlackListed()
		{
			return isBlackListed;
		}

		/**
		 * Sets the value of the isBlackListed property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setIsBlackListed(final String value)
		{
			this.isBlackListed = value;
		}

		/**
		 * Gets the value of the firstName property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getFirstName()
		{
			return firstName;
		}

		/**
		 * Sets the value of the firstName property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setFirstName(final String value)
		{
			this.firstName = value;
		}

		/**
		 * Gets the value of the lastName property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getLastName()
		{
			return lastName;
		}

		/**
		 * Sets the value of the lastName property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setLastName(final String value)
		{
			this.lastName = value;
		}

		/**
		 * Gets the value of the dateOfBirth property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getDateOfBirth()
		{
			return dateOfBirth;
		}

		/**
		 * Sets the value of the dateOfBirth property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setDateOfBirth(final String value)
		{
			this.dateOfBirth = value;
		}

		/**
		 * Gets the value of the dateOfMarriage property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getDateOfMarriage()
		{
			return dateOfMarriage;
		}

		/**
		 * Sets the value of the dateOfMarriage property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setDateOfMarriage(final String value)
		{
			this.dateOfMarriage = value;
		}

		/**
		 * Gets the value of the gender property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getGender()
		{
			return gender;
		}

		/**
		 * Sets the value of the gender property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setGender(final String value)
		{
			this.gender = value;
		}

		/**
		 * Gets the value of the emailID property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getEmailID()
		{
			return emailID;
		}

		/**
		 * Sets the value of the emailID property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setEmailID(final String value)
		{
			this.emailID = value;
		}

		/**
		 * Gets the value of the phoneNumber property.
		 *
		 */
		public String getPhoneNumber()
		{
			return phoneNumber;
		}

		/**
		 * Sets the value of the phoneNumber property.
		 *
		 */
		public void setPhoneNumber(final String value)
		{
			this.phoneNumber = value;
		}

		/**
		 * Gets the value of the commPreferences property.
		 *
		 * @return possible object is {@link MplCustomerXMLData.CustomerCreateUpdate.CommPreferences }
		 *
		 */
		public MplCustomerXMLData.CustomerCreateUpdate.CommPreferences getCommPreferences()
		{
			return commPreferences;
		}

		/**
		 * Sets the value of the commPreferences property.
		 *
		 * @param value
		 *           allowed object is {@link MplCustomerXMLData.CustomerCreateUpdate.CommPreferences }
		 *
		 */
		public void setCommPreferences(final MplCustomerXMLData.CustomerCreateUpdate.CommPreferences value)
		{
			this.commPreferences = value;
		}

		/**
		 * Gets the value of the frequency property.
		 *
		 * @return possible object is {@link String }
		 *
		 */
		public String getFrequency()
		{
			return frequency;
		}

		/**
		 * Sets the value of the frequency property.
		 *
		 * @param value
		 *           allowed object is {@link String }
		 *
		 */
		public void setFrequency(final String value)
		{
			this.frequency = value;
		}

		/**
		 * Gets the value of the subscription property.
		 *
		 * @return possible object is {@link MplCustomerXMLData.CustomerCreateUpdate.Subscription }
		 *
		 */
		public MplCustomerXMLData.CustomerCreateUpdate.Subscription getSubscription()
		{
			return subscription;
		}

		/**
		 * Sets the value of the subscription property.
		 *
		 * @param value
		 *           allowed object is {@link MplCustomerXMLData.CustomerCreateUpdate.Subscription }
		 *
		 */
		public void setSubscription(final MplCustomerXMLData.CustomerCreateUpdate.Subscription value)
		{
			this.subscription = value;
		}

		/**
		 * Gets the value of the addresses property.
		 *
		 * <p>
		 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make
		 * to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method
		 * for the addresses property.
		 *
		 * <p>
		 * For example, to add a new item, do as follows:
		 *
		 * <pre>
		 * getAddresses().add(newItem);
		 * </pre>
		 *
		 *
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link MplCustomerXMLData.CustomerCreateUpdate.Addresses }
		 *
		 *
		 */
		public List<MplCustomerXMLData.CustomerCreateUpdate.Addresses> getAddresses()
		{
			if (addresses == null)
			{
				addresses = new ArrayList<MplCustomerXMLData.CustomerCreateUpdate.Addresses>();
			}
			return this.addresses;
		}


		/**
		 * <p>
		 * Java class for anonymous complex type.
		 *
		 * <p>
		 * The following schema fragment specifies the expected content contained within this class.
		 *
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="Address1" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *         &lt;element name="Address2" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *         &lt;element name="Address3" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *         &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}byte"/>
		 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}byte"/>
		 *         &lt;element name="Pincode" type="{http://www.w3.org/2001/XMLSchema}int"/>
		 *         &lt;element name="defaultFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *       &lt;/sequence>
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 *
		 *
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder =
		{ "address1", "address2", "address3", "country", "city", "state", "pincode", "defaultFlag" })
		public static class Addresses
		{

			@XmlElement(name = "Address1", required = true)
			private String address1;
			@XmlElement(name = "Address2", required = true)
			private String address2;
			@XmlElement(name = "Address3", required = true)
			private String address3;
			@XmlElement(name = "Country")
			private String country;
			@XmlElement(name = "City", required = true)
			private String city;
			@XmlElement(name = "State")
			private String state;
			@XmlElement(name = "Pincode")
			private String pincode;
			@XmlElement(required = true)
			private String defaultFlag;

			/**
			 * Gets the value of the address1 property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getAddress1()
			{
				return address1;
			}

			/**
			 * Sets the value of the address1 property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setAddress1(final String value)
			{
				this.address1 = value;
			}

			/**
			 * Gets the value of the address2 property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getAddress2()
			{
				return address2;
			}

			/**
			 * Sets the value of the address2 property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setAddress2(final String value)
			{
				this.address2 = value;
			}

			/**
			 * Gets the value of the address3 property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getAddress3()
			{
				return address3;
			}

			/**
			 * Sets the value of the address3 property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setAddress3(final String value)
			{
				this.address3 = value;
			}

			/**
			 * Gets the value of the country property.
			 *
			 */
			public String getCountry()
			{
				return country;
			}

			/**
			 * Sets the value of the country property.
			 *
			 */
			public void setCountry(final String value)
			{
				this.country = value;
			}

			/**
			 * Gets the value of the city property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getCity()
			{
				return city;
			}

			/**
			 * Sets the value of the city property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setCity(final String value)
			{
				this.city = value;
			}

			/**
			 * Gets the value of the state property.
			 *
			 */
			public String getState()
			{
				return state;
			}

			/**
			 * Sets the value of the state property.
			 *
			 */
			public void setState(final String value)
			{
				this.state = value;
			}

			/**
			 * Gets the value of the pincode property.
			 *
			 */
			public String getPincode()
			{
				return pincode;
			}

			/**
			 * Sets the value of the pincode property.
			 *
			 */
			public void setPincode(final String value)
			{
				this.pincode = value;
			}

			/**
			 * Gets the value of the defaultFlag property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getDefaultFlag()
			{
				return defaultFlag;
			}

			/**
			 * Sets the value of the defaultFlag property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setDefaultFlag(final String value)
			{
				this.defaultFlag = value;
			}

		}


		/**
		 * <p>
		 * Java class for anonymous complex type.
		 *
		 * <p>
		 * The following schema fragment specifies the expected content contained within this class.
		 *
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="CommPref" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *         &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *       &lt;/sequence>
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 *
		 *
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder =
		{ "commPref", "subscribed" })
		public static class CommPreferences
		{

			@XmlElement(name = "CommPref", required = true)
			private String commPref;
			@XmlElement(name = "Subscribed", required = true)
			private String subscribed;

			/**
			 * Gets the value of the commPref property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getCommPref()
			{
				return commPref;
			}

			/**
			 * Sets the value of the commPref property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setCommPref(final String value)
			{
				this.commPref = value;
			}

			/**
			 * Gets the value of the subscribed property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getSubscribed()
			{
				return subscribed;
			}

			/**
			 * Sets the value of the subscribed property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setSubscribed(final String value)
			{
				this.subscribed = value;
			}

		}


		/**
		 * <p>
		 * Java class for anonymous complex type.
		 *
		 * <p>
		 * The following schema fragment specifies the expected content contained within this class.
		 *
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="Categories">
		 *           &lt;complexType>
		 *             &lt;complexContent>
		 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *                 &lt;sequence>
		 *                   &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *                   &lt;element name="SubCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *                   &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *                 &lt;/sequence>
		 *               &lt;/restriction>
		 *             &lt;/complexContent>
		 *           &lt;/complexType>
		 *         &lt;/element>
		 *         &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *       &lt;/sequence>
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 *
		 *
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder =
		{ "categories", "subscribed" })
		public static class Subscription
		{

			@XmlElement(name = "Categories", required = true)
			private MplCustomerXMLData.CustomerCreateUpdate.Subscription.Categories categories;
			@XmlElement(name = "Subscribed", required = true)
			private String subscribed;

			/**
			 * Gets the value of the categories property.
			 *
			 * @return possible object is {@link MplCustomerXMLData.CustomerCreateUpdate.Subscription.Categories }
			 *
			 */
			public MplCustomerXMLData.CustomerCreateUpdate.Subscription.Categories getCategories()
			{
				return categories;
			}

			/**
			 * Sets the value of the categories property.
			 *
			 * @param value
			 *           allowed object is {@link MplCustomerXMLData.CustomerCreateUpdate.Subscription.Categories }
			 *
			 */
			public void setCategories(final MplCustomerXMLData.CustomerCreateUpdate.Subscription.Categories value)
			{
				this.categories = value;
			}

			/**
			 * Gets the value of the subscribed property.
			 *
			 * @return possible object is {@link String }
			 *
			 */
			public String getSubscribed()
			{
				return subscribed;
			}

			/**
			 * Sets the value of the subscribed property.
			 *
			 * @param value
			 *           allowed object is {@link String }
			 *
			 */
			public void setSubscribed(final String value)
			{
				this.subscribed = value;
			}


			/**
			 * <p>
			 * Java class for anonymous complex type.
			 *
			 * <p>
			 * The following schema fragment specifies the expected content contained within this class.
			 *
			 * <pre>
			 * &lt;complexType>
			 *   &lt;complexContent>
			 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
			 *       &lt;sequence>
			 *         &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string"/>
			 *         &lt;element name="SubCategory" type="{http://www.w3.org/2001/XMLSchema}string"/>
			 *         &lt;element name="Subscribed" type="{http://www.w3.org/2001/XMLSchema}string"/>
			 *       &lt;/sequence>
			 *     &lt;/restriction>
			 *   &lt;/complexContent>
			 * &lt;/complexType>
			 * </pre>
			 *
			 *
			 */
			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder =
			{ "category", "subCategory", "subscribed" })
			public static class Categories
			{

				@XmlElement(name = "Category", required = true)
				private String category;
				@XmlElement(name = "SubCategory", required = true)
				private String subCategory;
				@XmlElement(name = "Subscribed", required = true)
				private String subscribed;

				/**
				 * Gets the value of the category property.
				 *
				 * @return possible object is {@link String }
				 *
				 */
				public String getCategory()
				{
					return category;
				}

				/**
				 * Sets the value of the category property.
				 *
				 * @param value
				 *           allowed object is {@link String }
				 *
				 */
				public void setCategory(final String value)
				{
					this.category = value;
				}

				/**
				 * Gets the value of the subCategory property.
				 *
				 * @return possible object is {@link String }
				 *
				 */
				public String getSubCategory()
				{
					return subCategory;
				}

				/**
				 * Sets the value of the subCategory property.
				 *
				 * @param value
				 *           allowed object is {@link String }
				 *
				 */
				public void setSubCategory(final String value)
				{
					this.subCategory = value;
				}

				/**
				 * Gets the value of the subscribed property.
				 *
				 * @return possible object is {@link String }
				 *
				 */
				public String getSubscribed()
				{
					return subscribed;
				}

				/**
				 * Sets the value of the subscribed property.
				 *
				 * @param value
				 *           allowed object is {@link String }
				 *
				 */
				public void setSubscribed(final String value)
				{
					this.subscribed = value;
				}

			}

		}

	}

}
