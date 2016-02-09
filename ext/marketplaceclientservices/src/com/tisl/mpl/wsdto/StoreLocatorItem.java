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
@XmlRootElement(name = "Item")
@XmlType(propOrder =
{ "ussId", "storeId" })
public class StoreLocatorItem
{
	@XmlElement(name = "ussId")
	private String ussId;
	@XmlElement(name = "storeId")
	private List<String> storeId;

	/**
	 * @return the ussId
	 */
	public String getUssId()
	{
		return ussId;
	}

	/**
	 * @param ussId
	 *           the ussId to set
	 */
	public void setUssId(final String ussId)
	{
		this.ussId = ussId;
	}

	/**
	 * @return the storeId
	 */
	public List<String> getStoreId()
	{
		return storeId;
	}

	/**
	 * @param storeId
	 *           the storeId to set
	 */
	public void setStoreId(final List<String> storeId)
	{
		this.storeId = storeId;
	}
}
