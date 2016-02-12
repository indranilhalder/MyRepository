package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PincodeServiceability")
@XmlType(propOrder =
{ "Pincode", "Item" })
public class PinCodeDeliveryModeListResponse
{
	@XmlElement(name = "Pincode")
	private String Pincode;
	@XmlElement(name = "Item")
	private List<PinCodeDeliveryModeResponse> Item;

	//	public PinCodeDeliveryModeListResponse()
	//	{
	//
	//	}


	public static enum Status
	{
		SUCCESS, FAILED, ERROR;
	}


	/**
	 * @return the pincode
	 */
	public String getPincode()
	{
		return Pincode;
	}


	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		Pincode = pincode;
	}


	/**
	 * @return the item
	 */
	public List<PinCodeDeliveryModeResponse> getItem()
	{
		return Item;
	}


	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<PinCodeDeliveryModeResponse> item)
	{
		Item = item;
	}
}
