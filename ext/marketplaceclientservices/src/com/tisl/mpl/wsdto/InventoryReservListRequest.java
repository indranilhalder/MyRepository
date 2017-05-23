/**
 *
 */
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
@XmlRootElement(name = "InventoryReservationRequest")
@XmlType(propOrder =
{ "cartId", "pinCode", "duration", "isFreebieCart", "isNewCart", "Item","JewelleryItem" })
public class InventoryReservListRequest
{

	@XmlElement(name = "CartId")
	private String cartId;
	@XmlElement(name = "pinCode")
	private String pinCode;
	@XmlElement(name = "duration")
	private String duration;
	
	@XmlElement(name = "isFreebieCart")
	private Boolean isFreebieCart;
	
	
	@XmlElement(name = "isNewCart")
	private Boolean isNewCart;
	
	@XmlElement(name = "Item")
	private List<InventoryReservRequest> Item;
	
	@XmlElement(name = "JewelleryItem")
	private List<InventoryReservJewelleryRequest> JewelleryItem;

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

	/**
	 * @return the pinCode
	 */

	public String getPinCode()
	{
		return pinCode;
	}

	/**
	 * @param pinCode
	 *           the pinCode to set
	 */
	public void setPinCode(final String pinCode)
	{
		this.pinCode = pinCode;
	}

	/**
	 * @return the duration
	 */

	public String getDuration()
	{
		return duration;
	}

	/**
	 * @param duration
	 *           the duration to set
	 */
	public void setDuration(final String duration)
	{
		this.duration = duration;
	}

	/**
	 * @return the item
	 */

	public List<InventoryReservRequest> getItem()
	{
		return Item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<InventoryReservRequest> item)
	{
		Item = item;
	}

	/**
	 * @return the isFreebieCart
	 */
	public Boolean getIsFreebieCart()
	{
		return isFreebieCart;
	}

	/**
	 * @param isFreebieCart the isFreebieCart to set
	 */
	public void setIsFreebieCart(Boolean isFreebieCart)
	{
		this.isFreebieCart = isFreebieCart;
	}

	/**
	 * @return the isNewCart
	 */
	public Boolean getIsNewCart()
	{
		return isNewCart;
	}

	/**
	 * @param isNewCart the isNewCart to set
	 */
	public void setIsNewCart(Boolean isNewCart)
	{
		this.isNewCart = isNewCart;
	}
	
	/**
	 * @return the Jewelleryitem
	 */

	public List<InventoryReservJewelleryRequest> getJewelleryItem()
	{
		return JewelleryItem;
	}

	/**
	 * @param JewelleryItem
	 *           the item to set
	 */
	public void setJewelleryItem(final List<InventoryReservJewelleryRequest> jewelleryItem)
	{
		JewelleryItem = jewelleryItem;
	}

}