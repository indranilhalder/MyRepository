/**
 * 
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author TECHOUTS
 *
 */
@XmlRootElement(name="TicketUpdateResponse")
@XmlType(propOrder =
{ "success" })
public class TicketUpdateResponseXML
{

	private String  success;

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
