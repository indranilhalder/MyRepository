/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class AddWsDTO implements java.io.Serializable
{
	public String name;
	public String address;
	public String pincode;
	public String city;
	public String state;
	public String mobileNumber;
	public String deliveryInstruction;

	//	public AddWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
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
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *           the address to set
	 */
	public void setAddress(final String address)
	{
		this.address = address;
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
	 * @return the deliveryInstruction
	 */
	public String getDeliveryInstruction()
	{
		return deliveryInstruction;
	}

	/**
	 * @param deliveryInstruction
	 *           the deliveryInstruction to set
	 */
	public void setDeliveryInstruction(final String deliveryInstruction)
	{
		this.deliveryInstruction = deliveryInstruction;
	}


}
