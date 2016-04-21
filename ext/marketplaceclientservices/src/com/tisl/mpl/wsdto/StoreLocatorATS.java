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
{ "item" })
public class StoreLocatorATS
{
	@XmlElement(name = "Item")
	private List<StoreLocatorItem> item;

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
