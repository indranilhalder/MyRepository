/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 559379
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "type", "inventory" })
public class DeliveryModeResOMSWsDto
{
	@XmlElement(name = "type")
	private String type;

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * @return the inventory
	 */
	public String getInventory()
	{
		return inventory;
	}

	/**
	 * @param inventory
	 *           the inventory to set
	 */
	public void setInventory(final String inventory)
	{
		this.inventory = inventory;
	}

	@XmlElement(name = "inventory")
	private String inventory;
}
