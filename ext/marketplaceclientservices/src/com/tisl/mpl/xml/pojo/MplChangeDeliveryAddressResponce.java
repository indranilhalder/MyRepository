/**
 *
 */
package com.tisl.mpl.xml.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author prasad1
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "OrderID", "Response" })
@XmlRootElement(name = "ChangeDeliveryAddressResponse")
public class MplChangeDeliveryAddressResponce
{

	private String OrderID;
	private String Response;


	/**
	 *
	 * @return Response
	 */
	@XmlElement(name = "Response")
	public String getOrderID()
	{
		return OrderID;
	}

	public void setOrderID(final String orderID)
	{
		OrderID = orderID;
	}

	/**
	 *
	 * @return Response
	 */
	@XmlElement(name = "Response")
	public String getResponse()
	{
		return Response;
	}

	public void setResponse(final String response)
	{
		Response = response;
	}
}
