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
 * @author Techouts
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "StoreATS")
@XmlType(propOrder =
{ "cartId", "item" })
public class StoreLocatorATS
{
	@XmlElement(name = "cartId")
	private String cartId;
	@XmlElement(name = "Item")
	private List<StoreLocatorItem> item;
	
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
	 * @return the item
	 */
	public List<StoreLocatorItem> getItem()
	{
		return item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<StoreLocatorItem> item)
	{
		this.item = item;
	}

}
