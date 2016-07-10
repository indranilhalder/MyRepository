/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;




/**
 * @author TCS
 *
 */

public class Pin implements java.io.Serializable
{
	private String pinCode;
	private String shipmentMode;
	private String deliveryMode;
	private String sellerID;
	private String primaryCatID;
	private String listingID;
	private String uSSID;
	private String codRestricted;
	private String prepaidRestricted;


	/**
	 * @return the pinCode
	 */
	@XmlElement(name = "PinCode")
	public String getPinCode()
	{
		return pinCode;
	}

	/**
	 * @param pinCode
	 *           the pinCode to set
	 */
	public void setPinCode(final String pinCode)
	{
		this.pinCode = pinCode;
	}

	/**
	 * @return the shipmentMode
	 */
	@XmlElement(name = "ShipmentMode")
	public String getShipmentMode()
	{
		return shipmentMode;
	}



	/**
	 * @param shipmentMode
	 *           the shipmentMode to set
	 */
	public void setShipmentMode(final String shipmentMode)
	{
		this.shipmentMode = shipmentMode;
	}

	/**
	 * @return the deliveryMode
	 */
	@XmlElement(name = "DeliveryMode")
	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	/**
	 * @return the sellerID
	 */
	@XmlElement(name = "SellerID")
	public String getSellerID()
	{
		return sellerID;
	}

	/**
	 * @param sellerID
	 *           the sellerID to set
	 */
	public void setSellerID(final String sellerID)
	{
		this.sellerID = sellerID;
	}

	/**
	 * @return the primaryCatID
	 */
	@XmlElement(name = "PrimaryCatID")
	public String getPrimaryCatID()
	{
		return primaryCatID;
	}

	/**
	 * @param primaryCatID
	 *           the primaryCatID to set
	 */
	public void setPrimaryCatID(final String primaryCatID)
	{
		this.primaryCatID = primaryCatID;
	}

	/**
	 * @return the listingID
	 */
	@XmlElement(name = "ListingID")
	public String getListingID()
	{
		return listingID;
	}

	/**
	 * @param listingID
	 *           the listingID to set
	 */
	public void setListingID(final String listingID)
	{
		this.listingID = listingID;
	}

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
	public void setUSSID(final String uSSID)
	{
		this.uSSID = uSSID;
	}

	/**
	 * @return the codRestricted
	 */
	@XmlElement(name = "CodRestricted")
	public String getCodRestricted()
	{
		return codRestricted;
	}

	/**
	 * @param codRestricted
	 *           the codRestricted to set
	 */
	public void setCodRestricted(final String codRestricted)
	{
		this.codRestricted = codRestricted;
	}

	/**
	 * @return the prepaidRestricted
	 */
	@XmlElement(name = "PrepaidRestricted")
	public String getPrepaidRestricted()
	{
		return prepaidRestricted;
	}

	/**
	 * @param prepaidRestricted
	 *           the prepaidRestricted to set
	 */
	public void setPrepaidRestricted(final String prepaidRestricted)
	{
		this.prepaidRestricted = prepaidRestricted;
	}




}
