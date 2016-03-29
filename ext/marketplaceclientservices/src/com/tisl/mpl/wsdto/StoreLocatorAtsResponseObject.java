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
@XmlRootElement(name = "StoreATSResponse")
@XmlType(propOrder =
{ "Item" })
public class StoreLocatorAtsResponseObject
{
	@XmlElement(name = "Item")
	private List<StoreLocatorResponseItem> Item;

	/**
	 * @return the item
	 */
	public List<StoreLocatorResponseItem> getItem()
	{
		return Item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<StoreLocatorResponseItem> item)
	{
		Item = item;
	}
}
