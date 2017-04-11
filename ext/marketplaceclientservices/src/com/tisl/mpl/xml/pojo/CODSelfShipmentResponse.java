/**
 * 
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Techouts
 *
 */
@XmlRootElement(name = "CODSelfShip")
@XmlType(propOrder =
{ "success"})
public class CODSelfShipmentResponse
{

	private String success;

	/**
	 * @return the success
	 */
	public String getSuccess()
	{
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success)
	{
		this.success = success;
	}
}
