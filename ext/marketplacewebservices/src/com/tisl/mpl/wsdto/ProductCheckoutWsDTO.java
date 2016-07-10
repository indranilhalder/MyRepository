/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class ProductCheckoutWsDTO implements java.io.Serializable
{
	private String pincode;
	private ArrayList<ProductsWsDTO> prod;
	private ArrayList<AddWsDTO> address;

	//	public ProductCheckoutWsDTO()
	//	{
	//
	//	}

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
	 * @return the prod
	 */
	public ArrayList<ProductsWsDTO> getProd()
	{
		return prod;
	}

	/**
	 * @param prod
	 *           the prod to set
	 */
	public void setProd(final ArrayList<ProductsWsDTO> prod)
	{
		this.prod = prod;
	}

	/**
	 * @return the address
	 */
	public ArrayList<AddWsDTO> getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *           the address to set
	 */
	public void setAddress(final ArrayList<AddWsDTO> address)
	{
		this.address = address;
	}



}
