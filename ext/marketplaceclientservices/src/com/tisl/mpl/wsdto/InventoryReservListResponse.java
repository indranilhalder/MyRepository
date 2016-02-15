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
@XmlRootElement(name = "InventoryReservationResponse")
@XmlType(propOrder =
{ "Item" })
public class InventoryReservListResponse
{
	@XmlElement(name = "Item")
	private List<InventoryReservResponse> Item;

	/**
	 * @return the item
	 */
	public List<InventoryReservResponse> getItem()
	{
		return Item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<InventoryReservResponse> item)
	{
		Item = item;
	}
}
