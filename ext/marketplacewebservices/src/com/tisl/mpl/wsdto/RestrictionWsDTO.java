/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */

/*
 * @XmlRootElement(name = "Pin")
 */@XmlType(propOrder =
{ "pincode", "shipmentMode", "deliveryMode", "sellerID", "primaryCatID", "listingID", "ussid", "codRestricted",
		"prepaidRestricted" })
public class RestrictionWsDTO implements java.io.Serializable
{
	private String pincode;
	private String shipmentMode;
	private String deliveryMode;
	private String sellerID;
	private String primaryCatID;
	private String listingID;
	private String ussid;
	private String codRestricted;
	private String prepaidRestricted;

	/*
	 * public RestrictionWsDTO() {
	 *
	 * }
	 */

	/**
	 * @return the pincode
	 */
	@XmlElement(name = "pincode")
	public String getPincode()
	{
		return pincode;
	}

	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		this.pincode = pincode;
	}

	/**
	 * @return the shipmentMode
	 */
	@XmlElement(name = "shipmentMode")
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
	@XmlElement(name = "deliveryMode")
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
	@XmlElement(name = "sellerID")
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
	@XmlElement(name = "primaryCatID")
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
	@XmlElement(name = "listingID")
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

	/**
	 * @return the codRestricted
	 */
	@XmlElement(name = "codRestricted")
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
	@XmlElement(name = "prepaidRestricted")
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

	/**
	 * @return the ussid
	 */
	@XmlElement(name = "ussid")
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
	}


}
