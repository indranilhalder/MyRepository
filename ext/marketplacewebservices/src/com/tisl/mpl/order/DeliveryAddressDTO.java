/**
 *
 */
package com.tisl.mpl.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "deliveryAddress")
public class DeliveryAddressDTO
{
	private String addressLine1;
	private String addressLine2;
	private String cityName;
	private String countryIso3166Alpha2Code;
	private String countryName;
	private String countrySubentity;
	private String latitudeValue;
	private String longitudeValue;
	private String name;
	private String phoneNumber;
	private String postalZone;

	/**
	 * @return the addressLine1
	 */
	@XmlElement(name = "addressLine1")
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	@XmlElement(name = "addressLine2")
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	 * @return the cityName
	 */
	@XmlElement(name = "cityName")
	public String getCityName()
	{
		return cityName;
	}

	/**
	 * @return the countryIso3166Alpha2Code
	 */
	@XmlElement(name = "countryIso3166Alpha2Code")
	public String getCountryIso3166Alpha2Code()
	{
		return countryIso3166Alpha2Code;
	}

	/**
	 * @return the countryName
	 */
	@XmlElement(name = "countryName")
	public String getCountryName()
	{
		return countryName;
	}

	/**
	 * @return the countrySubentity
	 */
	@XmlElement(name = "countrySubentity")
	public String getCountrySubentity()
	{
		return countrySubentity;
	}

	/**
	 * @return the latitudeValue
	 */
	@XmlElement(name = "latitudeValue")
	public String getLatitudeValue()
	{
		return latitudeValue;
	}

	/**
	 * @return the longitudeValue
	 */
	@XmlElement(name = "longitudeValue")
	public String getLongitudeValue()
	{
		return longitudeValue;
	}

	/**
	 * @return the name
	 */
	@XmlElement(name = "name")
	public String getName()
	{
		return name;
	}

	/**
	 * @return the phoneNumber
	 */
	@XmlElement(name = "phoneNumber")
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * @return the postalZone
	 */
	@XmlElement(name = "postalZone")
	public String getPostalZone()
	{
		return postalZone;
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
	 * @param addressLine2
	 *           the addressLine2 to set
	 */
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	/**
	 * @param cityName
	 *           the cityName to set
	 */
	public void setCityName(final String cityName)
	{
		this.cityName = cityName;
	}

	/**
	 * @param countryIso3166Alpha2Code
	 *           the countryIso3166Alpha2Code to set
	 */
	public void setCountryIso3166Alpha2Code(final String countryIso3166Alpha2Code)
	{
		this.countryIso3166Alpha2Code = countryIso3166Alpha2Code;
	}

	/**
	 * @param countryName
	 *           the countryName to set
	 */
	public void setCountryName(final String countryName)
	{
		this.countryName = countryName;
	}

	/**
	 * @param countrySubentity
	 *           the countrySubentity to set
	 */
	public void setCountrySubentity(final String countrySubentity)
	{
		this.countrySubentity = countrySubentity;
	}

	/**
	 * @param latitudeValue
	 *           the latitudeValue to set
	 */
	public void setLatitudeValue(final String latitudeValue)
	{
		this.latitudeValue = latitudeValue;
	}

	/**
	 * @param longitudeValue
	 *           the longitudeValue to set
	 */
	public void setLongitudeValue(final String longitudeValue)
	{
		this.longitudeValue = longitudeValue;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
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
	 * @param postalZone
	 *           the postalZone to set
	 */
	public void setPostalZone(final String postalZone)
	{
		this.postalZone = postalZone;
	}







}
