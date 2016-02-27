/**
 *
 */
package com.tisl.mpl.wsdto;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author TCS
 *
 */
@XStreamAlias("corporateAddress")
public class CorporateAddressWsDTO
{
	private String CorporateAddress1;
	private String CorporateAddress2;
	private String CorporateAddCountry;
	private String CorporateAddState;
	private String CorporateAddCity;
	private int CorporateAddPin;
	private String CorporateEmail;
	private Long CorporatePhone;
	private Long CorporateMobile;

	/**
	 * @return the corporateAddress1
	 */
	public String getCorporateAddress1()
	{
		return CorporateAddress1;
	}

	/**
	 * @param corporateAddress1
	 *           the corporateAddress1 to set
	 */
	public void setCorporateAddress1(final String corporateAddress1)
	{
		CorporateAddress1 = corporateAddress1;
	}

	/**
	 * @return the corporateAddress2
	 */
	public String getCorporateAddress2()
	{
		return CorporateAddress2;
	}

	/**
	 * @param corporateAddress2
	 *           the corporateAddress2 to set
	 */
	public void setCorporateAddress2(final String corporateAddress2)
	{
		CorporateAddress2 = corporateAddress2;
	}

	/**
	 * @return the corporateAddCountry
	 */
	public String getCorporateAddCountry()
	{
		return CorporateAddCountry;
	}

	/**
	 * @param corporateAddCountry
	 *           the corporateAddCountry to set
	 */
	public void setCorporateAddCountry(final String corporateAddCountry)
	{
		CorporateAddCountry = corporateAddCountry;
	}

	/**
	 * @return the corporateAddState
	 */
	public String getCorporateAddState()
	{
		return CorporateAddState;
	}

	/**
	 * @param corporateAddState
	 *           the corporateAddState to set
	 */
	public void setCorporateAddState(final String corporateAddState)
	{
		CorporateAddState = corporateAddState;
	}

	/**
	 * @return the corporateAddCity
	 */
	public String getCorporateAddCity()
	{
		return CorporateAddCity;
	}

	/**
	 * @param corporateAddCity
	 *           the corporateAddCity to set
	 */
	public void setCorporateAddCity(final String corporateAddCity)
	{
		CorporateAddCity = corporateAddCity;
	}

	/**
	 * @return the corporateAddPin
	 */
	public int getCorporateAddPin()
	{
		return CorporateAddPin;
	}

	/**
	 * @param corporateAddPin
	 *           the corporateAddPin to set
	 */
	public void setCorporateAddPin(final int corporateAddPin)
	{
		CorporateAddPin = corporateAddPin;
	}

	/**
	 * @return the corporateEmail
	 */
	public String getCorporateEmail()
	{
		return CorporateEmail;
	}

	/**
	 * @param corporateEmail
	 *           the corporateEmail to set
	 */
	public void setCorporateEmail(final String corporateEmail)
	{
		CorporateEmail = corporateEmail;
	}

	/**
	 * @return the corporatePhone
	 */
	public Long getCorporatePhone()
	{
		return CorporatePhone;
	}

	/**
	 * @param corporatePhone
	 *           the corporatePhone to set
	 */
	public void setCorporatePhone(final Long corporatePhone)
	{
		CorporatePhone = corporatePhone;
	}

	/**
	 * @return the corporateMobile
	 */
	public Long getCorporateMobile()
	{
		return CorporateMobile;
	}

	/**
	 * @param corporateMobile
	 *           the corporateMobile to set
	 */
	public void setCorporateMobile(final Long corporateMobile)
	{
		CorporateMobile = corporateMobile;
	}

}

