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
		"deliveryDate", "store" })
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
	@XmlElement(name = "FulfilmentType")
	private String fulfilmentType;
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
	public String getFulfilmentType()
	{
		return fulfilmentType;
	}

	/**
	 * @param fulfilmentType
	 *           the fulfilmentType to set
	 */
	public void setFulfilmentType(final String _fulfilmentType)
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



}
