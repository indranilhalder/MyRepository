package com.tisl.mpl.wsdto;

import java.io.Serializable;
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
@XmlRootElement(name = "PincodeServiceabilityCheck")
@XmlType(propOrder =
{ "pincode", "cartId", "item" })
public class PinCodeDeliveryModeListRequest implements Serializable
{
	@XmlElement(name = "pincode")
	private String pincode;
	@XmlElement(name = "cartId")
	private String cartId;
	@XmlElement(name = "item")
	private List<PinCodeDeliveryModeRequest> item;

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
	 * @return the item
	 */
	public List<PinCodeDeliveryModeRequest> getItem()
	{
		return item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<PinCodeDeliveryModeRequest> item)
	{
		this.item = item;
	}

	/**
	 * @return the cartId
	 */
	public String getCartId()
	{
		return cartId;
	}

	/**
	 * @param cartId
	 *           the cartId to set
	 */
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

}
