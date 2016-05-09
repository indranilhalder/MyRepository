/**
 *
 */
package com.tisl.mpl.wsdto;

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
@XmlRootElement(name = "ATS")
@XmlType(propOrder =
{ "storeId", "quantity" })
public class StoreLocatorAtsResponse
{
	@XmlElement(name = "storeId")
	private String storeId;
	@XmlElement(name = "quantity")
	private Integer quantity;

	/**
	 * @return the storeId
	 */
	public String getStoreId()
	{
		return storeId;
	}

	/**
	 * @param storeId
	 *           the storeId to set
	 */
	public void setStoreId(final String storeId)
	{
		this.storeId = storeId;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}
}
