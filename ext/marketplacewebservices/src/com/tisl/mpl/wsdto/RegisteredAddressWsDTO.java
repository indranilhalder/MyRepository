/**
 *
 */
package com.tisl.mpl.wsdto;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author TCS
 *
 */
@XStreamAlias("registerAddress")
public class RegisteredAddressWsDTO
{
	private String RegisteredAddress1;
	private String RegisteredAddress2;
	private String RegisteredAddCountry;
	private String RegisteredAddState;
	private String RegisteredAddCity;
	private String RegisteredAddPin;
	private String RegisteredEmail;
	private String RegisteredPhone;
	private String RegisteredMobile;
	private String RegisteredFAX;

	/**
	 * @return the registeredAddress1
	 */
	public String getRegisteredAddress1()
	{
		return RegisteredAddress1;
	}

	/**
	 * @param registeredAddress1
	 *           the registeredAddress1 to set
	 */
	public void setRegisteredAddress1(final String registeredAddress1)
	{
		RegisteredAddress1 = registeredAddress1;
	}

	/**
	 * @return the registeredAddress2
	 */
	public String getRegisteredAddress2()
	{
		return RegisteredAddress2;
	}

	/**
	 * @param registeredAddress2
	 *           the registeredAddress2 to set
	 */
	public void setRegisteredAddress2(final String registeredAddress2)
	{
		RegisteredAddress2 = registeredAddress2;
	}

	/**
	 * @return the registeredAddCountry
	 */
	public String getRegisteredAddCountry()
	{
		return RegisteredAddCountry;
	}

	/**
	 * @param registeredAddCountry
	 *           the registeredAddCountry to set
	 */
	public void setRegisteredAddCountry(final String registeredAddCountry)
	{
		RegisteredAddCountry = registeredAddCountry;
	}

	/**
	 * @return the registeredAddState
	 */
	public String getRegisteredAddState()
	{
		return RegisteredAddState;
	}

	/**
	 * @param registeredAddState
	 *           the registeredAddState to set
	 */
	public void setRegisteredAddState(final String registeredAddState)
	{
		RegisteredAddState = registeredAddState;
	}

	/**
	 * @return the registeredAddCity
	 */
	public String getRegisteredAddCity()
	{
		return RegisteredAddCity;
	}

	/**
	 * @param registeredAddCity
	 *           the registeredAddCity to set
	 */
	public void setRegisteredAddCity(final String registeredAddCity)
	{
		RegisteredAddCity = registeredAddCity;
	}

	/**
	 * @return the registeredAddPin
	 */
	public String getRegisteredAddPin()
	{
		return RegisteredAddPin;
	}

	/**
	 * @param registeredAddPin
	 *           the registeredAddPin to set
	 */
	public void setRegisteredAddPin(final String registeredAddPin)
	{
		RegisteredAddPin = registeredAddPin;
	}

	/**
	 * @return the registeredEmail
	 */
	public String getRegisteredEmail()
	{
		return RegisteredEmail;
	}

	/**
	 * @param registeredEmail
	 *           the registeredEmail to set
	 */
	public void setRegisteredEmail(final String registeredEmail)
	{
		RegisteredEmail = registeredEmail;
	}

	/**
	 * @return the registeredPhone
	 */
	public String getRegisteredPhone()
	{
		return RegisteredPhone;
	}

	/**
	 * @param registeredPhone
	 *           the registeredPhone to set
	 */
	public void setRegisteredPhone(final String registeredPhone)
	{
		RegisteredPhone = registeredPhone;
	}

	/**
	 * @return the registeredMobile
	 */
	public String getRegisteredMobile()
	{
		return RegisteredMobile;
	}

	/**
	 * @param registeredMobile
	 *           the registeredMobile to set
	 */
	public void setRegisteredMobile(final String registeredMobile)
	{
		RegisteredMobile = registeredMobile;
	}

	/**
	 * @return the registeredFAX
	 */
	public String getRegisteredFAX()
	{
		return RegisteredFAX;
	}

	/**
	 * @param registeredFAX
	 *           the registeredFAX to set
	 */
	public void setRegisteredFAX(final String registeredFAX)
	{
		RegisteredFAX = registeredFAX;
	}



}
