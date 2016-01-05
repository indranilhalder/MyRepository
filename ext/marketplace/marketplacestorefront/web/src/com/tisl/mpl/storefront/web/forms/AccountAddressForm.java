/**
 *
 */
package com.tisl.mpl.storefront.web.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author @TCS
 *
 */
public class AccountAddressForm extends AddressForm
{

	private String addressType;
	private String state;
	private String mobileNo;
	private String line3;
	private String locality;



	/**
	 * @return the locality
	 */
	public String getLocality()
	{
		return locality;
	}

	/**
	 * @param locality
	 *           the locality to set
	 */
	public void setLocality(final String locality)
	{
		this.locality = locality;
	}

	/**
	 * @return the line3
	 */
	public String getLine3()
	{
		return line3;
	}

	/**
	 * @param line3
	 *           the line3 to set
	 */
	public void setLine3(final String line3)
	{
		this.line3 = line3;
	}

	/**
	 * @return the addressType
	 */
	@NotNull(message = "{address.addressType.invalid}")
	@Size(min = 1, max = 255, message = "{address.addressType.invalid}")
	public String getAddressType()
	{
		return addressType;
	}

	/**
	 * @param addressType
	 *           the addressType to set
	 */
	public void setAddressType(final String addressType)
	{
		this.addressType = addressType;
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
	 * @return the mobileNo
	 */
	@Size(min = 10, max = 10, message = "{address.addressType.invalid}")
	public String getMobileNo()
	{
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *           the mobileNo to set
	 */
	public void setMobileNo(final String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

}
