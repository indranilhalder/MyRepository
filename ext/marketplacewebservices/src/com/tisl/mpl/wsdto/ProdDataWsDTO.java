/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author TCS
 *
 */
public class ProdDataWsDTO implements java.io.Serializable
{
	private String code;
	private ArrayList<DeliveryOptWsDTO> delivery;

	//	public ProdDataWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the delivery
	 */
	public ArrayList<DeliveryOptWsDTO> getDelivery()
	{
		return delivery;
	}

	/**
	 * @param delivery
	 *           the delivery to set
	 */
	public void setDelivery(final ArrayList<DeliveryOptWsDTO> delivery)
	{
		this.delivery = delivery;
	}



}
