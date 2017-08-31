/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "ListingID", "Item" })
public class InventoryReservJewelleryRequest
{
	@XmlElement(name = "listingID")
	private String ListingID;

	@XmlElement(name = "Item")
	private List<InventoryReservRequest> Item;

	/**
	 *
	 * @return the cartId
	 */

	public String getListingID()
	{
		return ListingID;
	}

	/**
	 * @param cartId
	 *           the cartId to set
	 */
	public void setListingID(final String ListingID)
	{
		this.ListingID = ListingID;
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


}
