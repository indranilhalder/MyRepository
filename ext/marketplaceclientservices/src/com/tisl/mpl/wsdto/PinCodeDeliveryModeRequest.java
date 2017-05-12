package com.tisl.mpl.wsdto;

import java.io.Serializable;
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
@XmlType(name = "item", propOrder =
{ "uSSID", "sellerID", "price", "isCOD", "fulfilmentType", "transportMode", "isDeliveryDateRequired", "deliveryMode",
		"deliveryDate", "store", "deliveryFulfillMode", "deliveryFulfillModeByP1", "isFragile", "isPrecious" })
public class PinCodeDeliveryModeRequest implements Serializable
{
	@XmlElement(name = "USSID")
	private String uSSID;
	@XmlElement(name = "SellerID")
	private String sellerID;
	@XmlElement(name = "Price")
	private double price;
	@XmlElement(name = "isCOD")
	private String isCOD;
	@XmlElement(name = "FulfilmentType")//private String fulfilmentType;
	private List<String> fulfilmentType;
	@XmlElement(name = "TransportMode")
	private String transportMode;
	@XmlElement(name = "isDeliveryDateRequired")
	private String isDeliveryDateRequired;
	@XmlElement(name = "DeliveryMode")
	private List<String> deliveryMode;

	@XmlElement(name = "deliveryDate")
	private String deliveryDate;

	@XmlElement(name = "Store")
	private List<String> store;


	@XmlElement(name = "deliveryFulfillMode")
	private String deliveryFulfillMode;

	@XmlElement(name = "deliveryFulfillModeByP1")
	private String deliveryFulfillModeByP1;

	@XmlElement(name = "isFragile")
	private String isFragile;

	@XmlElement(name = "isPrecious")
	private String isPrecious;

	/**
	 * @return the uSSID
	 */
	public String getUSSID()
	{
		return uSSID;
	}

	/**
	 * @param uSSID
	 *           the uSSID to set
	 */
	public void setUSSID(final String _uSSID)
	{
		uSSID = _uSSID;
	}

	/**
	 * @return the sellerID
	 */
	public String getSellerID()
	{
		return sellerID;
	}

	/**
	 * @param sellerID
	 *           the sellerID to set
	 */
	public void setSellerID(final String _sellerID)
	{
		sellerID = _sellerID;
	}

	/**
	 * @return the price
	 */
	public double getPrice()
	{
		return price;
	}

	/**
	 * @param price
	 *           the price to set
	 */
	public void setPrice(final double _price)
	{
		price = _price;
	}

	/**
	 * @return the isCOD
	 */
	public String getIsCOD()
	{
		return isCOD;
	}

	/**
	 * @param isCOD
	 *           the isCOD to set
	 */
	public void setIsCOD(final String isCOD)
	{
		this.isCOD = isCOD;
	}

	/**
	 * @return the fulfilmentType
	 */
	public List<String> getFulfilmentType()
	{
		return fulfilmentType;
	}

	/**
	 * @param fulfilmentType
	 *           the fulfilmentType to set
	 */
	public void setFulfilmentType(final List<String> _fulfilmentType)
	{
		fulfilmentType = _fulfilmentType;
	}

	/**
	 * @return the transportMode
	 */
	public String getTransportMode()
	{
		return transportMode;
	}

	/**
	 * @param transportMode
	 *           the transportMode to set
	 */
	public void setTransportMode(final String _transportMode)
	{
		transportMode = _transportMode;
	}

	/**
	 * @return the isDeliveryDateRequired
	 */
	public String getIsDeliveryDateRequired()
	{
		return isDeliveryDateRequired;
	}

	/**
	 * @param isDeliveryDateRequired
	 *           the isDeliveryDateRequired to set
	 */
	public void setIsDeliveryDateRequired(final String isDeliveryDateRequired)
	{
		this.isDeliveryDateRequired = isDeliveryDateRequired;
	}

	/**
	 * @return the deliveryMode
	 */
	public List<String> getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final List<String> _deliveryMode)
	{
		deliveryMode = _deliveryMode;
	}


	/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate()
	{
		return deliveryDate;
	}

	/**
	 * @param deliveryDate
	 *           the deliveryDate to set
	 */
	public void setDeliveryDate(final String deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the store
	 */
	public List<String> getStore()
	{
		return store;
	}

	/**
	 * @param store
	 *           the store to set
	 */
	public void setStore(final List<String> store)
	{
		this.store = store;
	}

	/**
	 * @return the deliveryFulfillMode
	 */
	public String getDeliveryFulfillMode()
	{
		return deliveryFulfillMode;
	}

	/**
	 * @param deliveryFulfillMode
	 *           the deliveryFulfillMode to set
	 */
	public void setDeliveryFulfillMode(final String deliveryFulfillMode)
	{
		this.deliveryFulfillMode = deliveryFulfillMode;
	}

	/**
	 * @return the deliveryFulfillModeByP1
	 */
	public String getDeliveryFulfillModeByP1()
	{
		return deliveryFulfillModeByP1;
	}

	/**
	 * @param deliveryFulfillModeByP1
	 *           the deliveryFulfillModeByP1 to set
	 */
	public void setDeliveryFulfillModeByP1(final String deliveryFulfillModeByP1)
	{
		this.deliveryFulfillModeByP1 = deliveryFulfillModeByP1;
	}

	/**
	 * @return the isFragile
	 */
	public String getIsFragile()
	{
		return isFragile;
	}

	/**
	 * @param isFragile
	 *           the isFragile to set
	 */
	public void setIsFragile(final String isFragile)
	{
		this.isFragile = isFragile;
	}

	/**
	 * @return the isPrecious
	 */
	public String getIsPrecious()
	{
		return isPrecious;
	}

	/**
	 * @param isPrecious
	 *           the isPrecious to set
	 */
	public void setIsPrecious(final String isPrecious)
	{
		this.isPrecious = isPrecious;
	}

}
