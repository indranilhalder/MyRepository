/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.ArrayList;


/**
 * @author 884206
 *
 */
public class ShippingWsDTO implements java.io.Serializable
{
	private String status;
	private ArrayList<AddWsDTO> adddetails;

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the adddetails
	 */
	public ArrayList<AddWsDTO> getAdddetails()
	{
		return adddetails;
	}

	/**
	 * @param adddetails
	 *           the adddetails to set
	 */
	public void setAdddetails(final ArrayList<AddWsDTO> adddetails)
	{
		this.adddetails = adddetails;
	}


}
