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
{ "ussId", "storeId","price","fulfillmentType","sellerID","transportMode"})
public class StoreLocatorItem
{
	@XmlElement(name = "ussId")
	private String ussId;
	@XmlElement(name = "storeId")
	private List<String> storeId;
	
	@XmlElement(name = "sellerID")
	private String sellerID;
	
	@XmlElement(name = "fulfillmentType")
	private String fulfillmentType;
	
	@XmlElement(name = "transportMode")
	private String transportMode;
	
	@XmlElement(name = "price")
	private double price;

	
	/**
	 * @return the fulfillmentType
	 */
	public String getFulfillmentType()
	{
		return fulfillmentType;
	}

	/**
	 * @param fulfillmentType the fulfillmentType to set
	 */
	public void setFulfillmentType(String fulfillmentType)
	{
		this.fulfillmentType = fulfillmentType;
	}

	/**
	 * @return the transportMode
	 */
	public String getTransportMode()
	{
		return transportMode;
	}

	/**
	 * @param transportMode the transportMode to set
	 */
	public void setTransportMode(String transportMode)
	{
		this.transportMode = transportMode;
	}

	
	/**
	 * @return the sellerID
	 */
	public String getSellerID()
	{
		return sellerID;
	}

	/**
	 * @param sellerID the sellerID to set
	 */
	public void setSellerID(String sellerID)
	{
		this.sellerID = sellerID;
	}

	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}

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
